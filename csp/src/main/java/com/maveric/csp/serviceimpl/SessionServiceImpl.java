package com.maveric.csp.serviceimpl;

import com.maveric.csp.dto.request.SessionRequest;
import com.maveric.csp.dto.request.UpdateSessionRequest;
import com.maveric.csp.dto.response.SessionDeleteResponse;
import com.maveric.csp.dto.response.SessionListResponse;
import com.maveric.csp.dto.response.SessionResponse;
import com.maveric.csp.entity.Customer;
import com.maveric.csp.entity.Session;
import com.maveric.csp.entity.SessionHistory;
import com.maveric.csp.exceptions.*;
import com.maveric.csp.repository.CustomerRepository;
import com.maveric.csp.repository.SessionHistoryRepository;
import com.maveric.csp.repository.SessionRepository;
import com.maveric.csp.service.SessionService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.maveric.csp.constants.Constants.*;

@Service
public class SessionServiceImpl implements SessionService {
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private SessionHistoryRepository sessionHistoryRepository;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Creates a new session based on the provided session request.
     *
     * @param sessionRequest The session request containing information for creating the session.
     * @return A SessionResponse object containing details of the created session.
     * @throws UserNotFoundException If the customer specified in the session request is not found.
     */
    @Override
    public SessionResponse createSession(SessionRequest sessionRequest) {
        Optional<Customer> customerExisted = customerRepository.findById(sessionRequest.getCustomerId());
        if (customerExisted.isPresent()) {
            //Converting DTo to entity
            Session session = mapper.map(sessionRequest, Session.class);
            session.setCustomer(customerExisted.get());

            Session savedSession = sessionRepository.save(session);
            //converting entity to Dto
            SessionResponse savedResponse = mapper.map(savedSession, SessionResponse.class);
            savedResponse.setArchiveFlag(ARCHIVE_FLAG_NO);
            savedResponse.setCustomerName(customerExisted.get().getCustomerName());
            return savedResponse;
        } else {
            throw new UserNotFoundException();
        }

    }

    /**
     * Deletes a session based on the provided session ID.
     *
     * @param id The ID of the session to be deleted.
     * @return The Session object representing the deleted session.
     * @throws SessionNotFoundException If the session with the specified ID is not found.
     * @throws SessionAlreadyDeleted If the session has already been marked as deleted.
     */
    @Override
    @Transactional
    public SessionDeleteResponse deleteSessionById(String id) {
        Optional<Session> sessionById = sessionRepository.findBySessionId(id);
        if (sessionById.isPresent()) {
            Session session = sessionById.get();
            if (session.getStatus() == DELETE_STATUS) {
                throw new SessionAlreadyDeleted();
            }
            session.setStatus(DELETE_STATUS);
            sessionRepository.save(session);
            creatingSessionHistory(session);
            return new SessionDeleteResponse(SESSION_DELETED, HttpStatus.OK);
        } else {
            throw new SessionNotFoundException();
        }

    }
    /**
     * Creates a session history entry based on the provided session.
     *
     * @param session The Session object for which to create a session history entry.
     */
    private void creatingSessionHistory(Session session) {
        SessionHistory sessionHistory = new SessionHistory();
        sessionHistory.setSessionId(session.getSessionId());
        sessionHistory.setSessionName(session.getSessionName());
        sessionHistory.setRemarks(session.getRemarks());
        sessionHistory.setStatus(DELETE_STATUS);
        sessionHistory.setCreatedOn(session.getCreatedOn());
        sessionHistory.setCreatedBy(session.getCreatedBy());
        sessionHistory.setUpdatedOn(session.getUpdatedOn());
        sessionHistory.setCustomerId(session.getCustomer().getId());
        sessionHistory.setDeletedOn(new Date());
        sessionHistoryRepository.save(sessionHistory);
    }
    /**
     * Creates an archive flag for a SessionResponse based on the difference between the current date and the update date.
     *
     * @param sessionResponse The SessionResponse object for which to create the archive flag.
     * @throws ParseException If there is an issue parsing date information.
     */
    private void createArchiveFlag(SessionResponse sessionResponse) throws ParseException {
        if (diffInDays(new Date(), sessionResponse.getUpdatedOn())) {
            sessionResponse.setArchiveFlag(ARCHIVE_FLAG_YES);
        } else {
            sessionResponse.setArchiveFlag(ARCHIVE_FLAG_NO);
        }
    }
    /**
     * Updates a session to archived status based on the provided session ID.
     *
     * @param id The ID of the session to be updated to archived status.
     * @return A SessionResponse object representing the updated archived session.
     * @throws ParseException If there is an issue parsing date information.
     * @throws ArchivedSessionException If the session cannot be archived due to update frequency.
     * @throws SessionNotFoundException If the session with the specified ID is not found.
     */




    @Override
    public SessionResponse updateToArchived(String id) throws ParseException {
        Optional<Session> sessionExistsWithId = sessionRepository.findBySessionId(id);
        if (sessionExistsWithId.isPresent()) {
            Session existed = sessionExistsWithId.get();
            Date date1 = new Date();
            if (existed.getUpdatedOn() != null && diffInDays(date1, existed.getUpdatedOn())) {
                existed.setUpdatedOn(new Date());
                existed.setStatus(ARCHIVE_STATUS);
                Session archivedSession = sessionRepository.save(existed);
                SessionResponse sessionResponse= mapper.map(archivedSession, SessionResponse.class);
                sessionResponse.setArchiveFlag(ARCHIVE_FLAG_NO);
                return sessionResponse;
            } else {
                throw new ArchivedSessionException();
            }
        } else {
            throw new SessionNotFoundException();
        }
    }




    /**
     * Calculates the difference in days between two dates and checks if the difference is greater than a specified threshold.
     * @param currentDate The current date for comparison.
     * @param updatedOn The date that needs to be compared against the current date.
     * @return True if the difference in days between the two dates is greater than the threshold, false otherwise.
     * @throws ParseException If there is an issue parsing date information.
     */

    public boolean diffInDays(Date currentDate, Date updatedOn) throws ParseException {
        if (currentDate == null || updatedOn == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        Date existedUpdatedOn = sdf.parse(String.valueOf(updatedOn));
        long diffFromUpdated = Math.abs(currentDate.getTime() - existedUpdatedOn.getTime());
        long diffFromUpdatedDate = TimeUnit.DAYS.convert(diffFromUpdated, TimeUnit.MILLISECONDS);
        return diffFromUpdatedDate > MAXIMUM_DORMANT_DAYS;
    }

    /**
     * Updates a session based on the provided session ID and session request.
     *
     * @param id The ID of the session to be updated.
     * @param sessionRequest The session request containing updated information for the session.
     * @return A SessionResponse object representing the updated session.
     * @throws SessionNotFoundException If the session with the specified ID is not found.
     * @throws UserNotFoundException If the user specified in the session request is not found.
     */
    @Override
    public SessionResponse udateSessionById(String id, UpdateSessionRequest sessionRequest) {
        Session existingSession = sessionRepository.findBySessionId(id)
                .orElseThrow(SessionNotFoundException::new);
        existingSession.setSessionName(sessionRequest.getSessionName());
        existingSession.setRemarks(sessionRequest.getRemarks());
        existingSession.setUpdatedOn(new Date());
        sessionRepository.save(existingSession);
        SessionResponse sessionResponse=mapper.map(existingSession, SessionResponse.class);
        sessionResponse.setArchiveFlag(ARCHIVE_FLAG_NO);
        return sessionResponse;
    }


    /**
     * Retrieves a paginated list of session responses with a specific status.
     *
     * @param status The status character to filter sessions by.
     * @param page The page number (0-based) for pagination.
     * @param pageSize The number of items per page.
     * @return A list of SessionResponse objects representing sessions with the specified status in the specified page.
     * @throws ParseException If there is an issue parsing date information.
     * @throws StatusNotFoundException If no sessions with the specified status are found.
     * @throws SessionNotFoundException If the provided page or pageSize is invalid.
     */
    @Override
    public SessionListResponse findWithStatus(char status, int page, int pageSize) throws ParseException {
        Page<Session> sessions = sessionRepository.findByStatus(status, PageRequest.of(page, pageSize));
        List<SessionResponse> sessionResponses = new ArrayList<>();
        if (sessions.isEmpty()) {
            throw new StatusNotFoundException();
        }
        if (page < DEFAULT_SIZE || pageSize < DEFAULT_SIZE) {
            throw new SessionNotFoundException();
        }
        for (Session session : sessions) {
            SessionResponse sessionResponse = mapper.map(session, SessionResponse.class);
            createArchiveFlag(sessionResponse);
            sessionResponses.add(sessionResponse);
        }
        long totalElements = sessions.getTotalElements();
        return new SessionListResponse(sessionResponses, totalElements);
    }

}




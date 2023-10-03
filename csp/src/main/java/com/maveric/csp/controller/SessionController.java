package com.maveric.csp.controller;

import com.maveric.csp.dto.request.SessionRequest;
import com.maveric.csp.dto.request.UpdateSessionRequest;
import com.maveric.csp.dto.response.SessionDeleteResponse;
import com.maveric.csp.dto.response.SessionListResponse;
import com.maveric.csp.dto.response.SessionResponse;
import com.maveric.csp.entity.Session;
import com.maveric.csp.repository.CustomerRepository;
import com.maveric.csp.repository.SessionHistoryRepository;
import com.maveric.csp.repository.SessionRepository;
import com.maveric.csp.serviceimpl.SessionServiceImpl;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

import static com.maveric.csp.constants.Constants.*;

@RestController
@Validated
@RequestMapping(BASE_SESSION_URL)
public class SessionController {

        @Autowired
    private SessionServiceImpl sessionService;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private SessionHistoryRepository sessionHistoryRepository;
    @Autowired
    private CustomerRepository customerRepository;


        /**
     * Handle HTTP POST requests to create a new session.
     *
     * @param sessionRequest The session details provided in the request body.
     * @return A ResponseEntity containing the created session's information in the response body.
     * @throws ParseException If there's an issue parsing date-related information.
     */
    @PostMapping
    public ResponseEntity<SessionResponse> createSession(@RequestBody @Valid SessionRequest sessionRequest) throws ParseException {
        SessionResponse createdSession = sessionService.createSession(sessionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSession);
    }


    /**
     * Update session details by ID.
     *
     * @param id The ID of the session to update.
     * @param sessionRequest The updated session details provided in the request body.
     * @return A ResponseEntity containing the updated session details in the response body.
     */
    @PutMapping(SESSION_URL_ID)
    public ResponseEntity<SessionResponse> uodateSessionById(@PathVariable String id, @RequestBody UpdateSessionRequest sessionRequest) {
            return new ResponseEntity<>(sessionService.udateSessionById(id,sessionRequest),HttpStatus.CREATED);
        }

    /**
     * Retrieve sessions by status and creator.
     * @param status The status of sessions to retrieve.
     * @param page The page number for pagination.
     * @param pageSize The page size for pagination.
     * @return A ResponseEntity containing a list of sessions matching the status and creator in the response body.
     */
    @GetMapping(SESSION_STATUS_URL)
    public ResponseEntity<SessionListResponse> findByStatus(@PathVariable char status, @RequestParam(defaultValue =DEFAULT_PAGE_VALUE) int page,
                                                            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE_VALUE) int pageSize ) throws ParseException {

        SessionListResponse sessionswithStatus= sessionService.findWithStatus(status,page,pageSize);
        return ResponseEntity.ok(sessionswithStatus);
    }
    /**
     * Update a session's status to archived by ID.
     *
     * @param id The ID of the session to update.
     * @return A ResponseEntity containing the archived session details in the response body.
     */
    @PatchMapping(SESSION_ARCHIVE_URL)
        public ResponseEntity<SessionResponse> updateToArchive(@PathVariable String id) throws ParseException {
        SessionResponse sessionResponse = sessionService.updateToArchived(id);
        return new ResponseEntity<>(sessionResponse, HttpStatus.OK);
    }
    /**
     * Delete a session by ID.
     *
     * @param id The ID of the session to delete.
     * @return A success message indicating that the session history was deleted successfully.
     */
    @DeleteMapping(SESSION_URL_ID)
    public ResponseEntity<SessionDeleteResponse> deleteUserById(@PathVariable String id) {

        return ResponseEntity.ok().body(sessionService.deleteSessionById(id));
    }
}
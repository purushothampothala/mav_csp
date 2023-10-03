package com.maveric.csp.serviceimpl;

import com.maveric.csp.dto.request.SessionRequest;
import com.maveric.csp.dto.request.UpdateSessionRequest;
import com.maveric.csp.dto.response.SessionDeleteResponse;
import com.maveric.csp.dto.response.SessionListResponse;
import com.maveric.csp.dto.response.SessionResponse;
import com.maveric.csp.entity.Customer;
import com.maveric.csp.entity.Session;
import com.maveric.csp.entity.SessionHistory;
import com.maveric.csp.exceptions.ArchivedSessionException;
import com.maveric.csp.exceptions.SessionNotFoundException;
import com.maveric.csp.exceptions.StatusNotFoundException;
import com.maveric.csp.exceptions.UserNotFoundException;
import com.maveric.csp.repository.CustomerRepository;
import com.maveric.csp.repository.SessionHistoryRepository;
import com.maveric.csp.repository.SessionRepository;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import java.text.ParseException;
import java.util.*;

import static com.maveric.csp.constants.Constants.SESSION_DELETED;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.anyChar;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



@SpringBootTest
class SessionServiceImplTest {

    private static final String EXISTING_SESSION_ID = "existingSessionId";
    private static final String NON_EXISTING_SESSION_ID = "nonExistingSessionId";
    private static final char DELETE_STATUS = 'D';

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private SessionServiceImpl sessionServiceImpl;

    @InjectMocks
    private SessionServiceImpl sessionService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private SessionHistoryRepository sessionHistoryRepository;
    @Mock
    private ModelMapper mapper;


    @BeforeEach
    public void setUp() throws Exception {
        mock(CustomerRepository.class);
        MockitoAnnotations.openMocks(this);

    }


    @Test
    void testCreateSession_Successful() throws ParseException {
        Customer existingCustomer = new Customer();
        existingCustomer.setId(1L);
        SessionRequest sessionRequest = new SessionRequest();
        sessionRequest.setCustomerId(1L);
        Session session = new Session();
        session.setSessionId("SESSION_01");
        SessionResponse expectedResponse = new SessionResponse();
        expectedResponse.setSessionId("SESSION_52");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        when(mapper.map(sessionRequest, Session.class)).thenReturn(session);
        when(sessionRepository.save(session)).thenReturn(session);
        when(mapper.map(session, SessionResponse.class)).thenReturn(expectedResponse);
        SessionResponse actualResponse = sessionService.createSession(sessionRequest);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testCreateSession_CustomerNotFound() {
        SessionRequest sessionRequest = new SessionRequest();
        sessionRequest.setCustomerId(1L);
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> sessionService.createSession(sessionRequest));
    }




    @Test
    void testUpdateSessionById_Success() {
        UpdateSessionRequest request = new UpdateSessionRequest();
        request.setSessionName("NewSession");
        request.setRemarks(" remarks");
        Session existingSession = new Session();
        existingSession.setSessionId("SESSION_52");
        existingSession.setSessionName("Session");
        existingSession.setRemarks("No-remarks");
        existingSession.setCreatedBy("Purushotham Pothala");
        existingSession.setUpdatedOn(new Date());
        when(sessionRepository.findBySessionId("SESSION_52")).thenReturn(Optional.of(existingSession));
        SessionResponse mappedResponse = new SessionResponse(); // You need to create a SessionResponse object here
        when(mapper.map(any(Session.class), eq(SessionResponse.class))).thenReturn(mappedResponse);
        SessionResponse result = sessionService.udateSessionById("SESSION_52", request);

        // Assertions
        verify(sessionRepository).save(existingSession);
        assertEquals(request.getSessionName(), existingSession.getSessionName());
        assertEquals(request.getRemarks(), existingSession.getRemarks());
        assertNotNull(existingSession.getUpdatedOn());
        assertEquals(mappedResponse, result);
    }

    @Test
    void testUpdateSessionById_SessionNotFound() {
        Mockito.when(sessionRepository.findBySessionId(Mockito.anyString())).thenReturn(Optional.empty());
        assertThrows(SessionNotFoundException.class, () -> {
            sessionService.udateSessionById("SESSION_01", new UpdateSessionRequest());
        });
    }


    @Test
    public void testFindWithStatus_Successful() throws ParseException {
        char status = 'A';
        int page = 0;
        int pageSize = 10;
        List<Session> sessions = new ArrayList<>();
        Session session1 = new Session();
        sessions.add(session1);
        Page<Session> sessionPage = new PageImpl<>(sessions);
        when(sessionRepository.findByStatus(eq(status), any(PageRequest.class)))
                .thenReturn(sessionPage);
        when(mapper.map(eq(session1), eq(SessionResponse.class))).thenReturn(new SessionResponse());
        SessionListResponse result = sessionService.findWithStatus(status, page, pageSize);
        assertNotNull(result);
        assertEquals(sessions.size(), result.getContent().size());
        verify(sessionRepository).findByStatus(eq(status), any(PageRequest.class));
        verify(mapper, times(sessions.size())).map(any(Session.class), eq(SessionResponse.class));
    }

    @Test
    void testFindWithStatus_StatusNotFound() throws ParseException {
        char status = 'A';
        int page = 0;
        int pageSize = 10;
        when(sessionRepository.findByStatus(eq(status), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));
        assertThrows(StatusNotFoundException.class,
                () -> sessionService.findWithStatus(status, page, pageSize));
        verify(sessionRepository).findByStatus(eq(status), any(PageRequest.class));
        verify(mapper, never()).map(any(Session.class), eq(SessionResponse.class));
    }

    @Test
    void testFindWithStatus_NegativePageAndPageSize() {
        char status = 'A';
        int page = -1; // Negative page
        int pageSize = -10; // Negative pageSize
        assertThrows(IllegalArgumentException.class, () -> {
            sessionService.findWithStatus(status, page, pageSize);
        });
        verifyNoInteractions(sessionRepository);
    }


    @Test
    void testUpdateToArchived() throws ParseException {
        Customer customer = new Customer();
        customer.setCustomerName("Customer Name");
        customer.setId(1L);
        customer.setSessions(new ArrayList<>());
        Session session = mock(Session.class);
        when(session.getUpdatedOn()).thenReturn(null);
        doNothing().when(session).setCreatedBy(Mockito.<String>any());
        doNothing().when(session).setCreatedOn(Mockito.<Date>any());
        doNothing().when(session).setCustomer(Mockito.<Customer>any());
        doNothing().when(session).setRemarks(Mockito.<String>any());
        doNothing().when(session).setSessionId(Mockito.<String>any());
        doNothing().when(session).setSessionName(Mockito.<String>any());
        doNothing().when(session).setStatus(anyChar());
        doNothing().when(session).setUpdatedOn(Mockito.<Date>any());
        session.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        session.setCreatedOn(Date.from(LocalDate.of(2020, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        session.setCustomer(customer);
        session.setRemarks("Remarks");
        session.setSessionId("SESSION_1");
        session.setSessionName("Session Name");
        session.setStatus('A');
        session.setUpdatedOn(Date.from(LocalDate.of(2020, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
        Optional<Session> ofResult = Optional.of(session);
        when(sessionRepository.findBySessionId(Mockito.<String>any())).thenReturn(ofResult);
        assertThrows(ArchivedSessionException.class, () -> sessionService.updateToArchived("SESSION_1"));
        verify(sessionRepository).findBySessionId(Mockito.<String>any());
        verify(session).getUpdatedOn();
        verify(session).setCreatedBy(Mockito.<String>any());
        verify(session).setCreatedOn(Mockito.<Date>any());
        verify(session).setCustomer(Mockito.<Customer>any());
        verify(session).setRemarks(Mockito.<String>any());
        verify(session).setSessionId(Mockito.<String>any());
        verify(session).setSessionName(Mockito.<String>any());
        verify(session).setStatus(anyChar());
        verify(session).setUpdatedOn(Mockito.<Date>any());
    }

    @Test
    void testUpdateToArchived_SessionNotFound() {
        String sessionId = "SESSION_01";
        when(sessionRepository.findBySessionId(sessionId)).thenReturn(Optional.empty());
        assertThrows(SessionNotFoundException.class, () -> {
            sessionService.updateToArchived(sessionId);
        });
        verify(sessionRepository).findBySessionId(sessionId);
        verify(sessionRepository, never()).save(any(Session.class));
        verify(mapper, never()).map(any(Session.class), eq(SessionResponse.class));
    }



    @Test
    void testDeleteSessionById_Successful() {
        // Arrange
        Session existingSession = new Session(EXISTING_SESSION_ID, "sessionName", "purushotham", null, null, 'A', "No remarks", new Customer(1L,"Purushotham"));
        when(sessionRepository.findBySessionId(EXISTING_SESSION_ID)).thenReturn(Optional.of(existingSession));

        // Act
        SessionDeleteResponse response = sessionService.deleteSessionById(EXISTING_SESSION_ID);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(SESSION_DELETED, response.getMessage());

        // Verify that the session status is updated and saved
        verify(sessionRepository, times(1)).save(existingSession);
        assertEquals(DELETE_STATUS, existingSession.getStatus());

              }


    @Test
    void testDeleteSessionById_SessionNotFound() {
        when(sessionRepository.findBySessionId(NON_EXISTING_SESSION_ID)).thenReturn(Optional.empty());
        assertThrows(SessionNotFoundException.class, () -> sessionService.deleteSessionById(NON_EXISTING_SESSION_ID));
        verify(sessionRepository, never()).save(any());
    }


}

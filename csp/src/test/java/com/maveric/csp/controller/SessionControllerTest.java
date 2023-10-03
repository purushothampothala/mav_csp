package com.maveric.csp.controller;

import com.maveric.csp.dto.response.SessionListResponse;
import com.maveric.csp.dto.response.SessionResponse;
import com.maveric.csp.dto.request.UpdateSessionRequest;
import com.maveric.csp.entity.Customer;
import com.maveric.csp.entity.Session;
import com.maveric.csp.exceptions.StatusNotFoundException;
import com.maveric.csp.serviceimpl.SessionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.text.ParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
class SessionControllerTest {
    Date date = new Date();
    Customer customer = new Customer();
    @Mock
    private SessionServiceImpl sessionService;
    @Mock
    private ModelMapper mapper;
    @InjectMocks
    private SessionController sessionController;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testUpdateSessionByIdValidRequest() {

        String sessionId = "SESSION_1";
        UpdateSessionRequest sessionRequest = new UpdateSessionRequest();


        SessionResponse updatedSession = new SessionResponse();

        when(sessionService.udateSessionById(sessionId, sessionRequest)).thenReturn(updatedSession);

        ResponseEntity<SessionResponse> responseEntity = sessionController.uodateSessionById(sessionId, sessionRequest);

        verify(sessionService, times(1)).udateSessionById(sessionId, sessionRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(updatedSession, responseEntity.getBody());
    }

    @Test
    void testFindByStatus_Successful() throws ParseException {
        char status = 'A';
        String createdBy = "USER123";
        int page = 0;
        int pageSize = 10;

        SessionListResponse mockResponse = new SessionListResponse();
        // Set mockResponse properties as needed

        when(sessionService.findWithStatus(status, page, pageSize))
                .thenReturn(mockResponse);

        ResponseEntity<SessionListResponse> responseEntity = sessionController.findByStatus(status, page, pageSize);

        verify(sessionService, times(1)).findWithStatus(status,  page, pageSize);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockResponse, responseEntity.getBody());
    }

    @Test
    void testFindByStatus_StatusNotFound() throws ParseException {
        char status = 'B';
        String createdBy = "USER123";
        int page = 0;
        int pageSize = 10;

        when(sessionService.findWithStatus(status, page, pageSize))
                .thenThrow(new StatusNotFoundException());
      }


    @Test
    void testUpdateToArchive_Successful() throws ParseException {
        String sessionId = "SESSION_01";

        SessionResponse archivedSessionResponse = new SessionResponse();
        archivedSessionResponse.setSessionId(sessionId);
        when(sessionService.updateToArchived(sessionId)).thenReturn(archivedSessionResponse);

        SessionResponse response = sessionController.updateToArchive(sessionId).getBody();
        assertNotNull(response);
        //assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(archivedSessionResponse, response);

        verify(sessionService).updateToArchived(sessionId);
    }

    @Test
    void testDeleteUserById_Successful() {
        String sessionId = "SESSION_01";
        sessionController.deleteUserById(sessionId);
        verify(sessionService).deleteSessionById(sessionId);
    }

}
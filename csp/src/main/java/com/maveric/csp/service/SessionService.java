package com.maveric.csp.service;

import com.maveric.csp.dto.request.SessionRequest;
import com.maveric.csp.dto.request.UpdateSessionRequest;
import com.maveric.csp.dto.response.SessionDeleteResponse;
import com.maveric.csp.dto.response.SessionListResponse;
import com.maveric.csp.dto.response.SessionResponse;
import com.maveric.csp.entity.Session;

import java.text.ParseException;

public interface SessionService {


     SessionResponse createSession(SessionRequest sessionRequest) throws ParseException;
     SessionResponse updateToArchived(String id) throws ParseException;
     SessionResponse udateSessionById(String id, UpdateSessionRequest session);
     SessionListResponse findWithStatus(char status, int page, int pageSize) throws ParseException;
     SessionDeleteResponse deleteSessionById(String id);


}

package com.maveric.csp.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class SessionResponse {
    private String sessionId;
    private String sessionName;
    private String createdBy;
    private Date createdOn;
    private Date updatedOn;
    private char status;
    private String remarks;
    private char archiveFlag;
    private long customerId;
    private String customerName;

}

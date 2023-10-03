package com.maveric.csp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SessionHistory {

    @Id
    private String sessionId;
    private String sessionName;
    private String createdBy;
    private Date createdOn;
    private Date updatedOn;
    private char status;
    private String remarks;
    private long customerId;
    private Date deletedOn;

    @PrePersist
    public void deletedOn(){
        deletedOn=new Date();
    }


}

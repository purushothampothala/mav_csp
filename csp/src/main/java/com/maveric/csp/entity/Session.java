package com.maveric.csp.entity;

import com.maveric.csp.config.StringPrefixedSequenceIdGenerator;
import jakarta.persistence.*;
import org.hibernate.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

import static com.maveric.csp.constants.Constants.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "customer")

public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE)
    @GenericGenerator(
            name = SESSION_SEQUENCE_NAME,
            strategy = SESSION_SEQUENCE_STRATEGY,
            parameters = {
                    @Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = INCREMENT_PARAM_VALUE),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = PREFIX_PARAMETER),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = NUMBER_FORMAT_PARAMETER_VALUE) })
    private String sessionId;

    private String sessionName;

    private String createdBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    private Date updatedOn;
    private char status;
    private String remarks;

   @ManyToOne()
   @JoinColumn(name = JOIN_COLUMN_CUSTOMER)
   private Customer customer;
    @PrePersist
    public void status() {
        setStatus(ACTIVE_STATUS);
        createdOn=new Date();
        updatedOn=new Date();
}

}

package com.maveric.csp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Customer {
    @Id
    @SequenceGenerator(name="customer_seq",
        sequenceName="customer_seq",
        allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="customer_seq")

    private long id;
    private String customerName;

    @OneToMany(mappedBy="customer")
    private List<Session>sessions;

    public Customer(long customerId,String customerName) {
    }
}

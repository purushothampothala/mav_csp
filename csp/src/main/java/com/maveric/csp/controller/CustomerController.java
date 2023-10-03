package com.maveric.csp.controller;

import com.maveric.csp.dto.request.CustomerRequest;
import com.maveric.csp.dto.response.CustomerResponse;
import com.maveric.csp.entity.Customer;
import com.maveric.csp.repository.CustomerRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ModelMapper mapper;

    @PostMapping("/customer")
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerRequest customerRequest){
        Customer customer=mapper.map(customerRequest,Customer.class);
        Customer savedCustomer= customerRepository.save(customer);
        CustomerResponse customerResponse=mapper.map(savedCustomer,CustomerResponse.class);
       return ResponseEntity.ok().body(customerResponse);

    }
}

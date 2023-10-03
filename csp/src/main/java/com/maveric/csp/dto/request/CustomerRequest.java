package com.maveric.csp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.maveric.csp.constants.Constants.CUSTOMER_NAME_ERR;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class CustomerRequest {
    @NotBlank(message = CUSTOMER_NAME_ERR)
    private String customerName;


}

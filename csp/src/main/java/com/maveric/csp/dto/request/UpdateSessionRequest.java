package com.maveric.csp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.maveric.csp.constants.Constants.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSessionRequest {

    @NotBlank(message = SESSION_NAME_ERR)
    private String sessionName;
    @NotBlank(message=REMARKS_ERR)
    private String remarks;

}

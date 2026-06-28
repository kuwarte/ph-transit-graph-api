package com.phtransitgraph.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OperatorRegisterRequest {

    @NotBlank(message = "full name is required")
    private String fullName;

    @NotBlank(message = "email is required")
    @Email(message = "invalid email format")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 8, message = "password must be at least 8 characters")
    private String password;

    @NotBlank(message = "oparator name is required")
    private String operatorName;

    private String franchiseNo;
    private String contactNumber;

}

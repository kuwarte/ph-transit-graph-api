package com.phtransitgraph.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OperatorRequest {

    @NotBlank(message = "operator name is required")
    private String operatorName;

    private String franchiseNo;
    private String contactNumber;
}

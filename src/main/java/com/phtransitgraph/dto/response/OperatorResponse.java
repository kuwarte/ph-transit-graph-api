package com.phtransitgraph.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OperatorResponse {
    private String id;
    private String userId;
    private String fullName;
    private String email;
    private String operatorName;
    private String franchiseNo;
    private String contactNumber;
    private boolean verified;
    private LocalDateTime createdAt;
}

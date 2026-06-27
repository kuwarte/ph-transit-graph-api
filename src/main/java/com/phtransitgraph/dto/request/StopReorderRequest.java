package com.phtransitgraph.dto.request;

import java.util.List;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class StopReorderRequest {

    @NotEmpty(message = "stops list cannot be empty")
    private List<StopReorderItem> stops;
}

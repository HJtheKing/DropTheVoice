package com.ssafy.a505.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RedisResponseDTO {
    private Long id;
    private Double x;
    private Double y;
}

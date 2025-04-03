package com.project.daeng_geun.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchRequestDTO {
    private Long senderId;
    private Long receiverId;
    private String status;
}

package com.dietergandalf.store_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StandDto {
    private Long standId;
    private Double price;
    private Long standUserId;
    private String standUserName;
    private Double size;
    private Long standOwnerId;
    private String standOwnerName;
}

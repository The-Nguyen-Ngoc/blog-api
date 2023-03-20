package com.example.blogapi.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ListRecentResponse {
    private List<RecentDto> recentDtoList;
    private long totalElement;
    private int totalPages;
}

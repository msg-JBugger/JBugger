package com.example.demo.bug_api_calls;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BugSearchResponse {
    int pageNumber;
    int pageSize;
    int totalPagesCount;
    int totalResultsCount;
    List<BugAttributes> items;
}

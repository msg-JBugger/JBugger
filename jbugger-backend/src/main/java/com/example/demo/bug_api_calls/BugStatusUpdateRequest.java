package com.example.demo.bug_api_calls;

import com.example.demo.enums.BugStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BugStatusUpdateRequest {
    BugStatusEnum status;
}

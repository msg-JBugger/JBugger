package com.example.demo.bug_api_calls;

import com.example.demo.enums.BugSeverity;
import com.example.demo.enums.BugStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BugUpdateRequest {
    String title;
    String description;
    String detectedInVersion;
    String fixedInVersion;
    BugSeverity severity;
    BugStatusEnum status;
    String assigneeUsername;
}

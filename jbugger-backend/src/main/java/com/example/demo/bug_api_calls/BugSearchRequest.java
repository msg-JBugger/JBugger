package com.example.demo.bug_api_calls;

import com.example.demo.enums.BugSeverity;
import com.example.demo.enums.BugStatus;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BugSearchRequest {
    int pageNumber;
    int pageSize;
    String title;
    String description;
    String version;
    String fixedRevision;
    Date targetDate;
    BugStatus status;
    BugSeverity severity;
    String reporterUsername;
    String assigneeUsername;
}

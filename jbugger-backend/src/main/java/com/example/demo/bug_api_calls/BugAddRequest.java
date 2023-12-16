package com.example.demo.bug_api_calls;

import com.example.demo.enums.BugSeverity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BugAddRequest {
    String title;
    String description;
    String detectedInVersion;
    String fixedInVersion;
    Date targetDate;
    BugSeverity severity;
    String assigneeUsername;
    String attachmentFilename;
    byte[] attachmentContent;
}

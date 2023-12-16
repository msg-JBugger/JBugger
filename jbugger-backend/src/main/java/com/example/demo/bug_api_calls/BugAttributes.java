package com.example.demo.bug_api_calls;

import com.example.demo.entity.Bug;
import com.example.demo.enums.BugSeverity;
import com.example.demo.enums.BugStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BugAttributes {
    long bugId;
    String title;
    String description;
    String detectedInVersion;
    String fixedInVersion;
    Date targetDate;
    BugStatus status;
    BugSeverity severity;
    String reporterUsername;
    String assigneeUsername;

    public static BugAttributes fromBug(Bug bug) {
        return BugAttributes.builder()
                .bugId(bug.getBugId())
                .title(bug.getTitle())
                .description(bug.getDescription())
                .detectedInVersion(bug.getDetectedInVersion())
                .fixedInVersion(bug.getFixedInVersion())
                .targetDate(bug.getTargetDate())
                .status(bug.getStatus())
                .severity(bug.getSeverity())
                .reporterUsername(bug.getCreatedByUser().getUsername())
                .assigneeUsername(bug.getAssignedToUser().getUsername())
                .build();
    }
}

package com.example.demo.bug_api_calls;

import com.example.demo.entity.Bug;
import com.example.demo.enums.BugSeverity;
import com.example.demo.enums.BugStatusEnum;
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
    BugStatusEnum status;
    BugSeverity severity;
    String reporterUsername;
    String assigneeUsername;

    public static BugAttributes fromBug(Bug bug) {
        var severity = switch (bug.getSeverity()) {
            case 0 -> BugSeverity.LOW;
            case 1 -> BugSeverity.MEDIUM;
            case 2 -> BugSeverity.HIGH;
            case 3 -> BugSeverity.CRITICAL;
            default -> throw new IllegalStateException("Unexpected value: " + bug.getSeverity());
        };

        return BugAttributes.builder()
                .bugId(bug.getBugId())
                .title(bug.getTitle())
                .description(bug.getDescription())
                .detectedInVersion(bug.getVersion())
                .fixedInVersion(bug.getFixedRevision())
                .targetDate(bug.getTargetDate())
                .status(bug.getStatus())
                .severity(severity)
                .reporterUsername(bug.getCreatedByUser().getUsername())
                .assigneeUsername(bug.getAssignedTo().getUsername())
                .build();
    }
}

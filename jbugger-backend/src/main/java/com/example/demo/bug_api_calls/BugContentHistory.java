package com.example.demo.bug_api_calls;

import com.example.demo.entity.History;
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
public class BugContentHistory {
    long historyId;
    Date modifiedDate;
    BugStatusEnum beforeStatus;
    BugStatusEnum afterStatus;

    public static BugContentHistory fromHistory(History history) {
        return BugContentHistory.builder()
                .historyId(history.getHistoryId())
                .modifiedDate(history.getModifiedDate())
                .beforeStatus(history.getBeforeStatus())
                .afterStatus(history.getAfterStatus())
                .build();
    }
}

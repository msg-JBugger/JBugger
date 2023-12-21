package com.example.demo.bug_api_calls;

import com.example.demo.entity.Bug;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BugContent {
    BugAttributes bugAttributes;
    List<BugContentComment> comments;
    List<BugContentAttachment> attachments;
    List<BugContentHistory> history;

    public static BugContent fromBug(Bug bug) {

        List<BugContentComment> comments = bug.getBugComments().stream().map(BugContentComment::fromComment).toList();
        List<BugContentAttachment> attachments = bug.getBugAttachments().stream().map(BugContentAttachment::fromAttachment).toList();
        List<BugContentHistory> historyList = bug.getBugHistory().stream().map(BugContentHistory::fromHistory).toList();

        return BugContent.builder()
                .bugAttributes(BugAttributes.fromBug(bug))
                .comments(comments)
                .attachments(attachments)
                .history(historyList)
                .build();
    }
}

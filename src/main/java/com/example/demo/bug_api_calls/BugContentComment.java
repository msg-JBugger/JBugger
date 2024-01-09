package com.example.demo.bug_api_calls;

import com.example.demo.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BugContentComment {
    long commentId;
    String text;
    Date date;
    String authorUsername;

    public static BugContentComment fromComment(Comment comment) {
        return BugContentComment.builder()
                .commentId(comment.getCommentId())
                .text(comment.getText())
                .date(comment.getDate())
                .authorUsername(comment.getUser().getUsername())
                .build();
    }
}

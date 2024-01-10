package com.example.demo;

import com.example.demo.bug_api_calls.BugContentComment;
import com.example.demo.entity.Comment;
import com.example.demo.entity.User;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BugContentCommentTest {

    @Test
    void testSetterMethods() {

        BugContentComment bugContentComment = new BugContentComment();

        long commentId = 1L;
        String text = "Test Comment";
        Date date = new Date();
        String authorUsername = "author";


        bugContentComment.setCommentId(commentId);
        bugContentComment.setText(text);
        bugContentComment.setDate(date);
        bugContentComment.setAuthorUsername(authorUsername);

        assertEquals(commentId, bugContentComment.getCommentId());
        assertEquals(text, bugContentComment.getText());
        assertEquals(date, bugContentComment.getDate());
        assertEquals(authorUsername, bugContentComment.getAuthorUsername());
    }

    @Test
    void testFromCommentWithSetters() {

        User author = new User();
        author.setUsername("author");
        Date mockDate = new Date();
        Comment comment = new Comment();
        comment.setCommentId(1L);
        comment.setText("Test Comment");
        comment.setDate(mockDate);
        comment.setUser(author);

        BugContentComment expectedResult = new BugContentComment();
        expectedResult.setDate(mockDate);
        expectedResult.setText("Test Comment");
        expectedResult.setCommentId(1L);
        expectedResult.setAuthorUsername("author");
        BugContentComment actualResult = BugContentComment.fromComment(comment);

        assertEquals(expectedResult, actualResult);

    }

}

package com.example.demo.controller;


import com.example.demo.auth.AuthenticationResponse;
import com.example.demo.auth.RegisterRequest;
import com.example.demo.comment_call.CommentDTO;
import com.example.demo.entity.Comment;
import com.example.demo.service.CommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentsController {

    private final CommentsService commentsService;

    @GetMapping("/{bugId}")
    public ResponseEntity<List<Comment>> getCommentsOfBug(@PathVariable Long bugId){
        List<Comment> comments = new ArrayList<>(commentsService.getCommentsForBug(bugId));
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addComment(@RequestBody CommentDTO commentDTO){
        String authenticatedUsername = getAuthenticatedUsername();
        commentDTO.setUsername(authenticatedUsername);
        this.commentsService.addComment(commentDTO);
        return ResponseEntity.ok("Comment added");
    }

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        return null;
    }
}
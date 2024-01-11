package com.example.demo.service;

import com.example.demo.comment_call.CommentDTO;
import com.example.demo.entity.Comment;
import com.example.demo.repo.BugRepositoryInterface;
import com.example.demo.repo.CommentsRepositoryInterface;
import com.example.demo.repo.UserRepositoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentsService {

    @Autowired
    private final CommentsRepositoryInterface commentsRepositoryInterface;

    @Autowired
    private final BugRepositoryInterface bugRepositoryInterface;

    @Autowired
    private final UserRepositoryInterface userRepositoryInterface;

    public List<Comment> getCommentsForBug(Long bugId) {
        return commentsRepositoryInterface.findAll()
                .stream()
                .filter(comment -> comment.getBug().getBugId().equals(bugId))
                .collect(Collectors.toList());
    }

    public void addComment(CommentDTO commentDTO){
        var comment = Comment
                .builder()
                .text(commentDTO.getText())
                .date(commentDTO.getDate())
                .bug(bugRepositoryInterface.findById(commentDTO.getBugId()).orElseThrow())
                .user(userRepositoryInterface.findByUsername(commentDTO.getUsername()).orElseThrow())
                .build();
        commentsRepositoryInterface.save(comment);
    }

}
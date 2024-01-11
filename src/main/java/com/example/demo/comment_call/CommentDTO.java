package com.example.demo.comment_call;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

public class CommentDTO {
    @NotNull
    private Long bugId;

    @NotNull
    private String username;

    private Date date;

    @NotNull
    @Size(max = 1000)
    private String text;

    public Long getBugId() {
        return bugId;
    }

    public void setBugId(Long id) {
        this.bugId = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText(){
        return text;
    }
    public void setText(String text){this.text = text;}

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }
}

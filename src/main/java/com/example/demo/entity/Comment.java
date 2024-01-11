package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentId;

    private String text;

    private Date date;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "bugId")
    private Bug bug;
}
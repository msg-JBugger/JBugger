package com.example.demo.entity;

import com.example.demo.enums.BugStatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "bugs")
public class Bug {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bugId;

    private String title;

    private String description;

    private String version;

    private String fixedRevision;

    private Date targetDate;

    private BugStatusEnum status;

    private int severity;

    @ManyToOne
    @JoinColumn(name = "createdByUserId")
    private User createdByUser;

    @ManyToOne
    @JoinColumn(name = "assignedToUserId")
    private User assignedTo;

    @OneToMany(
            mappedBy = "bug",
            targetEntity = Comment.class,
            cascade = CascadeType.ALL
    )
    private Set<Comment> bugComments = new HashSet<>();

    @OneToMany(
            mappedBy = "bug",
            targetEntity = Comment.class,
            cascade = CascadeType.ALL
    )
    private Set<Attachment> bugAttachments = new HashSet<>();

    @OneToMany(
            mappedBy = "bug",
            targetEntity = Comment.class,
            cascade = CascadeType.ALL
    )
    private Set<History> bugHistory = new HashSet<>();
}

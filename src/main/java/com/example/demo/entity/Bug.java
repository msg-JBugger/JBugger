package com.example.demo.entity;

import com.example.demo.enums.BugSeverity;
import com.example.demo.enums.BugStatus;
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

    private String detectedInVersion;

    private String fixedInVersion;

    private Date targetDate;

    private BugStatus status;

    private BugSeverity severity;

    @ManyToOne
    @JoinColumn(name = "createdByUserId")
    private User createdByUser;

    @ManyToOne
    @JoinColumn(name = "assignedToUserId")
    private User assignedToUser;

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

    public String bugInfo() {
        return String.format("#%s, titlu = '%s', versiune = '%s', severitate = '%s'",
                getBugId().toString(), title, detectedInVersion, severity);
    }
}

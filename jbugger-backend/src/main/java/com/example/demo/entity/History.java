package com.example.demo.entity;


import com.example.demo.enums.BugStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "histories")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long historyId;

    private Date modifiedDate;

    private BugStatus beforeStatus;

    private BugStatus afterStatus;

    @ManyToOne
    @JoinColumn(name = "bugId")
    private Bug bug;
}

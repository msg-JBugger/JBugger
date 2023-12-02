package com.example.demo.entity;


import com.example.demo.enums.BugStatusEnum;
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

    private BugStatusEnum beforeStatus;

    private BugStatusEnum afterStatus;

    @ManyToOne
    @JoinColumn(name = "bugId")
    private User bug;
}

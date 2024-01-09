package com.example.demo.events;

import com.example.demo.entity.Bug;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
@Builder
public class BugUpdateEvent {
    private Bug updatedBug;
}

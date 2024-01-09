package com.example.demo.events;

import com.example.demo.entity.User;
import lombok.*;

@Data
@AllArgsConstructor
@Getter
@Builder
public class LoginEvent {
    private User loggedUser;
}

package com.example.demo.events;

import com.example.demo.entity.User;
import com.example.demo.user_call.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
@Builder
public class UserUpdateEvent {
    User updated;
    User updater;
    UserInfo oldInfo;
}

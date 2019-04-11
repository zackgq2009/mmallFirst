package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import net.sf.jsqlparser.schema.Server;

import javax.servlet.http.HttpSession;

public interface IUserService {
    ServerResponse<User> login(String username, String password);

    ServerResponse<String> logout(HttpSession session);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str, String type);

    ServerResponse<User> getUserInfo(HttpSession session);

    ServerResponse<String> forgetQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String question, String answer);

    ServerResponse<String> changePasswordByToken(String username, String newPassword, String token);

    ServerResponse<String> changePasswordByOldPassword(User user, String oldPassword, String newPassword);
}

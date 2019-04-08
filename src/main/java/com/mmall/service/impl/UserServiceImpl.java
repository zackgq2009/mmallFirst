package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import net.sf.jsqlparser.schema.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 *
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        //密码MD5登录
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码不正确");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse<String> logout(HttpSession session) {
        return null;
    }

    @Override
    public ServerResponse<String> register(User user) {
//        int resultCount = userMapper.checkUsername(user.getUsername());
//        if (resultCount > 0) {
//            return ServerResponse.createByErrorMessage("用户名已存在");
//        }
        ServerResponse response = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!response.isSuccess()) {
            return response;
        }

//        resultCount = userMapper.checkEmail(user.getEmail());
//        if (resultCount > 0) {
//            return ServerResponse.createByErrorMessage("email已存在");
//        }
        response = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!response.isSuccess()) {
            return response;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        //md5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(type)) {
            int resultCount;
            switch (type) {
                case Const.USERNAME:
                    resultCount = userMapper.checkUsername(str);
                    if (resultCount == 0) {
                        return ServerResponse.createBySuccessMessage("用户名不存在");
                    }
                    return ServerResponse.createByErrorMessage("用户名已存在");
                case Const.EMAIL:
                    resultCount = userMapper.checkEmail(str);
                    if (resultCount == 0) {
                        return ServerResponse.createBySuccessMessage("email不存在");
                    }
                    return ServerResponse.createByErrorMessage("email已存在");
            }
            return ServerResponse.createBySuccessMessage("校验成功");
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
    }

    @Override
    public ServerResponse<User> getUserInfo(HttpSession session) {
        return null;
    }

    @Override
    public ServerResponse<String> forgetQuestion(String username) {
        ServerResponse<String> validResult = this.checkValid(Const.USERNAME, username);
        if (validResult.isSuccess()) {
            return ServerResponse.createByErrorMessage("输入的用户名不存在");
        }
        String question = userMapper.getQuestionByUsername(username);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("用户的提示问题为空");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            //说明问题以及问题答案是这个用户的，并且是正确的，这个时候我们不只是单纯的传出我们的提示信息，主要是要把临时token传出去
//            return ServerResponse.createBySuccessMessage("提示问题的答案正确");
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey("token_" + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        } else {
            return ServerResponse.createByErrorMessage("提示问题的答案错误");
        }
    }

    @Override
    public ServerResponse<String> changePassword(String username, String oldPassword, String newPassword) {
        return null;
    }
}

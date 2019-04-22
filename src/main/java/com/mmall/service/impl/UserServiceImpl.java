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
import sun.tools.jstat.Token;

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
        ServerResponse<String> validResult = this.checkValid(username, Const.USERNAME);
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
            //构建本地缓存，调用链的方式 ,1000是设置缓存的初始化容量，maximumSize是设置缓存最大容量，当超过了最大容量，guava将使用LRU算法（最少使用算法），来移除缓存项
            //expireAfterAccess(12,TimeUnit.HOURS)设置缓存有效期为12个小时
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PRIFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        } else {
            return ServerResponse.createByErrorMessage("提示问题的答案错误");
        }
    }

    @Override
    public ServerResponse<String> changePasswordByToken(String username, String newPassword, String token) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(token)) {
            ServerResponse<String> validResult = this.checkValid(username, Const.USERNAME);
            if (validResult.isSuccess()) {
                return ServerResponse.createByErrorMessage("输入的用户名不存在");
            } else {
                String tokenValue = TokenCache.getValue(TokenCache.TOKEN_PRIFIX + username);
                if (org.apache.commons.lang3.StringUtils.equals(tokenValue, token)) {
                    String MD5NewPassword = MD5Util.MD5EncodeUtf8(newPassword);
                    int updateResult = userMapper.updatePasswordByToken(username, MD5NewPassword);
                    if (updateResult > 0) {
                        return ServerResponse.createBySuccessMessage("密码修改成功");
                    } else {
                        return ServerResponse.createByErrorMessage("密码修改失败");
                    }
                } else {
                    return ServerResponse.createByErrorMessage("token无效或者过期");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("token不合法");
        }
    }

    @Override
    public ServerResponse<String> changePasswordByOldPassword(User user, String oldPassword, String newPassword) {
        String MD5OldPassword = MD5Util.MD5EncodeUtf8(oldPassword);
        String MD5NewPassword = MD5Util.MD5EncodeUtf8(newPassword);
        int userId = user.getId();

//        int updateResult = userMapper.updatePasswordByOldPassword(userId, MD5OldPassword, MD5NewPassword);
//        if (updateResult > 0) {
//            return ServerResponse.createBySuccessMessage("密码修改成功");
//        } else {
//            return ServerResponse.createByErrorMessage("密码修改失败");
//        }
        int checkResult = userMapper.checkPassword(userId, MD5OldPassword);
        if (checkResult > 0) {
            user.setPassword(MD5NewPassword);
            int updatePasswordResult = userMapper.updateByPrimaryKeySelective(user);
            if (updatePasswordResult > 0) {
                return ServerResponse.createBySuccessMessage("密码修改成功");
            } else {
                return ServerResponse.createByErrorMessage("密码修改失败");
            }
        } else {
            return ServerResponse.createByErrorMessage("输入的旧密码错误");
        }
    }

    @Override
    public ServerResponse<User> updateUserInfo(User user) {
        //我要在update之前去确认一些数据，例如username，跟email
        String username = user.getUsername();
        int userId = user.getId();
        String email = user.getEmail();

        ServerResponse<String> checkValidResult = this.checkValid(username, Const.USERNAME);
        if (checkValidResult.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户名有问题，这个用户名不存在");
        } else {
            int checkEmailResult = userMapper.checkEmailByUserId(userId, email);
            if (checkEmailResult > 0) {
                return ServerResponse.createByErrorMessage("此邮箱已经有人使用，请更改邮箱之后再次提交");
            } else {
                User updateUser = new User();
                updateUser.setId(userId);
                updateUser.setEmail(email);
                updateUser.setUsername(username);
                updateUser.setPhone(user.getPhone());
                updateUser.setRole(user.getRole());
                updateUser.setQuestion(user.getQuestion());
                updateUser.setAnswer(user.getAnswer());

                int updateResult = userMapper.updateByPrimaryKeySelective(updateUser);
                if (updateResult > 0) {
                    return ServerResponse.createBySuccess("更新用户信息成功", updateUser);
                } else {
                    return ServerResponse.createByErrorMessage("更新用户信息失败");
                }
            }
        }
    }


}

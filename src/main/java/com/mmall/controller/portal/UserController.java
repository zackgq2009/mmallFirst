package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import net.sf.jsqlparser.schema.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 用户的controller
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;
    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        //controller调用service， service再调用mybatis, mybatis > dao
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    @RequestMapping(value = "checkValid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    @RequestMapping(value = "getUserInfo.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户貌似未登陆，暂时无法获取到用户的信息，请登陆后再进行尝试");
    }

    @RequestMapping(value = "updateUserInfo.do", method = RequestMethod.GET)
    @ResponseBody
    //注意两点，第一是username不能更改，id是从session中获取到的，第二是我们要去验证email是否被其他用户使用过了，如果使用过则这个邮箱就不能被再次使用了
    public ServerResponse<User> updateUserInfo(HttpSession session, User newUser) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户貌似未登录，暂时无法获取到用户的信息，请登录后再进行尝试");
        } else {
            int userId = user.getId();
            String username = user.getUsername();

            //强制把id跟username绑定要传进来的User上，这样可以防止横向越权
            newUser.setId(userId);
            newUser.setUsername(username);
            ServerResponse<User> updateResult = iUserService.updateUserInfo(newUser);
            if (updateResult.isSuccess()) {
                session.setAttribute(Const.CURRENT_USER, updateResult.getData());
            }
            newUser.setUsername(username);
            return iUserService.updateUserInfo(newUser);
        }
    }

    @RequestMapping(value = "forgetQuestion.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetQuestion(String username) {
        return iUserService.forgetQuestion(username);
    }

    @RequestMapping(value = "checkAnswer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    @RequestMapping(value = "changePasswordByToken.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> changePasswordByToken(String username, String newPassword, String token) {
        return iUserService.changePasswordByToken(username, newPassword, token);
    }

    @RequestMapping(value = "changePasswordByOldPassword.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> changePasswordByOldPassword(String oldPassword, String newPassword, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return iUserService.changePasswordByOldPassword(user, oldPassword, newPassword);
        }
        return ServerResponse.createByErrorMessage("用户未登录，或许账户有问题，暂时无法修改原有密码");
    }

}


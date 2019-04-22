package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    User selectLogin(@Param("usernameA") String username, @Param("passwordA") String password);

    int checkEmail(String email);

    String getQuestionByUsername(String username);

    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    int updatePasswordByToken(@Param("username") String username,@Param("newPassword") String newPassword);

    int updatePasswordByOldPassword(@Param("userId") Integer userId, @Param("oldPassword") String oldPassword, @Param("newPassword") String newPassword);

    int checkPassword(@Param("userId") Integer userId,@Param("password") String password);

    int checkEmailByUserId(@Param("userId") Integer userId, @Param("email") String email);
}
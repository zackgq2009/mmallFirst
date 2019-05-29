package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(value = "/manage/category/")
public class CategoryManageController {

    @Autowired
    private ICategoryService iCategoryService;

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "subCategory.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Category>> getSubCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        //获取子分类
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户为登录，请登录");
        } else if (iUserService.checkAdminRole(user).isSuccess()) {
            //校验是否是管理员
            //通过categoryId查询下子分类节点，并且不递归
            return iCategoryService.getSubCategory(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("不是管理员在登录，无权进行操作");
        }
    }

    @RequestMapping(value = "addCategory.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> addCategory(HttpSession session, @RequestParam(value = "superCategoryId", defaultValue = "0") int superCategoryId, String categoryName) {
        //添加新的商品分类
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户为登录，请登录");
        } else if (iUserService.checkAdminRole(user).isSuccess()) {
            //校验是否是管理员
//            return ServerResponse.createBySuccessMessage("管理员正确登录，请继续操作");
            return iCategoryService.addCategory(superCategoryId, categoryName); //添加我们的品类
        } else {
            return ServerResponse.createByErrorMessage("不是管理员在登录，无权进行操作");
        }
    }

    @RequestMapping(value = "setCategoryName.do", method = RequestMethod.PUT)
    @ResponseBody
    public ServerResponse<String> setCategoryName(HttpSession session, int categoryId, String categoryName) {
        //修改分类名称
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户为登录，请登录");
        } else if (iUserService.checkAdminRole(user).isSuccess()) {
            //校验是否是管理员
            return iCategoryService.updateCategoryName(categoryId, categoryName); // 更新我们的品类
        } else {
            return ServerResponse.createByErrorMessage("不是管理员在登录，无权进行操作");
        }
    }

    @RequestMapping(value = "getCategoryId.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Integer>> getCategoryId(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") int categoryId) {
        //获取分类id以及相应子分类的id
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户为登录，请登录");
        } else if (iUserService.checkAdminRole(user).isSuccess()) {
            //校验是否是管理员
            //通过categoryId来获取其子分类节点的id以及子子孙孙的子分类节点的id
            // 1---100---1000----10000-----1000000
            // 1---100---1000----10000-----1000000
            return iCategoryService.getCategoryId(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("不是管理员在登录，无权进行操作");
        }
    }
}

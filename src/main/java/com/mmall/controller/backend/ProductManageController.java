package com.mmall.controller.backend;


import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by Tsingkuo
 */
@RestController
@RequestMapping(value = "/manage/product/")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @RequestMapping(value = "save.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product) {
        //保存商品的接口
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        } else if (iUserService.checkAdminRole(user).isSuccess()) {
            //校验是否是管理员
            //传入product对象，把该对象保存下来
            return iProductService.saveOrUpdateProduct(product);
        } else {
            return ServerResponse.createByErrorMessage("不是管理员在登录，无权进行操作");
        }
    }

    @RequestMapping(value = "set_sale_status.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setProductStatus(HttpSession session, Integer productId, Integer status) {
        //更新产品的售卖状态
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        } else if (iUserService.checkAdminRole(user).isSuccess()) {
            //校验是否是管理员
            //传入productId，Status来更新产品售卖的状态
            return iProductService.updateProductStatus(productId, status);
        } else {
            return ServerResponse.createByErrorMessage("不是管理员在登录，无权进行操作");
        }
    }

    @RequestMapping(value = "detail.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse manageProductDetail(HttpSession session, Integer productId) {
        //根据productId获取商品的详情
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        } else if (iUserService.checkAdminRole(user).isSuccess()) {
            //校验是否是管理员
            //传入productId，返回ProductDetailVo对象
            return iProductService.manageProductDetail(productId);
        } else {
            return ServerResponse.createByErrorMessage("不是管理员在登录，无权进行操作");
        }
    }



}

package com.mmall.controller.portal;

import com.mmall.common.ServerResponse;
import com.mmall.service.ICartService;
import net.sf.jsqlparser.schema.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * created by tsingkuo
 */
@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;

    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getCartList(HttpSession session) {
        return null;
    }

    @RequestMapping(value = "add.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse addProductToCart(HttpSession session, Integer productId, Integer productCount) {
        return null;
    }

    @RequestMapping(value = "update.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse updateProductCountInCart(HttpSession session, Integer productId, Integer productCount) {
        return null;
    }

    @RequestMapping(value = "delete_product.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse deleteProductInCart(HttpSession session, Integer productId) {
        return null;
    }

    @RequestMapping(value = "select.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse selectProductInCart(HttpSession session, Integer productId) {
        return null;
    }

    @RequestMapping(value = "unselect.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse unSelectProductInCart(HttpSession session, Integer productId) {
        return null;
    }

    @RequestMapping(value = "allCount.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getProductsCountInCart(HttpSession session) {
        return null;
    }

    @RequestMapping(value = "productCount.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getProductCountInCart(HttpSession session, Integer productId) {
        return null;
    }

    @RequestMapping(value = "selectAll.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse selectAllProducts(HttpSession session) {
        return null;
    }

    @RequestMapping(value = "unSelectAll.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse unSelectAllProducts(HttpSession session) {
        return null;
    }

}

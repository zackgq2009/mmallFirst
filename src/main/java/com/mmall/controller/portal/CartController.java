package com.mmall.controller.portal;

import com.mmall.common.ServerResponse;
import com.mmall.service.ICartService;
import net.sf.jsqlparser.schema.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public ServerResponse getCartList() {
        return null;
    }

    @RequestMapping(value = "add.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse addProductToCart(Integer productId, Integer productCount) {
        return null;
    }

    @RequestMapping(value = "update.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse updateProductCountInCart(Integer productId, Integer productCount) {
        return null;
    }

    @RequestMapping(value = "delete_product.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse deleteProductInCart(Integer productId) {
        return null;
    }

    @RequestMapping(value = "select.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse selectProductInCart(Integer productId) {
        return null;
    }

    @RequestMapping(value = "unselect.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse unSelectProductInCart(Integer productId) {
        return null;
    }

    @RequestMapping(value = "allCount.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getProductsCountInCart() {
        return null;
    }

    @RequestMapping(value = "productCount.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getProductCountInCart(Integer productId) {
        return null;
    }

    @RequestMapping(value = "selectAll.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse selectAllProducts() {
        return null;
    }

    @RequestMapping(value = "unSelectAll.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse unSelectAllProducts() {
        return null;
    }

}

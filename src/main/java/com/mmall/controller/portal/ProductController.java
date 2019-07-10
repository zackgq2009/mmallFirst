package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * created by tsingkuo
 */
@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    @RequestMapping(value = "detail.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ProductDetailVo> getDetail(Integer productId) {
        return iProductService.getProductDetail(productId);
    }


    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> getDetail(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                                                             @RequestParam(value = "keyword", required = false) String keyword,
                                                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                             @RequestParam(value = "orderBy", defaultValue = "price_desc") String orderBy) {
        return iProductService.getProductByKeywordandCategory(categoryId, keyword, pageNum, pageSize, orderBy);
    }
}

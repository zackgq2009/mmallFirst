package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;

public interface IProductService {

    public ServerResponse saveOrUpdateProduct(Product product);

    public ServerResponse updateProductStatus(Integer productId, Integer status);

    public ServerResponse manageProductDetail(Integer productId);

    public ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize);

    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize);

    public ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    public ServerResponse<PageInfo> getProductByKeywordandCategory(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy);
}

package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

public interface IProductService {

    public ServerResponse saveOrUpdateProduct(Product product);

    public ServerResponse updateProductStatus(Integer productId, Integer status);

    public ServerResponse manageProductDetail(Integer productId);

    public ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize);

    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize);
}

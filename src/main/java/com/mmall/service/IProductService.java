package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

public interface IProductService {

    public ServerResponse saveOrUpdateProduct(Product product);

    public ServerResponse updateProductStatus(Integer productId, Integer status);

    public ServerResponse manageProductDetail(Integer productId);
}

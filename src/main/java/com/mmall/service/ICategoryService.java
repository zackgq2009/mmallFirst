package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

public interface ICategoryService {

    public ServerResponse<List<Category>> getSubCategory(Integer categoryId);

    public ServerResponse<String> addCategory(Integer superCategoryId, String categoryName);

    public ServerResponse<String> updateCategoryName(Integer categoryId, String categoryName);

    public ServerResponse<List<Integer>> getCategoryId(Integer categoryId);

}

package com.mmall.dao;

import com.mmall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectProducts();

    List<Product> selectByNameOrId(@Param(value = "productName") String productName, @Param(value = "productId") Integer productId);

    List<Product> selectByNameAndId(@Param(value = "productName1") String productName, @Param(value = "productId1") Integer productId);

    List<Product> selectByNameAndCategoryId(@Param(value = "productName") String productName, @Param(value = "categoryIds") List<Integer> categoryIds);
}
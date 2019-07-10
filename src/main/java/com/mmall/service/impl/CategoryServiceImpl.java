package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import net.sf.jsqlparser.schema.Server;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse<List<Category>> getSubCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoriesByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    @Override
    public ServerResponse<String> addCategory(Integer superCategoryId, String categoryName) {
        if (superCategoryId == null || StringUtils.isNotBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加商品时传入的参数有误");
        } else {
            Category category = new Category();
            category.setName(categoryName);
            category.setParentId(superCategoryId);
            category.setStatus(true); //代表这个分类的状态是可用的

            int rowCount = categoryMapper.insert(category);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("添加品类成功");
            } else {
                return ServerResponse.createByErrorMessage("添加品类失败");
            }
        }
    }

    @Override
    public ServerResponse<String> updateCategoryName(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isNotBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("修改商品名称时传入的参数有误");
        } else {
            Category category = new Category();
            category.setId(categoryId);
            category.setName(categoryName);

            int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("修改品类名称成功");
            } else {
                return ServerResponse.createByErrorMessage("修改品类名称失败");
            }
        }
    }

    //通过调用递归的方法来获取到搜索的节点Id已经搜索的children的id
    @Override
    public ServerResponse<List<Integer>> getCategoryId(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        getAllChildrenCategory(categorySet, categoryId);

        List<Integer> categoryIdList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(categorySet)) {
            return ServerResponse.createByErrorMessage("无法通过该id号查询到相应的categoryid以及子id");
        } else {
            for (Category categoryItem : categorySet
            ) {
                categoryIdList.add(categoryItem.getId());
            }
            return ServerResponse.createBySuccess(categoryIdList);
        }
    }

    //通过递归查询获取搜索的子分类节点
    //使用set是使用set的去重机制，保证获取到的categorySet是一个没有重复的集合
    private Set<Category> getAllChildrenCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }

        List<Category> categoryList = categoryMapper.selectCategoriesByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            return categorySet;
        } else {
            for (Category categoryItem : categoryList
            ) {
//                categorySet.add(categoryItem);
                getAllChildrenCategory(categorySet, categoryItem.getId());
            }
            return categorySet;
        }
    }
}

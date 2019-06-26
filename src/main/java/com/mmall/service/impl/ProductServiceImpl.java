package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse saveOrUpdateProduct(Product product) {
        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImages = product.getSubImages().split(",");
                if (subImages.length > 0) {
                    product.setMainImage(subImages[0]);
                }
            }

            if (product.getId() != null) {
                //有ID号，说明是更新产品
                int rowCount = productMapper.updateByPrimaryKeySelective(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccessMessage("更新产品成功");
                } else {
                    return ServerResponse.createByErrorMessage("更新产品失败");
                }
            } else {
                //没有ID号，说明是插入新产品
                int rowCount = productMapper.insertSelective(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccessMessage("创建产品成功");
                } else {
                    return ServerResponse.createByErrorMessage("创建产品失败");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("请传入合法的产品信息，现在传入的产品信息不正确");
        }
    }

    public ServerResponse updateProductStatus(Integer productId, Integer status) {
        if (productId != null && status != null) {
            Product product = new Product();
            product.setId(productId);
            product.setStatus(status);

            int rowCount = productMapper.updateByPrimaryKeySelective(product);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("成功更新产品售卖状态");
            } else {
                return ServerResponse.createByErrorMessage("更新产品售卖状态失败");
            }
        } else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
    }


    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        } else {
            Product product = productMapper.selectByPrimaryKey(productId);
            if (product == null) {
                return ServerResponse.createByErrorMessage("查询不到该产品，产品已经下架或者已经删除");
            } else {
                ProductDetailVo productDetailVo = assembleProductDetailVo(product);
                return ServerResponse.createBySuccess(productDetailVo);
            }
        }
    }

    public ServerResponse<PageInfo> getProductList(Integer pageNum, Integer pageSize) {
        if (pageNum == null && pageSize == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        } else {
            //我们通过pageHelper这个插件来实现列表自动分页的功能
            //第一步我们直接PageHelper.startPage(pageNum, pageSize)
            //第二步我们就直接通过mapper对象进行查询，返回一个List
            //第三步我们new出来一个PageInfo对象，并且构造的时候，传入list
            //我们最终就需要这个pageInfo对象
            PageHelper.startPage(pageNum, pageSize);
            List<Product> productList = productMapper.selectProducts();

//            List<ProductListVo> productListVoList = new ArrayList<>();
            List<ProductListVo> productListVoList = Lists.newArrayList();
            for (Product item : productList
            ) {
                ProductListVo productListVo = assembleProductListVo(item);
                productListVoList.add(productListVo);
            }

            PageInfo pageInfo = new PageInfo(productListVoList);
            return ServerResponse.createBySuccess(pageInfo);
            //其他的代码是这样的
//            PageInfo pageInfo1 = new PageInfo(productList);
//            pageInfo1.setList(productListVoList);
//            return ServerResponse.createBySuccess(pageInfo1);
        }
    }

    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize) {
        if (StringUtils.isBlank(productName) && productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        } else {
            PageHelper.startPage(pageNum, pageSize);
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
            List<Product> productList = productMapper.selectByNameAndId(productName, productId);

            List<ProductListVo> productListVoList = Lists.newArrayList();
            for (Product item : productList
            ) {
                ProductListVo productListVo = assembleProductListVo(item);
                productListVoList.add(productListVo);
            }

            PageInfo pageInfo = new PageInfo(productListVoList);
            return ServerResponse.createBySuccess(pageInfo);
        }
    }


    /**
     * 工具方法  把pojo对象同步到vo对象上
     */
    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();

        productDetailVo.setId(product.getId());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setSubtitle(product.getSubtitle());

        //imageHost
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));

        //parentCategoryId
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            productDetailVo.setParentCategoryId(0); //默认根节点
        } else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        //createTime
        //updateTime
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

        return productDetailVo;

    }

    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();

        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setName(product.getName());
        productListVo.setPrice(product.getPrice());
        productListVo.setStatus(product.getStatus());
        productListVo.setSubtitle(product.getSubtitle());

        return productListVo;
    }

}

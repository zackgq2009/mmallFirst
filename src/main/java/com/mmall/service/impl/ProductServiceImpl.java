package com.mmall.service.impl;

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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                //没有ID号，说明是保存新产品
                int rowCount = productMapper.updateByPrimaryKeySelective(product);
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


    public ServerResponse manageProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        } else {
            Product product = productMapper.selectByPrimaryKey(productId);
            if (product == null) {
                return ServerResponse.createByErrorMessage("查询不到该产品，产品已经下架或者已经删除");
            } else {
                ProductDetailVo productDetailVo = this.assembleProductDetailVo(product);
                return ServerResponse.createBySuccess(productDetailVo);
            }
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

}

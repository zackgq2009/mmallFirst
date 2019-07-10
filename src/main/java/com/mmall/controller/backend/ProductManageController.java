package com.mmall.controller.backend;


import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by Tsingkuo
 */
@RestController
@RequestMapping(value = "/manage/product/")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    @RequestMapping(value = "save.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product) {
        //保存商品的接口
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        } else if (iUserService.checkAdminRole(user).isSuccess()) {
            //校验是否是管理员
            //传入product对象，把该对象保存下来
            return iProductService.saveOrUpdateProduct(product);
        } else {
            return ServerResponse.createByErrorMessage("不是管理员在登录，无权进行操作");
        }
    }

    @RequestMapping(value = "set_sale_status.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setProductStatus(HttpSession session, Integer productId, Integer status) {
        //更新产品的售卖状态
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        } else if (iUserService.checkAdminRole(user).isSuccess()) {
            //校验是否是管理员
            //传入productId，Status来更新产品售卖的状态
            return iProductService.updateProductStatus(productId, status);
        } else {
            return ServerResponse.createByErrorMessage("不是管理员在登录，无权进行操作");
        }
    }

    @RequestMapping(value = "detail.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getProductDetail(HttpSession session, Integer productId) {
        //根据productId获取商品的详情
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        } else if (iUserService.checkAdminRole(user).isSuccess()) {
            //校验是否是管理员
            //传入productId，返回ProductDetailVo对象
            return iProductService.manageProductDetail(productId);
        } else {
            return ServerResponse.createByErrorMessage("不是管理员在登录，无权进行操作");
        }
    }

    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> getProductList(HttpSession httpSession, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        //根据pageNum以及pageSize来获取到一个产品列表
        User user = (User)httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        } else if (iUserService.checkAdminRole(user).isSuccess()) {
            //校验是否是管理员
            //通过pageHelper这个插件来实现产品列表的自动分页功能，它会在mybatis生成的sql语句的句尾添加limit  offset这个字段的值
            return iProductService.getProductList(pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("不是管理员在登录，无权进行操作");
        }
    }

    @RequestMapping(value = "search.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo> searchProduct(HttpSession httpSession, @RequestParam(value = "productName") String productName, @RequestParam(value = "productId") Integer productId, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
        //根据productName或者productId来搜索到相应的product
        User user = (User)httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        } else if (iUserService.checkAdminRole(user).isSuccess()) {
            //校验是否是管理员
            return iProductService.searchProduct(productName, productId, pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("不是管理员在登录，无权进行操作");
        }
    }

    @RequestMapping(value = "upload.do", method = RequestMethod.POST)
    @ResponseBody
    //关于file的name需要设置明确，因为我们通过postman进行测试的时候，文件也需要设置正确的key，这个key必须要跟file的name保持一致，否则无法上传文件，也就是无法获取到这个multipartFile
    public ServerResponse<Map> upload(HttpSession session, @RequestParam(value = "upload_file") MultipartFile file, HttpServletRequest request) {
        //根据multipartFile上传文件，并且上传的目录地址根绝servletContext来决定
        //最初因为返回一个String，包含上传文件的所有信息，host, path，文件名等，但发现这样不利于前端的使用，需要传递给前端分开的参数，便于前端使用
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        //校验是否是管理员
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        } else if (iUserService.checkAdminRole(user).isSuccess()) {
            /**
             *             //视频中的代码
             *             String path = request.getSession().getServletContext().getRealPath("upload");
             *             String targetFileName = iFileService.upload(file,path);
             *             String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
             *
             *             Map fileMap = Maps.newHashMap();
             *             fileMap.put("uri",targetFileName);
             *             fileMap.put("url",url);
             *             return ServerResponse.createBySuccess(fileMap);
             *             //方法最终返回一个map对象，其中包含url跟uri
             */
            //视频中的代码
          String path = request.getSession().getServletContext().getRealPath("upload");
          String targetFileName = iFileService.upload(file,path);
            if (StringUtils.isBlank(targetFileName)) {
                return ServerResponse.createByErrorMessage("上传文件失败");
            } else {
                String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;

                Map fileMap = Maps.newHashMap();
                fileMap.put("uri",targetFileName);
                fileMap.put("url",url);
                return ServerResponse.createBySuccess(fileMap);
            //方法最终返回一个map对象，其中包含url跟uri
            //业务逻辑是上传一个文件，返回文件名

//            String path = request.getSession().getServletContext().getRealPath("upload");
//            //System.out.println(path);
//            String fileName = iFileService.upload(file, path);
//            String host = PropertiesUtil.getProperty("ftp.server.http.prefix", "http://127.0.0.1/");
//
//            Map fileInfoMap = Maps.newHashMap();
//            fileInfoMap.put("host", host);
//            //todo 文件上传是通过这个path，但最终我们是否可以在host之后直接加入这个path找到文件目录？
//            fileInfoMap.put("path", path);
//            fileInfoMap.put("name", fileName);
//            //return ServerResponse.createBySuccess(fileName);
//            return ServerResponse.createBySuccess(fileInfoMap);
            }
        } else {
            return ServerResponse.createByErrorMessage("不是管理员在登录，无权进行操作");
        }
    }


    @RequestMapping(value = "richTextUpload.do", method = RequestMethod.POST)
    @ResponseBody
    public Map richTextUpload(HttpSession session, @RequestParam(value = "upload_file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        Map resultMap = Maps.newHashMap();
        //根据multipartFile上传文件，并且上传的目录地址根绝servletContext来决定
        //最初因为返回一个String，包含上传文件的所有信息，host, path，文件名等，但发现这样不利于前端的使用，需要传递给前端分开的参数，便于前端使用
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        //校验是否是管理员
        if (user == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "请登录管理员");
            return resultMap;
        } else if (iUserService.checkAdminRole(user).isSuccess()) {
            //业务代码
            //富文本中对于返回值有自己的要求，我们使用simditor， 所以按照simditor的要求进行返回
            /**
             * simditor的格式
             * {
             *   "success": true/false,
             *   "msg": "error message", # optional
             *   "file_path": "[real file path]"
             * }
             * 所以我们给富文本编辑器返回一个map格式的对象，如果的接口只针对simditor类型的前端插件有作用
             */
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file,path);
            if (StringUtils.isBlank(targetFileName)) {
                resultMap.put("success", false);
                resultMap.put("msg", "富文本中文件上传失败");
                return resultMap;
            } else {
                String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;

                resultMap.put("success", true);
                resultMap.put("msg", "富文本中文件上传成功");
                resultMap.put("file_path", url);
                //与前端的约定，需要在富文本上传文件成功时返回一个header类型
                response.addHeader("Access-Control-Allow-Headers", "X-File-Name");

                return resultMap;
            }
        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "无权限操作");
            return resultMap;
        }
    }


}

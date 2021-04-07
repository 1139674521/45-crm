package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.exceptions.ParamsException;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController extends BaseController {
    @Resource
    private UserService userService;
    /**
     * ⽤户登录
     * @param userName
     * @param userPwd
     * @return
     */
    @PostMapping("user/login")
    @ResponseBody
    public ResultInfo userLogin(String userName,String userPwd) {
        ResultInfo resultInfo = new ResultInfo();
        // 通过 try catch 捕获 Service 层抛出的异常
        try {
            // 调⽤Service层的登录⽅法，得到返回的⽤户对象
            UserModel userModel =userService.userLogin(userName,userPwd);
            /**
             * 登录成功后，有两种处理：
             * 1. 将⽤户的登录信息存⼊ Session （ 问题：重启服务器，Session 失效，客户端需要重复登录 ）
             * 2. 将⽤户信息返回给客户端，由客户端（Cookie）保存
             */
            // 将返回的UserModel对象设置到 ResultInfo 对象中
            resultInfo.setResult(userModel);
        } catch (ParamsException e) {// ⾃定义异常
            e.printStackTrace();
            // 设置状态码和提示信息
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());
        }catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("操作失败");
        }
        return resultInfo;
    }

    /**
     * ⽤户密码修改
     * @param request
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @return
     */
    @RequestMapping("user/updateUserPassword")
    @ResponseBody
    public ResultInfo updateUserPasswode(HttpServletRequest request,String oldPassword,String newPassword,String confirmPassword){
        ResultInfo resultInfo = new ResultInfo();
        try {
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            userService.updateUserPassword(userId,oldPassword,newPassword,confirmPassword);
        } catch (ParamsException e) {// ⾃定义异常
            e.printStackTrace();
            // 设置状态码和提示信息
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());
        }catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("操作失败");
        }
        return resultInfo;
    }

    @RequestMapping("user/toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }
}

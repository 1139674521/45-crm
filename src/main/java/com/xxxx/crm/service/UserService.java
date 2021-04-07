package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.Md5Util;
import com.xxxx.crm.utils.UserIDBase64;
import com.xxxx.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class UserService extends BaseService<User,Integer> {
    @Resource
    private UserMapper userMapper;

    /**
     * ⽤户登录
     * @param userName
     * @param userPwd
     * @return
     */
    public UserModel userLogin(String userName,String userPwd){
        // 1. 验证参数
        checkLoginParams(userName,userPwd);
        // 2. 根据⽤户名，查询⽤户对象
        User user =userMapper.queryUserByUserName(userName);
        // 3. 判断⽤户是否存在 (⽤户对象为空，记录不存在，⽅法结束)
        AssertUtil.isTrue(user == null,"用户不存在");
        // 4. ⽤户对象不为空（⽤户存在，校验密码。密码不正确，⽅法结束）
        checkLoginPwd(userPwd,user.getUserPwd());
        // 5. 密码正确（⽤户登录成功，返回⽤户的相关信息）
        return buildUserInfo(user);
    }


    /**
     * 构建返回的⽤户信息
     * @param user
     * @return
     */

    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }


    /**
     * 验证登录密码
     * @param userPwd 前台传递的密码
     * @param upwd 数据库中查询到的密码
     * @return
     */

    private void checkLoginPwd(String userPwd,String upwd) {
        userPwd = Md5Util.encode(userPwd);
        AssertUtil.isTrue(userPwd.equals(upwd),"用户密码不正确");

    }
    /**
     * 验证⽤户登录参数
     * @param userName
     * @param userPwd
     */

    private void checkLoginParams(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不能为空");
    }


    /**
     * ⽤户密码修改
     * 1. 参数校验
     * ⽤户ID：userId ⾮空 ⽤户对象必须存在
     * 原始密码：oldPassword ⾮空 与数据库中密⽂密码保持⼀致
     * 新密码：newPassword ⾮空 与原始密码不能相同
     * 确认密码：confirmPassword ⾮空 与新密码保持⼀致
     * 2. 设置⽤户新密码
     * 新密码进⾏加密处理
     * 3. 执⾏更新操作
     * 受影响的⾏数⼩于1，则表示修改失败
     *
     * 注：在对应的更新⽅法上，添加事务控制
     */
    public void updateUserPassword (Integer userId,String oldPassword,String newPassword,String confirmPassword){
        // 通过userId获取⽤户对象
        User user = userMapper.selectByPrimaryKey(userId);
        //参数校验
        checkPasswordParams(user,oldPassword,newPassword,confirmPassword);
        //设置用户新密码
        user.setUserPwd(Md5Util.encode(newPassword));
        //执行添加操作
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"密码修改失败");
    }

    private void checkPasswordParams(User user, String oldPassword, String newPassword, String confirmPassword) {
        AssertUtil.isTrue(user==null,"用户未登录或不存在");
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"请输入原始密码");
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPassword)),"原始密码不正确");
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"新密码不能为空");
        AssertUtil.isTrue(Md5Util.encode(newPassword).equals(user.getUserPwd()),"新密码不能和原始密码一致");
        AssertUtil.isTrue(StringUtils.isBlank(confirmPassword),"请输入确认密码");
        AssertUtil.isTrue(!confirmPassword.equals(newPassword),"两次密码输入不一致，请重新输入");
    }
}

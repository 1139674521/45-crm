layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    /**
     * ⽤户登录 表单提交
     */
    form.on("submit(login)", function(data){
        // 获取表单元素的值 （⽤户名 + 密码）
        var fieldData = data.field;
        // 判断参数是否为空
        if (fieldData.username == "undefined"||fieldData.username.trim() == ""){
            layer.msg("⽤户名称不能为空！");
            return false;
        }
        if (fieldData.password == "undefined"||fieldData.password.trim() == ""){
            layer.msg("⽤户密码不能为空！");
            return false;
        }
        $.ajax({
            type:"post",
            url:ctx+"/user/login",
            data:{
                userName:fieldData.username,
                userPwd:fieldData.password
            },
            dataType:"json",
            success:function (data){
                // 判断是否登录成功
                if (data.code==200){
                    layer.msg("登录成功",function () {
                        // 将⽤户信息存到cookie中
                        var result = data.result;
                        $.cookie("userIdStr",result.userIdStr);
                        $.cookie("userName",result.userName);
                        $.cookie("trueName",result.trueName);

                        window.location.href = ctx+"/main";
                    });
                }else {
                    layer.msg(data.msg);
                }
            }
        });
        // 阻⽌表单跳转
        return false;
    })

});
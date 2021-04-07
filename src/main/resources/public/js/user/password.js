layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);
    /**
     * ⽤户密码修改 表单提交
     */
    form.on("submit(saveBtn)", function(data) {
        // 获取表单元素的内容
        var fieldData = data.field;
        $.ajax({
            
        })
    })
});
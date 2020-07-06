$(function () {
    $("#sidebar-menu").simplerSidebar({
        opener: '#toggle-sidebar',
        sidebar: {
            align: 'left',
            width: 450
        }
    });

    $(document).keydown(function (event) {
        console.info("keydown");
        if (event.keyCode == 13 && $("#sidebar-menu").data("simplersidebar")=="active"){
            userLogin();
        }
    })

});



function userLogin() {
    var userName = $("#userName").val();
    var password = $("#password").val();
    if (password == null || password == "") {
        alert("密码必填！");
        return;
    }
    if (userName == null || userName == "") {
        alert("账号必填");
        return;
    }
    $.ajax({
        url:"/userLogin",
        type:"post",
        data:JSON.stringify({"userName":userName,"password":password}),
        dataType:"json",
        contentType:"application/json",
        success:function (data) {
            if (data.code == 100){
                window.location.href = "/";
            }else {
                alert(data.msg);
            }
        }
    })
}



$(".tag-link").click(function () {
    $(this).parent().siblings().find('.tag-link').removeClass("active");
    $(this).parent().addClass("tab-active");
    $(this).parent().siblings().removeClass("tab-active");
})



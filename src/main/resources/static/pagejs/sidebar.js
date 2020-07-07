$(function () {
    $("#sidebar-menu").simplerSidebar({
        opener: '#toggle-sidebar',
        sidebar: {
            align: 'left',
            width: 450
        }
    });

    $(document).keydown(function (event) {
        let cookieList = document.cookie;
        let sidebarMenu = document.getElementById("sidebar-menu");
        let simplersidebar = sidebarMenu.dataset.simplersidebar;
        let cookie = cookieList.split(";");
        let value = null;
        for (let i = 0;i < cookie.length;i++){
            let c = cookie[i].split("=");
            if (c[0] == "pjl-blog-token"){
                value = c[1];
            }
        }
        if (event.keyCode == 13 && value == null && simplersidebar == 'active'){
            console.info(document.cookie);
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



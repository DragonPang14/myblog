$(function () {
    $(document).keydown(function (event) {
        let value = getCookie("pjl-blog-token")
        if (event.keyCode == 13 && value == null) {
            console.info(document.cookie);
            userLogin();
        }
    })
})


function getCookie(cookieKey) {
    let cookieList = document.cookie;
    let cookie = cookieList.split(";");
    let value = null;
    for (let i = 0; i < cookie.length; i++) {
        let c = cookie[i].split("=");
        if (c[0] == cookieKey) {
            value = c[1];
            return value;
        }
    }
    return null;
}

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
        url: "/userLogin",
        type: "post",
        data: JSON.stringify({"userName": userName, "password": password}),
        dataType: "json",
        contentType: "application/json",
        success: function (data) {
            if (data.code == 100) {
                window.location.href = "/admin/adminIndex";
            } else {
                alert(data.msg);
            }
        }
    })
}
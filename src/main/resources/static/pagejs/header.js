function login() {
    $("#login").modal('show');
}

function userLogin() {
    var userName = $("#userName").val();
    var password = $("#password").val();
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


$(window).scroll(function () {
    if ($(document).scrollTop() > 450){
        $(".index-header").addClass("invert");
    }else {
        $(".index-header").removeClass("invert");
    }
})

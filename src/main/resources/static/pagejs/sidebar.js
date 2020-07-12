$(function () {
    $("#sidebar-menu").simplerSidebar({
        opener: '#toggle-sidebar',
        sidebar: {
            align: 'left',
            width: 450
        }
    });

    $(document).keydown(function (event) {
        let sidebarMenu = document.getElementById("sidebar-menu");
        let simplersidebar = sidebarMenu.dataset.simplersidebar;
        let value = getCookie("pjl-blog-token")
        if (event.keyCode == 13 && value == null && simplersidebar == 'active') {
            console.info(document.cookie);
            userLogin();
        }
    })

});

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
                window.location.href = "/";
            } else {
                alert(data.msg);
            }
        }
    })
}


$("#toggle-sidebar").click(function () {
    if (getCookie("pjl-blog-token") != null){
        getNotificationCount();
    }
});

$("#message-tab").click(function () {
    if (getCookie("pjl-blog-token") != null){
        getNotifications(1);
    }
})


$(".tag-link").click(function () {
    $(this).parent().siblings().find('.tag-link').removeClass("active");
    $(this).parent().addClass("tab-active");
    $(this).parent().siblings().removeClass("tab-active");
})

function getNotificationCount() {
    $.get({
        url: "/unReadNotifications",
        dataType: "json",
        success: function (data) {
            if (data.code == 100 && data.obj > 0) {
                $("#message-tab").find("span").text(data.obj);
            }
        }
    })
}

function getNotifications(page) {
    $("#spinner-sidebar").fadeIn();
    $("#unread-message-tab").empty();
    if (page == null || page == "") {
        page = 1;
    }
    $.get({
        url: "/notifications?page=" + page,
        dataType: "json",
        success: function (data) {
            console.info(data.obj);
            if (data.code == 100 && data.obj.pageList.length > 0) {
                let htmlStr = '';
                $.each(data.obj.pageList, function (index, notification) {
                    let date = new Date(notification.gmtCreate);
                    let year = date.getFullYear();
                    let month = date.getMonth() + 1;
                    let day = date.getDate();
                    if (notification.status == 1){
                        htmlStr += '<div class="notifications gray">';
                    } else {
                        htmlStr += '<div class="notifications">';
                    }
                    htmlStr += notification.sender.name + '&nbsp;&nbsp;' + notification.actionStr + '你的' + notification.targetTypeStr + '&nbsp;&nbsp;';
                    htmlStr += '<a href="/article/'+notification.targetId+'?isNotify=true">'+notification.targetContent+'</a>';
                    htmlStr += '<span class="gray float-right">'+year+'-'+month+'-'+day+'</span>';
                    htmlStr += '<div class="notification-content">';
                    htmlStr += notification.notiContent;
                    htmlStr += '</div>';
                    htmlStr += '</div>';
                });
                $("#unread-message-tab").append(htmlStr);
                if (data.obj.pages.length > 1){

                    $(".sidebar-page-nav").empty();
                    let pageHtml = '';
                    if (data.obj.showPre) {
                        let pre = data.obj.currentPage-1;
                        pageHtml += '<a href="javascript:getNotifications('+pre+')">PRE</a>';
                    }
                    $.each(data.obj.pages,function (index,page) {
                        pageHtml += '<a href="javascript:getNotifications('+page+')">'+page+'</a>';
                    });
                    if (data.obj.showNext) {
                        let next = data.obj.currentPage+1;
                        pageHtml += '<a href="javascript:getNotifications('+next+')">NEXT</a>';
                    }
                    $(".sidebar-page-nav").append(pageHtml);
                    $(".sidebar-page-nav").css("display","flex");
                }
            }
            $("#spinner-sidebar").fadeOut();
        }
    })

}


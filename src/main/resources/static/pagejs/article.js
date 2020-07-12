$(function () {
    var testView = editormd.markdownToHTML("text-md-view", {
        // markdown : "[TOC]\n### Hello world!\n## Heading 2", // Also, you can dynamic set Markdown text
        // htmlDecode : true,  // Enable / disable HTML tag encode.
        // htmlDecode : "style,script,iframe",  // Note: If enabled, you should filter some dangerous HTML tags for website security.
        toc: true,
        tocContainer: "#toc-container",
    });
    if (getQueryString("isNotify") != null) {
        $("body , html").animate({scrollTop: $(".reply-count").offset().top}, 300);   //300是所用时间
    }

})

window.onscroll = function (ev) {
    var top = document.getElementsByClassName("toc-wrapper")[0].getBoundingClientRect().top;
    if (top < 0) {
        $(".toc-wrapper").addClass("toc-fixed");
    }
    if ($(document).scrollTop() < 450) {
        $(".toc-wrapper").removeClass("toc-fixed");
    }
}

function moveReplyBox(id,receiverId) {
    $(".reply-close").css("display", "block");
    //更换commit按钮
    $("#commit-comment-reply").css("display", "block");
    $("#commit-article-reply").css("display", "none");
    $("#reply").insertAfter($("#comments-reply-content-" + id));
    $("#target_id").val(id);
    $("#receiver_id").val(receiverId);
}

function resetReplyBox(valueId,receiverId) {
    $(".reply-close").css("display", "none");
    //更换commit按钮
    $("#commit-article-reply").css("display", "block");
    $("#commit-comment-reply").css("display", "none");
    $("#reply").insertAfter($(".reply-cards"));

    $("#target_id").val(valueId);
    $("#receiver_id").val(receiverId);
}


function sendComment(articleId, type) {
    let content;
    content = $("#reply-edit-content").val();
    let name = $("input[name=name]").val();
    let mail = $("input[name=mail]").val();
    let blog = $("input[name=blog]").val();
    let targetId = $("#target_id").val();
    let receiverId = $("#receiver_id").val();
    $.ajax({
        url: "/sendComment",
        type: "post",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "articleId": articleId,
            "parentId": targetId,
            "receiverId":receiverId,
            "type": type,
            "content": content,
            "name": name,
            "mail": mail,
            "blog": blog
        }),
        success: function (data) {
            console.info(data);
            if (data.code == 1001) {
                alert("游客昵称和邮箱必填");
            } else if (data.code == 100 && type == 1) {
                window.location.reload();
            } else if (data.code == 100 && type == 2) {
                window.location.reload();
            } else {
                alert(data.msg);
            }
        }
    });
}

function getQueryString(name) {
    let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    let r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return decodeURIComponent(r[2]);
    }
    ;
    return null;
}
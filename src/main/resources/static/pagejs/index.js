var page = 1;

$(function () {

    getArticleList(getQueryString("tag"));

    getTags();

    $(".intro-title").fadeIn(1000);


})

$(window).scroll(function () {
    var top = document.getElementsByClassName("tags-wrapper")[0].getBoundingClientRect().top;
    if (top < 0){
        $(".tags-wrapper").addClass("toc-fixed");
    }
    if ($(document).scrollTop() < 350){
        $(".tags-wrapper").removeClass("toc-fixed");
    }

    var top1 = $(window).scrollTop();     //获取相对滚动条顶部的偏移

    if (top1>200) {      //当偏移大于200px时让图标淡入（css设置为隐藏）

        $(".top").fadeIn();

    }else{
        //当偏移小于200px时让图标淡出
        $(".top").fadeOut();
    }
})



function getArticleList(tag) {
    $("#spinner").css("display","block");
    var url = "/getArticleList?page="+page;
    if (tag != null){
        url += "&tag="+tag;
    }
    $.get({
        url:url,
        dataType:"json",
        success:function (data) {
            if(data.code == 100 && data.obj != null){
                var listHtml = "";
                $.each(data.obj.pageList,function (index,article) {
                    var date = new Date(article.gmtCreate);
                    var year = date.getFullYear();
                    var month = date.getMonth() + 1;
                    var day = date.getDate();
                    listHtml += '<article class="index-post">';
                    listHtml += '<a class="abstract-title" href="/article/'+article.id+'">';
                    listHtml += '<span>'+article.title+'</span>';
                    listHtml += '</a>';
                    listHtml += '<div class="abstract-content">';
                    listHtml += article.description;
                    listHtml += '</div>';
                    listHtml += '<div class="abstract-post-meta">';
                    listHtml += '<div class="abstract-date">';
                    listHtml += '<i class="zi zi_calendar pr-2"></i>';
                    listHtml += '<span class="abstract-time">'+year+'/'+month+'/'+day+'</span>';
                    listHtml += '</div>';
                    listHtml += '<div class="abstract-tags">';
                    $.each(article.tagList,function (index,tag) {
                        listHtml += '<a class="post-tag" href="/?tag='+tag.id+'"  >'+tag.tagName+'</a>\n';
                    });
                    listHtml += '</div>';
                    listHtml += '</div>';
                    listHtml += '</article>';
                });
                $(".main").append(listHtml);
                $(".spinner-border").css("display","none");
            }else if(data.obj == null){
                $(".spinner-border").css("display","none");
                alert("没有更多了！");
                $("#btn-page").fadeOut();
            }else {
                alert(data.msg);
            }
        }

    })
    
}

$(".top").click(function(){
    $("body , html").animate({scrollTop:0},300);   //300是所用时间
});

$("#btn-page").click(function () {
    page++;
    getArticleList(getQueryString("tag"));
})

function getTags() {
    $.ajax({
        url:"/getTags?used=1",
        type:"get",
        dataType: "json",
        success:function (data) {
            if (data.code == 100){
                var tagHtml = '';
                $.each(data.obj,function (index,tag) {
                    tagHtml += '<a class="post-tag" href="/?tag='+tag.id+'" id="'+tag.id+'">'+tag.tagName+'</a>\n'
                })
                $("#tag-content").append(tagHtml);
            }
        }
    })
}

function getQueryString(name) {
    let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    let r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return decodeURIComponent(r[2]);
    };
    return null;
}
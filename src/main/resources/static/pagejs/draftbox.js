let page = 1;


$(function () {
    getDraftBox();
});

function getDraftBox() {
    $("#spinner").css("display","block");
    $.get({
        url: "/getDraftBox?page="+page,
        dataType: "json",
        success: function (data) {
            if (data.code == 100 && data.obj != null) {
                let listHtml = '';
                $.each(data.obj.pageList,function (index,draft) {
                    let date = new Date(draft.gmtCreate);
                    let year = date.getFullYear();
                    let month = date.getMonth() + 1;
                    let day = date.getDate();
                    listHtml += '<article class="index-post">';
                    listHtml += '<a class="abstract-title" href="/publish/'+draft.id+'" id="'+draft.id+'">';
                    listHtml += '<span>'+draft.title+'</span>';
                    listHtml += '</a>';
                    listHtml += '<div class="abstract-post-meta">';
                    listHtml += '<div class="abstract-date">';
                    listHtml += '<i class="zi zi_calendar pr-2"></i>';
                    listHtml += '<span class="abstract-time">'+year+'/'+month+'/'+day+'</span>';
                    listHtml += '</div>';
                    listHtml += '<a class="gray n-font float-right" href="javascript:removeDraft('+draft.id+')">舍弃</a>';
                    listHtml += '</div>';
                    listHtml += '</article>';
                });
                $(".main").append(listHtml);
                $(".spinner-border").css("display","none");
                $(".load-more").css("display","block");
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

$("#btn-page").click(function () {
    page++;
    getDraftBox();
})

$("#removeAllDraft").click(function () {
   removeDraft();
});

function removeDraft(id){
    let url = "/removeDraft";
    if (id != null){
        url += "?id=" + id;
    }
    $.get({
        url:url,
        dataType:"json",
        success:function (data) {
            if(data.code == 100 ){
                if (id != null){
                    $("#"+id).parent('article').remove();
                } else {
                    $("article").remove();
                }
            }
        }
    })
}

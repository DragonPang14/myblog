$(function () {
    $("#sidebar-menu").simplerSidebar({
        opener: '#toggle-sidebar',
        sidebar: {
            align: 'left',
            width: 450
        }
    });

    getTimeLine();

});


function getTimeLine() {
    $.get({
        url:"/getTimeLine",
        dataType:"json",
        success:function (data) {
            if(data.code == 100 && data.obj != null){
                let timeLineHtml = '';
                timeLineHtml += '<div style="display: flex">';
                timeLineHtml += '<div class="total-archive">';
                timeLineHtml += 'TOTAL : ' + data.obj.length;
                timeLineHtml += '</div>';
                timeLineHtml += '</div>';
                let yearMap = new Map();
                $.each(data.obj,function (index,article) {
                    let contentHtml = '';
                    let date = new Date(article.gmtCreate);
                    let year = date.getFullYear();
                    let month = date.getMonth() + 1;
                    let day = date.getDate();
                    //time-line年份
                    if (!yearMap.has(year)) {
                        contentHtml = '<div class="archive-year">'+year+'</div>';
                    } else {
                        contentHtml = yearMap.get(year);
                    }
                    contentHtml += '<div class="archive-post-item">';
                    contentHtml += '<span class="archive-post-date">';
                    contentHtml += month + '/' + day;
                    contentHtml += '</span>';
                    contentHtml += '<a class="archive-post-title" href="/article/' + article.id + '">'+article.title+'</a>';
                    contentHtml += '</div>';
                    yearMap.set(year, contentHtml);
                })
                //按年份append
                $("#archive-content").append(timeLineHtml);
                for (year of yearMap.keys()){
                    let yearHtml = yearMap.get(year);
                    $("#archive-content").append(yearHtml);
                }
            }
        }
    })
}
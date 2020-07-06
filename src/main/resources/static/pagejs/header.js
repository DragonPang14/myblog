function login() {
    $("#sidebar-menu").simplerSidebar({
        opener: '#toggle-sidebar',
        sidebar: {
            align: 'left',
            width: 350
        }
    });
}


$(window).scroll(function () {
    if ($(document).scrollTop() > 450){
        $(".index-header").addClass("invert");
    }else {
        $(".index-header").removeClass("invert");
    }
})

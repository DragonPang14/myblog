$(window).scroll(function () {
    if ($(document).scrollTop() > 450) {
        $(".index-header").addClass("invert");
    } else {
        $(".index-header").removeClass("invert");
    }
})

$(function () {
    var testView = editormd.markdownToHTML("text-md-view", {
        // markdown : "[TOC]\n### Hello world!\n## Heading 2", // Also, you can dynamic set Markdown text
        // htmlDecode : true,  // Enable / disable HTML tag encode.
        // htmlDecode : "style,script,iframe",  // Note: If enabled, you should filter some dangerous HTML tags for website security.
        toc:true,
        tocContainer:"#toc-container",
    });
})

window.onscroll = function (ev) {
    var top = document.getElementsByClassName("toc-wrapper")[0].getBoundingClientRect().top;
    if (top < 0){
        $(".toc-wrapper").addClass("toc-fixed");
    }
    if ($(document).scrollTop() < 300){
        $(".toc-wrapper").removeClass("toc-fixed");
    }
}

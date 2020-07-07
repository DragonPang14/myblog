$(function () {
    var testView = editormd.markdownToHTML("text-md-view", {
        // markdown : "[TOC]\n### Hello world!\n## Heading 2", // Also, you can dynamic set Markdown text
        // htmlDecode : true,  // Enable / disable HTML tag encode.
        // htmlDecode : "style,script,iframe",  // Note: If enabled, you should filter some dangerous HTML tags for website security.
        toc:true,
        tocContainer:"#toc-container",
    });

    var replyEditor = editormd("reply-edit-box", {
        width: "90%",
        height: "300px",
        placeholder: "支持markdown语法",
        emoji: true,
        path: "/js/lib/"  // Autoload modules mode, codemirror, marked... dependents libs path
    });
})

window.onscroll = function (ev) {
    var top = document.getElementsByClassName("toc-wrapper")[0].getBoundingClientRect().top;
    if (top < 0){
        $(".toc-wrapper").addClass("toc-fixed");
    }
    if ($(document).scrollTop() < 450){
        $(".toc-wrapper").removeClass("toc-fixed");
    }
}

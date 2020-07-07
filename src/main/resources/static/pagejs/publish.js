$(function () {
    getTags();

    var editor = editormd("md-content", {
        width: "100%",
        height: "650px",
        placeholder: "支持markdown语法",
        imageUpload: true,
        imageFormats: ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
        imageUploadURL: "/uploadImage",
        saveHTMLToTextarea:true,
        emoji: true,
        toolbarIconTexts:{
            addSummary:"摘要"
        },
        toolbarHandlers:{
            addSummary : function() {
                editor.insertValue("<!--summary-->");
            }
        },
        lang:{
            toolbar:{
                addSummary:"Alt+/ 可以添加摘要分隔符"
            }
        },
        onload : function() {
            var keyMap = {
                "Alt-/": function() {
                    editor.insertValue("<!--summary-->");
                }
            };
            this.addKeyMap(keyMap);
        },
        path: "/js/lib/"  // Autoload modules mode, codemirror, marked... dependents libs path
    });
});


function saveTag() {
    var flag = true;
    $("#tag-modal .form-control").each(function () {
        if (!verifyInput($(this).val())){
            $(this).addClass("is-invalid").attr("placeholder","必填！");
            flag = false;
        }
    });
    console.info(flag);
    var name = $("#tagName").val();
    var remarks = $("#remarks").val();
    if (flag){
        $.ajax({
            url:"/saveTag",
            type:"post",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify({"tagName":name,"remarks":remarks}),
            success:function (data) {
                if (data.code == 100){
                    alert("创建成功，请重新进入页面使用新标签");
                    $("#tag-modal").modal('hide');
                }else {
                    alert(data.msg);
                }
            }
        })
    }
}

function getTags() {
    $("#tag-content").empty();
    $.ajax({
        url:"/getTags?used=0",
        type:"get",
        dataType: "json",
        success:function (data) {
            if (data.code == 100){
                var tagHtml = '';
                $.each(data.obj,function (index,tag) {
                    tagHtml += '<a class="unselect-tag m-1" href="javascript:;" id="'+tag.id+'">'+tag.tagName+'</a>\n'
                })
                $("#tag-content").append(tagHtml);
            }
        }
    })
}


function publishWithMd() {
    let articleId = $("#article-id").val();
    let title = $("#title").val();
    let description = $("textarea[name='md-content-html-code']").val();
    let content = $("#content").val();
    let tag = '';
    let type = 1;
    if($("#about-me-check").is(":checked")){
        type = 2;
    }
    $(".selected-tag").each(function () {
        tag += $(this).data("tag-id");
        tag += ',';
    });
    if (title == null || title == "") {
        alert("请输入标题！");
        return;
    }
    if (content == null || content == "") {
        alert("请输入描述！");
        return;
    }
    if (tag == '' && type == 1){
        alert("请选择标签！");
        return;
    }
    $.ajax({
        url: "/doPublish",
        dataType: "json",
        type: "post",
        contentType: "application/json",
        data: JSON.stringify({"id":articleId,"title": title,"description":description,"content": content,"tag":tag,"type":type}),
        success: function (data) {
            if (data.code == 100) {
                window.location.href = "/";
            } else {
                alert(data.msg);
            }
        }
    });
}

function verifyInput(value) {
    if(value == null||value == ""){
        return false;
    }else {
        return true;
    }
}

var bind_search_event = function () {
    var search_input = document.querySelector('#search-tag')
    search_input.addEventListener('input', function (evt) {
        var input = evt.target.value
        var search_input_trim = input.trim()
        if (search_input_trim && search_input_trim != '') {
            $('.unselect-tag').hide()
        }
        $('.unselect-tag').each(function () {
            var tag_text = this.text.toLowerCase()
            if (tag_text.includes(search_input_trim.toLowerCase())) {
                this.style.display = ''
            }
        })
    })
}

var template_tag = function (tag_text,tag_id) {
    var tag = `
        <a class="btn btn-light btn-sm selected-tag mr-2" data-tag-id="${tag_id}" href="javascript:;">
            ${tag_text}
            <span class="ml-2">✗</span>
        </a>
    `
    return tag
}

var check_tag_limit = function () {
    var add_tag_btn = document.querySelector('#dropdown-button')
    var p = add_tag_btn.parentNode
    ++p.dataset.tagNums
    var max = p.dataset.tagMaxNum
    var tag_nums = p.dataset.tagNums
    if (tag_nums >= max) {
        add_tag_btn.disabled = true
    }
}

var hide_tag = function (tag) {
    tag.hidden = true
    tag.classList.remove('unselect-tag')
    tag.classList.add('hidden-tag')
}

var show_tag = function (id) {
    $('.hidden-tag').each(function () {
        if (this.id == id) {
            this.classList.add('unselect-tag')
            this.classList.remove('hidden-tag')
            this.hidden = false
        }
    })
}

var bind_tag_click_event = function() {
    $('#tag-content').click(function (e) {
        if (e.target.classList.contains('unselect-tag')){
            var tag_text = e.target.text
            var tag_id = e.target.id
            var tag = template_tag(tag_text,tag_id)
            var add_tag_btn = document.querySelector('#dropdown-button')
            add_tag_btn.insertAdjacentHTML('beforeBegin', tag)
            bind_tag_close_event()
            check_tag_limit()
            hide_tag(e.target)
        }
    })
}

var bind_tag_close_event = function(){
    $('.ml-2').click(function (e) {
        var p = e.target.parentNode
        --p.parentNode.dataset.tagNums
        var add_tag_btn = document.querySelector('#dropdown-button')
        add_tag_btn.disabled = false
        var id = p.dataset.tagId
        show_tag(id)
        p.remove()
    })
}


bind_search_event()
bind_tag_click_event()

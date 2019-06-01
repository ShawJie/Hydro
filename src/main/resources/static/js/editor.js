let markdown = {
    data: function () {
        return {
            imageInsertModal: undefined,
            markdownEditor: null
        }
    },
    props: {
        needWrap: Boolean,
        content: String,
        messageInsert: String,
        messageInsertLink: String,
        messageInsertUpload: String,
        messageInsertButton: String
    },
    template: '<div>' +
        '<textarea id="page-content">{{content}}</textarea>' +
        '<div class="ui mini modal image-insert">' +
        '<div class="header">{{messageInsert}}</div>' +
        '<div class="content">' +
        '<div class="ui form">' +
        '<div class="field">' +
        '<div class="ui radio checkbox">' +
        '<input type="radio" name="insertType" value="0" class="hidden">' +
        '<label>{{messageInsertLink}}</label></div></div>' +
        '<div class="field">' +
        '<div class="ui radio checkbox">' +
        '<input type="radio" name="insertType" value="1" class="hidden">' +
        '<label>{{messageInsertUpload}}</label></div>' +
        '<input type="file" class="hide" id="file_chooser" accept="image/png,image/jpg,image/jpeg" @change="fileSelected"></div>' +
        '<div class="field">' +
        '<div class="ui green button" @click="imageInsert">{{messageInsertButton}}</div>' +
        '</div></div></div></div></div>',
    created: function () {
        $.ajax("https://cdn.bootcss.com/simplemde/1.11.2/simplemde.min.js", {
            type: "GET",
            success: this.initial,
            dataType: "script",
            cache: true
        });
    },
    methods: {
        initial: function(){
            this.imageInsertModal = document.getElementsByClassName('image-insert')[0];
            $(document.querySelectorAll('.image-insert .ui.radio')).checkbox();

            let toolbar = ["bold", "italic", "heading", "|", "quote", "code", "unordered-list", "ordered-list", "|", "link", {
                name: 'image',
                action: editor => {
                    $(this.imageInsertModal).modal('show');
                },
                className: 'fa fa-picture-o',
                title: 'Insert image'
            }, "|", "table", "preview", "side-by-side", "fullscreen", "guide"];

            if (this.needWrap) {
                toolbar.splice(3, 0, {
                    name: 'wrap',
                    action: editor => {
                        let cm = editor.codemirror;
                        let selectedText = cm.getSelection();

                        cm.replaceSelection(selectedText + '<br>');
                    },
                    className: 'fa fa-level-down',
                    title: "Wrap"
                });
            }

            this.markdownEditor = new SimpleMDE({
                el: document.getElementById('page-content'),
                toolbar: toolbar,
                renderingConfig: {
                    singleLineBreaks: this.needWrap
                }
            });
        },
        imageInsert: function () {
            let cm = this.markdownEditor.codemirror;
            let selectedText = cm.getSelection();
            let value = document.querySelector('.image-insert input[type="radio"]:checked').value;
            switch (value) {
                case "0":
                    cm.replaceSelection(selectedText + '![](http://)');
                    $(this.imageInsertModal).modal('hide');
                    break;
                case "1":
                    document.getElementById('file_chooser').click();
                    break;
                default:
                    $(this.imageInsertModal).modal('hide');
                    break;
            }
        },
        fileSelected: function ($event) {
            let fileChooser = $event.target;
            let _self = this;
            if (fileChooser.files && fileChooser.files[0]) {
                let fileName = fileChooser.files[0].name.match(/^(\S+)\./)[1];

                let data = new FormData();
                data.append("fileName", fileName);
                data.append('mediaFile', fileChooser.files[0]);

                $.ajax("/admin/media/save", {
                    type: 'put',
                    data: data,
                    processData: false,
                    contentType: false
                }).then(function (responseData) {
                    if(responseData.success){
                        let imageData = responseData.data;
                        let cm = _self.markdownEditor.codemirror;
                        let selectedText = cm.getSelection();
                        cm.replaceSelection(selectedText + `![](/media/${imageData.filePath})`);
                        $(_self.imageInsertModal).modal('hide');
                    }else{
                        window.noty('error', responseData.msg, {timeout: 5000});
                    }
                })
            }
        },
        getResult: function () {
            return this.markdownEditor.value();
        },
        getRenderResult: function () {
            return this.markdownEditor.markdown(this.markdownEditor.value());
        }
    }
}
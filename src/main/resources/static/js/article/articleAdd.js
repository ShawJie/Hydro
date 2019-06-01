var articleAddVm = new Vue({
    el: document.getElementsByClassName('main-layout')[0],
    components: {
        "markdown-editor": markdown
    },
    data: {
        article: {
            id: null,
            title: '',
            tagList: [],
            categoryId: null,
            publicise: false
        },
        selectedTag: [],
        newTag: '',
        newCategory: '',
    },
    mounted: function () {
        let _self = this;

        this.fianlHead = '<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/github-markdown-css/2.10.0/github-markdown.min.css" />';

        if (articleInfo != null){
            for(let field in this.article){
                this.article[field] = articleInfo[field];
            }

            this.article.tagList.forEach(function(e){
                _self.selectedTag.push(e.id.toString());
            });
        }

        $('.ui.accordion').accordion();
        $("#tags").dropdown({placeholder: window._message.pickup_tags});
        $("#tags").dropdown('set selected', this.selectedTag);
        $('#category').dropdown({clearable: true, placeholder: window._message.pickup_category});
        $("#category").dropdown('set selected', this.article.categoryId);

        $('.private').checkbox({ onChange: function () {
           _self.article.publicise = !_self.article.publicise;
        }});
    },
    methods: {
        checkForSubmit: function($callback, ask = true){
            var _self = this;
            if(this.article.title != ''){
                if(ask){
                    window.noty("alert", `<i class="chess knight icon"></i>${window._message.publish_ask}`, {
                        layout: 'bottomRight',
                        confirm:{
                            yes:function () {
                                $callback();
                            },
                            no:function () {

                            }
                        }
                    });
                }else{
                    $callback();
                }
            }else{
                window.noty('warning', window._message.title_empty, {
                    layout: 'bottomRight',
                    timeout: 3000
                });
            }
        },
        publish: function () {
            var htmlContent = this.$refs.pageEditor.getRenderResult();
            var container = document.createElement("div");
            container.className = 'markdown-body';
            container.innerHTML = htmlContent;

            var data = $.extend(true, {
                markdownContent: this.$refs.pageEditor.getResult(),
                htmlContent: this.fianlHead + container.outerHTML,
            }, this.article);

            this.convertJsonFormat(data, 'tagList');

            var _self = this;
            $.ajax('/admin/article/publish', {
                type: 'put',
                data: data,
                dataType: 'json'
            }).then(function(response){
                if(response.success){
                    for(var field in _self.article){
                        _self.article[field] = response.data[field];
                    }

                    window.noty("success", response.msg, {
                        timeout: 8000,
                        layout: "bottomRight"
                    });
                }
            });
        },
        review: function () {
            let htmlContent = this.$refs.pageEditor.getRenderResult();
            let container = document.createElement("div");
            container.className = 'markdown-body';
            container.innerHTML = htmlContent;

            let data = $.extend(true, {
                htmlContent: this.fianlHead + container.outerHTML,
                categoryText: this.article.categoryId ? $('#category').dropdown('get item', this.article.categoryId).text() : ''
            }, this.article);

            this.convertJsonFormat(data, 'tagList');

            $.post("/admin/article/review", data).then(function (responseData) {
                if(responseData.success){
                    const review = window.open("/admin/article/review/0");
                    window.addEventListener('message', function (e){
                        if (e.data == 'ready') {
                            review.postMessage(responseData.data, window.location.origin);
                        }
                    });
                }else{
                    // do nothing
                }
            });
        },
        convertJsonFormat: function (data, target) {
            data[target].forEach(function (ele, index) {
                for(var item in ele){
                    data[`${target}[${index}].${item}`] = ele[item];
                }
            });

            delete data[target];
        },
        addTag: function () {
            var _self = this;
            $.ajax({
                url: "/admin/article/tag/save",
                type: 'put',
                datatype: 'json',
                data: {
                    tagName: _self.newTag,
                }
            }).then(function (responseData) {
                if(responseData.success){
                    let data = responseData.data;
                    window.noty('success', responseData.msg, {timeout: 5000});
                    var newTag = document.createElement('option');
                    newTag.value = data.id;
                    newTag.innerHTML = data.tagName;
                    $('#tags').append(newTag);
                } else{
                    window.noty('warning', responseData.msg);
                }
            });
        },
        addCategory: function () {
            var _self = this;
            $.ajax({
                url: "/admin/article/category/save",
                type: 'put',
                datatype: 'json',
                data: {
                    categoryName: _self.newCategory,
                }
            }).then(function (responseData) {
                if(responseData.success){
                    let data = responseData.data;
                    window.noty('success', responseData.msg, {timeout: 5000});
                    var newCategory = document.createElement('option');
                    newCategory.value = data.id;
                    newCategory.innerHTML = data.categoryName;
                    $('#category').append(newCategory);
                } else{
                    window.noty('warning', responseData.msg);
                }
            });
        }
    },
    watch: {
        selectedTag: function (val, oldVal) {
            this.article.tagList.length = 0;
            var _self = this;
            val.forEach(function(e){
                _self.article.tagList.push({
                    id: Number(e),
                    tagName: $('#tags').dropdown('get item', e).text(),
                    description: ''
                });
            });
        }
    }
});
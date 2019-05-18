var tagListVm = new Vue({
    el: document.getElementsByClassName('main-layout')[0],
    components: {
        'paginate': VuejsPaginate
    },
    data: {
        tagList: [],
        tag: {
            id: null,
            tagName: '',
            description: ''
        },
        paramName: '',
        editTag: {},
        modal: null,
        totalCount: 0
    },
    mounted: function () {
        this.getTags();
        this.initialModal();
    },
    methods: {
        checkForSubmit: function(target){
            if(target.tg == ''){
                window.noty('warning', window._message.name_empty, {timeout: 3000, layout: 'bottomRight'});
                return;
            }
            this.saveCategory(target);
        },
        saveTag: function(data){
            var _self = this;
            $.ajax('/admin/article/tag/save', {
                type: 'put',
                dataType: 'json',
                data: data
            }).then(function (response) {
                if(response.success){
                    window.noty("success", response.msg, {
                        timeout: 5000
                    });
                    _self.getTags();
                }else{
                    window.noty('warning', response.msg);
                }
            });
        },
        getTags: function (pageIndex = 1, pageSize = 10) {
            var _self = this;
            $.get("/admin/article/tag/list", {
                pageIndex: pageIndex,
                pageSize: pageSize,
                tagName: _self.paramName
            }).then(function (responseData) {
                if(responseData.success){
                    let pageModel = responseData.data;
                    _self.tagList = pageModel.data;
                    _self.totalCount = pageModel.count;
                }
            });
        },
        initialModal: function () {
            var _self = this;
            this.modal = {
                target: '.ui.modal',
                options: {
                    closable: false,
                    onHidden: function () {
                        _self.editTag = {};
                    }
                },
                show: function () {
                    $(this.target).modal(this.options).modal('show');
                },
                hide: function () {
                    $(this.target).modal('hide');
                }
            }
        },
        editModal: function (tagItem) {
            this.editTag = tagItem;
            this.modal.show();
        },
        removeTag: function (tagId) {
            var _self = this;
            window.noty("alert", `<i class="chess knight icon"></i>${window._message.delete_ask}`, {
                layout: 'bottomRight',
                confirm:{
                    yes:function () {
                        $.ajax(`/admin/article/tag/${tagId}`, {
                            type: 'delete',
                            datatype: 'json',
                        }).then(function (response) {
                            if(response.success){
                                window.noty('success', response.msg, {timeout: 3500});
                                _self.getTags();
                            }else{
                                window.noty("warning", response.msg);
                            }
                        })
                    },
                    no:function () {

                    }
                }
            });
        },
        paginationCallback: function (pageNum) {
            this.getTags(pageNum);
        }
    },
    computed: {
        pageCount: function () {
            return this.totalCount / 10;
        }
    }
});
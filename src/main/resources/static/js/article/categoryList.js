var categoryListVm = new Vue({
    el: document.getElementsByClassName('main-layout')[0],
    components: {
        'paginate': VuejsPaginate
    },
    data: {
        categoryList: [],
        category: {
            id: null,
            categoryName: '',
            description: ''
        },
        paramName: '',
        editCategory: {},
        modal: null,
        totalCount: 0
    },
    mounted: function () {
        this.getCategories();
        this.initialModal();
    },
    methods: {
        checkForSubmit: function(target){
            if(target.categoryName == ''){
                window.noty('warning', window._message.name_empty, {timeout: 3000, layout: 'bottomRight'});
                return;
            }
            this.saveCategory(target);
        },
        saveCategory: function(data){
            var _self = this;
            $.ajax('/admin/article/category/save', {
                type: 'put',
                dataType: 'json',
                data: data
            }).then(function (response) {
                if(response.success){
                    window.noty("success", response.msg, {
                        timeout: 5000
                    });
                    _self.getCategories();
                }else{
                    window.noty('warning', response.msg);
                }
            });
        },
        getCategories: function (pageIndex = 1, pageSize = 10) {
            var _self = this;
            $.get("/admin/article/category/list", {
                pageIndex: pageIndex,
                pageSize: pageSize,
                categoryName: _self.paramName
            }).then(function (responseData) {
                if(responseData.success){
                    let pageModel = responseData.data;
                    _self.categoryList = pageModel.data;
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
                        _self.editCategory = {};
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
        editModal: function (categoryItem) {
            this.editCategory = categoryItem;
            this.modal.show();
        },
        removeCategory: function (categoryId) {
            var _self = this;
            window.noty("alert", `<i class="chess knight icon"></i>${window._message.delete_ask}`, {
                layout: 'bottomRight',
                confirm:{
                    yes:function () {
                        $.ajax(`/admin/article/category/${categoryId}`, {
                            type: 'delete',
                            datatype: 'json',
                        }).then(function (response) {
                            if(response.success){
                                window.noty('success', response.msg, {timeout: 3500});
                                _self.getCategories();
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
            this.getCategories(pageNum);
        }
    },
    computed: {
        pageCount: function () {
            return this.totalCount / 10;
        }
    }
});
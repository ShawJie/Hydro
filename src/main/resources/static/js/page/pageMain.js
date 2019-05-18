var pageMainVm = new Vue({
    el: document.getElementsByClassName('main-layout')[0],
    data: {
        pageList: []
    },
    mounted: function () {
        this.listCustomPage();
    },
    methods: {
        listCustomPage: function () {
            let _self = this;
            $.post("/admin/page").then((responseData) => {
               if(responseData.success){
                   _self.pageList = responseData.data;
               }
            });
        },
        removePage: function (customPage) {
            let _self = this;
            window.noty('alert', window._message.delete_ask, {
                layout: 'bottomRight',
                confirm: {
                    yes: function () {
                        $.ajax("/admin/page/remove", {
                            type: 'delete',
                            data: {
                                pageId: customPage.id
                            }
                        }).then(function (responseData) {
                           if(responseData.success){
                               window.noty('success', responseData.msg, {timeout: 5000});
                               let index = _self.pageList.findIndex((e) => {
                                    e.id == customPage.id;
                               });

                               _self.pageList.splice(index, 1);
                           }else{
                               window.noty('error', responseData.msg);
                           }
                        });
                    },
                    no: function () {

                    }
                }
            })
        }
    }
});
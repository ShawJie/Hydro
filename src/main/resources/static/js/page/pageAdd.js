var pageAddVm = new Vue({
    el: document.getElementsByClassName('main-layout')[0],
    components: {
        "markdown-editor": markdown
    },
    data: {
        customPage: {
            id: null,
            pageName: '',
            routePath: '',
        },
        webSitePath: '',
        routePathExists: false,
    },
    mounted: function () {
        let _self = this;
        $('.ui.calendar').calendar({
            type: 'date',
            onChange: function (date, text, mode) {
                _self.customPage.releaseDate = date;
            }
        });

        if(customPage != null){
            for(let field in this.customPage){
                this.customPage[field] = customPage[field];
            }
            this.customPage.releaseDate = customPage.releaseDate;
            $(".ui.calendar").calendar('set date', this.customPage.releaseDate, true, false);
        }

        this.webSitePath = window.location.origin;
    },
    methods: {
        replaceSpecialChar: function () {
            this.customPage.routePath = this.customPage.routePath.replace(/[&\|\\\*^%$#@\s\-]/g, "-");
            let _self = this;
            $.get("/admin/page/exists", {
                routePath: this.customPage.routePath,
                id: this.customPage.id
            }).then(responseData => {
                _self.routePathExists = responseData;
            });
        },
        checkForSubmit: function (publishState) {
            let _self = this;

            let havePass = true;
            if(this.customPage.pageName == ''){
                window.noty('error', window._message.pageName_empty);
                havePass = false;
            }
            if(this.customPage.routePath == ''){
                window.noty('error', window._message.routePath_empty);
                havePass = false;
            }
            if(!havePass)
                return;
            if(publishState){
               window.noty('alert', window._message.publish_ask, {
                   confirm: {
                       yes: function () {
                           _self.postCustomPage(publishState);
                       },
                       no: function () {

                       }
                   },
                   layout: 'bottomRight'
               })
            }else{
                _self.postCustomPage(publishState)
            }
        },
        postCustomPage: function (publishState) {
            let data = $.extend({}, this.customPage, {
                customPageContent: this.$refs.pageEditor.getRenderResult(),
                publish: publishState
            });

            let _self = this;
            $.ajax("/admin/page/add", {
                type: 'put',
                data: data
            }).then((responseData) => {
                if(responseData.success){
                    window.noty('success', responseData.msg, {timeout: 5000});
                    _self.customPage = responseData.data;
                }
            });
        },
        reviewPage: function () {
            let pageName;
            if((pageName = this.customPage.pageName) == ''){
                window.noty('error', window._message.pageName_empty);
                return;
            }

            $.post("/admin/page/review", {pageName: pageName,
                customPageContent: this.$refs.pageEditor.getRenderResult()
            }).then((responseData) => {
                const review = window.open("/admin/page/review");
                window.addEventListener('message', function (e){
                    if (e.data == 'ready') {
                        review.postMessage(responseData.data, window.location.origin);
                    }
                });
            });
        },
    },
});
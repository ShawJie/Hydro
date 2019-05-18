var themeVm = new Vue({
    el: document.getElementsByClassName('main-layout')[0],
    data: {
        themeList: [],
        themeArchiveFile: null,
        archiveFileInfo: {},
        progressBar: null,
        theme: {}
    },
    mounted: function () {
        this.initialModal();
        this.listTheme();
        this.progressBar = $('.upload-progress-bar');
        this.progressBar.progress({
            value: 0
        });
    },
    methods: {
        listTheme: function () {
            let _self = this;
            $.get("/admin/theme/list").then((ResponseData)=>{
                if(ResponseData.success){
                    _self.themeList = ResponseData.data;
                }
            });
        },
        initialModal: function () {
            let _self = this;
            this.installModal = {
                target: '.ui.modal.install',
                options: {
                    closable: false,
                    onHidden: function () {
                        _self.themeArchiveFile = null;
                        _self.archiveFileInfo = {};
                        _self.progressBar.progress({
                            value: 0
                        });
                    }
                },
                show: function () {
                    $(this.target).modal(this.options).modal('show');
                },
                hide: function () {
                    $(this.target).modal('hide');
                }
            }

            this.optionModal = {
                target: '.ui.modal.option',
                options: {
                    closable: false,
                    onHidden: function () {
                        _self.theme = {};
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
        openChooser: function() {
            document.querySelector('#zip-chooser').click();
        },
        haveChosen: function($event) {
            let target = $event.target;
            if (target.files && target.files[0]) {
                this.progressBar.progress({
                    value: 0,
                });

                this.themeArchiveFile = target.files[0];
                this.archiveFileInfo = {
                    fileName: target.files[0].name,
                }
            }
        },
        uploadTheme: function () {
            let _self = this;

            let data = new FormData();
            data.append('themeArchive', this.themeArchiveFile);
            $.ajax('/admin/theme/install', {
                data: data,
                contentType: false,
                processData: false,
                type: 'POST',
                xhr: function () {
                    let xhr = $.ajaxSettings.xhr();
                    if(xhr.upload){
                        xhr.upload.addEventListener('progress', (e) => {
                            if (e.lengthComputable) {
                                let percent = Math.floor(e.loaded/e.total*100);
                                _self.progressBar.progress({
                                    value: percent,
                                    total: 100,
                                    text: {
                                        success: 'Upload Finished'
                                    }
                                });
                            }
                        }, false)
                    }
                    return xhr;
                }
            }).then(function (responseData) {
                if(responseData.success){
                    window.noty('success', responseData.msg, {timeout: 3000});
                    _self.theme = responseData.data;
                    _self.themeList.unshift(responseData.data);
                    _self.optionModal.show();
                }else{
                    window.noty('error', responseData.msg.replace(/\r\n/g, '</br>'));
                }
            });
        },
        applyTheme: function (theme) {
            let _self = this;
            $.post("/admin/theme/apply", theme).then((responseData)=>{
                if(responseData.success){
                    window.noty('success', responseData.msg, {timeout: 5000});
                    _self.listTheme();
                }else{
                    window.noty('error', responseData.msg);
                }
            });
        },
        removeTheme: function (theme) {
            let _self = this;
            window.noty("alert", `${window._message.deleteAsk}${theme.themeName}`, {
                layout: 'bottomRight',
                confirm:{
                    yes: function () {
                        $.ajax("/admin/theme/remove", {
                            data: theme,
                            type: 'delete'
                        }).then((responseData)=>{
                            if(responseData.success){
                                window.noty('success', responseData.msg, {timeout: 5000});
                                _self.listTheme();
                            }else{
                                window.noty('error', responseData.msg);
                            }
                        });
                    },
                    no: function () {
                        
                    }
                }
            })
        },
        optionModalShower: function (theme) {
            this.theme = theme;
            this.optionModal.show();
        },
        saveOptions: function () {
            let optionMap = {};
            for(let key in this.theme.option){
                optionMap[key] = JSON.stringify({
                    textName: this.themeOptionMap[key].textName,
                    value: document.querySelector(`.option-set-part input[name="${key}"]`).value
                });
            }
            $.post("/admin/theme/saveOption", {
                themePath: this.theme.themePath,
                option: optionMap
            }).then(function (responseData) {
               if(responseData.success){
                   window.noty('success', responseData.msg, {timeout: 5000});
               }
            });
        }
    },
    computed: {
        renderingImage: function () {
            if ($.isEmptyObject(this.theme)){
                return "";
            }
            return '/themes/' + this.theme.themePath + this.theme.rendering;
        },
        themeOptionMap: function () {
            if(!$.isEmptyObject(this.theme) && !$.isEmptyObject(this.theme.option)){
                let map = {};
                for(let item in this.theme.option){
                    map[item] = JSON.parse(this.theme.option[item]);
                }
                return map;
            }
            return {};
        }
    },
});
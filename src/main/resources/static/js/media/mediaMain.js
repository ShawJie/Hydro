var mediaVm = new Vue({
    el: document.getElementsByClassName('main-layout')[0],
    data: {
        mediaList: [],
        selectAll: false,
        selectedItem: [],
        media: {
            fileName: '',
            filePath: '',
            description: '',
        },
        tempData: {},
        reviewPath: '',
        searchKey: '',
        editMode: false,
        totalCount: 0
    },
    mounted: function () {
        this.waterfallWatcher(document.querySelector(".media-container"));
        this.msnry = new Masonry(".media-container", {
            itemSelector: ".media-item"
        });

        this.initialModal();
        this.listMediaItem();
        $(".ui.checkbox").checkbox();
    },
    methods: {
        initialModal: function () {
            let _self = this;
            this.addModal = {
                target: '.ui.modal.add-modal',
                options: {
                    closable: false,
                    onHidden: function () {
                        for(let item in _self.media){
                            _self.media[item] = '';
                        }
                        _self.reviewPath = '';
                    }
                },
                show: function () {
                    $(this.target).modal(this.options).modal('show');
                },
                hide: function () {
                    $(this.target).modal('hide');
                }
            };

            this.editModal = {
                target: '.ui.modal.edit-modal',
                options: {
                    onHidden: function () {
                        _self.tempData = {};
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
        openEditModal: function(target){
            this.tempData = $.extend({}, target);
            this.tempData.style = { backgroundImage: "url('/media/" + this.tempData.filePath + "')"};
            this.tempData.clipPath = window.location.host + '/media/' + target.filePath;
            this.editModal.show();
        },
        fileChooserOpen: function(){
            document.querySelector('#file_chooser').click();
        },
        fileChoosed: function ($event) {
            let fileChooser = $event.target;
            let _self = this;
            if (fileChooser.files && fileChooser.files[0]) {
                let reader = new FileReader();
                reader.onload = function(e) {
                    _self.reviewPath = e.target.result;
                };
                reader.readAsDataURL(fileChooser.files[0]);
                this.mediaFile = fileChooser.files[0];
                this.media.fileName = fileChooser.files[0].name.match(/^(\S+)\./)[1];
            }
        },
        checkForSubmit: function(){
            if(this.mediaFile == null){
                window.noty('alert', window._message.fileRequire, { timeout: 5000});
                return;
            }else if(this.media.fileName == ''){
                window.noty('alert', window._message.nameRequire, { timeout: 3000});
                return;
            }
            this.saveMedia();
        },
        saveMedia: function () {
            let data = new FormData();
            let _self = this;
            for(let field in this.media){
                data.append(field, this.media[field]);
            }
            data.append('mediaFile', this.mediaFile);
            $.ajax("/admin/media/save", {
                type: 'put',
                data: data,
                processData: false,
                contentType: false
            }).then(function (responseData) {
                if(responseData.success){
                    window.noty('success', responseData.msg, {timeout: 5000});
                    _self.mediaList.unshift(responseData.data);
                    _self.addModal.hide();
                }else{
                    window.noty('error', responseData.msg, {timeout: 5000});
                }
            })
        },
        deleteMedia: function(){
            if(this.selectedItem.length == 0){
                return;
            }
            let _self = this;
            window.noty("alert", window._message.deleteAsk, {
                layout: 'bottomRight',
                confirm:{
                    yes:function () {
                        $.ajax("/admin/media/delete", {
                            data: {
                                mediaIds: _self.selectedItem
                            },
                            type: 'delete',
                            dataType: 'json',
                        }).then(function (responseData) {
                            if(responseData.success){
                                window.noty('success', responseData.msg, {timeout: 5000});
                                for(let i = 0; i < _self.mediaList.length; i++){
                                    if(_self.selectedItem.find(id => id == _self.mediaList[i].id) != -1){
                                        _self.splice(i, 1);
                                    }
                                }
                                _self.selectedItem.length = 0;
                            }
                        });
                    },
                    no:function () {

                    }
                }
            });
        },
        listMediaItem: function (skipCount = 0, loadCount = 10) {
            let _self = this;
            $.get("/admin/media/list", {
                skipCount: skipCount,
                loadCount: loadCount,
                fileName: _self.searchKey
            }).then((responseData) => {
                if(responseData.success){
                    let pageInfo = responseData.data;
                    _self.mediaList = _self.mediaList.concat(pageInfo.data);
                    _self.totalCount = pageInfo.count;
                }
            });
        },
        copyToClipboard: function () {
            let input = document.querySelector(".clip-path>input");
            input.select();
            try{
                if(document.execCommand("Copy","false",null)){
                    window.noty('alert', '复制成功', {timeout: 3000});
                }else{
                    window.noty('alert', '复制失败', {timeout: 3000});
                }
            }catch(err){
                window.noty('error', '复制错误', {timeout: 3000});
            }
        },
        toggleAll: function () {
            if(this.selectAll){
                this.selectedItem = [];
            }else{
                let _self = this;
                this.mediaList.forEach((ele)=>{
                    _self.selectedItem.push(ele.id);
                })
            }
        },
        saveEdit: function () {
            if(this.tempData.fileName == ''){
                window.noty('alert', window._message.nameRequire, { timeout: 3000});
                return;
            }
            this.editMode = false;
            let _self = this;
            $.post("/admin/media/update", {
                id: _self.tempData.id,
                fileName: _self.tempData.fileName,
                description: _self.tempData.description
            }).then((responseData)=>{
                if(responseData.success){
                    window.noty('success', responseData.msg, { timeout: 3000});
                    _self.mediaList.forEach(t => {
                        if(t.id == responseData.data.id){
                            t.fileName = responseData.data.fileName;
                            t.description = responseData.data.description;
                        }
                    });
                }
            });
        },
        waterfallWatcher: function (target) {
            let _self = this;
            function mutationResolver(mutationList) {
                mutationList.forEach((mutation) => {
                    if(mutation.addedNodes.length > 0){
                        _self.msnry.addItems(mutation.addedNodes);
                    }
                    if(mutation.removedNodes.length > 0){
                        _self.msnry.remove(mutation.removedNodes);
                    }
                });
                return new Promise(function (resolve, reject) {
                    setTimeout(function () {
                        resolve();
                    }, 250);
                });
            }

            function resetWaterFall(mutationList){
                mutationResolver(mutationList).then(() => {
                    _self.msnry.layout();
                });
            }

            let observer = new MutationObserver(resetWaterFall);
            observer.observe(target, {
               childList: true,
               subtree: true
            });
        },
        nextPage: function () {
            let skipCount = this.mediaList.length;

            this.listMediaItem(skipCount);
        }
    },
    watch: {
        selectedItem: function (val, oldVal) {
            if(val.length == this.mediaList.length){
                this.selectAll = true;
            }else{
                this.selectAll = false;
            }
        }
    }
});
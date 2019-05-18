var settingVm = new Vue({
    el: document.getElementsByClassName("main-layout")[0],
    data: {
        systemSetting: {},
        systemSettingClone: {},
        customSetting: {},
        ownerInfo: {},
        avatorTip: false,
        customModal: null,
        lazyLoadCustom: false,
        customSet: {
            id: null,
            itemName: "",
            itemValue: "",
        }
    },
    mounted: function () {
        let _self = this;
        $.get("/admin/setting/defaultSetting").then(responseData => {
            let data = responseData.data;
            _self.systemSetting = data.settings;
            _self.ownerInfo = data.ownerInfo;
        });
        this.initialCustomModal();
    },
    methods: {
        initialCustomModal: function () {
            let _self = this;
            this.customModal = {
                target: '.ui.modal',
                options: {
                    closable: false,
                    onHidden: function () {
                    }
                },
                show: function () {
                    if(!_self.lazyLoadCustom){
                        _self.lazyLoadCustom = true;
                        $.get("/admin/setting/customSetting").then((responseData) => {
                            _self.customSetting = responseData.data;
                        });
                    }
                    $(this.target).modal(this.options).modal('show');
                },
                hide: function () {
                    $(this.target).modal('hide');
                }
            }
        },
        checkForSubmit: function(){
            let pass = true;
            if(this.ownerInfo.userName == ''){
                window.noty('warning', window._message.username_empty, {timeout: 5000});
                pass = false;
            }
            if(this.ownerInfo.avator == ''){
                window.noty('warning', window._message.avator_empty, {timeout: 5000});
                pass = false;
            }
            if(this.ownerInfo.password != null && this.ownerInfo.password.length < 6){
                window.noty('warning', window._message.password_less_six, {timeout: 5000});
                pass = false;
            }
            if(pass){
                this.saveMainSetting();
            }
            return;
        },
        saveMainSetting: function () {
            let _self =this;
            let systemSettingChange = [];
            for(let e in this.systemSetting){
                if(_self.systemSetting[e].itemValue != _self.systemSettingClone[e]){
                    systemSettingChange.push({
                        id: _self.systemSetting[e].id,
                        itemName: e,
                        itemValue: _self.systemSettingClone[e]
                    });
                }
            }

            let data = $.extend({}, {
                settings: systemSettingChange,
            }, this.ownerInfo);

            this.convertJsonFormat(data, 'settings');

            $.post("/admin/setting/updateMainSetting", data).then((responseData) => {
                if(responseData.success){
                    window.noty("success", responseData.msg, {timeout: 5000});
                }
            });
        },
        appendToForm: function(setting){
            this.customSet = $.extend({}, setting);
            this.customSet.origin = setting.itemName;
        },
        addCustomSet: function(){
            let _self = this;
            if(this.customSet.itemName == ''){
                window.noty('warning', window._message.key_empty, {timeout: 5000});
                return;
            }
            $.ajax("/admin/setting/custom", {
                type: 'PUT',
                data: _self.customSet
            }).then(function (responseData) {
                if(responseData.success){
                    _self.$set(_self.customSetting, responseData.data.itemName, responseData.data);
                    _self.resetCustomSet();
                    window.noty("success", responseData.msg, {timeout: 5000});
                }else{
                    window.noty("error", responseData.msg, {timeout: 5000});
                }
            });
        },
        editCustomSet: function(){
            let _self = this;
            let origin = this.customSet.origin;
            delete this.customSet.origin;
            $.ajax("/admin/setting/custom", {
                type: 'POST',
                data: _self.customSet
            }).then(function (responseData) {
                if(responseData.success){
                    _self.$delete(_self.customSetting, origin);
                    _self.$set(_self.customSetting, responseData.data.itemName, responseData.data);
                    _self.resetCustomSet();
                    window.noty("success", responseData.msg, {timeout: 5000});
                }else{
                    window.noty("error", responseData.msg, {timeout: 5000});
                }
            });
        },
        deleteCustomSet: function(setting){
            let _self = this;
            $.ajax("/admin/setting/custom", {
                type: 'delete',
                data: {settingId: setting.id}
            }).then(function (responseData) {
                if(responseData.success){
                    if(responseData.data != null){
                        _self.$delete(_self.customSetting, responseData.data.itemName);
                    }
                    window.noty("success", responseData.msg, {timeout: 5000});
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
        resetCustomSet: function () {
            this.customSet.itemName = "";
            this.customSet.itemValue = "";
            this.customSet.id = null;
            delete this.customSet.origin;
        }
    },
    computed: {
        headImg: function () {
            if(this.ownerInfo.avator == undefined){
                return "";
            }
            if (this.ownerInfo.avator.startsWith('/')){
                return window.location.origin + this.ownerInfo.avator;
            }else{
                let webSiteRegex = /^((http:\/{2})|(https:\/{2}))?([a-zA-Z0-9]([a-zA-Z0-9\-]{0,61}[a-zA-Z0-9])?\.)+[a-zA-Z]{2,6}(\/)/
                if (webSiteRegex.test(this.ownerInfo.avator)){
                    return this.ownerInfo.avator;
                }
            }
            return "";
        }
    },
    watch: {
        systemSetting: function (val, oldVal) {
            for(let key in this.systemSetting){
                this.systemSettingClone[key] = this.systemSetting[key].itemValue;
            }
        },
        customSet: {
            deep: true,
            handler: function (val, oldVal) {
                if(val.itemName == '' && val.itemValue == ''){
                    val.id = null;
                    delete val.origin;
                }
            }
        }
    }
});
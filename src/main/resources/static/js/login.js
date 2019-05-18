var loginVm = new Vue({
    el: document.getElementsByClassName('wapper')[0],
    data: {
        account: '',
        password: '',
        isDone: true
    },
    mounted: function () {
        $(".fields").form({
            on: 'blur',
            fields:{
                account: 'empty',
                password: ['empty', 'minLength[6]']
            }
        });
    },
    methods: {
        doLogin: function () {
            if(this.isDone && this.check()){
                this.isDone = false;
                var _self = this;
                $.ajax('/admin/login', {
                    type: 'post',
                    data: {
                        account: _self.account,
                        password: _self.password
                    },
                    success:function (responseData) {
                        if(responseData.success){
                            window.noty('success', responseData.msg, {
                                timeout: 1500,
                                after: function () {
                                    window.location.href = "/admin";
                                }
                            });
                        }else{
                            window.noty('error', responseData.msg);
                        }
                        _self.isDone = true;
                    }
                });
            }
        },
        check: function () {
            if(!$(".fields").form('validate field','account')){
                window.noty('warning', window._message.account_empty, {timeout: 3000});
                return false;
            }else if(!$(".fields").form('validate field','password')){
                window.noty('warning', window._message.password_wrong, {timeout: 3000});
                return false;
            }
            return true;
        }
    }
});
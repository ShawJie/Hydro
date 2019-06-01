var loginVm = new Vue({
    el: document.getElementsByClassName('wapper')[0],
    data: {
        account: '',
        password: '',
        isDone: true,
        form: undefined,
    },
    mounted: function () {
        this.form = document.getElementsByClassName('fields')[0];

        let _self = this;
        $(this.form).form({
            on: 'blur',
            fields:{
                account: {
                    identifier: 'account',
                    rules: [{
                        type: 'empty',
                        prompt: 'account_empty'
                    }]
                },
                password: {
                    identifier: 'password',
                    rules: [{
                        type: 'empty',
                        prompt: 'password_empty'
                    }, {
                        type: 'minLength[6]',
                        prompt: 'password_less_charset'
                    }]
                }
            },
            onFailure: _self.check
        });
    },
    methods: {
        doLogin: function () {
            if(this.isDone && $(this.form).form('validate form')[0] == undefined){
                this.isDone = false;
                let _self = this;
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
        check: function (formErrors, fields) {
            formErrors.forEach(e => {
                window.noty('warning', window._message[e], {timeout: 3000});
            });
        }
    }
});
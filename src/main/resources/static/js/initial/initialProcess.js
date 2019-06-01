var initialInfoVm = new Vue({
    el: '.info-content',
    data: {
        user: {
            account: '',
            password: '',
            userName: '',
            email: '',
            github: '',
        },
        currentStep: 0,
        accountInfo: null,
        userInfo: null
    },
    mounted: function(){
        this.accountInfo = document.getElementsByClassName('account-info')[0];
        this.userInfo = document.getElementsByClassName('user-info')[0];

        let _self = this;
        $(this.accountInfo).form({
            fields: {
                account: {
                    identifier: 'account',
                    rules: [{
                        type: 'empty',
                        prompt: 'account_empty'
                    }, {
                        type: 'minLength[5]',
                        prompt: "account_less_charset"
                    }],
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
            onFailure: _self.validate
        });

        $(this.userInfo).form({
            fields: {
                username: {
                    identifier: 'username',
                    rules: [{
                        type: 'empty',
                        prompt: 'username_empty'
                    }]
                }
            },
            onFailure: _self.validate
        });
    },
    methods: {
        nextStep: function () {
            let result;
            switch (this.currentStep) {
                case 0:
                    result = $(this.accountInfo).form("validate form")[0];
                    if(result == undefined){
                        this.currentStep ++;
                    }
                    break;
                case 1:
                    result = $(this.userInfo).form("validate form")[0];
                    if(result == undefined){
                        this.currentStep ++;
                    }
                    break;
                default:
                    break;
            }
        },
        prevStep: function () {
            this.currentStep --;
        },
        validate: function (formErrors, fields) {
            formErrors.forEach(e => {
                new Noty({
                    theme: 'sunset',
                    type: 'error',
                    text: message[e],
                    timeout: 4000
                }).show();
            });
        },
        submit: function () {
            let data = $.extend({}, this.user);
            $.ajax("/initial/submit", {
                type: 'put',
                data: data
            }).then(responseData => {
                if(responseData.success){
                    window.location.pathname = "/";
                }
            });
        }
    }
});
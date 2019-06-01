var slideBarVm = new Vue({
    el: '.left-side-bar',
    data: {
        links: {},
        currentLink: null
    },
    mounted: function () {
        let currentPath = window.location.pathname;

        this.loadAllLink(this.$el);
        this.renderClass(currentPath);

        $(document).pjax('.left-side-bar a', 'section', { fragment:'section', timeout: 2000 });
        $(document).on('pjax:beforeSend', this.pjaxBeforeSend);
        $(document).on('pjax:complete', this.pjaxRequestComplete)
    },
    methods: {
        loadAllLink: function(parent){
            let parentNode = parent.children;
            let _self = this;
            for(let i = 0; i < parentNode.length; i++){
                let e = parentNode[i];
                if(e.classList.value == 'item'){
                    if(e.hasAttribute('href')){
                        let path = e.getAttribute('href');
                        this.links[path] = {
                            target: e,
                            parentNode: parent
                        };
                        e.onclick = function () {
                            _self.renderClass(path);
                        }
                    }
                    else{
                        let menu = e.querySelector('.menu');
                        menu.previousElementSibling.onclick = function(event){
                            _self.toggleMenu(event);
                        }
                        menu.style.display = 'none';
                        this.loadAllLink(menu);
                    }
                }
            }
        },
        renderClass: function (path) {
            if(this.currentLink != null){
                let oldTarget = this.links[this.currentLink].target;
                let classList = [];
                oldTarget.className.split(" ").forEach(e => {
                    if(e != "active"){
                        classList.push(e);
                    }
                });
                oldTarget.className = classList.join(" ");
            }
            let link = this.links[path];
            if(link != undefined){
                link.target.className += ' active';
                if(link.parentNode != this.$el && this.currentLink == null){
                    $(link.parentNode).transition('swing down', '500ms');
                }
                this.currentLink = path;
            }
        },
        toggleMenu: function (event) {
            let menu = event.target.nextElementSibling;
            if(menu != null){
                if(menu.className.split(" ").find(e => {return e == 'menu'})){
                    $(menu).transition('swing down', '500ms');
                }
            }
        },
        pjaxBeforeSend: function () {
            let dimmer = document.createElement('div');
            dimmer.className = 'dimmer';
            dimmer.innerHTML = "<div class='ui active centered inline loader'></div>";
            let beforeTarget = document.getElementsByTagName('section')[0];
            document.getElementsByTagName('body')[0].insertBefore(dimmer, beforeTarget);
        },
        pjaxRequestComplete: function (xhr) {
            window.hydro.loadScripts(xhr.delegateTarget.URL).then(function () {
                let dimmer = document.getElementsByClassName('dimmer')[0];
                $(dimmer).transition({
                    animation: 'slide down',
                    duration: '300ms',
                    onComplete: function () {
                        this.parentElement.removeChild(this);
                    }
                })
            });
        }
    }
});

var topBarVm = new Vue({
    el: '.top-bar',
    mounted: function () {
        $(document.getElementsByClassName('author-head')[0]).dropdown({on:'hover'});
    },
    methods: {
        jumpToSetting: function () {
            let link = document.querySelector('.left-side-bar a[href="/admin/setting"]');
            link.click();
        },
        logout: function () {
            window.location.pathname = "/admin/logout"
        }
    }
})
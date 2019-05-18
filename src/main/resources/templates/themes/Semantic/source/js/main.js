var seman_top_menu = {
    data: function(){
        return {
            isActive: false,
            lastScrollVal: 0
        }
    },
    props: {
        aboutPath: String
    },
    template: '<div class="top menu" :class="{active: isActive}">' +
        '<div class="ui basic segment" :class="{\'center aligned\': isActive}">' +
        '<div class="ui link list horizontal very relaxed">' +
        '<a class="item" href="/">HOME</a>' +
        '<a class="item" href="/posts">POST</a>' +
        '<a class="item" href="/archives">ARCHIVES</a>' +
        '<a class="item" :href="aboutPath">ABOUT ME</a>' +
        '</div></div></div>',
    created: function () {
        window.addEventListener('scroll', this.observerScroll);
    },
    methods: {
        observerScroll: function (event) {
            let outOfTop = window.scrollY;
            if(outOfTop > this.lastScrollVal || outOfTop == 0){
                this.isActive = false;
            }else{
                this.isActive = true;
            }
            this.lastScrollVal = outOfTop;
        }
    }
};

var asideVm = new Vue({
    el: 'aside',
    components: {
        "top-menu": seman_top_menu
    }
});
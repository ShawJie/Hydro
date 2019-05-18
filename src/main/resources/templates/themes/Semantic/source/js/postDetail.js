var postDetailVm = new Vue({
    el: '.post-content',
    data: {
        post: {
            category: {},
            tagList: []
        },
        monthText: ['壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖', '拾'],
    },
    mounted: function () {
        let pathName = window.location.pathname;
        if(/post\/\d+/.test(pathName)){
            let postId = pathName.match(/post\/(\d+)/)[1];
            this.getPostDetail(postId);
        }else{

        }
    },
    methods: {
        getPostDetail: function (postId) {
            let _self = this;
            $.get(`/api/article/${postId}`).then(responseData => {
                if(responseData.success){
                    _self.post = responseData.data;
                    _self.$nextTick(function () {
                        _self.renderHeaderId();
                        _self.initToc();
                    });

                    document.getElementsByTagName('title')[0].innerHTML = `${_self.post.title} - Hydro`;
                }else {
                    console.error("post not found");
                }
            });
        },
        renderHeaderId: function () {
            document.querySelectorAll('.markdown-body h1,h2,h3').forEach(function (ele, index) {
                ele.id = `_${index}`;
            });
        },
        initToc: function () {
            let sticky =  document.querySelector('.post-content .ui.sticky');
            $(sticky).sticky({
                context: '.post-info',
                offset: 175
            })

            tocbot.init({
                tocSelector: '.toc-container',
                contentSelector: '.markdown-body',
                orderedList: false,
                headingSelector: 'h1, h2, h3',
            });
        }
    },
    computed: {
        createDate: function () {
            let dateStr = '';
            if(this.post.createDate != null){
                let originDate = new Date(this.post.createDate);
                let month = originDate.getMonth();

                if(month + 1 < this.monthText.length){
                    dateStr += this.monthText[month];
                }else{
                    let lost = month % this.monthText.length;
                    dateStr += (this.monthText[month - lost - 1] + this.monthText[lost - 1]);
                }
                dateStr += `月 , ${originDate.getDate()} / ${originDate.getFullYear()}`;
            }
            return dateStr;
        }
    }
});
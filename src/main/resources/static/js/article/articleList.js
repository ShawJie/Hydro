var articleListVm = new Vue({
    el: document.getElementsByClassName('main-layout')[0],
    components: {
        'paginate': VuejsPaginate
    },
    data: {
        articleList: [],
        totalCount: 0,
        article: {
            title: '',
            tag: '',
            cid: '',
            createDate: null,
        },
        onLoad: false
    },
    mounted: function (){
        var _self = this;

        $('#createDate').calendar({
            type: 'date',
            onChange: function (date, text, mode) {
                _self.article.createDate = date;
            }
        });
        $('select').dropdown({ clearable: true });

        this.queryPath = '/admin/article/list';

        this.getArticles(1);
    },
    methods:{
        getArticles: function(pageIndex, pageSize = 10) {
            this.onLoad = true;
            this.articleList.length = 0;

            var _self = this;
            var data = {};
            for(var item in this.article){
                if(this.article[item] == '' || this.article[item] == null){
                    continue;
                }
                data[item] = this.article[item];
            }

            $.get(this.queryPath, $.extend({
                pageIndex: pageIndex,
                pageSize: pageSize
            }, data)).done(function (responseData) {
               if(responseData.success){
                   let value = responseData.data;
                   _self.articleList = value.data;
                   _self.totalCount = value.count;
               }
               _self.onLoad = false;
            });
        },
        paginationCallback: function (pageNum) {
            this.getArticles(pageNum);
        },
        review: function (articleId) {
            const review = window.open(`/admin/article/review/${articleId}`);
            window.addEventListener('message', e => {
                if (e.data == 'ready') {
                    review.postMessage('init', window.location.origin);
                }
            });
        },
        edit: function (articleId) {
            $.pjax({
                url:'/admin/article/edit/' + articleId,
                container:'section',
                fragment:'section',
                timeout: 3000
            });
        }
    },
    computed: {
        pageCount: function () {
            return this.totalCount / 10;
        }
    }
});
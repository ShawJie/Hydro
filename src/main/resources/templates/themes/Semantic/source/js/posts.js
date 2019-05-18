var postsVm = new Vue({
    el: 'main',
    components: {
        'paginate': VuejsPaginate
    },
    data: {
        tags: [],
        categories: [],
        postList: [],
        postAmount: 0,
        param: {
            tid: null,
            cid: null,
            title: null,
        }
    },
    mounted: function () {
        this.loadParam();

        this.initialAllData();
    },
    methods: {
        loadParam: function () {
            let pathName = window.location.href;
            let regex = /\?(.+)/;
            if (regex.test(pathName)) {
                let paramSet = pathName.match(/\?(.+)/)[1];
                let keyValMaps = paramSet.split("&")
                for (let index in keyValMaps) {
                    let keyVal = keyValMaps[index].split("=")
                    this.param[keyVal[0]] = keyVal[1];
                }
            }
        },
        initialAllData: function () {
            let _self = this;
            $.get("/api/tag_list").then(responseData => {
                if (responseData.success) {
                    _self.tags = _self.tags.concat(responseData.data);
                }
            });

            $.get("/api/category_list").then(responseData => {
                if (responseData.success) {
                    _self.categories = _self.categories.concat(responseData.data);
                }
            });

            this.listPost();
        },
        listPost: function (page = 1, pageSize = 10) {
            let _self = this;
            this.postList.length = 0;
            $.get("/api/article_list", {
                pageIndex: page,
                pageSize: pageSize,
                cid: _self.param.cid,
                tid: _self.param.tid,
                title: _self.param.title
            }).then(responseData => {
                if (responseData.success) {
                    let paginationData = responseData.data;
                    _self.postAmount = paginationData.count;
                    _self.postList = _self.postList.concat(paginationData.data);
                }
            });
        },
        paginationCallback: function (pageNum) {
            this.listPost(pageNum);
        },
        reSearch: function (key, val) {
            let paramStr = '?';
            for (let k in this.param) {
                if (k == key) {
                    if (this.param[key] == val) {
                        this.param[key] = null;
                    } else {
                        this.param[key] = val;
                    }
                }
                if (this.param[k] != null) {
                    paramStr += `${k}=${this.param[k]}&`;
                }
            }
            history.replaceState(this.param, "Posts - Hydro", `/posts${paramStr.substring(0, paramStr.length - 1)}`);
            this.listPost(1);
        }
    },
    computed: {
        pageCount: function () {
            return this.postAmount / 10;
        }
    }
});
var archivesVm = new Vue({
    el: 'main > .ui.container',
    data: {
        timeline: {},
        postData: [],
        postAmount: 0
    },
    mounted: function () {
        this.listPostArchive(1);
    },
    methods: {
        listPostArchive: function (pageIndex = 1, pageSize = 10) {
            let _self = this;
            $.get("/api/article_archives", {
                pageIndex: pageIndex,
                pageSize: pageSize
            }).then(responseData => {
                if(responseData.success){
                    let pageModel = responseData.data;
                    _self.postData = _self.postData.concat(pageModel.data);
                    _self.postAmount = pageModel.count;
                }
            });
        },
        formatDate: function (date) {
            return `${date.getMonth() + 1} / ${date.getDate()}`;
        }
    },
    watch: {
        postData: function (val, oldVal) {
            for(let i = oldVal.length; i < val.length; i++){
                let date = new Date(val[i].createDate);

                let yearLev = this.timeline[date.getFullYear()] ? this.timeline[date.getFullYear()] : (this.timeline[date.getFullYear()] = {});
                let monthDate = this.formatDate(date);
                if(!yearLev[monthDate]){
                    yearLev[monthDate] = new Array();
                }
                yearLev[monthDate].push(val[i]);
            }
        }
    }
})
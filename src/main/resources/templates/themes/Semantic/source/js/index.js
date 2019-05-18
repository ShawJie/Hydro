var bannerVm = new Vue({
	el: document.getElementsByClassName('banner')[0],
	data:{
		ownerInfo: {
			avator: '',
			userName: '',
			email: '',
			github: ''
		},
	},
	mounted: function () {
		this.loadOwnerInfo();
	},
	methods: {
		loadOwnerInfo: function () {
			let _self = this;
			$.get("/api/owner_info").then(responseData => {
				if(responseData.success){
					let data = responseData.data;
					for(let key in _self.ownerInfo){
						_self.ownerInfo[key] = data[key];
					}
				}
			});
		}
	}
});

var mainVm = new Vue({
	el: 'main',
	data: {
		postList: [],
		postAmount: 0,
		pageIndex: 1,
		monthText: ['壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖', '拾'],
		containerHeight: 0,
	},
	mounted: function () {
		this.listPost(this.pageIndex);
	},
	methods: {
		listPost: function (page = 1, pageSize = 10) {
			let _self = this;
			$.get("/api/article_list", {
				pageIndex: page,
				pageSize: pageSize
			}).then(responseData => {
				if (responseData.success) {
					let paginationData = responseData.data;
					_self.postAmount = paginationData.count;
					_self.postList = _self.postList.concat(paginationData.data);
				}
			});
		},
		nextPage: function(){
			this.pageIndex += 1;
			this.listPost(this.pageIndex);
		},
		jumpToPost: function(postId){
			window.location.pathname = `/post/${postId}`;
		},
		dateFormat: function (date) {
			let dateStr = '';

			let originDate = new Date(date);
			let month = originDate.getMonth();

			if(month + 1 < this.monthText.length){
				dateStr += this.monthText[month];
			}else{
				let lost = month % this.monthText.length;
				dateStr += (this.monthText[month - lost - 1] + this.monthText[lost - 1]);
			}
			dateStr += `月 , ${originDate.getDate()} / ${originDate.getFullYear()}`;
			return dateStr;
		}
	},
	watch: {
		postList: function () {
			this.$nextTick(function () {
				let height = document.querySelector('main>.post.container').offsetHeight;
				this.containerHeight = height + 'px';
			});
		}
	}
});
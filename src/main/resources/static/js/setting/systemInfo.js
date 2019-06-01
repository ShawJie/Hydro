var aboutVm = new Vue({
    el: document.getElementsByClassName('main-layout')[0],
    data: {
        socket: null,
        inProcess: false,
        version: null,
        message: null,
        step: '',
        remoteVersion: ''
    },
    mounted: function () {
        this.version = version;

        let host = window.location.host;
        this.socket = new WebSocket(`ws://${host}/admin/update`);
        this.socket.onmessage = this.receiveMessage;

        if(hasDecompressed){
            this.step = 'readyInstall';
            return;
        }

        let _self = this;
        this.socket.addEventListener("open", function (event) {
            _self.checkUpdate();
        });

        window.addEventListener('beforeunload', function () {
            _self.socket.close();
        });
    },
    methods: {
        checkUpdate: function () {
            this.sendData("check");
            this.step = 'check';
            this.inProcess = true;
        },
        downloadUpdate: function() {
            this.sendData("download");
            this.inProcess = true;
        },
        installUpdate: function(){
            this.sendData("install");
        },
        receiveMessage: function(event) {
            this.inProcess = false;
            let message = JSON.parse(event.data);
            let _self = this;
            switch (message.cmd) {
                case "check":
                    if(message.success){
                        this.remoteVersion = message.data;
                        this.step = 'hasUpdate'
                    }else{
                        this.step = 'upToDate';
                    }
                    break;
                case "download":
                    if(message.success){
                        this.step = 'downloading';
                        this.$nextTick(()=>{
                            this.downloadPrgress = document.getElementsByClassName('download')[0];
                        });
                    }else{
                        this.step = 'upToDate'
                    }
                    break;
                case "process":
                    if(message.success){
                        this.inProcess = true;
                        if(this.downloadPrgress){
                            $(this.downloadPrgress).progress({
                                total: 100,
                                value: message.data
                            });
                            if(message.data == 100){
                                this.inProcess = false;
                                this.step = 'decompressing';
                            }
                        }
                    }else{
                        window.noty('error', message.data);
                    }
                    break;
                case "decompress":
                    if(message.success){
                        this.step = 'readyInstall';
                    }
                    break;
            }
        },
        sendData: function (message) {
            this.socket.send(message);
        },
    },
    computed: {
        updateInfo: function () {
            let info;
            switch (this.step) {
                case "check":
                    info = window._message.check;
                    break;
                case "upToDate":
                    info = window._message.upToDate;
                    break;
                case "hasUpdate":
                    info = window._message.hasUpdate;
                    break;
                case "downloading":
                    info = window._message.downloading;
                    break;
                case "decompressing":
                    info = window._message.decompress;
                    break;
                case "readyInstall":
                    info = window._message.installReady;
                    break;
                default:
                    info = "";
            }
            return info;
        }
    }
});
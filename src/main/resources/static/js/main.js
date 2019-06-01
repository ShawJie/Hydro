window.noty = function (type, text, option) {
    option = option == undefined ? {} : option;
    let n = new Noty({
        theme: 'sunset',
        type: type,
        text: text,
        timeout: option.timeout ? option.timeout : false,
        callbacks:{
            afterClose: option.after
        },
        buttons: option.confirm ? [
            Noty.button('Yes', 'ui primary button', function () {
                option.confirm.yes();
                n.close();
            }),
            Noty.button('No', 'ui secondary button', function () {
                option.confirm.no();
                n.close();
            })
        ] : undefined,
        layout:option.layout ? option.layout : "topRight",
    });
    n.show();
};

window.hydro = (function () {
    const scriptLoadList = [
        {
            pathRegex:/admin\/$/,
            scripts:[]
        },
        {
            pathRegex:/admin\/article$/,
            scripts:[
                'https://cdn.bootcss.com/semantic-ui-calendar/0.0.8/calendar.min.js',
                'https://unpkg.com/vuejs-paginate@0.9.0',
                '/js/article/articleList.js'
            ]
        },
        {
            pathRegex: /admin\/article\/add$/,
            scripts:[
                '/js/editor.js',
                '/js/article/articleAdd.js'
            ]
        },
        {
            pathRegex:/admin\/article\/edit\/\d+$/,
            scripts:[
                '/js/editor.js',
                '/js/article/articleAdd.js'
            ]
        },
        {
            pathRegex:/admin\/article\/category$/,
            scripts:[
                'https://unpkg.com/vuejs-paginate@0.9.0',
                '/js/article/categoryList.js'
            ]
        },
        {
            pathRegex:/admin\/article\/tag$/,
            scripts:[
                'https://unpkg.com/vuejs-paginate@0.9.0',
                '/js/article/tagList.js'
            ]
        },
        {
            pathRegex:/admin\/media$/,
            scripts:[
                'https://unpkg.com/masonry-layout@4/dist/masonry.pkgd.js',
                '/js/media/mediaMain.js'
            ]
        },
        {
            pathRegex:/admin\/theme$/,
            scripts:[
                '/js/theme/themeMain.js'
            ]
        },
        {
            pathRegex: /admin\/setting$/,
            scripts: [
                '/js/setting/settingMain.js'
            ]
        },
        {
            pathRegex: /admin\/setting\/about$/,
            scripts: [
                '/js/setting/systemInfo.js'
            ]
        },
        {
            pathRegex: /admin\/page$/,
            scripts: [
                '/js/page/pageMain.js'
            ]
        },
        {
            pathRegex: /admin\/page\/add$/,
            scripts: [
                'https://cdn.bootcss.com/semantic-ui-calendar/0.0.8/calendar.min.js',
                '/js/editor.js',
                '/js/page/pageAdd.js'
            ]
        }
    ];
    function RecursiveLoad(scripts, index) {
        if(index < scripts.length){
            onDemandScript(scripts[index], function () {
                RecursiveLoad(scripts, index + 1);
            });
        }
    }
    function loadScripts(url) {
        scriptLoadList.every(function (value, index) {
            if (value.pathRegex.test(url)) {
                RecursiveLoad(value.scripts, 0);
                return false;
            }
            return true;
        });
        return new Promise(function (resolve, reject) {
            setTimeout(() => {
                resolve();
            }, 1000);
        });
    }

    function onDemandScript ( url, callback ) {
        callback = (typeof callback != 'undefined') ? callback : {};

        $.ajax(url, {
            type: "GET",
            success: callback,
            dataType: "script",
            cache: true
        });
    }

    return {
        loadScripts:function (url) {
            return loadScripts(url);
        }
    }
})();
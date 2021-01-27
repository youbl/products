const jsArr = [
    '/res/unpkg/vue.min.js',
    '/res/unpkg/axios.min.js',
    '/res/unpkg/elemeIndex.js',
    '/res/qs.min.js',
];
const cssArr = [
    '/res/unpkg/elemeIndex.css',
];

for (let i = 0, j = jsArr.length; i < j; i++) {
    let item = jsArr[i];
    document.write('<script type="text/javascript" src="' + item + '"></script>');
}

for (let i = 0, j = cssArr.length; i < j; i++) {
    let item = jsArr[i];
    document.write('<link rel="stylesheet" href="' + item + '">');
}
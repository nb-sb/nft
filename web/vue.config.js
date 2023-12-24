module.exports = {
    lintOnSave: false,
    transpileDependencies: [],
    publicPath: process.env.VUE_APP_OUTPUT_DIR,//生成目录的文件名。
    outputDir: process.env.VUE_APP_ROUTER_URL, //生产环境构建文件的目录
    assetsDir: '',//输出的静态资源目录名 没有默认放到根目录下  static
    productionSourceMap: false,
    devServer: {
        compress: true,
        hot: true, //热更新
        port: 8088
    },
}

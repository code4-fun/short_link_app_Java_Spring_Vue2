module.exports = {
    devServer: {
        disableHostCheck: true,
        https: true,
        port: 8081,
        host: '127.0.0.1'
    },
    css: {
        loaderOptions: {
            sass: {
                prependData: ' @import "@/assets/styles/styles.scss"; '
            }
        }
    }
}

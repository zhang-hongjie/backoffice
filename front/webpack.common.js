const path = require('path');
const webpack = require('webpack');
const htmlWebpackPlugin = require('html-webpack-plugin');
const extractTextPlugin = require('extract-text-webpack-plugin');
const autoprefixer = require('autoprefixer');
const sassMixins = __dirname + '/node_modules/compass-mixins/lib';

module.exports = {
    entry: {
        'polyfills': './src/polyfills.ts',
        'vendor': './src/vendor.ts',
        'app': './src/main.ts',
        'static' : './src/style/app.scss'
    },

    resolve: {
        extensions: ['', '.js', '.ts', '.scss']
    },

    module: {

        exprContextCritical: false,

        preLoaders: [
            {
                test: /\.ts$/,
                loader: 'tslint',
                exclude: absolutePathTo('node_modules')
            }
        ],

        loaders: [
            {
                test: /\.ts$/,
                loader: 'ts'
            },
            {
                test: /\.html$/,
                loader: 'html'
            },
            {
                test: /\.(png|jpe?g|gif|svg|woff|woff2|ttf|eot|ico)$/,
                loader: 'file?name=assets/[name].[hash].[ext]'
            },
            {
                test: /\.scss$/,
                loader: 'raw-loader!sass-loader?includePaths[]=' + sassMixins,
                exclude : /app\.scss$/
            },
            {
                test: /app\.scss$/,
                loader: extractTextPlugin.extract(['raw-loader', 'sass-loader?includePaths[]=' + sassMixins])
            }
        ]
    },

    plugins: [
        new webpack.optimize.CommonsChunkPlugin({
            name: ['app', 'vendor', 'polyfills']
        }),

        new htmlWebpackPlugin({
            template: 'src/public/index.html'
        })
    ],

    /**
     * PostCSS : Add vendor prefixes to your css
     * Reference: https://github.com/postcss/autoprefixer-core
     */
    postcss: [
        autoprefixer({
            browsers: ['last 2 version']
        })
    ],

    /**
     * Apply the tslint loader as pre/postLoader
     * Reference: https://github.com/wbuchwalter/tslint-loader
     */
    tslint: {
        emitErrors: false,
        failOnHint: false,
        resourcePath: 'src'
    }
};

function absolutePathTo(dir) {
    return path.resolve(__dirname, dir);
}

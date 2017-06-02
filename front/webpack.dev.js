const commonConfig = require('./webpack.common.js');
const path = require('path');
const webpack = require('webpack');
const webpackMerge = require('webpack-merge');
const extractTextPlugin = require('extract-text-webpack-plugin');
const sassLintPlugin = require('sasslint-webpack-plugin');

const ENV = process.env.NODE_ENV = process.env.ENV = 'development';

module.exports = webpackMerge(commonConfig, {

    debug: true,

    devtool: 'cheap-module-source-map',

    output: {
        path: absolutePathTo('dist'),
        publicPath: 'http://localhost:3000/',
        filename: '[name].js',
        sourceMapFilename: '[name].map',
        chunkFilename: '[id].chunk.js',
    },

    plugins: [
        new sassLintPlugin({
            configFile: './sass-lint.yml',
            context: ['./src'],
            ignoreFiles: [],
            ignorePlugins: ['extract-text-webpack-plugin'],
            glob: '**/*.s?(a|c)ss',
            quiet: false,
            failOnWarning: false,
            failOnError: false,
            testing: false
        }),
        new webpack.DefinePlugin({
            'process.env': {
                'ENV': JSON.stringify(ENV)
            }
        }),
        new extractTextPlugin('[name].css')
    ],

    devServer: {
        historyApiFallback: true,
        stats: 'minimal',
        proxy : {
            '/backoffice/login' : {
                target : "http://localhost:8080",
                secure: false
            },
            '/backoffice/front/api/*' : {
                target : "http://localhost:8080",
                secure: false
            },
            '/backoffice/front/assets/*': {
                pathRewrite: {'^/backoffice/front': ''},
                target: 'http://localhost:3000',
                secure: false
            }
        }
    }
});

function absolutePathTo(dir) {
    return path.resolve(__dirname, dir);
}

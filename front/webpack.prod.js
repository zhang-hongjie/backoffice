const commonConfig = require('./webpack.common.js');
const path = require('path');
const webpack = require('webpack');
const webpackMerge = require('webpack-merge');
const webpackMd5Hash = require('webpack-md5-hash');
const extractTextPlugin = require('extract-text-webpack-plugin');
const copyWebpackPlugin = require('copy-webpack-plugin');

const ENV = process.env.NODE_ENV = process.env.ENV = 'production';

module.exports = webpackMerge(commonConfig, {
    devtool: 'cheap-module-source-map',

    output: {
        path: absolutePathTo('dist'),
        publicPath: '',
        filename: '[name].[chunkhash].js',
        sourceMapFilename: '[name].[chunkhash].map',
        chunkFilename: '[id].[chunkhash].chunk.js'
    },

    htmlLoader: {
        minimize: false
    },

    plugins: [
        new webpack.DefinePlugin({
            'process.env': {
                'ENV': JSON.stringify(ENV)
            }
        }),

        new webpackMd5Hash(),
        new webpack.NoErrorsPlugin(),
        new webpack.optimize.DedupePlugin(),
        new webpack.optimize.UglifyJsPlugin({
            beautify: false,
            mangle: {
                screw_ie8: true,
                keep_fnames: true
            },
            compress: {
                screw_ie8: true
            },
            comments: false
        }),

        new copyWebpackPlugin([
            { from: absolutePathTo('src/public') },
            { from: absolutePathTo('assets'), to : 'assets' }
        ]),

        new extractTextPlugin('[name].[hash].css')
    ]
});

function absolutePathTo(dir) {
    return path.resolve(__dirname, dir);
}


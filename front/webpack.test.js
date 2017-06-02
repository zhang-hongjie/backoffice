const webpack = require('webpack');

const ENV = process.env.NODE_ENV = process.env.ENV = 'test';
const sassMixins = __dirname + '/node_modules/compass-mixins/lib';
const sassLintPlugin = require('sasslint-webpack-plugin');

module.exports = {
    devtool: 'inline-source-map',

    resolve: {
        extensions: ['', '.js', '.ts', '.scss']
    },

    module: {
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
                loader: 'null'
            },
            {
                test: /\.scss$/,
                loader: 'raw-loader!sass-loader?includePaths[]=' + sassMixins
            }
        ],
        postLoaders: [
          {
            test: /\.(js|ts)$/,
            exclude: [/[a-z]\.spec\.ts/, /(node_modules)\//],
            loader: 'istanbul-instrumenter-loader?force-sourcemap'
          }
        ]
    },

    plugins: [
        new sassLintPlugin({
            configFile: './sass-lint.yml',
            context: ['./src'],
            ignoreFiles: [],
            ignorePlugins: ['extract-text-webpack-plugin'],
            glob: '**/*.s?(a|c)ss',
            quiet: false,
            failOnWarning: true,
            failOnError: true,
            testing: false
        }),
        new webpack.DefinePlugin({
            'process.env': {
                'ENV': JSON.stringify(ENV)
            }
        })
    ]
};

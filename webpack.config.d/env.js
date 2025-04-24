// webpack.config.d/env.js
const webpack = require('webpack');

config.plugins = config.plugins || []
config.plugins.push(
    new webpack.DefinePlugin({
        // Add env vars to inject into react app here like so
        'process.env.URL': JSON.stringify(process.env.URL || '')
    })
);

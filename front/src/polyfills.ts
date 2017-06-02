import 'ts-helpers';
require('core-js/shim');
require('zone.js/dist/zone');

if (process.env.ENV === 'production') {
    // Production
} else {
    // Development
    require('zone.js/dist/long-stack-trace-zone');
}

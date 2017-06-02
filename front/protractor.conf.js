var ProtractorHTMLReporter  = require('protractor-html-reporter');
var path = require('path');
var childProcess = require('child_process');

exports.config = {
    seleniumServerJar: 'node_modules/protractor/node_modules/webdriver-manager/selenium/selenium-server-standalone-2.53.1.jar',
    troubleshoot: false,
    directConnect: true,
    chromeOnly: true,
    capabilities: {
        browserName: "chrome",
        chromeOptions: {
            args: ['--disable-extensions']
        },
    },
    specs: [
        'e2e/_login.js',
        'e2e/basic/*.e2e.js',
        'e2e/scenarii/*.e2e.js',
        'e2e/_logout.js'
    ],
    allScriptsTimeout: 60000,
    baseUrl: "http://172.19.31.20:8080/backoffice/front",
    framework: 'jasmine',
    onPrepare: function() {
        jasmine.getEnv().addReporter(new ProtractorHTMLReporter({
            path: 'reports/e2e'
        }));
        browser.driver.manage().window().setSize(1280, 1024);
    },
    useAllAngular2AppRoots: true,
    jasmineNodeOpts: {
        showColors: true,
        defaultTimeoutInterval: 60000,
        isVerbose: false,
        includeStackTrace: false
    }
};

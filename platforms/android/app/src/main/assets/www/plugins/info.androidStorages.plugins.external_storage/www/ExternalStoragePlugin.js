cordova.define("info.androidStorages.plugins.external_storage.ExternalStoragePlugin", function(require, exports, module) {
var exec = require('cordova/exec');

exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'ExternalStoragePlugin', 'coolMethod', [arg0]);
};

});

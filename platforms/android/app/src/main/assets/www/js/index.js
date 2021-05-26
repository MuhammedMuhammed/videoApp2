/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var app = {
    permissions: null,
    // Application Constructor
    initialize: function() {
        this.bindEvents();
 
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicitly call 'app.receivedEvent(...);'
    onDeviceReady: function() {

       /***
         * @mExternalDirectory getting Path that used by external storage
         *it has been checked in different devices assuming it may has differd directory name
         *according to directory a path to file would be created
         */
         app.permissions = cordova.plugins.permissions;
         let mExternalDirectory;
         let videoele = document.getElementById("VideoPlayer");      
       
        app.receivedEvent('deviceready');
    },
    // Update DOM on a Received Event
    receivedEvent: function(id) {
        try {
            // alert(id)
            
        function success(result)
        {
            alert(result)
        };
            Cordova.exec(success,null ,"ExternalStoragePlugin","coolMethod",[111,222]);

        } catch (error) {
        alert(error);            
        }
        console.log('Received Event: ' + id);
    },
    onFileSystemSuccess:function(fileSystem) {
        fileSystem.root.getDirectory("App_files", {create: false, exclusive: false}, app.onGetDirectoryWin, app.onGetDirectoryFail);
    },fail:function(evt) {
        console.log(evt.target.error.code);
    },onGetDirectoryWin : function(parent) {
alert(parent)
    },
    onGetDirectoryFail : function() {
        console.log("error getting dir")
    }
    , checkIfFileExists:function(path){
        window.requestFileSystem(LocalFileSystem.PERSISTENT, 0, function(fileSystem){
            fileSystem.root.getFile(path, { create: false }, this.fileExists, this.fileDoesNotExist);
        }, this.getFSFail); //of requestFileSystem
    },
    fileExists:function(fileEntry){
        alert("File " + " exists!");
    },
     fileDoesNotExist:function(){
        alert("file does not exist");
    },
     getFSFail:function(evt) {
         alert("failed" + evt.target.error.code)
        console.log(evt.target.error.code);
    },
     getSdRefAndEmbed:function(){
        cordova.plugins.diagnostic.getExternalSdCardDetails(function(details){
        details.forEach(function(detail){
            if(detail.type === "root"){
            cordova.file.externalSdCardRoot = detail.filePath;
            embedMedia();
            }
        });
        }, function(error){
        console.error(error);
        });
    },
    storagePerm: function () {
        let perms = ["android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"
        ];
                app.permissions.checkPermission("android.permission.READ_EXTERNAL_STORAGE", function (status) {
            alert('success checking permission');
            console.log('Has read storage:', status.hasPermission);
            if (!status.hasPermission) {
                app.permissions.requestPermissions(perms, function (status) {
                    console.log('success requesting READ_CONTACTS permission');
                }, function (err) {
                    console.log('failed to set permission');
                });
            }
        }, function (err) {
            console.log(err);
        });
    },
    
    
};
function requestLocationAuth(firstTime) {
    cordova.plugins.diagnostic.requestLocationAuthorization(function(status){
        var needRequest = false;
        switch(status){
            case cordova.plugins.diagnostic.permissionStatus.NOT_REQUESTED:
                needRequest = true;
                console.log("Permission not requested");
                break;
            case cordova.plugins.diagnostic.permissionStatus.DENIED_ONCE: // Only Android
            case cordova.plugins.diagnostic.permissionStatus.DENIED_ALWAYS:
                needRequest = true;
                console.log("Permission denied");
                break;
            case cordova.plugins.diagnostic.permissionStatus.GRANTED:
                console.log("Permission granted always");
                break;
            case cordova.plugins.diagnostic.permissionStatus.GRANTED_WHEN_IN_USE:
                console.log("Permission granted only when in use");
                break;
        }

        if (needRequest && firstTime) {
            requestLocationAuth(false);
        }

    }, function(error){
        console.error(error);
    }, cordova.plugins.diagnostic.locationAuthorizationMode.ALWAYS);
}
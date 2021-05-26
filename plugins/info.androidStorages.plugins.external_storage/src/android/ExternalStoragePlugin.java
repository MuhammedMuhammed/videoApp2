package info.androidStorages.plugins.external_storage;
import org.apache.cordova.*;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.widget.Toast;
import android.net.Uri;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import android.util.Log;

import android.os.Build;
import android.provider.DocumentsContract;
//import Android.*;
/**
 * This class echoes a string called from JavaScript.
 */
public class ExternalStoragePlugin extends CordovaPlugin {
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {
            isWriteStoragePermissionGranted();
            isReadStoragePermissionGranted();


            return true;
            
        }
        return false;
    }

    public boolean isExternalStorageAvailableRW()
    {
        String extStorageSTate = Environment.getExternalStorageState();
        if(extStorageSTate.equals(Environment.MEDIA_MOUNTED))
        {
            return true;
        }
        return false;
    }
    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";

        // ExternalStorageProvider
        String docId = DocumentsContract.getDocumentId(uri);
        String[] split = docId.split(String.valueOf(':'));
        String type = split[0];

        if ("primary".equalsIgnoreCase(type)) {
            return Environment.getExternalStorageDirectory() + "/" + split[1];
        } else {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                //getExternalMediaDirs() added in API 21
                File[] external = context.getExternalMediaDirs();
                if (external.length > 1) {
                    filePath = external[1].getAbsolutePath();
                    filePath = filePath.substring(0, filePath.indexOf("Android")) + split[1];
                }
            } else {
                filePath = "/storage/" + type + "/" + split[1];
            }
            return filePath;
        }
    }

    public  boolean isReadStoragePermissionGranted() {
        String TAG = "permission";


        if (Build.VERSION.SDK_INT >= 23) {

            if (this.cordova.getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Uri uri = Uri.parse("content://com.android.externalstorage.documents/document/C55E-1506%3ADownload%2Fvideofortest.mp4");
                String i = Environment.getExternalStorageDirectory().getPath();
                File sdCard = Environment.getExternalStorageDirectory();
                File file = new File(uri.getHost()+uri.getPort()+uri.getPath());
//            executeGlobalJavascript("alert('"+uri.getPath()+"')");
//            executeGlobalJavascript("alert('"+getRealPathFromURI_API19(this.cordova.getActivity().getApplicationContext(),uri) +"/"+ file.exists()+"')");
//                try {
//                    executeGlobalJavascript("alert('111')");
//
//                    if(!isExternalStorageAvailableRW()) {
//                        executeGlobalJavascript("alert('122')");
//
//                        FileReader fr = null;
//                        File f = new File(this.cordova.getActivity().getExternalFilesDir("Download"), "videofortest.mp4");
//                        executeGlobalJavascript("alert('" + f.getPath() + "')");
//
//
////                      fr = new FileReader(i);
//                    }
//                }catch (Exception e){
//                    executeGlobalJavascript("alert('"+e.getMessage()+"')");
//                }


                executeGlobalJavascript("document.getElementById('VideoPlayer').setAttribute('src','" +"file://" + getRealPathFromURI_API19(this.cordova.getActivity().getApplicationContext(),uri) + "')");

                Log.v(TAG,"Permission is granted1");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked1");
                this.cordova.getActivity().requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted1");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        String TAG = "permission";
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.cordova.getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)
            {
                Uri uri = Uri.parse("content://com.android.externalstorage.documents/document/C55E-1506%3ADownload%2Fvideofortest.mp4");
                String i = Environment.getExternalStorageDirectory().getPath();
                File sdCard = Environment.getExternalStorageDirectory();
                File file = new File(uri.getHost()+uri.getPort()+uri.getPath());
//            executeGlobalJavascript("alert('"+uri.getPath()+"')");
//            executeGlobalJavascript("alert('"+getRealPathFromURI_API19(this.cordova.getActivity().getApplicationContext(),uri) +"/"+ file.exists()+"')");
//                try {
//                    executeGlobalJavascript("alert('111')");
//
//                    if(!isExternalStorageAvailableRW()) {
//                        executeGlobalJavascript("alert('122')");
//
//                        FileReader fr = null;
//                        File f = new File(this.cordova.getActivity().getExternalFilesDir("Download"), "videofortest.mp4");
//                        executeGlobalJavascript("alert('" + f.getPath() + "')");
//
//
////                      fr = new FileReader(i);
//                    }
//                }catch (Exception e){
//                    executeGlobalJavascript("alert('"+e.getMessage()+"')");
//                }


                executeGlobalJavascript("document.getElementById('VideoPlayer').setAttribute('src','" +"file://" + getRealPathFromURI_API19(this.cordova.getActivity().getApplicationContext(),uri) + "')");

                Log.v(TAG,"Permission is granted2");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked2");
                this.cordova.getActivity().requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted2");
            return true;
        }
    }
    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
    private void executeGlobalJavascript(final String jsString){
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:" + jsString);
            }
        });
    }
}

package util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import util.category.LogTag;


public class ImageConverter {
    final private String TAG = LogTag.getTag(this);
    private static Context context;
    private static ImageConverter instance;


    private ImageConverter() {
    }

    public static ImageConverter getInstance(Context context) {
        ImageConverter.context = context;
        if (instance == null) {
            instance = new ImageConverter();
        }
        return instance;
    }

    public Bitmap uriToBitmap(Uri uri) {
        return uriToBitmap(uri, false);
    }

    public Bitmap uriToBitmap(Uri uri, boolean isFromServer) {
        Bitmap bitmap = null;
        URL url = null;
        InputStream in = null;
        HttpURLConnection conn = null;
        try {

            if (isFromServer) {
                url = new URL(uri.toString());
                // url에 접속 시도
                conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                // 스트림 생성
                in = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);
            } else {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            }

        } catch (Exception e) {
            Log.d(TAG, "uriToBitmap e.getMessage() : " + e.getMessage());
        } finally {
            try {
                if (in != null) in.close();
                if (conn != null) conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;

    }


    public File bitmapToFile(Bitmap bitmap) {
        Log.d(TAG, "bitmapToFile(Bitmap) begins to run.. : " + bitmap);
        File file = null;
        FileOutputStream out = null;
        try {
            file = createEmptyTempFile();
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            Log.d(TAG, "bitmapToFile(Bitmap) e.getMessage() : " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (out != null) out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "bitmapToFile(Bitmap) returns.. file.getAbsolutePath() : " + file.getAbsolutePath());
        return file;
    }

    public void deleteTempDir() {
        Log.d(TAG, "deleteTempDir() begins to run()..");

        File file = new File(Environment.getExternalStorageDirectory() + "/vexmall/");
        if (file.exists()) {
            File[] files = file.listFiles();
            for (File f : files) {
                f.delete();
            }
            file.delete();
        }
        Log.d(TAG, "deleteTempDir() returns..");
    }

    public File createEmptyTempFile() throws IOException {
        Log.d(TAG, "createEmptyTempFile() begins to run()..");

        // 임시폴더 생성
        File folder = null;
        File tempFile = null;

        folder = new File(Environment.getExternalStorageDirectory() + "/vexmall/");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        // 파일 이름 지정
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String filename = "vexmall_" + timeStamp;

        // 빈파일 생성
        tempFile = File.createTempFile(filename, ".jpeg", folder);
        Log.d(TAG, "createEmptyTempFile() returns.. tempFile.getAbsolutePath() : " + tempFile.getAbsolutePath());
        return tempFile;
    }


}

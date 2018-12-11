package util;

import android.content.Context;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import util.category.LogTag;

public class CustomImageLoad {

    final private String TAG = LogTag.getTag(this);
    private static CustomImageLoad customImageLoad;
    ImageLoaderConfiguration config;

    private CustomImageLoad(){

    }

    public static CustomImageLoad getInstance(){
        if(customImageLoad == null){
            customImageLoad = new CustomImageLoad();
        }
        return customImageLoad;
    }


    public void initImageLoader(Context context) {
        Log.d(TAG, "initImageLoader() begins to run...");
        config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .build();
        ImageLoader.getInstance().init(config);
        Log.d(TAG, "initImageLoader() finished.");
    }
}

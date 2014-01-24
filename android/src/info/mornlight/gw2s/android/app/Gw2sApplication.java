package info.mornlight.gw2s.android.app;


import android.app.Application;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import info.mornlight.gw2s.android.R;

import java.io.File;
import java.io.IOException;

public class Gw2sApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        File cacheDir = StorageUtils.getCacheDirectory(this);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.no_image)
                .showImageForEmptyUri(R.drawable.no_image)
                .showImageOnFail(R.drawable.no_image)
                .resetViewBeforeLoading()
                .cacheInMemory()
                .cacheOnDisc()
                .delayBeforeLoading(0)
                .build();

        // Create global configuration and initialize ImageLoader with this configuration
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
            .threadPriority(Thread.NORM_PRIORITY)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .discCache(new UnlimitedDiscCache(cacheDir)) // default
                .discCacheSize(100 * 1024 * 1024)
                .discCacheFileCount(10000)
                .defaultDisplayImageOptions(options)
            .build();
        ImageLoader.getInstance().init(config);

        try {
            App.instance().initialize(this);
        } catch (IOException e) {
            //initialize failed, just let the app crash
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onTerminate() {
        App.instance().uninitialize();
    }
}
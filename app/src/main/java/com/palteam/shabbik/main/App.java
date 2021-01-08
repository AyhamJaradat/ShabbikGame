package com.palteam.shabbik.main;

import android.app.Application;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        init();

        initializeImageLoader();

        ConfigureLog4j();
    }

    /**
     * Initialize class components.
     */
    private void init() {

    }

    private void initializeImageLoader() {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).memoryCacheExtraOptions(480, 800)
                .threadPriority(Thread.NORM_PRIORITY - 2).threadPoolSize(3)
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    private void ConfigureLog4j() {

//        String fileName = getApplicationContext().getFilesDir().getPath()
//                + File.separator + FileUtils.LOG_FILE_NAME;
//        String filePattern = "%d - [%c] - %p : %m%n";
//        int maxBackupSize = 10;
//        long maxFileSize = 1024 * 1024 * 2;
//        Log4jHelper
//                .configure(fileName, filePattern, maxBackupSize, maxFileSize);
    }

}

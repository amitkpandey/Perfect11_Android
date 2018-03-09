package com.perfect11.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.utility.PreferenceUtility;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;


public class MyApplication extends Application {

    public static Gson gson;

    public static boolean isBackPressedEnabled = true;

    private static Context mContext;

    private static final boolean IS_DEBUG_BUILD = false;

    /**
     * The options.
     */
//    public static DisplayImageOptions options;
    @Override
    public void onCreate() {
        super.onCreate();
        gson = new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory<>()).create();
//        initImageLoader(getApplicationContext());
//        options = new DisplayImageOptions.Builder()
////				.showImageOnLoading(R.drawable.ic_stub)
////				.showImageForEmptyUri(R.drawable.ic_empty)
////				.showImageOnFail(R.drawable.ic_error)
//                .cacheInMemory(true)
//                .cacheOnDisk(true).considerExifParams(true)
//                .bitmapConfig(Bitmap.Config.RGB_565).build();

//        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//
//            @Override
//            public void uncaughtException(Thread thread, Throwable ex) {
//                ex.printStackTrace();
//                sendLogFile(ex);
//            }
//        });

        mContext = this;
        /*AccountPreferenceManager.initializeInstance(mContext);*/
    }



    private void sendLogFile(Throwable ex) {
        String fullName = getFile(ex);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"arpan.sarkar@vaptech.in"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "MyApp log file");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + fullName));
        intent.putExtra(Intent.EXTRA_TEXT, "Log file attached."); // do this so some email clients don't complain about empty body.
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        System.exit(1);
    }

    private String getFile(Throwable ex) {
        File nFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "LogFile.txt");

        try {
            if (nFile.exists())
                nFile.delete();
            nFile.createNewFile();
            PrintWriter mFileWriter = new PrintWriter(nFile);
            ex.printStackTrace(mFileWriter);
            mFileWriter.flush();
            mFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nFile.getAbsolutePath();
    }

    public static void checkPreviousAndCurrentUser(Context mContext, String previousUserID, String currentUserID) {
        if (!previousUserID.equals(currentUserID)) {
            PreferenceUtility.saveObjectInAppPreference(mContext, null, PreferenceUtility.APP_PREFERENCE_NAME);
        }
    }

    /**
     * Initialize the image loader.
     *
     * @param context the context
     */
    private static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them, or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this); method.
//        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
//        config.threadPriority(Thread.NORM_PRIORITY - 2);
//        config.denyCacheImageMultipleSizesInMemory();
//        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
//        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
//        config.tasksProcessingOrder(QueueProcessingType.LIFO);
//        config.writeDebugLogs(); // Remove for release app
//
//        // Initialize ImageLoader with configuration.
//        ImageLoader.getInstance().init(config.build());
    }

    private static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringAdapter();
        }
    }

    private static class StringAdapter extends TypeAdapter<String> {
        public String read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }

        public void write(JsonWriter writer, String value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }
}
package com.imageCaptured;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.perfect11.R;
import com.utility.AlertDialogCallBack;
import com.utility.CommonUtility;
import com.utility.DialogUtility;
import com.utility.FileUtils;
import com.utility.MarshMallowPermission;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

public class VideoSelectionDialog extends Activity {//.event.dialog.VideoSelectionDialog
    public final static int STILL_IMAGE = 1;
    public final static int STILL_DOCUMENT = 3;
    public final static int VIDEO_CAPTURE = 2;

    //    private final int PICK_IMAGE_MULTIPLE = 1001;
    private static final int PICTURE_GALLERY_REQUEST = 2572;
    private static final int FILE_IMAGE_GALLERY_REQUEST = 2600;
    private static final int VIDEO_PIC_REQUEST = 1551;
    private static final int CAMERA_PIC_REQUEST = 1337;
    private static final int CAMERA_VIDEO_REQUEST = 1336;
    private static final int MODIFY_SELECT_IMAGE = 1338;
    private final int COMPRESS = 100;
    private boolean isBigLogo = false;

    public String picturePath = "";
//    private Uri uri;

    public int mediaType = 0;
    private ArrayList<String> pathArray;

    private File temp_dir;
    private File temp_path;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getWindow().getDecorView().findViewById(android.R.id.content).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        setContentView(R.layout.picture_dialog);
        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
        if (!marshMallowPermission.checkPermissionForExternalStorage())
            marshMallowPermission.requestPermissionForExternalStorage();
        readFromBundle();
        initView();
    }

    private void readFromBundle() {
        try {
            mediaType = getIntent().getIntExtra("type", 1);
            System.out.println("mediaType " + mediaType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startImageCaptured() {
//        Intent cameraActivity = new Intent(this, CameraActivity.class);
//        cameraActivity.putExtra("path", temp_path.getAbsolutePath());
//        startActivityForResult(cameraActivity, CAMERA_PIC_REQUEST);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        int REQUEST_CAMERA = 1003;
        startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }

    private void initView() {
        temp_dir = new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.app_name));
        if (!temp_dir.exists()) {
            temp_dir.mkdirs();
        }
        temp_path = new File(temp_dir, System.currentTimeMillis() + ".jpg");
        if (temp_path.exists())
            temp_path.delete();
    }


    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.btn_album:
                if (mediaType == STILL_IMAGE) {
                    loadImageFromAlbum();
                } else if (mediaType == VIDEO_CAPTURE) {
                    loadVideoFromAlbum();
                }
                break;
            case R.id.btn_camera:
                if (mediaType == STILL_IMAGE)
                    startImageCaptured();
                else if (mediaType == STILL_DOCUMENT)
                    startImageCaptured();
                else if (mediaType == VIDEO_CAPTURE)
                    startVideoCaptured();
                break;
            case R.id.ll_root:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.btn_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }

    private void startVideoCaptured() {
        File mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + System.currentTimeMillis() + ".mp4");
        Uri videoUri = Uri.fromFile(mediaFile);
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // low quality(1 means high quality)
//            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);
//            takeVideoIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 20);//X mb *1024*1024
            startActivityForResult(takeVideoIntent, CAMERA_VIDEO_REQUEST);
        }
    }

    private void loadVideoFromAlbum() {
        Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
        //comma-separated MIME types
        mediaChooser.setType("video/*");
        startActivityForResult(mediaChooser, VIDEO_PIC_REQUEST);
    }

    private void loadImageFromAlbum() {
//        ActivityController.startNextActivityForResult(this, CustomPhotoGalleryActivity.class, false, PICK_IMAGE_MULTIPLE);
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICTURE_GALLERY_REQUEST);
    }

    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case CAMERA_PIC_REQUEST:
                        if (data != null) {
                            Bitmap photo = (Bitmap) data.getExtras().get("data");
                            System.out.println("photo " + photo);

                            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                            Uri tempUri = getImageUri(this, photo);

                            // CALL THIS METHOD TO GET THE ACTUAL PATH
                            File finalFile = new File(getRealPathFromURI(tempUri));

                            System.out.println("finalFile " + finalFile);

//                            picturePath = data.getStringExtra("path");
//                            picturePath = saveScaledBitMap(BitmapFactory.decodeFile(picturePath),
//                            picturePath.substring(picturePath.indexOf("."), picturePath.length()));
//                            sendImagePath(picturePath, false);
//                            finishAndSetResult(RESULT_OK, photo);
                            finishAndSetResult(RESULT_OK, getRealPathFromURI(tempUri));
                        } else {
                            picturePath = temp_path.getAbsolutePath();
                            finishAndSetResult(RESULT_OK, picturePath);
                        }
                        break;
//                    case PICK_IMAGE_MULTIPLE:
//                        if (data != null) {
//                            String path = data.getStringExtra("data");
//                            int count = data.getIntExtra("count", 0);
//
//                            if (count > 1) {
//                                pathArray = new ArrayList<>();
////                    for (String w : path.split(",", 0)) {
////                        pathArray.add(w);
////                    }
//                                Collections.addAll(pathArray, path.split(",", 0));
//                            } else {
//                                String[] newPath = path.split(",");
//                                pathArray = new ArrayList<>();
//                                pathArray.add(newPath[0]);
//                            }
//                            finishAndSetResult(RESULT_OK, pathArray);
//                        }
//                        break;
                    case PICTURE_GALLERY_REQUEST:
                        if (data != null) {
                            if (Build.VERSION.SDK_INT < 19) {
                                Uri selectedImage = data.getData();
                                System.out.println("selectedImage " + selectedImage + " SDK " + Build.VERSION.SDK_INT);
                                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                                if (cursor != null) {
                                    cursor.moveToFirst();
                                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                    picturePath = cursor.getString(columnIndex);
                                    cursor.close();
                                }
                                picturePath = saveScaledBitMap(CommonUtility.decodeFile(800, 800, picturePath),
                                        picturePath.substring(picturePath.indexOf("."), picturePath.length()));

                            } else {
                                try {
                                    InputStream imInputStream = getContentResolver().openInputStream(data.getData());
                                    Bitmap bitmap = BitmapFactory.decodeStream(imInputStream);
                                    bitmap = getResizedBitmap(bitmap, 400);
                                    picturePath = saveGalleryImageOnKitkat(bitmap);

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                // finishAndSetResult(RESULT_OK, picturePath, false);
                            }

                            sendImagePath(picturePath, false);

                        } else {
                            picturePath = temp_path.getAbsolutePath();
                            finishAndSetResult(RESULT_CANCELED, picturePath);
                        }
                        break;
                    case VIDEO_PIC_REQUEST:
                        if (data != null) {
                            String videoFilePath = "";
                            if (Build.VERSION.SDK_INT < 19) {
                                Uri videoLocation = data.getData();
//                                this.uri = videoLocation;
                                videoFilePath = getRealPathFromURI(videoLocation);

                            } else {
                                try {
                                    InputStream imInputStream = getContentResolver().openInputStream(data.getData());
                                    videoFilePath = saveVideoInFile(imInputStream);
                                    int fileSize = checkFileSize(videoFilePath);
                                    System.out.println("upload file size before compression is " + fileSize + " in mb: " + (fileSize / 1024));
//                                    new VideoCompressor().execute(videoFilePath);
                                    sendImagePath(videoFilePath, true);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
//                            sendImagePath(videoFilePath, true);
                        } else {
                            finishAndSetResult(RESULT_CANCELED, "");
                        }
                        break;
                    case CAMERA_VIDEO_REQUEST:
                        if (data != null) {
                            Uri videoUri = data.getData();
                            System.out.println("videoUri " + videoUri);
                            File finalFile = new File(getRealPathFromURI(videoUri));

                            System.out.println("finalFile " + finalFile);
//                            sendImagePath(getRealPathFromURI(videoUri), true);
                            int fileSize = checkFileSize(getRealPathFromURI(videoUri));
                            System.out.println("upload file size before compression is " + fileSize + " in mb: " + (fileSize / 1024));

                            sendImagePath(getRealPathFromURI(videoUri), true);
//                            new VideoCompressor().execute(getRealPathFromURI(videoUri));
                        } else {
                            finishAndSetResult(RESULT_CANCELED, "");
                        }
                        break;
                    case MODIFY_SELECT_IMAGE:
                        if (data != null) {
                            System.out.println("data + " + data);
                            finishAndSetResult(RESULT_OK, data.getStringExtra("path"));
                        }
                        break;
                    case FILE_IMAGE_GALLERY_REQUEST:
                        if (data != null) {
                            System.out.println("data + " + data);
                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                                finishAndSetResult(RESULT_OK, data.getStringExtra("path"));
                            } else {
                                Uri videoUri = data.getData();
                                try {
                                    // Get the file path from the URI
                                    String selectFileAttachment = FileUtils.getPath(this, videoUri);
                                    finishAndSetResult(RESULT_OK, selectFileAttachment);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    default:
                        finish();
                        break;
                }
            } else {
                finish();
                Log.w("DialogChoosePicture", "Warning: activity result not ok");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }


    private void sendImagePath(String filePath, final boolean isVideo) {
        int fileSize = checkFileSize(filePath);
        System.out.println("upload file size is " + fileSize + " in mb: " + (fileSize / 1024));
        if (isVideo) {
            if (fileSize < 1024 * 6) {
                finishAndSetResult((filePath == null || filePath.length() == 0) ? RESULT_CANCELED : RESULT_OK, filePath);
            } else {
                DialogUtility.showMessageOkWithCallback(getResources().getString(R.string.video_validation_alert), this,
                        new AlertDialogCallBack() {

                            @Override
                            public void onSubmit() {
                                finishAndSetResult(RESULT_CANCELED, "");

                            }

                            @Override
                            public void onCancel() {
                                finishAndSetResult(RESULT_CANCELED, "");
                            }
                        });
            }
        } else {
            if (fileSize < 1024 * 5) {
                finishAndSetResult((filePath == null || filePath.length() == 0) ? RESULT_CANCELED : RESULT_OK, filePath);
            } else {
                DialogUtility.showMessageOkWithCallback(getResources().getString(R.string.image_validation_alert), this,
                        new AlertDialogCallBack() {

                            @Override
                            public void onSubmit() {
                                finishAndSetResult(RESULT_CANCELED, "");

                            }

                            @Override
                            public void onCancel() {
                                finishAndSetResult(RESULT_CANCELED, "");
                            }
                        });
            }
        }
    }

    public int checkFileSize(String filepath) {
        try {
            File fileNew = new File(filepath);
            // System.out.println("Size of : " + file_size);
            return Integer.parseInt(String.valueOf(fileNew.length() / 1024));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private String saveScaledBitMap(Bitmap bitmap, String extension) {
        FileOutputStream out = null;
        try {

            File cacheDir;

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                cacheDir = new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.app_name));
            else
                cacheDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (!cacheDir.exists())
                cacheDir.mkdirs();

            try {
                File noMediaFile = new File(cacheDir, ".nomedia");
                if (!noMediaFile.exists())
                    noMediaFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }

            String filename = System.currentTimeMillis() + extension;// ".png";
            File file = new File(cacheDir, filename);
            temp_path = file.getAbsoluteFile();
            // if(!file.exists())
            file.createNewFile();

            out = new FileOutputStream(file);
            if (bitmap != null) {
//				System.out.println("captured bitmap height " + bitmap.getHeight() + "     weight: " + bitmap.getWidth());
                bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS, out);
//                bitmap = null;
                return file.getAbsolutePath();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Throwable ignore) {
                ignore.printStackTrace();
            }
        }
        return null;
    }


    private String saveVideoInFile(InputStream inputStream) {
        try {
            File cacheDir;

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                cacheDir = new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.app_name));
            else
                cacheDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
            if (!cacheDir.exists())
                cacheDir.mkdirs();

            String filename = System.currentTimeMillis() + ".mp4";
            File file = new File(cacheDir, filename);
            temp_path = file.getAbsoluteFile();
            // if(!file.exists())
            file.createNewFile();

            final OutputStream output = new FileOutputStream(file);
            try {
                try {
                    final byte[] buffer = new byte[1024];
                    int read;

                    while ((read = inputStream.read(buffer)) != -1)
                        output.write(buffer, 0, read);

                    output.flush();
                } finally {
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            inputStream.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String saveGalleryImageOnKitkat(Bitmap bitmap) {
        try {
            File cacheDir;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                cacheDir = new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.app_name));
            else
                cacheDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (!cacheDir.exists())
                cacheDir.mkdirs();
            String filename = System.currentTimeMillis() + ".png";
            File file = new File(cacheDir, filename);
            temp_path = file.getAbsoluteFile();
            // if(!file.exists())
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, COMPRESS, out);
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    private Uri getUri() {
        String state = Environment.getExternalStorageState();
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

//    public String getImageUri(Context inContext, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        return String.valueOf(Uri.parse(path.replace("content:/", "")));
//    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void finishAndSetResult(int result, String path) {
        Log.e("IMG", "finishAndSetResult called " + false + "  Path: " + path);
        Intent intent = getIntent();
        intent.putExtra("path", path);
//        intent.putExtra("isSystemCamera", false);
//        intent.putExtra("isVideo", false);
        if (path != null && path.length() > 0)
            setResult(result, intent);
        else
            setResult(RESULT_CANCELED);
        finish();
    }

//    public void finishAndSetResult(int result, Bitmap path) {
//        Log.e("IMG", "finishAndSetResult called " + false + "  Path: " + path);
//        String pathToImage = uri.getPath();
//        System.out.println("getImageUri  " + pathToImage);
//
//        Intent intent = getIntent();
//        intent.putExtra("path", getImageUri(this, path));
////        intent.putExtra("isSystemCamera", true);
////        intent.putExtra("isVideo", false);
//        if (getImageUri(this, path) != null && getImageUri(this, path).length() > 0)
//            setResult(result, intent);
//        else
//            setResult(RESULT_CANCELED);
//        finish();
//    }

    public void finishAndSetResult(int result, ArrayList<String> pathArray) {
        Log.e("IMG", "finishAndSetResult called " + false + "  Path: " + pathArray);
        Intent intent = getIntent();
        intent.putExtra("pathArray", pathArray);
        if (pathArray != null && pathArray.size() > 0)
            setResult(result, intent);
        else
            setResult(RESULT_CANCELED);
        finish();
    }

    public void finishAndSetResult(int result, String path, boolean isVideo) {
        System.out.println("IMG finishAndSetResult called " + isVideo + "  Path: " + path);
        Intent intent = getIntent();
        intent.putExtra("path", path);
        intent.putExtra("isVideo", isVideo);

//        if (isVideo) {
        if (path != null && path.length() > 0)
            setResult(result, intent);
        else
            setResult(RESULT_CANCELED);
        finish();
//        } else {
//            Bundle bundle = new Bundle();
//            bundle.putString("path", path);
//            bundle.putBoolean("isBigLogo", isBigLogo);
//            ActivityController.startNextActivityForResult(this, ImageModificationDialog.class, bundle, false, MODIFY_SELECT_IMAGE);
//        }
    }

}

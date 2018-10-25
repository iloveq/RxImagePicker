package com.woaiqw.library.controller;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.ContentResolverCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.os.CancellationSignal;
import android.text.TextUtils;
import android.widget.Toast;

import com.woaiqw.library.annotation.ResultType;
import com.woaiqw.library.factory.ImagePickerFactory;
import com.woaiqw.library.listener.ImagePickerResultListener;
import com.woaiqw.library.model.Counter;
import com.woaiqw.library.model.Image;
import com.woaiqw.library.util.PhotoUtils;
import com.woaiqw.library.util.Utils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.woaiqw.library.util.Constants.REQUEST_IMAGE_CHOOSE_ACTIVITY_TAKE_PHOTO_BUTTON;

/**
 * Created by haoran on 2018/10/17.
 */
public class ImageController {

    private Disposable travel;
    private Disposable takePhoto;
    private Disposable result;
    private File takePhotoTempFile;
    private Uri takePhotoUri;
    private String takePhotoPath;


    public interface ImageControllerListener {
        void onSuccess(List<Image> images);

        void onError(String msg);
    }

    public final class ForceLoadContentObserver extends ContentObserver {
        ForceLoadContentObserver() {
            super(new Handler());
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            release();
        }
    }

    private CompositeDisposable disposable;
    private final String[] IMAGE_INFO = {
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.DATE_ADDED
    };

    CancellationSignal cancelSignal;

    final ForceLoadContentObserver contentObserver;

    final ImagePickerResultListener resultListener;

    private ImageController() {
        contentObserver = new ForceLoadContentObserver();
        cancelSignal = new CancellationSignal();
        disposable = new CompositeDisposable();
        resultListener = ImagePickerFactory.getImagePickerResultListener();
    }


    private static final class Holder {
        private static final ImageController IN = new ImageController();
    }

    public static ImageController get() {
        return Holder.IN;
    }

    private void attach(Disposable rx) {
        disposable.add(rx);
    }


    public void getSource(final Context context, final ImageControllerListener listener) {
        final List<Image> list = new ArrayList<>();
        travel = Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(ObservableEmitter<Cursor> emitter) {
                Cursor cursor = ContentResolverCompat.query(context.getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_INFO, null,
                        null, IMAGE_INFO[6] + " DESC", cancelSignal);
                if (cursor != null) {
                    try {
                        cursor.getCount();
                        cursor.registerContentObserver(contentObserver);
                    } catch (RuntimeException ex) {
                        cursor.close();
                        throw ex;
                    }
                }
                emitter.onNext(cursor);
            }
        }).map(new Function<Cursor, List<Image>>() {
            @Override
            public List<Image> apply(Cursor data) {
                if (data != null) {
                    while (data.moveToNext()) {
                        String imageName = data.getString(data.getColumnIndexOrThrow(IMAGE_INFO[0]));
                        String imagePath = data.getString(data.getColumnIndexOrThrow(IMAGE_INFO[1]));
                        File file = new File(imagePath);
                        if (!file.exists() || file.length() <= 0) {
                            continue;
                        }
                        long imageSize = data.getLong(data.getColumnIndexOrThrow(IMAGE_INFO[2]));
                        int imageWidth = data.getInt(data.getColumnIndexOrThrow(IMAGE_INFO[3]));
                        int imageHeight = data.getInt(data.getColumnIndexOrThrow(IMAGE_INFO[4]));
                        String imageMimeType = data.getString(data.getColumnIndexOrThrow(IMAGE_INFO[5]));
                        long imageAddTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_INFO[6]));
                        Image Image = new Image();
                        Image.name = imageName;
                        Image.path = imagePath;
                        Image.size = imageSize;
                        Image.width = imageWidth;
                        Image.height = imageHeight;
                        Image.mimeType = imageMimeType;
                        Image.addTime = imageAddTime;
                        list.add(Image);
                    }
                }
                return list;
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Image>>() {
            @Override
            public void accept(List<Image> images) {
                listener.onSuccess(images);

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                listener.onError(throwable.getMessage());
            }
        });
        attach(travel);
    }

    public interface PhotoPathListener {
        void getPhotoPath(String path);
    }

    public void takePhoto(WeakReference<Activity> source) {
        final Activity activity = source.get();
        final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        takePhoto = Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(ObservableEmitter<File> emitter) {
                if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                    if (Utils.existSDCard()) {
                        takePhotoTempFile = new File(Environment.getExternalStorageDirectory(), "/Pictures");
                    } else {
                        takePhotoTempFile = Environment.getDataDirectory();
                    }
                    takePhotoTempFile = PhotoUtils.createFile(takePhotoTempFile, "IMG_", ".jpg", new PhotoPathListener() {
                        @Override
                        public void getPhotoPath(String path) {
                            takePhotoPath = path;
                        }
                    });
                    emitter.onNext(takePhotoTempFile);
                }
            }
        }).map(new Function<File, Uri>() {
            @Override
            public Uri apply(File file) {
                if (file == null) {
                    throw new IllegalStateException(" temp file generate failure ");
                }
                Uri uri;
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                    uri = Uri.fromFile(file);
                } else {
                    /**
                     * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
                     * 并且这样可以解决MIUI系统上拍照返回size为0的情况
                     */
                    uri = FileProvider.getUriForFile(activity, Utils.getFileProviderAuthorityName(), file);
                    //加入uri权限 要不三星手机不能拍照
                    List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        activity.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                }

                return uri;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Uri>() {
            @Override
            public void accept(Uri uri) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CHOOSE_ACTIVITY_TAKE_PHOTO_BUTTON);
                takePhotoUri = uri;
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        attach(takePhoto);
    }

    public void convertTakePhotoResult(int resultType) {
        switch (resultType) {
            case ResultType.FILE:
                resultListener.onPhotoTook(getTakePhotoTempFile());
                break;
            case ResultType.URI:
                resultListener.onPhotoTook(getTakePhotoUri());
                break;
            case ResultType.PATH:
                resultListener.onPhotoTook(getTakePhotoPath());
                break;

        }
    }


    public void convertPickedPhotoResult(int resultType) {
        List<Image> list = Counter.getInstance().getCheckedList();
        final List<String> resultOfPath = new ArrayList<>();
        for (Image image : list) {
            resultOfPath.add(image.path);
        }
        switch (resultType) {
            case ResultType.FILE:
                result = Observable.create(new ObservableOnSubscribe<List<File>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<File>> emitter) {
                        List<File> resultOfFile = new ArrayList<>();
                        for (String path : resultOfPath) {
                            File file = new File(path);
                            resultOfFile.add(file);
                        }
                        emitter.onNext(resultOfFile);
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<File>>() {
                    @Override
                    public void accept(List<File> files) {
                        resultListener.onPicked(files);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        resultListener.onException(throwable.getMessage());
                    }
                });
                attach(result);
                break;
            case ResultType.URI:
                List<Uri> resultOfUri = new ArrayList<>();
                for (String path : resultOfPath) {
                    Uri uri = Uri.parse(path);
                    resultOfUri.add(uri);
                }
                resultListener.onPicked(resultOfUri);
                break;
            case ResultType.PATH:
                resultListener.onPicked(resultOfPath);
                break;

        }

    }

    public File getTakePhotoTempFile() {
        if (takePhotoTempFile == null) {
            throw new IllegalStateException(" temp file generate failure ");
        }
        return takePhotoTempFile;
    }

    public Uri getTakePhotoUri() {
        if (takePhotoUri == null) {
            throw new IllegalStateException(" uri generate failure ");
        }
        return takePhotoUri;
    }

    public String getTakePhotoPath() {
        if (TextUtils.isEmpty(takePhotoPath)) {
            takePhotoPath = "";
        }
        return takePhotoPath;
    }

    public ImagePickerResultListener getResultListener() {
        return resultListener;
    }

    public void release() {
        if (travel != null) {
            disposable.remove(travel);
        }
        if (takePhoto != null) {
            disposable.remove(takePhoto);
        }
        if (result != null) {
            disposable.remove(result);
        }

        takePhotoPath = null;
        takePhotoTempFile = null;
        takePhotoUri = null;


    }

}

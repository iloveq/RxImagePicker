package com.woaiqw.library.controller;


import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.ContentResolverCompat;
import android.support.v4.os.CancellationSignal;

import com.woaiqw.library.annotation.ResultType;
import com.woaiqw.library.factory.ImagePickerFactory;
import com.woaiqw.library.listener.ImagePickerResultListener;
import com.woaiqw.library.model.Counter;
import com.woaiqw.library.model.Image;

import java.io.File;
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

/**
 * Created by haoran on 2018/10/17.
 */
public class ImageController {

    private Disposable source;
    private Disposable result;

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

    ImagePickerResultListener resultListener;

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
        source = Observable.create(new ObservableOnSubscribe<Cursor>() {
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
        attach(source);
    }

    public void convertResult(int resultType) {
        List<Image> list = Counter.getInstance().getCheckedList();
        final List<String> resultOfPath = new ArrayList<>();
        for (Image image : list) {
            resultOfPath.add(image.path);
        }
        switch (resultType) {
            case ResultType.FILE:
                result = Observable.create(new ObservableOnSubscribe<List<File>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<File>> emitter) throws Exception {
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
                        resultListener.onPhotoTook(files);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        resultListener.onException(throwable.getMessage());
                    }
                });
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

    public void release() {
        if (source != null) {
            disposable.remove(source);
        }
        if (result != null) {
            disposable.remove(result);
        }
        if (resultListener != null) {
            resultListener = null;
        }
    }

}

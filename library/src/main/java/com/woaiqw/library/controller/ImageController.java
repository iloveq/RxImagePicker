package com.woaiqw.library.controller;


import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.ContentResolverCompat;
import android.support.v4.os.CancellationSignal;

import com.woaiqw.library.R;
import com.woaiqw.library.model.Album;
import com.woaiqw.library.model.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by haoran on 2018/10/17.
 */
public class ImageController {

    public final class ForceLoadContentObserver extends ContentObserver {
        public ForceLoadContentObserver() {
            super(new Handler());
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {

        }
    }

    private CompositeDisposable disposable;
    private final String[] IMAGE_INFO = {     //查询图片需要的数据列
            MediaStore.Images.Media.DISPLAY_NAME,   //图片的显示名称  aaa.jpg
            MediaStore.Images.Media.DATA,           //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
            MediaStore.Images.Media.SIZE,           //图片的大小，long型  132492
            MediaStore.Images.Media.WIDTH,          //图片的宽度，int型  1920
            MediaStore.Images.Media.HEIGHT,         //图片的高度，int型  1080
            MediaStore.Images.Media.MIME_TYPE,      //图片的类型     image/jpeg
            MediaStore.Images.Media.DATE_ADDED};    //图片被添加的时间，long型  1450518608
    CancellationSignal mCancellationSignal;

    final ForceLoadContentObserver mObserver;

    private ImageController() {
        mObserver = new ForceLoadContentObserver();
        mCancellationSignal = new CancellationSignal();
        disposable = new CompositeDisposable();
    }

    private ArrayList<Album> albums = new ArrayList<>();

    private static final class Holder {
        private static final ImageController IN = new ImageController();
    }

    public static ImageController get() {
        return Holder.IN;
    }

    public void attach(Disposable rx) {
        disposable.add(rx);
    }


    public Observable<List<Album>> getSource(final Context context) {
        Observable observable = Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(ObservableEmitter<Cursor> emitter) {
                Cursor cursor = ContentResolverCompat.query(context.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_INFO, null,
                        null, IMAGE_INFO[6] + " DESC", mCancellationSignal);
                if (cursor != null) {
                    try {
                        // Ensure the cursor window is filled.
                        cursor.getCount();
                        cursor.registerContentObserver(mObserver);
                    } catch (RuntimeException ex) {
                        cursor.close();
                        throw ex;
                    }
                }
                emitter.onNext(cursor);
            }
        }).map(new Function<Cursor, List<Album>>() {
            @Override
            public List<Album> apply(Cursor data) {
                if (data != null) {
                    List<Image> list = new ArrayList<>();
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
                        //封装实体
                        Image Image = new Image();
                        Image.name = imageName;
                        Image.path = imagePath;
                        Image.size = imageSize;
                        Image.width = imageWidth;
                        Image.height = imageHeight;
                        Image.mimeType = imageMimeType;
                        Image.addTime = imageAddTime;
                        list.add(Image);
                        //根据父路径分类存放图片
                        File imageFile = new File(imagePath);
                        File imageParentFile = imageFile.getParentFile();
                        Album album = new Album();
                        album.name = imageParentFile.getName();
                        album.path = imageParentFile.getAbsolutePath();
                        if (!albums.contains(album)) {
                            ArrayList<Image> images = new ArrayList<>();
                            images.add(Image);
                            album.cover = Image;
                            album.images = images;
                            albums.add(album);
                        } else {
                            albums.get(albums.indexOf(album)).images.add(Image);
                        }
                    }
                    if (data.getCount() > 0 && list.size() > 0) {
                        Album album = new Album();
                        album.name = context.getResources().getString(R.string.all_images);
                        album.path = "/";
                        album.cover = list.get(0);
                        album.images = list;
                        albums.add(0, album);
                    }
                }
                return albums;
            }
        }).subscribeOn(Schedulers.newThread());

        return observable;
    }

    public void release() {
        disposable.dispose();
    }


}

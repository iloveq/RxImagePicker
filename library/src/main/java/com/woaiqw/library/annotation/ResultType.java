package com.woaiqw.library.annotation;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.woaiqw.library.annotation.ResultType.FILE;
import static com.woaiqw.library.annotation.ResultType.PATH;
import static com.woaiqw.library.annotation.ResultType.URI;


/**
 * Created by haoran on 2018/10/12.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({URI, FILE, PATH})
public @interface ResultType {

    int URI = 0;
    int FILE = 1;
    int PATH = 2;

}

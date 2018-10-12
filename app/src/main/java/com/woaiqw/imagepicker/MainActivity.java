package com.woaiqw.imagepicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.woaiqw.library.ImagePicker;
import com.woaiqw.library.annotation.ResultType;
import com.woaiqw.library.listener.ImagePickerResultListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker
                        .in(MainActivity.this)
                        .createFactory()
                        .setGridColumn(4)
                        .setTheme(R.style.AppTheme)
                        .setResultType(ResultType.URI)
                        .onResult(new ImagePickerResultListener() {
                            @Override
                            public void onSelected(Object o) {

                            }

                            @Override
                            public void onTakePhoto(Object o) {

                            }
                        })
                        .build();

            }
        });


    }

}
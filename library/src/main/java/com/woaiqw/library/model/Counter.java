package com.woaiqw.library.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haoran on 2018/10/22.
 */
public class Counter {

    private static volatile Counter instance = null;
    private int count;
    private List<Image> list = new ArrayList<>();

    public static Counter getInstance() {
        if (null == instance) {
            synchronized (Counter.class) {
                if (null == instance) {
                    instance = new Counter();
                }
            }
        }
        return instance;
    }

    public void increase() {
        count++;
    }

    public void decrease() {
        count--;
    }

    public int getCount() {
        return count;
    }

    public void reset() {
        this.count = 0;
    }

    public List<Image> getCheckedList() {
        List<Image> checkedList = new ArrayList<>();
        for (Image image : list) {
            if (image.checked) {
                checkedList.add(image);
            }
        }
        return checkedList;
    }

    public void setList(List<Image> images) {
        clear();
        list.addAll(images);
    }

    public void clear() {
        list.clear();
    }

    public List<Image> getList() {
        return list;
    }

    public void resetCheckedStatus(Image image) {
        for (Image image1 : list) {
            if (image != null && image1 != null) {
                if (image.equals(image1)) {
                    image1.checked = false;
                }
            }
        }
    }
}

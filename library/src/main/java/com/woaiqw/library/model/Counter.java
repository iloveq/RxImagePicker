package com.woaiqw.library.model;

/**
 * Created by haoran on 2018/10/22.
 */
public class Counter {

    private static volatile Counter instance = null;
    private int count;

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

    public void reset(int count) {
        this.count = 0;
    }
}

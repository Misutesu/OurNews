package com.team60.ournews.module.presenterTemp;

/**
 * Created by Misutesu on 2017/2/27.
 */

public interface Presenter<V> {
    void attachView(V view);

    void detachView();
}

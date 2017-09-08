package com.example.pancho.w6;

/**
 * Created by FRANCISCO on 22/08/2017.
 */

public interface BasePresenter <V extends BaseView>{

    void attachView(V view);
    void detachView();
}

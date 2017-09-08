package com.example.pancho.w6.view.mainactivity;

import android.content.Context;

import com.example.pancho.w6.BasePresenter;
import com.example.pancho.w6.BaseView;
import com.example.pancho.w6.model.Movies;

/**
 * Created by FRANCISCO on 22/08/2017.
 */

public interface MainActivityContract {

    interface View extends BaseView {

        void sendResult(Movies movies);
    }

    interface Presenter extends BasePresenter<View>{
        void setContext(Context context);

        void makeRestCall(String query, boolean force);
    }
}

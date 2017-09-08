package com.example.pancho.w6.view.secondView;

import android.content.Context;

import com.example.pancho.w6.BasePresenter;
import com.example.pancho.w6.BaseView;
import com.example.pancho.w6.model.Details.Details;

/**
 * Created by FRANCISCO on 22/08/2017.
 */

public interface SecondViewContract {

    interface View extends BaseView {

        void sendResult(Details details);
    }

    interface Presenter extends BasePresenter<View>{
        void setContext(Context context);

        void makeRestCall(String query, boolean force);
    }
}

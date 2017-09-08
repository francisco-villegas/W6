package com.example.pancho.w6.injection.secondview;

import com.example.pancho.w6.view.secondView.*;

import dagger.Module;
import dagger.Provides;

/**
 * Created by FRANCISCO on 22/08/2017.
 */

@Module
public class SecondViewModule {

    @Provides
//    @Singleton this is going to make the class as singleton
    SecondViewPresenter providesSecondViewPresenter(){

        return new SecondViewPresenter();
    }
}

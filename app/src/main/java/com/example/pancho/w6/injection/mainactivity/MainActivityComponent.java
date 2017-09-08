package com.example.pancho.w6.injection.mainactivity;

import com.example.pancho.w6.view.mainactivity.MainActivity;

import dagger.Component;

/**
 * Created by FRANCISCO on 22/08/2017.
 */

@Component(modules = MainActivityModule.class)  //@Component(modules = 1.class,2.class) separated by commas for 2 or more modules
public interface MainActivityComponent {

//    void inject(MainActivity mainActivity); no difference between inject or insert because is the name of the method only in here
    void insert(MainActivity mainActivity);

}

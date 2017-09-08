package com.example.pancho.w6.injection.secondview;

import com.example.pancho.w6.view.secondView.*;

import dagger.Component;

/**
 * Created by FRANCISCO on 22/08/2017.
 */

@Component(modules = SecondViewModule.class)  //@Component(modules = 1.class,2.class) separated by commas for 2 or more modules
public interface SecondViewComponent {

    void insert(SecondView secondView);

}

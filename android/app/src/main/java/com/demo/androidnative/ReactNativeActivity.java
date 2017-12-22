package com.demo.androidnative;

import com.facebook.react.ReactActivity;

/**
 * description:
 * Created by yuanliang.wu on 2017/12/21.
 */

public class ReactNativeActivity extends ReactActivity {

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "MixReactNative";
    }
}

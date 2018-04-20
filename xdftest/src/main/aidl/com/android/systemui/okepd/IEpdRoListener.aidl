// IEpdRoListener.aidl
package com.android.systemui.okepd;

// Declare any non-default types here with import statements

interface IEpdRoListener {
    /**
     * EPD rotation successful
     */
    boolean onRotationCompleted();

    /**
    * EPD rotation no change
    */
    void onRotationNoChanged();
}

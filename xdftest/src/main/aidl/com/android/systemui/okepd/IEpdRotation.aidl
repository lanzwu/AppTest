// IEpdRotation.aidl
package com.android.systemui.okepd;

import com.android.systemui.okepd.IEpdRoListener;

// Declare any non-default types here with import statements

interface IEpdRotation {
    /**
     * update epd rotation.
     * @param rotation Rotation that represents the request orientation of EPD Display.
     * rotation = 0 initial orientation, rotation = 1 rotated to left vertical screen
     * rotation = 2 opposite of the initial orientation, rotation = 3 rotated to right vertical screen
     * @param needCallback true change the orientation and callback to app, false only change the orientation
     * @return -1 error, 1 successful
     */
    int updateEpdRotation(int rotation, boolean needCallback);

    /**
     * get epd rotation.
     * @return the current orientation of EPD Display.
     * return -1 error.
     */
    int getEpdRotation();

    /**
     * register orientation listener.
     * @param listener Callback this listener When EPD orientation changed.
     */
    void registerListener(IEpdRoListener listener);

    /**
     * unregister orientation IEpdRoListener.
     */
    void unregisterListener();
}


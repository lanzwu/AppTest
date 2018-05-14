package android.app;

import android.graphics.Rect;

public class OKAYAdapterManager {

    public boolean setEinkMode(int mode) {
        return true;
    }

    public boolean disableFastHandWrite() {
        return true;
    }

    public boolean enableFastHandWrite() {
        return true;
    }

    public boolean setPartialUpdateRegion(Rect[] areas) {
        return true;
    }

    public boolean enableEinkForceUpdate() {
        return true;
    }

    public boolean setHandWriteArea(Rect[] areas) {
        return true;
    }

    public boolean setHandWriteStroke(float strokeWidth) {
        return true;
    }

    public boolean setEinkFinger(boolean enable) {
        return true;
    }

    public boolean setEinkPen(boolean enable) {
        return true;
    }
}

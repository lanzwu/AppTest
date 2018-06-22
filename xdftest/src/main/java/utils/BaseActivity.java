package utils;

import android.app.Activity;
import android.app.OKAYAdapterManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ServiceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Display;
import android.view.IWindowManager;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.xdftest.MainActivity;

/**
 * Created by zhouxiangyu on 2017/12/22.
 */

public class BaseActivity extends Activity {
    private Dismiss dismiss;
    private IWindowManager wm;
    public EinkPresentation presentation;
    public Display[] displays;
    public OKAYAdapterManager okayManager;
    private boolean enableBack = false;
    private boolean enableVolume = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            init();
        }

        wm = IWindowManager.Stub.asInterface(ServiceManager.getService("window"));

        //禁止使用home键和电源键
        enableHomeBtn(false);

        okayManager = (OKAYAdapterManager) getSystemService("okay");
        if (okayManager == null) {
            Toast.makeText(this, "相关接口缺失，请更新最新系统版本！", Toast.LENGTH_LONG).show();
            this.finish();
        } else {
            okayManager.setEinkMode(1);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Settings.canDrawOverlays(this)) {
            init();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        } else {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void init() {
        DisplayManager manager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        if (manager != null) {
            displays = manager.getDisplays();
        }
        if (displays.length == 2 && presentation == null) {
            presentation = new EinkPresentation(this, displays[1]);
            setPresentation(presentation);
            PermissionUtils.verifyStoragePermissions(this);
        }
    }

    public void showPresentation() {
        if(presentation != null) {
            presentation.show();
        }
    }

    public void refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                okayManager.enableEinkForceUpdate();
            }
        }, 800);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.REQUEST_CODE) {
            if (grantResults.length > 0 && !(grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                PermissionUtils.verifyStoragePermissions(this);
            }
        }
    }

    public void setPresentation(Dismiss dismiss) {
        this.dismiss = dismiss;
    }

    public void startFullScreenHandwrite() {
        okayManager.enableFastHandWrite();
        okayManager.setHandWriteArea(new Rect[]{new Rect(0, 0, EinkPresentation.screenWidth, EinkPresentation.screenHeight)});
    }

    public void startHandWriteArea(Rect[] rects) {
        okayManager.enableFastHandWrite();
        okayManager.setHandWriteArea(rects);
    }

    public void closeHandWrite() {
        okayManager.disableFastHandWrite();
        okayManager.setHandWriteArea(new Rect[]{});
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (!enableBack) {
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (!enableVolume) {
                    return true;
                }
                break;
            default:
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    public void enableBackBtn(boolean enable) {
        enableBack = enable;
    }

    public void enableVolumeBtn(boolean enable) {
        enableVolume = enable;
    }

    public void enableHomeBtn(boolean enable) {
        try {
            if (enable) {
                wm.setTestFlag(false);
            } else {
                wm.setTestFlag(true);
            }
        } catch (Exception e) {
            Log.d("zhouxiangyu", e.toString());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        enableBackBtn(false);
        MainActivity.enableClick = true;
        if (dismiss != null) {
            dismiss.clearDialog();
        }
    }
}

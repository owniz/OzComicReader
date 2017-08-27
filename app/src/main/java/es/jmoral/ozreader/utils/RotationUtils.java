package es.jmoral.ozreader.utils;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.WindowManager;

/**
 * Created by grender on 4/07/17.
 */

public class RotationUtils {
    public enum RotationState {
        PORTRAIT, LANDSCAPE, REVERSE_PORTRAIT, REVERSE_LANDSCAPE, UNKNOWN
    }

    public static RotationState getRotationState(@NonNull Context context) {
        final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return RotationState.PORTRAIT;
            case Surface.ROTATION_90:
                return RotationState.LANDSCAPE;
            case Surface.ROTATION_180:
                return RotationState.REVERSE_PORTRAIT;
            case Surface.ROTATION_270:
                return RotationState.REVERSE_LANDSCAPE;
            default:
                return RotationState.UNKNOWN;
        }
    }

    public static void lockOrientation(@NonNull AppCompatActivity appCompatActivity) {
        int orientation;
        switch (getRotationState(appCompatActivity)) {
            case PORTRAIT:
                orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                break;
            case LANDSCAPE:
                orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                break;
            case REVERSE_PORTRAIT:
                orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                break;
            case REVERSE_LANDSCAPE:
                orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                break;
            default:
                orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
        }
        appCompatActivity.setRequestedOrientation(orientation);
    }

    public static void restoreOrientation(@NonNull AppCompatActivity appCompatActivity) {
        appCompatActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }
}
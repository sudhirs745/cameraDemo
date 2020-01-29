package com.sudhir.camerademo;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Size;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Utility {


    static class CompareSizesByArea implements Comparator<Size> {

        public static CompareSizesByArea newInstance(){
            return new CompareSizesByArea();
        }
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    static Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                                  int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {

        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
//            Log.d(TAG, "chooseOptimalSize: w: " + option.getWidth() + ", h: " + option.getHeight());

            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            return choices[0];
        }
    }
}

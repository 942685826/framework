package com.yaxon.frameWork.utils;

import android.hardware.Camera;

/**
 * 摄像头处理
 *
 * @author guojiaping
 * @version 2015-11-7 创建<br>
 */
public class CameraUtils {
    private CameraUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断摄像头是否可用
     *
     * @return
     */
    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters parameters = mCamera.getParameters();
            mCamera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCamera != null) {
                mCamera.release();
            }
        }
        return canUse;
    }
}

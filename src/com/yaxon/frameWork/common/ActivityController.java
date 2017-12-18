package com.yaxon.frameWork.common;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GJP on 2017/9/7.
 */
public class ActivityController {
    private static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

}

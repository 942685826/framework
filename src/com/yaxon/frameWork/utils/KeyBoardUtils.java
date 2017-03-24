package com.yaxon.frameWork.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * android输法切换
 *
 * @author guojiaping
 * @version create on 2016/11/10.
 */
public class KeyBoardUtils {
    private KeyBoardUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 切换软键盘的状态
     * 如当前为收起变为弹出，若当前为弹出变为收起
     */
    public static void toggleKeyboard(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 强制隐藏输入法键盘
     */
    public static void hideKeyboard(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    /**
     * 强制显示输入法键盘
     */
    public static void showKeyboard(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, 2);
    }

    public static boolean isKeyboard(EditText editText) {
        boolean bool = false;
        InputMethodManager inputMethodManager = (InputMethodManager)
                editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            bool = true;
        }
        return bool;
    }
}

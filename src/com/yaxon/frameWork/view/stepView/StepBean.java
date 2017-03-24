package com.yaxon.frameWork.view.stepView;

/**
 * 进度条状态
 *
 * @author guojiaping
 * @version 2017-3-23 创建<br>
 */
public class StepBean {
    public static final int STEP_UNDO = -1;
    public static final int STEP_CURRENT = 0;
    public static final int STEP_COMPLETED = 1;
    public static final int STEP_LAST_COMPLETED = 2;
    public static final int STEP_LAST_UNCOMPLETED = 3;
    private String name;
    private int state;

    public StepBean(String name, int state) {
        this.name = name;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}

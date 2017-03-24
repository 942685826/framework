package com.yaxon.frameWork.view.stepView;

import android.app.Activity;
import android.os.Bundle;
import com.yaxon.frameWork.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guojiaping
 * @version 2017-3-23 创建<br>
 */
public class HorizontalStepActivity extends Activity {
    private List<StepBean> stepsBeanList;
    private HorizontalStepView mHorizontalStepView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.horizontal_step_layout);
        mHorizontalStepView = (HorizontalStepView) findViewById(R.id.hsv_step_view);

        stepsBeanList = new ArrayList<>();
        StepBean stepBean0 = null;
        StepBean stepBean1 = null;
        StepBean stepBean2 = null;
        StepBean stepBean3 = null;
        int stepIndex = 3;
        switch (stepIndex) {
            case 1:
                stepBean0 = new StepBean("手机绑定", 1);
                stepBean1 = new StepBean("实名认证", 0);
                stepBean2 = new StepBean("学时充值", -1);
                stepBean3 = new StepBean("开始用车", 3);
                break;
            case 2:
                stepBean0 = new StepBean("手机绑定", 1);
                stepBean1 = new StepBean("实名认证", 1);
                stepBean2 = new StepBean("学时充值", 0);
                stepBean3 = new StepBean("开始用车", 3);
                break;
            case 3:
                stepBean0 = new StepBean("手机绑定", 1);
                stepBean1 = new StepBean("实名认证", 1);
                stepBean2 = new StepBean("学时充值", 1);
                stepBean3 = new StepBean("开始用车", 2);
                break;
        }
        stepsBeanList.add(stepBean0);
        stepsBeanList.add(stepBean1);
        stepsBeanList.add(stepBean2);
        stepsBeanList.add(stepBean3);
        mHorizontalStepView.setStepViewTexts(stepsBeanList);
    }
}

package com.ws.coyc.wsnote.UI.DialogFragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.UI.PopUp.SetTextPopup.OnDescribeInterface;
import com.ws.coyc.wsnote.UI.PopUp.SetTextPopup.TextEditPopWind;

/**
 * Created by coyc on 16-8-27.
 */

public class Dialog_addWait extends FatherDialog {

    private Button mDisBt;
    private Button mOkBt;

    private OnDescribeInterface describeInterface;
    private String image_path = "";

    public Dialog_addWait()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        v = inflater.inflate(R.layout.popupdescriberecord, container);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        initData();
        initView();

        return v;

    }
    public void initView()
    {
        super.initView();
        mDisBt = (Button) v.findViewById(R.id.bt_dis);
        mDisBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                mPopup.dismiss();
            }
        });
        mOkBt = (Button) v.findViewById(R.id.bt_ok);
        mOkBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if(!Dialog_addWait.super.check())
                {
                    return;
                }

                describeInterface.onDescribed(mText_name.getText().toString(), mText_goods.getText().toString()
                        ,mTv_phone.getText().toString(),mTv_address.getText().toString(),image_path);

//                mPopup.dismiss();
            }
        });
    }

    public void initData() {
        super.initData();

    }

    public void setDescribeInterface(OnDescribeInterface describeInterface) {
        this.describeInterface = describeInterface;
    }
}

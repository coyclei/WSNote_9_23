package com.ws.coyc.wsnote.UI.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ws.coyc.wsnote.Data.DataManager;
import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.UI.Activity.MainActivity;
import com.ws.coyc.wsnote.UI.Activity.PersonActivity;
import com.ws.coyc.wsnote.UI.Activity.WSNoteInfo;
import com.ws.coyc.wsnote.UI.Activity.WareHouseActivity;
import com.ws.coyc.wsnote.UI.Adapter.ListPosition;


/**
 * Created by leipe on 2016/3/13.
 */
public class F_4 extends Fragment {



    private View mView;
    public static Context mContext;

    private TextView mTv_name;

    private TextView mTv_sign;

    private ImageView mIv_face;
    private TextView mTv_personsize;
    private TextView mTv_goods;

    private RelativeLayout mRl_person;
    private RelativeLayout mRl_goods;

    private Button mTv_loginOut;
    private Button mTv_exit;
    private Button mBt_version;


    private ListPosition listPosition = new ListPosition();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.f_personinfo_layout, container, false);

        initData();

        initView();

        return mView;
    }


    public static Handler mHandler;
    public static final int Add_One_OverInfo = 0;

    private void initData() {
        mContext = getContext();

        mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case Add_One_OverInfo:

                        break;
                }
            }
        };

    }

    private void initView() {

        mTv_name = (TextView) mView.findViewById(R.id.tv_name);
        mTv_sign = (TextView) mView.findViewById(R.id.tv_sign);
        mIv_face = (ImageView) mView.findViewById(R.id.iv_face);
        mTv_personsize = (TextView) mView.findViewById(R.id.tv_joinactive_count);
        mTv_goods = (TextView) mView.findViewById(R.id.tv_createactive_count);
        mRl_person = (RelativeLayout) mView.findViewById(R.id.rl_join);
        mRl_goods = (RelativeLayout) mView.findViewById(R.id.rl_create);

        mTv_loginOut = (Button) mView.findViewById(R.id.tv_loginOut);
        mTv_loginOut.setOnClickListener(mClickListener);
        mTv_exit = (Button) mView.findViewById(R.id.tv_exit);
        mBt_version = (Button) mView.findViewById(R.id.tv_version);
        mTv_exit.setOnClickListener(mClickListener);

        mTv_personsize.setText(DataManager.getInstance().getPersonSize()+"");



        mRl_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PersonActivity.class);
                startActivity(intent);
            }
        });
        mRl_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WareHouseActivity.class);
                startActivity(intent);
            }
        });
        mBt_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,WSNoteInfo.class);
                startActivity(intent);
            }
        });

    }


    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.tv_loginOut:

                    break;

                case R.id.tv_exit:
                    getActivity().finish();
                    break;
            }
        }
    };

}

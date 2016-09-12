package com.ws.coyc.wsnote.UI.DialogFragment.GoodsDialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ws.coyc.wsnote.Data.Goods;
import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.SQLiteHelper.Utils.l;
import com.ws.coyc.wsnote.UI.Activity.MainActivity;
import com.ws.coyc.wsnote.UI.Activity.OnGetImage;
import com.ws.coyc.wsnote.UI.Activity.WareHouseActivity;
import com.ws.coyc.wsnote.Utils.ImageLoader;
import com.ws.coyc.wsnote.Utils.SDFile;

/**
 * Created by coyc on 16-9-10.
 */

public class GoodsDialog extends DialogFragment {

    protected View view;
    protected Goods goods;
    protected EditText mEt_name;
    protected EditText mEt_info;
    protected EditText mEt_money_in;
    protected EditText mEt_money_out;
    protected EditText mEt_count;
    protected ImageView mIv_addImage;

    protected Button mBt_ok;
    protected TextView mTv_title;

    protected String image_path = "";

    protected OnGetGoods onGetGoods;


    public void setOnGetGoodsListener(OnGetGoods onGetGoods)
    {
        this.onGetGoods = onGetGoods;
    }

    public void setGoods(Goods goods)
    {
        this.goods = goods;
    }
    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((int) (dm.widthPixels * 0.95), (int)(dm.heightPixels*0.65));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.goodsdialog, container);

        initData();
        initView();

        return view;

    }

    private void initView() {
        mEt_name = (EditText) view.findViewById(R.id.tv_name);
        mEt_info = (EditText) view.findViewById(R.id.tv_goods_info);
        mEt_money_in = (EditText) view.findViewById(R.id.tv_text_money_in);
        mEt_money_out = (EditText) view.findViewById(R.id.tv_text_money_out);
        mEt_count = (EditText) view.findViewById(R.id.tv_info);
        mIv_addImage = (ImageView) view.findViewById(R.id.iv_image);

        mTv_title = (TextView) view.findViewById(R.id.tv_title);
        mBt_ok = (Button) view.findViewById(R.id.bt_ok);

        if(goods==null)
        {
            goods = new Goods();
        }else
        {
            image_path = goods.image_url_local;
            initImage();
            mEt_name.setText(goods.name);
            mEt_info.setText(goods.info);
            mEt_money_in.setText(goods.money_in_temp+"");
            mEt_money_out.setText(goods.money_out_temp+"");
            mEt_count.setText(goods.count+"");
        }



        mIv_addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WareHouseActivity.setOnGetImage(new OnGetImage() {
                    @Override
                    public void onGetImage(String path) {
                        image_path = path;
                        l.l("path "+path);
                        ImageLoader.getInstance().loadImage(path,mIv_addImage,5);
                    }
                });
                WareHouseActivity.mHandler.sendEmptyMessage(MainActivity.To_CAMARE);
            }
        });
    }

    private void initImage() {
        if(SDFile.CheckFileExistsABS(image_path))
        {
            mIv_addImage.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().loadImage(image_path,mIv_addImage,5);
        }
    }

    private void initData() {
        
    }


    public boolean check()
    {

        if(mEt_name.getText().length()==0)
        {
            Toast.makeText(getActivity(),"请输入商品名",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mEt_info.getText().length()==0)
        {
            Toast.makeText(getActivity(),"请输入商品信息",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mEt_money_in.getText().length()==0)
        {
            Toast.makeText(getActivity(),"请输入默认进货价",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mEt_money_out.getText().length()==0)
        {
            Toast.makeText(getActivity(),"请输入默认出货价",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void createGoods()
    {
        goods.name = mEt_name.getText().toString();
        goods.info = mEt_info.getText().toString();
        goods.money_in_temp = Integer.parseInt(mEt_money_in.getText().toString());
        goods.money_out_temp = Integer.parseInt(mEt_money_out.getText().toString());
        goods.name = mEt_name.getText().toString();
        goods.image_url_local = image_path;

    }

}

package com.ws.coyc.wsnote.UI.DialogFragment.GoodsDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by coyc on 16-9-11.
 */

public class EditGoodsDialog extends GoodsDialog{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = super.onCreateView(inflater, container, savedInstanceState);



        mTv_title.setText("编辑商品");

        mBt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!check())
                {
                    return;
                }
                createGoods();

                onGetGoods.onGetGoods(goods);
                EditGoodsDialog.this.dismiss();
            }
        });
        return view;
    }

    public void createGoods()
    {
        super.createGoods();
        goods.count =Integer.parseInt(mEt_count.getText().toString());
    }
}

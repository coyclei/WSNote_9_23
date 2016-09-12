package com.ws.coyc.wsnote.UI.DialogFragment.GoodsDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by coyc on 16-9-11.
 */

public class AddGoodsDialog extends GoodsDialog{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = super.onCreateView(inflater, container, savedInstanceState);

        mBt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!check())
                {
                    return;
                }
                createGoods();
                onGetGoods.onGetGoods(goods);
                AddGoodsDialog.this.dismiss();
            }
        });
        return view;
    }

    public void createGoods()
    {
        super.createGoods();
        if(mEt_count.getText().length()==0)
        {
            goods.count = 0;
        }else
        {
            goods.count =Integer.parseInt(mEt_count.getText().toString());
        }

    }
}

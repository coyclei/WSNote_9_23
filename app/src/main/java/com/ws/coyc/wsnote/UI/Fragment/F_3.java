package com.ws.coyc.wsnote.UI.Fragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.ws.coyc.wsnote.Data.BillInfoOver;
import com.ws.coyc.wsnote.Data.DataManager;
import com.ws.coyc.wsnote.Data.Goods;
import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.SQLiteHelper.Utils.l;
import com.ws.coyc.wsnote.UI.Activity.ChartActivity;
import com.ws.coyc.wsnote.UI.Adapter.Info3Adapter;
import com.ws.coyc.wsnote.UI.Adapter.ListPosition;
import com.ws.coyc.wsnote.UI.PopUp.SetTextPopup.OnDescribeInterface3;
import com.ws.coyc.wsnote.UI.PopUp.SetTextPopup.OnShow3Interface;
import com.ws.coyc.wsnote.UI.PopUp.SetTextPopup.Show3Wind;
import com.ws.coyc.wsnote.UI.PopUp.SetTextPopup.TextEditPopWind3;
import com.ws.coyc.wsnote.Utils.MyColor;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by leipe on 2016/3/13.
 */
public class F_3 extends Fragment {



    private View mView;
    private ListView mList;
    private ImageView mIv_bc;
    public static Info3Adapter mAdapter;
    public static Context mContext;
    public  ArrayList<BillInfoOver> infos = new ArrayList<>();
    private Button mBt_add;

    private LinearLayout more;

    private boolean isChooseMode = false;
    private Button mBt_delete;
    private Button mBt_quite;

    private RelativeLayout mRl_count;
    private TextView mTv_bill_count;
    private TextView mTv_bill_money;

    private int bill_money = 0;



    private ListPosition listPosition = new ListPosition();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.f_3, container, false);

        initData();

        initView();

        return mView;
    }


    public static Handler mHandler;
    public static final int Add_One_OverInfo = 0;

    private void initData() {
        mContext = getContext();
        infos = DataManager.getInstance().infos_over;
        mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case Add_One_OverInfo:
                        Bundle data = msg.getData();

                        addInfoDataIn3(data.getString("name",""),data.getString("text",""),
                                data.getString("prise_in","")
                                ,data.getString("prise_out","")
                                ,data.getString("phone")
                                ,data.getString("address")
                                ,data.getString("image_path")
                        );
                        break;
                }
            }
        };

    }

    private void initView() {

        mRl_count = (RelativeLayout) mView.findViewById(R.id.rl_count);
        mRl_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChartActivity.class);
                mContext.startActivity(intent);
            }
        });

        mTv_bill_count = (TextView) mView.findViewById(R.id.tv_bill_count);
        mTv_bill_money = (TextView) mView.findViewById(R.id.tv_bill_money);
        initBillText();

        more = (LinearLayout) mView.findViewById(R.id.ll_more);

        mBt_delete  = (Button) mView.findViewById(R.id.bt_delete);
        mBt_quite  = (Button) mView.findViewById(R.id.bt_quite);

        mBt_delete.setOnClickListener(clickListener);
        mBt_quite.setOnClickListener(clickListener);


        mBt_add = (Button) mView.findViewById(R.id.bt_add);
        mBt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();

                Goods goods = new Goods("sp1","is good for sleep",100,200);
                int re = (int)DataManager.getInstance().goodsTable.insert(goods.getContentValues());
                l.l("add one goods : "+re+"");
            }
        });

        mList = (ListView) mView.findViewById(R.id.lv_list);
        mList.setBackgroundColor(Color.argb(40,0,0,0));
        mAdapter = new Info3Adapter(infos,getActivity(),listPosition);

        mList.setAdapter(mAdapter);
        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!isChooseMode)
                {
//                    InChooseMode();
//                    infos_wait.get(i).isChoose = true;
//                    mAdapter.notifyDataSetChanged();
//                    mList.setSelection(i);
                }

                return true;
            }
        });

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {
                if(isChooseMode)
                {
                    if(infos.get(position).isChoose)
                    {
                        infos.get(position).isChoose = false;
                    }else
                    {
                        infos.get(position).isChoose = true;
                    }
                    mAdapter.notifyDataSetChanged();
                    mList.setSelection(position);
                }else
                {

                    if(listPosition.position == position)
                    {
                        new Show3Wind(getActivity(), infos.get(position).person.name, infos.get(position).goods, infos.get(position).all_in_money+"", infos.get(position).all_out_money+"",
                                "账单信息", 400, true, new OnShow3Interface() {
                            @Override
                            public void onChange(String name, String text, String prise_in, String prise_out,String phone,String address,String path) {
                                infos.get(position).person.name = name;
                                infos.get(position).person.address1 = address;
                                infos.get(position).person.phone = phone;
                                infos.get(position).goods = text;
                                infos.get(position).all_out_money = Integer.parseInt(prise_out);
                                infos.get(position).all_in_money =  Integer.parseInt(prise_in);
                                infos.get(position).image_url =  path;

                                mAdapter.notifyDataSetChanged();

                                infos.get(position).update();
                                initBillText();
                            }

                            @Override
                            public void onDelete() {

                                removeOnInfoIn(position);
                                initBillText();
                            }
                        },infos.get(position).person.phone,infos.get(position).person.address1,infos.get(position).image_url).show();
                    }else
                    {
                        listPosition.position = position;
                        mAdapter.notifyDataSetChanged();
                    }

                }
            }
        });


    }

    private void initBillText() {
        initBillCountText();
        initBillMoneyText();
    }

    private void initBillCountText() {
        if(infos.size() == 0)
        {
            mTv_bill_count.setText("");
        }else
        {
            mTv_bill_count.setText("总账单数:"+infos.size());
        }
    }

    private void initBillMoneyText() {

        bill_money = 0;
        int size = infos.size();
        for(int i = 0;i<size;i++)
        {
            bill_money += (infos.get(i).all_out_money-infos.get(i).all_in_money);
        }
        if(bill_money>=0)
        {
            mTv_bill_money.setText("总利润:"+bill_money);
            mTv_bill_money.setTextColor(MyColor.coffe_5);
        }else if(bill_money<0)
        {
            mTv_bill_money.setText("亏损:"+(-bill_money));
            mTv_bill_money.setTextColor(MyColor.red_5);
        }
        if(infos.size()==0)
        {
            mTv_bill_money.setText("");
        }
    }

    public static void add(Activity activity,String name,String goods,OnDescribeInterface3 onDescribeInterface3,String phone,String address,String path)
    {
        add(activity,name,goods,"",onDescribeInterface3,phone,address,path);
    }

    public void add()
    {
        add(getActivity(),"","","",onDescribeInterface3,"","","");
    }

    private OnDescribeInterface3 onDescribeInterface3 =  new OnDescribeInterface3() {
        @Override
        public void onDescribed(String name, String text, String prise_in, String prise_out,String phone,String address,String path) {

            addInfoDataIn3(name, text, prise_in, prise_out,phone,address,path);
        }
    };

    public void addInfoDataIn3(String name, String text, String prise_in, String prise_out,String phone,String address,String path) {
        BillInfoOver info3 = new BillInfoOver();
        info3.dateStart = new Date();
//        info3.end_date = new Date();
        info3.all_out_money = Integer.parseInt(prise_out);
        info3.all_in_money = Integer.parseInt(prise_in);
        info3.person.name = name;
        info3.person.phone = phone;
        info3.person.address1 = address;
        info3.goods = text;
        info3.image_url = path;


        DataManager.getInstance().addOverInfoAnyWay(info3);
        mAdapter.notifyDataSetChanged();
        initBillText();
    }

    public static void add(Activity activity, String name, String goods, String in_prise, OnDescribeInterface3 onDescribeInterface3,String phone,String address,String path) {
        new TextEditPopWind3(activity,name,goods,in_prise, "新增账单", 400, true,onDescribeInterface3,phone,address,path).show();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public void updateList() {
        infos = DataManager.getInstance().infos_over;
        mAdapter = new Info3Adapter(infos,getActivity(),listPosition);
        mList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        initBillText();
    }

    public void InChooseMode()
    {
        isChooseMode = true;
        more.setVisibility(View.VISIBLE);
        mBt_add.setVisibility(View.INVISIBLE);
    }

    public void removeOnInfoIn(int position) {

        DataManager.getInstance().removeOverInfo(infos.get(position));
        mAdapter.notifyDataSetChanged();
        initBillText();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.bt_delete:
                    OutChooseMode();
                    delectOldInfo();
                    break;



                case R.id.bt_quite:
                    OutChooseMode();
                    refrshAll();
                    break;
            }
        }
    };

    public void OutChooseMode()
    {
        isChooseMode = false;
        more.setVisibility(View.INVISIBLE);
        mBt_add.setVisibility(View.VISIBLE);
    }

    private void delectOldInfo() {
        int size = infos.size();
        for(int i = 0;i<size;i++)
        {
            infos.get(i).deleteChoose();
        }
        DataManager.getInstance().initInfo_over();
        mAdapter.notifyDataSetChanged();
        initBillText();
    }

    private void refrshAll() {
        int size = infos.size();
        for(int i = 0;i<size;i++)
        {
            infos.get(i).isChoose = false;
        }
        mAdapter.notifyDataSetChanged();

    }

}

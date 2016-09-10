package com.ws.coyc.wsnote.UI.Fragment;
import android.app.Activity;
import android.content.Context;
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


import com.ws.coyc.wsnote.Data.BillInfoIng;
import com.ws.coyc.wsnote.Data.DataManager;
import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.UI.Adapter.Info2Adapter;
import com.ws.coyc.wsnote.UI.Adapter.ListPosition;
import com.ws.coyc.wsnote.UI.PopUp.SetTextPopup.OnDescribeInterface2;
import com.ws.coyc.wsnote.UI.PopUp.SetTextPopup.OnDescribeInterface3;
import com.ws.coyc.wsnote.UI.PopUp.SetTextPopup.OnShow2Interface;
import com.ws.coyc.wsnote.UI.PopUp.SetTextPopup.Show2Wind;
import com.ws.coyc.wsnote.UI.PopUp.SetTextPopup.TextEditPopWind2;

import java.util.ArrayList;


/**
 * Created by leipe on 2016/3/13.
 */
public class F_2 extends Fragment {



    private View mView;
    private ListView mList;
    private ImageView mIv_bc;
    public static Info2Adapter mAdapter;
    public  ArrayList<BillInfoIng> infos = new ArrayList<>();
    public static Context mContext;
    private Button mBt_add;

    private LinearLayout more;
    private boolean isChooseMode = false;
    private Button mBt_delete;
    private Button mBt_quite;

    private ListPosition listPosition = new ListPosition();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.f_2, container, false);

        initData();

        initView();

        return mView;
    }



    public static final int Update_List = 0;
    public static Handler mHandler;
    public static final int Add_One_IngInfo = 0;

    private void initData() {

        mContext = getContext();
        infos = DataManager.getInstance().infos_ing;
        mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case Add_One_IngInfo:
                        Bundle data = msg.getData();
                        addOneInfoDataIn2(data.getString("name",""),data.getString("text",""),
                                data.getString("prise_in",""),data.getBoolean("isFH"),data.getBoolean("isFK")
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

        more = (LinearLayout) mView.findViewById(R.id.ll_more);


        mBt_add = (Button) mView.findViewById(R.id.bt_add);
        mBt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                add();
            }
        });

        mBt_delete  = (Button) mView.findViewById(R.id.bt_delete);
        mBt_quite  = (Button) mView.findViewById(R.id.bt_quite);

        mBt_delete.setOnClickListener(clickListener);
        mBt_quite.setOnClickListener(clickListener);


        mList = (ListView) mView.findViewById(R.id.lv_list);
        mList.setBackgroundColor(Color.argb(40,0,0,0));
        mAdapter = new Info2Adapter(infos,getActivity(),listPosition);

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
                        listPosition.position = position;
                        preToF3(position);
                    }else
                    {
                        listPosition.position = position;
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void preToF3(final int position) {
        boolean fk ;
        boolean fh;
        if(infos.get(position).fh_state.equals(BillInfoIng.FH_STATE_TRUE))
        {
            fh = true;
        }else
        {
            fh = false;
        }

        if(infos.get(position).fk_state.equals(BillInfoIng.FK_STATE_TRUE))
        {
            fk = true;
        }else
        {
            fk = false;
        }
        new Show2Wind(getActivity(), infos.get(position).person.name, infos.get(position).goods
                , infos.get(position).all_in_money+"", fk, fh, "订单信息", 400, true, new OnShow2Interface() {
            @Override
            public void onChange(String name, String text, String prise_in, boolean isfh, boolean isfk,String phone,String address,String path) {
                infos.get(position).person.name = name;
                infos.get(position).person.address1 = address;
                infos.get(position).person.phone = phone;
                infos.get(position).goods = text;
                infos.get(position).image_url = path;
                infos.get(position).all_in_money = Integer.parseInt(prise_in);
                if(isfh)
                {
                    infos.get(position).fh_state = BillInfoIng.FH_STATE_TRUE;
                }else
                {
                    infos.get(position).fh_state = BillInfoIng.FH_STATE_FALSE;
                }
                if(isfk)
                {
                    infos.get(position).fk_state = BillInfoIng.FK_STATE_TRUE;
                }else
                {
                    infos.get(position).fk_state = BillInfoIng.FK_STATE_FALSE;
                }

                mAdapter.notifyDataSetChanged();

                infos.get(position).update();
            }

            @Override
            public void onSend(String name, String text, String prise_in,String phone,String address,String path) {
                F_3.add(getActivity(),name, text, prise_in, new OnDescribeInterface3() {
                    @Override
                    public void onDescribed(String name, String text, String prise_in, String prise_out,String phone,String address,String path) {

                        Message message = new Message();
                        message.what = F_3.Add_One_OverInfo;
                        Bundle data = new Bundle();
                        data.putString("name",name);
                        data.putString("text",text);
                        data.putString("prise_in",prise_in);
                        data.putString("prise_out",prise_out);
                        data.putString("phone",phone);
                        data.putString("address",address);
                        data.putString("image_path",path);

                        message.setData(data);
                        F_3.mHandler.sendMessage(message);

                        removeOnInfoIn(position);
                    }
                }, phone, address, path);
            }



            @Override
            public void onDelete() {
                removeOnInfoIn(position);

            }
        },infos.get(position).person.phone,infos.get(position).person.address1,infos.get(position).image_url).show();
    }




    private  OnDescribeInterface2 onDescribeInterface2 = new OnDescribeInterface2() {
        @Override
        public void onDescribed(String name, String text, String prise, boolean isFH, boolean isFK,String phone,String address,String path) {
            addOneInfoDataIn2(name, text, prise, isFH, isFK,phone,address,path);
        }
    };

    public static void addOneInfoDataIn2(String name, String text, String prise, boolean isFH, boolean isFK,String phone,String address,String path) {
        BillInfoIng info2 = new BillInfoIng();
        info2.person.name = name;
        info2.goods = text;

        info2.person.address1 = address;
        info2.person.phone = phone;
        info2.image_url = path;

        info2.all_in_money = Integer.parseInt(prise);
        if(isFH)
        {
            info2.fh_state = BillInfoIng.FH_STATE_TRUE;
        }else
        {
            info2.fh_state = BillInfoIng.FH_STATE_FALSE;
        }

        if(isFK)
        {
            info2.fk_state = BillInfoIng.FK_STATE_TRUE;
        }else
        {
            info2.fk_state = BillInfoIng.FK_STATE_FALSE;
        }
        DataManager.getInstance().addIngInfoAnyWay(info2);
        mAdapter.notifyDataSetChanged();

    }


    public static void add(Activity activity, String name, String goods, OnDescribeInterface2 onDescribeInterface2,String phone,String address,String path)
    {
        TextEditPopWind2 textEditPopWind2 = new TextEditPopWind2(activity,name ,goods, "新增待完成订单", 400, true,onDescribeInterface2,phone,address,path );
        textEditPopWind2.show();
    }

    public void add() {
        add(getActivity(),"","",onDescribeInterface2,"","","");
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public void updateList() {
        listPosition.position = 0;
        infos = DataManager.getInstance().infos_ing;
        mAdapter = new Info2Adapter(infos,getActivity(),listPosition);
        mList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
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

    public void InChooseMode()
    {
        isChooseMode = true;
        more.setVisibility(View.VISIBLE);
        mBt_add.setVisibility(View.INVISIBLE);
    }
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
        DataManager.getInstance().initInfo_wait();
        mAdapter.notifyDataSetChanged();
    }


    public void removeOnInfoIn(int position) {
        DataManager.getInstance().removeIngInfo(infos.get(position));

        mAdapter.notifyDataSetChanged();
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

package com.ws.coyc.wsnote.UI.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.ws.coyc.wsnote.Data.DataManager;
import com.ws.coyc.wsnote.Data.BillInfoWait;
import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.SQLiteHelper.Utils.l;
import com.ws.coyc.wsnote.UI.Activity.MainActivity;
import com.ws.coyc.wsnote.UI.Adapter.Info1Adapter;
import com.ws.coyc.wsnote.UI.Adapter.ListPosition;
import com.ws.coyc.wsnote.UI.PopUp.SetTextPopup.OnDescribeInterface;
import com.ws.coyc.wsnote.UI.PopUp.SetTextPopup.OnDescribeInterface2;
import com.ws.coyc.wsnote.UI.PopUp.SetTextPopup.OnDescribeInterface3;
import com.ws.coyc.wsnote.UI.PopUp.SetTextPopup.OnShow1Interface;
import com.ws.coyc.wsnote.UI.PopUp.SetTextPopup.Show1Wind;
import com.ws.coyc.wsnote.UI.PopUp.SetTextPopup.TextEditPopWind;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by leipe on 2016/3/13.
 */
public class F_1 extends Fragment {

    private View mView;
    private ListView mList;
    private Info1Adapter mAdapter;
    public  ArrayList<BillInfoWait> infos ;

    private Button mBt_add;
    private Button mBt_delete;
    private Button mBt_quite;

    private LinearLayout more;

    private boolean isChooseMode = false;
    private ListPosition listPosition = new ListPosition();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.f_1, container, false);

        initData();

        initView();

        return mView;
    }



    public static final int Update_List = 0;

    private void initData() {
        infos = DataManager.getInstance().infos_wait;
    }

    private void initView() {


        more = (LinearLayout) mView.findViewById(R.id.ll_more);

        mBt_delete  = (Button) mView.findViewById(R.id.bt_delete);
        mBt_quite  = (Button) mView.findViewById(R.id.bt_quite);

        mBt_delete.setOnClickListener(clickListener);
        mBt_quite.setOnClickListener(clickListener);

        mBt_add = (Button) mView.findViewById(R.id.bt_add);
        mBt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Dialog_addWait dialog_addWait = new Dialog_addWait();
//                dialog_addWait.setTitle("新增订单q");
//                dialog_addWait.setDescribeInterface(new OnDescribeInterface() {
//                    @Override
//                    public void onDescribed(String name, String text, String info, String address, String image_path) {
//                        addOneWaitInfo(name, text, info, address, image_path);
//                    }
//                });
//                FragmentManager fragmentManager = getActivity().getFragmentManager();
//
//                dialog_addWait.show(fragmentManager,"");

                TextEditPopWind textEditPopWind = new TextEditPopWind(getActivity(), "", "新增订单", 350, true, new OnDescribeInterface() {
                    @Override
                    public void onDescribed(String name,String text,String phone,String address,String image_path) {
                        addOneWaitInfo(name, text, phone, address, image_path);
                    }
                },"","","");
                textEditPopWind.show();
            }
        });




        mList = (ListView) mView.findViewById(R.id.lv_list);
        mList.setBackgroundColor(Color.argb(40,0,0,0));
        mAdapter = new Info1Adapter(infos,getActivity(),listPosition);



        mList.setAdapter(mAdapter);
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
                        preToF2F3(position);
                    }else
                    {
                        listPosition.position = position;
                        mAdapter.notifyDataSetChanged();
                    }

                }

            }
        });

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


    }

    private void addOneWaitInfo(String name, String text, String phone, String address, String image_path) {
        BillInfoWait info = new BillInfoWait();
        info.dateStart = new Date();
        info.person.name = name;
        info.goods = text;
        info.person.phone = phone;
        info.person.address1 = address;
        l.l("image_path"+image_path);
        info.image_url = image_path;

        DataManager.getInstance().addWaitInfoAnyWay(info);
        mAdapter.notifyDataSetChanged();
    }

    private void preToF2F3(final int position) {
        new Show1Wind(getActivity(), infos.get(position).person.name, infos.get(position).goods
                ,infos.get(position).person.phone,infos.get(position).person.address1, "订单信息", 350, true, new OnShow1Interface() {
            @Override
            public void onChange(String name, String text,String phone,String address,String path) {
                infos.get(position).person.name = name;
                infos.get(position).goods = text;
                infos.get(position).image_url = path;
                infos.get(position).person.phone = phone;
                infos.get(position).person.address1 = address;

                mAdapter.notifyDataSetChanged();
                infos.get(position).update();

            }

            @Override
            public void onBuy(String name, String text,String phone,String address,String path) {
                F_2.add(getActivity(),name, text, new OnDescribeInterface2() {
                    @Override
                    public void onDescribed(String name, String text, String prise_in, boolean isFH, boolean isFK,String phone,String address,String path) {

                        Message message = new Message();
                        message.what = F_2.Add_One_IngInfo;
                        Bundle data = new Bundle();
                        data.putString("name",name);
                        data.putString("text",text);
                        data.putString("prise_in",prise_in);
                        data.putBoolean("isFH",isFH);
                        data.putBoolean("isFK",isFK);
                        data.putString("info",phone);
                        data.putString("address",address);
                        data.putString("image_path",path);
                        message.setData(data);
                        F_2.mHandler.sendMessage(message);

                        Message msg = new Message();
                        msg.what = MainActivity.ChangePage;
                        msg.arg1 = 1;
                        MainActivity.mHandler.sendMessage(msg);

                        removeOnInfoIn1(position);
                    }
                },phone,address,path);
            }

            @Override
            public void onSend(String name, String text,String phone,String address,String path) {
                F_3.add(getActivity(),name, text, new OnDescribeInterface3() {
                    @Override
                    public void onDescribed(String name, String text, String prise_in, String prise_out,String phone,String address,String path) {


                        Message message = new Message();
                        message.what = F_3.Add_One_OverInfo;
                        Bundle data = new Bundle();
                        data.putString("name",name);
                        data.putString("text",text);
                        data.putString("prise_in",prise_in);
                        data.putString("prise_out",prise_out);
                        data.putString("info",phone);
                        data.putString("address",address);
                        data.putString("image_path",path);

                        message.setData(data);
                        F_3.mHandler.sendMessage(message);

                        Message msg = new Message();
                        msg.what = MainActivity.ChangePage;
                        msg.arg1 = 2;
                        MainActivity.mHandler.sendMessage(msg);

                        removeOnInfoIn1(position);
                    }
                }, phone, address, path);
            }

            @Override
            public void onDelete() {
                removeOnInfoIn1(position);
            }
        },infos.get(position).image_url).show();
    }

    public void removeOnInfoIn1(int position) {

        DataManager.getInstance().removeWaitInfo(infos.get(position));

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

    private void refrshAll() {
        int size = infos.size();
        for(int i = 0;i<size;i++)
        {
            infos.get(i).isChoose = false;
        }
        mAdapter.notifyDataSetChanged();

    }

    private void delectOldInfo() {

        int size = infos.size();
        for(int i = 0;i<size;i++)
        {
            infos.get(i).deleteChoose();
        }
        DataManager.getInstance().initInfo_ing();
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public void updateList() {
        listPosition.position = 0;
        infos = DataManager.getInstance().infos_wait;
        mAdapter = new Info1Adapter(infos,getActivity(),listPosition);
        mList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

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


}

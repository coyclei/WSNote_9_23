package com.ws.coyc.wsnote.UI.PopUp.SetTextPopup.PopUp.DateSelect;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ws.coyc.wsnote.Data.DateInfo;
import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.UI.Adapter.DateSelecteListAdapter;
import com.ws.coyc.wsnote.UI.Adapter.ListPosition;
import com.ws.coyc.wsnote.UI.PopUp.SetTextPopup.BgPopWind;
import com.ws.coyc.wsnote.UI.PopUp.SetTextPopup.DensityUtils;
import com.ws.coyc.wsnote.UI.PopUp.SetTextPopup.OnDescribeInterface;
import com.ws.coyc.wsnote.Utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;

public class DateSelectPopWind {

	private PopupWindow mPopup;
	private Context mContext;
	private View v;
	private int mWindhight = 0;
	private View ac;

	private ListView mLv_year;
	private ListView mLv_mouth;
	private ListView mLv_day;

	private DateSelecteListAdapter adapter_year;
	private DateSelecteListAdapter adapter_mouth;
	private DateSelecteListAdapter adapter_day;

	private ArrayList<DateInfo> infos_year = new ArrayList<>();
	private ArrayList<DateInfo> infos_mouth = new ArrayList<>();
	private ArrayList<DateInfo> infos_day = new ArrayList<>();

	private ListPosition listPosition_year = new ListPosition(-1);
	private ListPosition listPosition_mouth = new ListPosition(-1);
	private ListPosition listPosition_day = new ListPosition(-1);

	private Button text;

	private OnSelectDateArea onSelectDateArea;

	/**
	 * @param c
     * @param h
     */
	public DateSelectPopWind(Context c,int h, boolean show,OnSelectDateArea s,View ac)
	{
		mContext = c;
		mWindhight = h;
		this.ac = ac;
		onSelectDateArea = s;
		Date current = new Date();
		listPosition_year.position = DateUtils.getYear(current)-2012;
		listPosition_mouth.position = DateUtils.getMouth(current)-1;
		listPosition_day.position = DateUtils.getDay(current)-1;

		initData();
		initPopupWind(show);
	}

	
	private void initData() {

		init_day(31);
		init_mouth(12);
		init_year(2012,2020);

		adapter_year = new DateSelecteListAdapter(infos_year,mContext,listPosition_year);
		adapter_mouth = new DateSelecteListAdapter(infos_mouth,mContext,listPosition_mouth);
		adapter_day = new DateSelecteListAdapter(infos_day,mContext,listPosition_day);
	}

	private void init_day(int k) {
		infos_day.clear();
		for(int i = 1;i<=k;i++)
		{
			infos_day.add(new DateInfo(i));
		}
	}

	private void init_mouth(int k) {

		infos_mouth.clear();
		for(int i = 1;i<=k;i++)
		{
			infos_mouth.add(new DateInfo(i));
		}
	}

	private void init_year(int start,int k) {
		if(start>k)
		{
			return;
		}
		infos_year.clear();
		for(int i = start;i<=k;i++)
		{
			infos_year.add(new DateInfo(i));
		}
	}

	@SuppressLint("InflateParams")
	private void initPopupWind(boolean show)
	{

		View view = LayoutInflater.from(mContext).inflate(R.layout.popupdateselect, null);
		v = view;

		mPopup = new PopupWindow(view, LayoutParams.WRAP_CONTENT, DensityUtils.dp2px(mContext, mWindhight), true);
		mPopup.setTouchable(true);
		mPopup.setBackgroundDrawable(view.getBackground());
		mPopup.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
			}
		});
		initView();

		if(show)
		{
			show();
		}
	}

	private void initView() {
		mLv_year = (ListView) v.findViewById(R.id.lv_year);
		mLv_mouth = (ListView) v.findViewById(R.id.lv_mouth);
		mLv_day = (ListView) v.findViewById(R.id.lv_day);

		mLv_year.setAdapter(adapter_year);
		mLv_mouth.setAdapter(adapter_mouth);
		mLv_day.setAdapter(adapter_day);


		mLv_year.setSelection(listPosition_year.position);
		mLv_mouth.setSelection(listPosition_mouth.position);
		mLv_day.setSelection(listPosition_day.position);

		if(listPosition_mouth.position==-1)
		{
			mLv_mouth.setVisibility(View.INVISIBLE);
		}
		if(listPosition_day.position == -1)
		{
			mLv_day.setVisibility(View.INVISIBLE);
		}

		mLv_year.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				if(listPosition_year.position == i)
				{
					listPosition_year.position = -1;
					listPosition_mouth.position = -1;
					listPosition_day.position = -1;
					mLv_mouth.setVisibility(View.INVISIBLE);
					mLv_day.setVisibility(View.INVISIBLE);
					adapter_mouth.notifyDataSetChanged();
					adapter_day.notifyDataSetChanged();
					text.setText("选择时间段");
				}else
				{
					listPosition_year.position = i;
					mLv_mouth.setVisibility(View.VISIBLE);
					if(listPosition_mouth.position == -1)
					{
						setYear();
					}else
					{
						if(listPosition_day.position == -1)
						{
							setMouth();
						}else
						{
							setDay();
						}
					}
				}

				adapter_year.notifyDataSetChanged();
			}
		});

		mLv_mouth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


				if(listPosition_mouth.position == i)
				{
					listPosition_mouth.position = -1;
					listPosition_day.position = -1;
					mLv_day.setVisibility(View.INVISIBLE);
					adapter_day.notifyDataSetChanged();
					setYear();
				}else
				{
					listPosition_mouth.position = i;
					mLv_day.setVisibility(View.VISIBLE);
					if(listPosition_day.position == -1)
					{
						setMouth();
					}else
					{
						setDay();
					}
				}
				adapter_mouth.notifyDataSetChanged();
			}
		});

		mLv_day.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				if(listPosition_day.position == i)
				{
					listPosition_day.position = -1;
					setMouth();
				}else
				{
					listPosition_day.position = i;
					setDay();
					chooseDayReturn();

				}
				adapter_day.notifyDataSetChanged();
			}
		});

		text = (Button) v.findViewById(R.id.tv_ok);
		setDay();
		text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(listPosition_year.position == -1)
				{
					Toast.makeText(mContext,"至少选择年份单位进行查询",Toast.LENGTH_SHORT).show();
					return;
				}else
				{
					if(listPosition_mouth.position == -1)
					{
						String year = infos_year.get(listPosition_year.position).date+"";
						String s1 = year+"-01-01 00:00:00";
						String s2 = year+"-12-31 23:59:59";
						doReturn(s1, s2,year+"年");
						// TODO: 16-8-26 use the two date search SQL and show in UI
					}else
					{
						if(listPosition_day.position == -1)
						{
							String year = infos_year.get(listPosition_year.position).date+"";
							String mouth = infos_mouth.get(listPosition_mouth.position).date+"";
							String s1 = year+"-"+mouth+"-01 00:00:00";
							String s2 = year+"-"+mouth+"-"
									+CountDays(infos_year.get(listPosition_year.position).date
									,infos_mouth.get(listPosition_mouth.position).date)+" 23:59:59";
							doReturn(s1, s2,year+"年"+mouth+"月份");
						}else
						{
							chooseDayReturn();
						}
					}
				}

			}
		});
	}

	private void chooseDayReturn() {
		String year = infos_year.get(listPosition_year.position).date+"";
		String mouth = infos_mouth.get(listPosition_mouth.position).date+"";
		String day = infos_day.get(listPosition_day.position).date+"";
		String s1 = year+"-"+mouth+"-"+day+" 00:00:00";
		String s2 = year+"-"+mouth+"-"+day+" 23:59:59";
		doReturn(s1, s2,year+"年"+mouth+"月"+day+"号");
	}

	private void doReturn(String s1, String s2,String date) {
		Date start =  DateUtils.ConverToDate_S(s1);
		Date end =  DateUtils.ConverToDate_S(s2);
		onSelectDateArea.onDescribed(start,end,date);
		mPopup.dismiss();
	}

	private void setDay() {
		try {

			text.setText("查看"+infos_year.get(listPosition_year.position).date+"年"+
					infos_mouth.get(listPosition_mouth.position).date+"月"+
					infos_day.get(listPosition_day.position).date+"号的账单");
		}catch (IndexOutOfBoundsException e)
		{
			e.printStackTrace();
		}

	}

	private void setMouth() {
		text.setText("查看"+infos_year.get(listPosition_year.position).date+"年"
                +infos_mouth.get(listPosition_mouth.position).date+"月份的账单");
		init_day(CountDays(listPosition_mouth.position+1,listPosition_year.position+1));
	}

	private int CountDays(int m,int y) {
		if(m==1||m==3||m==5||m==7||m==8||m==10||m==12)
		{
//
			return 31;
		}else
		{
			if(m ==2)
			{
				if(y%4 == 0)
				{
					return 29;
				}else
				{
					return 28;
				}
			}else
			{
				return 30;
			}
		}
	}

	private void setYear() {
		text.setText("查看"+infos_year.get(listPosition_year.position).date+"年的账单");
	}

	public void show()
	{
		mPopup.showAsDropDown(ac);
	}

	public void hide()
	{
		mPopup.dismiss();
	}





}









package com.ws.coyc.wsnote.UI.PopUp.SetTextPopup;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ws.coyc.wsnote.Data.DataManager;
import com.ws.coyc.wsnote.Data.OnFindPerson;
import com.ws.coyc.wsnote.Data.Person;
import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.SQLiteHelper.Utils.l;
import com.ws.coyc.wsnote.UI.Activity.MainActivity;
import com.ws.coyc.wsnote.UI.Activity.OnGetImage;
import com.ws.coyc.wsnote.UI.Adapter.PersonListAdapter;
import com.ws.coyc.wsnote.Utils.ImageLoader;
import com.ws.coyc.wsnote.Utils.SDFile;

import java.util.ArrayList;


public class PopUpWindFather{


	protected PopupWindow mPopup;
	protected Context mContext;
	protected Activity mActivity;

	protected TextView mTv_tille;
	protected EditText mText_name;
	protected EditText mText_goods;
	protected EditText mTv_phone;
	protected EditText mTv_address;
	protected ImageView mIm_image;


	protected String mTitle;
	protected String  name = "";
	protected String goods = "";
	protected String phone = "";
	protected String address = "";
	protected String image_path = "";
	protected BgPopWind bgPopWind;

	protected View v;
	protected ListView mLv_list;

	protected int mWindhight = 0;

	public PopUpWindFather()
	{

	}





	public void initData()
	{

	}


	public void initView()
	{
		mTv_tille = (TextView) v.findViewById(R.id.tv_title);
		mText_name = (EditText) v.findViewById(R.id.tv_name);
		mText_goods = (EditText) v.findViewById(R.id.tv_goods_info);
		mTv_phone = (EditText) v.findViewById(R.id.tv_info);
		mTv_address = (EditText) v.findViewById(R.id.tv_address);
		mLv_list = (ListView) v.findViewById(R.id.lv_list);
		mIm_image = (ImageView) v.findViewById(R.id.iv_image);
		initImage();


		mIm_image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				MainActivity.setOnGetImage(new OnGetImage() {
					@Override
					public void onGetImage(String path) {
						image_path = path;
						ImageLoader.getInstance().loadImage(path,mIm_image,5);
					}
				});
				MainActivity.mHandler.sendEmptyMessage(MainActivity.To_CAMARE);
				}
		});


		mTv_tille.setText(mTitle);
		mTv_tille.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				copyToEditText(mText_goods);
			}
		});

		mText_name.setText(name);
		mText_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean b) {
				if(!b)
				{
					mLv_list.setVisibility(View.INVISIBLE);
				}
			}
		});
		mText_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
				l.l(keyEvent.getCharacters());
				return false;
			}
		});
			mText_name.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
					l.l("beforeTextChanged "+charSequence);
				}

				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

					l.l("onTextChanged "+charSequence);
				}

				@Override
				public void afterTextChanged(Editable editable) {
					l.l("afterTextChanged "+editable);
					if(editable.length()<=0)
					{
						mLv_list.setVisibility(View.INVISIBLE);
						return;
					}
					findPerson(editable.toString());
				}
			});

		mText_goods.setText(goods);
		mTv_phone.setText(phone);
		mTv_address.setText(address);


		mText_name.setFocusable(true);
		mText_name.requestFocus();


		mLv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Person p = persons_temp.get(i);
				mText_name.setText(p.name);
				mTv_address.setText(p.address1);
				mTv_phone.setText(p.phone);
				mLv_list.setVisibility(View.INVISIBLE);
				mText_goods.setFocusable(true);
				mText_goods.requestFocus();
			}
		});

		longClickCopy(mTv_address);
		longClickCopy(mText_name);
		longClickCopy(mText_goods);
		longClickCopy(mTv_phone);



	}

	private void longClickCopy(final EditText editText) {
		editText.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
//				Toast.makeText(v.getContext(),"long c..",Toast.LENGTH_SHORT).show();

				copyToEditText(editText);

				return true;
			}
		});
	}

	public void copyToEditText(EditText editText)
	{
		ClipboardManager cm = (ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData data = cm.getPrimaryClip();

		try {
			editText.append(data.getItemAt(0).getText());
		}catch (NullPointerException e)
		{
			e.printStackTrace();
		}

	}

	private void initImage() {
		if(SDFile.CheckFileExistsABS(image_path))
		{
			mIm_image.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().loadImage(image_path,mIm_image,5);
		}
	}

	private ArrayList<Person> persons_temp ;


	private void findPerson(String string) {
		if(string.length()<=0)
		{
			return;
		}
		DataManager.getInstance().findPersonByKeyWord(string, new OnFindPerson() {
            @Override
            public void onfind(ArrayList<Person> persons) {
                if(persons.size()>0)
                {
                    mLv_list.setVisibility(View.VISIBLE);
					persons_temp = persons;
                    PersonListAdapter adapter = new PersonListAdapter(persons_temp,mContext);
                    mLv_list.setAdapter(adapter);
                }else
                {
                    l.l("onfind size == 0");
                    mLv_list.setVisibility(View.INVISIBLE);
                }

            }
        });
	}

	public void initPopupWind(boolean show)
	{

		bgPopWind = new BgPopWind(mContext,false);
		mPopup = new PopupWindow(v, LayoutParams.WRAP_CONTENT, DensityUtils.dp2px(mContext, 400), true);
//		mPopup = new PopupWindow(v, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		mPopup.setTouchable(true);
		mPopup.setBackgroundDrawable(v.getBackground());
		mPopup.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				bgPopWind.dismiss();
			}
		});
		if(show)
		{
			show();
		}
	}
	public void show()
	{
		mPopup.showAtLocation(v,Gravity.FILL_HORIZONTAL|Gravity.BOTTOM,0,0);
	}


	public boolean check()
	{

		if(mText_name.getText().length()==0)
		{
			Toast.makeText(mContext,"请输入姓名",Toast.LENGTH_SHORT).show();
			return false;
		}

		if(mText_goods.getText().length()==0)
		{
			Toast.makeText(mContext,"请输入商品信息",Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}





}









package com.ws.coyc.wsnote.UI.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.ws.coyc.wsnote.Data.DataManager;
import com.ws.coyc.wsnote.Data.OnFindBill;
import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.SQLiteHelper.SQLiteManager;
import com.ws.coyc.wsnote.SQLiteHelper.Utils.l;
import com.ws.coyc.wsnote.UI.Adapter.MyPagerAdapter;
import com.ws.coyc.wsnote.UI.Fragment.F_1;
import com.ws.coyc.wsnote.UI.Fragment.F_2;
import com.ws.coyc.wsnote.UI.Fragment.F_3;
import com.ws.coyc.wsnote.UI.Fragment.F_4;
import com.ws.coyc.wsnote.UI.PopUp.SetTextPopup.PopUp.DateSelect.DateSelectPopWind;
import com.ws.coyc.wsnote.UI.PopUp.SetTextPopup.PopUp.DateSelect.OnSelectDateArea;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * 主界面
 * 包括4个Fragment与底部操作栏
 */
public class MainActivity extends FragmentActivity {



    private ViewPager mViewPager;
    private RelativeLayout mRl_pager;
    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    private PagerAdapter mPagerAdapter;
    public static Handler mHandler;

    private F_1 mF_1;
    private F_2 mF_2;
    private F_3 mF_3;
    private F_4 mF_4;

    private RelativeLayout mRl_topBar;
    private TextView mTv_title;
    private RelativeLayout mRl_search;
    private ImageButton mIb_serach;
    private EditText mEt_serach;
    private TextView mTv_choose_date;

    private static final int REQUEST_CODE_TAKE_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        l.l("2233");
        Toast.makeText(getBaseContext(),"qqaaaqqqq",Toast.LENGTH_SHORT).show();



        initData();
        initView();
    }

    public static final int ChangePage = 0;
    public static final int To_CAMARE = 1;
    private void initData() {



        DataManager.getInstance().init(this);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case ChangePage:
                        int id = msg.arg1;
                        mViewPager.setCurrentItem(id);
                        break;
                    case To_CAMARE:
                        ToCamare();
//                        shoot();
                        break;

                }
            }
        };


        mF_1 = new F_1();
        mF_2 = new F_2();
        mF_3 = new F_3();
        mF_4 = new F_4();
        mFragments.add(mF_2);
        mFragments.add(mF_1);
        mFragments.add(mF_3);
        mFragments.add(mF_4);

//        receiver = new CameraBroadcastReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(android.hardware.Camera.ACTION_NEW_PICTURE);
//        registerReceiver(receiver, filter);
    }

//    private CameraBroadcastReceiver receiver;


    private void initView() {
        mRl_topBar = (RelativeLayout) findViewById(R.id.rl_titlebar);

        mTv_title = (TextView) findViewById(R.id.tv_title);
        mTv_title.setText("最近7天");
        mTv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mViewPager.getCurrentItem()<3)
                {
                    exitSerach();
                    dateSelectPopWind.show();
                }else
                {

                }
            }
        });
        mTv_title.setLongClickable(true);
        mTv_title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if(mViewPager.getCurrentItem()<3)
                {
                    DataManager.getInstance().toCurrentDate();
                    mF_1.updateList();
                    mF_2.updateList();
                    mF_3.updateList();
                    mTv_choose_date.setText("最近7天");
                    exitSerach();
                }

                return true;
            }
        });

        dateSelectPopWind = new DateSelectPopWind(getBaseContext(), 200, false, new OnSelectDateArea() {
            @Override
            public void onDescribed(Date start, Date end,String date) {
                DataManager.getInstance().refreshInfoByDate(start,end);
                mF_1.updateList();
                mF_2.updateList();
                mF_3.updateList();
                mTv_choose_date.setText(date);
            }
        }, mTv_title);

        mRl_search = (RelativeLayout) findViewById(R.id.rl_search);

        mRl_pager = (RelativeLayout) findViewById(R.id.rl_pager);

        mTv_choose_date = (TextView) findViewById(R.id.tv_choose_date);


        mViewPager = (ViewPager) findViewById(R.id.vp_pager);

        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(2);
        mViewPager.setOffscreenPageLimit(4);//设置最大页面缓存数(不含当前页面)
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                updateButtonBarByCurrentPageItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTv_title.setText("3 已完成");

        mIb_serach = (ImageButton) findViewById(R.id.bt_search);
        mEt_serach = (EditText) findViewById(R.id.et_search);
        mEt_serach.setVisibility(View.INVISIBLE);
        mEt_serach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() == 0)
                {
                    return;
                }
                DataManager.getInstance().findBillByKeyWord(mEt_serach.getText().toString(), new OnFindBill() {
                    @Override
                    public void onfind() {
                        mF_1.updateList();
                        mF_2.updateList();
                        mF_3.updateList();
                    }
                });
            }
        });

        mIb_serach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSerachMode)
                {
                    exitSerach();

                }else
                {
                    isSerachMode = true;
                    mEt_serach.setText("");
                    mEt_serach.setVisibility(View.VISIBLE);
                    mEt_serach.setFocusable(true);
                    mEt_serach.requestFocus();
                    mIb_serach.setImageResource(R.mipmap.colse);
                }
            }
        });

        mIb_serach.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Intent intent = new Intent(MainActivity.this,WSNoteInfo.class);
                startActivity(intent);

                return true;
            }
        });
    }

    private void exitSerach() {
        if(isSerachMode)
        {
            isSerachMode = false;
            mEt_serach.setVisibility(View.INVISIBLE);
            mIb_serach.setImageResource(R.mipmap.serach);
            DataManager.getInstance().refreshInfoByDate();
            mF_1.updateList();
            mF_2.updateList();
            mF_3.updateList();
        }

    }

    private boolean isSerachMode = false;




    /**
     * 页面滑动时，改变底部选中区域颜色
     */
    private void updateButtonBarByCurrentPageItem(int currentpage) {
        setBack();
        switch (currentpage) {
            case 1:
                mTv_title.setText("2 待采购");
                mRl_search.setVisibility(View.VISIBLE);
                mTv_choose_date.setVisibility(View.VISIBLE);
                break;
            case 0:
                mTv_title.setText("1 交易中");
                mRl_search.setVisibility(View.VISIBLE);
                mTv_choose_date.setVisibility(View.VISIBLE);
                break;
            case 2:
                mTv_title.setText("3 已完成");
                mRl_search.setVisibility(View.VISIBLE);
                mTv_choose_date.setVisibility(View.VISIBLE);
                break;
            default:
                mTv_title.setText("WsNote");
                mRl_search.setVisibility(View.INVISIBLE);
                mTv_choose_date.setVisibility(View.INVISIBLE);
                break;
        }
    }

    /**
     * 将按钮颜色设置为初始状态
     */
    private void setBack() {

    }

    private DateSelectPopWind dateSelectPopWind;

    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_title:



                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SQLiteManager.getInstance().close();
//        unregisterReceiver(receiver);
    }


    private void updateFragmentList()
    {
//        mF_music.updateList();
//        int size  =  mFragments.size();
//        for(int i = 2;i<size;i++)
//        {
//            ((F_FriendMusicList)mFragments.get(i)).updateList();
//        }
    }

    private String imagePath = "";
    public static OnGetImage onGetImage;
    public static void setOnGetImage(OnGetImage onGet) {
        onGetImage = onGet;
    }

    private void ToCamare() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //系统常量， 启动相机的关键
        String f = System.currentTimeMillis()+".jpg";
        imagePath = Environment.getExternalStorageDirectory()+"/WSNote/"+f;
        String  imageDir = Environment.getExternalStorageDirectory()+"/WSNote";
        File file_dir = new File(imageDir);
        if(!file_dir.exists())
            file_dir.mkdirs();
        Uri fileUri = Uri.fromFile(new File(imagePath));
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); //指定图片存放位置，指定后，在onActivityResult里得到的Data将为null
        startActivityForResult(openCameraIntent, REQUEST_CODE_TAKE_PICTURE); // 参数常量为自定义的request code, 在取返回结果时有用
    }

    public void shoot() {
        l.l("shoot");
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file;

            File dir = new File(Environment.getExternalStorageDirectory() + "/ycz/pictures");
            if(!dir.exists()){
                dir.mkdir();
            }
            file = new File(dir, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())+ ".jpg");
            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
                    MediaStore.EXTRA_OUTPUT, Uri.fromFile(file)),REQUEST_CODE_TAKE_PICTURE);
        } else {
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_CODE_TAKE_PICTURE:
                if ( resultCode == RESULT_OK) {
                    l.l("onActivityResult......."+imagePath);
                    if(onGetImage!=null)
                    {
                        onGetImage.onGetImage(imagePath);
                    }
                }
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
    }


}

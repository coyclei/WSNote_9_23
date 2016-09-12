package com.ws.coyc.wsnote.UI.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.ws.coyc.wsnote.Data.DataManager;
import com.ws.coyc.wsnote.Data.Goods;
import com.ws.coyc.wsnote.R;
import com.ws.coyc.wsnote.SQLiteHelper.Utils.l;
import com.ws.coyc.wsnote.UI.Adapter.GoodsInfoAdapter;
import com.ws.coyc.wsnote.UI.DialogFragment.GoodsDialog.AddGoodsDialog;
import com.ws.coyc.wsnote.UI.DialogFragment.GoodsDialog.EditGoodsDialog;
import com.ws.coyc.wsnote.UI.DialogFragment.GoodsDialog.GoodsDialog;
import com.ws.coyc.wsnote.UI.DialogFragment.GoodsDialog.OnGetGoods;

import java.io.File;
import java.util.ArrayList;

public class WareHouseActivity extends AppCompatActivity {

    private ArrayList<Goods> goodses;
    private GoodsInfoAdapter adapter;
    private ListView listView;

    private ImageButton ib_chart;
    private ImageButton ib_add;
    private ImageButton ib_serach;
    private ImageButton ib_more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);

        initData();

        initView();
    }


    public static Handler mHandler;
    public static final int To_CAMARE = 1;
    private void initData() {
        DataManager.getInstance().initPersonInfo();
        goodses = DataManager.getInstance().goodses;
        adapter = new GoodsInfoAdapter(goodses,getApplication());
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case To_CAMARE:
                        ToCamare();
                        break;

                }
            }
        };
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.lv_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EditGoodsDialog editGoodsDialog = new EditGoodsDialog();
                editGoodsDialog.setOnGetGoodsListener(new OnGetGoods() {
                    @Override
                    public void onGetGoods(Goods goods) {
                        DataManager.getInstance().updateGoods(goods);
                        adapter.notifyDataSetChanged();
                    }
                });
                editGoodsDialog.setGoods(goodses.get(i));
                editGoodsDialog.show(getFragmentManager(),"coyc");
            }
        });


        ib_chart = (ImageButton) findViewById(R.id.ib_chart);
        ib_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(WareHouseActivity.this,BarChartActivity.class);
                startActivity(intent);
            }
        });


        ib_add = (ImageButton) findViewById(R.id.ib_add);
        ib_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddGoodsDialog addGoodsDialog = new AddGoodsDialog();
                addGoodsDialog.setOnGetGoodsListener(new OnGetGoods() {
                    @Override
                    public void onGetGoods(Goods goods) {
                        DataManager.getInstance().addGoods(goods);
                        adapter.notifyDataSetChanged();
                    }
                });
                addGoodsDialog.show(getFragmentManager(),"coyc");

            }
        });


        ib_serach = (ImageButton) findViewById(R.id.ib_serach);
        ib_serach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        ib_more = (ImageButton) findViewById(R.id.ib_more);
        ib_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private static final int REQUEST_CODE_TAKE_PICTURE = 1;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        l.l("onActivityResult requestCode "+requestCode  +" resultCode "+resultCode);
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
    private String imagePath = "";
    public static OnGetImage onGetImage;
    public static void setOnGetImage(OnGetImage onGet) {
        onGetImage = onGet;
    }

}

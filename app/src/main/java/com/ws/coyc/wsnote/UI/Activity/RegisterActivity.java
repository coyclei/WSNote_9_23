//package com.ws.coyc.wsnote.UI.Activity;
//
//import android.app.Activity;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//
//import com.ws.coyc.wsnote.R;
//import com.ws.coyc.wsnote.UI.Layout.ImageSelectPopup.ImageSelectPopWind;
//import com.ws.coyc.wsnote.UI.Layout.ImageSelectPopup.OnImageSelectedInterface;
//import com.ws.coyc.wsnote.Utils.ImageLoader;
//
//import org.apache.http.conn.ssl.SSLSocketFactory;
//import org.apache.http.params.HttpConnectionParams;
//import org.apache.http.params.HttpParams;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.Socket;
//import java.net.SocketException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.net.URL;
//import java.net.UnknownHostException;
//import java.security.KeyManagementException;
//import java.security.KeyStore;
//import java.security.KeyStoreException;
//import java.security.NoSuchAlgorithmException;
//import java.security.UnrecoverableKeyException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//
//public class RegisterActivity extends Activity {
//
//
//    private EditText mEt_phonenum;
//    private EditText mEt_msgcode;
//    private EditText mEt_name;
//    private EditText mEt_password;
//    private ImageView mIv_face;
//    private EditText  mEt_nickname;
//
//    private ImageSelectPopWind imageSelectPopWind;
//    private String mImagePath = "";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_regist);
//
////        SMSSDK.initSDK(this, "109773d2ea9d8", "d29c06c41dae2ddb5ed72dc0bb6662ee");
////        SMSSDK.registerEventHandler(eh); //注册短信回调
//
//
//        mEt_phonenum = (EditText) findViewById(R.id.tv_phonenum);
//        mEt_msgcode = (EditText) findViewById(R.id.tv_msgcode);
//        mEt_name = (EditText) findViewById(R.id.tv_name);
//        mEt_nickname = (EditText) findViewById(R.id.tv_nickname);
//        mEt_password = (EditText) findViewById(R.id.tv_password);
//        mIv_face = (ImageView) findViewById(R.id.iv_face);
//
//    }
//
//    private Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//
//            switch (msg.what)
//            {
//                case 0:
//                    int k = msg.arg1;
//                    Log.i("coyc","handleMessage "+k);
//                    ((Button)findViewById(R.id.ib_getmsgcode)).setText(k+"s 重新获取");
//                    break;
//                case 1:
//                    setSMSCodeClickable(true);
//                    break;
//            }
//
//        }
//    };
//
//
//    public void onClick(View v)
//    {
//        switch (v.getId())
//        {
//            case R.id.ib_getmsgcode:
//                if (checkNum()) break;
//
//                setSMSCodeClickable(false);
//                /*
//                check info num and to send msg
//                 */
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
////                        SMSSDK.getVerificationCode("86",mEt_phonenum.getText().toString());
//
//                        int k = 60;
//                        while (k!=0)
//                        {
//                            Message msg = new Message();
//                            msg.what = 0;
//                            msg.arg1 = k;
//                            mHandler.sendMessage(msg);
//                            Log.i("coyc","while "+k);
//                            try {
//                                Thread.sleep(1000);
//                                k--;
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                        mHandler.sendEmptyMessage(1);
//                    }
//                }).start();
//
//                break;
//
//            case R.id.ib_register:
//
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
////                        SMSSDK.submitVerificationCode("86",mEt_phonenum.getText().toString(),mEt_msgcode.getText().toString());
//                    }
//                }).start();
//
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String httpUrl = "http://192.168.1.117:8080/joinTravel/user/signUp.do";
//
//                        Log.i("coyc", "let`s do !!!.");
////                        HttpPost httpPost = new HttpPost("http://192.168.1.117:8080/joinTravel/user/signUp.do");
////                        HttpResponse httpResponse;
////                        String result = "";
////                        List<NameValuePair> params = new ArrayList<>();
////                        params.add(new BasicNameValuePair("userBean", o.toString()));
////                        try {
////                            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
////                            httpResponse = new DefaultHttpClient().execute(httpPost);
////
////                            if (httpResponse.getStatusLine().getStatusCode() == 200) {
////                                result = EntityUtils.toString(httpResponse.getEntity());
////                                Log.i("coyc", "result.." + result);
////                            } else {
////                                Log.i("coyc", "http Request Error" + result);
////                            }
////                        } catch (IllegalStateException e) {
////                            Log.i("coyc", "IllegalStateException");
////
////                        } catch (IOException e) {
////                            e.printStackTrace();
////                            Log.i("coyc", "IOException");
////                        }
//                        try {
//                            l.l(doHttpsPost(httpUrl,o.toString()));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//
//
//
//                /*
//                假如成功完成了注册
//                 */
//
//                if (checkInfo()) break;
//
//                String name = mEt_name.getText().toString();
//                String nickname = mEt_nickname.getText().toString();
//                String password = mEt_password.getText().toString();
//                String phoneNum = mEt_phonenum.getText().toString();
//                PersonManager.getInstance().afterRegister(name,nickname,password,phoneNum,mImagePath);
//                MainActivity.mHandler.sendEmptyMessage(MainActivity.Login_In);
//                RegisterActivity.this.finish();
//                break;
//            case R.id.iv_face:
//
//                if(imageSelectPopWind == null)
//                {
//                    imageSelectPopWind = new ImageSelectPopWind(getBaseContext(), new OnImageSelectedInterface() {
//                        @Override
//                        public void onImageSelected(String path) {
//                            mImagePath = path;
//                            ImageLoader.getInstance().loadImage(path,(ImageView)findViewById(R.id.iv_face),3);
//                        }
//                    });
//                }
//                imageSelectPopWind.show();
//
//                break;
//            case R.id.iv_back:
//
//                RegisterActivity.this.finish();
//                break;
//
//        }
//    }
//
//    private boolean checkNum() {
//        String phoneNum = mEt_phonenum.getText().toString();
//        if(phoneNum.length()!=11)
//        {
//            Toast.makeText(getBaseContext(),"请输入正确的手机号",Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        return false;
//    }
//
//    private boolean checkInfo() {
//        String msgCode = mEt_msgcode.getText().toString();
//        String name = mEt_name.getText().toString();
//        String nickname = mEt_nickname.getText().toString();
//        String password = mEt_password.getText().toString();
//        if(name.length()==0)
//        {
//            Toast.makeText(getBaseContext(),"请输入您的真实姓名",Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        if(nickname.length()==0)
//        {
//            Toast.makeText(getBaseContext(),"请输入昵称",Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        if(password.length()==0)
//        {
//            Toast.makeText(getBaseContext(),"请输入密码",Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        if(checkNum())
//        {
//            return true;
//        }
//        if(msgCode.length()==0)
//        {
//            Toast.makeText(getBaseContext(),"请输入验证码",Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        if(mImagePath.length()==0)
//        {
//            Toast.makeText(getBaseContext(),"请选择头像",Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        return false;
//    }
//
//    private void setSMSCodeClickable(boolean b)
//    {
//        findViewById(R.id.ib_getmsgcode).setClickable(b);
//        if(b)
//        {
//            findViewById(R.id.ib_getmsgcode).setBackgroundColor(Color.BLUE);
//            ((Button)findViewById(R.id.ib_getmsgcode)).setText("重新获取");
//        }else
//        {
//            findViewById(R.id.ib_getmsgcode).setBackgroundColor(Color.GRAY);
//        }
//    }
//
//
//
//
//
//
//
//
////    public static String doHttpPost(String serverURL, String xmlString) throws Exception {
////        Log.d("doHttpPost", "serverURL="+serverURL);
////        HttpParams httpParameters = new BasicHttpParams();
////        HttpConnectionParams.setConnectionTimeout(httpParameters, 10*1000);
////        HttpConnectionParams.setSoTimeout(httpParameters, 10*1000);
////        HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
////        HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
////        HttpClient hc = new DefaultHttpClient();
////        HttpPost post = new HttpPost(serverURL);
////        post.addHeader("Content-Type", "text/xml");
////        post.setEntity(new StringEntity(xmlString, "UTF-8"));
////        post.setParams(httpParameters);
////        HttpResponse response = null;
////        try {
////            response = hc.execute(post);
////        } catch (UnknownHostException e) {
////            throw new Exception("Unable to access " + e.getLocalizedMessage());
////        } catch (SocketException e) {
////            throw new Exception(e.getLocalizedMessage());
////        }
////        int sCode = response.getStatusLine().getStatusCode();
////        Log.d("response code ", "sCode="+sCode);
////        if (sCode == HttpStatus.SC_OK) {
////            return EntityUtils.toString(response.getEntity());
////        } else
////            throw new Exception("StatusCode is " + sCode);
////    }
//
//
////    public static String doHttpsPost(String serverURL, String xmlString) throws Exception {
////        HttpParams httpParameters = new BasicHttpParams();
////        HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
////        HttpConnectionParams.setSoTimeout(httpParameters, 10000);
////        HttpClient hc = initHttpClient(httpParameters);
////        HttpPost post = new HttpPost(serverURL);
//////        post.addHeader("Content-Type", "text/xml");
////        post.setEntity(new StringEntity(xmlString, "UTF-8"));
////        post.setParams(httpParameters);
////        HttpResponse response = null;
////        try {
////            response = hc.execute(post);
////        } catch (UnknownHostException e) {
////            throw new Exception("Unable to access " + e.getLocalizedMessage());
////        } catch (SocketException e) {
////            throw new Exception(e.getLocalizedMessage());
////        }
////        int sCode = response.getStatusLine().getStatusCode();
////        if (sCode == HttpStatus.SC_OK) {
////            return EntityUtils.toString(response.getEntity());
////        } else
////            throw new Exception("StatusCode is " + sCode);
////    }
//
////    public static HttpClient initHttpClient(HttpParams params) {
////        try {
////            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
////            trustStore.load(null, null);
////
////            SSLSocketFactory sf = new SSLSocketFactoryImp(trustStore);
////            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
////
////            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
////            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
////
////            SchemeRegistry registry = new SchemeRegistry();
////            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
////            registry.register(new Scheme("https", sf, 443));
////
////            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
////
////            return new DefaultHttpClient(ccm, params);
////        } catch (Exception e) {
////            return new DefaultHttpClient(params);
////        }
////    }
//
//
//
////    public static class SSLSocketFactoryImp extends SSLSocketFactory {
////        final SSLContext sslContext = SSLContext.getInstance("TLS");
////
////        public SSLSocketFactoryImp(KeyStore truststore) throws NoSuchAlgorithmException,
////                KeyManagementException, KeyStoreException, UnrecoverableKeyException {
////            super(truststore);
////
////            TrustManager tm = new X509TrustManager() {
////                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
////                    return null;
////                }
////
////                @Override
////                public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
////                                               String authType) throws java.security.cert.CertificateException {
////                }
////
////                @Override
////                public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
////                                               String authType) throws java.security.cert.CertificateException {
////                }
////            };
////            sslContext.init(null, new TrustManager[] {
////                    tm
////            }, null);
////        }
////
////        @Override
////        public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
////                throws IOException, UnknownHostException {
////            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
////        }
////
////        @Override
////        public Socket createSocket() throws IOException {
////            return sslContext.getSocketFactory().createSocket();
////        }
////    }
//
//}

package com.sincar.customer.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.sincar.customer.R;
import com.sincar.customer.util.Util;
import com.sincar.customer.preference.PreferenceManager;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 2020.02.17 spirit
 * 네트워크 통신 컨트롤러
 * 1. 프로그래스 바 컨트롤
 * 2. 서버 통신 컨트롤 및 데이터 리턴
 */
public class NetWorkController {

    /**
     * 요청 완료 인터페이스 설정
     * @param listener
     */
    public void setHttpConnectionListner(NetworkControllerListener listener) {
        this.networkControllerListener = listener;
    }

    /**
     * 요청 완료에 대한 리스너 인터페이스
     */
    public static interface NetworkControllerListener {
        public abstract void responseComplete(String url, String result, int responseCode);
    }

    /**
     * 요청 완료 인터페이스 설정(다이얼로그)
     * @param onClickListener
     */
    public void setHttpConnectionListner(android.content.DialogInterface.OnClickListener onClickListener) {
        // TODO Auto-generated method stub
        this.networkControllerListener = (NetworkControllerListener) onClickListener;
    }

    // 리턴 될 인터페이스
    private NetworkControllerListener networkControllerListener;
    // 프로그래스 바
    private ProgressDialog mProgressDialog;
    // 연결 URL
    public String mStringUrl;
    public URL mUrl;
    private Context mContext;
    private HttpURLConnection httpURLConnection;
    public StringBuilder builder;

    /**
     * 기본 네트워크 통신 생성자
     * @param url     : 연결할 URL
     * @param context
     * @throws MalformedURLException
     */
    public NetWorkController(String url, Context context) throws MalformedURLException {
        // TODO Auto-generated constructor stub
        mStringUrl = url;
        mUrl = new URL(mStringUrl);
        mContext = context;
        builder = new StringBuilder();
    }

    /**
     * 프로그래스 다이얼로그 보여주기
     */
    private void showDialog() {
        try {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage(mContext.getString(R.string.connect_server_msg));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 기본 통신 파라미터 (key, value) 셋팅
     * @param postParams
     */
    public void sendParams(HashMap<String, String> postParams) {
        showDialog();
        // GET 방식으로 KEY & VALUE 로 변경
        String param = getParams(postParams);
        new NetworkThread(param).execute();
    }

    /**
     * Json 데이터 통신 파라미터 셋팅
     * Json + HashMap 전송
     * @param jsonObject
     */
    public void sendParamAddJson(JSONObject jsonObject, String jsonKey) {
        showDialog();
        // Json 데이터 기본 필수 값
        HashMap<String, String> postParams = new HashMap<>();
        // 사용자 코드
//        postParams.put("dbs", PreferenceManager.getInstance().getDbs());
        // 기업 코드
        postParams.put("uid", PreferenceManager.getInstance().getUid());
        // DB 서버 접속 URL (jsdbUrl - 업체 별 DB 접속 경로가 상이하여 프리퍼런스 사용 x, 보안 상 문제도 있음)
//        postParams.put("cid", PreferenceManager.getInstance().getCid());
        // 전송할 Json data
        postParams.put(jsonKey, jsonObject.toString());
        // GET 방식으로 KEY & VALUE 로 변경
        String param = getParams(postParams);
        new NetworkThread(param).execute();
    }

    /**
     * Json 데이터 통신 파라미터 셋팅
     * Json + HashMap 전송
     * @param jsonObject
     */
    public void sendParamAddJsonNotDilaog(JSONObject jsonObject, String jsonKey) {
        // Json 데이터 기본 필수 값
        HashMap<String, String> postParams = new HashMap<>();
        // 사용자 코드
//        postParams.put("dbs", PreferenceManager.getInstance().getDbs());
        // 기업 코드
        postParams.put("uid", PreferenceManager.getInstance().getUid());
        // DB 서버 접속 URL (jsdbUrl - 업체 별 DB 접속 경로가 상이하여 프리퍼런스 사용 x, 보안 상 문제도 있음)
//        postParams.put("cid", PreferenceManager.getInstance().getCid());
        // 전송할 Json data
        postParams.put(jsonKey, jsonObject.toString());
        // GET 방식으로 KEY & VALUE 로 변경
        String param = getParams(postParams);
        new NetworkThread(param).execute();
    }

    /**
     * MAP 으로 된 파라메터를 String 으로 변환
     * @param map
     * @return String
     */
    public String getParams(HashMap map) {
        StringBuffer sb = new StringBuffer();
        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            String param = (String) map.get(key);
            if (TextUtils.isEmpty(param)) {
                return "";
            }else{
                try {
                    param = URLEncoder.encode(param, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    return "";
                }
                sb.append(key + "=");
                sb.append(param + "&");
            }
        }
        return sb.toString();
    }

    /**
     * 프로그래스 다이얼로그 해제
     */
    public void dismiss() throws NullPointerException {
        try {
            if (mProgressDialog == null) {
                httpURLConnection.disconnect();
                return;
            }
            mProgressDialog.dismiss();
            httpURLConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * KEY & VALUE 데이터 전송
     */
    public class NetworkThread extends AsyncTask<Void, Void, Void> {
        String params;
        private static final int msgCANNOT_CONNECTION = 1;
        int mode = 0;
        private int responseCode = 0;

        public NetworkThread(String params) {
            this.params = params.toString();
        }

        public void setParam(String params) {
            this.params = params.toString();
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Void doInBackground(Void... param) {

            try {
                // URL 연결
                httpURLConnection = (HttpURLConnection) mUrl.openConnection();
//                if (isSession) {
//                    if (isSessionFalse) {
//                        httpURLConnection.setRequestProperty("cookie", mCookies);
//                    }
//                }
                httpURLConnection.setDefaultUseCaches(false);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setInstanceFollowRedirects(false);
                httpURLConnection.setConnectTimeout(15000);
                BufferedOutputStream writer = new BufferedOutputStream(httpURLConnection.getOutputStream());
                if (params != null) {
                    writer.write(this.params.toString().getBytes());
                    writer.flush();
                }

                // requestCode 확인
                // 200 : 성공
                // 기타 : 실패
                responseCode = httpURLConnection.getResponseCode();

                if (httpURLConnection.getResponseCode() == 200) {
                    InputStreamReader tmp = new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    String str;
                    while ((str = reader.readLine()) != null) {
                        builder.append(str + "\n");
                    }

//                    isSession = setSession();
                    publishProgress();
                } else {
                    mode = msgCANNOT_CONNECTION;
                    publishProgress();
                }

            } catch (FileNotFoundException e) {
                mode = msgCANNOT_CONNECTION;
                publishProgress();
                e.printStackTrace();

            } catch (UnsupportedEncodingException e) {
                mode = msgCANNOT_CONNECTION;
                publishProgress();
                e.printStackTrace();

            } catch (IOException e) {
                mode = msgCANNOT_CONNECTION;
                publishProgress();
                e.printStackTrace();
            } catch (NullPointerException e) {
                mode = msgCANNOT_CONNECTION;
                publishProgress();
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e) {
                mode = msgCANNOT_CONNECTION;
                publishProgress();
                e.printStackTrace();
            }
            return null;
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onProgressUpdate(Progress[])
         */
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            // 성공 시
            if (mode == 0) {
                if (mProgressDialog != null) {
                    dismiss();
                }

                if (networkControllerListener != null) {
                    // 리턴 셋팅
                    networkControllerListener.responseComplete(mStringUrl, builder.toString(), responseCode);
                }
                this.cancel(true);
            } else {
                if (mProgressDialog != null) {
                    dismiss();
                }

                networkControllerListener.responseComplete(mStringUrl, builder.toString(), responseCode);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


    /**
     * KEY & VALUE 데이터 전송
     * 프로그래스 바 연결
     */
    public class SendDatasThread extends AsyncTask<Void, Void, Void> {
        String params;
        private static final int msgCANNOT_CONNECTION = 1;
        int mode = 0;

        public SendDatasThread(String params) {
            this.params = params.toString();
        }

        public void setParam(String params) {
            this.params = params.toString();
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Void doInBackground(Void... param) {

            try {
                // URL 연결
                httpURLConnection = (HttpURLConnection) mUrl.openConnection();
//                if (isSession) {
//                    if (isSessionFalse) {
//                        httpURLConnection.setRequestProperty("cookie", mCookies);
//                    }
//                }
                httpURLConnection.setDefaultUseCaches(false);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setInstanceFollowRedirects(false);
                httpURLConnection.setConnectTimeout(15000);
                BufferedOutputStream writer = new BufferedOutputStream(httpURLConnection.getOutputStream());
                if (params != null) {
                    writer.write(this.params.toString().getBytes());
                    writer.flush();
                }

                // requestCode 확인
                // 200 : 성공
                // 기타 : 실패
                if (httpURLConnection.getResponseCode() == 200) {
                    InputStreamReader tmp = new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    String str;
                    while ((str = reader.readLine()) != null) {
                        builder.append(str + "\n");
                    }

//                    isSession = setSession();
                    publishProgress();
                } else {
                    mode = msgCANNOT_CONNECTION;
                    publishProgress();
                }

            } catch (FileNotFoundException e) {
                mode = msgCANNOT_CONNECTION;
                publishProgress();
                e.printStackTrace();

            } catch (UnsupportedEncodingException e) {
                mode = msgCANNOT_CONNECTION;
                publishProgress();
                e.printStackTrace();

            } catch (IOException e) {
                mode = msgCANNOT_CONNECTION;
                publishProgress();
                e.printStackTrace();
            } catch (NullPointerException e) {
                mode = msgCANNOT_CONNECTION;
                publishProgress();
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e) {
                mode = msgCANNOT_CONNECTION;
                publishProgress();
                e.printStackTrace();
            }
            return null;
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onProgressUpdate(Progress[])
         */
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            // 성공 시
            if (mode == 0) {
                dismiss();
                if (networkControllerListener != null) {
                    // 리턴 셋팅
                    try {
                        networkControllerListener.responseComplete(mStringUrl, builder.toString(), httpURLConnection.getResponseCode());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                this.cancel(true);
            } else {
                dismiss();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}

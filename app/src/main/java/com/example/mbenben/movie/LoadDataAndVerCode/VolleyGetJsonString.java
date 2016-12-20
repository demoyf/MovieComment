package com.example.mbenben.movie.LoadDataAndVerCode;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mbenben.movie.RegisterAndForgetPassword.LoadRegisterResult;
import com.example.mbenben.movie.Login.LoadLoginResult;
import com.example.mbenben.movie.RegisterAndForgetPassword.OnUpdatePassword;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by alone on 2016/10/12.
 * 此类封装了通过Volley获取wsk写的JSON数据
 */
public class VolleyGetJsonString {
    //    将请求队列设置为静态的，防止多次初始化
    private static RequestQueue requestQueue;
    private StringRequest request;
    /*
    * 注意，注册接口，必须要注入
    * */
    public void setLoadRegisterResult(LoadRegisterResult loadRegisterResult) {
        this.loadRegisterResult = loadRegisterResult;
    }

    private LoadRegisterResult loadRegisterResult;
    public VolleyGetJsonString(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        } else {
            requestQueue.start();
        }
    }
    /*
    * 获取wsk的登陆返回数据
    * */
    public void getLoginResult(String url, final LoadLoginResult mLoadResult) {
        request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                Log.d("xyf", "sss :" + s);
                mLoadResult.loginLoadSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("xyf", volleyError.toString());
                mLoadResult.loginLoadFail();
            }
        });
        if (requestQueue != null) {
            requestQueue.add(request);
        } else {
            Log.d("xyf", "fal");
        }
        request.setTag("movie");
    }

    /*
    * 判断注册的账号是否为空
    * */
    public  void getIsNull(String url) {
         request= new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    if (jsonObject != null) {
                        String isNull = jsonObject.getString("isNull");
                        /*
                        * 判断这个返回值是否为空
                        * */
                        if (TextUtils.isEmpty(isNull)) {
                            loadRegisterResult.registerError();
                        } else {
                            if (isNull.equals("1")) {
                                /*
                                * 不存在
                                * */
                                loadRegisterResult.phoneIsNull(true);
                            } else {
                                /*
                                * 已经存在了
                                * */
                                loadRegisterResult.phoneIsNull(false);
                            }
                        }
                    } else {
                        loadRegisterResult.registerError();
                    }
                } catch (JSONException e) {
                    loadRegisterResult.registerError();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                loadRegisterResult.registerError();
            }
        });
        requestQueue.add(request);
        request.setTag("movie");
    }

    /*
    * 用于停止请求队列，防止内存泄漏
    * */
    public void stopRequest(){
        if (request != null) {
            request.cancel();
        }
    }
    /*
    *获取注册的数据
    * */
    public void getRegister(String url) {
            request = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    loadRegisterResult.registerSuccess(s);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    loadRegisterResult.registerError();
                }
            });
            requestQueue.add(request);
            request.setTag("movie");
    }

    /*
    * 判断登陆的账号是否为空
    * */
    public void getLoginIsNull(String url,final LoadLoginResult loadResult) {
        request= new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    if (jsonObject != null) {
                        String isNull = jsonObject.getString("isNull");
                        /*
                        * 判断这个返回值是否为空
                        * */
                            if (isNull.equals("1")) {
                                /*
                                * 不存在
                                * */
                                loadResult.judgeIsNull(true);
                            } else {
                                /*
                                * 已经存在了
                                * */
                                loadResult.judgeIsNull(false);
                            }
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                loadResult.loginLoadFail();
            }
        });
        request.setTag("movie");
        requestQueue.add(request);
    }

    /*
    *  修改密码
    *  @param url 请求的URL
    *  updatePassword 接口的实例
    * */
    public void updatePassword(String url,final OnUpdatePassword updatePassword) {
        request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String result = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    result = jsonObject.getString("updatePassword");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result != null) {
                    if ("1".equals(result)) {
                        updatePassword.onUpdateSuccess();
                    } else {
                        updatePassword.onUpdateError();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updatePassword.onUpdateError();
            }
        });
        request.setTag("movie");

        requestQueue.add(request);
    }
    /*
    * 停止请求队列
    * */
    public static void StopRequestQueue() {
        if (requestQueue!=null)
        requestQueue.cancelAll("movie");
    }
}

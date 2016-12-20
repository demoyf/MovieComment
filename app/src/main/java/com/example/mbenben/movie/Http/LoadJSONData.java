package com.example.mbenben.movie.Http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mbenben.movie.Bean.CommentCriticBean;
import com.example.mbenben.movie.Bean.MovieInfoBean;
import com.example.mbenben.movie.Bean.MovieNameBean;
import com.example.mbenben.movie.Movie.PublicCirticKey;
import com.example.mbenben.movie.Movie.PublishCritic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alone on 2016/11/5.
 */
public class LoadJSONData implements PublicCirticKey {
    private static RequestQueue requestQueue;
    private Gson mGson = new Gson();
    public LoadJSONData(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        } else {
            requestQueue.start();
        }
    }

    /*
    * 获取JSON数据
    * @param URL
    * 回调接口实现对象
    * */
    public void LoadDtaFromServer(String URL, final OnLoadDataResult dataResult) {
        StringRequest request = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                dataResult.loadSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dataResult.loadError();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 0, 1.0f));
        request.setTag("demo");
//        if (requestQu)
        requestQueue.add(request);
    }

    /*
    * 解析String类型的JSON数据.
    * @
    * */
    public String[] parseStringJSONObject(String result, String[] param) {
        String[] re = new String[param.length];
        try {
            JSONObject jsonObject = new JSONObject(result);
            for (int i = 0; i < param.length; i++) {
                String s = jsonObject.getString(param[i]);
                re[i] = s;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return re;
    }

    public List<PublishCritic> parsePublicCirticArray(String result) {
        List<PublishCritic> publishCritics = new ArrayList<>();
//        Log.d("xyf", "result " + result);
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0;i<jsonArray.length();i++) {
                PublishCritic critic = mGson.fromJson(jsonArray.get(i).toString(), PublishCritic.class);
//                Log.d("xyf", "time " + critic.getTime());
                int height = (int) (200 * Math.random() + 200);
                if (height < 260) {
                    height += 50;
                }
                critic.setHeight(height);
                publishCritics.add(critic);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return publishCritics;
    }

    public List<PublishCritic> parsePublicCirticArrayWithoutHeight(String result) {
        List<PublishCritic> publishCritics = mGson.fromJson(result,
                new TypeToken<List<PublishCritic>>() {
                }.getType());
        return publishCritics;
    }
    public List<CommentCriticBean> parseCommentCriticArray(String result) {
        List<CommentCriticBean> commentCriticBeen = null;
        commentCriticBeen = mGson.fromJson(result, new TypeToken<List<CommentCriticBean>>() {
        }.getType());
        return commentCriticBeen;
    }
    public List<String> parseNoKeyJsonArray(String result) {
        List<String> strings = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(result);
            for (int i = 0;i<array.length();i++) {
                strings.add(array.get(i).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return strings;
    }

    public List<MovieNameBean> parseRadomMovie(String result) {
        List<MovieNameBean> strings = mGson.fromJson(result, new TypeToken<List<MovieNameBean>>() {
        }.getType());
        return strings;
    }

    public MovieInfoBean parseMovieInfoBean(String result) {
        return mGson.fromJson(result, MovieInfoBean.class);
    }
    public void onCancel() {
        requestQueue.cancelAll("demo");
        requestQueue.getCache().clear();
    }

    public void cancelArrayListRequest() {
        requestQueue.cancelAll("demo");
        requestQueue.getCache().clear();
    }
    //JSONArray转换成JSONObject
    public JSONObject JSONArraytoJSONObject(String result) {
        JSONArray jsonArray = null;
        JSONObject jsonObject = null;
        try {
            jsonArray = new JSONArray(result);
            jsonObject = jsonArray.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    public void SendDataServer(final String url, final SendDataToServer sendDataToServer){
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                sendDataToServer.SendSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                sendDataToServer.SendloadError();
            }
        });
        requestQueue.add(request);
    }
    /*
      * 转变符号
      *
      * */
    public String replaceURLChar(String imageUrl) {
        imageUrl = imageUrl.replace('X', '/');
        imageUrl = imageUrl.replace('M', ':');
        imageUrl=imageUrl.replace("Z","/");
        return imageUrl;
    }
}

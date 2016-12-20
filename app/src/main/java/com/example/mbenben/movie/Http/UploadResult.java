package com.example.mbenben.movie.Http;

import com.squareup.okhttp.Request;


/**
 * Created by alone on 2016/10/27.
 */
public interface UploadResult {
    public void uploadSuccess(String response);
    public void uploadError(Request request, Exception e);
}

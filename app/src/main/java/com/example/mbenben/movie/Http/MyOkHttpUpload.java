package com.example.mbenben.movie.Http;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;


/**
 * Created by alone on 2016/10/27.
 */
public class MyOkHttpUpload {
    private static OkHttpClient mOkHttpClient = new OkHttpClient();
    public static void uploadImageToServer(File file, String url, final UploadResult uploadResult) {
        if (file == null && !file.exists()) {
            uploadResult.uploadError(null, null);
        }
        RequestBody fileBody = RequestBody.
                create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"picture\";filename=\"demo.jpg\""), fileBody)
                .build();
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.post(requestBody);
        Request request = builder.build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                uploadResult.uploadError(request, e);
                if (call!=null && !call.isCanceled())
                call.cancel();
            }
            @Override
            public void onResponse(Response response) throws IOException {
                uploadResult.uploadSuccess(response.body().string());
                if (call!=null && !call.isCanceled())
                    call.cancel();
            }
        });
    }
}

package com.example.mbenben.movie.Movie;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mbenben.movie.Http.LoadJSONData;
import com.example.mbenben.movie.Http.OnLoadDataResult;
import com.example.mbenben.movie.LoadDataAndVerCode.GetURL;
import com.example.mbenben.movie.R;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alone on 2016/11/17.
 */
public class SendCriticActivity extends AppCompatActivity implements OnCompressResult{
    final MediaType JSON= MediaType.parse("application/json;charset=utf-8");
    OkHttpClient mOkHttpClient = new OkHttpClient();
    private EditText titleEdit, contentEdit;
    private Button mButton;
    private ImageView mImageView,
            backImageView,clearImageView, closeImageView;
    private ListView mListView;
    String imagePath = null;
    String url = "http://119.29.194.92:8080/movie/critic!savePublishCritic?phone="+Activity_movie.phone;
    private String path;
    private final static String IMAGE_TYPE = "image/*";
    private final static int RESULT_IMAGE = 100;
    private OnCompressResult mOnCompressResult;
    private ProgressBar mProgressBar;
    String title = null;
    String content = null;
    private List<String> mStrings = new ArrayList<>();
    private ArrayAdapter<String> mStringArrayAdapter;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            clearImageView.setVisibility(View.GONE);
            if (msg.what == 1) {
                Toast.makeText(SendCriticActivity.this, "发表成功", Toast.LENGTH_SHORT).show();
                titleEdit.setText("");
                contentEdit.setText("");
                mProgressBar.setVisibility(View.GONE);
                finish();
            } else if (msg.what == 2) {
                Toast.makeText(SendCriticActivity.this, "发表失败", Toast.LENGTH_SHORT).show();
                titleEdit.setText("");
                contentEdit.setText("");
                mProgressBar.setVisibility(View.GONE);
                finish();
            } else {
                Toast.makeText(SendCriticActivity.this, "发表失败", Toast.LENGTH_SHORT).show();
                titleEdit.setText("");
                contentEdit.setText("");
                mProgressBar.setVisibility(View.GONE);
                finish();
            }
        }
    };
    private LoadJSONData mLoadJSONData;
    private boolean isItemClick = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_critic_activity_layout);
        path = getCacheDir().getAbsolutePath();
        titleEdit = (EditText) findViewById(R.id.send_critic_title_text);
        contentEdit = (EditText) findViewById(R.id.send_critic_content_edit);
        mImageView = (ImageView) findViewById(R.id.send_critic_add_image);
        backImageView = (ImageView) findViewById(R.id.back_icon_in_tool);
        mButton = (Button) findViewById(R.id.send_critic_button);
        clearImageView = (ImageView) findViewById(R.id.send_critic_clear_text);
        closeImageView = (ImageView) findViewById(R.id.send_critic_close_list);
        mOnCompressResult = this;
        mListView = (ListView) findViewById(R.id.send_critic_list_view);
        mLoadJSONData = new LoadJSONData(this);
        mStringArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1,mStrings);
        mListView.setAdapter(mStringArrayAdapter);
        mProgressBar = (ProgressBar) findViewById(R.id.send_critic_progress);
        initEvent();
    }

    File mFile = null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_IMAGE) {
                final Cursor cursor = this.getContentResolver().query(data.getData(),
                        null, null, null, null);
//                确认返回不是空的。
                if (cursor != null) {
                    //                    获取图片的路径信息
                    if (cursor.moveToFirst()) {
                        int index = cursor.getColumnIndex(
                                MediaStore.Images.Media.DATA);
//                      mImageView.setImageBitmap(bitmap);
                        imagePath = cursor.getString(index);
                    }
                } else {
                    imagePath = data.getData().getPath();
                }
                if (!TextUtils.isEmpty(imagePath)) {
                    Log.d("xyf", "ima " + imagePath);
                    Picasso.with(this).load(new File(imagePath)).
                            config(Bitmap.Config.RGB_565).
                            resize(100, 100).centerCrop().into(mImageView);
                }
            }
        }
    }
    private void initEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("xyf", "you click " + position);
                isItemClick = true;
                titleEdit.setText(mStrings.get(position));
                mStrings.clear();
                mStringArrayAdapter.clear();
                mStringArrayAdapter.notifyDataSetChanged();
                closeImageView.setVisibility(View.GONE);
            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_TYPE);
                startActivityForResult(intent, RESULT_IMAGE);
            }
        });
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProgressBar.getVisibility() == View.VISIBLE) {
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                title =  titleEdit.getText().toString().trim();
                content = contentEdit.getText().toString().trim();
                if (TextUtils.isEmpty(imagePath)) {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(SendCriticActivity.this, "图片暂时不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(SendCriticActivity.this, "不能输入空值", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    compress(mOnCompressResult);
                }
            }
        });
        titleEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(final Editable s) {
                if (isItemClick) {
                    isItemClick = false;
                    return;
                }
                String searchText = titleEdit.getText().toString().trim();
                if (searchText.length() == 0) {
                    mStrings.clear();
                    mStringArrayAdapter.clear();
                    mStringArrayAdapter.notifyDataSetChanged();
                    clearImageView.setVisibility(View.GONE);
                    closeImageView.setVisibility(View.GONE);
                }
                if (clearImageView.getVisibility() == View.GONE) {
                    if (searchText.length() >= 1) {
                        clearImageView.setVisibility(View.VISIBLE);
                        clearImageView.setClickable(true);
                        mStrings.clear();
                        mStringArrayAdapter.clear();
                        mStringArrayAdapter.notifyDataSetChanged();
                    }
                }
                if (searchText.length() >= 1) {
                    try {
                        searchText = URLEncoder.encode(searchText, "utf-8");
                        Log.d("xyf", "search text " + searchText);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String url = GetURL.getSearchMovieNameUrl(searchText);
                    mLoadJSONData.LoadDtaFromServer(url, new OnLoadDataResult() {
                        @Override
                        public void loadSuccess(String result) {
                            List<String> strings = mLoadJSONData.parseNoKeyJsonArray(result);
                            if (strings != null && strings.size() > 0) {
                                mStrings.clear();
                                mStrings.addAll(strings);
                                mStringArrayAdapter.notifyDataSetChanged();
                                closeImageView.setVisibility(View.VISIBLE);
                            } else {
                                mStrings.clear();
                                mStringArrayAdapter.clear();
                                mStringArrayAdapter.notifyDataSetChanged();
                                closeImageView.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void loadError() {
                            Toast.makeText(SendCriticActivity.this,
                                    "网络异常", Toast.LENGTH_SHORT).show();
                            closeImageView.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeImageView.setVisibility(View.GONE);
                Log.d("xyf", "cancel");
                mStrings.clear();
                mStringArrayAdapter.clear();
                mStringArrayAdapter.notifyDataSetChanged();
            }
        });
        clearImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleEdit.setText("");
                clearImageView.setVisibility(View.GONE);
                clearImageView.setClickable(false);
            }
        });
    }
    public void compress(final OnCompressResult onCompressResult) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                Log.d("xyf", "demo size " + bitmap.getRowBytes());
                mFile = new File(path, "demo.jpg");
                if (!mFile.exists()) {
                    try {
                        mFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                FileOutputStream fileOutputStream = null;
                try {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    int res = 100;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, res, outputStream);
                    if (outputStream.toByteArray().length / 1024 < 400) {

                    } else {
                        Log.d("xyf", "res " + res);
                        while ((outputStream.toByteArray().length / 1024) > 400) {
                            Log.d("xyf", (outputStream.toByteArray().length / 1024) + "  length");
                            outputStream.reset();
                            res -= 10;
                            if (res < 30) {
                                res = 20;
                                bitmap.compress(Bitmap.CompressFormat.JPEG, res, outputStream);
                                break;
                            }
                            bitmap.compress(Bitmap.CompressFormat.JPEG, res, outputStream);
                        }
                    }
                    fileOutputStream = new FileOutputStream(mFile);
                    fileOutputStream.write(outputStream.toByteArray());
                    Log.d("xyf", "size " + (outputStream.toByteArray().length / 1024));
                    fileOutputStream.flush();
                    onCompressResult.compressSuccess();
                    Log.d("xyf", "compress success");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fileOutputStream != null) {
                        bitmap.recycle();
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    @Override
    public void compressSuccess() {
        if (!mFile.exists()) {
            mHandler.sendEmptyMessage(3);
            return;
        }
        PublishCritic publishCritic = new PublishCritic();
        publishCritic.setCritic(content);
        publishCritic.setGood((int) (200 + 200 * Math.random()));
        publishCritic.setIsPrivate(0);
        publishCritic.setTitle(title);
        final String json = new Gson().toJson(publishCritic);
        RequestBody jsonBody = RequestBody.create(JSON, json);
        RequestBody fileBody =
                RequestBody.create(MediaType.parse("application/octet-stream"),
                        mFile
                );
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"critic\";"), jsonBody
                ).addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"picture\";filename=demo.jpg"), fileBody
                ).build();
        Request request = new
                Request.Builder().url(url).post(requestBody).build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mFile.delete();
                call.cancel();
            }
            @Override
            public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                String re = response.body().string();
                Log.d("xyf", "succc");
                try {
                    JSONObject jsonObject = new JSONObject(re);
                    String save = jsonObject.getString("save");
                    if (save.equals("1")) {
                        mHandler.sendEmptyMessage(1);
                    } else {
                        mHandler.sendEmptyMessage(0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(0);
                }
                mFile.delete();
                call.cancel();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoadJSONData.onCancel();
        mStrings.clear();
    }
}

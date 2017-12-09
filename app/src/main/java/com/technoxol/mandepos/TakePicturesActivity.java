package com.technoxol.mandepos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.technoxol.mandepos.Adapters.ImagesAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class TakePicturesActivity extends BaseActivity {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    ArrayList<String> paths = new ArrayList<>();
    ArrayList<Bitmap> images = new ArrayList<>();
    private ProgressDialog uploading;
    private TextView catName;
    private RecyclerView imagesRecyclerView;
    private ImagesAdapter imagesAdapter;
    private ImageView imageView;
    private Bitmap bitmap;
    private Uri fileUri; // file url to store image/video
    private String imagePathStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_pictures);

        initUtils();
        initViews();

        uploading = new ProgressDialog(TakePicturesActivity.this);
        uploading.setIcon(R.drawable.skafs_logo);
        uploading.setTitle("Uploading Images");
        uploading.setMessage("Please wait...");
        uploading.setIndeterminate(false);
        uploading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        uploading.setCancelable(false);

        imagesAdapter = new ImagesAdapter(TakePicturesActivity.this, images);
        imagesRecyclerView.setLayoutManager(new LinearLayoutManager(TakePicturesActivity.this, LinearLayoutManager.HORIZONTAL, false));
        imagesRecyclerView.setAdapter(imagesAdapter);

    }

    private void initViews() {

        catName = findViewById(R.id.catName);
        imagesRecyclerView = findViewById(R.id.picturesList);
        imageView = findViewById(R.id.image);
    }

    public void onSendPics(View view) {

        if (utils.isNetworkConnected()) {
            if (!images.isEmpty()) {

                uploading.show();
                httpService.uploadImage(new HttpResponseCallback() {
                    @Override
                    public void onCompleteHttpResponse(String response, String requestUrl) {

                        if (response == null) {
                            utils.showToast("No Response....!");
                        } else {
                            Log.e("Response", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getBoolean("success")) {
                                    uploading.dismiss();
                                    utils.showToast("Uploaded successfully....");
                                } else {
                                    utils.showToast(jsonObject.optString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, imagePathStr);

            } else {
                utils.showToast("Select Picture First....!");
            }
        } else {
            utils.showToast("No internet connection...");
        }
    }

    public void onClickTakePickture(View view) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                if (data.getData() != null) {
                    Uri selectedImage = data.getData();

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                    Date d = new Date();
                    CharSequence s = DateFormat.format("MM-dd-yy hh-mm-ss", d.getTime());
                    File f = utils.convertBitmapToFile(bitmap, s.toString() + ".jpg");
                    imagePathStr = f.getPath();
                    paths.add(f.getPath());
                    images.add(bitmap);
                    imagesAdapter.notifyDataSetChanged();

                    Log.e("Data", "Not Null");
                } else {
                    Bitmap bmp = (Bitmap) data.getExtras().get("data");

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                    Date d = new Date();
                    CharSequence s = DateFormat.format("MM-dd-yy hh-mm-ss", d.getTime());
                    File f = utils.convertBitmapToFile(bmp, s.toString() + ".jpg");
                    imagePathStr = f.getPath();
                    paths.add(f.getPath());
                    images.add(bmp);
                    imagesAdapter.notifyDataSetChanged();

                    Log.e("Data", "NULL");
                }

            }
        }
    }

    public void finishSurvey(View view) {
        startActivity(new Intent(getApplication(), LicenceDetailsActivity.class));
    }
}

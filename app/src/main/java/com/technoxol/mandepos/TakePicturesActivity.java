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

    private ProgressDialog uploading;
    private TextView catName;
    private RecyclerView imagesRecyclerView;
    private ImagesAdapter imagesAdapter;
    private ImageView imageView;
    private int catNum = 1;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    public static final int MEDIA_TYPE_IMAGE = 1;

    private Uri fileUri; // file url to store image/video

    ArrayList<String> paths = new ArrayList<>();
    ArrayList<Bitmap> images = new ArrayList<>();
    private String[] cats = {"License Pictures","Premises Pictures","Signboard Pictures"};
    private int i;

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

        imagesAdapter = new ImagesAdapter(TakePicturesActivity.this,images);
        imagesRecyclerView.setLayoutManager(new LinearLayoutManager(TakePicturesActivity.this, LinearLayoutManager.HORIZONTAL, false));
        imagesRecyclerView.setAdapter(imagesAdapter);

    }

    private void initViews() {

        catName = (TextView) findViewById(R.id.catName);
        catName.setText(cats[0]);
        imagesRecyclerView = (RecyclerView) findViewById(R.id.picturesList);

        imageView = (ImageView) findViewById(R.id.image);
    }

    public void onSendPics(View view) {

//        if (utils.isNetworkConnected()) {
//
//            Log.e("Path Size", String.valueOf(paths.size()));
//
////            if (!images.isEmpty()) {
////                utils.showToast("Uploading images in background....");
////                if (catNum < 3) {
////                    catNum++;
////                    catName.setText(cats[catNum - 1]);
////                    images.clear();
////                    imagesAdapter.notifyDataSetChanged();
////                } else {
////                    images.clear();
////                    utils.startNewActivity(LicenceDetailsActivity.class, null, true);
////                }
////            } else {
////                utils.showToast("Select Picture First....!");
////            }
//        } else {
//            utils.showAlertDialoge();
//        }
        if (utils.isNetworkConnected()){


            if (!images.isEmpty()) {

                for (i = 0; i < paths.size(); i++) {

                    Log.e("PATH SIZE", String.valueOf(paths.size()));
                    final int remaining = paths.size()-(i+1);
                    Log.e("REMAININ", String.valueOf(remaining));
                    uploading.show();


                    httpService.uploadImage(new HttpResponseCallback() {
                        @Override
                        public void onCompleteHttpResponse(String response, String requestUrl) {

                            if (response == null) {
                                utils.showToast("No Response....!");
                                return;
                            } else {
                                Log.e("Response", response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getBoolean("success")){
                                        if (remaining == 0){
                                            uploading.dismiss();
                                            if (catNum < 3) {
                                                utils.showToast("Uploaded");
                                                catNum++;
                                                catName.setText(cats[catNum - 1]);
                                                images.clear();
                                                paths.clear();
                                                imagesAdapter.notifyDataSetChanged();
                                            } else {
                                                utils.showToast("All images uploaded successfully....");
                                                images.clear();
                                                paths.clear();
                                                utils.startNewActivity(LicenceDetailsActivity.class, null, true);
                                            }
                                        } else {
                                            utils.showToast(String.valueOf(remaining) + " Remaining...");
                                        }
                                    } else {

                                        utils.showToast(jsonObject.optString("message"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, paths.get(i).toString());
                }
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


                    Bitmap bmp = null;
                    try {
                        bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);


//                RoundedBitmapDrawable roundedBitmapDrawable= RoundedBitmapDrawableFactory.create(getResources(), bmp);
//                roundedBitmapDrawable.setCircular(true);
//
//                proPic.setImageDrawable(roundedBitmapDrawable);
//                proPicLoading.setVisibility(View.GONE);
                    Date d = new Date();
                    CharSequence s  = DateFormat.format("MM-dd-yy hh-mm-ss", d.getTime());
                    File f = utils.convertBitmapToFile(bmp, s.toString() + ".jpg");
                    paths.add(f.getPath());
                    images.add(bmp);
                    imagesAdapter.notifyDataSetChanged();

                    Log.e("Data", "Not Null");
//                Drawable drawable = new BitmapDrawable(getResources(), thumbnail);
//                imageView.setImageBitmap(thumbnail);
//                Picasso.with(TakePicturesActivity.this).load(destination.getAbsolutePath()).into(imageView);

                } else {
                    Bitmap bmp = (Bitmap)data.getExtras().get("data");

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);

//                RoundedBitmapDrawable roundedBitmapDrawable= RoundedBitmapDrawableFactory.create(getResources(), bmp);
//                roundedBitmapDrawable.setCircular(true);
//
//                proPic.setImageDrawable(roundedBitmapDrawable);
//                proPicLoading.setVisibility(View.GONE);
                    Date d = new Date();
                    CharSequence s  = DateFormat.format("MM-dd-yy hh-mm-ss", d.getTime());
                    File f = utils.convertBitmapToFile(bmp, s.toString() + ".jpg");
                    paths.add(f.getPath());
                    images.add(bmp);
                    imagesAdapter.notifyDataSetChanged();

                    Log.e("Data", "NULL");
                }

            }
        }
    }
}

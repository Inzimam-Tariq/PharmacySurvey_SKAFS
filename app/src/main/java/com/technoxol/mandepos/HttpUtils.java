package com.technoxol.mandepos;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method;


class HttpUtils {
    Context mContext;
    Utils utils;
    private CustomDialogs customDialogs;
    int serverResponseCode;


    HttpUtils(Context mContext) {
        this.mContext = mContext;
        utils = new Utils(mContext);
        customDialogs = new CustomDialogs(mContext);

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null) && netInfo.isConnected();
    }

    void httpPost(final String requestUrl, final HashMap<String, String> params, final HttpResponseCallback callback, boolean isShowProgressDialog, String dialogMsg) {

        if (!isNetworkConnected()) {
            utils.showToast("No Internet...!");
            return;
        }
        if(isShowProgressDialog)
        {
            customDialogs.showProgressDialog(dialogMsg);
        }

        final String TAG = "HttpPOSTRequest";
        StringRequest stringRequest = new StringRequest(Method.POST, requestUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                callback.onCompleteHttpResponse(response, requestUrl);
                customDialogs.hideProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                int statusCode = volleyError.networkResponse.statusCode;

//                utils.printStackTrace(volleyError);
                customDialogs.hideProgressDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                return params;
            }
        };

        //Volley does retry for you if you have specified the policy.
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 2000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        // Add Request to queue
        stringRequest.setShouldCache(false);

        ControllerApp.getInstance().addToRequestQueue(stringRequest,TAG);

    }

    public void httpMultipartRequest(final String requestUrl, final Map<String, String> params, final String filePath, String fileNameKey,
                                     final HttpResponseCallback callback, boolean isShowProgressDialog) {
//         Picture
        //mimeType:@"image/jpeg"
        final String TAG = "HttpMultipartRequest";

//        final String fileNameKey = "picture";

        if (!isNetworkConnected()) {
            utils.showToast("No internet Connection....");
            return;
        }
        utils.printLog(TAG, "" + requestUrl);

//        File file = new File(filePath);
        File file = utils.resizeImageFile(new File(filePath), 200);

        MultipartRequest multipartRequest = new MultipartRequest(requestUrl, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                volleyError.printStackTrace();
                customDialogs.hideProgressDialog();
            }
        }, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "" + response);
//                Toast.makeText(mContext, "response " + response, Toast.LENGTH_LONG).show();
                customDialogs.hideProgressDialog();
                callback.onCompleteHttpResponse(response, requestUrl);
            }
        }, file, fileNameKey, params) {
//            @Override
//            protected Map<String, String> getParams() {
//                return params;
//            }


            @Override
            public byte[] getBody() throws AuthFailureError {
                return super.getBody();
            }

            @Override
            public String getBodyContentType() {
                return super.getBodyContentType();
            }

            @Override
            protected Map<String, String> getParams() {

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<>();
//
//                if (TOKEN != null) {
//                    headers.put("Content-Type", "multipart/form-data");
//                    headers.put("TOKEN", TOKEN);
//                }


                return headers;
            }
        };

//Volley does retry for you if you have specified the policy.
        multipartRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        // Add Request to queue
        multipartRequest.setShouldCache(false);

        ControllerApp.getInstance().addToRequestQueue(multipartRequest,TAG);


    }
}

package com.technoxol.mandepos;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


class MultipartRequest extends Request<String> {

    private MultipartEntity entity;

    private final Response.Listener<String> mListener;
    private final File mFilePart;
    private Map<String, String> params;
    private String FILE_PART_NAME ;

    MultipartRequest(String url, Response.ErrorListener errorListener, Response.Listener<String> listener, File file, String fileNameKey, Map<String, String> params)
    {
        super(Method.POST, url, errorListener);

        entity = new MultipartEntity();
        mListener = listener;
        mFilePart = file;
        this.params = params;
        this.FILE_PART_NAME=fileNameKey;
        buildMultipartEntity();
    }

    private void buildMultipartEntity()
    {
//        entity.addPart("picture", new FileBody(mFilePart, "image/*"));
        try
        {
            entity.addPart(FILE_PART_NAME, new FileBody(mFilePart));
            for (Object o : params.entrySet()) {
                Map.Entry pairs = (Map.Entry) o;
                entity.addPart("" + pairs.getKey(), new StringBody("" + pairs.getValue()));
            }
       }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
           VolleyLog.e("UnsupportedEncodingException");
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if (headers == null || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<>();
        }

//        if (TOKEN != null) {
//            headers.put("Content-Type", "application/x-www-form-urlencoded");
//            headers.put("TOKEN", TOKEN);
//        }

//        headers.put("Accept", "application/json");
//        headers.put("X-Requested-With", "XMLHTTPRequest");
        return headers;
    }

   @Override
    public String getBodyContentType()
    {
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError
    {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
       try
       {
            entity.writeTo(bos);
       }
        catch (IOException e)
        {
            e.printStackTrace();
           VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
       return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response)
    {
        try {
           String jsonString =new String(response.data, HttpHeaderParser.parseCharset(response.headers));
           return Response.success(jsonString,HttpHeaderParser.parseCacheHeaders(response));
        }catch (UnsupportedEncodingException e) {
           e.printStackTrace();
           return Response.error(new ParseError(e));
        }

   }

    @Override
    protected void deliverResponse(String response)
    {

        mListener.onResponse(response);
    }
}
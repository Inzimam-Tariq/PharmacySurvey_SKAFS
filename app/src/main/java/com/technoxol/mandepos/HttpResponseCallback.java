package com.technoxol.mandepos;

/**
 * Created by root on 11/16/15.
 */
public interface HttpResponseCallback {

    public void onCompleteHttpResponse(String response, String requestUrl);
}

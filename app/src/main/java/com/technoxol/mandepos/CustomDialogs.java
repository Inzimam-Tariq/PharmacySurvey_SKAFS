package com.technoxol.mandepos;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;


public class CustomDialogs {

    private Context mContext;

    ProgressDialog pDialog;

    public CustomDialogs(Context mContext)
    {
        this.mContext = mContext;
        pDialog = new ProgressDialog(mContext);
    }

    public  void showProgressDialog(String msg) {

        pDialog.setMessage(msg);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    public  void hideProgressDialog() {
        pDialog.hide();
    }

    public  void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}

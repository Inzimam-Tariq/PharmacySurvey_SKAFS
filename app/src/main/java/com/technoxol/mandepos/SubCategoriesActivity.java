package com.technoxol.mandepos;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.technoxol.mandepos.Adapters.SubCatAdapter;
import com.technoxol.mandepos.models.SubCatInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubCategoriesActivity extends BaseActivity implements HttpResponseCallback {

    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private SubCatInfo subCatInfo;
    private SubCatAdapter subCatAdapter;
    private List<SubCatInfo> subCatInfosList;
    private String id, salesType;
    private TextView salesTypeET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categories);
        initUtils();

        salesTypeET = (TextView) findViewById(R.id.saleTypeCatET);

        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey("id")){
            id = bundle.getString("id");
        }
        if (bundle.containsKey("type")){
            salesType = bundle.getString("type");
            salesTypeET.setText(salesType);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Getting Survey");
        progressDialog.setMessage("Please wait...");

        recyclerView = (RecyclerView) findViewById(R.id.subCatRecyclerview);

        subCatInfosList = new ArrayList<>();

        subCatAdapter = new SubCatAdapter(this, subCatInfosList, id, salesType);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(subCatAdapter);

        progressDialog.show();
        httpService.getSubCat(id,this);
    }

    @Override
    public void onCompleteHttpResponse(String response, String requestUrl) {
        if (response == null) {
            utils.showToast("No Response....!");
            return;
        } else {
            try {
                progressDialog.dismiss();
                JSONObject jsonObject = new JSONObject(response);

                Log.e("response",response);
                if (jsonObject.getBoolean("success")){

                    JSONArray subcategories = jsonObject.getJSONArray("subcategories");
                    for (int i = 0; i < subcategories.length(); i++){
                        JSONObject cats = subcategories.getJSONObject(i);
                        subCatInfo = new SubCatInfo();
                        Log.e("cat_id: ",cats.optString("category_id"));
                        subCatInfo.setCategory_id(cats.optString("category_id"));
                        subCatInfo.setCategory_title(cats.optString("category_title"));

                        subCatInfosList.add(subCatInfo);
                    }

                    subCatAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.technoxol.mandepos.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.technoxol.mandepos.HttpResponseCallback;
import com.technoxol.mandepos.HttpService;
import com.technoxol.mandepos.R;
import com.technoxol.mandepos.SharedPrefUtils;
import com.technoxol.mandepos.Utils;
import com.technoxol.mandepos.models.OptionsInfo;
import com.technoxol.mandepos.models.QuestionsInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.technoxol.mandepos.AppConstants.answers;
import static com.technoxol.mandepos.AppConstants.answersPosition;
import static com.technoxol.mandepos.AppConstants.answersText;
import static com.technoxol.mandepos.AppConstants.answers_id;
import static com.technoxol.mandepos.AppConstants.selected;

/**
 * Created by Jawad Zulqarnain on 10/11/2017.
 */

public class QuestionsListAdapter extends BaseAdapter {

    private Context mContext;
    private List<QuestionsInfo> questionList;
    private Utils utils;
    private HttpService httpService;
    private SharedPrefUtils sharedPrefUtils;
    private List<OptionsInfo> optionsList;

    public QuestionsListAdapter(Context mContext, List<QuestionsInfo> questionList) {
        this.mContext = mContext;
        this.questionList = questionList;
        optionsList = new ArrayList<>();
        selected = new ArrayList<>();
        answers_id = new ArrayList<>();
        answers = new ArrayList<>();
        httpService = new HttpService(mContext);
        sharedPrefUtils = new SharedPrefUtils(mContext);

    }

    @Override
    public int getCount() {
        return questionList.size();
    }

    @Override
    public Object getItem(int position) {
        return questionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final NamesViewHolder viewHolder;

        if(convertView==null){

            // inflate the layout
            LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.question_item,parent,false);

            // well set up the ViewHolder
            viewHolder = new NamesViewHolder();
            viewHolder.questionEditText = (EditText) convertView.findViewById(R.id.questionEditText);
            viewHolder.questionNumber = (TextView) convertView.findViewById(R.id.questionNumber);
            viewHolder.questionText = (TextView) convertView.findViewById(R.id.questionText);
            viewHolder.optionET = (EditText) convertView.findViewById(R.id.optionET);
            // store the holder with the view.
            convertView.setTag(viewHolder);

        }else{
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (NamesViewHolder) convertView.getTag();
        }


        final QuestionsInfo questionsInfo = questionList.get(position);

        viewHolder.questionNumber.setText(String.valueOf(position+1)+":");
        viewHolder.questionText.setText(questionsInfo.getQuestion_text());
//        Log.e("Questions size", String.valueOf(questionList.size()));
        if (questionsInfo.getOptions_size().matches("0")){
            viewHolder.questionEditText.setVisibility(View.VISIBLE);
            viewHolder.optionET.setVisibility(View.GONE);

            if (!selected.contains(questionsInfo.getQuestion_id())) {

                viewHolder.questionEditText.setText("");
                viewHolder.questionEditText.setEnabled(true);
                Log.e("Selected","No");

            } else {
                int i=0,j=0;
                for (i=0; i<answersPosition.size(); i++){
                    if (answersPosition.get(i).toString().matches(String.valueOf(position))){
                        j = i;
                    }
                }
                viewHolder.questionEditText.setText(answers.get(j).toString());
                Log.e("Selected","Yes");

            }
        } else {
            viewHolder.optionET.setVisibility(View.VISIBLE);

            if (selected.contains(questionsInfo.getQuestion_id())) {

                int i = 0, j = 0;
                for (i=0; i<answersPosition.size(); i++){
                    if (answersPosition.get(i).toString().matches(String.valueOf(position))){
                        j = i;
                    }
                }

                viewHolder.optionET.setText(answersText.get(j).toString());

            } else {
                viewHolder.optionET.setText("Select Option");
            }

            viewHolder.questionEditText.setVisibility(View.GONE);
        }

        viewHolder.questionEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!selected.contains(questionsInfo.getQuestion_id())) {
                    viewHolder.questionEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_DONE
                                    || actionId == EditorInfo.IME_ACTION_NEXT) {

                                viewHolder.strOptionET = viewHolder.questionEditText.getText().toString();

                                if (!viewHolder.strOptionET.isEmpty()) {
                                    selected.add(questionsInfo.getQuestion_id());
                                    answers.add(viewHolder.strOptionET);
                                    answersText.add(viewHolder.strOptionET);
                                    answers_id.add("0");
                                    answersPosition.add(String.valueOf(position));
                                }
                                utils.hideKeyboard(v);
                                viewHolder.questionEditText.setEnabled(false);
                            }
                            return true;
                        }
                    });

                } else {
                    utils.showToast("You can't change it now.....!");
                }
                return false;
            }
        });

        viewHolder.optionET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!selected.contains(questionsInfo.getQuestion_id())) {

                    final ProgressDialog getting = new ProgressDialog(mContext);
                    getting.setIcon(R.drawable.skafs_logo);
                    getting.setMessage("Please wait...");
                    getting.setIndeterminate(false);
                    getting.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    getting.setCancelable(false);
                    getting.show();
                    httpService.getOptions(questionsInfo.getQuestion_id(), new HttpResponseCallback() {
                        @Override
                        public void onCompleteHttpResponse(String response, String requestUrl) {

                            if (response == null) {
                                utils.showToast("No Response....!");
                                return;
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getBoolean("success")) {

                                        optionsList.clear();

                                        JSONArray optionsArray = jsonObject.getJSONArray("options");
                                        for (int k = 0; k < optionsArray.length(); k++) {

                                            JSONObject optionsData = optionsArray.getJSONObject(k);
                                            OptionsInfo optionsInfo = new OptionsInfo();
                                            optionsInfo.setOption_id(optionsData.optString("option_id"));
                                            optionsInfo.setOption_text(optionsData.optString("option_text"));
                                            optionsInfo.setOption_value(optionsData.optString("option_value"));

                                            optionsList.add(optionsInfo);
                                        }

                                    } else {

                                        utils.showToast("Server Error...!");
                                    }

//                                Toast.makeText(mContext, String.valueOf(optionsList.size()), Toast.LENGTH_SHORT).show();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                    final String[] options = new String[optionsList.size()];

                                    for (int i = 0; i < optionsList.size(); i++) {

                                        options[i] = optionsList.get(i).getOption_text();
                                    }
                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            viewHolder.strOptionET = options[which];
                                            viewHolder.optionET.setText(viewHolder.strOptionET);
                                            selected.add(questionsInfo.getQuestion_id());
                                            answers.add(optionsList.get(which).getOption_value());
                                            answersText.add(optionsList.get(which).getOption_text());
                                            answers_id.add(optionsList.get(which).getOption_id());
                                            answersPosition.add(String.valueOf(position));
                                        }
                                    });
                                    getting.dismiss();
                                    builder.show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } else {
                    utils.showToast("You can't change it now.....!");
                }
            }
        });



        return convertView;
    }

    private class NamesViewHolder
    {
        public TextView questionNumber, questionText;

        public EditText questionEditText, optionET;
        private String strOptionET;
    }

}

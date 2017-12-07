package com.technoxol.mandepos.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
 * Created by Jawad Zulqarnain on 7/10/2017.
 */

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.MyViewHolder> {

    private Context mContext;
    private List<QuestionsInfo> questionList;
    private Utils utils;
    private HttpService httpService;
    private SharedPrefUtils sharedPrefUtils;
    private List<OptionsInfo> optionsList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView questionNumber, questionText;

        public EditText questionEditText, optionET;
        private String strOptionET;

        public MyViewHolder(View view) {
            super(view);
            questionEditText = (EditText) view.findViewById(R.id.questionEditText);
            questionNumber = (TextView) view.findViewById(R.id.questionNumber);
            questionText = (TextView) view.findViewById(R.id.questionText);
            optionET = (EditText) view.findViewById(R.id.optionET);
        }
    }


    public QuestionsAdapter(Context mContext, List<QuestionsInfo> questionList) {
        this.mContext = mContext;
        this.questionList = questionList;
        optionsList = new ArrayList<>();
        selected = new ArrayList<>();
        answers_id = new ArrayList<>();
        answers = new ArrayList<>();
        answersText = new ArrayList<>();
        answersPosition = new ArrayList<>();
        httpService = new HttpService(mContext);
        sharedPrefUtils = new SharedPrefUtils(mContext);

    }

    @Override
    public QuestionsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_item, parent, false);

        utils = new Utils(mContext);
        return new QuestionsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final QuestionsAdapter.MyViewHolder holder, final int position) {
        final QuestionsInfo questionsInfo = questionList.get(position);

        holder.questionNumber.setText(String.valueOf(position+1)+":");
        holder.questionText.setText(questionsInfo.getQuestion_text());
//        Log.e("Questions size", String.valueOf(questionList.size()));
        if (questionsInfo.getOptions_size().matches("0")){
            holder.questionEditText.setVisibility(View.VISIBLE);
            holder.optionET.setVisibility(View.GONE);

            if (!selected.contains(questionsInfo.getQuestion_id())) {

                holder.questionEditText.setText("");
                holder.questionEditText.setEnabled(true);
                Log.e("Selected","No");

            } else {
                int i=0,j=0;
                for (i=0; i<answersPosition.size(); i++){
                    if (answersPosition.get(i).toString().matches(String.valueOf(position))){
                        j = i;
                    }
                }
                holder.questionEditText.setText(answers.get(j).toString());
                Log.e("Selected","Yes");

            }
        } else {
            holder.optionET.setVisibility(View.VISIBLE);

            if (selected.contains(questionsInfo.getQuestion_id())) {

                int i = 0, j = 0;
                for (i=0; i<answersPosition.size(); i++){
                    if (answersPosition.get(i).toString().matches(String.valueOf(position))){
                        j = i;
                    }
                }

                holder.optionET.setText(answersText.get(j).toString());

            } else {
                holder.optionET.setText("Select Option");
            }

            holder.questionEditText.setVisibility(View.GONE);
        }

        holder.questionEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!selected.contains(questionsInfo.getQuestion_id())) {
                    holder.questionEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_DONE
                                    || actionId == EditorInfo.IME_ACTION_NEXT) {

                                holder.strOptionET = holder.questionEditText.getText().toString();

                                if (!holder.strOptionET.isEmpty()) {
                                    selected.add(questionsInfo.getQuestion_id());
                                    answers.add(holder.strOptionET);
                                    answersText.add(holder.strOptionET);
                                    answers_id.add("0");
                                    answersPosition.add(String.valueOf(position));
                                }
                                utils.hideKeyboard(v);
                                holder.questionEditText.setEnabled(false);
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

        holder.optionET.setOnClickListener(new View.OnClickListener() {
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

                                            holder.strOptionET = options[which];
                                            holder.optionET.setText(holder.strOptionET);
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
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }
}

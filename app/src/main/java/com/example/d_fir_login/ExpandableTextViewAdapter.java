package com.example.d_fir_login;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableTextViewAdapter extends BaseExpandableListAdapter {

    Context context;

    String[] faq_ques = {
            "1. What to do, If am not able to get OTP?",
            "2. How can I scan documents properly?",
            "3. Who can upload files in any particular Criminal Record?",
            "4. Why uploaded files are not visible in any particular case? ",
            "5. How can I edit uploaded documents?"
    };

    String[][] faq_ans = {
            {"- Check your registered mobile number to that particular ID. Check your internet connection. For further queries kindly contact System Administrator."},
            {"- Make sure your document is at optimum distance. Keep your brightness level at 50% . Try to keep your scan as straight as possible."},
            {"- Investigating officer can create case as well as upload files  regarding that particular Criminal Record."},
            {"- Restart application. Check your internet connection . Check your internet connection while uploading Criminal record."},
            {"- Uploaded files are not Deleted in any database but changed files can be  Added in that Case."}
    };

    public ExpandableTextViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return faq_ques.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return faq_ans[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return faq_ques[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return faq_ans[groupPosition][childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String question = (String)getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.faq_questions, null);
        }

        TextView ques = convertView.findViewById(R.id.faq_Ques);
        ques.setTypeface(null, Typeface.BOLD);
        ques.setText(question);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String answer = (String)getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.faq_answer, null);
        }
        TextView ans = convertView.findViewById(R.id.faq_Ans);
        ans.setText(answer);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
package com.example.student.homemade;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class FeedbackFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // private OnFragmentInteractionListener mListener;

    public FeedbackFragment() {
        // Required empty public constructor
    }



    public static FeedbackFragment newInstance(String param1, String param2) {
        FeedbackFragment fragment = new FeedbackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        Button feedbackBtn = view.findViewById(R.id.btnFeedback);
        final EditText subject = view.findViewById(R.id.feedbackSubject);
        final EditText body = view.findViewById(R.id.feedbackBody);

        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(subject.getText().toString(), body.getText().toString());
            }
        });

        return view;
    }


    public void sendEmail(String subject, String body){

        if(subject.isEmpty()){
            inform("Empty Subject");
            //Toast.makeText(getContext(), "Empty Subject", Toast.LENGTH_LONG).show();
        }
        else if(body.isEmpty()){
            inform("Empty Body");
            //Toast.makeText(getContext(), "Empty Body", Toast.LENGTH_LONG).show();
        }
        else {

            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:govardhangdg@gmail.com, cupofjava08@gmail.com"));


            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, body);

            startActivity(Intent.createChooser(intent, "Send Email through ..."));

        }
    }

    private void inform(String string){
        final Snackbar snack = Snackbar.make(getView()/*.findViewById(android.R.id.content)*/, string, Snackbar.LENGTH_INDEFINITE).setDuration(8000);

        snack.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snack.dismiss();
            }
        }).setActionTextColor(getView()/*.findViewById(android.R.id.content)*/.getResources().getColor(R.color.white));

        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(getView()/*.findViewById(android.R.id.content)*/.getResources().getColor(R.color.white));

        view.setBackgroundColor(getView()/*.findViewById(android.R.id.content)*/.getResources().getColor(R.color.signupColor));
        snack.show();
    }


}

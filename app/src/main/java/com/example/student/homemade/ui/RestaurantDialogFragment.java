package com.example.student.homemade.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.student.homemade.OrderPageActivity;
import com.example.student.homemade.R;

import java.util.ArrayList;
import java.util.Objects;

import javax.security.auth.callback.Callback;


//Creates a popup dialog on click on restaurant cards
public class RestaurantDialogFragment extends DialogFragment implements View.OnClickListener {
    String descr;
    TextView textView,description;
    RatingBar ratingBar;
    String stars;
    String title;
    TextView review;
    Bitmap bmp;
    String review_text;
    String TAG="RestaurantDialogFragment";
    private Callback callback;
    public RestaurantDialogFragment() {
        this.stars="4";
        this.title="Restaurant Name";
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(RestaurantDialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }
//    public RestaurantDialogFragment(String title,String stars) {
//        this.stars=stars;
//        this.title=title;
//    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        StringBuilder review_text=new StringBuilder();
        title = "Restaurant Name";
        stars = "4";
        if (args != null) {
            title = args.getString("title", "Restaurant Name");
            stars = args.getString("stars","4");
            ArrayList<String> reviews= args.getStringArrayList("reviews");
            for(String i: reviews) {
                review_text.append(i +"\n");
            }
            byte[] byteArray = args.getByteArray("image");
            if(byteArray!=null&&byteArray.length>0){
                bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                Log.d(TAG,"Mapping ");
            } else{
                bmp=null;
            }


            Log.d("review_text",review_text.toString());
            descr = args.getString("description", "Be the first to describe this place!");
        }
        ImageView imageView = Objects.requireNonNull(getView()).findViewById(R.id.dialog_image);
        if(bmp==null){
            Glide.with(Objects.requireNonNull(getActivity())).load(R.drawable.default_rest).into(imageView);
        }
        else{
            DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
//                DisplayMetrics dm = new DisplayMetrics();
//                mContext.getResources().getWindowManager().getDefaultDisplay().getMetrics(dm);
            imageView.setMinimumHeight(dm.heightPixels);
            imageView.setMinimumWidth(dm.widthPixels);
            imageView.setImageBitmap(bmp);
        }


        description=getView().findViewById(R.id.description);
        description.setText(descr);
        review = getView().findViewById(R.id.reviews);
        Log.d("review_text",review_text.toString());
        review.setText(review_text.toString());
        textView=getView().findViewById(R.id.title);
        textView.setText(title);
        ratingBar= getView().findViewById(R.id.rating);
        ratingBar.setRating(Float.valueOf(stars));
        ratingBar.setIsIndicator(true);
        Button button = getView().findViewById(R.id.dismiss);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),OrderPageActivity.class);
                intent.putExtra("restaurantName",title);
                startActivity(intent);
            }
        });





    }
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fullscreen_dialog , container, false);
        getDialog().setTitle("Simple Dialog");
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageButton close = rootView.findViewById(R.id.fullscreen_dialog_close);
        Button action =rootView.findViewById(R.id.dismiss);

        close.setOnClickListener(this);
        action.setOnClickListener(this);
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {

            case R.id.fullscreen_dialog_close:
                dismiss();
                break;

            case R.id.dismiss:
                dismiss();
                break;

        }

    }
}
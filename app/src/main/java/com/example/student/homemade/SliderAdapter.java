
package com.example.student.homemade;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.student.homemade.R;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private Typeface myFont, myFont1;


//    private TextView slideHeading, slideDescription;
//    private ImageView slide_imageView;


    public SliderAdapter(Context context) {

        this.context = context;
    }

    // img Array
    public int[] image_slide ={
            R.drawable.eat_icon_slider,
            R.drawable.diet,
            R.drawable.grocery,
            R.drawable.money
    };

    // heading Array
    public String[] heading_slide ={
            "Tasty Food",
            "Healthy Food",
            "From the Best Cooks",
            "Value for Money"
    };

    // description Array
    public String[] description_slide ={
            "Find the tastiest food that you can have here. We never compromise on the quality and your tastes ",
            "Ingredients picked up from the most fresh farms, will give you a healthy and enjoyable experience",
            "        We hire only the best cooks",
            "We offer food at most reasonable prices. You will be shocked!"
    };




    @Override
    public int getCount() {

        return heading_slide.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container,false);
        container.addView(view);

        myFont = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/PlayfairDisplay_Black.ttf");
        myFont1 = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/OpenSans-LightItalic.ttf");
        ImageView slide_imageView = view.findViewById(R.id.imageView1);
        TextView slideHeading = view.findViewById(R.id.tvHeading);

        TextView  slideDescription = view.findViewById(R.id.tvDescription);


        slide_imageView.setImageResource(image_slide[position]);
        slideHeading.setText(heading_slide[position]);
        slideHeading.setTypeface(myFont);
        slideDescription.setText(description_slide[position]);
        slideDescription.setTypeface(myFont1);

        return view;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }

//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        View view = (View) object;
//        container.removeView(view);
//    }

}



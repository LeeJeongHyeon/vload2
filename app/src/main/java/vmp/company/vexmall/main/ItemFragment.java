package vmp.company.vexmall.main;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import vmp.company.vexmall.R;

public class ItemFragment extends Fragment {

    ImageView[] imagefragment = null;

    int position;
    Bitmap[] bitmaps;

    public void setPosition( int position, Bitmap[] bitmaps ){
        this.position = position;
        this.bitmaps = bitmaps;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.page,container,false);

        ImageView imageView = view.findViewById(R.id.imagefragment);
        imageView.setImageBitmap( bitmaps[ position ] );
        //imageView.setImageResource(getArguments().getInt("imageUrl"));

        return view;
    }



}

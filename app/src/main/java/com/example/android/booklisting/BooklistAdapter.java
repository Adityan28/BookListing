package com.example.android.booklisting;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 06-07-2017.
 */

public class BooklistAdapter extends ArrayAdapter<Booklists>{

    public BooklistAdapter(Activity context , ArrayList<Booklists> booklist){
        super(context,0,booklist);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Booklists booklists = getItem(position);
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.book_view,parent,false);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.image_view);
        imageView.setImageBitmap(booklists.getmThumbNail());

        TextView textView1 = (TextView)convertView.findViewById(R.id.text_view_1);
        textView1.setText(booklists.getmBookName());

        TextView textView2 = (TextView)convertView.findViewById(R.id.text_view_2);
        textView2.setText(booklists.getmAuthorName());
        return convertView;
    }
}

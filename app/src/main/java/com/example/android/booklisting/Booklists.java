package com.example.android.booklisting;

import android.graphics.Bitmap;

/**
 * Created by user on 01-07-2017.
 */

public class Booklists {
    private Bitmap mThumbNail;
    private String mBookName;
    private String mAuthorName="";
    private String mInfoLink;

    public Booklists(Bitmap thumbnail, String bookname,String[] authorname,String infolink){
        mThumbNail=thumbnail;
        mBookName=bookname;
        for (int i=0;i<authorname.length;i++){
            mAuthorName +=authorname[i];
            mAuthorName += "\n";
        }
        mInfoLink=infolink;
    }

    public Bitmap getmThumbNail() {
        return mThumbNail;
    }

    public String getmBookName() {
        return mBookName;
    }

    public String getmAuthorName() {
        return mAuthorName;
    }

    public String getmInfoLink() {
        return mInfoLink;
    }
}

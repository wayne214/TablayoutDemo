package com.example.wayne.tablayoutdemo.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wayne on 2018/5/4.
 */

public class Book implements Parcelable {

    public int bookId;
    public String bookName;


    public Book(int bookId, String bookName) {
        this.bookId = bookId;
        this.bookName = bookName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(bookId);
        out.writeString(bookName);
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {

        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    private Book(Parcel in) {
        bookId = in.readInt();
        bookName = in.readString();
    }
}

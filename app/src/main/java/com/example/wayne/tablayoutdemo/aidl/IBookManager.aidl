package com.example.wayne.tablayoutdemo.aidl;
import com.example.wayne.tablayoutdemo.aidl.Book;
interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
}
package com.example.a14512.nimd_douban.modules.book.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a14512.nimd_douban.R;
import com.example.a14512.nimd_douban.modules.book.model.entity.Book;
import com.example.a14512.nimd_douban.modules.book.view.BookDetailActivity;

import java.util.ArrayList;

/**
 * Created by 14512 on 2017/9/5.
 */

public class BookAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<Book> books;

    private int Last_Item_state = 99;
    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 100;
    //正在加载中
    public static final int LOADING_MORE = 101;
    //没有更多了
    public static final int NO_DATA_MORE = 102;

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        if (viewType == Last_Item_state) {
            return new BottomHolder(LayoutInflater.from(context).inflate(R.layout.item_recycler_bottom, null));
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_book, null);
            final BookHolder bookHolder = new BookHolder(view);
            bookHolder.bookView.setOnClickListener(v -> {
                int position = bookHolder.getAdapterPosition();
                Book book = new Book();
                book = books.get(position);
                startActivity(book.getId(), v);
            });
            return bookHolder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position+1 == getItemCount()){
            return Last_Item_state;
        } else {
            return 0;
        }
    }

    private void startActivity(String id, View v) {
        Intent intent = new Intent(v.getContext(), BookDetailActivity.class);
        intent.putExtra("id", id);
        v.getContext().startActivity(intent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BottomHolder) {
            BottomHolder bottomHolder = (BottomHolder) holder;
            switch (Last_Item_state){
                case PULLUP_LOAD_MORE:
                    bottomHolder.hintText.setText("上拉加载更多...");
                    bottomHolder.progressBar.setVisibility(View.VISIBLE);
                    break;
                case LOADING_MORE:
                    bottomHolder.hintText.setText("正在加载中...");
                    bottomHolder.progressBar.setVisibility(View.VISIBLE);
                    break;
                case NO_DATA_MORE:
                    bottomHolder.hintText.setText("----我是有底线的----");
                    bottomHolder.progressBar.setVisibility(View.GONE);
                    break;
            }
        } else if (holder instanceof BookHolder) {
            BookHolder bookHolder = (BookHolder) holder;
            Book book = books.get(position);
            if (book != null) {
                Glide.with(context).load(book.getImages().getSmall()).into(bookHolder.ivBook);
                bookHolder.bookName.setText(book.getTitle());
                bookHolder.bookGrade.setText(book.getRating().getAverage());
                bookHolder.bookArt.setText(book.getAlt());
            }
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    private class BookHolder extends RecyclerView.ViewHolder {
        View bookView;
        ImageView ivBook;
        TextView bookName;
        TextView bookGrade;
        TextView bookArt;
        public BookHolder(View itemView) {
            super(itemView);
            bookView = itemView;
            ivBook = (ImageView) itemView.findViewById(R.id.iv_book);
            bookName = (TextView) itemView.findViewById(R.id.tv_book_name);
            bookGrade = (TextView) itemView.findViewById(R.id.tv_book_grade);
            bookArt = (TextView) itemView.findViewById(R.id.tv_book_art);
        }
    }

    private class BottomHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        TextView hintText;

        public BottomHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.recycler_bottom_progress);
            hintText = (TextView) itemView.findViewById(R.id.recycler_bottom_text);
        }
    }

    public void addData(ArrayList<Book> books) {
        this.books.addAll(books);
        notifyDataSetChanged();
    }

    public void changeMoreStatus(int status) {
        Last_Item_state= status;
    }
}

package com.android.hellomvijava.Model;

import android.support.annotation.NonNull;

import com.android.hellomvijava.View.MainViewState;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import io.reactivex.Observable;


public interface MainView extends MvpView {

    @NonNull
    Observable<Integer> getImageIntent();

    void render(MainViewState viewState);

}

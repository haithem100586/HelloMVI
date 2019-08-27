package com.android.hellomvijava.View;

import com.android.hellomvijava.Model.MainView;
import com.android.hellomvijava.Model.PartialMainState;
import com.android.hellomvijava.Utils.DataSource;
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter extends MviBasePresenter<MainView,MainViewState> {

    private DataSource dataSource;

    MainPresenter(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected void bindIntents(){

        Observable<PartialMainState> gotData = intent(MainView::getImageIntent)
                .switchMap(index -> dataSource.getImageLinkFromList(index)
                .map(imageLink -> (PartialMainState)new PartialMainState.GotImageLink(imageLink))
                .startWith(new PartialMainState.Loading())
                //.onErrorReturn(error -> new PartialMainState.Error(error))
                .onErrorReturn(PartialMainState.Error::new)
                .subscribeOn(Schedulers.io()));

        MainViewState initState = new MainViewState(false,false,
                "", null);

        Observable<PartialMainState> initIntent = gotData.observeOn(AndroidSchedulers.mainThread());

        subscribeViewState(initIntent.scan(initState, this::viewStateReducer), MainView::render);

    }


    private MainViewState viewStateReducer(MainViewState prevState, PartialMainState changeState)
    {

       MainViewState newState = prevState;

       if (changeState instanceof PartialMainState.Loading)
       {
           newState.isLoading = true;
           newState.isImageViewShow = false;
       }

       if (changeState instanceof PartialMainState.GotImageLink)
       {
           newState.isLoading = false;
           newState.isImageViewShow = true;
           newState.imageLink = ((PartialMainState.GotImageLink)changeState).imageLink;
       }

       if (changeState instanceof PartialMainState.Error)
       {
           newState.isLoading = false;
           newState.isImageViewShow = false;
           newState.error = ((PartialMainState.Error)changeState).error;
       }

       return newState;
    }
}

package com.android.hellomvijava.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.hellomvijava.Model.MainView;
import com.android.hellomvijava.R;
import com.android.hellomvijava.Utils.DataSource;
import com.hannesdorfmann.mosby3.mvi.MviActivity;
import com.jakewharton.rxbinding2.view.RxView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import io.reactivex.Observable;


///  https://www.youtube.com/watch?v=Rpzt3bTdEO8&t=1034s  ///
/* - MainActivity implements MainView interface (it contains method for random image
    and method to render view depending on activity state (loading, imageShow, error)
   - MainActivity extends MviActivity and implement method createPresenter
   when instantiate MainPresenter (new DataSource(imageList))
   - we used interface PartialMainState  in class MainPresenter,
   it contains three class state (Loading, GotImageLink, Error)
 */

public class MainActivity extends MviActivity<MainView,MainPresenter> implements MainView {

    ImageView imageView;
    Button button;
    ProgressBar progressBar;

    List<String> imageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.bt_get_data);
        imageView = findViewById(R.id.recycler_data);
        progressBar = findViewById(R.id.progress_bar);
        imageList = createListImage();
    }


    private List<String> createListImage() {

        return Arrays.asList(
                "https://www.perfectly-nintendo.com/wp-content/gallery/zelda-figma-1-27-09-2016/1.jpg",
                "https://i.ytimg.com/vi/bBmWWvJquGM/maxresdefault.jpg",
                "http://www.fightersgeneration.com/np8/char/link-3d-ocarina7.jpg",
                "https://www.ssbwiki.com/images/0/00/Link_BotW.png",
                "https://wallpaperset.com/w/full/3/c/8/211061.jpp");
    }

    private Integer getRandomNumberInRange(int min  , int max) {
        if (min >= max)
            throw new IllegalArgumentException("max must be greater than min");
        Random random = new Random();
        return  random.nextInt((max-min)+1)+min;
    }

    //////// Method implemented for class MviActivity<MainView,MainPresenter>  ////////
    @NonNull
    @Override
    public MainPresenter createPresenter(){

        return new MainPresenter(new DataSource(imageList));
    }
    ///////////////////////////////////////////////////////////////////////////////////


    ///////////////  Methods implemented for interface MainView   /////////////////////
    @NonNull
    @Override
    public Observable<Integer> getImageIntent() {
        return RxView.clicks(button).map(click -> getRandomNumberInRange(0,imageList.size()-1));
    }


    @Override
    public void render(MainViewState viewState) {

        // here we will process change stat to display view
        if (viewState.isLoading)
        {
            progressBar.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            button.setEnabled(false);
        }
        else if (viewState.error != null)
        {
            progressBar.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
            button.setEnabled(true);
            Toast.makeText(this, viewState.error.getMessage() , Toast.LENGTH_LONG).show();
        }
        else if  (viewState.isImageViewShow )
        {
            imageView.setVisibility(View.VISIBLE);
            button.setEnabled(true);

            Picasso.get().load(viewState.imageLink).fetch(new Callback() {
                @Override
                public void onSuccess() {
                    imageView.setAlpha(0f);
                    Picasso.get().load(viewState.imageLink).into(imageView);
                    imageView.animate().setDuration(300).alpha(1f).start();//fade animation

                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {

                    progressBar.setVisibility(View.GONE);

                }
            });
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////

}

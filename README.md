# HelloMVI

In this application, we will create the interfaces MainView and PartialMainState in Model package 
and we will create MainPresenter and MainViewState in View package.

First, we create  interface MainView extends MvpView that contains two methods:
- getImageIntent() to random image
- render(MainViewState viewState) to render view depending on view state (loading, imageShow, error)

Then, we create class MainViewState  that contains view states

After, we create interface PartialMainState to control state, it contains classes for view states:
- class Loading
- class GotImageLink 
- class Error

Then, we create class MainPresenter extends MviBasePresenter, it’s the presenter of view.

Finally, Activity implements MainView interface and we must implement the methods,
and Activity extends MviActivity and implement method createPresenter when instantiate MainPresenter(new DataSource(imageList))

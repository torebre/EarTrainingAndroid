package com.kjipo.eartraining;

import android.app.Application;

import com.kjipo.eartraining.score.ScoreActivityModule;
import com.kjipo.eartraining.svg.SvgModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        MainActivityModule.class,
//        SvgModule.class,
//        ServiceModule.class,
        ViewModelModule.class,
        ScoreActivityModule.class
})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(EarTrainingApplication earTrainingApplication);


}


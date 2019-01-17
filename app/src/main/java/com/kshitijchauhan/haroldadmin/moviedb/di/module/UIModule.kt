package com.kshitijchauhan.haroldadmin.moviedb.di.module

import com.kshitijchauhan.haroldadmin.moviedb.ui.common.BottomNavManager
import com.kshitijchauhan.haroldadmin.moviedb.ui.common.ProgressBarManager
import dagger.Module
import dagger.Provides

@Module
class UIModule {

    @Provides
    fun provideBottomNavManager() = BottomNavManager()

    @Provides
    fun provideProgressBarManager() = ProgressBarManager()

}
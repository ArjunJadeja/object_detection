package com.arjunjadeja.objectdetection.di

import android.content.Context
import com.arjunjadeja.objectdetection.data.ObjectDetectionService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Singleton
    @Provides
    fun provideObjectDetectorService(@ApplicationContext context: Context): ObjectDetectionService {
        return ObjectDetectionService(context = context)
    }

}
package hu.yettel.highwayvignette.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.yettel.highwayvignette.data.repository.HighwayRepository
import hu.yettel.highwayvignette.data.repository.HighwayRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHighwayRepository(impl: HighwayRepositoryImpl): HighwayRepository
}
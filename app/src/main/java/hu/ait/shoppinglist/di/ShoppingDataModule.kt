package hu.ait.shoppinglist.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.ait.shoppinglist.data.ShoppingDAO
import hu.ait.shoppinglist.data.ShoppingDatabase
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideShoppingDao(appDatabase: ShoppingDatabase): ShoppingDAO {
        return appDatabase.shoppingDao()
    }

    @Provides
    @Singleton
    fun provideShoppingAppDatabase(@ApplicationContext appContext: Context): ShoppingDatabase {
        return ShoppingDatabase.getDatabase(appContext)
    }
}
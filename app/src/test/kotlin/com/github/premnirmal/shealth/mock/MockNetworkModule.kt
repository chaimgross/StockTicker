package com.sec.android.app.shealth.mock

import android.content.Context
import com.sec.android.app.shealth.components.AsyncBus
import com.sec.android.app.shealth.model.AlarmScheduler
import com.sec.android.app.shealth.model.IHistoryProvider
import com.sec.android.app.shealth.model.IStocksProvider
import com.sec.android.app.shealth.network.ChartApi
import com.sec.android.app.shealth.network.GithubApi
import com.sec.android.app.shealth.network.NewsApi
import com.sec.android.app.shealth.network.NewsProvider
import com.sec.android.app.shealth.network.StocksApi
import com.sec.android.app.shealth.network.SuggestionApi
import com.sec.android.app.shealth.network.YahooFinance
import com.sec.android.app.shealth.widget.WidgetDataProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by android on 3/22/17.
 */
@Module
class MockNetworkModule {

  @Provides @Singleton internal fun provideHttpClient(
    context: Context,
    bus: AsyncBus
  ): OkHttpClient =
    Mocker.provide(OkHttpClient::class)

  @Provides @Singleton internal fun provideStocksApi(): StocksApi = Mocker.provide(StocksApi::class)

  @Provides @Singleton internal fun provideGson(): Gson = GsonBuilder().create()

  @Provides @Singleton internal fun provideGsonFactory(gson: Gson): GsonConverterFactory {
    return GsonConverterFactory.create(gson)
  }

  @Provides @Singleton internal fun provideYahooFinance(
    context: Context,
    okHttpClient: OkHttpClient,
    gson: Gson,
    converterFactory: GsonConverterFactory
  ): YahooFinance = Mocker.provide(YahooFinance::class)

  @Provides @Singleton internal fun provideSuggestionsApi(
    context: Context,
    okHttpClient: OkHttpClient,
    gson: Gson,
    converterFactory: GsonConverterFactory
  ): SuggestionApi =
    Mocker.provide(SuggestionApi::class)

  @Provides @Singleton internal fun provideStocksProvider(): IStocksProvider =
    Mocker.provide(IStocksProvider::class)

  @Provides @Singleton internal fun provideWidgetDataFactory(): WidgetDataProvider =
    Mocker.provide(WidgetDataProvider::class)

  @Provides @Singleton internal fun provideNewsApi(
    context: Context,
    okHttpClient: OkHttpClient,
    gson: Gson,
    converterFactory: GsonConverterFactory
  ): NewsApi = Mocker.provide(NewsApi::class)

  @Provides @Singleton internal fun provideGithubApi(
    context: Context,
    okHttpClient: OkHttpClient,
    converterFactory: GsonConverterFactory
  ): GithubApi = Mocker.provide(GithubApi::class)

  @Provides @Singleton internal fun provideNewsProvider(): NewsProvider =
    Mocker.provide(NewsProvider::class)

  @Provides @Singleton internal fun provideHistoricalDataApi(
    context: Context,
    okHttpClient: OkHttpClient,
    gson: Gson,
    converterFactory: GsonConverterFactory
  ): ChartApi =
    Mocker.provide(ChartApi::class)

  @Provides @Singleton internal fun provideHistoricalDataProvider(): IHistoryProvider =
    Mocker.provide(IHistoryProvider::class)

  @Provides @Singleton internal fun provideAlarmScheduler(): AlarmScheduler = AlarmScheduler()
}
package com.secta9ine.didapp.di;

import com.google.gson.Gson;
import com.secta9ine.didapp.v2.data.remote.V2PlayerApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class AppModule_ProvideV2PlayerApiFactory implements Factory<V2PlayerApi> {
  private final Provider<OkHttpClient> okHttpClientProvider;

  private final Provider<Gson> gsonProvider;

  public AppModule_ProvideV2PlayerApiFactory(Provider<OkHttpClient> okHttpClientProvider,
      Provider<Gson> gsonProvider) {
    this.okHttpClientProvider = okHttpClientProvider;
    this.gsonProvider = gsonProvider;
  }

  @Override
  public V2PlayerApi get() {
    return provideV2PlayerApi(okHttpClientProvider.get(), gsonProvider.get());
  }

  public static AppModule_ProvideV2PlayerApiFactory create(
      Provider<OkHttpClient> okHttpClientProvider, Provider<Gson> gsonProvider) {
    return new AppModule_ProvideV2PlayerApiFactory(okHttpClientProvider, gsonProvider);
  }

  public static V2PlayerApi provideV2PlayerApi(OkHttpClient okHttpClient, Gson gson) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideV2PlayerApi(okHttpClient, gson));
  }
}

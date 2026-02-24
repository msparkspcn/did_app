package com.secta9ine.didapp.v2.data.remote;

import com.google.gson.Gson;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class SnapshotWebSocketClient_Factory implements Factory<SnapshotWebSocketClient> {
  private final Provider<OkHttpClient> okHttpClientProvider;

  private final Provider<Gson> gsonProvider;

  public SnapshotWebSocketClient_Factory(Provider<OkHttpClient> okHttpClientProvider,
      Provider<Gson> gsonProvider) {
    this.okHttpClientProvider = okHttpClientProvider;
    this.gsonProvider = gsonProvider;
  }

  @Override
  public SnapshotWebSocketClient get() {
    return newInstance(okHttpClientProvider.get(), gsonProvider.get());
  }

  public static SnapshotWebSocketClient_Factory create(Provider<OkHttpClient> okHttpClientProvider,
      Provider<Gson> gsonProvider) {
    return new SnapshotWebSocketClient_Factory(okHttpClientProvider, gsonProvider);
  }

  public static SnapshotWebSocketClient newInstance(OkHttpClient okHttpClient, Gson gson) {
    return new SnapshotWebSocketClient(okHttpClient, gson);
  }
}

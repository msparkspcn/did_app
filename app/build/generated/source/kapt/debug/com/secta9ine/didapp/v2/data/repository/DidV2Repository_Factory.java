package com.secta9ine.didapp.v2.data.repository;

import com.google.gson.Gson;
import com.secta9ine.didapp.v2.data.local.V2SnapshotDao;
import com.secta9ine.didapp.v2.data.remote.SnapshotWebSocketClient;
import com.secta9ine.didapp.v2.data.remote.V2PlayerApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class DidV2Repository_Factory implements Factory<DidV2Repository> {
  private final Provider<V2PlayerApi> apiProvider;

  private final Provider<V2SnapshotDao> daoProvider;

  private final Provider<Gson> gsonProvider;

  private final Provider<SnapshotWebSocketClient> wsClientProvider;

  public DidV2Repository_Factory(Provider<V2PlayerApi> apiProvider,
      Provider<V2SnapshotDao> daoProvider, Provider<Gson> gsonProvider,
      Provider<SnapshotWebSocketClient> wsClientProvider) {
    this.apiProvider = apiProvider;
    this.daoProvider = daoProvider;
    this.gsonProvider = gsonProvider;
    this.wsClientProvider = wsClientProvider;
  }

  @Override
  public DidV2Repository get() {
    return newInstance(apiProvider.get(), daoProvider.get(), gsonProvider.get(), wsClientProvider.get());
  }

  public static DidV2Repository_Factory create(Provider<V2PlayerApi> apiProvider,
      Provider<V2SnapshotDao> daoProvider, Provider<Gson> gsonProvider,
      Provider<SnapshotWebSocketClient> wsClientProvider) {
    return new DidV2Repository_Factory(apiProvider, daoProvider, gsonProvider, wsClientProvider);
  }

  public static DidV2Repository newInstance(V2PlayerApi api, V2SnapshotDao dao, Gson gson,
      SnapshotWebSocketClient wsClient) {
    return new DidV2Repository(api, dao, gson, wsClient);
  }
}

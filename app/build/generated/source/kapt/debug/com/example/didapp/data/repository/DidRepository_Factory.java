package com.example.didapp.data.repository;

import com.example.didapp.data.local.DidDao;
import com.example.didapp.data.remote.DidApi;
import com.example.didapp.util.AssetDownloader;
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
public final class DidRepository_Factory implements Factory<DidRepository> {
  private final Provider<DidApi> apiProvider;

  private final Provider<DidDao> daoProvider;

  private final Provider<AssetDownloader> downloaderProvider;

  public DidRepository_Factory(Provider<DidApi> apiProvider, Provider<DidDao> daoProvider,
      Provider<AssetDownloader> downloaderProvider) {
    this.apiProvider = apiProvider;
    this.daoProvider = daoProvider;
    this.downloaderProvider = downloaderProvider;
  }

  @Override
  public DidRepository get() {
    return newInstance(apiProvider.get(), daoProvider.get(), downloaderProvider.get());
  }

  public static DidRepository_Factory create(Provider<DidApi> apiProvider,
      Provider<DidDao> daoProvider, Provider<AssetDownloader> downloaderProvider) {
    return new DidRepository_Factory(apiProvider, daoProvider, downloaderProvider);
  }

  public static DidRepository newInstance(DidApi api, DidDao dao, AssetDownloader downloader) {
    return new DidRepository(api, dao, downloader);
  }
}

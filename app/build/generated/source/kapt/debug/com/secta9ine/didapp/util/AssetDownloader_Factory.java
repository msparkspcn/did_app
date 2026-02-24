package com.secta9ine.didapp.util;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AssetDownloader_Factory implements Factory<AssetDownloader> {
  private final Provider<Context> contextProvider;

  public AssetDownloader_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public AssetDownloader get() {
    return newInstance(contextProvider.get());
  }

  public static AssetDownloader_Factory create(Provider<Context> contextProvider) {
    return new AssetDownloader_Factory(contextProvider);
  }

  public static AssetDownloader newInstance(Context context) {
    return new AssetDownloader(context);
  }
}

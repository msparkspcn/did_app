package com.secta9ine.didapp.di;

import com.secta9ine.didapp.data.local.DidDatabase;
import com.secta9ine.didapp.v2.data.local.V2SnapshotDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class AppModule_ProvideV2SnapshotDaoFactory implements Factory<V2SnapshotDao> {
  private final Provider<DidDatabase> databaseProvider;

  public AppModule_ProvideV2SnapshotDaoFactory(Provider<DidDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public V2SnapshotDao get() {
    return provideV2SnapshotDao(databaseProvider.get());
  }

  public static AppModule_ProvideV2SnapshotDaoFactory create(
      Provider<DidDatabase> databaseProvider) {
    return new AppModule_ProvideV2SnapshotDaoFactory(databaseProvider);
  }

  public static V2SnapshotDao provideV2SnapshotDao(DidDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideV2SnapshotDao(database));
  }
}

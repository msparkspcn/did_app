package com.example.didapp.di;

import com.example.didapp.data.local.DidDao;
import com.example.didapp.data.local.DidDatabase;
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
public final class AppModule_ProvideDidDaoFactory implements Factory<DidDao> {
  private final Provider<DidDatabase> databaseProvider;

  public AppModule_ProvideDidDaoFactory(Provider<DidDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public DidDao get() {
    return provideDidDao(databaseProvider.get());
  }

  public static AppModule_ProvideDidDaoFactory create(Provider<DidDatabase> databaseProvider) {
    return new AppModule_ProvideDidDaoFactory(databaseProvider);
  }

  public static DidDao provideDidDao(DidDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideDidDao(database));
  }
}

package com.example.didapp.di;

import com.example.didapp.data.remote.DidApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class AppModule_ProvideDidApiFactory implements Factory<DidApi> {
  @Override
  public DidApi get() {
    return provideDidApi();
  }

  public static AppModule_ProvideDidApiFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static DidApi provideDidApi() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideDidApi());
  }

  private static final class InstanceHolder {
    private static final AppModule_ProvideDidApiFactory INSTANCE = new AppModule_ProvideDidApiFactory();
  }
}

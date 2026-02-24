package com.secta9ine.didapp.ui.viewmodel;

import com.secta9ine.didapp.v2.data.repository.DidV2Repository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class DidV2ViewModel_Factory implements Factory<DidV2ViewModel> {
  private final Provider<DidV2Repository> repositoryProvider;

  public DidV2ViewModel_Factory(Provider<DidV2Repository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public DidV2ViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static DidV2ViewModel_Factory create(Provider<DidV2Repository> repositoryProvider) {
    return new DidV2ViewModel_Factory(repositoryProvider);
  }

  public static DidV2ViewModel newInstance(DidV2Repository repository) {
    return new DidV2ViewModel(repository);
  }
}

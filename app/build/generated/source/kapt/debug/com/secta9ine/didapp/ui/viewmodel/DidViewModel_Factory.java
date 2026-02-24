package com.secta9ine.didapp.ui.viewmodel;

import com.secta9ine.didapp.data.repository.DidRepository;
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
public final class DidViewModel_Factory implements Factory<DidViewModel> {
  private final Provider<DidRepository> repositoryProvider;

  public DidViewModel_Factory(Provider<DidRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public DidViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static DidViewModel_Factory create(Provider<DidRepository> repositoryProvider) {
    return new DidViewModel_Factory(repositoryProvider);
  }

  public static DidViewModel newInstance(DidRepository repository) {
    return new DidViewModel(repository);
  }
}

package com.example.didapp.ui.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u000b\u001a\u00020\fR\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/example/didapp/ui/viewmodel/DidViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/example/didapp/data/repository/DidRepository;", "(Lcom/example/didapp/data/repository/DidRepository;)V", "didItems", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/example/didapp/data/local/DidEntity;", "getDidItems", "()Lkotlinx/coroutines/flow/StateFlow;", "syncData", "", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class DidViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.didapp.data.repository.DidRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.example.didapp.data.local.DidEntity>> didItems = null;
    
    @javax.inject.Inject()
    public DidViewModel(@org.jetbrains.annotations.NotNull()
    com.example.didapp.data.repository.DidRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.example.didapp.data.local.DidEntity>> getDidItems() {
        return null;
    }
    
    public final void syncData() {
    }
}
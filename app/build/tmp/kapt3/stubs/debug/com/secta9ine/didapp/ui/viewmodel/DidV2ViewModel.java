package com.secta9ine.didapp.ui.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\f\u001a\u00020\rH\u0014R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u000e"}, d2 = {"Lcom/secta9ine/didapp/ui/viewmodel/DidV2ViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/secta9ine/didapp/v2/data/repository/DidV2Repository;", "(Lcom/secta9ine/didapp/v2/data/repository/DidV2Repository;)V", "didId", "", "snapshot", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/secta9ine/didapp/v2/contract/PlayerSnapshotDto;", "getSnapshot", "()Lkotlinx/coroutines/flow/StateFlow;", "onCleared", "", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class DidV2ViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.secta9ine.didapp.v2.data.repository.DidV2Repository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String didId = "did-001";
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.secta9ine.didapp.v2.contract.PlayerSnapshotDto> snapshot = null;
    
    @javax.inject.Inject()
    public DidV2ViewModel(@org.jetbrains.annotations.NotNull()
    com.secta9ine.didapp.v2.data.repository.DidV2Repository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.secta9ine.didapp.v2.contract.PlayerSnapshotDto> getSnapshot() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
}
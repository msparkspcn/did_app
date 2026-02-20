package com.example.didapp.data.repository;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u000e\u0010\u0010\u001a\u00020\u0011H\u0082@\u00a2\u0006\u0002\u0010\u0012J\u000e\u0010\u0013\u001a\u00020\u0011H\u0086@\u00a2\u0006\u0002\u0010\u0012R\u001d\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/example/didapp/data/repository/DidRepository;", "", "api", "Lcom/example/didapp/data/remote/DidApi;", "dao", "Lcom/example/didapp/data/local/DidDao;", "downloader", "Lcom/example/didapp/util/AssetDownloader;", "(Lcom/example/didapp/data/remote/DidApi;Lcom/example/didapp/data/local/DidDao;Lcom/example/didapp/util/AssetDownloader;)V", "allDidItems", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/example/didapp/data/local/DidEntity;", "getAllDidItems", "()Lkotlinx/coroutines/flow/Flow;", "sampleItems", "checkAndInjectSamples", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "syncWithRemote", "app_debug"})
public final class DidRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.example.didapp.data.remote.DidApi api = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.didapp.data.local.DidDao dao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.didapp.util.AssetDownloader downloader = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.example.didapp.data.local.DidEntity> sampleItems = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.example.didapp.data.local.DidEntity>> allDidItems = null;
    
    @javax.inject.Inject()
    public DidRepository(@org.jetbrains.annotations.NotNull()
    com.example.didapp.data.remote.DidApi api, @org.jetbrains.annotations.NotNull()
    com.example.didapp.data.local.DidDao dao, @org.jetbrains.annotations.NotNull()
    com.example.didapp.util.AssetDownloader downloader) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.didapp.data.local.DidEntity>> getAllDidItems() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object syncWithRemote(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object checkAndInjectSamples(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}
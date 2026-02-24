package com.secta9ine.didapp.data.repository;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\fH\u0002J\u000e\u0010\u0013\u001a\u00020\u0014H\u0082@\u00a2\u0006\u0002\u0010\u0015J\u001c\u0010\u0016\u001a\u00020\u00142\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0082@\u00a2\u0006\u0002\u0010\u0018J\u001c\u0010\u0019\u001a\u00020\u00142\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0082@\u00a2\u0006\u0002\u0010\u0018J\u000e\u0010\u001a\u001a\u00020\u0014H\u0086@\u00a2\u0006\u0002\u0010\u0015R\u001d\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/secta9ine/didapp/data/repository/DidRepository;", "", "api", "Lcom/secta9ine/didapp/data/remote/DidApi;", "dao", "Lcom/secta9ine/didapp/data/local/DidDao;", "downloader", "Lcom/secta9ine/didapp/util/AssetDownloader;", "(Lcom/secta9ine/didapp/data/remote/DidApi;Lcom/secta9ine/didapp/data/local/DidDao;Lcom/secta9ine/didapp/util/AssetDownloader;)V", "allDidItems", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/secta9ine/didapp/data/local/DidEntity;", "getAllDidItems", "()Lkotlinx/coroutines/flow/Flow;", "sampleItems", "buildAssetFileName", "", "entity", "checkAndInjectSamples", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "downloadAssets", "entities", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "replaceRemoteItems", "syncWithRemote", "app_debug"})
public final class DidRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.secta9ine.didapp.data.remote.DidApi api = null;
    @org.jetbrains.annotations.NotNull()
    private final com.secta9ine.didapp.data.local.DidDao dao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.secta9ine.didapp.util.AssetDownloader downloader = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.secta9ine.didapp.data.local.DidEntity> sampleItems = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.secta9ine.didapp.data.local.DidEntity>> allDidItems = null;
    
    @javax.inject.Inject()
    public DidRepository(@org.jetbrains.annotations.NotNull()
    com.secta9ine.didapp.data.remote.DidApi api, @org.jetbrains.annotations.NotNull()
    com.secta9ine.didapp.data.local.DidDao dao, @org.jetbrains.annotations.NotNull()
    com.secta9ine.didapp.util.AssetDownloader downloader) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.secta9ine.didapp.data.local.DidEntity>> getAllDidItems() {
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
    
    private final java.lang.Object replaceRemoteItems(java.util.List<com.secta9ine.didapp.data.local.DidEntity> entities, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object downloadAssets(java.util.List<com.secta9ine.didapp.data.local.DidEntity> entities, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.String buildAssetFileName(com.secta9ine.didapp.data.local.DidEntity entity) {
        return null;
    }
}
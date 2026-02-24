package com.secta9ine.didapp.v2.data.repository;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\n\u0002\u0010$\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ0\u0010\u0012\u001a\u0002H\u0013\"\u0010\b\u0000\u0010\u0013\u0018\u0001*\b\u0012\u0004\u0012\u0002H\u00130\u00142\u0006\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u0002H\u0013H\u0082\b\u00a2\u0006\u0002\u0010\u0017J\u001c\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00110\u00192\u0006\u0010\u001a\u001a\u00020\u0011H\u0002J\u0016\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\rH\u0082@\u00a2\u0006\u0002\u0010\u001eJ\u0016\u0010\u001f\u001a\u00020\u001c2\u0006\u0010 \u001a\u00020\u00112\u0006\u0010!\u001a\u00020\"J\u0006\u0010#\u001a\u00020\u001cJ\u0016\u0010$\u001a\u00020\u001c2\u0006\u0010 \u001a\u00020\u0011H\u0086@\u00a2\u0006\u0002\u0010%R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u000b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\r0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082D\u00a2\u0006\u0002\n\u0000\u00a8\u0006&"}, d2 = {"Lcom/secta9ine/didapp/v2/data/repository/DidV2Repository;", "", "api", "Lcom/secta9ine/didapp/v2/data/remote/V2PlayerApi;", "dao", "Lcom/secta9ine/didapp/v2/data/local/V2SnapshotDao;", "gson", "Lcom/google/gson/Gson;", "wsClient", "Lcom/secta9ine/didapp/v2/data/remote/SnapshotWebSocketClient;", "(Lcom/secta9ine/didapp/v2/data/remote/V2PlayerApi;Lcom/secta9ine/didapp/v2/data/local/V2SnapshotDao;Lcom/google/gson/Gson;Lcom/secta9ine/didapp/v2/data/remote/SnapshotWebSocketClient;)V", "snapshotFlow", "Lkotlinx/coroutines/flow/Flow;", "Lcom/secta9ine/didapp/v2/contract/PlayerSnapshotDto;", "getSnapshotFlow", "()Lkotlinx/coroutines/flow/Flow;", "wsUrl", "", "enumValueOfOrDefault", "T", "", "raw", "default", "(Ljava/lang/String;Ljava/lang/Enum;)Ljava/lang/Enum;", "parseMetadata", "", "metadataJson", "replaceSnapshot", "", "snapshot", "(Lcom/secta9ine/didapp/v2/contract/PlayerSnapshotDto;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "startRealtime", "didId", "scope", "Lkotlinx/coroutines/CoroutineScope;", "stopRealtime", "syncInitialSnapshot", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class DidV2Repository {
    @org.jetbrains.annotations.NotNull()
    private final com.secta9ine.didapp.v2.data.remote.V2PlayerApi api = null;
    @org.jetbrains.annotations.NotNull()
    private final com.secta9ine.didapp.v2.data.local.V2SnapshotDao dao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    @org.jetbrains.annotations.NotNull()
    private final com.secta9ine.didapp.v2.data.remote.SnapshotWebSocketClient wsClient = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String wsUrl = "ws://10.212.44.212:8080/ws/player-snapshot";
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.secta9ine.didapp.v2.contract.PlayerSnapshotDto> snapshotFlow = null;
    
    @javax.inject.Inject()
    public DidV2Repository(@org.jetbrains.annotations.NotNull()
    com.secta9ine.didapp.v2.data.remote.V2PlayerApi api, @org.jetbrains.annotations.NotNull()
    com.secta9ine.didapp.v2.data.local.V2SnapshotDao dao, @org.jetbrains.annotations.NotNull()
    com.google.gson.Gson gson, @org.jetbrains.annotations.NotNull()
    com.secta9ine.didapp.v2.data.remote.SnapshotWebSocketClient wsClient) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.secta9ine.didapp.v2.contract.PlayerSnapshotDto> getSnapshotFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object syncInitialSnapshot(@org.jetbrains.annotations.NotNull()
    java.lang.String didId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    public final void startRealtime(@org.jetbrains.annotations.NotNull()
    java.lang.String didId, @org.jetbrains.annotations.NotNull()
    kotlinx.coroutines.CoroutineScope scope) {
    }
    
    public final void stopRealtime() {
    }
    
    private final java.lang.Object replaceSnapshot(com.secta9ine.didapp.v2.contract.PlayerSnapshotDto snapshot, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.util.Map<java.lang.String, java.lang.String> parseMetadata(java.lang.String metadataJson) {
        return null;
    }
}
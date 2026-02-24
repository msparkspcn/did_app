package com.secta9ine.didapp.v2.data.remote;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J*\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\n0\u000fJ\u0006\u0010\u0011\u001a\u00020\nJ\u0012\u0010\u0012\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0013\u001a\u00020\fH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/secta9ine/didapp/v2/data/remote/SnapshotWebSocketClient;", "", "okHttpClient", "Lokhttp3/OkHttpClient;", "gson", "Lcom/google/gson/Gson;", "(Lokhttp3/OkHttpClient;Lcom/google/gson/Gson;)V", "webSocket", "Lokhttp3/WebSocket;", "connect", "", "wsUrl", "", "didId", "onSnapshot", "Lkotlin/Function1;", "Lcom/secta9ine/didapp/v2/contract/PlayerSnapshotDto;", "disconnect", "parseSnapshot", "text", "app_debug"})
public final class SnapshotWebSocketClient {
    @org.jetbrains.annotations.NotNull()
    private final okhttp3.OkHttpClient okHttpClient = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    @org.jetbrains.annotations.Nullable()
    private okhttp3.WebSocket webSocket;
    
    @javax.inject.Inject()
    public SnapshotWebSocketClient(@org.jetbrains.annotations.NotNull()
    okhttp3.OkHttpClient okHttpClient, @org.jetbrains.annotations.NotNull()
    com.google.gson.Gson gson) {
        super();
    }
    
    public final void connect(@org.jetbrains.annotations.NotNull()
    java.lang.String wsUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String didId, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.secta9ine.didapp.v2.contract.PlayerSnapshotDto, kotlin.Unit> onSnapshot) {
    }
    
    public final void disconnect() {
    }
    
    private final com.secta9ine.didapp.v2.contract.PlayerSnapshotDto parseSnapshot(java.lang.String text) {
        return null;
    }
}
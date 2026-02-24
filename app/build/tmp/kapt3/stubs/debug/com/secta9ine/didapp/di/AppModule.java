package com.secta9ine.didapp.di;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0007\u001a\u00020\bH\u0007J\b\u0010\t\u001a\u00020\nH\u0007J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0006H\u0007J\b\u0010\u000e\u001a\u00020\u000fH\u0007J\b\u0010\u0010\u001a\u00020\u0011H\u0007J\u0018\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u000fH\u0007J\u0010\u0010\u0016\u001a\u00020\u00172\u0006\u0010\r\u001a\u00020\u0006H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/secta9ine/didapp/di/AppModule;", "", "()V", "DEV_API_BASE_URL", "", "provideDatabase", "Lcom/secta9ine/didapp/data/local/DidDatabase;", "context", "Landroid/content/Context;", "provideDidApi", "Lcom/secta9ine/didapp/data/remote/DidApi;", "provideDidDao", "Lcom/secta9ine/didapp/data/local/DidDao;", "database", "provideGson", "Lcom/google/gson/Gson;", "provideOkHttpClient", "Lokhttp3/OkHttpClient;", "provideV2PlayerApi", "Lcom/secta9ine/didapp/v2/data/remote/V2PlayerApi;", "okHttpClient", "gson", "provideV2SnapshotDao", "Lcom/secta9ine/didapp/v2/data/local/V2SnapshotDao;", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class AppModule {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String DEV_API_BASE_URL = "http://10.212.44.212:8080/api/";
    @org.jetbrains.annotations.NotNull()
    public static final com.secta9ine.didapp.di.AppModule INSTANCE = null;
    
    private AppModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.secta9ine.didapp.data.local.DidDatabase provideDatabase(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.secta9ine.didapp.data.local.DidDao provideDidDao(@org.jetbrains.annotations.NotNull()
    com.secta9ine.didapp.data.local.DidDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final com.secta9ine.didapp.v2.data.local.V2SnapshotDao provideV2SnapshotDao(@org.jetbrains.annotations.NotNull()
    com.secta9ine.didapp.data.local.DidDatabase database) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.google.gson.Gson provideGson() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final okhttp3.OkHttpClient provideOkHttpClient() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.secta9ine.didapp.data.remote.DidApi provideDidApi() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.secta9ine.didapp.v2.data.remote.V2PlayerApi provideV2PlayerApi(@org.jetbrains.annotations.NotNull()
    okhttp3.OkHttpClient okHttpClient, @org.jetbrains.annotations.NotNull()
    com.google.gson.Gson gson) {
        return null;
    }
}
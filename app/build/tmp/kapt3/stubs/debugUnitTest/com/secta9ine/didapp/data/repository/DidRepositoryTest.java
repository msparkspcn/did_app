package com.secta9ine.didapp.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000b\u001a\u00020\fH\u0007J\f\u0010\r\u001a\u00060\fj\u0002`\u000eH\u0007J\f\u0010\u000f\u001a\u00060\fj\u0002`\u000eH\u0007J\f\u0010\u0010\u001a\u00060\fj\u0002`\u000eH\u0007J\b\u0010\u0011\u001a\u00020\fH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/secta9ine/didapp/data/repository/DidRepositoryTest;", "", "()V", "api", "Lcom/secta9ine/didapp/data/remote/DidApi;", "dao", "Lcom/secta9ine/didapp/data/local/DidDao;", "downloader", "Lcom/secta9ine/didapp/util/AssetDownloader;", "repository", "Lcom/secta9ine/didapp/data/repository/DidRepository;", "setup", "", "syncWithRemote should clear all when API returns empty list", "Lkotlinx/coroutines/test/TestResult;", "syncWithRemote should fetch from API, save to DAO, and download assets", "syncWithRemote should inject samples when API fails and DB is empty", "teardown", "app_debugUnitTest"})
public final class DidRepositoryTest {
    private com.secta9ine.didapp.data.repository.DidRepository repository;
    @org.jetbrains.annotations.NotNull()
    private final com.secta9ine.didapp.data.remote.DidApi api = null;
    @org.jetbrains.annotations.NotNull()
    private final com.secta9ine.didapp.data.local.DidDao dao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.secta9ine.didapp.util.AssetDownloader downloader = null;
    
    public DidRepositoryTest() {
        super();
    }
    
    @org.junit.Before()
    public final void setup() {
    }
    
    @org.junit.After()
    public final void teardown() {
    }
}
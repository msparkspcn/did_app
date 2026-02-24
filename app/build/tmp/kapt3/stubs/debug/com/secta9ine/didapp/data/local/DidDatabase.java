package com.secta9ine.didapp.data.local;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&\u00a8\u0006\u0007"}, d2 = {"Lcom/secta9ine/didapp/data/local/DidDatabase;", "Landroidx/room/RoomDatabase;", "()V", "didDao", "Lcom/secta9ine/didapp/data/local/DidDao;", "v2SnapshotDao", "Lcom/secta9ine/didapp/v2/data/local/V2SnapshotDao;", "app_debug"})
@androidx.room.Database(entities = {com.secta9ine.didapp.data.local.DidEntity.class, com.secta9ine.didapp.v2.data.local.V2SnapshotEntity.class, com.secta9ine.didapp.v2.data.local.V2ZoneEntity.class, com.secta9ine.didapp.v2.data.local.V2AssetEntity.class, com.secta9ine.didapp.v2.data.local.V2ZonePlaylistItemEntity.class}, version = 2, exportSchema = false)
public abstract class DidDatabase extends androidx.room.RoomDatabase {
    
    public DidDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.secta9ine.didapp.data.local.DidDao didDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.secta9ine.didapp.v2.data.local.V2SnapshotDao v2SnapshotDao();
}
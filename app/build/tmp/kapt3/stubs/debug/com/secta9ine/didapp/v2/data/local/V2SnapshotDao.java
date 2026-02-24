package com.secta9ine.didapp.v2.data.local;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u0004H\u00a7@\u00a2\u0006\u0002\u0010\u0005J\u000e\u0010\u0006\u001a\u00020\u0004H\u00a7@\u00a2\u0006\u0002\u0010\u0005J\u000e\u0010\u0007\u001a\u00020\u0004H\u00a7@\u00a2\u0006\u0002\u0010\u0005J\u000e\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\u0005J\u001c\u0010\n\u001a\u00020\u00042\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u0014\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\f0\u0010H\'J\u0010\u0010\u0012\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00130\u0010H\'J\u0014\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f0\u0010H\'J\u0014\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\f0\u0010H\'J@\u0010\u0017\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u00132\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00160\f2\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00110\f2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\r0\fH\u0097@\u00a2\u0006\u0002\u0010\u001cJ\u001c\u0010\u001d\u001a\u00020\u00042\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00110\fH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u0016\u0010\u001e\u001a\u00020\u00042\u0006\u0010\u001f\u001a\u00020\u0013H\u00a7@\u00a2\u0006\u0002\u0010 J\u001c\u0010!\u001a\u00020\u00042\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00160\fH\u00a7@\u00a2\u0006\u0002\u0010\u000e\u00a8\u0006\""}, d2 = {"Lcom/secta9ine/didapp/v2/data/local/V2SnapshotDao;", "", "()V", "clearAssets", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearZonePlaylistItems", "clearZones", "hasSnapshot", "", "insertZonePlaylistItems", "entities", "", "Lcom/secta9ine/didapp/v2/data/local/V2ZonePlaylistItemEntity;", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeAssets", "Lkotlinx/coroutines/flow/Flow;", "Lcom/secta9ine/didapp/v2/data/local/V2AssetEntity;", "observeSnapshot", "Lcom/secta9ine/didapp/v2/data/local/V2SnapshotEntity;", "observeZonePlaylistItems", "observeZones", "Lcom/secta9ine/didapp/v2/data/local/V2ZoneEntity;", "replaceSnapshot", "snapshot", "zones", "assets", "zonePlaylistItems", "(Lcom/secta9ine/didapp/v2/data/local/V2SnapshotEntity;Ljava/util/List;Ljava/util/List;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "upsertAssets", "upsertSnapshot", "entity", "(Lcom/secta9ine/didapp/v2/data/local/V2SnapshotEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "upsertZones", "app_debug"})
@androidx.room.Dao()
public abstract class V2SnapshotDao {
    
    public V2SnapshotDao() {
        super();
    }
    
    @androidx.room.Query(value = "SELECT * FROM v2_snapshot LIMIT 1")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.secta9ine.didapp.v2.data.local.V2SnapshotEntity> observeSnapshot();
    
    @androidx.room.Query(value = "SELECT * FROM v2_zone ORDER BY zIndex ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.secta9ine.didapp.v2.data.local.V2ZoneEntity>> observeZones();
    
    @androidx.room.Query(value = "SELECT * FROM v2_asset")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.secta9ine.didapp.v2.data.local.V2AssetEntity>> observeAssets();
    
    @androidx.room.Query(value = "SELECT * FROM v2_zone_playlist_item ORDER BY zoneId ASC, playOrder ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.secta9ine.didapp.v2.data.local.V2ZonePlaylistItemEntity>> observeZonePlaylistItems();
    
    @androidx.room.Query(value = "SELECT COUNT(*) > 0 FROM v2_snapshot")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object hasSnapshot(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsertSnapshot(@org.jetbrains.annotations.NotNull()
    com.secta9ine.didapp.v2.data.local.V2SnapshotEntity entity, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsertZones(@org.jetbrains.annotations.NotNull()
    java.util.List<com.secta9ine.didapp.v2.data.local.V2ZoneEntity> entities, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsertAssets(@org.jetbrains.annotations.NotNull()
    java.util.List<com.secta9ine.didapp.v2.data.local.V2AssetEntity> entities, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertZonePlaylistItems(@org.jetbrains.annotations.NotNull()
    java.util.List<com.secta9ine.didapp.v2.data.local.V2ZonePlaylistItemEntity> entities, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM v2_zone")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearZones(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM v2_asset")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearAssets(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM v2_zone_playlist_item")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearZonePlaylistItems(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Transaction()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object replaceSnapshot(@org.jetbrains.annotations.NotNull()
    com.secta9ine.didapp.v2.data.local.V2SnapshotEntity snapshot, @org.jetbrains.annotations.NotNull()
    java.util.List<com.secta9ine.didapp.v2.data.local.V2ZoneEntity> zones, @org.jetbrains.annotations.NotNull()
    java.util.List<com.secta9ine.didapp.v2.data.local.V2AssetEntity> assets, @org.jetbrains.annotations.NotNull()
    java.util.List<com.secta9ine.didapp.v2.data.local.V2ZonePlaylistItemEntity> zonePlaylistItems, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}
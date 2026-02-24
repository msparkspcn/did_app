package com.secta9ine.didapp.v2.data.local;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0017\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001B9\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\b\u0010\t\u001a\u0004\u0018\u00010\b\u0012\u0006\u0010\n\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0017\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\bH\u00c6\u0003J\u0010\u0010\u001b\u001a\u0004\u0018\u00010\bH\u00c6\u0003\u00a2\u0006\u0002\u0010\u000fJ\t\u0010\u001c\u001a\u00020\u0005H\u00c6\u0003JL\u0010\u001d\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\b2\b\b\u0002\u0010\n\u001a\u00020\u0005H\u00c6\u0001\u00a2\u0006\u0002\u0010\u001eJ\u0013\u0010\u001f\u001a\u00020 2\b\u0010!\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\"\u001a\u00020\bH\u00d6\u0001J\t\u0010#\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0015\u0010\t\u001a\u0004\u0018\u00010\b\u00a2\u0006\n\n\u0002\u0010\u0010\u001a\u0004\b\u000e\u0010\u000fR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\n\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\r\u00a8\u0006$"}, d2 = {"Lcom/secta9ine/didapp/v2/data/local/V2ZonePlaylistItemEntity;", "", "id", "", "zoneId", "", "assetId", "playOrder", "", "durationSec", "transition", "(JLjava/lang/String;Ljava/lang/String;ILjava/lang/Integer;Ljava/lang/String;)V", "getAssetId", "()Ljava/lang/String;", "getDurationSec", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getId", "()J", "getPlayOrder", "()I", "getTransition", "getZoneId", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "(JLjava/lang/String;Ljava/lang/String;ILjava/lang/Integer;Ljava/lang/String;)Lcom/secta9ine/didapp/v2/data/local/V2ZonePlaylistItemEntity;", "equals", "", "other", "hashCode", "toString", "app_debug"})
@androidx.room.Entity(tableName = "v2_zone_playlist_item")
public final class V2ZonePlaylistItemEntity {
    @androidx.room.PrimaryKey(autoGenerate = true)
    private final long id = 0L;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String zoneId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String assetId = null;
    private final int playOrder = 0;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Integer durationSec = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String transition = null;
    
    public V2ZonePlaylistItemEntity(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String zoneId, @org.jetbrains.annotations.NotNull()
    java.lang.String assetId, int playOrder, @org.jetbrains.annotations.Nullable()
    java.lang.Integer durationSec, @org.jetbrains.annotations.NotNull()
    java.lang.String transition) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getZoneId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAssetId() {
        return null;
    }
    
    public final int getPlayOrder() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getDurationSec() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTransition() {
        return null;
    }
    
    public final long component1() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    public final int component4() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.secta9ine.didapp.v2.data.local.V2ZonePlaylistItemEntity copy(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String zoneId, @org.jetbrains.annotations.NotNull()
    java.lang.String assetId, int playOrder, @org.jetbrains.annotations.Nullable()
    java.lang.Integer durationSec, @org.jetbrains.annotations.NotNull()
    java.lang.String transition) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}
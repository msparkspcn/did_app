package com.secta9ine.didapp.v2.contract;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B[\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0018\u0010\b\u001a\u0014\u0012\u0004\u0012\u00020\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\t\u0012\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000e0\t\u00a2\u0006\u0002\u0010\u000fJ\t\u0010\u001b\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0015J\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0015J\t\u0010\u001e\u001a\u00020\u0007H\u00c6\u0003J\u001b\u0010\u001f\u001a\u0014\u0012\u0004\u0012\u00020\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\tH\u00c6\u0003J\u0015\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000e0\tH\u00c6\u0003Jl\u0010!\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u001a\b\u0002\u0010\b\u001a\u0014\u0012\u0004\u0012\u00020\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\t2\u0014\b\u0002\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000e0\tH\u00c6\u0001\u00a2\u0006\u0002\u0010\"J\u0013\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010&\u001a\u00020\'H\u00d6\u0001J\t\u0010(\u001a\u00020\nH\u00d6\u0001R\u001d\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000e0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0015\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u0016\u001a\u0004\b\u0014\u0010\u0015R\u0015\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u0016\u001a\u0004\b\u0017\u0010\u0015R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R#\u0010\b\u001a\u0014\u0012\u0004\u0012\u00020\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0011\u00a8\u0006)"}, d2 = {"Lcom/secta9ine/didapp/v2/contract/PlayerSnapshotDto;", "", "version", "", "validFromEpochSec", "validToEpochSec", "layout", "Lcom/secta9ine/didapp/v2/contract/LayoutDto;", "zonePlaylists", "", "", "", "Lcom/secta9ine/didapp/v2/contract/ZonePlaylistItemDto;", "assets", "Lcom/secta9ine/didapp/v2/contract/AssetDto;", "(JLjava/lang/Long;Ljava/lang/Long;Lcom/secta9ine/didapp/v2/contract/LayoutDto;Ljava/util/Map;Ljava/util/Map;)V", "getAssets", "()Ljava/util/Map;", "getLayout", "()Lcom/secta9ine/didapp/v2/contract/LayoutDto;", "getValidFromEpochSec", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getValidToEpochSec", "getVersion", "()J", "getZonePlaylists", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "(JLjava/lang/Long;Ljava/lang/Long;Lcom/secta9ine/didapp/v2/contract/LayoutDto;Ljava/util/Map;Ljava/util/Map;)Lcom/secta9ine/didapp/v2/contract/PlayerSnapshotDto;", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
public final class PlayerSnapshotDto {
    private final long version = 0L;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long validFromEpochSec = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long validToEpochSec = null;
    @org.jetbrains.annotations.NotNull()
    private final com.secta9ine.didapp.v2.contract.LayoutDto layout = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, java.util.List<com.secta9ine.didapp.v2.contract.ZonePlaylistItemDto>> zonePlaylists = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, com.secta9ine.didapp.v2.contract.AssetDto> assets = null;
    
    public PlayerSnapshotDto(long version, @org.jetbrains.annotations.Nullable()
    java.lang.Long validFromEpochSec, @org.jetbrains.annotations.Nullable()
    java.lang.Long validToEpochSec, @org.jetbrains.annotations.NotNull()
    com.secta9ine.didapp.v2.contract.LayoutDto layout, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, ? extends java.util.List<com.secta9ine.didapp.v2.contract.ZonePlaylistItemDto>> zonePlaylists, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, com.secta9ine.didapp.v2.contract.AssetDto> assets) {
        super();
    }
    
    public final long getVersion() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getValidFromEpochSec() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getValidToEpochSec() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.secta9ine.didapp.v2.contract.LayoutDto getLayout() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.util.List<com.secta9ine.didapp.v2.contract.ZonePlaylistItemDto>> getZonePlaylists() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, com.secta9ine.didapp.v2.contract.AssetDto> getAssets() {
        return null;
    }
    
    public final long component1() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.secta9ine.didapp.v2.contract.LayoutDto component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.util.List<com.secta9ine.didapp.v2.contract.ZonePlaylistItemDto>> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, com.secta9ine.didapp.v2.contract.AssetDto> component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.secta9ine.didapp.v2.contract.PlayerSnapshotDto copy(long version, @org.jetbrains.annotations.Nullable()
    java.lang.Long validFromEpochSec, @org.jetbrains.annotations.Nullable()
    java.lang.Long validToEpochSec, @org.jetbrains.annotations.NotNull()
    com.secta9ine.didapp.v2.contract.LayoutDto layout, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, ? extends java.util.List<com.secta9ine.didapp.v2.contract.ZonePlaylistItemDto>> zonePlaylists, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, com.secta9ine.didapp.v2.contract.AssetDto> assets) {
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
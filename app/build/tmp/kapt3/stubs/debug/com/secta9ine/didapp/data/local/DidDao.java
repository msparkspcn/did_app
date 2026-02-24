package com.secta9ine.didapp.data.local;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u001c\u0010\u0005\u001a\u00020\u00032\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0014\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00070\u000bH\'J\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\f0\u0007H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u001c\u0010\u000e\u001a\u00020\u00032\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\f0\u0007H\u00a7@\u00a2\u0006\u0002\u0010\tJ\u001e\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\u0013\u00a8\u0006\u0014"}, d2 = {"Lcom/secta9ine/didapp/data/local/DidDao;", "", "clearAll", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteItemsNotIn", "ids", "", "", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllDidItems", "Lkotlinx/coroutines/flow/Flow;", "Lcom/secta9ine/didapp/data/local/DidEntity;", "getAllDidItemsSync", "insertItems", "items", "updateLocalPath", "id", "localPath", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface DidDao {
    
    @androidx.room.Query(value = "SELECT * FROM did_items ORDER BY timestamp ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.secta9ine.didapp.data.local.DidEntity>> getAllDidItems();
    
    @androidx.room.Query(value = "SELECT * FROM did_items")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllDidItemsSync(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.secta9ine.didapp.data.local.DidEntity>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertItems(@org.jetbrains.annotations.NotNull()
    java.util.List<com.secta9ine.didapp.data.local.DidEntity> items, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM did_items WHERE id NOT IN (:ids)")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteItemsNotIn(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> ids, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE did_items SET localPath = :localPath WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateLocalPath(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String localPath, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM did_items")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object clearAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}
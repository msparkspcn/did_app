package com.secta9ine.didapp.v2.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class V2SnapshotDao_Impl extends V2SnapshotDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<V2SnapshotEntity> __insertionAdapterOfV2SnapshotEntity;

  private final EntityInsertionAdapter<V2ZoneEntity> __insertionAdapterOfV2ZoneEntity;

  private final EntityInsertionAdapter<V2AssetEntity> __insertionAdapterOfV2AssetEntity;

  private final EntityInsertionAdapter<V2ZonePlaylistItemEntity> __insertionAdapterOfV2ZonePlaylistItemEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearZones;

  private final SharedSQLiteStatement __preparedStmtOfClearAssets;

  private final SharedSQLiteStatement __preparedStmtOfClearZonePlaylistItems;

  public V2SnapshotDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfV2SnapshotEntity = new EntityInsertionAdapter<V2SnapshotEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `v2_snapshot` (`id`,`version`,`validFromEpochSec`,`validToEpochSec`,`layoutId`,`canvasWidth`,`canvasHeight`,`coordinateSystem`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @Nullable final V2SnapshotEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getVersion());
        if (entity.getValidFromEpochSec() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getValidFromEpochSec());
        }
        if (entity.getValidToEpochSec() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getValidToEpochSec());
        }
        if (entity.getLayoutId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getLayoutId());
        }
        statement.bindLong(6, entity.getCanvasWidth());
        statement.bindLong(7, entity.getCanvasHeight());
        if (entity.getCoordinateSystem() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getCoordinateSystem());
        }
        statement.bindLong(9, entity.getUpdatedAt());
      }
    };
    this.__insertionAdapterOfV2ZoneEntity = new EntityInsertionAdapter<V2ZoneEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `v2_zone` (`zoneId`,`x`,`y`,`width`,`height`,`zIndex`,`backgroundHex`,`fitMode`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @Nullable final V2ZoneEntity entity) {
        if (entity.getZoneId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getZoneId());
        }
        statement.bindDouble(2, entity.getX());
        statement.bindDouble(3, entity.getY());
        statement.bindDouble(4, entity.getWidth());
        statement.bindDouble(5, entity.getHeight());
        statement.bindLong(6, entity.getZIndex());
        if (entity.getBackgroundHex() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getBackgroundHex());
        }
        if (entity.getFitMode() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getFitMode());
        }
      }
    };
    this.__insertionAdapterOfV2AssetEntity = new EntityInsertionAdapter<V2AssetEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `v2_asset` (`assetId`,`type`,`source`,`metadataJson`,`defaultDurationSec`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @Nullable final V2AssetEntity entity) {
        if (entity.getAssetId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getAssetId());
        }
        if (entity.getType() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getType());
        }
        if (entity.getSource() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getSource());
        }
        if (entity.getMetadataJson() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getMetadataJson());
        }
        if (entity.getDefaultDurationSec() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getDefaultDurationSec());
        }
      }
    };
    this.__insertionAdapterOfV2ZonePlaylistItemEntity = new EntityInsertionAdapter<V2ZonePlaylistItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `v2_zone_playlist_item` (`id`,`zoneId`,`assetId`,`playOrder`,`durationSec`,`transition`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @Nullable final V2ZonePlaylistItemEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getZoneId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getZoneId());
        }
        if (entity.getAssetId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getAssetId());
        }
        statement.bindLong(4, entity.getPlayOrder());
        if (entity.getDurationSec() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getDurationSec());
        }
        if (entity.getTransition() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getTransition());
        }
      }
    };
    this.__preparedStmtOfClearZones = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM v2_zone";
        return _query;
      }
    };
    this.__preparedStmtOfClearAssets = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM v2_asset";
        return _query;
      }
    };
    this.__preparedStmtOfClearZonePlaylistItems = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM v2_zone_playlist_item";
        return _query;
      }
    };
  }

  @Override
  public Object upsertSnapshot(final V2SnapshotEntity entity,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfV2SnapshotEntity.insert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertZones(final List<V2ZoneEntity> entities,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfV2ZoneEntity.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertAssets(final List<V2AssetEntity> entities,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfV2AssetEntity.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertZonePlaylistItems(final List<V2ZonePlaylistItemEntity> entities,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfV2ZonePlaylistItemEntity.insert(entities);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object replaceSnapshot(final V2SnapshotEntity snapshot, final List<V2ZoneEntity> zones,
      final List<V2AssetEntity> assets, final List<V2ZonePlaylistItemEntity> zonePlaylistItems,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> V2SnapshotDao_Impl.super.replaceSnapshot(snapshot, zones, assets, zonePlaylistItems, __cont), $completion);
  }

  @Override
  public Object clearZones(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearZones.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearZones.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAssets(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAssets.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearAssets.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearZonePlaylistItems(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearZonePlaylistItems.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearZonePlaylistItems.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<V2SnapshotEntity> observeSnapshot() {
    final String _sql = "SELECT * FROM v2_snapshot LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"v2_snapshot"}, new Callable<V2SnapshotEntity>() {
      @Override
      @Nullable
      public V2SnapshotEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "version");
          final int _cursorIndexOfValidFromEpochSec = CursorUtil.getColumnIndexOrThrow(_cursor, "validFromEpochSec");
          final int _cursorIndexOfValidToEpochSec = CursorUtil.getColumnIndexOrThrow(_cursor, "validToEpochSec");
          final int _cursorIndexOfLayoutId = CursorUtil.getColumnIndexOrThrow(_cursor, "layoutId");
          final int _cursorIndexOfCanvasWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "canvasWidth");
          final int _cursorIndexOfCanvasHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "canvasHeight");
          final int _cursorIndexOfCoordinateSystem = CursorUtil.getColumnIndexOrThrow(_cursor, "coordinateSystem");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final V2SnapshotEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final long _tmpVersion;
            _tmpVersion = _cursor.getLong(_cursorIndexOfVersion);
            final Long _tmpValidFromEpochSec;
            if (_cursor.isNull(_cursorIndexOfValidFromEpochSec)) {
              _tmpValidFromEpochSec = null;
            } else {
              _tmpValidFromEpochSec = _cursor.getLong(_cursorIndexOfValidFromEpochSec);
            }
            final Long _tmpValidToEpochSec;
            if (_cursor.isNull(_cursorIndexOfValidToEpochSec)) {
              _tmpValidToEpochSec = null;
            } else {
              _tmpValidToEpochSec = _cursor.getLong(_cursorIndexOfValidToEpochSec);
            }
            final String _tmpLayoutId;
            if (_cursor.isNull(_cursorIndexOfLayoutId)) {
              _tmpLayoutId = null;
            } else {
              _tmpLayoutId = _cursor.getString(_cursorIndexOfLayoutId);
            }
            final int _tmpCanvasWidth;
            _tmpCanvasWidth = _cursor.getInt(_cursorIndexOfCanvasWidth);
            final int _tmpCanvasHeight;
            _tmpCanvasHeight = _cursor.getInt(_cursorIndexOfCanvasHeight);
            final String _tmpCoordinateSystem;
            if (_cursor.isNull(_cursorIndexOfCoordinateSystem)) {
              _tmpCoordinateSystem = null;
            } else {
              _tmpCoordinateSystem = _cursor.getString(_cursorIndexOfCoordinateSystem);
            }
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new V2SnapshotEntity(_tmpId,_tmpVersion,_tmpValidFromEpochSec,_tmpValidToEpochSec,_tmpLayoutId,_tmpCanvasWidth,_tmpCanvasHeight,_tmpCoordinateSystem,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<V2ZoneEntity>> observeZones() {
    final String _sql = "SELECT * FROM v2_zone ORDER BY zIndex ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"v2_zone"}, new Callable<List<V2ZoneEntity>>() {
      @Override
      @NonNull
      public List<V2ZoneEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfZoneId = CursorUtil.getColumnIndexOrThrow(_cursor, "zoneId");
          final int _cursorIndexOfX = CursorUtil.getColumnIndexOrThrow(_cursor, "x");
          final int _cursorIndexOfY = CursorUtil.getColumnIndexOrThrow(_cursor, "y");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfZIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "zIndex");
          final int _cursorIndexOfBackgroundHex = CursorUtil.getColumnIndexOrThrow(_cursor, "backgroundHex");
          final int _cursorIndexOfFitMode = CursorUtil.getColumnIndexOrThrow(_cursor, "fitMode");
          final List<V2ZoneEntity> _result = new ArrayList<V2ZoneEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final V2ZoneEntity _item;
            final String _tmpZoneId;
            if (_cursor.isNull(_cursorIndexOfZoneId)) {
              _tmpZoneId = null;
            } else {
              _tmpZoneId = _cursor.getString(_cursorIndexOfZoneId);
            }
            final float _tmpX;
            _tmpX = _cursor.getFloat(_cursorIndexOfX);
            final float _tmpY;
            _tmpY = _cursor.getFloat(_cursorIndexOfY);
            final float _tmpWidth;
            _tmpWidth = _cursor.getFloat(_cursorIndexOfWidth);
            final float _tmpHeight;
            _tmpHeight = _cursor.getFloat(_cursorIndexOfHeight);
            final int _tmpZIndex;
            _tmpZIndex = _cursor.getInt(_cursorIndexOfZIndex);
            final String _tmpBackgroundHex;
            if (_cursor.isNull(_cursorIndexOfBackgroundHex)) {
              _tmpBackgroundHex = null;
            } else {
              _tmpBackgroundHex = _cursor.getString(_cursorIndexOfBackgroundHex);
            }
            final String _tmpFitMode;
            if (_cursor.isNull(_cursorIndexOfFitMode)) {
              _tmpFitMode = null;
            } else {
              _tmpFitMode = _cursor.getString(_cursorIndexOfFitMode);
            }
            _item = new V2ZoneEntity(_tmpZoneId,_tmpX,_tmpY,_tmpWidth,_tmpHeight,_tmpZIndex,_tmpBackgroundHex,_tmpFitMode);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<V2AssetEntity>> observeAssets() {
    final String _sql = "SELECT * FROM v2_asset";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"v2_asset"}, new Callable<List<V2AssetEntity>>() {
      @Override
      @NonNull
      public List<V2AssetEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfAssetId = CursorUtil.getColumnIndexOrThrow(_cursor, "assetId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfMetadataJson = CursorUtil.getColumnIndexOrThrow(_cursor, "metadataJson");
          final int _cursorIndexOfDefaultDurationSec = CursorUtil.getColumnIndexOrThrow(_cursor, "defaultDurationSec");
          final List<V2AssetEntity> _result = new ArrayList<V2AssetEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final V2AssetEntity _item;
            final String _tmpAssetId;
            if (_cursor.isNull(_cursorIndexOfAssetId)) {
              _tmpAssetId = null;
            } else {
              _tmpAssetId = _cursor.getString(_cursorIndexOfAssetId);
            }
            final String _tmpType;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmpType = null;
            } else {
              _tmpType = _cursor.getString(_cursorIndexOfType);
            }
            final String _tmpSource;
            if (_cursor.isNull(_cursorIndexOfSource)) {
              _tmpSource = null;
            } else {
              _tmpSource = _cursor.getString(_cursorIndexOfSource);
            }
            final String _tmpMetadataJson;
            if (_cursor.isNull(_cursorIndexOfMetadataJson)) {
              _tmpMetadataJson = null;
            } else {
              _tmpMetadataJson = _cursor.getString(_cursorIndexOfMetadataJson);
            }
            final Integer _tmpDefaultDurationSec;
            if (_cursor.isNull(_cursorIndexOfDefaultDurationSec)) {
              _tmpDefaultDurationSec = null;
            } else {
              _tmpDefaultDurationSec = _cursor.getInt(_cursorIndexOfDefaultDurationSec);
            }
            _item = new V2AssetEntity(_tmpAssetId,_tmpType,_tmpSource,_tmpMetadataJson,_tmpDefaultDurationSec);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<V2ZonePlaylistItemEntity>> observeZonePlaylistItems() {
    final String _sql = "SELECT * FROM v2_zone_playlist_item ORDER BY zoneId ASC, playOrder ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"v2_zone_playlist_item"}, new Callable<List<V2ZonePlaylistItemEntity>>() {
      @Override
      @NonNull
      public List<V2ZonePlaylistItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfZoneId = CursorUtil.getColumnIndexOrThrow(_cursor, "zoneId");
          final int _cursorIndexOfAssetId = CursorUtil.getColumnIndexOrThrow(_cursor, "assetId");
          final int _cursorIndexOfPlayOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "playOrder");
          final int _cursorIndexOfDurationSec = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSec");
          final int _cursorIndexOfTransition = CursorUtil.getColumnIndexOrThrow(_cursor, "transition");
          final List<V2ZonePlaylistItemEntity> _result = new ArrayList<V2ZonePlaylistItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final V2ZonePlaylistItemEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpZoneId;
            if (_cursor.isNull(_cursorIndexOfZoneId)) {
              _tmpZoneId = null;
            } else {
              _tmpZoneId = _cursor.getString(_cursorIndexOfZoneId);
            }
            final String _tmpAssetId;
            if (_cursor.isNull(_cursorIndexOfAssetId)) {
              _tmpAssetId = null;
            } else {
              _tmpAssetId = _cursor.getString(_cursorIndexOfAssetId);
            }
            final int _tmpPlayOrder;
            _tmpPlayOrder = _cursor.getInt(_cursorIndexOfPlayOrder);
            final Integer _tmpDurationSec;
            if (_cursor.isNull(_cursorIndexOfDurationSec)) {
              _tmpDurationSec = null;
            } else {
              _tmpDurationSec = _cursor.getInt(_cursorIndexOfDurationSec);
            }
            final String _tmpTransition;
            if (_cursor.isNull(_cursorIndexOfTransition)) {
              _tmpTransition = null;
            } else {
              _tmpTransition = _cursor.getString(_cursorIndexOfTransition);
            }
            _item = new V2ZonePlaylistItemEntity(_tmpId,_tmpZoneId,_tmpAssetId,_tmpPlayOrder,_tmpDurationSec,_tmpTransition);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object hasSnapshot(final Continuation<? super Boolean> $completion) {
    final String _sql = "SELECT COUNT(*) > 0 FROM v2_snapshot";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Boolean>() {
      @Override
      @NonNull
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp == null ? null : _tmp != 0;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

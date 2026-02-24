package com.secta9ine.didapp.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.secta9ine.didapp.v2.data.local.V2SnapshotDao;
import com.secta9ine.didapp.v2.data.local.V2SnapshotDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class DidDatabase_Impl extends DidDatabase {
  private volatile DidDao _didDao;

  private volatile V2SnapshotDao _v2SnapshotDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `did_items` (`id` TEXT NOT NULL, `type` TEXT NOT NULL, `content` TEXT NOT NULL, `localPath` TEXT, `timestamp` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `v2_snapshot` (`id` INTEGER NOT NULL, `version` INTEGER NOT NULL, `validFromEpochSec` INTEGER, `validToEpochSec` INTEGER, `layoutId` TEXT NOT NULL, `canvasWidth` INTEGER NOT NULL, `canvasHeight` INTEGER NOT NULL, `coordinateSystem` TEXT NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `v2_zone` (`zoneId` TEXT NOT NULL, `x` REAL NOT NULL, `y` REAL NOT NULL, `width` REAL NOT NULL, `height` REAL NOT NULL, `zIndex` INTEGER NOT NULL, `backgroundHex` TEXT, `fitMode` TEXT NOT NULL, PRIMARY KEY(`zoneId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `v2_asset` (`assetId` TEXT NOT NULL, `type` TEXT NOT NULL, `source` TEXT NOT NULL, `metadataJson` TEXT NOT NULL, `defaultDurationSec` INTEGER, PRIMARY KEY(`assetId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `v2_zone_playlist_item` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `zoneId` TEXT NOT NULL, `assetId` TEXT NOT NULL, `playOrder` INTEGER NOT NULL, `durationSec` INTEGER, `transition` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '45691d7ea9f3825ce99a9bbb26e414f4')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `did_items`");
        db.execSQL("DROP TABLE IF EXISTS `v2_snapshot`");
        db.execSQL("DROP TABLE IF EXISTS `v2_zone`");
        db.execSQL("DROP TABLE IF EXISTS `v2_asset`");
        db.execSQL("DROP TABLE IF EXISTS `v2_zone_playlist_item`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsDidItems = new HashMap<String, TableInfo.Column>(5);
        _columnsDidItems.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDidItems.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDidItems.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDidItems.put("localPath", new TableInfo.Column("localPath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDidItems.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDidItems = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDidItems = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDidItems = new TableInfo("did_items", _columnsDidItems, _foreignKeysDidItems, _indicesDidItems);
        final TableInfo _existingDidItems = TableInfo.read(db, "did_items");
        if (!_infoDidItems.equals(_existingDidItems)) {
          return new RoomOpenHelper.ValidationResult(false, "did_items(com.secta9ine.didapp.data.local.DidEntity).\n"
                  + " Expected:\n" + _infoDidItems + "\n"
                  + " Found:\n" + _existingDidItems);
        }
        final HashMap<String, TableInfo.Column> _columnsV2Snapshot = new HashMap<String, TableInfo.Column>(9);
        _columnsV2Snapshot.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsV2Snapshot.put("version", new TableInfo.Column("version", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsV2Snapshot.put("validFromEpochSec", new TableInfo.Column("validFromEpochSec", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsV2Snapshot.put("validToEpochSec", new TableInfo.Column("validToEpochSec", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsV2Snapshot.put("layoutId", new TableInfo.Column("layoutId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsV2Snapshot.put("canvasWidth", new TableInfo.Column("canvasWidth", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsV2Snapshot.put("canvasHeight", new TableInfo.Column("canvasHeight", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsV2Snapshot.put("coordinateSystem", new TableInfo.Column("coordinateSystem", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsV2Snapshot.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysV2Snapshot = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesV2Snapshot = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoV2Snapshot = new TableInfo("v2_snapshot", _columnsV2Snapshot, _foreignKeysV2Snapshot, _indicesV2Snapshot);
        final TableInfo _existingV2Snapshot = TableInfo.read(db, "v2_snapshot");
        if (!_infoV2Snapshot.equals(_existingV2Snapshot)) {
          return new RoomOpenHelper.ValidationResult(false, "v2_snapshot(com.secta9ine.didapp.v2.data.local.V2SnapshotEntity).\n"
                  + " Expected:\n" + _infoV2Snapshot + "\n"
                  + " Found:\n" + _existingV2Snapshot);
        }
        final HashMap<String, TableInfo.Column> _columnsV2Zone = new HashMap<String, TableInfo.Column>(8);
        _columnsV2Zone.put("zoneId", new TableInfo.Column("zoneId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsV2Zone.put("x", new TableInfo.Column("x", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsV2Zone.put("y", new TableInfo.Column("y", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsV2Zone.put("width", new TableInfo.Column("width", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsV2Zone.put("height", new TableInfo.Column("height", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsV2Zone.put("zIndex", new TableInfo.Column("zIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsV2Zone.put("backgroundHex", new TableInfo.Column("backgroundHex", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsV2Zone.put("fitMode", new TableInfo.Column("fitMode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysV2Zone = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesV2Zone = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoV2Zone = new TableInfo("v2_zone", _columnsV2Zone, _foreignKeysV2Zone, _indicesV2Zone);
        final TableInfo _existingV2Zone = TableInfo.read(db, "v2_zone");
        if (!_infoV2Zone.equals(_existingV2Zone)) {
          return new RoomOpenHelper.ValidationResult(false, "v2_zone(com.secta9ine.didapp.v2.data.local.V2ZoneEntity).\n"
                  + " Expected:\n" + _infoV2Zone + "\n"
                  + " Found:\n" + _existingV2Zone);
        }
        final HashMap<String, TableInfo.Column> _columnsV2Asset = new HashMap<String, TableInfo.Column>(5);
        _columnsV2Asset.put("assetId", new TableInfo.Column("assetId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsV2Asset.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsV2Asset.put("source", new TableInfo.Column("source", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsV2Asset.put("metadataJson", new TableInfo.Column("metadataJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsV2Asset.put("defaultDurationSec", new TableInfo.Column("defaultDurationSec", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysV2Asset = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesV2Asset = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoV2Asset = new TableInfo("v2_asset", _columnsV2Asset, _foreignKeysV2Asset, _indicesV2Asset);
        final TableInfo _existingV2Asset = TableInfo.read(db, "v2_asset");
        if (!_infoV2Asset.equals(_existingV2Asset)) {
          return new RoomOpenHelper.ValidationResult(false, "v2_asset(com.secta9ine.didapp.v2.data.local.V2AssetEntity).\n"
                  + " Expected:\n" + _infoV2Asset + "\n"
                  + " Found:\n" + _existingV2Asset);
        }
        final HashMap<String, TableInfo.Column> _columnsV2ZonePlaylistItem = new HashMap<String, TableInfo.Column>(6);
        _columnsV2ZonePlaylistItem.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsV2ZonePlaylistItem.put("zoneId", new TableInfo.Column("zoneId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsV2ZonePlaylistItem.put("assetId", new TableInfo.Column("assetId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsV2ZonePlaylistItem.put("playOrder", new TableInfo.Column("playOrder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsV2ZonePlaylistItem.put("durationSec", new TableInfo.Column("durationSec", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsV2ZonePlaylistItem.put("transition", new TableInfo.Column("transition", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysV2ZonePlaylistItem = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesV2ZonePlaylistItem = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoV2ZonePlaylistItem = new TableInfo("v2_zone_playlist_item", _columnsV2ZonePlaylistItem, _foreignKeysV2ZonePlaylistItem, _indicesV2ZonePlaylistItem);
        final TableInfo _existingV2ZonePlaylistItem = TableInfo.read(db, "v2_zone_playlist_item");
        if (!_infoV2ZonePlaylistItem.equals(_existingV2ZonePlaylistItem)) {
          return new RoomOpenHelper.ValidationResult(false, "v2_zone_playlist_item(com.secta9ine.didapp.v2.data.local.V2ZonePlaylistItemEntity).\n"
                  + " Expected:\n" + _infoV2ZonePlaylistItem + "\n"
                  + " Found:\n" + _existingV2ZonePlaylistItem);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "45691d7ea9f3825ce99a9bbb26e414f4", "5b0d527e30b009404e34ce876012e07e");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "did_items","v2_snapshot","v2_zone","v2_asset","v2_zone_playlist_item");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `did_items`");
      _db.execSQL("DELETE FROM `v2_snapshot`");
      _db.execSQL("DELETE FROM `v2_zone`");
      _db.execSQL("DELETE FROM `v2_asset`");
      _db.execSQL("DELETE FROM `v2_zone_playlist_item`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(DidDao.class, DidDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(V2SnapshotDao.class, V2SnapshotDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public DidDao didDao() {
    if (_didDao != null) {
      return _didDao;
    } else {
      synchronized(this) {
        if(_didDao == null) {
          _didDao = new DidDao_Impl(this);
        }
        return _didDao;
      }
    }
  }

  @Override
  public V2SnapshotDao v2SnapshotDao() {
    if (_v2SnapshotDao != null) {
      return _v2SnapshotDao;
    } else {
      synchronized(this) {
        if(_v2SnapshotDao == null) {
          _v2SnapshotDao = new V2SnapshotDao_Impl(this);
        }
        return _v2SnapshotDao;
      }
    }
  }
}

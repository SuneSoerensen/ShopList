package com.example.shoplist;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

public class ListItemDao_Impl implements ListItemDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfListItem;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfListItem;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfListItem;

  public ListItemDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfListItem = new EntityInsertionAdapter<ListItem>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `ListItem`(`id`,`title`,`price`,`checkBox`,`store`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ListItem value) {
        stmt.bindLong(1, value.id);
        if (value.title == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.title);
        }
        stmt.bindLong(3, value.price);
        final int _tmp;
        _tmp = value.checkBox ? 1 : 0;
        stmt.bindLong(4, _tmp);
        if (value.store == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.store);
        }
      }
    };
    this.__deletionAdapterOfListItem = new EntityDeletionOrUpdateAdapter<ListItem>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `ListItem` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ListItem value) {
        stmt.bindLong(1, value.id);
      }
    };
    this.__updateAdapterOfListItem = new EntityDeletionOrUpdateAdapter<ListItem>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `ListItem` SET `id` = ?,`title` = ?,`price` = ?,`checkBox` = ?,`store` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ListItem value) {
        stmt.bindLong(1, value.id);
        if (value.title == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.title);
        }
        stmt.bindLong(3, value.price);
        final int _tmp;
        _tmp = value.checkBox ? 1 : 0;
        stmt.bindLong(4, _tmp);
        if (value.store == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.store);
        }
        stmt.bindLong(6, value.id);
      }
    };
  }

  @Override
  public void insert(ListItem... listItems) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfListItem.insert(listItems);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(ListItem listItem) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfListItem.handle(listItem);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(ListItem le) {
    __db.beginTransaction();
    try {
      __updateAdapterOfListItem.handle(le);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<ListItem> getAll() {
    final String _sql = "SELECT * FROM Listitem";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfTitle = _cursor.getColumnIndexOrThrow("title");
      final int _cursorIndexOfPrice = _cursor.getColumnIndexOrThrow("price");
      final int _cursorIndexOfCheckBox = _cursor.getColumnIndexOrThrow("checkBox");
      final int _cursorIndexOfStore = _cursor.getColumnIndexOrThrow("store");
      final List<ListItem> _result = new ArrayList<ListItem>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ListItem _item;
        _item = new ListItem();
        _item.id = _cursor.getInt(_cursorIndexOfId);
        _item.title = _cursor.getString(_cursorIndexOfTitle);
        _item.price = _cursor.getInt(_cursorIndexOfPrice);
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfCheckBox);
        _item.checkBox = _tmp != 0;
        _item.store = _cursor.getString(_cursorIndexOfStore);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}

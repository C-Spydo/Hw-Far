package com.spydotechcorps.hwfar.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.spydotechcorps.hwfar.database.Dbhandler;

public class MyContentProvider extends ContentProvider{

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private Dbhandler myDB;
    public static final int DISTANCES = 1;
    public static final int DISTANCES_ID = 2;

    @Override
    public boolean onCreate() {
        myDB = new Dbhandler(getContext());
                //getContext(), null, null, 1);
        return true;
    }


    private static final String AUTHORITY =
            // i think there is an error, here, my authority is a bit wrong.
            "com.spydotechcorps.hwfar.provider.MyContentProvider";
       // "com.spydotechcorps.hwfar.provider;.MyContentProvider";
    private static final String DISTANCES_TABLE = "distances";
    public static final Uri BASE_URI =
        Uri.parse("content://" + AUTHORITY);
    public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_URI, Dbhandler.TABLE_DISTANCES);




    static {
        sURIMatcher.addURI(AUTHORITY, Dbhandler.TABLE_DISTANCES, DISTANCES);
        sURIMatcher.addURI(AUTHORITY, Dbhandler.TABLE_DISTANCES + "/#",
                DISTANCES_ID);
    }

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        int rowsDeleted = 0;

        switch (uriType) {
            case DISTANCES:
                rowsDeleted = sqlDB.delete(Dbhandler.TABLE_DISTANCES,
                        selection,
                        selectionArgs);
                break;

            case DISTANCES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(Dbhandler.TABLE_DISTANCES,
                            Dbhandler.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(Dbhandler.TABLE_DISTANCES,
                            Dbhandler.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;

    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        int uriType = sURIMatcher.match(uri);

        SQLiteDatabase sqlDB = myDB.getWritableDatabase();

        long _id = 0;
        _id = sqlDB.insert(Dbhandler.TABLE_DISTANCES,
                null, values);
       /* switch (uriType) {
            case DISTANCES:
                id = sqlDB.insert(Dbhandler.TABLE_DISTANCES,
                        null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: "
                        + uri);
        }*/
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(DISTANCES_TABLE + "/" + _id);

    }

   /* @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }*/

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(Dbhandler.TABLE_DISTANCES);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case DISTANCES_ID:
                queryBuilder.appendWhere(Dbhandler.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            case DISTANCES:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        Cursor cursor = queryBuilder.query(myDB.getReadableDatabase(),
                projection, selection, selectionArgs, null, null,
                sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),
                uri);

        return cursor;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = myDB.getWritableDatabase();
        int rowsUpdated = 0;

        switch (uriType) {
            case DISTANCES:
                rowsUpdated =
                        sqlDB.update(Dbhandler.TABLE_DISTANCES,
                                values,
                                selection,
                                selectionArgs);
                break;
            case DISTANCES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated =
                            sqlDB.update(Dbhandler.TABLE_DISTANCES,
                                    values,
                                    Dbhandler.COLUMN_ID + "=" + id,
                                    null);
                } else {
                    rowsUpdated =
                            sqlDB.update(Dbhandler.TABLE_DISTANCES,
                                    values,
                                    Dbhandler.COLUMN_ID + "=" + id
                                            + " and "
                                            + selection,
                                    selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " +
                        uri);
        }
        getContext().getContentResolver().notifyChange(uri,
                null);
        return rowsUpdated;

    }
}

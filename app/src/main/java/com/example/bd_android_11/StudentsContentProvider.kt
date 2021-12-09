package com.example.bd_android_11

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.text.TextUtils
import android.util.Log

class StudentsContentProvider : ContentProvider() {

    val AUTHORITY : String = "com.chuduk.providers.StudentsProvider";
    val STUDETNTS_PATH : String = "students";
    val GROUPS_PATH : String = "groups";

    val STUDENTS_URI : Uri = Uri.parse("content://" + AUTHORITY + "/" + STUDETNTS_PATH);
    val GROUPS_URI : Uri = Uri.parse("content://" + AUTHORITY + "/" + GROUPS_PATH);

    // Типы данных
    // набор строк
    val STUDENTS_CONTENT_TYPE = ("vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + STUDETNTS_PATH)

    // одна строка
    val STUDENTS_CONTENT_ITEM_TYPE = ("vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + STUDETNTS_PATH)

    val GROUPS_CONTENT_TYPE = ("vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + GROUPS_PATH)

    // одна строка
    val GROUPS_CONTENT_ITEM_TYPE = ("vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + GROUPS_PATH)

    val uriMatcher : UriMatcher = UriMatcher(UriMatcher.NO_MATCH);
    val URI_STUDENTS = 1
    val URI_STUDENTS_ID = 2
    val URI_GROUPS = 3
    val URI_GROUPS_ID = 4

    var dbHelper : DatabaseHelper? = null;

    override fun onCreate(): Boolean {
        uriMatcher.addURI(AUTHORITY, STUDETNTS_PATH, URI_STUDENTS);
        uriMatcher.addURI(AUTHORITY, STUDETNTS_PATH + "/#", URI_STUDENTS_ID);
        uriMatcher.addURI(AUTHORITY, GROUPS_PATH, URI_GROUPS);
        uriMatcher.addURI(AUTHORITY, GROUPS_PATH + "/#", URI_GROUPS_ID);

        dbHelper = DatabaseHelper(context);
        return true;
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        var selectionString = selection;
        var sortString = sortOrder;
        var table = "";
        when(uriMatcher.match(uri)) {
            URI_STUDENTS -> {
                table = "STUDENTS";
                if(TextUtils.isEmpty(sortOrder)) {
                    sortString = "STUDENTNAME ASC";
                }
            }
            URI_GROUPS -> {
                table = "GROUPS";
                if(TextUtils.isEmpty(sortOrder)) {
                    sortString = "GROUPNAME ASC";
                }
            }
            URI_STUDENTS_ID -> {
                table = "STUDENTS";
                val id = uri.lastPathSegment;
                if (TextUtils.isEmpty(selectionString)) {
                    selectionString = "IDSTUDENT = " + id
                } else {
                    selectionString = selection + " AND IDSTUDENT = " + id;
                }
            }
            URI_GROUPS_ID -> {
                table = "GROUPS";
                val id = uri.lastPathSegment;
                if (TextUtils.isEmpty(selectionString)) {
                    selectionString = "IDGROUP = " + id
                } else {
                    selectionString = selection + " AND IDGROUP = " + id;
                }
            }
            else -> throw IllegalArgumentException("Wrong URI: " + uri)
        }
        val db = dbHelper?.readableDatabase;
        val cursor = db?.query(table, projection, selectionString, selectionArgs, null, null, sortString);
        cursor?.setNotificationUri(context?.contentResolver, STUDENTS_URI);
        return cursor;
    }

    override fun getType(uri: Uri): String? {
        when (uriMatcher.match(uri)) {
            URI_STUDENTS -> return STUDENTS_CONTENT_TYPE
            URI_STUDENTS_ID -> return STUDENTS_CONTENT_ITEM_TYPE
            URI_GROUPS -> return GROUPS_CONTENT_TYPE
            URI_GROUPS_ID -> return GROUPS_CONTENT_ITEM_TYPE
        }
        return null;
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (uriMatcher.match(uri) != URI_STUDENTS)
            throw IllegalArgumentException("Wrong URI: " + uri);

        val db = dbHelper?.writableDatabase;
        val rowID = db!!.insert("STUDENTS", null, values)
        val resultUri = ContentUris.withAppendedId(STUDENTS_URI, rowID)
        // уведомляем ContentResolver, что данные по адресу resultUri изменились
        // уведомляем ContentResolver, что данные по адресу resultUri изменились
        context!!.contentResolver.notifyChange(resultUri, null)
        return resultUri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        var selectionString = selection;
        when (uriMatcher.match(uri)) {
            URI_STUDENTS_ID -> {
                val id = uri.lastPathSegment;
                if (TextUtils.isEmpty(selection)) {
                    selectionString = "IDSTUDENT = " + id;
                } else {
                    selectionString = selection + " AND IDSTUDENT = " + id;
                }
            }
            else -> throw IllegalArgumentException("Wrong URI: " + uri)
        }
        val db = dbHelper?.writableDatabase;
        val cnt = db!!.delete("STUDENTS", selectionString, selectionArgs)
        context!!.contentResolver.notifyChange(uri, null)
        return cnt;
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        var selectionString = selection;
        when (uriMatcher.match(uri)) {
            URI_STUDENTS -> Log.d("log: ", "URI_STUDENTS")
            URI_STUDENTS_ID -> {
                val id = uri.lastPathSegment;
                if (TextUtils.isEmpty(selection)) {
                    selectionString = "IDSTUDENT = " + id;
                } else {
                    selectionString = selection + " AND IDSTUDENT = " + id;
                }
            }
            else -> throw IllegalArgumentException("Wrong URI: $uri")
        }
        val db = dbHelper?.writableDatabase;
        val cnt  = db?.update("STUDENTS", values, selectionString, selectionArgs)
        context!!.contentResolver.notifyChange(uri, null)
        return cnt!!;
    }
}
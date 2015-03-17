package com.example.test.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xs on 2014/12/20.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "tempInfo.db";
    private static final String STUDENT_TABLE_NAME = "student";
    private static final String TEACHER_TABLE_NAME = "teacher";
    private static final String PICTURE_TABLE_NAME = "picture";
    private static final String CLASS_TABLE_NAME = "class";
    private static final String COURSE_TABLE_NAME = "course";
    private static final String ROLL_TABLE_NAME = "roll";

    private static List<String> create_table_sqls = new ArrayList<String>();

    static {
        create_table_sqls.add("drop table if exists student");
        create_table_sqls.add(" create table " +
                "student(Sid text,Sname text, Ssex text, image blob," +
                "classDepartment text, Sphone text, Semail text)");

        create_table_sqls.add("drop table if exists teacher");
        create_table_sqls.add(" create table " +
                "teacher(Tid text,Tname text, Tsex text, Tposition text," +
                "Tphone text, Temail text, image blob)");

        create_table_sqls.add("drop table if exists course");
        create_table_sqls.add(" create table course(course_id text,course_name text, " +
                "course_credit float(3,1), course_elective text,Tid text)");

        create_table_sqls.add("drop table if exists roll");
        create_table_sqls.add(" create table roll(Sid text,course_id text," +
                " Rtime datetime, Rarrvied text,Rscore float(3,1))");
    }


    private SQLiteDatabase db;

    public DBHelper(Context c) {
        super(c, DB_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        Iterator<String> iterator = create_table_sqls.iterator();
        while (iterator.hasNext()) {
            db.execSQL(iterator.next());
        }
    }

    public void insert(String tableName, ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(tableName, null, values);
        db.close();
    }

    public Cursor query(String tableName) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(tableName, null, null, null, null, null, null);
        return c;
    }

    public Cursor query(String tableName, String selection, String selectionargs[]) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(tableName, null, selection, selectionargs, null, null, null);
        return c;
    }

    public void del(int id, String tableName) {
        if (db == null)
            db = getWritableDatabase();
        db.delete(tableName, "id=?", new String[]{String.valueOf(id)});
    }

    public void close() {
        if (db != null)
            db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}


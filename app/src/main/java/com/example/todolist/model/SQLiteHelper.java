package com.example.todolist.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todolist.model.Task;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Task.db";
    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE tbltask(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "time TEXT," +
                "category TEXT," +
                "status INTEGER)";
        db.execSQL(sql);
    }

    public long addTask(Task t){
        ContentValues v = new ContentValues();
        v.put("name", t.getName());
        v.put("time", t.getTime());
        v.put("category", t.getCategory());
        v.put("status", t.getStatus());
        SQLiteDatabase sld = getWritableDatabase();
        return sld.insert("tbltask", null, v);
    }

    public List<Task> getAll(){
        List<Task> rs = new ArrayList<>();
        SQLiteDatabase sld = getReadableDatabase();
        Cursor c = sld.query("tbltask", null, null, null, null, null, null);
        while((c != null) && c.moveToNext()){
            int id = c.getInt(0);
            String name = c.getString(1);
            String time = c.getString(2);
            String category = c.getString(3);
            int status = c.getInt(4);
            rs.add(new Task(id, name, time, category, status));
        }
        return rs;
    }

    public int updateTask(Task t){
        ContentValues v = new ContentValues();
        v.put("id", t.getId());
        v.put("name", t.getName());
        v.put("time", t.getTime());
        v.put("category", t.getCategory());
        v.put("status", t.getStatus());
        SQLiteDatabase sld = getWritableDatabase();
        String wClause = "id=?";
        String[] wArgs = {String.valueOf(t.getId())};
        return sld.update("tbltask",v, wClause, wArgs);
    }

    public int deleteTask(int id){
        String wClause = "id=?";
        String[] wArgs = {String.valueOf(id)};
        SQLiteDatabase sld = getWritableDatabase();
        return sld.delete("tbltask", wClause, wArgs);
    }

    public List<Task> getByName(String name){
        String sql = "name like ?";
        String[] args = {"%" + name + "%"};
        SQLiteDatabase slq = getReadableDatabase();
        Cursor c = slq.query("tbltask",null, sql, args, null, null, null);
        List<Task> rs = new ArrayList<>();
        while((c!=null) && c.moveToNext()){
            int id = c.getInt(0);
            String tname = c.getString(1);
            String time = c.getString(2);
            String category = c.getString(3);
            int status = c.getInt(4);
            rs.add(new Task(id, tname, time, category, status));
        }
        return rs;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

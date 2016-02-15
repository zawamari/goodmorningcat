package com.cat.morning.goodmorningcat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by imarie on 16/02/15.
 */
public class MyDBHelper extends SQLiteOpenHelper {

    // コンストラクタ
    public MyDBHelper( Context context ){
        // 任意のデータベースファイル名と、バージョンを指定する
        super( context, "catAlerm.db", null, 1 );
    }


    @Override
    public void onCreate( SQLiteDatabase db ) {
        // テーブルを作成。SQLの文法は通常のSQLiteと同様
        db.execSQL(
                "create table alert_set_table ("
                        + "id  integer primary key autoincrement not null, "
                        + "week text not null, "
                        + "time  text not null, "
                        + "cat_type integer not null");
        // 必要なら、ここで他のテーブルを作成したり、初期データを挿入したりする
    }


    /**
     * アプリケーションの更新などによって、データベースのバージョンが上がった場合に実行される処理
     * 今回は割愛
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 取りあえず、空実装でよい
    }
}

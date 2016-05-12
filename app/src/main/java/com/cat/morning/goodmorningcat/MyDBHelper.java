package com.cat.morning.goodmorningcat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by imarie on 16/02/15.
 */
public class MyDBHelper extends SQLiteOpenHelper {

    private static MyDBHelper mInstance = null;

    // コンストラクタ
    public MyDBHelper( Context context ){
        // 任意のデータベースファイル名と、バージョンを指定する
        super( context, "catAlerm.db", null, 1 );
    }

    public static synchronized MyDBHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyDBHelper(context);
        }
        return mInstance;
    }


    @Override
    public void onCreate( SQLiteDatabase db ) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS alert_set_table ("
                        + "id  INTEGER PRIMARY KEY AUTOINCREMENT , "
                        + "week BLOB NOT NULL, "                // 曜日
                        + "time TEXT NOT NULL, "                // 時間
                        + "vibrate INTEGER NOT NULL, "          // バイブレーション 0:有効 1:無効
                        + "cat_type INTEGER NOT NULL DEFAULT 0, "
                        + "status INTEGER NOT NULL DEFAULT 0, "  // status 0:有効 1:無効
                        + "manner INTEGER NOT NULL DEFAULT 0, "  // マナーモード： 0:マナーモードでも音を鳴らす 1:マナーモードなら音を鳴らさない
                        + "limit_time INTEGER NOT NULL DEFAULT 0, "   // 音を鳴らす最大時間
                        + "snooze INTEGER NOT NULL DEFAULT 0, "  // スヌーズ 0;あり 1;なし
                        + "volume INTEGER NOT NULL DEFAULT 3)");// ボリューム 0:なし 1; 2; 3;中間 4; 5;
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS cat ("
                        + "id  INTEGER PRIMARY KEY AUTOINCREMENT , "
                        + "name TEXT, "
                        + "status INTEGER DEFAULT '0')");
        // status 0:有効 1:無効

        db.execSQL("INSERT INTO cat (name, status) VALUES ('太郎', 0)" );
        db.execSQL("INSERT INTO cat (name, status) VALUES ('ザビエル', 0)" );
        db.execSQL("INSERT INTO cat (name, status) VALUES ('みけこ', 0)" );
        db.execSQL("INSERT INTO cat (name, status) VALUES ('マイケル', 0)" );

    }


    /**
     * アプリケーションの更新などによって、データベースのバージョンが上がった場合に実行される処理
     * 今回は割愛
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS alert_set_table");
        onCreate(db);
    }
}

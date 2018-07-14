package com.jingbanyun.im.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.jingbanyun.im.app.IMApplication
import org.jetbrains.anko.db.*

class DatabaseHelper(ctx: Context = IMApplication.instance) : ManagedSQLiteOpenHelper(ctx, NAME, null, VERSION) {

    companion object {
        val NAME = "im.db"
        val VERSION = 1
    }

    /**
     * 数据库的创建
     */
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.createTable(ContactTable.NAME, true,
                ContactTable.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                ContactTable.NAME to TEXT)
    }

    /**
     * 数据库的升级
     */
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.dropTable(ContactTable.NAME,true)
        onCreate(p0)
    }
}
package com.example.ahmedessam.livehealthysales.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by ahmed essam on 21/05/2017.
 */

@Database(name = AppDataBase.NAME ,version = AppDataBase.VERSION)
public class AppDataBase {
    public static final String NAME ="salesDB";
    public static final int VERSION =1;
}

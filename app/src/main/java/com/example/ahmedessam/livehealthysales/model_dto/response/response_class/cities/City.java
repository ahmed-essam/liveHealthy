package com.example.ahmedessam.livehealthysales.model_dto.response.response_class.cities;

import com.example.ahmedessam.livehealthysales.database.AppDataBase;
import com.example.ahmedessam.livehealthysales.model_dto.response.response_class.Areas.Area;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;
import java.util.Locale;

/**
 * Created by ahmed essam on 13/08/2017.
 */
@Table(database = AppDataBase.class)
public class City extends BaseModel{
    @Column
    private String Name;
    @Column
    private String Name_AR;
    @Column
    private int ID;

    public static void clearCitiesDB(){
        List<City> movieList = SQLite.select().from(City.class).queryList();
        FlowManager.getModelAdapter(City.class).deleteAll(movieList);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName_AR() {
        return Name_AR;
    }

    public void setName_AR(String name_AR) {
        Name_AR = name_AR;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        if (Locale.getDefault().getDisplayLanguage() == "english"){
            return getName();
        }else
            return getName_AR();
    }
}

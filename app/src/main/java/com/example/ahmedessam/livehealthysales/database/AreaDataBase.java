package com.example.ahmedessam.livehealthysales.database;

import com.example.ahmedessam.livehealthysales.model_dto.response.response_class.Areas.Area;
import com.example.ahmedessam.livehealthysales.model_dto.response.response_class.cities.City;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;
import java.util.Locale;

/**
 * Created by ahmed essam on 21/08/2017.
 */
@Table(database = AppDataBase.class)
public class AreaDataBase extends BaseModel {
    @Column
    private String Name;
    @Column
    private String Name_AR;
    @PrimaryKey
    private int ID;
    @ForeignKey(tableClass = City.class)
    private int CityId;

    public static void clearAreasDB(){
        List<Area> movieList = SQLite.select().from(Area.class).queryList();
        FlowManager.getModelAdapter(Area.class).deleteAll(movieList);
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

    public int getCityId() {
        return CityId;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }

    @Override
    public String toString() {
        if (Locale.getDefault().getDisplayLanguage() == "english") {
            return getName();
        }else
            return getName_AR();
    }

}

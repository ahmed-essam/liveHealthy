package com.yackeen.livehealthy.sales.model_dto.response.response_class.getAllAreas;

import com.yackeen.livehealthy.sales.database.AppDataBase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by ahmed essam on 22/08/2017.
 */
@Table(database = AppDataBase.class)
public class AllArea extends BaseModel {
    @Column
    private String Name;
    @Column
    private String Name_AR;
    @PrimaryKey
    private int ID;
    @Column
    private String City_Name;
    @Column
    private String City_Name_AR;
    @Column
    private int City_ID;

    public static void ClearAreasDB(){
        List<AllArea> movieList = SQLite.select().from(AllArea.class).queryList();
        FlowManager.getModelAdapter(AllArea.class).deleteAll(movieList);
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

    public String getCity_Name() {
        return City_Name;
    }

    public void setCity_Name(String city_Name) {
        City_Name = city_Name;
    }

    public String getCity_Name_AR() {
        return City_Name_AR;
    }

    public void setCity_Name_AR(String city_Name_AR) {
        City_Name_AR = city_Name_AR;
    }

    public int getCity_ID() {
        return City_ID;
    }

    public void setCity_ID(int city_ID) {
        City_ID = city_ID;
    }
}

package com.example.ahmedessam.livehealthysales.model_dto.response.response_class.Areas;

import java.util.Locale;

/**
 * Created by ahmed essam on 13/08/2017.
 */

public class Area {
    private String Name;
    private String Name_AR;
    private int ID;

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
        if (Locale.getDefault().getDisplayLanguage() == "english") {
            return getName();
        }else
            return getName_AR();
    }
}

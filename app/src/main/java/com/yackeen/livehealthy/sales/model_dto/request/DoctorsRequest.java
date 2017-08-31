package com.yackeen.livehealthy.sales.model_dto.request;

/**
 * Created by ahmed essam on 08/08/2017.
 */

public class DoctorsRequest {

    private String Name;
    private int PageNumber;
    private int NumberRecords;
    private String lang;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getPageNumber() {
        return PageNumber;
    }

    public void setPageNumber(int pageNumber) {
        PageNumber = pageNumber;
    }

    public int getNumberRecords() {
        return NumberRecords;
    }

    public void setNumberRecords(int numberRecords) {
        NumberRecords = numberRecords;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}

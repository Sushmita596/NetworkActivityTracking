package com.example.sampleapp;

public class DBModel {

    int _id;
    String creationTime;
    String updatedTime;
    String events;

    public DBModel(){   }
    public DBModel(int id, String creationTime, String updatedTime, String events){
        this._id = id;
        this.creationTime = creationTime;
        this.updatedTime = updatedTime;
        this.events = events;
    }

    public DBModel(String creationTime, String updatedTime, String events){
        this.creationTime = creationTime;
        this.updatedTime = updatedTime;
        this.events = events;
    }

    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this._id = id;
    }

    public String getCreationTime(){
        return this.creationTime;
    }

    public void setCreationTime(String creationTime){
        this.creationTime = creationTime;
    }

    public String getUpdatedTime(){
        return this.updatedTime;
    }

    public void setUpdatedTime(String updatedTime){
        this.updatedTime = updatedTime;
    }

    public String getEvents(){
        return this.events;
    }

    public void setEvents(String events){
        this.events = events;
    }
}

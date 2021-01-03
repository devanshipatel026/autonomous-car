package model;

import java.util.List;

public class Data {
    //Encapsulation
    private List<Track> track;

    private List<TravelLog> travelLog;

    public void setTrack(List<Track> track){
        this.track = track;
    }
    public List<Track> getTrack(){
        return this.track;
    }
    public void setTravelLog(List<TravelLog> travelLog){
        this.travelLog = travelLog;
    }
    public List<TravelLog> getTravelLog(){
        return this.travelLog;
    }
}


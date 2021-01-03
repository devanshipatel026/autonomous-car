package model;

public class TravelLog
{
    private int position;

    private String laneChange;

    public void setPosition(int position){
        this.position = position;
    }
    public int getPosition(){
        return this.position;
    }
    public void setLaneChange(String laneChange){
        this.laneChange = laneChange;
    }
    public String getLaneChange(){
        return this.laneChange;
    }
}

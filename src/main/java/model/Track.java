package model;

import java.util.List;

public class Track
{
    private int position;

    private List<String> obstacles;

    public void setPosition(int position){
        this.position = position;
    }
    public int getPosition(){
        return this.position;
    }
    public void setObstacles(List<String> obstacles){
        this.obstacles = obstacles;
    }
    public List<String> getObstacles(){
        return this.obstacles;
    }
}

import com.google.gson.Gson;
import model.Data;
import model.Track;
import model.TravelLog;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class AutonomousCar {
    public static int position = -1;
    public static void main(String[] args){
        // create Gson instance
        Gson gson = new Gson();
        try {
            // create a reader using the json files for successful and out-of-bounds response
            Reader reader = Files.newBufferedReader(Paths.get("src\\main\\java\\data\\out-of-bounds.json"));
           // Reader reader = Files.newBufferedReader(Paths.get("src\\main\\java\\data\\success.json"));

            Data data = gson.fromJson(reader,Data.class);

            // creating list that stores traversing between lanes
            List<String> laneList = processTravelLog(data.getTravelLog());

            //Initializing position to -1, in case of no lane data
            if(position != -1){
                errorResponse(position);
                return;
            }

            List<Track> trackList = data.getTrack();

            // This condition triggers when there is no data for travelLog
            if(laneList.isEmpty()){
                for (int i = 0; i < trackList.size(); i++) {
                    for(int j=0; j< trackList.get(i).getObstacles().size(); j++){
                        if(trackList.get(i).getObstacles().get(j).equals('b')){
                            errorResponse(trackList.get(i).getPosition());
                            return;
                        }
                    }
                }
                // No travelLog Data but there is no obstacle in trackList
                successResponse();
                return;
            }

            //Comparing the positions in the data files with the laneList generated and returning the responses accordingly
            for (int i = 0; i < trackList.size(); i++) {
                String lane;
                if(trackList.get(i).getPosition() > laneList.size()){
                    lane = laneList.get(laneList.size()-1);
                }else{
                    lane = laneList.get(trackList.get(i).getPosition());
                }

                for(int j=0; j< trackList.get(i).getObstacles().size(); j++){
                    if(lane.equals(trackList.get(i).getObstacles().get(j))){
                        errorResponse(trackList.get(i).getPosition());
                        return;
                    }
                }

            }

            successResponse();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JSONObject successResponse() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("status", "success");
        System.out.println(json);
        return json;
    }

    private static JSONObject errorResponse(int position) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("status", "error");
        json.put("position", position);
        System.out.println(json);
        return json;
    }

    // This function creates List for each position as index and lane as value
    private static List<String> processTravelLog(List<TravelLog> travelLogs) {
        Collections.sort(travelLogs, new positionComparator());
        List<String> laneList = new ArrayList<>();

        // if travelLogs are not available
        if(travelLogs == null || travelLogs.size()==0){
            return laneList;
        }

        int j = 1;  //position
        laneList.add("b");  //adding b at position 0 of the laneList
        for (int i = 0; i < travelLogs.size(); i++) {
            while(j<travelLogs.get(i).getPosition()){
                //add same element in a list
                laneList.add(laneList.get(laneList.size()-1));   //getting lane of previous position
                j++;
                continue;
            }

            //If lane is a and lane changed to right, add b to laneList
            if ((laneList.get(laneList.size() - 1).equals("a") && travelLogs.get(i).getLaneChange().equals("right"))
            ) {
                laneList.add("b");
            }
            //If lane is b and lane changed to right, add c else add a to laneList
            else if ((laneList.get(laneList.size() - 1).equals("b"))
            ) {
                if(travelLogs.get(i).getLaneChange().equals("right")){
                    laneList.add("c");
                }
                else{
                    laneList.add("a");
                }
            }
            //If lane is c and lane changed to left, add b to laneList
            else if ((laneList.get(laneList.size() - 1).equals("c") && travelLogs.get(i).getLaneChange().equals("left"))
            ) {
                laneList.add("b");
            }
            else{
                // if there is no valid lane then returns position for throwing error
                position = j;
                return null;
            }
            j++;
        }
        return laneList;
    }

    //Using comparator to sort the array
    public static class positionComparator implements Comparator<TravelLog> {
        @Override
        public int compare(TravelLog o1, TravelLog o2) {
            if (o1.getPosition() <= o2.getPosition()) {
                return -1;
            } else {
                return 1;
            }
        }
    }

}

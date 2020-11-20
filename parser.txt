import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ScheduleDecoder {

    static String timeFile;

    public static void main(String[] args) throws IOException {
        timeFile = readString();
        //System.out.println(timeFile);
        parseFile(timeFile);
    }

    public static void parseFile(String file){
        String[] lines = file.split("\n");
        for(int i = 1; i < lines.length; i++){
            String[] data = lines[i].split(",");
            String storeNumber = data[0];
            //Resolve store master schedule
            Object schedule = new Object();
            addEvent(schedule, "18/12/20", data[5], data[6], true);
            addEvent(schedule, "19/12/20", data[7], data[8], true);
            addEvent(schedule, "20/12/20", data[9], data[10], true);
            addEvent(schedule, "21/12/20", data[11], data[12], true);
            addEvent(schedule, "22/12/20", data[13], data[14], true);
            addEvent(schedule, "23/12/20", data[15], data[16], true);
            addEvent(schedule, "24/12/20", data[17], data[18], true);
            addEvent(schedule, "25/12/20", data[19], data[19], false);
            addEvent(schedule, "26/12/20", data[20], data[21], true);
            addEvent(schedule, "27/12/20", data[22], data[23], true);
            addEvent(schedule, "28/12/20", data[24], data[25], true);
            addEvent(schedule, "29/12/20", data[26], data[27], true);
            addEvent(schedule, "30/12/20", data[28], data[29], true);
            addEvent(schedule, "31/12/20", data[30], data[31], true);
            addEvent(schedule, "1/01/21", data[32], data[33], true);
            break;
        }
    }

    //replace object with BBooleanSchedule, BDate, BTime, BTime
    public static void addEvent(Object schedule, String date, String startTime, String endTime, boolean event){
        if(endTime.equals("00:00")){endTime = "11:59";}
        //resolve specialEvents folder
        //remove events with matching date
        //if event = true
            //if startTime != 00:00
                //add false event 00:00 to startTime
            //add true event startTime to endTime
            //if endTime != 11:59
                //add false event endTime to 11:59
        //if event = false
            //add event 00:00 to 11:59
        System.out.println(date + " : " + startTime + " : " + endTime);
    }

    public static String readString(){
        StringBuilder contentBuilder = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new FileReader("docToRead.csv"))){
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null){
                contentBuilder.append(sCurrentLine).append("\n");
            }
        } catch (IOException e){
            System.out.println(e.toString());
        }
        return contentBuilder.toString();
    }
}

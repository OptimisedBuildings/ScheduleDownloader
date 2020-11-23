package com.optimised_buildings_ltd.ScheduleDownloader;

import com.honeywell.trend.driver.core.BTrendSystem;
import com.tridium.bql.projection.BProjectionTable;

import javax.baja.collection.BITable;
import javax.baja.collection.Column;
import javax.baja.collection.TableCursor;
import javax.baja.naming.BOrd;
import javax.baja.nre.annotations.Facet;
import javax.baja.nre.annotations.NiagaraAction;
import javax.baja.nre.annotations.NiagaraProperty;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.schedule.BBooleanSchedule;
import javax.baja.sys.*;
import java.util.Spliterator;

@NiagaraType
@NiagaraProperty(name = "csv", type = "BString", defaultValue = "BString.DEFAULT", facets = @Facet(name = "\"multiLine\"", value = "true"))
@NiagaraProperty(name = "report", type = "BString", defaultValue = "BString.DEFAULT", facets = @Facet(name = "\"multiLine\"", value = "true"))
@NiagaraAction(name = "importCsv")

public class BSpecialEventImporter extends BComponent {
/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.optimised_buildings_ltd.ScheduleDownloader.BSpecialEventImporter(4272065376)1.0$ @*/
/* Generated Mon Nov 23 13:02:15 GMT 2020 by Slot-o-Matic (c) Tridium, Inc. 2012 */

////////////////////////////////////////////////////////////////
// Property "csv"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code csv} property.
   * @see #getCsv
   * @see #setCsv
   */
  public static final Property csv = newProperty(0, BString.DEFAULT, BFacets.make("multiLine", true));
  
  /**
   * Get the {@code csv} property.
   * @see #csv
   */
  public String getCsv() { return getString(csv); }
  
  /**
   * Set the {@code csv} property.
   * @see #csv
   */
  public void setCsv(String v) { setString(csv, v, null); }

////////////////////////////////////////////////////////////////
// Property "report"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code report} property.
   * @see #getReport
   * @see #setReport
   */
  public static final Property report = newProperty(0, BString.DEFAULT, BFacets.make("multiLine", true));
  
  /**
   * Get the {@code report} property.
   * @see #report
   */
  public String getReport() { return getString(report); }
  
  /**
   * Set the {@code report} property.
   * @see #report
   */
  public void setReport(String v) { setString(report, v, null); }

////////////////////////////////////////////////////////////////
// Action "import"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code import} action.
   * @see #importCsv()
   */
  public static final Action importCsv = newAction(0, null);
  
  /**
   * Invoke the {@code import} action.
   * @see #importCsv
   */
  public void importCsv() { invoke(importCsv, null, null); }

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BSpecialEventImporter.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

  String lastError;

public void doImport(){
  String csv = this.getCsv();
  String[] lines = csv.split("\n");
  for(int i = 1; i < lines.length; i++){
    String[] data = lines[i].split(",");
    String storeNumber = data[0];
    BTrendSystem store = this.resolveStore(storeNumber);
    if(store == null){
      this.reportError(storeNumber,"BTrendSystem not found");
      continue;
    }
    BBooleanSchedule masterSchedule = ((BBooleanSchedule)store.get("masterSchedule"));
    if(masterSchedule == null){
      this.reportError(storeNumber,"BBooleanSchedule not found");
      continue;
    }
    try{
      this.addEvent(masterSchedule, BDate.make("18/12/20"), this.timeFormatter(data[5], false), this.timeFormatter(data[6], true), true);
      this.addEvent(masterSchedule, BDate.make("19/12/20"), this.timeFormatter(data[7], false), this.timeFormatter(data[8], true), true);
      this.addEvent(masterSchedule, BDate.make("20/12/20"), this.timeFormatter(data[9], false), this.timeFormatter(data[10], true), true);
      this.addEvent(masterSchedule, BDate.make("21/12/20"), this.timeFormatter(data[11], false), this.timeFormatter(data[12], true), true);
      this.addEvent(masterSchedule, BDate.make("22/12/20"), this.timeFormatter(data[13], false), this.timeFormatter(data[14], true), true);
      this.addEvent(masterSchedule, BDate.make("23/12/20"), this.timeFormatter(data[15], false), this.timeFormatter(data[16], true), true);
      this.addEvent(masterSchedule, BDate.make("24/12/20"), this.timeFormatter(data[17], false), this.timeFormatter(data[18], true), true);
      this.addEvent(masterSchedule, BDate.make("25/12/20"), this.timeFormatter(data[19], false), this.timeFormatter(data[19], true), false);
      this.addEvent(masterSchedule, BDate.make("26/12/20"), this.timeFormatter(data[20], false), this.timeFormatter(data[21], true), true);
      this.addEvent(masterSchedule, BDate.make("27/12/20"), this.timeFormatter(data[22], false), this.timeFormatter(data[23], true), true);
      this.addEvent(masterSchedule, BDate.make("28/12/20"), this.timeFormatter(data[24], false), this.timeFormatter(data[25], true), true);
      this.addEvent(masterSchedule, BDate.make("29/12/20"), this.timeFormatter(data[26], false), this.timeFormatter(data[27], true), true);
      this.addEvent(masterSchedule, BDate.make("30/12/20"), this.timeFormatter(data[28], false), this.timeFormatter(data[29], true), true);
      this.addEvent(masterSchedule, BDate.make("31/12/20"), this.timeFormatter(data[30], false), this.timeFormatter(data[31], true), true);
      this.addEvent(masterSchedule, BDate.make("01/01/21"), this.timeFormatter(data[32], false), this.timeFormatter(data[33], true), true);
    } catch (Exception e){
      this.reportError(storeNumber, e.getMessage());
      continue;
    }

  }

}

public BTime timeFormatter(String timeString, boolean end) throws Exception {
  if(timeString.contains("00:00") && end){
    return BTime.make(23,59,0);
  }
  String[] hoursAndMinutes = timeString.split(":");
  return BTime.make(Integer.parseInt(hoursAndMinutes[0]),Integer.parseInt(hoursAndMinutes[1]),0);
}

public void addEvent(BBooleanSchedule masterSchedule, BDate date, BTime startTime, BTime endTime, boolean eventType) throws Exception{
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
}



public void reportError(String storeNumber, String error){
  //TODO Add to report

}

public BTrendSystem resolveStore(String storeNumber){
  int storeCount = 0;
  BTrendSystem returnedSystem = null;
  Cursor c = ((BITable)BOrd.make("station:|slot:/Drivers|bql:select * from TrendN4:TrendSystem where displayName like '*" + storeNumber + "*'").resolve().get()).cursor();
  while(c.next()){
    storeCount++;
    returnedSystem = (BTrendSystem)c.get();
  }
  if(storeCount != 1) {
    lastError = storeCount + " results found";
    return null;
  } else {
    return returnedSystem;
  }
}


}

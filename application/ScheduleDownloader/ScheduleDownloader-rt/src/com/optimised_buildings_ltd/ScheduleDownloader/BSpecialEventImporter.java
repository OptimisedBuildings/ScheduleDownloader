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
import javax.baja.schedule.BCompositeSchedule;
import javax.baja.schedule.BDailySchedule;
import javax.baja.schedule.BDateSchedule;
import javax.baja.status.BStatusBoolean;
import javax.baja.sys.*;
import java.util.Spliterator;

@NiagaraType
@NiagaraProperty(name = "csv", type = "BString", defaultValue = "BString.DEFAULT", facets = @Facet(name = "\"multiLine\"", value = "true"))
@NiagaraProperty(name = "report", type = "BString", defaultValue = "BString.DEFAULT", facets = @Facet(name = "\"multiLine\"", value = "true"))
@NiagaraAction(name = "importCsv")

public class BSpecialEventImporter extends BComponent {


/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.optimised_buildings_ltd.ScheduleDownloader.BSpecialEventImporter(3802655261)1.0$ @*/
/* Generated Wed Nov 25 12:03:43 GMT 2020 by Slot-o-Matic (c) Tridium, Inc. 2012 */

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
// Action "importCsv"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code importCsv} action.
   * @see #importCsv()
   */
  public static final Action importCsv = newAction(0, null);
  
  /**
   * Invoke the {@code importCsv} action.
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

public void doImportCsv(){
  this.setReport("");
  String csv = this.getCsv();
  String[] lines = csv.split("\n");
  for(int i = 1; i < lines.length; i++){
    String[] data = lines[i].split(",");
    String storeNumber = String.format("%03d", Integer.parseInt(data[0]));
    BTrendSystem store = this.resolveStore(storeNumber);
    if(store == null){
      this.reportError(storeNumber,lastError);
      continue;
    }
    BBooleanSchedule masterSchedule = ((BBooleanSchedule)store.get("masterSchedule"));
    if(masterSchedule == null){
      this.reportError(storeNumber,"BBooleanSchedule not found");
      continue;
    }
    BCompositeSchedule hiddenSchedule = masterSchedule.getSchedule();
    BCompositeSchedule specialEvents = (BCompositeSchedule) hiddenSchedule.get("specialEvents");
    specialEvents.removeAll();
    try{
      this.addEvent(masterSchedule, BDate.make("2020-12-18"), this.timeFormatter(data[6], false), this.timeFormatter(data[7], true), true);
      this.addEvent(masterSchedule, BDate.make("2020-12-19"), this.timeFormatter(data[9], false), this.timeFormatter(data[10], true), true);
      this.addEvent(masterSchedule, BDate.make("2020-12-20"), this.timeFormatter(data[12], false), this.timeFormatter(data[13], true), true);
      this.addEvent(masterSchedule, BDate.make("2020-12-21"), this.timeFormatter(data[15], false), this.timeFormatter(data[16], true), true);
      this.addEvent(masterSchedule, BDate.make("2020-12-22"), this.timeFormatter(data[18], false), this.timeFormatter(data[19], true), true);
      this.addEvent(masterSchedule, BDate.make("2020-12-23"), this.timeFormatter(data[21], false), this.timeFormatter(data[22], true), true);
      this.addEvent(masterSchedule, BDate.make("2020-12-24"), this.timeFormatter(data[24], false), this.timeFormatter(data[25], true), true);
      this.addEvent(masterSchedule, BDate.make("2020-12-25"), this.timeFormatter(data[26], false), this.timeFormatter(data[26], true), false);
      this.addEvent(masterSchedule, BDate.make("2020-12-26"), this.timeFormatter(data[28], false), this.timeFormatter(data[29], true), true);
      this.addEvent(masterSchedule, BDate.make("2020-12-27"), this.timeFormatter(data[31], false), this.timeFormatter(data[32], true), true);
      this.addEvent(masterSchedule, BDate.make("2020-12-28"), this.timeFormatter(data[34], false), this.timeFormatter(data[35], true), true);
      this.addEvent(masterSchedule, BDate.make("2020-12-29"), this.timeFormatter(data[37], false), this.timeFormatter(data[38], true), true);
      this.addEvent(masterSchedule, BDate.make("2020-12-30"), this.timeFormatter(data[40], false), this.timeFormatter(data[41], true), true);
      this.addEvent(masterSchedule, BDate.make("2020-12-31"), this.timeFormatter(data[43], false), this.timeFormatter(data[44], true), true);
      this.addEvent(masterSchedule, BDate.make("2021-01-01"), this.timeFormatter(data[46], false), this.timeFormatter(data[47], true), true);
      this.reportError(storeNumber, "success");
    } catch (Exception e){
      this.reportError(storeNumber, e.getMessage());
    }

  }

}

public BTime timeFormatter(String timeString, boolean end) throws Exception {
  if(timeString.contains("Closed")){
    return BTime.make(0,0,0);
  }
  String[] hoursAndMinutes = timeString.split(":");
  return BTime.make(Integer.parseInt(hoursAndMinutes[0]),Integer.parseInt(hoursAndMinutes[1]),0);
}

public void addEvent(BBooleanSchedule masterSchedule, BDate date, BTime startTime, BTime endTime, boolean eventType) throws Exception{
  BCompositeSchedule hiddenSchedule = masterSchedule.getSchedule();
  BCompositeSchedule specialEvents = (BCompositeSchedule) hiddenSchedule.get("specialEvents");

  //Add event
  String eventName = "ChristmasEvent" + date.toString().replaceAll("-","");
  specialEvents.add(eventName, new BDailySchedule());
  BDailySchedule event = (BDailySchedule) specialEvents.get(eventName);
  BDateSchedule days = (BDateSchedule)event.getDays();
  days.setDay(date.getDay());
  days.setMonth(date.getMonth());
  days.setYear(date.getYear());

  if(eventType) {
    if(!startTime.toString().contains("12:00 AM")) {
      event.getDay().add(BTime.make(0,0,0), startTime, new BStatusBoolean(false));
    }
    event.getDay().add(startTime, endTime, new BStatusBoolean(true));
    if(!endTime.toString().contains("12:00 AM")) {
      event.getDay().add(endTime, BTime.make(0,0,0), new BStatusBoolean(false));
    }
  } else {
    event.getDay().add(BTime.make(0,0,0), BTime.make(0,0,0), new BStatusBoolean(false));
  }
}



public void reportError(String storeNumber, String error){
  //TODO Add to report
  this.setReport(this.getReport() + storeNumber + ", " + error + "\n");
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

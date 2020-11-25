package com.optimised_buildings_ltd.ScheduleDownloader;

import javax.baja.collection.BITable;
import javax.baja.naming.BOrd;
import javax.baja.nre.annotations.Facet;
import javax.baja.nre.annotations.NiagaraAction;
import javax.baja.nre.annotations.NiagaraProperty;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.schedule.*;
import javax.baja.sys.*;
@NiagaraType
@NiagaraAction(name = "uploadToMaster")
@NiagaraProperty(name = "report", type = "BString", defaultValue = "BString.DEFAULT", facets = @Facet(name = "\"multiLine\"", value = "true"))
@NiagaraProperty(name = "testMode", type = "BBoolean", defaultValue = "BBoolean.TRUE")

public class BScheduleUploader extends BComponent{


    public void doUploadToMaster(){
        String bql = "station:|slot:/Drivers/TREND|bql:select * from schedule:WeekSchedule where name = 'week' and parent.parent.displayName like '*IMPORT*'";
        BITable result = (BITable) BOrd.make(bql).resolve().get();
        Cursor c = result.cursor();
        while(c.next()){
            try {
                BWeekSchedule importedSchedule = (BWeekSchedule) c.get();
                BComponent store = (BComponent) importedSchedule.getParent().getParent().getParent().getParent().getParent().getParent();
                BWeekSchedule masterWeek = (BWeekSchedule) ((BBooleanSchedule) store.get("masterSchedule")).getSchedule().get("week");

                ((BTimeSchedule) masterWeek.getSunday().getDay().get("time")).setStart(((BTimeSchedule) importedSchedule.getSunday().getDay().get("time1")).getStart());
                ((BTimeSchedule) masterWeek.getSunday().getDay().get("time")).setFinish(((BTimeSchedule) importedSchedule.getSunday().getDay().get("time1")).getFinish());

                ((BTimeSchedule) masterWeek.getMonday().getDay().get("time")).setStart(((BTimeSchedule) importedSchedule.getMonday().getDay().get("time1")).getStart());
                ((BTimeSchedule) masterWeek.getMonday().getDay().get("time")).setFinish(((BTimeSchedule) importedSchedule.getMonday().getDay().get("time1")).getFinish());

                ((BTimeSchedule) masterWeek.getTuesday().getDay().get("time")).setStart(((BTimeSchedule) importedSchedule.getTuesday().getDay().get("time1")).getStart());
                ((BTimeSchedule) masterWeek.getTuesday().getDay().get("time")).setFinish(((BTimeSchedule) importedSchedule.getTuesday().getDay().get("time1")).getFinish());

                ((BTimeSchedule) masterWeek.getWednesday().getDay().get("time")).setStart(((BTimeSchedule) importedSchedule.getWednesday().getDay().get("time1")).getStart());
                ((BTimeSchedule) masterWeek.getWednesday().getDay().get("time")).setFinish(((BTimeSchedule) importedSchedule.getWednesday().getDay().get("time1")).getFinish());

                ((BTimeSchedule) masterWeek.getThursday().getDay().get("time")).setStart(((BTimeSchedule) importedSchedule.getThursday().getDay().get("time1")).getStart());
                ((BTimeSchedule) masterWeek.getThursday().getDay().get("time")).setFinish(((BTimeSchedule) importedSchedule.getThursday().getDay().get("time1")).getFinish());

                ((BTimeSchedule) masterWeek.getFriday().getDay().get("time")).setStart(((BTimeSchedule) importedSchedule.getFriday().getDay().get("time1")).getStart());
                ((BTimeSchedule) masterWeek.getFriday().getDay().get("time")).setFinish(((BTimeSchedule) importedSchedule.getFriday().getDay().get("time1")).getFinish());

                ((BTimeSchedule) masterWeek.getSaturday().getDay().get("time")).setStart(((BTimeSchedule) importedSchedule.getSaturday().getDay().get("time1")).getStart());
                ((BTimeSchedule) masterWeek.getSaturday().getDay().get("time")).setFinish(((BTimeSchedule) importedSchedule.getSaturday().getDay().get("time1")).getFinish());
            } catch (Exception e){
                this.setReport(this.getReport() + e.toString() + "\n");
            }
            if(this.getTestMode()) {
                break;
            }
        }
    }

/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.optimised_buildings_ltd.ScheduleDownloader.BScheduleUploader(1064981371)1.0$ @*/
/* Generated Wed Nov 25 12:33:44 GMT 2020 by Slot-o-Matic (c) Tridium, Inc. 2012 */

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
// Property "testMode"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code testMode} property.
   * @see #getTestMode
   * @see #setTestMode
   */
  public static final Property testMode = newProperty(0, ((BBoolean)(BBoolean.TRUE)).getBoolean(), null);
  
  /**
   * Get the {@code testMode} property.
   * @see #testMode
   */
  public boolean getTestMode() { return getBoolean(testMode); }
  
  /**
   * Set the {@code testMode} property.
   * @see #testMode
   */
  public void setTestMode(boolean v) { setBoolean(testMode, v, null); }

////////////////////////////////////////////////////////////////
// Action "uploadToMaster"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code uploadToMaster} action.
   * @see #uploadToMaster()
   */
  public static final Action uploadToMaster = newAction(0, null);
  
  /**
   * Invoke the {@code uploadToMaster} action.
   * @see #uploadToMaster
   */
  public void uploadToMaster() { invoke(uploadToMaster, null, null); }

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BScheduleUploader.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/

}

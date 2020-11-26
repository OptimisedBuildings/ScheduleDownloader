package com.optimised_buildings_ltd.ScheduleDownloader;

import com.honeywell.trend.driver.comm.linklayer.BTcpVirtualCnc2;
import com.honeywell.trend.driver.core.BTrendDevice;
import com.honeywell.trend.driver.core.BTrendSystem;
import com.honeywell.trend.driver.core.schedule.BTrendScheduleExport;
import com.honeywell.trend.driver.core.schedule.BTrendScheduleImport;

import javax.baja.collection.BITable;
import javax.baja.naming.BOrd;
import javax.baja.nre.annotations.Facet;
import javax.baja.nre.annotations.NiagaraAction;
import javax.baja.nre.annotations.NiagaraProperty;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.schedule.BBooleanSchedule;
import javax.baja.schedule.BTimeSchedule;
import javax.baja.sys.*;

@NiagaraType
@NiagaraAction(name = "softDownload")
@NiagaraProperty(name = "report", type = "BString", defaultValue = "BString.DEFAULT", facets = @Facet(name = "\"multiLine\"", value = "true"))
@NiagaraAction(name = "pause")
@NiagaraAction(name = "resume")
@NiagaraAction(name = "halt")
@NiagaraProperty(name = "status", type = "BString", defaultValue = "BString.DEFAULT", flags = Flags.READONLY)

public class BScheduleDownloader extends BComponent {

public void doSoftDownload() throws Exception{
  running = true;

  String storeBql = "station:|slot:/Drivers/TREND|bql:select * from TrendN4:TrendSystem";
  BITable storeTable = (BITable) BOrd.make(storeBql).resolve().get();
  Cursor storeCursor = storeTable.cursor();
  while(storeCursor.next()){
    while(pauseFlag && !haltFlag){
      this.setStatus("Paused.");
      Thread.sleep(500);
      this.setStatus("Paused..");
      Thread.sleep(500);
      this.setStatus("Paused..");
      Thread.sleep(500);
    }
    if(haltFlag){
      running = false;
      return;
    }


      BTrendSystem store = (BTrendSystem) storeCursor.get();
    try {
      this.setStatus("Running. Current Store: " + store.getDisplayName(null));
      if (store.getStatus().toString().contains("down")) {
        String error = ((BTcpVirtualCnc2)store.getVCncConnectionManager().get("PrimarySetupVCnc")).getLastDisconnectCause();
        this.addToReport(store.getDisplayName(null), "Lan: " + error);
        continue;
      }
      BTrendScheduleImport trendImport = (BTrendScheduleImport) this.resolve(true, store);
      BTrendScheduleExport trendExport = (BTrendScheduleExport) this.resolve(false, store);
      BTrendDevice controller = (BTrendDevice) trendExport.getParent().getParent();
      if(controller.getStatus().toString().contains("down")){
        String error = controller.getHealth().getLastFailCause();
        this.addToReport(store.getDisplayName(null), "Controller: " + error);
        continue;
      }

      //EXPORT
      trendExport.doExecute();
      long startTime = System.currentTimeMillis();
      while(true){
        if(System.currentTimeMillis() > startTime + 120000){
          this.addToReport(store.getDisplayName(null), "Export timeout");
          throw new InterruptedException();
        } else if (trendExport.getState().toString().contains("Idle")){
          if(trendExport.getLastSuccess().isAfter(trendExport.getLastFailure())){
            break;
          } else {
            this.addToReport(store.getDisplayName(null),trendExport.getFaultCause());
            throw new InterruptedException();
          }
        }
      }

      //IMPORT
      trendImport.doExecute();
      startTime = System.currentTimeMillis();
      while(true){
        if(System.currentTimeMillis() > startTime + 120000){
          this.addToReport(store.getDisplayName(null), "Import timeout");
          throw new InterruptedException();
        } else if (trendImport.getState().toString().contains("Idle")){
          if(trendImport.getLastSuccess().isAfter(trendImport.getLastFailure())){
            break;
          } else {
            this.addToReport(store.getDisplayName(null),trendImport.getFaultCause());
            throw new InterruptedException();
          }
        }
      }

      //check against each other.
      if(this.checkScheduleMatch(controller, ((BBooleanSchedule)store.get("masterSchedule")),((BBooleanSchedule)trendImport.getParent()))){
        this.addToReport(store.getDisplayName(null), "Success");
      } else {
        this.addToReport(store.getDisplayName(null), "Schedules do not match");
      }


    } catch (InterruptedException e){
    } catch (Exception e){
      this.addToReport(store.getDisplayName(null), e.toString());
    }
  }
}

public boolean checkScheduleMatch(BTrendDevice controller, BBooleanSchedule masterSchedule, BBooleanSchedule controllerSchedule){
  String versionString = controller.getConfiguration().getIdentification().getVersionString();
  if(versionString.contains("IQ2")){
    String encodedControllerSchedule = this.scheduleEncoder(((BComponent)controllerSchedule.getSchedule().get("week")));
    String encodedMasterSchedule = this.scheduleEncoder(((BComponent)masterSchedule.getSchedule().get("week")));
    return encodedControllerSchedule.equals(encodedMasterSchedule);
  } else if (versionString.contains("IQ3") || versionString.contains("IQ4")){
    String encodedControllerSchedule = this.scheduleEncoder(controllerSchedule.getSchedule());
    String encodedMasterSchedule = this.scheduleEncoder(masterSchedule.getSchedule());
    return encodedControllerSchedule.equals(encodedMasterSchedule);
  } else {
    return false;
  }
}

public String scheduleEncoder(BComponent schedule){
  String bql = "station:|" + schedule.getSlotPath() + "|bql:select * from schedule:TimeSchedule where effectiveValue.boolean = true";
  BITable result = (BITable)BOrd.make(bql).resolve().get();
  Cursor c = result.cursor();
  StringBuilder encodedString = new StringBuilder();
  while(c.next()){
    BTimeSchedule timeSchedule = (BTimeSchedule)c.get();
    encodedString.append(timeSchedule.getParent().getParent().getName());
    encodedString.append(timeSchedule.getStart());
    encodedString.append(timeSchedule.getFinish());
  }
  return encodedString.toString().replaceAll("11:59 PM","12:00 AM");
}


public void addToReport(String storeName, String status){
  this.setReport(this.getReport() + storeName + ", " + status + "\n");
}

int schedulesFound;
//type true = import, false = export
public BComponent resolve(boolean type, BComponent store){
  if(type){
    BTrendScheduleImport trendImport = null;
    schedulesFound = 0;
    String bql = "station:|" + store.getSlotPath() + "|bql:select * from TrendN4:TrendScheduleImport where parent.displayName like '*IMPORT*'";
    BITable result = (BITable)BOrd.make(bql).resolve().get();
    Cursor c = result.cursor();
    while(c.next()){
      schedulesFound++;
      trendImport = (BTrendScheduleImport)c.get();
    }
    if(schedulesFound != 1){
      return null;
    } else {
      return trendImport;
    }
  } else {
    BTrendScheduleExport trendExport = null;
    schedulesFound = 0;
    String bql = "station:|" + store.getSlotPath() + "|bql:select * from TrendN4:TrendScheduleExport where parent.displayName like '*EXPORT*'";
    BITable result = (BITable)BOrd.make(bql).resolve().get();
    Cursor c = result.cursor();
    while(c.next()) {
      schedulesFound++;
      trendExport = (BTrendScheduleExport) c.get();
    }
    if(schedulesFound != 1){
      return null;
    } else {
      return trendExport;
    }
  }
}

boolean running;
boolean pauseFlag;
boolean haltFlag;

public void onPause(){
  pauseFlag = true;
}

public void onResume(){
  pauseFlag = false;
}

public void onHalt(){
  if(running){
    haltFlag = true;
  }
}


/*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
/*@ $com.optimised_buildings_ltd.ScheduleDownloader.BScheduleDownloader(3579196511)1.0$ @*/
/* Generated Wed Nov 25 16:01:01 GMT 2020 by Slot-o-Matic (c) Tridium, Inc. 2012 */

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
// Property "status"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code status} property.
   * @see #getStatus
   * @see #setStatus
   */
  public static final Property status = newProperty(Flags.READONLY, BString.DEFAULT, null);
  
  /**
   * Get the {@code status} property.
   * @see #status
   */
  public String getStatus() { return getString(status); }
  
  /**
   * Set the {@code status} property.
   * @see #status
   */
  public void setStatus(String v) { setString(status, v, null); }

////////////////////////////////////////////////////////////////
// Action "softDownload"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code softDownload} action.
   * @see #softDownload()
   */
  public static final Action softDownload = newAction(0, null);
  
  /**
   * Invoke the {@code softDownload} action.
   * @see #softDownload
   */
  public void softDownload() { invoke(softDownload, null, null); }

////////////////////////////////////////////////////////////////
// Action "pause"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code pause} action.
   * @see #pause()
   */
  public static final Action pause = newAction(0, null);
  
  /**
   * Invoke the {@code pause} action.
   * @see #pause
   */
  public void pause() { invoke(pause, null, null); }

////////////////////////////////////////////////////////////////
// Action "resume"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code resume} action.
   * @see #resume()
   */
  public static final Action resume = newAction(0, null);
  
  /**
   * Invoke the {@code resume} action.
   * @see #resume
   */
  public void resume() { invoke(resume, null, null); }

////////////////////////////////////////////////////////////////
// Action "halt"
////////////////////////////////////////////////////////////////
  
  /**
   * Slot for the {@code halt} action.
   * @see #halt()
   */
  public static final Action halt = newAction(0, null);
  
  /**
   * Invoke the {@code halt} action.
   * @see #halt
   */
  public void halt() { invoke(halt, null, null); }

////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
  
  @Override
  public Type getType() { return TYPE; }
  public static final Type TYPE = Sys.loadType(BScheduleDownloader.class);

/*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/
}

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
import javax.baja.sys.*;
import java.util.Spliterator;

@NiagaraType
@NiagaraProperty(name = "csv", type = "BString", defaultValue = "BString.DEFAULT", facets = @Facet(name = "\"multiLine\"", value = "true"))
@NiagaraProperty(name = "report", type = "BString", defaultValue = "BString.DEFAULT", facets = @Facet(name = "\"multiLine\"", value = "true"))
@NiagaraAction(name = "import")

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
   * @see #import()
   */
  public static final Action import = newAction(0, null);
  
  /**
   * Invoke the {@code import} action.
   * @see #import
   */
  public void import() { invoke(import, null, null); }

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
      //TODO add to report
      continue;
    }
  }

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

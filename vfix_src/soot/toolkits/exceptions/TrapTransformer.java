package soot.toolkits.exceptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import soot.BodyTransformer;
import soot.Unit;
import soot.Value;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.toolkits.graph.UnitGraph;
import soot.util.HashMultiMap;
import soot.util.MultiMap;

public abstract class TrapTransformer extends BodyTransformer {
   public Set<Unit> getUnitsWithMonitor(UnitGraph ug) {
      MultiMap<Unit, Value> unitMonitors = new HashMultiMap();
      List<Unit> workList = new ArrayList();
      Set<Unit> doneSet = new HashSet();
      Iterator var5 = ug.getHeads().iterator();

      while(var5.hasNext()) {
         Unit head = (Unit)var5.next();
         workList.add(head);
      }

      while(true) {
         Unit curUnit;
         boolean hasChanged;
         do {
            if (workList.isEmpty()) {
               return unitMonitors.keySet();
            }

            curUnit = (Unit)workList.remove(0);
            hasChanged = false;
            Value exitValue = null;
            if (curUnit instanceof EnterMonitorStmt) {
               EnterMonitorStmt ems = (EnterMonitorStmt)curUnit;
               hasChanged = unitMonitors.put(curUnit, ems.getOp());
            } else if (curUnit instanceof ExitMonitorStmt) {
               ExitMonitorStmt ems = (ExitMonitorStmt)curUnit;
               exitValue = ems.getOp();
            }

            Iterator var15 = ug.getPredsOf(curUnit).iterator();

            while(var15.hasNext()) {
               Unit pred = (Unit)var15.next();
               Iterator var10 = unitMonitors.get(pred).iterator();

               while(var10.hasNext()) {
                  Value v = (Value)var10.next();
                  if (v != exitValue && unitMonitors.put(curUnit, v)) {
                     hasChanged = true;
                  }
               }
            }
         } while(!doneSet.add(curUnit) && !hasChanged);

         workList.addAll(ug.getSuccsOf(curUnit));
      }
   }
}

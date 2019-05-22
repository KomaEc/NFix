package soot.jbco.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.PatchingChain;
import soot.Trap;
import soot.Unit;
import soot.baf.JSRInst;
import soot.baf.TableSwitchInst;
import soot.baf.TargetArgInst;

public class Debugger {
   public static void printBaf(Body b) {
      System.out.println(b.getMethod().getName() + "\n");
      int i = 0;
      Map<Unit, Integer> index = new HashMap();
      Iterator it = b.getUnits().iterator();

      while(it.hasNext()) {
         index.put(it.next(), new Integer(i++));
      }

      it = b.getUnits().iterator();

      while(it.hasNext()) {
         Object o = it.next();
         System.out.println(((Integer)index.get(o)).toString() + " " + o + " " + (o instanceof TargetArgInst ? ((Integer)index.get(((TargetArgInst)o).getTarget())).toString() : ""));
      }

      System.out.println("\n");
   }

   public static void printUnits(Body b, String msg) {
      int i = 0;
      Map<Unit, Integer> numbers = new HashMap();
      PatchingChain<Unit> u = b.getUnits();
      Iterator it = u.snapshotIterator();

      while(it.hasNext()) {
         numbers.put(it.next(), new Integer(i++));
      }

      int jsr = 0;
      System.out.println("\r\r" + b.getMethod().getName() + "  " + msg);
      Iterator udit = u.snapshotIterator();

      while(udit.hasNext()) {
         Unit unit = (Unit)udit.next();
         Integer numb = (Integer)numbers.get(unit);
         if (numb == 149) {
            System.out.println("hi");
         }

         if (unit instanceof TargetArgInst) {
            if (unit instanceof JSRInst) {
               ++jsr;
            }

            TargetArgInst ti = (TargetArgInst)unit;
            if (ti.getTarget() == null) {
               System.out.println(unit + " null null null null null null null null null");
            } else {
               System.out.println(((Integer)numbers.get(unit)).toString() + " " + unit + "   #" + ((Integer)numbers.get(ti.getTarget())).toString());
            }
         } else if (unit instanceof TableSwitchInst) {
            TableSwitchInst tswi = (TableSwitchInst)unit;
            System.out.println(((Integer)numbers.get(unit)).toString() + " SWITCH:");
            System.out.println("\tdefault: " + tswi.getDefaultTarget() + "  " + ((Integer)numbers.get(tswi.getDefaultTarget())).toString());
            int index = 0;

            for(int x = tswi.getLowIndex(); x <= tswi.getHighIndex(); ++x) {
               System.out.println("\t " + x + ": " + tswi.getTarget(index) + "  " + ((Integer)numbers.get(tswi.getTarget(index++))).toString());
            }
         } else {
            System.out.println(numb.toString() + " " + unit);
         }
      }

      Iterator tit = b.getTraps().iterator();

      while(tit.hasNext()) {
         Trap t = (Trap)tit.next();
         System.out.println(numbers.get(t.getBeginUnit()) + " " + t.getBeginUnit() + " to " + numbers.get(t.getEndUnit()) + " " + t.getEndUnit() + "  at " + numbers.get(t.getHandlerUnit()) + " " + t.getHandlerUnit());
      }

      if (jsr > 0) {
         System.out.println("\r\tJSR Instructions: " + jsr);
      }

   }

   public static void printUnits(PatchingChain<Unit> u, String msg) {
      int i = 0;
      HashMap<Unit, Integer> numbers = new HashMap();
      Iterator it = u.snapshotIterator();

      while(it.hasNext()) {
         numbers.put(it.next(), new Integer(i++));
      }

      System.out.println("\r\r***********  " + msg);
      Iterator udit = u.snapshotIterator();

      while(true) {
         while(udit.hasNext()) {
            Unit unit = (Unit)udit.next();
            Integer numb = (Integer)numbers.get(unit);
            if (numb == 149) {
               System.out.println("hi");
            }

            if (unit instanceof TargetArgInst) {
               TargetArgInst ti = (TargetArgInst)unit;
               if (ti.getTarget() == null) {
                  System.out.println(unit + " null null null null null null null null null");
               } else {
                  System.out.println(((Integer)numbers.get(unit)).toString() + " " + unit + "   #" + ((Integer)numbers.get(ti.getTarget())).toString());
               }
            } else if (unit instanceof TableSwitchInst) {
               TableSwitchInst tswi = (TableSwitchInst)unit;
               System.out.println(((Integer)numbers.get(unit)).toString() + " SWITCH:");
               System.out.println("\tdefault: " + tswi.getDefaultTarget() + "  " + ((Integer)numbers.get(tswi.getDefaultTarget())).toString());
               int index = 0;

               for(int x = tswi.getLowIndex(); x <= tswi.getHighIndex(); ++x) {
                  System.out.println("\t " + x + ": " + tswi.getTarget(index) + "  " + ((Integer)numbers.get(tswi.getTarget(index++))).toString());
               }
            } else {
               System.out.println(numb.toString() + " " + unit);
            }
         }

         return;
      }
   }
}

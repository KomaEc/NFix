package soot.toolkits.scalar;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.options.Options;
import soot.toolkits.exceptions.PedanticThrowAnalysis;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.util.ArraySet;

public class FastColorer {
   public static void unsplitAssignColorsToLocals(Body unitBody, Map<Local, Object> localToGroup, Map<Local, Integer> localToColor, Map<Object, Integer> groupToColorCount) {
      ExceptionalUnitGraph unitGraph = new ExceptionalUnitGraph(unitBody, PedanticThrowAnalysis.v(), Options.v().omit_excepting_unit_edges());
      LiveLocals liveLocals = new SimpleLiveLocals(unitGraph);
      FastColorer.UnitInterferenceGraph intGraph = new FastColorer.UnitInterferenceGraph(unitBody, localToGroup, liveLocals, unitGraph);
      Map<Local, String> localToOriginalName = new HashMap();
      Iterator var8 = intGraph.getLocals().iterator();

      while(var8.hasNext()) {
         Local local = (Local)var8.next();
         int signIndex = local.getName().indexOf("#");
         if (signIndex != -1) {
            localToOriginalName.put(local, local.getName().substring(0, signIndex));
         } else {
            localToOriginalName.put(local, local.getName());
         }
      }

      Map<StringGroupPair, List<Integer>> originalNameAndGroupToColors = new HashMap();
      int[] freeColors = new int[10];
      Iterator var22 = intGraph.getLocals().iterator();

      while(true) {
         Local local;
         do {
            if (!var22.hasNext()) {
               return;
            }

            local = (Local)var22.next();
         } while(localToColor.containsKey(local));

         Object group = localToGroup.get(local);
         int colorCount = (Integer)groupToColorCount.get(group);
         if (freeColors.length < colorCount) {
            freeColors = new int[Math.max(freeColors.length * 2, colorCount)];
         }

         for(int i = 0; i < colorCount; ++i) {
            freeColors[i] = 1;
         }

         Local[] interferences = intGraph.getInterferencesOf(local);
         int assignedColor;
         if (interferences != null) {
            Local[] var15 = interferences;
            int var16 = interferences.length;

            for(assignedColor = 0; assignedColor < var16; ++assignedColor) {
               Local element = var15[assignedColor];
               if (localToColor.containsKey(element)) {
                  int usedColor = (Integer)localToColor.get(element);
                  freeColors[usedColor] = 0;
               }
            }
         }

         String originalName = (String)localToOriginalName.get(local);
         List<Integer> originalNameColors = (List)originalNameAndGroupToColors.get(new StringGroupPair(originalName, group));
         if (originalNameColors == null) {
            originalNameColors = new ArrayList();
            originalNameAndGroupToColors.put(new StringGroupPair(originalName, group), originalNameColors);
         }

         boolean found = false;
         assignedColor = 0;
         Iterator colorIt = ((List)originalNameColors).iterator();

         while(colorIt.hasNext()) {
            Integer color = (Integer)colorIt.next();
            if (freeColors[color] == 1) {
               found = true;
               assignedColor = color;
            }
         }

         if (!found) {
            assignedColor = colorCount++;
            groupToColorCount.put(group, new Integer(colorCount));
            ((List)originalNameColors).add(new Integer(assignedColor));
         }

         localToColor.put(local, new Integer(assignedColor));
      }
   }

   public static void assignColorsToLocals(Body unitBody, Map<Local, Object> localToGroup, Map<Local, Integer> localToColor, Map<Object, Integer> groupToColorCount) {
      ExceptionalUnitGraph unitGraph = new ExceptionalUnitGraph(unitBody, PedanticThrowAnalysis.v(), Options.v().omit_excepting_unit_edges());
      LiveLocals liveLocals = new SimpleLiveLocals(unitGraph);
      final FastColorer.UnitInterferenceGraph intGraph = new FastColorer.UnitInterferenceGraph(unitBody, localToGroup, liveLocals, unitGraph);
      List<Local> sortedLocals = new ArrayList(intGraph.getLocals());
      Collections.sort(sortedLocals, new Comparator<Local>() {
         public int compare(Local o1, Local o2) {
            int interferences1 = intGraph.getInterferenceCount(o1);
            int interferences2 = intGraph.getInterferenceCount(o2);
            return interferences2 - interferences1;
         }
      });
      Iterator var8 = sortedLocals.iterator();

      while(true) {
         Local local;
         do {
            if (!var8.hasNext()) {
               return;
            }

            local = (Local)var8.next();
         } while(localToColor.containsKey(local));

         Object group = localToGroup.get(local);
         int colorCount = (Integer)groupToColorCount.get(group);
         BitSet blockedColors = new BitSet(colorCount);
         Local[] interferences = intGraph.getInterferencesOf(local);
         if (interferences != null) {
            Local[] var14 = interferences;
            int var15 = interferences.length;

            for(int var16 = 0; var16 < var15; ++var16) {
               Local element = var14[var16];
               if (localToColor.containsKey(element)) {
                  int usedColor = (Integer)localToColor.get(element);
                  blockedColors.set(usedColor);
               }
            }
         }

         int assignedColor = -1;

         for(int i = 0; i < colorCount; ++i) {
            if (!blockedColors.get(i)) {
               assignedColor = i;
               break;
            }
         }

         if (assignedColor < 0) {
            assignedColor = colorCount++;
            groupToColorCount.put(group, new Integer(colorCount));
         }

         localToColor.put(local, new Integer(assignedColor));
      }
   }

   private static class UnitInterferenceGraph {
      Map<Local, Set<Local>> localToLocals;
      List<Local> locals = new ArrayList();

      public List<Local> getLocals() {
         return this.locals;
      }

      public UnitInterferenceGraph(Body body, Map<Local, Object> localToGroup, LiveLocals liveLocals, ExceptionalUnitGraph unitGraph) {
         this.locals.addAll(body.getLocals());
         this.localToLocals = new HashMap(body.getLocalCount() * 2 + 1, 0.7F);
         Iterator var5 = body.getUnits().iterator();

         while(true) {
            HashSet liveLocalsAtUnit;
            Value defValue;
            do {
               Unit unit;
               List defBoxes;
               do {
                  if (!var5.hasNext()) {
                     return;
                  }

                  unit = (Unit)var5.next();
                  defBoxes = unit.getDefBoxes();
               } while(defBoxes.isEmpty());

               if (defBoxes.size() != 1) {
                  throw new RuntimeException("invalid number of def boxes");
               }

               liveLocalsAtUnit = new HashSet();
               Iterator var9 = unitGraph.getSuccsOf(unit).iterator();

               while(var9.hasNext()) {
                  Unit succ = (Unit)var9.next();
                  List<Local> beforeSucc = liveLocals.getLiveLocalsBefore(succ);
                  liveLocalsAtUnit.addAll(beforeSucc);
               }

               defValue = ((ValueBox)defBoxes.get(0)).getValue();
            } while(!(defValue instanceof Local));

            Local defLocal = (Local)defValue;
            Iterator var15 = liveLocalsAtUnit.iterator();

            while(var15.hasNext()) {
               Local otherLocal = (Local)var15.next();
               if (localToGroup.get(otherLocal).equals(localToGroup.get(defLocal))) {
                  this.setInterference(defLocal, otherLocal);
               }
            }
         }
      }

      public void setInterference(Local l1, Local l2) {
         Set<Local> locals = (Set)this.localToLocals.get(l1);
         if (locals == null) {
            locals = new ArraySet();
            this.localToLocals.put(l1, locals);
         }

         ((Set)locals).add(l2);
         locals = (Set)this.localToLocals.get(l2);
         if (locals == null) {
            locals = new ArraySet();
            this.localToLocals.put(l2, locals);
         }

         ((Set)locals).add(l1);
      }

      int getInterferenceCount(Local l) {
         Set<Local> localSet = (Set)this.localToLocals.get(l);
         return localSet == null ? 0 : localSet.size();
      }

      Local[] getInterferencesOf(Local l) {
         Set<Local> localSet = (Set)this.localToLocals.get(l);
         return localSet == null ? null : (Local[])localSet.toArray(new Local[localSet.size()]);
      }
   }
}

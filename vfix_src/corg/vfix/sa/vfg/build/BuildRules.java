package corg.vfix.sa.vfg.build;

import corg.vfix.sa.vfg.VFGNode;
import java.util.ArrayList;
import java.util.Iterator;
import soot.Value;

public class BuildRules {
   public static boolean satisfyRule(VFGNode node1, VFGNode node2) {
      return intraPointer(node1, node2) || intraObject(node1, node2) || interPointer(node1, node2) || interObject(node1, node2);
   }

   public static ArrayList<Value> getValueIntersection(ArrayList<Value> list1, ArrayList<Value> list2) {
      ArrayList<Value> intersection = new ArrayList();
      Iterator var4 = list1.iterator();

      while(var4.hasNext()) {
         Value v1 = (Value)var4.next();
         Iterator var6 = list2.iterator();

         while(var6.hasNext()) {
            Value v2 = (Value)var6.next();
            if (v1.equals(v2) && !intersection.contains(v1)) {
               intersection.add(v1);
            }
         }
      }

      return intersection;
   }

   private static boolean intraPointer(VFGNode node1, VFGNode node2) {
      ArrayList<Value> set1 = node1.getSrcs();
      ArrayList<Value> set2 = node2.getTrans();
      set2.addAll(node2.getSinks());
      return hasIntersection(set1, set2);
   }

   private static boolean intraObject(VFGNode node1, VFGNode node2) {
      return false;
   }

   private static boolean interPointer(VFGNode node1, VFGNode node2) {
      return false;
   }

   private static boolean interObject(VFGNode node1, VFGNode node2) {
      return false;
   }

   private static boolean hasIntersection(ArrayList<Value> list1, ArrayList<Value> list2) {
      Iterator var3 = list1.iterator();

      while(var3.hasNext()) {
         Value v1 = (Value)var3.next();
         Iterator var5 = list2.iterator();

         while(var5.hasNext()) {
            Value v2 = (Value)var5.next();
            if (v1.equals(v2)) {
               return true;
            }
         }
      }

      return false;
   }
}

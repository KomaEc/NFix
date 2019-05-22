package soot.jimple.toolkits.thread.mhp;

import java.util.Iterator;
import soot.toolkits.scalar.ArraySparseSet;

public class MonitorSet extends ArraySparseSet {
   MonitorSet() {
   }

   public Object getMonitorDepth(String objName) {
      Iterator it = this.iterator();

      while(it.hasNext()) {
         Object obj = it.next();
         if (obj instanceof MonitorDepth) {
            MonitorDepth md = (MonitorDepth)obj;
            if (md.getObjName().equals(objName)) {
               return md;
            }
         }
      }

      return null;
   }

   public MonitorSet clone() {
      MonitorSet newSet = new MonitorSet();
      newSet.union(this);
      return newSet;
   }

   public void union(MonitorSet other) {
   }

   public void intersection(MonitorSet other, MonitorSet dest) {
      if (other.contains("&")) {
         this.copy(dest);
      } else if (this.contains("&")) {
         other.copy(dest);
      } else {
         Iterator it = this.iterator();

         while(it.hasNext()) {
            Object o = it.next();
            if (o instanceof MonitorDepth) {
               MonitorDepth md = (MonitorDepth)o;
               Object obj = dest.getMonitorDepth(md.getObjName());
               if (obj != null) {
                  if (md.getDepth() != ((MonitorDepth)obj).getDepth()) {
                     throw new RuntimeException("stmt inside different monitor depth !");
                  }

                  dest.add(obj);
               }
            }
         }
      }

   }

   public void test() {
      System.out.println("====MonitorSet===");
      Iterator it = this.iterator();

      while(it.hasNext()) {
         Object obj = it.next();
         if (obj instanceof MonitorDepth) {
            MonitorDepth md = (MonitorDepth)obj;
            System.out.println("obj: " + md.getObjName());
            System.out.println("depth: " + md.getDepth());
         } else {
            System.out.println(obj);
         }
      }

      System.out.println("====MonitorSet end====");
   }
}

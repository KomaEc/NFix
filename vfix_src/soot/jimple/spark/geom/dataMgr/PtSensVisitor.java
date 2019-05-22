package soot.jimple.spark.geom.dataMgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import soot.PointsToSet;
import soot.Scene;
import soot.jimple.spark.geom.dataRep.ContextVar;
import soot.jimple.spark.geom.geomPA.GeomPointsTo;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.PointsToSetInternal;

public abstract class PtSensVisitor<VarType extends ContextVar> {
   protected boolean readyToUse = false;
   protected GeomPointsTo ptsProvider = (GeomPointsTo)Scene.v().getPointsToAnalysis();
   public List<VarType> outList = new ArrayList();
   protected Map<Node, List<VarType>> tableView = new HashMap();

   public void prepare() {
      this.tableView.clear();
      this.readyToUse = false;
   }

   public void finish() {
      if (!this.readyToUse) {
         this.readyToUse = true;
         this.outList.clear();
         if (this.tableView.size() == 0) {
            return;
         }

         Iterator var1 = this.tableView.entrySet().iterator();

         while(var1.hasNext()) {
            Entry<Node, List<VarType>> entry = (Entry)var1.next();
            List<VarType> resList = (List)entry.getValue();
            this.outList.addAll(resList);
         }
      }

   }

   public boolean getUsageState() {
      return this.readyToUse;
   }

   public int numOfDiffObjects() {
      return this.readyToUse ? this.outList.size() : this.tableView.size();
   }

   public boolean hasNonEmptyIntersection(PtSensVisitor<VarType> other) {
      Iterator var2 = this.tableView.entrySet().iterator();

      while(true) {
         List list1;
         List list2;
         do {
            do {
               if (!var2.hasNext()) {
                  return false;
               }

               Entry<Node, List<VarType>> entry = (Entry)var2.next();
               Node var = (Node)entry.getKey();
               list1 = (List)entry.getValue();
               list2 = other.getCSList(var);
            } while(list1.size() == 0);
         } while(list2.size() == 0);

         Iterator var7 = list1.iterator();

         while(var7.hasNext()) {
            VarType cv1 = (ContextVar)var7.next();
            Iterator var9 = list2.iterator();

            while(var9.hasNext()) {
               VarType cv2 = (ContextVar)var9.next();
               if (cv1.intersect(cv2)) {
                  return true;
               }
            }
         }
      }
   }

   public List<VarType> getCSList(Node var) {
      return (List)this.tableView.get(var);
   }

   public PointsToSet toSparkCompatiableResult(VarNode vn) {
      if (!this.readyToUse) {
         this.finish();
      }

      PointsToSetInternal ptset = vn.makeP2Set();
      Iterator var3 = this.outList.iterator();

      while(var3.hasNext()) {
         VarType cv = (ContextVar)var3.next();
         ptset.add(cv.var);
      }

      return ptset;
   }

   public void debugPrint() {
      if (!this.readyToUse) {
         this.finish();
      }

      Iterator var1 = this.outList.iterator();

      while(var1.hasNext()) {
         VarType cv = (ContextVar)var1.next();
         System.out.printf("\t%s\n", cv.toString());
      }

   }

   public abstract boolean visit(Node var1, long var2, long var4, int var6);
}

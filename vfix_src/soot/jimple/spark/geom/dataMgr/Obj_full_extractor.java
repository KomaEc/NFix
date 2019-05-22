package soot.jimple.spark.geom.dataMgr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.jimple.spark.geom.dataRep.IntervalContextVar;
import soot.jimple.spark.pag.Node;

public class Obj_full_extractor extends PtSensVisitor<IntervalContextVar> {
   private List<IntervalContextVar> backupList = new ArrayList();
   private IntervalContextVar tmp_icv = new IntervalContextVar();

   public boolean visit(Node var, long L, long R, int sm_int) {
      if (this.readyToUse) {
         return false;
      } else {
         List<IntervalContextVar> resList = (List)this.tableView.get(var);
         Object resList;
         if (resList == null) {
            resList = new ArrayList();
         } else {
            this.backupList.clear();
            this.tmp_icv.L = L;
            this.tmp_icv.R = R;
            Iterator var8 = resList.iterator();

            while(var8.hasNext()) {
               IntervalContextVar old_cv = (IntervalContextVar)var8.next();
               if (old_cv.contains(this.tmp_icv)) {
                  return false;
               }

               if (!this.tmp_icv.merge(old_cv)) {
                  this.backupList.add(old_cv);
               }
            }

            List<IntervalContextVar> tmpList = this.backupList;
            this.backupList = resList;
            resList = tmpList;
            L = this.tmp_icv.L;
            R = this.tmp_icv.R;
         }

         IntervalContextVar icv = new IntervalContextVar(L, R, var);
         ((List)resList).add(icv);
         this.tableView.put(var, resList);
         return true;
      }
   }
}

package soot.jimple.spark.geom.dataMgr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.jimple.spark.geom.dataRep.SimpleInterval;

public class ContextsCollector {
   public List<SimpleInterval> bars = new ArrayList();
   protected List<SimpleInterval> backupList = new ArrayList();
   protected SimpleInterval tmp_si = new SimpleInterval();
   protected int nBudget = -1;

   public int getBudget() {
      return this.nBudget;
   }

   public int setBudget(int n) {
      int original = this.nBudget;
      this.nBudget = n;
      return original;
   }

   public boolean insert(long L, long R) {
      this.backupList.clear();
      this.tmp_si.L = L;
      this.tmp_si.R = R;
      long minL = L;
      long maxR = R;
      Iterator var9 = this.bars.iterator();

      SimpleInterval old_si;
      while(var9.hasNext()) {
         old_si = (SimpleInterval)var9.next();
         if (old_si.contains(this.tmp_si)) {
            return false;
         }

         if (!this.tmp_si.merge(old_si)) {
            if (old_si.L < minL) {
               minL = old_si.L;
            }

            if (old_si.R > maxR) {
               maxR = old_si.R;
            }

            this.backupList.add(old_si);
         }
      }

      List<SimpleInterval> tmpList = this.backupList;
      this.backupList = this.bars;
      this.bars = tmpList;
      old_si = new SimpleInterval(this.tmp_si);
      this.bars.add(old_si);
      if (this.nBudget != -1 && this.bars.size() > this.nBudget) {
         this.bars.clear();
         old_si.L = minL;
         old_si.R = maxR;
         this.bars.add(old_si);
      }

      return true;
   }

   public void clear() {
      this.bars.clear();
   }
}

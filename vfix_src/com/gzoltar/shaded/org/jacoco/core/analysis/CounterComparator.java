package com.gzoltar.shaded.org.jacoco.core.analysis;

import java.io.Serializable;
import java.util.Comparator;

public class CounterComparator implements Comparator<ICounter>, Serializable {
   private static final long serialVersionUID = -3777463066252746748L;
   public static final CounterComparator TOTALITEMS;
   public static final CounterComparator COVEREDITEMS;
   public static final CounterComparator MISSEDITEMS;
   public static final CounterComparator COVEREDRATIO;
   public static final CounterComparator MISSEDRATIO;
   private final ICounter.CounterValue value;
   private final boolean reverse;

   private CounterComparator(ICounter.CounterValue value) {
      this(value, false);
   }

   private CounterComparator(ICounter.CounterValue value, boolean reverse) {
      this.value = value;
      this.reverse = reverse;
   }

   public int compare(ICounter c1, ICounter c2) {
      int cmp = Double.compare(c1.getValue(this.value), c2.getValue(this.value));
      return this.reverse ? -cmp : cmp;
   }

   public CounterComparator reverse() {
      return new CounterComparator(this.value, !this.reverse);
   }

   public NodeComparator on(ICoverageNode.CounterEntity entity) {
      return new NodeComparator(this, entity);
   }

   static {
      TOTALITEMS = new CounterComparator(ICounter.CounterValue.TOTALCOUNT);
      COVEREDITEMS = new CounterComparator(ICounter.CounterValue.COVEREDCOUNT);
      MISSEDITEMS = new CounterComparator(ICounter.CounterValue.MISSEDCOUNT);
      COVEREDRATIO = new CounterComparator(ICounter.CounterValue.COVEREDRATIO);
      MISSEDRATIO = new CounterComparator(ICounter.CounterValue.MISSEDRATIO);
   }
}

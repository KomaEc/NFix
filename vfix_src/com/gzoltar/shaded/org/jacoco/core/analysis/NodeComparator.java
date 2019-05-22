package com.gzoltar.shaded.org.jacoco.core.analysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NodeComparator implements Comparator<ICoverageNode>, Serializable {
   private static final long serialVersionUID = 8550521643608826519L;
   private final Comparator<ICounter> counterComparator;
   private final ICoverageNode.CounterEntity entity;

   NodeComparator(Comparator<ICounter> counterComparator, ICoverageNode.CounterEntity entity) {
      this.counterComparator = counterComparator;
      this.entity = entity;
   }

   public NodeComparator second(final Comparator<ICoverageNode> second) {
      return new NodeComparator((Comparator)null, (ICoverageNode.CounterEntity)null) {
         private static final long serialVersionUID = -5515272752138802838L;

         public int compare(ICoverageNode o1, ICoverageNode o2) {
            int result = NodeComparator.this.compare(o1, o2);
            return result == 0 ? second.compare(o1, o2) : result;
         }
      };
   }

   public <T extends ICoverageNode> List<T> sort(Collection<T> summaries) {
      List<T> result = new ArrayList(summaries);
      Collections.sort(result, this);
      return result;
   }

   public int compare(ICoverageNode n1, ICoverageNode n2) {
      ICounter c1 = n1.getCounter(this.entity);
      ICounter c2 = n2.getCounter(this.entity);
      return this.counterComparator.compare(c1, c2);
   }
}

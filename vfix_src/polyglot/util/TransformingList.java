package polyglot.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TransformingList extends AbstractList {
   protected final Transformation trans;
   protected final List underlying;

   public TransformingList(Collection underlying, Transformation trans) {
      this((List)(new ArrayList(underlying)), trans);
   }

   public TransformingList(List underlying, Transformation trans) {
      this.underlying = underlying;
      this.trans = trans;
   }

   public int size() {
      return this.underlying.size();
   }

   public Object get(int index) {
      return this.trans.transform(this.underlying.get(index));
   }
}

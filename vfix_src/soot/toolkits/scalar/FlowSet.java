package soot.toolkits.scalar;

import java.util.Iterator;
import java.util.List;

public interface FlowSet<T> extends Iterable<T> {
   FlowSet<T> clone();

   FlowSet<T> emptySet();

   void copy(FlowSet<T> var1);

   void clear();

   void union(FlowSet<T> var1);

   void union(FlowSet<T> var1, FlowSet<T> var2);

   void intersection(FlowSet<T> var1);

   void intersection(FlowSet<T> var1, FlowSet<T> var2);

   void difference(FlowSet<T> var1);

   void difference(FlowSet<T> var1, FlowSet<T> var2);

   boolean isEmpty();

   int size();

   void add(T var1);

   void add(T var1, FlowSet<T> var2);

   void remove(T var1);

   void remove(T var1, FlowSet<T> var2);

   boolean contains(T var1);

   boolean isSubSet(FlowSet<T> var1);

   Iterator<T> iterator();

   List<T> toList();
}

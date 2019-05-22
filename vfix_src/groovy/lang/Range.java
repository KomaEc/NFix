package groovy.lang;

import java.util.List;

public interface Range<T extends Comparable> extends List<T> {
   Comparable getFrom();

   Comparable getTo();

   boolean isReverse();

   boolean containsWithinBounds(Object var1);

   void step(int var1, Closure var2);

   List<T> step(int var1);

   String inspect();
}

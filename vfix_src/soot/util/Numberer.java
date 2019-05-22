package soot.util;

public interface Numberer<E> {
   void add(E var1);

   long get(E var1);

   E get(long var1);

   int size();
}

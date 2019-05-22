package edu.emory.mathcs.backport.java.util;

import java.util.Collection;

public interface Queue extends Collection {
   boolean add(Object var1);

   boolean offer(Object var1);

   Object remove();

   Object poll();

   Object element();

   Object peek();
}

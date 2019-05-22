package com.gzoltar.shaded.org.pitest.functional;

import java.util.Collection;
import java.util.List;

public interface FunctionalIterable<A> extends Iterable<A> {
   void forEach(SideEffect1<A> var1);

   <B> List<B> map(F<A, B> var1);

   <B> void mapTo(F<A, B> var1, Collection<? super B> var2);

   <B> List<B> flatMap(F<A, ? extends Iterable<B>> var1);

   List<A> filter(F<A, Boolean> var1);

   boolean contains(F<A, Boolean> var1);
}

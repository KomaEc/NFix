package com.gzoltar.shaded.org.pitest.reloc.antlr.common.collections;

import java.util.NoSuchElementException;

public interface Stack {
   int height();

   Object pop() throws NoSuchElementException;

   void push(Object var1);

   Object top() throws NoSuchElementException;
}

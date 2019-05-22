package com.google.common.hash;

interface LongAddable {
   void increment();

   void add(long var1);

   long sum();
}

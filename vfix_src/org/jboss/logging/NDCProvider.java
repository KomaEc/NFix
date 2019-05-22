package org.jboss.logging;

public interface NDCProvider {
   void clear();

   String get();

   int getDepth();

   String pop();

   String peek();

   void push(String var1);

   void setMaxDepth(int var1);
}

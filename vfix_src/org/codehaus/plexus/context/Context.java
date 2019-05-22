package org.codehaus.plexus.context;

public interface Context {
   Object get(Object var1) throws ContextException;

   boolean contains(Object var1);

   void put(Object var1, Object var2) throws IllegalStateException;

   void hide(Object var1) throws IllegalStateException;

   void makeReadOnly();
}

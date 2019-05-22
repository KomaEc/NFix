package org.apache.velocity.util.introspection;

public interface IntrospectorCacheListener {
   void triggerClear();

   void triggerGet(Class var1, ClassMap var2);

   void triggerPut(Class var1, ClassMap var2);
}

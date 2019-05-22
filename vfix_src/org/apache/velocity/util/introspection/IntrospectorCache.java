package org.apache.velocity.util.introspection;

public interface IntrospectorCache {
   void clear();

   ClassMap get(Class var1);

   ClassMap put(Class var1);

   void addListener(IntrospectorCacheListener var1);

   void removeListener(IntrospectorCacheListener var1);
}

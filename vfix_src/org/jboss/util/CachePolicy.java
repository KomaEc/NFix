package org.jboss.util;

public interface CachePolicy {
   Object get(Object var1);

   Object peek(Object var1);

   void insert(Object var1, Object var2);

   void remove(Object var1);

   void flush();

   int size();

   void create() throws Exception;

   void start() throws Exception;

   void stop();

   void destroy();
}

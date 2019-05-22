package org.codehaus.groovy.binding;

import groovy.lang.GroovyObjectSupport;
import java.util.LinkedHashMap;
import java.util.Map;

class BindPathSnooper extends GroovyObjectSupport {
   static final DeadEndObject DEAD_END = new DeadEndObject();
   Map<String, BindPathSnooper> fields = new LinkedHashMap();

   public Object getProperty(String property) {
      if (this.fields.containsKey(property)) {
         return this.fields.get(property);
      } else {
         BindPathSnooper snooper = new BindPathSnooper();
         this.fields.put(property, snooper);
         return snooper;
      }
   }

   public Object invokeMethod(String name, Object args) {
      return DEAD_END;
   }
}

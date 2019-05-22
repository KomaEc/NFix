package org.codehaus.groovy.runtime.callsite;

import groovy.lang.GroovyObject;
import groovy.lang.GroovyRuntimeException;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;

public class PogoGetPropertySite extends AbstractCallSite {
   private final Class aClass;

   public PogoGetPropertySite(CallSite parent, Class aClass) {
      super(parent);
      this.aClass = aClass;
   }

   public CallSite acceptGetProperty(Object receiver) {
      return (CallSite)(receiver.getClass() != this.aClass ? this.createGetPropertySite(receiver) : this);
   }

   public CallSite acceptGroovyObjectGetProperty(Object receiver) {
      return (CallSite)(receiver.getClass() != this.aClass ? this.createGroovyObjectGetPropertySite(receiver) : this);
   }

   public Object getProperty(Object receiver) throws Throwable {
      try {
         return ((GroovyObject)receiver).getProperty(this.name);
      } catch (GroovyRuntimeException var3) {
         throw ScriptBytecodeAdapter.unwrap(var3);
      }
   }
}

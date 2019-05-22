package org.codehaus.groovy.runtime.callsite;

import groovy.lang.GroovyRuntimeException;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;

public class PojoMetaClassGetPropertySite extends AbstractCallSite {
   public PojoMetaClassGetPropertySite(CallSite parent) {
      super(parent);
   }

   public final CallSite acceptGetProperty(Object receiver) {
      return this;
   }

   public final Object getProperty(Object receiver) throws Throwable {
      try {
         return InvokerHelper.getProperty(receiver, this.name);
      } catch (GroovyRuntimeException var3) {
         throw ScriptBytecodeAdapter.unwrap(var3);
      }
   }

   public Object callGetProperty(Object receiver) throws Throwable {
      try {
         return InvokerHelper.getProperty(receiver, this.name);
      } catch (GroovyRuntimeException var3) {
         throw ScriptBytecodeAdapter.unwrap(var3);
      }
   }
}

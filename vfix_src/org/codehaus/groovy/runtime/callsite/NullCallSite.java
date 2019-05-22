package org.codehaus.groovy.runtime.callsite;

import groovy.lang.GroovyRuntimeException;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.NullObject;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;

public final class NullCallSite extends AbstractCallSite {
   public NullCallSite(CallSite callSite) {
      super(callSite);
   }

   public Object call(Object receiver, Object[] args) throws Throwable {
      if (receiver == null) {
         try {
            return CallSiteArray.defaultCall(this, NullObject.getNullObject(), args);
         } catch (GroovyRuntimeException var4) {
            throw ScriptBytecodeAdapter.unwrap(var4);
         }
      } else {
         return CallSiteArray.defaultCall(this, receiver, args);
      }
   }

   public Object getProperty(Object receiver) throws Throwable {
      if (receiver == null) {
         try {
            return InvokerHelper.getProperty(NullObject.getNullObject(), this.name);
         } catch (GroovyRuntimeException var3) {
            throw ScriptBytecodeAdapter.unwrap(var3);
         }
      } else {
         return this.acceptGetProperty(receiver).getProperty(receiver);
      }
   }
}

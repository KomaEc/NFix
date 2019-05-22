package org.codehaus.groovy.runtime.callsite;

import groovy.lang.GroovyObject;
import groovy.lang.GroovyRuntimeException;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;

public class PogoInterceptableSite extends AbstractCallSite {
   public PogoInterceptableSite(CallSite site) {
      super(site);
   }

   public final Object invoke(Object receiver, Object[] args) throws Throwable {
      try {
         return ((GroovyObject)receiver).invokeMethod(this.name, InvokerHelper.asUnwrappedArray(args));
      } catch (GroovyRuntimeException var4) {
         throw ScriptBytecodeAdapter.unwrap(var4);
      }
   }

   public final Object call(Object receiver, Object[] args) throws Throwable {
      if (receiver instanceof GroovyObject) {
         try {
            return ((GroovyObject)receiver).invokeMethod(this.name, InvokerHelper.asUnwrappedArray(args));
         } catch (GroovyRuntimeException var4) {
            throw ScriptBytecodeAdapter.unwrap(var4);
         }
      } else {
         return CallSiteArray.defaultCall(this, receiver, args);
      }
   }

   public Object callCurrent(GroovyObject receiver, Object[] args) throws Throwable {
      return this.call(receiver, args);
   }
}

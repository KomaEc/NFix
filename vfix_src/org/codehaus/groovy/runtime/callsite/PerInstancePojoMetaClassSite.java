package org.codehaus.groovy.runtime.callsite;

import groovy.lang.GroovyRuntimeException;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;

public class PerInstancePojoMetaClassSite extends AbstractCallSite {
   private final ClassInfo info;

   public PerInstancePojoMetaClassSite(CallSite site, ClassInfo info) {
      super(site);
      this.info = info;
   }

   public Object call(Object receiver, Object[] args) throws Throwable {
      if (this.info.hasPerInstanceMetaClasses()) {
         try {
            return InvokerHelper.getMetaClass(receiver).invokeMethod(receiver, this.name, args);
         } catch (GroovyRuntimeException var4) {
            throw ScriptBytecodeAdapter.unwrap(var4);
         }
      } else {
         return CallSiteArray.defaultCall(this, receiver, args);
      }
   }
}

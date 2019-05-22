package org.codehaus.groovy.runtime.callsite;

import groovy.lang.GroovyObject;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaClass;
import groovy.lang.MissingMethodException;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.metaclass.MissingMethodExecutionFailed;

public class PogoMetaClassSite extends MetaClassSite {
   public PogoMetaClassSite(CallSite site, MetaClass metaClass) {
      super(site, metaClass);
   }

   public final Object call(Object receiver, Object[] args) throws Throwable {
      if (this.checkCall(receiver)) {
         try {
            try {
               return this.metaClass.invokeMethod(receiver, this.name, args);
            } catch (MissingMethodException var4) {
               if (var4 instanceof MissingMethodExecutionFailed) {
                  throw (MissingMethodException)var4.getCause();
               } else if (receiver.getClass() == var4.getType() && var4.getMethod().equals(this.name)) {
                  return ((GroovyObject)receiver).invokeMethod(this.name, args);
               } else {
                  throw var4;
               }
            }
         } catch (GroovyRuntimeException var5) {
            throw ScriptBytecodeAdapter.unwrap(var5);
         }
      } else {
         return CallSiteArray.defaultCall(this, receiver, args);
      }
   }

   protected final boolean checkCall(Object receiver) {
      return receiver instanceof GroovyObject && ((GroovyObject)receiver).getMetaClass() == this.metaClass;
   }

   public final Object callCurrent(GroovyObject receiver, Object[] args) throws Throwable {
      if (this.checkCall(receiver)) {
         try {
            try {
               return this.metaClass.invokeMethod(receiver, this.name, args);
            } catch (MissingMethodException var4) {
               if (var4 instanceof MissingMethodExecutionFailed) {
                  throw (MissingMethodException)var4.getCause();
               } else if (receiver.getClass() == var4.getType() && var4.getMethod().equals(this.name)) {
                  return receiver.invokeMethod(this.name, args);
               } else {
                  throw var4;
               }
            }
         } catch (GroovyRuntimeException var5) {
            throw ScriptBytecodeAdapter.unwrap(var5);
         }
      } else {
         return CallSiteArray.defaultCallCurrent(this, receiver, args);
      }
   }
}

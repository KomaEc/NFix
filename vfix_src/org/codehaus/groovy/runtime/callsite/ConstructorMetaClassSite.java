package org.codehaus.groovy.runtime.callsite;

import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaClass;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;

public class ConstructorMetaClassSite extends MetaClassSite {
   public ConstructorMetaClassSite(CallSite site, MetaClass metaClass) {
      super(site, metaClass);
   }

   public Object callConstructor(Object receiver, Object[] args) throws Throwable {
      if (receiver == this.metaClass.getTheClass()) {
         try {
            return this.metaClass.invokeConstructor(args);
         } catch (GroovyRuntimeException var4) {
            throw ScriptBytecodeAdapter.unwrap(var4);
         }
      } else {
         return CallSiteArray.defaultCallConstructor(this, (Class)receiver, args);
      }
   }
}

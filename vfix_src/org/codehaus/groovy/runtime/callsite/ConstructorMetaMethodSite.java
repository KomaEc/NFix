package org.codehaus.groovy.runtime.callsite;

import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaClassImpl;
import groovy.lang.MetaMethod;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;

public class ConstructorMetaMethodSite extends MetaMethodSite {
   private final int version;

   public ConstructorMetaMethodSite(CallSite site, MetaClassImpl metaClass, MetaMethod method, Class[] params) {
      super(site, metaClass, method, params);
      this.version = metaClass.getVersion();
   }

   public final Object invoke(Object receiver, Object[] args) throws Throwable {
      MetaClassHelper.unwrap(args);

      try {
         return this.metaMethod.doMethodInvoke(this.metaClass.getTheClass(), args);
      } catch (GroovyRuntimeException var4) {
         throw ScriptBytecodeAdapter.unwrap(var4);
      }
   }

   public final Object callConstructor(Object receiver, Object[] args) throws Throwable {
      if (receiver == this.metaClass.getTheClass() && ((MetaClassImpl)this.metaClass).getVersion() == this.version && MetaClassHelper.sameClasses(this.params, args)) {
         MetaClassHelper.unwrap(args);

         try {
            return this.metaMethod.doMethodInvoke(this.metaClass.getTheClass(), args);
         } catch (GroovyRuntimeException var4) {
            throw ScriptBytecodeAdapter.unwrap(var4);
         }
      } else {
         return CallSiteArray.defaultCallConstructor(this, receiver, args);
      }
   }
}

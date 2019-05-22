package org.codehaus.groovy.runtime.callsite;

import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaClass;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;

public class StaticMetaClassSite extends MetaClassSite {
   private final ClassInfo classInfo;
   private final int version;

   public StaticMetaClassSite(CallSite site, MetaClass metaClass) {
      super(site, metaClass);
      this.classInfo = ClassInfo.getClassInfo(metaClass.getTheClass());
      this.version = this.classInfo.getVersion();
   }

   private boolean checkCall(Object receiver) {
      return receiver == this.metaClass.getTheClass() && this.version == this.classInfo.getVersion();
   }

   public final Object call(Object receiver, Object[] args) throws Throwable {
      if (this.checkCall(receiver)) {
         try {
            return this.metaClass.invokeStaticMethod(receiver, this.name, args);
         } catch (GroovyRuntimeException var4) {
            throw ScriptBytecodeAdapter.unwrap(var4);
         }
      } else {
         return CallSiteArray.defaultCall(this, receiver, args);
      }
   }

   public final Object callStatic(Class receiver, Object[] args) throws Throwable {
      if (this.checkCall(receiver)) {
         try {
            return this.metaClass.invokeStaticMethod(receiver, this.name, args);
         } catch (GroovyRuntimeException var4) {
            throw ScriptBytecodeAdapter.unwrap(var4);
         }
      } else {
         return CallSiteArray.defaultCallStatic(this, receiver, args);
      }
   }
}

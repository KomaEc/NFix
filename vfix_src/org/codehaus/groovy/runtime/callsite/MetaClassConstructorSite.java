package org.codehaus.groovy.runtime.callsite;

import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaClass;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;

public class MetaClassConstructorSite extends MetaClassSite {
   private final ClassInfo classInfo;
   private final int version;

   public MetaClassConstructorSite(CallSite site, MetaClass metaClass) {
      super(site, metaClass);
      this.classInfo = ClassInfo.getClassInfo(metaClass.getTheClass());
      this.version = this.classInfo.getVersion();
   }

   public Object callConstructor(Object receiver, Object[] args) throws Throwable {
      try {
         return receiver == this.metaClass.getTheClass() && this.version == this.classInfo.getVersion() ? this.metaClass.invokeConstructor(args) : CallSiteArray.defaultCallConstructor(this, receiver, args);
      } catch (GroovyRuntimeException var4) {
         throw ScriptBytecodeAdapter.unwrap(var4);
      }
   }
}

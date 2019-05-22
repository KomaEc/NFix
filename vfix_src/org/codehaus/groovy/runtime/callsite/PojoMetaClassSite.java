package org.codehaus.groovy.runtime.callsite;

import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaClass;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;

public class PojoMetaClassSite extends MetaClassSite {
   private final ClassInfo classInfo;
   private final int version;

   public PojoMetaClassSite(CallSite site, MetaClass metaClass) {
      super(site, metaClass);
      this.classInfo = ClassInfo.getClassInfo(metaClass.getTheClass());
      this.version = this.classInfo.getVersion();
   }

   public Object call(Object receiver, Object[] args) throws Throwable {
      if (this.checkCall(receiver)) {
         try {
            return this.metaClass.invokeMethod(receiver, this.name, args);
         } catch (GroovyRuntimeException var4) {
            throw ScriptBytecodeAdapter.unwrap(var4);
         }
      } else {
         return CallSiteArray.defaultCall(this, receiver, args);
      }
   }

   protected final boolean checkCall(Object receiver) {
      return receiver.getClass() == this.metaClass.getTheClass() && this.version == this.classInfo.getVersion();
   }
}

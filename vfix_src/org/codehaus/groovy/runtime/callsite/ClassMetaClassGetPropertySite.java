package org.codehaus.groovy.runtime.callsite;

import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaClass;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;

class ClassMetaClassGetPropertySite extends AbstractCallSite {
   final MetaClass metaClass;
   private final Class aClass;
   private final ClassInfo classInfo;
   private final int version;

   public ClassMetaClassGetPropertySite(CallSite parent, Class aClass) {
      super(parent);
      this.aClass = aClass;
      this.classInfo = ClassInfo.getClassInfo(aClass);
      this.version = this.classInfo.getVersion();
      this.metaClass = this.classInfo.getMetaClass();
   }

   public final CallSite acceptGetProperty(Object receiver) {
      return (CallSite)(receiver == this.aClass && this.version == this.classInfo.getVersion() ? this : this.createGetPropertySite(receiver));
   }

   public final Object getProperty(Object receiver) throws Throwable {
      try {
         return this.metaClass.getProperty(this.aClass, this.name);
      } catch (GroovyRuntimeException var3) {
         throw ScriptBytecodeAdapter.unwrap(var3);
      }
   }
}

package org.codehaus.groovy.runtime.callsite;

import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaClassImpl;
import groovy.lang.MetaProperty;
import org.codehaus.groovy.runtime.GroovyCategorySupport;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;

public class GetEffectivePojoPropertySite extends AbstractCallSite {
   private final MetaClassImpl metaClass;
   private final MetaProperty effective;
   private final int version;

   public GetEffectivePojoPropertySite(CallSite site, MetaClassImpl metaClass, MetaProperty effective) {
      super(site);
      this.metaClass = metaClass;
      this.effective = effective;
      this.version = metaClass.getVersion();
   }

   public final CallSite acceptGetProperty(Object receiver) {
      return (CallSite)(!GroovyCategorySupport.hasCategoryInCurrentThread() && receiver.getClass() == this.metaClass.getTheClass() && this.version == this.metaClass.getVersion() ? this : this.createGetPropertySite(receiver));
   }

   public final Object getProperty(Object receiver) throws Throwable {
      try {
         return this.effective.getProperty(receiver);
      } catch (GroovyRuntimeException var3) {
         throw ScriptBytecodeAdapter.unwrap(var3);
      }
   }
}

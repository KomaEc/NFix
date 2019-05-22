package org.codehaus.groovy.runtime.callsite;

import groovy.lang.GroovyObject;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaClass;
import groovy.lang.MetaProperty;
import org.codehaus.groovy.runtime.GroovyCategorySupport;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;

class GetEffectivePogoPropertySite extends AbstractCallSite {
   private final MetaClass metaClass;
   private final MetaProperty effective;

   public GetEffectivePogoPropertySite(CallSite site, MetaClass metaClass, MetaProperty effective) {
      super(site);
      this.metaClass = metaClass;
      this.effective = effective;
   }

   public final Object callGetProperty(Object receiver) throws Throwable {
      if (!GroovyCategorySupport.hasCategoryInCurrentThread() && receiver instanceof GroovyObject && ((GroovyObject)receiver).getMetaClass() == this.metaClass) {
         try {
            return this.effective.getProperty(receiver);
         } catch (GroovyRuntimeException var3) {
            throw ScriptBytecodeAdapter.unwrap(var3);
         }
      } else {
         return this.createGetPropertySite(receiver).getProperty(receiver);
      }
   }

   public final CallSite acceptGetProperty(Object receiver) {
      return (CallSite)(!GroovyCategorySupport.hasCategoryInCurrentThread() && receiver instanceof GroovyObject && ((GroovyObject)receiver).getMetaClass() == this.metaClass ? this : this.createGetPropertySite(receiver));
   }

   public final Object callGroovyObjectGetProperty(Object receiver) throws Throwable {
      if (!GroovyCategorySupport.hasCategoryInCurrentThread() && receiver instanceof GroovyObject && ((GroovyObject)receiver).getMetaClass() == this.metaClass) {
         try {
            return this.effective.getProperty(receiver);
         } catch (GroovyRuntimeException var3) {
            throw ScriptBytecodeAdapter.unwrap(var3);
         }
      } else {
         return this.createGetPropertySite(receiver).getProperty(receiver);
      }
   }

   public final CallSite acceptGroovyObjectGetProperty(Object receiver) {
      return (CallSite)(!GroovyCategorySupport.hasCategoryInCurrentThread() && receiver instanceof GroovyObject && ((GroovyObject)receiver).getMetaClass() == this.metaClass ? this : this.createGroovyObjectGetPropertySite(receiver));
   }

   public final Object getProperty(Object receiver) throws Throwable {
      try {
         return this.effective.getProperty(receiver);
      } catch (GroovyRuntimeException var3) {
         throw ScriptBytecodeAdapter.unwrap(var3);
      }
   }
}

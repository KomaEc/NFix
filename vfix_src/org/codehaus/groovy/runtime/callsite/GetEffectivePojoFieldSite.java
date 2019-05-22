package org.codehaus.groovy.runtime.callsite;

import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaClassImpl;
import java.lang.reflect.Field;
import org.codehaus.groovy.reflection.CachedField;
import org.codehaus.groovy.runtime.GroovyCategorySupport;

class GetEffectivePojoFieldSite extends AbstractCallSite {
   private final MetaClassImpl metaClass;
   private final Field effective;
   private final int version;

   public GetEffectivePojoFieldSite(CallSite site, MetaClassImpl metaClass, CachedField effective) {
      super(site);
      this.metaClass = metaClass;
      this.effective = effective.field;
      this.version = metaClass.getVersion();
   }

   public final CallSite acceptGetProperty(Object receiver) {
      return (CallSite)(!GroovyCategorySupport.hasCategoryInCurrentThread() && receiver.getClass() == this.metaClass.getTheClass() && this.version == this.metaClass.getVersion() ? this : this.createGetPropertySite(receiver));
   }

   public final Object getProperty(Object receiver) {
      try {
         return this.effective.get(receiver);
      } catch (IllegalAccessException var3) {
         throw new GroovyRuntimeException("Cannot get the property '" + this.name + "'.", var3);
      }
   }
}

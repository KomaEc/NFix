package org.codehaus.groovy.runtime.callsite;

import groovy.lang.GroovyObject;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaClass;
import java.lang.reflect.Field;
import org.codehaus.groovy.reflection.CachedField;
import org.codehaus.groovy.runtime.GroovyCategorySupport;

public class GetEffectivePogoFieldSite extends AbstractCallSite {
   private final MetaClass metaClass;
   private final Field effective;

   public GetEffectivePogoFieldSite(CallSite site, MetaClass metaClass, CachedField effective) {
      super(site);
      this.metaClass = metaClass;
      this.effective = effective.field;
   }

   public final Object callGetProperty(Object receiver) throws Throwable {
      return !GroovyCategorySupport.hasCategoryInCurrentThread() && receiver instanceof GroovyObject && ((GroovyObject)receiver).getMetaClass() == this.metaClass ? this.getProperty(receiver) : this.createGetPropertySite(receiver).getProperty(receiver);
   }

   public final CallSite acceptGetProperty(Object receiver) {
      return (CallSite)(!GroovyCategorySupport.hasCategoryInCurrentThread() && receiver instanceof GroovyObject && ((GroovyObject)receiver).getMetaClass() == this.metaClass ? this : this.createGetPropertySite(receiver));
   }

   public final Object callGroovyObjectGetProperty(Object receiver) throws Throwable {
      return !GroovyCategorySupport.hasCategoryInCurrentThread() && ((GroovyObject)receiver).getMetaClass() == this.metaClass ? this.getProperty(receiver) : this.createGroovyObjectGetPropertySite(receiver).getProperty(receiver);
   }

   public final CallSite acceptGroovyObjectGetProperty(Object receiver) {
      return (CallSite)(!GroovyCategorySupport.hasCategoryInCurrentThread() && receiver instanceof GroovyObject && ((GroovyObject)receiver).getMetaClass() == this.metaClass ? this : this.createGroovyObjectGetPropertySite(receiver));
   }

   public final Object getProperty(Object receiver) {
      try {
         return this.effective.get(receiver);
      } catch (IllegalAccessException var3) {
         throw new GroovyRuntimeException("Cannot get the property '" + this.name + "'.", var3);
      }
   }
}

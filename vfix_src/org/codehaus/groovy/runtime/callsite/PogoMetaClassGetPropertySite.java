package org.codehaus.groovy.runtime.callsite;

import groovy.lang.GroovyObject;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaClass;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;

public class PogoMetaClassGetPropertySite extends AbstractCallSite {
   private final MetaClass metaClass;

   public PogoMetaClassGetPropertySite(CallSite parent, MetaClass metaClass) {
      super(parent);
      this.metaClass = metaClass;
   }

   public final CallSite acceptGetProperty(Object receiver) {
      return (CallSite)(receiver instanceof GroovyObject && ((GroovyObject)receiver).getMetaClass() == this.metaClass ? this : this.createGetPropertySite(receiver));
   }

   public final CallSite acceptGroovyObjectGetProperty(Object receiver) {
      return (CallSite)(receiver instanceof GroovyObject && ((GroovyObject)receiver).getMetaClass() == this.metaClass ? this : this.createGroovyObjectGetPropertySite(receiver));
   }

   public final Object getProperty(Object receiver) throws Throwable {
      try {
         return this.metaClass.getProperty(receiver, this.name);
      } catch (GroovyRuntimeException var3) {
         throw ScriptBytecodeAdapter.unwrap(var3);
      }
   }
}

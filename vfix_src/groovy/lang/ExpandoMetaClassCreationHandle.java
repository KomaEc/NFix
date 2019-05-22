package groovy.lang;

import org.codehaus.groovy.reflection.ClassInfo;

public class ExpandoMetaClassCreationHandle extends MetaClassRegistry.MetaClassCreationHandle {
   public static final ExpandoMetaClassCreationHandle instance = new ExpandoMetaClassCreationHandle();

   protected MetaClass createNormalMetaClass(Class theClass, MetaClassRegistry registry) {
      return (MetaClass)(theClass != ExpandoMetaClass.class ? new ExpandoMetaClass(theClass, true, true) : super.createNormalMetaClass(theClass, registry));
   }

   public void registerModifiedMetaClass(ExpandoMetaClass emc) {
      Class klazz = emc.getJavaClass();
      GroovySystem.getMetaClassRegistry().setMetaClass(klazz, emc);
   }

   public boolean hasModifiedMetaClass(ExpandoMetaClass emc) {
      return emc.getClassInfo().getModifiedExpando() != null;
   }

   public static void enable() {
      MetaClassRegistry metaClassRegistry = GroovySystem.getMetaClassRegistry();
      synchronized(metaClassRegistry) {
         if (metaClassRegistry.getMetaClassCreationHandler() != instance) {
            ClassInfo.clearModifiedExpandos();
            metaClassRegistry.setMetaClassCreationHandle(instance);
         }

      }
   }

   public static void disable() {
      MetaClassRegistry metaClassRegistry = GroovySystem.getMetaClassRegistry();
      synchronized(metaClassRegistry) {
         if (metaClassRegistry.getMetaClassCreationHandler() == instance) {
            ClassInfo.clearModifiedExpandos();
            metaClassRegistry.setMetaClassCreationHandle(new MetaClassRegistry.MetaClassCreationHandle());
         }

      }
   }
}

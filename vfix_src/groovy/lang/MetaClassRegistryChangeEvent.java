package groovy.lang;

import java.util.EventObject;

public class MetaClassRegistryChangeEvent extends EventObject {
   private Class clazz;
   private MetaClass metaClass;

   public MetaClassRegistryChangeEvent(Object source, Class clazz, MetaClass metaClass) {
      super(source);
      this.clazz = clazz;
      this.metaClass = metaClass;
   }

   public Class getClassToUpdate() {
      return this.clazz;
   }

   public MetaClass getNewMetaClass() {
      return this.metaClass;
   }
}

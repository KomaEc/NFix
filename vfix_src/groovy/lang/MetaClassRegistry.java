package groovy.lang;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.metaclass.ClosureMetaClass;

public interface MetaClassRegistry {
   MetaClass getMetaClass(Class var1);

   void setMetaClass(Class var1, MetaClass var2);

   void removeMetaClass(Class var1);

   MetaClassRegistry.MetaClassCreationHandle getMetaClassCreationHandler();

   void setMetaClassCreationHandle(MetaClassRegistry.MetaClassCreationHandle var1);

   void addMetaClassRegistryChangeEventListener(MetaClassRegistryChangeEventListener var1);

   void removeMetaClassRegistryChangeEventListener(MetaClassRegistryChangeEventListener var1);

   MetaClassRegistryChangeEventListener[] getMetaClassRegistryChangeEventListeners();

   Iterator iterator();

   public static class MetaClassCreationHandle {
      private boolean disableCustomMetaClassLookup;

      public final MetaClass create(Class theClass, MetaClassRegistry registry) {
         return this.disableCustomMetaClassLookup ? this.createNormalMetaClass(theClass, registry) : this.createWithCustomLookup(theClass, registry);
      }

      private MetaClass createWithCustomLookup(Class theClass, MetaClassRegistry registry) {
         try {
            Class customMetaClass = Class.forName("groovy.runtime.metaclass." + theClass.getName() + "MetaClass");
            Constructor customMetaClassConstructor;
            if (DelegatingMetaClass.class.isAssignableFrom(customMetaClass)) {
               customMetaClassConstructor = customMetaClass.getConstructor(MetaClass.class);
               MetaClass normalMetaClass = this.createNormalMetaClass(theClass, registry);
               return (MetaClass)customMetaClassConstructor.newInstance(normalMetaClass);
            } else {
               customMetaClassConstructor = customMetaClass.getConstructor(MetaClassRegistry.class, Class.class);
               return (MetaClass)customMetaClassConstructor.newInstance(registry, theClass);
            }
         } catch (ClassNotFoundException var6) {
            return this.createNormalMetaClass(theClass, registry);
         } catch (Exception var7) {
            throw new GroovyRuntimeException("Could not instantiate custom Metaclass for class: " + theClass.getName() + ". Reason: " + var7, var7);
         }
      }

      protected MetaClass createNormalMetaClass(Class theClass, MetaClassRegistry registry) {
         return (MetaClass)(GeneratedClosure.class.isAssignableFrom(theClass) ? new ClosureMetaClass(registry, theClass) : new MetaClassImpl(registry, theClass));
      }

      public boolean isDisableCustomMetaClassLookup() {
         return this.disableCustomMetaClassLookup;
      }

      public void setDisableCustomMetaClassLookup(boolean disableCustomMetaClassLookup) {
         this.disableCustomMetaClassLookup = disableCustomMetaClassLookup;
      }
   }
}

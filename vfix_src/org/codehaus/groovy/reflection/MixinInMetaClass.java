package org.codehaus.groovy.reflection;

import groovy.lang.DelegatingMetaClass;
import groovy.lang.ExpandoMetaClass;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.GroovySystem;
import groovy.lang.MetaClass;
import groovy.lang.MetaMethod;
import groovy.lang.MetaProperty;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.runtime.HandleMetaClass;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.runtime.metaclass.MixedInMetaClass;
import org.codehaus.groovy.runtime.metaclass.MixinInstanceMetaMethod;
import org.codehaus.groovy.runtime.metaclass.MixinInstanceMetaProperty;
import org.codehaus.groovy.runtime.metaclass.NewInstanceMetaMethod;
import org.codehaus.groovy.util.ManagedConcurrentMap;
import org.codehaus.groovy.util.ReferenceBundle;

public class MixinInMetaClass extends ManagedConcurrentMap {
   final ExpandoMetaClass emc;
   final CachedClass mixinClass;
   final CachedConstructor constructor;
   private static ReferenceBundle softBundle = ReferenceBundle.getSoftBundle();

   public MixinInMetaClass(ExpandoMetaClass emc, CachedClass mixinClass) {
      super(softBundle);
      this.emc = emc;
      this.mixinClass = mixinClass;
      this.constructor = this.findDefaultConstructor(mixinClass);
      emc.addMixinClass(this);
   }

   private CachedConstructor findDefaultConstructor(CachedClass mixinClass) {
      CachedConstructor[] arr$ = mixinClass.getConstructors();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         CachedConstructor constr = arr$[i$];
         if (Modifier.isPublic(constr.getModifiers())) {
            CachedClass[] classes = constr.getParameterTypes();
            if (classes.length == 0) {
               return constr;
            }
         }
      }

      throw new GroovyRuntimeException("No default constructor for class " + mixinClass.getName() + "! Can't be mixed in.");
   }

   public synchronized Object getMixinInstance(Object object) {
      Object mixinInstance = this.get(object);
      if (mixinInstance == null) {
         mixinInstance = this.constructor.invoke(MetaClassHelper.EMPTY_ARRAY);
         new MixedInMetaClass(mixinInstance, object);
         this.put(object, mixinInstance);
      }

      return mixinInstance;
   }

   public synchronized void setMixinInstance(Object object, Object mixinInstance) {
      if (mixinInstance == null) {
         this.remove(object);
      } else {
         this.put(object, mixinInstance);
      }

   }

   public CachedClass getInstanceClass() {
      return this.emc.getTheCachedClass();
   }

   public CachedClass getMixinClass() {
      return this.mixinClass;
   }

   public static void mixinClassesToMetaClass(MetaClass self, List<Class> categoryClasses) {
      Class selfClass = self.getTheClass();
      if (self instanceof HandleMetaClass) {
         self = (MetaClass)((HandleMetaClass)self).replaceDelegate();
      }

      if (!(self instanceof ExpandoMetaClass)) {
         if (!(self instanceof DelegatingMetaClass) || !(((DelegatingMetaClass)self).getAdaptee() instanceof ExpandoMetaClass)) {
            throw new GroovyRuntimeException("Can't mixin methods to meta class: " + self);
         }

         self = ((DelegatingMetaClass)self).getAdaptee();
      }

      ExpandoMetaClass mc = (ExpandoMetaClass)self;
      List<MetaMethod> arr = new ArrayList();
      Iterator i$ = categoryClasses.iterator();

      label92:
      while(i$.hasNext()) {
         Class categoryClass = (Class)i$.next();
         CachedClass cachedCategoryClass = ReflectionCache.getCachedClass(categoryClass);
         MixinInMetaClass mixin = new MixinInMetaClass(mc, cachedCategoryClass);
         MetaClass metaClass = GroovySystem.getMetaClassRegistry().getMetaClass(categoryClass);
         List<MetaProperty> propList = metaClass.getProperties();
         Iterator i$ = propList.iterator();

         while(i$.hasNext()) {
            MetaProperty prop = (MetaProperty)i$.next();
            if (self.getMetaProperty(prop.getName()) == null) {
               mc.registerBeanProperty(prop.getName(), new MixinInstanceMetaProperty(prop, mixin));
            }
         }

         CachedField[] arr$ = cachedCategoryClass.getFields();
         int len$ = arr$.length;

         int mod;
         for(mod = 0; mod < len$; ++mod) {
            MetaProperty prop = arr$[mod];
            if (self.getMetaProperty(prop.getName()) == null) {
               mc.registerBeanProperty(prop.getName(), new MixinInstanceMetaProperty(prop, mixin));
            }
         }

         i$ = metaClass.getMethods().iterator();

         while(true) {
            while(true) {
               MetaMethod method;
               do {
                  do {
                     if (!i$.hasNext()) {
                        continue label92;
                     }

                     method = (MetaMethod)i$.next();
                     mod = method.getModifiers();
                  } while(!Modifier.isPublic(mod));
               } while(method instanceof CachedMethod && ((CachedMethod)method).getCachedMethod().isSynthetic());

               if (Modifier.isStatic(mod)) {
                  if (method instanceof CachedMethod) {
                     staticMethod(self, arr, (CachedMethod)method);
                  }
               } else if (method.getDeclaringClass().getTheClass() != Object.class || method.getName().equals("toString")) {
                  MixinInstanceMetaMethod metaMethod = new MixinInstanceMetaMethod(method, mixin);
                  arr.add(metaMethod);
               }
            }
         }
      }

      i$ = arr.iterator();

      while(i$.hasNext()) {
         Object res = i$.next();
         MetaMethod metaMethod = (MetaMethod)res;
         if (metaMethod.getDeclaringClass().isAssignableFrom(selfClass)) {
            mc.registerInstanceMethod(metaMethod);
         } else {
            mc.registerSubclassInstanceMethod(metaMethod);
         }
      }

   }

   private static void staticMethod(final MetaClass self, List<MetaMethod> arr, CachedMethod method) {
      CachedClass[] paramTypes = method.getParameterTypes();
      if (paramTypes.length != 0) {
         NewInstanceMetaMethod metaMethod;
         if (paramTypes[0].isAssignableFrom(self.getTheClass())) {
            if (paramTypes[0].getTheClass() == self.getTheClass()) {
               metaMethod = new NewInstanceMetaMethod(method);
            } else {
               metaMethod = new NewInstanceMetaMethod(method) {
                  public CachedClass getDeclaringClass() {
                     return ReflectionCache.getCachedClass(self.getTheClass());
                  }
               };
            }

            arr.add(metaMethod);
         } else if (self.getTheClass().isAssignableFrom(paramTypes[0].getTheClass())) {
            metaMethod = new NewInstanceMetaMethod(method);
            arr.add(metaMethod);
         }

      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof MixinInMetaClass)) {
         return false;
      } else if (!super.equals(o)) {
         return false;
      } else {
         MixinInMetaClass that = (MixinInMetaClass)o;
         if (this.mixinClass != null) {
            if (!this.mixinClass.equals(that.mixinClass)) {
               return false;
            }
         } else if (that.mixinClass != null) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int result = super.hashCode();
      result = 31 * result + (this.emc != null ? this.emc.hashCode() : 0);
      result = 31 * result + (this.mixinClass != null ? this.mixinClass.hashCode() : 0);
      result = 31 * result + (this.constructor != null ? this.constructor.hashCode() : 0);
      return result;
   }
}

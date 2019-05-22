package org.codehaus.groovy.runtime.callsite;

import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaClassImpl;
import java.util.Map;
import org.codehaus.groovy.reflection.CachedConstructor;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;

public class ConstructorSite extends MetaClassSite {
   final CachedConstructor constructor;
   final Class[] params;
   private final int version;

   public ConstructorSite(CallSite site, MetaClassImpl metaClass, CachedConstructor constructor, Class[] params) {
      super(site, metaClass);
      this.constructor = constructor;
      this.params = params;
      this.version = metaClass.getVersion();
   }

   public Object callConstructor(Object receiver, Object[] args) throws Throwable {
      if (this.checkCall(receiver, args)) {
         MetaClassHelper.unwrap(args);

         try {
            return this.constructor.doConstructorInvoke(args);
         } catch (GroovyRuntimeException var4) {
            throw ScriptBytecodeAdapter.unwrap(var4);
         }
      } else {
         return CallSiteArray.defaultCallConstructor(this, receiver, args);
      }
   }

   protected final boolean checkCall(Object receiver, Object[] args) {
      return receiver == this.metaClass.getTheClass() && ((MetaClassImpl)this.metaClass).getVersion() == this.version && MetaClassHelper.sameClasses(this.params, args);
   }

   public static ConstructorSite createConstructorSite(CallSite site, MetaClassImpl metaClass, CachedConstructor constructor, Class[] params, Object[] args) {
      if (constructor.correctArguments(args) == args && noWrappers(args)) {
         return (ConstructorSite)(noCoerce(constructor, args) ? new ConstructorSite.ConstructorSiteNoUnwrap(site, metaClass, constructor, params) : new ConstructorSite.ConstructorSiteNoUnwrapNoCoerce(site, metaClass, constructor, params));
      } else {
         return new ConstructorSite(site, metaClass, constructor, params);
      }
   }

   public static class NoParamSite extends ConstructorSite.ConstructorSiteNoUnwrapNoCoerce {
      private static final Object[] NO_ARGS = new Object[0];

      public NoParamSite(CallSite site, MetaClassImpl metaClass, CachedConstructor constructor, Class[] params) {
         super(site, metaClass, constructor, params);
      }

      public final Object callConstructor(Object receiver, Object[] args) throws Throwable {
         if (this.checkCall(receiver, args)) {
            Object bean = this.constructor.invoke(NO_ARGS);

            try {
               ((MetaClassImpl)this.metaClass).setProperties(bean, (Map)args[0]);
               return bean;
            } catch (GroovyRuntimeException var5) {
               throw ScriptBytecodeAdapter.unwrap(var5);
            }
         } else {
            return CallSiteArray.defaultCallConstructor(this, receiver, args);
         }
      }
   }

   public static class ConstructorSiteNoUnwrapNoCoerce extends ConstructorSite {
      public ConstructorSiteNoUnwrapNoCoerce(CallSite site, MetaClassImpl metaClass, CachedConstructor constructor, Class[] params) {
         super(site, metaClass, constructor, params);
      }

      public Object callConstructor(Object receiver, Object[] args) throws Throwable {
         if (this.checkCall(receiver, args)) {
            try {
               return this.constructor.invoke(args);
            } catch (GroovyRuntimeException var4) {
               throw ScriptBytecodeAdapter.unwrap(var4);
            }
         } else {
            return CallSiteArray.defaultCallConstructor(this, receiver, args);
         }
      }
   }

   public static class ConstructorSiteNoUnwrap extends ConstructorSite {
      public ConstructorSiteNoUnwrap(CallSite site, MetaClassImpl metaClass, CachedConstructor constructor, Class[] params) {
         super(site, metaClass, constructor, params);
      }

      public final Object callConstructor(Object receiver, Object[] args) throws Throwable {
         if (this.checkCall(receiver, args)) {
            try {
               return this.constructor.doConstructorInvoke(args);
            } catch (GroovyRuntimeException var4) {
               throw ScriptBytecodeAdapter.unwrap(var4);
            }
         } else {
            return CallSiteArray.defaultCallConstructor(this, receiver, args);
         }
      }
   }
}

package org.codehaus.groovy.runtime.callsite;

import groovy.lang.GroovyInterceptable;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.MetaClassImpl;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.InvokerHelper;

public final class CallSiteArray {
   public final CallSite[] array;
   public static final Object[] NOPARAM = new Object[0];
   public final Class owner;

   public CallSiteArray(Class owner, String[] names) {
      this.owner = owner;
      this.array = new CallSite[names.length];

      for(int i = 0; i < this.array.length; ++i) {
         this.array[i] = new AbstractCallSite(this, i, names[i]);
      }

   }

   public static Object defaultCall(CallSite callSite, Object receiver, Object[] args) throws Throwable {
      return createCallSite(callSite, receiver, args).call(receiver, args);
   }

   public static Object defaultCallCurrent(CallSite callSite, GroovyObject receiver, Object[] args) throws Throwable {
      return createCallCurrentSite(callSite, receiver, args, callSite.getArray().owner).callCurrent(receiver, args);
   }

   public static Object defaultCallStatic(CallSite callSite, Class receiver, Object[] args) throws Throwable {
      return createCallStaticSite(callSite, receiver, args).callStatic(receiver, args);
   }

   public static Object defaultCallConstructor(CallSite callSite, Object receiver, Object[] args) throws Throwable {
      return createCallConstructorSite(callSite, (Class)receiver, args).callConstructor(receiver, args);
   }

   private static CallSite createCallStaticSite(CallSite callSite, Class receiver, Object[] args) {
      MetaClass metaClass = InvokerHelper.getMetaClass(receiver);
      Object site;
      if (metaClass instanceof MetaClassImpl) {
         site = ((MetaClassImpl)metaClass).createStaticSite(callSite, args);
      } else {
         site = new StaticMetaClassSite(callSite, metaClass);
      }

      replaceCallSite(callSite, (CallSite)site);
      return (CallSite)site;
   }

   private static CallSite createCallConstructorSite(CallSite callSite, Class receiver, Object[] args) {
      MetaClass metaClass = InvokerHelper.getMetaClass(receiver);
      Object site;
      if (metaClass instanceof MetaClassImpl) {
         site = ((MetaClassImpl)metaClass).createConstructorSite(callSite, args);
      } else {
         site = new MetaClassConstructorSite(callSite, metaClass);
      }

      replaceCallSite(callSite, (CallSite)site);
      return (CallSite)site;
   }

   private static CallSite createCallCurrentSite(CallSite callSite, GroovyObject receiver, Object[] args, Class sender) {
      Object site;
      if (receiver instanceof GroovyInterceptable) {
         site = new PogoInterceptableSite(callSite);
      } else {
         MetaClass metaClass = receiver.getMetaClass();
         if (receiver.getClass() != metaClass.getTheClass() && !metaClass.getTheClass().isInterface()) {
            site = new PogoInterceptableSite(callSite);
         } else if (metaClass instanceof MetaClassImpl) {
            site = ((MetaClassImpl)metaClass).createPogoCallCurrentSite(callSite, sender, args);
         } else {
            site = new PogoMetaClassSite(callSite, metaClass);
         }
      }

      replaceCallSite(callSite, (CallSite)site);
      return (CallSite)site;
   }

   private static CallSite createPojoSite(CallSite callSite, Object receiver, Object[] args) {
      Class klazz = receiver.getClass();
      MetaClass metaClass = InvokerHelper.getMetaClass(receiver);
      if (callSite.getUsage().get() == 0 && metaClass instanceof MetaClassImpl) {
         MetaClassImpl mci = (MetaClassImpl)metaClass;
         ClassInfo info = mci.getTheCachedClass().classInfo;
         return (CallSite)(info.hasPerInstanceMetaClasses() ? new PerInstancePojoMetaClassSite(callSite, info) : mci.createPojoCallSite(callSite, receiver, args));
      } else {
         ClassInfo info = ClassInfo.getClassInfo(klazz);
         return (CallSite)(info.hasPerInstanceMetaClasses() ? new PerInstancePojoMetaClassSite(callSite, info) : new PojoMetaClassSite(callSite, metaClass));
      }
   }

   private static CallSite createPogoSite(CallSite callSite, Object receiver, Object[] args) {
      if (receiver instanceof GroovyInterceptable) {
         return new PogoInterceptableSite(callSite);
      } else {
         MetaClass metaClass = ((GroovyObject)receiver).getMetaClass();
         return (CallSite)(metaClass instanceof MetaClassImpl ? ((MetaClassImpl)metaClass).createPogoCallSite(callSite, args) : new PogoMetaClassSite(callSite, metaClass));
      }
   }

   private static CallSite createCallSite(CallSite callSite, Object receiver, Object[] args) {
      if (receiver == null) {
         return new NullCallSite(callSite);
      } else {
         CallSite site;
         if (receiver instanceof Class) {
            site = createCallStaticSite(callSite, (Class)receiver, args);
         } else if (receiver instanceof GroovyObject) {
            site = createPogoSite(callSite, receiver, args);
         } else {
            site = createPojoSite(callSite, receiver, args);
         }

         replaceCallSite(callSite, site);
         return site;
      }
   }

   private static void replaceCallSite(CallSite oldSite, CallSite newSite) {
      oldSite.getArray().array[oldSite.getIndex()] = newSite;
   }
}

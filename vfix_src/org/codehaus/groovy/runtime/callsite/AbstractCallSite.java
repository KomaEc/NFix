package org.codehaus.groovy.runtime.callsite;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.lang.MetaClassImpl;
import groovy.lang.MetaProperty;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;
import org.codehaus.groovy.reflection.CachedClass;
import org.codehaus.groovy.reflection.CachedField;
import org.codehaus.groovy.reflection.ParameterTypes;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.codehaus.groovy.runtime.GroovyCategorySupport;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.wrappers.Wrapper;

public class AbstractCallSite implements CallSite {
   protected final int index;
   protected final String name;
   protected final CallSiteArray array;
   protected final AtomicInteger usage;

   public AbstractCallSite(CallSiteArray array, int index, String name) {
      this.name = name;
      this.index = index;
      this.array = array;
      this.usage = GroovyCategorySupport.getCategoryNameUsage(name);
   }

   public AbstractCallSite(CallSite prev) {
      this.name = prev.getName();
      this.index = prev.getIndex();
      this.array = prev.getArray();
      this.usage = prev.getUsage();
   }

   public int getIndex() {
      return this.index;
   }

   public CallSiteArray getArray() {
      return this.array;
   }

   public String getName() {
      return this.name;
   }

   public AtomicInteger getUsage() {
      return this.usage;
   }

   public final Object callSafe(Object receiver, Object[] args) throws Throwable {
      return receiver == null ? null : this.call(receiver, args);
   }

   public final Object callSafe(Object receiver) throws Throwable {
      return receiver == null ? null : this.call(receiver);
   }

   public final Object callSafe(Object receiver, Object arg1) throws Throwable {
      return receiver == null ? null : this.call(receiver, arg1);
   }

   public final Object callSafe(Object receiver, Object arg1, Object arg2) throws Throwable {
      return receiver == null ? null : this.call(receiver, arg1, arg2);
   }

   public final Object callSafe(Object receiver, Object arg1, Object arg2, Object arg3) throws Throwable {
      return receiver == null ? null : this.call(receiver, arg1, arg2, arg3);
   }

   public Object callSafe(Object receiver, Object arg1, Object arg2, Object arg3, Object arg4) throws Throwable {
      return receiver == null ? null : this.call(receiver, arg1, arg2, arg3, arg4);
   }

   public Object call(Object receiver, Object[] args) throws Throwable {
      return CallSiteArray.defaultCall(this, receiver, args);
   }

   public Object call(Object receiver) throws Throwable {
      return this.call(receiver, CallSiteArray.NOPARAM);
   }

   public Object call(Object receiver, Object arg1) throws Throwable {
      return this.call(receiver, ArrayUtil.createArray(arg1));
   }

   public Object call(Object receiver, Object arg1, Object arg2) throws Throwable {
      return this.call(receiver, ArrayUtil.createArray(arg1, arg2));
   }

   public Object call(Object receiver, Object arg1, Object arg2, Object arg3) throws Throwable {
      return this.call(receiver, ArrayUtil.createArray(arg1, arg2, arg3));
   }

   public Object call(Object receiver, Object arg1, Object arg2, Object arg3, Object arg4) throws Throwable {
      return this.call(receiver, ArrayUtil.createArray(arg1, arg2, arg3, arg4));
   }

   public Object callCurrent(GroovyObject receiver, Object[] args) throws Throwable {
      return CallSiteArray.defaultCallCurrent(this, receiver, args);
   }

   public Object callCurrent(GroovyObject receiver) throws Throwable {
      return this.callCurrent(receiver, CallSiteArray.NOPARAM);
   }

   public Object callCurrent(GroovyObject receiver, Object arg1) throws Throwable {
      return this.callCurrent(receiver, ArrayUtil.createArray(arg1));
   }

   public Object callCurrent(GroovyObject receiver, Object arg1, Object arg2) throws Throwable {
      return this.callCurrent(receiver, ArrayUtil.createArray(arg1, arg2));
   }

   public Object callCurrent(GroovyObject receiver, Object arg1, Object arg2, Object arg3) throws Throwable {
      return this.callCurrent(receiver, ArrayUtil.createArray(arg1, arg2, arg3));
   }

   public Object callCurrent(GroovyObject receiver, Object arg1, Object arg2, Object arg3, Object arg4) throws Throwable {
      return this.callCurrent(receiver, ArrayUtil.createArray(arg1, arg2, arg3, arg4));
   }

   public Object callStatic(Class receiver, Object[] args) throws Throwable {
      return CallSiteArray.defaultCallStatic(this, receiver, args);
   }

   public Object callStatic(Class receiver) throws Throwable {
      return this.callStatic(receiver, CallSiteArray.NOPARAM);
   }

   public Object callStatic(Class receiver, Object arg1) throws Throwable {
      return this.callStatic(receiver, ArrayUtil.createArray(arg1));
   }

   public Object callStatic(Class receiver, Object arg1, Object arg2) throws Throwable {
      return this.callStatic(receiver, ArrayUtil.createArray(arg1, arg2));
   }

   public Object callStatic(Class receiver, Object arg1, Object arg2, Object arg3) throws Throwable {
      return this.callStatic(receiver, ArrayUtil.createArray(arg1, arg2, arg3));
   }

   public Object callStatic(Class receiver, Object arg1, Object arg2, Object arg3, Object arg4) throws Throwable {
      return this.callStatic(receiver, ArrayUtil.createArray(arg1, arg2, arg3, arg4));
   }

   public Object callConstructor(Object receiver, Object[] args) throws Throwable {
      return CallSiteArray.defaultCallConstructor(this, receiver, args);
   }

   public Object callConstructor(Object receiver) throws Throwable {
      return this.callConstructor(receiver, CallSiteArray.NOPARAM);
   }

   public Object callConstructor(Object receiver, Object arg1) throws Throwable {
      return this.callConstructor(receiver, ArrayUtil.createArray(arg1));
   }

   public Object callConstructor(Object receiver, Object arg1, Object arg2) throws Throwable {
      return this.callConstructor(receiver, ArrayUtil.createArray(arg1, arg2));
   }

   public Object callConstructor(Object receiver, Object arg1, Object arg2, Object arg3) throws Throwable {
      return this.callConstructor(receiver, ArrayUtil.createArray(arg1, arg2, arg3));
   }

   public Object callConstructor(Object receiver, Object arg1, Object arg2, Object arg3, Object arg4) throws Throwable {
      return this.callConstructor(receiver, ArrayUtil.createArray(arg1, arg2, arg3, arg4));
   }

   static boolean noCoerce(ParameterTypes metaMethod, Object[] args) {
      CachedClass[] paramClasses = metaMethod.getParameterTypes();
      if (paramClasses.length != args.length) {
         return false;
      } else {
         for(int i = 0; i < paramClasses.length; ++i) {
            CachedClass paramClass = paramClasses[i];
            if (args[i] != null && !paramClass.isDirectlyAssignable(args[i])) {
               return true;
            }
         }

         return false;
      }
   }

   static boolean noWrappers(Object[] args) {
      for(int i = 0; i != args.length; ++i) {
         if (args[i] instanceof Wrapper) {
            return false;
         }
      }

      return true;
   }

   public Object callGetProperty(Object receiver) throws Throwable {
      return this.acceptGetProperty(receiver).getProperty(receiver);
   }

   public Object callGroovyObjectGetProperty(Object receiver) throws Throwable {
      return this.acceptGroovyObjectGetProperty(receiver).getProperty(receiver);
   }

   public CallSite acceptGetProperty(Object receiver) {
      return this.createGetPropertySite(receiver);
   }

   public CallSite acceptGroovyObjectGetProperty(Object receiver) {
      return this.createGroovyObjectGetPropertySite(receiver);
   }

   protected final CallSite createGetPropertySite(Object receiver) {
      if (receiver == null) {
         return new NullCallSite(this);
      } else if (receiver instanceof GroovyObject) {
         return this.createGroovyObjectGetPropertySite(receiver);
      } else {
         return receiver instanceof Class ? this.createClassMetaClassGetPropertySite((Class)receiver) : this.createPojoMetaClassGetPropertySite(receiver);
      }
   }

   protected final CallSite createGroovyObjectGetPropertySite(Object receiver) {
      Class aClass = receiver.getClass();

      try {
         Method method = aClass.getMethod("getProperty", String.class);
         if (method != null && method.isSynthetic() && ((GroovyObject)receiver).getMetaClass() instanceof MetaClassImpl) {
            return this.createPogoMetaClassGetPropertySite((GroovyObject)receiver);
         }
      } catch (NoSuchMethodException var4) {
      }

      return receiver instanceof Class ? this.createClassMetaClassGetPropertySite((Class)receiver) : this.createPogoGetPropertySite(aClass);
   }

   public Object getProperty(Object receiver) throws Throwable {
      throw new UnsupportedOperationException();
   }

   private CallSite createPojoMetaClassGetPropertySite(Object receiver) {
      MetaClass metaClass = InvokerHelper.getMetaClass(receiver);
      Object site;
      if (metaClass.getClass() == MetaClassImpl.class && !GroovyCategorySupport.hasCategoryInCurrentThread()) {
         MetaProperty effective = ((MetaClassImpl)metaClass).getEffectiveGetMetaProperty(receiver.getClass(), receiver, this.name, false);
         if (effective != null) {
            if (effective instanceof CachedField) {
               site = new GetEffectivePojoFieldSite(this, (MetaClassImpl)metaClass, (CachedField)effective);
            } else {
               site = new GetEffectivePojoPropertySite(this, (MetaClassImpl)metaClass, effective);
            }
         } else {
            site = new PojoMetaClassGetPropertySite(this);
         }
      } else {
         site = new PojoMetaClassGetPropertySite(this);
      }

      this.array.array[this.index] = (CallSite)site;
      return (CallSite)site;
   }

   private CallSite createClassMetaClassGetPropertySite(Class aClass) {
      CallSite site = new ClassMetaClassGetPropertySite(this, aClass);
      this.array.array[this.index] = site;
      return site;
   }

   private CallSite createPogoMetaClassGetPropertySite(GroovyObject receiver) {
      MetaClass metaClass = receiver.getMetaClass();
      Object site;
      if (metaClass.getClass() == MetaClassImpl.class && !GroovyCategorySupport.hasCategoryInCurrentThread()) {
         MetaProperty effective = ((MetaClassImpl)metaClass).getEffectiveGetMetaProperty(metaClass.getClass(), receiver, this.name, false);
         if (effective != null) {
            if (effective instanceof CachedField) {
               site = new GetEffectivePogoFieldSite(this, metaClass, (CachedField)effective);
            } else {
               site = new GetEffectivePogoPropertySite(this, metaClass, effective);
            }
         } else {
            site = new PogoMetaClassGetPropertySite(this, metaClass);
         }
      } else {
         site = new PogoMetaClassGetPropertySite(this, metaClass);
      }

      this.array.array[this.index] = (CallSite)site;
      return (CallSite)site;
   }

   private CallSite createPogoGetPropertySite(Class aClass) {
      CallSite site = new PogoGetPropertySite(this, aClass);
      this.array.array[this.index] = site;
      return site;
   }

   public final Object callGetPropertySafe(Object receiver) throws Throwable {
      return receiver == null ? null : this.callGetProperty(receiver);
   }

   public final Object callGroovyObjectGetPropertySafe(Object receiver) throws Throwable {
      return receiver == null ? null : this.callGroovyObjectGetProperty(receiver);
   }
}

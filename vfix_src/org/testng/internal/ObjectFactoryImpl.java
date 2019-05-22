package org.testng.internal;

import java.lang.reflect.Constructor;
import org.testng.IObjectFactory;
import org.testng.TestNGException;

public class ObjectFactoryImpl implements IObjectFactory {
   private static final long serialVersionUID = -4547389328475540017L;

   public Object newInstance(Constructor constructor, Object... params) {
      try {
         constructor.setAccessible(true);
         return constructor.newInstance(params);
      } catch (IllegalAccessException var4) {
         return ClassHelper.tryOtherConstructor(constructor.getDeclaringClass());
      } catch (InstantiationException var5) {
         return ClassHelper.tryOtherConstructor(constructor.getDeclaringClass());
      } catch (Exception var6) {
         throw new TestNGException("Cannot instantiate class " + (constructor != null ? constructor.getDeclaringClass().getName() : ": couldn't find a suitable constructor"), var6);
      }
   }
}

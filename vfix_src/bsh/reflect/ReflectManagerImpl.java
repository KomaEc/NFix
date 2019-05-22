package bsh.reflect;

import bsh.ReflectManager;
import java.lang.reflect.AccessibleObject;

public class ReflectManagerImpl extends ReflectManager {
   public boolean setAccessible(Object var1) {
      if (var1 instanceof AccessibleObject) {
         ((AccessibleObject)var1).setAccessible(true);
         return true;
      } else {
         return false;
      }
   }
}

package org.junit.runners.model;

import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;

public abstract class FrameworkMember<T extends FrameworkMember<T>> implements Annotatable {
   abstract boolean isShadowedBy(T var1);

   boolean isShadowedBy(List<T> members) {
      Iterator i$ = members.iterator();

      FrameworkMember each;
      do {
         if (!i$.hasNext()) {
            return false;
         }

         each = (FrameworkMember)i$.next();
      } while(!this.isShadowedBy(each));

      return true;
   }

   protected abstract int getModifiers();

   public boolean isStatic() {
      return Modifier.isStatic(this.getModifiers());
   }

   public boolean isPublic() {
      return Modifier.isPublic(this.getModifiers());
   }

   public abstract String getName();

   public abstract Class<?> getType();

   public abstract Class<?> getDeclaringClass();
}

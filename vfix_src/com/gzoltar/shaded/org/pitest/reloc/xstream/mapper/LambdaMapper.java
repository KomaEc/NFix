package com.gzoltar.shaded.org.pitest.reloc.xstream.mapper;

import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.Types;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class LambdaMapper extends MapperWrapper {
   public LambdaMapper(Mapper wrapped) {
      super(wrapped);
   }

   public String serializedClass(Class type) {
      Class<?> replacement = null;
      if (Types.isLambdaType(type)) {
         if (Serializable.class.isAssignableFrom(type)) {
            Class<?>[] interfaces = type.getInterfaces();
            if (interfaces.length > 1) {
               for(int i = 0; replacement == null && i < interfaces.length; ++i) {
                  Class<?> iface = interfaces[i];
                  Method[] var6 = iface.getMethods();
                  int var7 = var6.length;

                  for(int var8 = 0; var8 < var7; ++var8) {
                     Method method = var6[var8];
                     if (!method.isDefault() && !Modifier.isStatic(method.getModifiers())) {
                        replacement = iface;
                        break;
                     }
                  }
               }
            } else {
               replacement = interfaces[0];
            }
         } else {
            replacement = Mapper.Null.class;
         }
      }

      return super.serializedClass(replacement == null ? type : replacement);
   }
}

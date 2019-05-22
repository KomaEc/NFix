package org.testng;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ConversionUtils {
   public static Object[] wrapDataProvider(Class cls, Collection<Object[]> data) {
      List result = new ArrayList();
      Iterator i$ = data.iterator();

      while(i$.hasNext()) {
         Object o = i$.next();
         Object[] parameters = (Object[])((Object[])o);
         Constructor ctor = null;

         try {
            Constructor[] arr$ = cls.getConstructors();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               Constructor c = arr$[i$];
               if (c.getParameterTypes().length == parameters.length) {
                  ctor = c;
                  break;
               }
            }

            if (ctor == null) {
               throw new TestNGException("Couldn't find a constructor in " + cls);
            }

            result.add(ctor.newInstance(parameters));
         } catch (Exception var11) {
            var11.printStackTrace();
         }
      }

      return result.toArray();
   }
}

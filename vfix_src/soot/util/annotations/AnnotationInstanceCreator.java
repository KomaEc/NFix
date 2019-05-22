package soot.util.annotations;

import com.google.common.reflect.AbstractInvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.jboss.util.Classes;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationTag;

public class AnnotationInstanceCreator {
   public Object create(AnnotationTag tag) {
      ClassLoader cl = this.getClass().getClassLoader();

      try {
         final Class<?> clazz = Classes.loadClass(tag.getType().replace('/', '.'));
         final Map<String, Object> map = new HashMap();
         Iterator var5 = tag.getElems().iterator();

         while(var5.hasNext()) {
            AnnotationElem elem = (AnnotationElem)var5.next();
            AnnotationElemSwitch sw = new AnnotationElemSwitch();
            elem.apply(sw);
            AnnotationElemSwitch.AnnotationElemResult<Object> result = (AnnotationElemSwitch.AnnotationElemResult)sw.getResult();
            map.put(result.getKey(), result.getValue());
         }

         Object result = Proxy.newProxyInstance(cl, new Class[]{clazz}, new AbstractInvocationHandler() {
            protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
               String name = method.getName();
               Class<?> retType = method.getReturnType();
               if (name.equals("annotationType")) {
                  return clazz;
               } else {
                  Object result = map.get(name);
                  if (result != null) {
                     if (result instanceof Object[]) {
                        Object[] oa = (Object[])((Object[])result);
                        return Arrays.copyOf(oa, oa.length, retType);
                     } else {
                        return (retType.equals(Boolean.TYPE) || retType.equals(Boolean.class)) && result instanceof Integer ? (Integer)result != 0 : result;
                     }
                  } else {
                     result = method.getDefaultValue();
                     if (result != null) {
                        return result;
                     } else {
                        throw new RuntimeException("No value for " + name + " declared in the annotation " + clazz);
                     }
                  }
               }
            }
         });
         return result;
      } catch (ClassNotFoundException var9) {
         throw new RuntimeException("Could not load class: " + tag.getType());
      }
   }
}

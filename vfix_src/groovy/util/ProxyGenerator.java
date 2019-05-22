package groovy.util;

import groovy.lang.Binding;
import groovy.lang.Closure;
import groovy.lang.DelegatingMetaClass;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.GroovyShell;
import groovy.lang.GroovySystem;
import groovy.lang.MetaClass;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.codehaus.groovy.runtime.ConversionHandler;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.runtime.InvokerHelper;

public class ProxyGenerator {
   public static final ProxyGenerator INSTANCE = new ProxyGenerator();
   private ClassLoader override = null;
   private boolean debug = false;
   private boolean emptyMethods = false;
   private List<Method> objectMethods = this.getInheritedMethods(Object.class, new ArrayList());
   private List<Method> groovyObjectMethods = this.getInheritedMethods(GroovyObject.class, new ArrayList());

   public boolean getDebug() {
      return this.debug;
   }

   public void setDebug(boolean debug) {
      this.debug = debug;
   }

   public boolean getEmptyMethods() {
      return this.emptyMethods;
   }

   public void setEmptyMethods(boolean emptyMethods) {
      this.emptyMethods = emptyMethods;
   }

   public ClassLoader getOverride() {
      return this.override;
   }

   public void setOverride(ClassLoader override) {
      this.override = override;
   }

   public GroovyObject instantiateAggregateFromBaseClass(Class clazz) {
      return this.instantiateAggregateFromBaseClass((Map)null, clazz);
   }

   public GroovyObject instantiateAggregateFromBaseClass(Map map, Class clazz) {
      return this.instantiateAggregateFromBaseClass(map, clazz, (Object[])null);
   }

   public GroovyObject instantiateAggregateFromBaseClass(Closure cl, Class clazz) {
      Map<String, Closure> m = new HashMap();
      m.put("*", cl);
      return this.instantiateAggregateFromBaseClass(m, clazz, (Object[])null);
   }

   public GroovyObject instantiateAggregateFromBaseClass(Class clazz, Object[] constructorArgs) {
      return this.instantiateAggregate((Map)null, (List)null, clazz, constructorArgs);
   }

   public GroovyObject instantiateAggregateFromBaseClass(Map map, Class clazz, Object[] constructorArgs) {
      return this.instantiateAggregate(map, (List)null, clazz, constructorArgs);
   }

   public GroovyObject instantiateAggregateFromInterface(Class clazz) {
      return this.instantiateAggregateFromInterface((Map)null, clazz);
   }

   public GroovyObject instantiateAggregateFromInterface(Map map, Class clazz) {
      List<Class> interfaces = new ArrayList();
      interfaces.add(clazz);
      return this.instantiateAggregate(map, interfaces);
   }

   public GroovyObject instantiateAggregate(List<Class> interfaces) {
      return this.instantiateAggregate((Map)null, interfaces);
   }

   public GroovyObject instantiateAggregate(Map closureMap, List<Class> interfaces) {
      return this.instantiateAggregate(closureMap, interfaces, (Class)null);
   }

   public GroovyObject instantiateAggregate(Map closureMap, List<Class> interfaces, Class clazz) {
      return this.instantiateAggregate(closureMap, interfaces, clazz, (Object[])null);
   }

   public GroovyObject instantiateAggregate(Map closureMap, List<Class> interfaces, Class clazz, Object[] constructorArgs) {
      Map map = new HashMap();
      if (closureMap != null) {
         map = closureMap;
      }

      Object interfacesToImplement;
      if (interfaces == null) {
         interfacesToImplement = new ArrayList();
      } else {
         interfacesToImplement = interfaces;
      }

      Class baseClass = GroovyObjectSupport.class;
      if (clazz != null) {
         baseClass = clazz;
      }

      boolean hasArgs = constructorArgs != null && constructorArgs.length > 0;
      String name = this.shortName(baseClass.getName()) + "_groovyProxy";
      StringBuffer buffer = new StringBuffer();
      buffer.append("class ").append(name);
      if (clazz != null) {
         buffer.append(" extends ").append(baseClass.getName());
      }

      for(int i = 0; i < ((List)interfacesToImplement).size(); ++i) {
         Class thisInterface = (Class)((List)interfacesToImplement).get(i);
         if (i == 0) {
            buffer.append(" implements ");
         } else {
            buffer.append(", ");
         }

         buffer.append(thisInterface.getName());
      }

      buffer.append(" {\n").append("    private closureMap\n    ");
      buffer.append(name).append("(map");
      if (hasArgs) {
         buffer.append(", args");
      }

      buffer.append(") {\n");
      buffer.append("        super(");
      if (hasArgs) {
         buffer.append("*args");
      }

      buffer.append(")\n");
      buffer.append("        this.closureMap = map\n");
      buffer.append("    }\n");
      Map<String, Method> selectedMethods = new HashMap();
      List<Method> publicAndProtectedMethods = this.getInheritedMethods(baseClass, new ArrayList());
      boolean closureIndicator = ((Map)map).containsKey("*");
      Iterator i$ = publicAndProtectedMethods.iterator();

      while(true) {
         while(true) {
            Method method;
            do {
               do {
                  do {
                     do {
                        if (!i$.hasNext()) {
                           List<Method> interfaceMethods = new ArrayList();
                           Iterator i$ = ((List)interfacesToImplement).iterator();

                           while(i$.hasNext()) {
                              Class thisInterface = (Class)i$.next();
                              this.getInheritedMethods(thisInterface, interfaceMethods);
                           }

                           i$ = interfaceMethods.iterator();

                           while(i$.hasNext()) {
                              Method method = (Method)i$.next();
                              if (!this.containsEquivalentMethod(publicAndProtectedMethods, method)) {
                                 selectedMethods.put(method.getName(), method);
                                 this.addMapOrDummyCall((Map)map, buffer, method);
                              }
                           }

                           i$ = ((Map)map).keySet().iterator();

                           while(i$.hasNext()) {
                              Object o = i$.next();
                              String methodName = (String)o;
                              if (methodName.indexOf(36) == -1 && methodName.indexOf(42) == -1 && !selectedMethods.keySet().contains(methodName)) {
                                 this.addNewMapCall(buffer, methodName);
                              }
                           }

                           buffer.append("}\n").append("new ").append(name);
                           buffer.append("(map");
                           if (hasArgs) {
                              buffer.append(", constructorArgs");
                           }

                           buffer.append(")");
                           Binding binding = new Binding();
                           binding.setVariable("map", map);
                           binding.setVariable("constructorArgs", constructorArgs);
                           ClassLoader cl = this.override != null ? this.override : baseClass.getClassLoader();
                           if (clazz == null && ((List)interfacesToImplement).size() > 0) {
                              Class c = (Class)((List)interfacesToImplement).get(0);
                              cl = c.getClassLoader();
                           }

                           GroovyShell shell = new GroovyShell(cl, binding);
                           if (this.debug) {
                              System.out.println("proxy source:\n------------------\n" + buffer.toString() + "\n------------------");
                           }

                           try {
                              return (GroovyObject)shell.evaluate(buffer.toString());
                           } catch (MultipleCompilationErrorsException var19) {
                              throw new GroovyRuntimeException("Error creating proxy: " + var19.getMessage());
                           }
                        }

                        method = (Method)i$.next();
                     } while(method.getName().indexOf(36) != -1);
                  } while(Modifier.isFinal(method.getModifiers()));
               } while(ConversionHandler.isCoreObjectMethod(method));
            } while(this.containsEquivalentMethod(selectedMethods.values(), method));

            if (!((Map)map).containsKey(method.getName()) && !closureIndicator) {
               if (Modifier.isAbstract(method.getModifiers())) {
                  selectedMethods.put(method.getName(), method);
                  this.addMapOrDummyCall((Map)map, buffer, method);
               }
            } else {
               selectedMethods.put(method.getName(), method);
               this.addOverridingMapCall(buffer, method, closureIndicator);
            }
         }
      }
   }

   public GroovyObject instantiateDelegate(Object delegate) {
      return this.instantiateDelegate((List)null, delegate);
   }

   public GroovyObject instantiateDelegate(List<Class> interfaces, Object delegate) {
      return this.instantiateDelegate((Map)null, interfaces, delegate);
   }

   public GroovyObject instantiateDelegate(Map closureMap, List<Class> interfaces, Object delegate) {
      return this.instantiateDelegateWithBaseClass(closureMap, interfaces, delegate, (Class)null);
   }

   public GroovyObject instantiateDelegateWithBaseClass(Map closureMap, List<Class> interfaces, Object delegate) {
      return this.instantiateDelegateWithBaseClass(closureMap, interfaces, delegate, delegate.getClass());
   }

   public GroovyObject instantiateDelegateWithBaseClass(Map closureMap, List<Class> interfaces, Object delegate, Class baseClass) {
      String name = this.shortName(delegate.getClass().getName()) + "_delegateProxy";
      return this.instantiateDelegateWithBaseClass(closureMap, interfaces, delegate, baseClass, name);
   }

   public GroovyObject instantiateDelegateWithBaseClass(Map closureMap, List<Class> interfaces, Object delegate, Class baseClass, String name) {
      Map map = new HashMap();
      if (closureMap != null) {
         map = closureMap;
      }

      List<String> selectedMethods = new ArrayList();
      Object interfacesToImplement;
      if (interfaces == null) {
         interfacesToImplement = new ArrayList();
      } else {
         interfacesToImplement = interfaces;
      }

      StringBuffer buffer = new StringBuffer();
      buffer.append("import org.codehaus.groovy.runtime.InvokerHelper\nclass ").append(name);
      if (baseClass != null) {
         buffer.append(" extends ").append(baseClass.getName());
      }

      for(int i = 0; i < ((List)interfacesToImplement).size(); ++i) {
         Class thisInterface = (Class)((List)interfacesToImplement).get(i);
         if (i == 0) {
            buffer.append(" implements ");
         } else {
            buffer.append(", ");
         }

         buffer.append(thisInterface.getName());
      }

      buffer.append(" {\n").append("    private delegate\n").append("    private closureMap\n    ");
      buffer.append(name).append("(map, delegate) {\n");
      buffer.append("        this.closureMap = map\n");
      buffer.append("        this.delegate = delegate\n");
      buffer.append("    }\n");
      List<Method> interfaceMethods = new ArrayList();
      Iterator i$ = ((List)interfacesToImplement).iterator();

      while(i$.hasNext()) {
         Class thisInterface = (Class)i$.next();
         this.getInheritedMethods(thisInterface, interfaceMethods);
      }

      i$ = interfaceMethods.iterator();

      while(i$.hasNext()) {
         Method method = (Method)i$.next();
         if (!this.containsEquivalentMethod(this.objectMethods, method) && !this.containsEquivalentMethod(this.groovyObjectMethods, method)) {
            selectedMethods.add(method.getName());
            this.addWrappedCall(buffer, method, (Map)map);
         }
      }

      List<Method> additionalMethods = this.getInheritedMethods(delegate.getClass(), new ArrayList());
      Iterator i$ = additionalMethods.iterator();

      while(i$.hasNext()) {
         Method method = (Method)i$.next();
         if (method.getName().indexOf(36) == -1 && !this.containsEquivalentMethod(interfaceMethods, method) && !this.containsEquivalentMethod(this.objectMethods, method) && !this.containsEquivalentMethod(this.groovyObjectMethods, method)) {
            selectedMethods.add(method.getName());
            this.addWrappedCall(buffer, method, (Map)map);
         }
      }

      i$ = ((Map)map).keySet().iterator();

      while(i$.hasNext()) {
         Object o = i$.next();
         String methodName = (String)o;
         if (!selectedMethods.contains(methodName)) {
            this.addNewMapCall(buffer, methodName);
         }
      }

      buffer.append("}\n").append("new ").append(name);
      buffer.append("(map, delegate)");
      Binding binding = new Binding();
      binding.setVariable("map", map);
      binding.setVariable("delegate", delegate);
      ClassLoader cl = this.override != null ? this.override : delegate.getClass().getClassLoader();
      GroovyShell shell = new GroovyShell(cl, binding);
      if (this.debug) {
         System.out.println("proxy source:\n------------------\n" + buffer.toString() + "\n------------------");
      }

      try {
         return (GroovyObject)shell.evaluate(buffer.toString());
      } catch (MultipleCompilationErrorsException var16) {
         throw new GroovyRuntimeException("Error creating proxy: " + var16.getMessage());
      }
   }

   private void addWrappedCall(StringBuffer buffer, Method method, Map map) {
      if (map.containsKey(method.getName())) {
         this.addOverridingMapCall(buffer, method, false);
      } else {
         Class[] parameterTypes = this.addMethodPrefix(buffer, method);
         this.addWrappedMethodBody(buffer, method, parameterTypes);
         this.addMethodSuffix(buffer);
      }

   }

   private boolean containsEquivalentMethod(Collection<Method> publicAndProtectedMethods, Method candidate) {
      Iterator i$ = publicAndProtectedMethods.iterator();

      Method method;
      do {
         if (!i$.hasNext()) {
            return false;
         }

         method = (Method)i$.next();
      } while(!candidate.getName().equals(method.getName()) || !candidate.getReturnType().equals(method.getReturnType()) || !this.hasMatchingParameterTypes(candidate, method));

      return true;
   }

   private boolean hasMatchingParameterTypes(Method method, Method candidate) {
      Class[] candidateParamTypes = candidate.getParameterTypes();
      Class[] methodParamTypes = method.getParameterTypes();
      if (candidateParamTypes.length != methodParamTypes.length) {
         return false;
      } else {
         for(int i = 0; i < methodParamTypes.length; ++i) {
            if (!candidateParamTypes[i].equals(methodParamTypes[i])) {
               return false;
            }
         }

         return true;
      }
   }

   private List<Method> getInheritedMethods(Class baseClass, List<Method> methods) {
      methods.addAll(DefaultGroovyMethods.toList((Object[])baseClass.getMethods()));

      for(Class currentClass = baseClass; currentClass != null; currentClass = currentClass.getSuperclass()) {
         Method[] protectedMethods = currentClass.getDeclaredMethods();
         Method[] arr$ = protectedMethods;
         int len$ = protectedMethods.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Method method = arr$[i$];
            if (method.getName().indexOf(36) == -1 && Modifier.isProtected(method.getModifiers()) && !this.containsEquivalentMethod(methods, method)) {
               methods.add(method);
            }
         }
      }

      return methods;
   }

   private void addNewMapCall(StringBuffer buffer, String methodName) {
      buffer.append("    def ").append(methodName).append("(Object[] args) {\n").append("        this.@closureMap['").append(methodName).append("'] (*args)\n    }\n");
   }

   private void addOverridingMapCall(StringBuffer buffer, Method method, boolean closureIndicator) {
      Class[] parameterTypes = this.addMethodPrefix(buffer, method);
      this.addMethodBody(buffer, closureIndicator ? "*" : method.getName(), parameterTypes);
      this.addMethodSuffix(buffer);
   }

   private void addMapOrDummyCall(Map map, StringBuffer buffer, Method method) {
      Class[] parameterTypes = this.addMethodPrefix(buffer, method);
      if (map.containsKey(method.getName())) {
         this.addMethodBody(buffer, method.getName(), parameterTypes);
      } else if (!this.emptyMethods) {
         this.addUnsupportedBody(buffer);
      }

      this.addMethodSuffix(buffer);
   }

   private void addUnsupportedBody(StringBuffer buffer) {
      buffer.append("throw new UnsupportedOperationException()");
   }

   private Class[] addMethodPrefix(StringBuffer buffer, Method method) {
      buffer.append("    ").append(this.getSimpleName(method.getReturnType())).append(" ").append(method.getName()).append("(");
      Class[] parameterTypes = method.getParameterTypes();

      for(int parameterTypeIndex = 0; parameterTypeIndex < parameterTypes.length; ++parameterTypeIndex) {
         Class parameter = parameterTypes[parameterTypeIndex];
         if (parameterTypeIndex != 0) {
            buffer.append(", ");
         }

         buffer.append(this.getSimpleName(parameter)).append(" ").append("p").append(parameterTypeIndex);
      }

      buffer.append(") { ");
      return parameterTypes;
   }

   private void addMethodBody(StringBuffer buffer, String method, Class[] parameterTypes) {
      buffer.append("this.@closureMap['").append(method).append("'] (");

      for(int j = 0; j < parameterTypes.length; ++j) {
         if (j != 0) {
            buffer.append(", ");
         }

         buffer.append("p").append(j);
      }

      buffer.append(")");
   }

   private void addWrappedMethodBody(StringBuffer buffer, Method method, Class[] parameterTypes) {
      buffer.append("\n        Object[] args = [");

      for(int j = 0; j < parameterTypes.length; ++j) {
         if (j != 0) {
            buffer.append(", ");
         }

         buffer.append("p").append(j);
      }

      buffer.append("]\n        ");
      buffer.append("InvokerHelper.invokeMethod(delegate, '").append(method.getName()).append("', args)\n");
   }

   private void addMethodSuffix(StringBuffer buffer) {
      buffer.append("    }\n");
   }

   public String getSimpleName(Class c) {
      if (!c.isArray()) {
         return c.getName().replaceAll("\\$", "\\.");
      } else {
         int dimension = 0;

         Class componentClass;
         for(componentClass = c; componentClass.isArray(); ++dimension) {
            componentClass = componentClass.getComponentType();
         }

         return componentClass.getName().replaceAll("\\$", "\\.") + DefaultGroovyMethods.multiply((String)"[]", (Number)dimension);
      }
   }

   public String shortName(String name) {
      int index = name.lastIndexOf(46);
      return index == -1 ? name : name.substring(index + 1, name.length());
   }

   private static void setMetaClass(MetaClass metaClass) {
      MetaClass newMetaClass = new DelegatingMetaClass(metaClass) {
         public Object invokeStaticMethod(Object object, String methodName, Object[] arguments) {
            return InvokerHelper.invokeMethod(ProxyGenerator.INSTANCE, methodName, arguments);
         }
      };
      GroovySystem.getMetaClassRegistry().setMetaClass(ProxyGenerator.class, newMetaClass);
   }

   static {
      setMetaClass(GroovySystem.getMetaClassRegistry().getMetaClass(ProxyGenerator.class));
   }
}

package org.testng.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.testng.IClass;
import org.testng.IInstanceInfo;
import org.testng.ITestContext;
import org.testng.ITestObjectFactory;
import org.testng.TestNGException;
import org.testng.annotations.IAnnotation;
import org.testng.annotations.IObjectFactoryAnnotation;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.testng.internal.annotations.AnnotationHelper;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlTest;

public class TestNGClassFinder extends BaseClassFinder {
   private ITestContext m_testContext = null;
   private Map<Class, List<Object>> m_instanceMap = Maps.newHashMap();

   public TestNGClassFinder(ClassInfoMap cim, Map<Class, List<Object>> instanceMap, XmlTest xmlTest, IConfiguration configuration, ITestContext testContext) {
      this.m_testContext = testContext;
      if (null == instanceMap) {
         instanceMap = Maps.newHashMap();
      }

      IAnnotationFinder annotationFinder = configuration.getAnnotationFinder();
      ITestObjectFactory objectFactory = configuration.getObjectFactory();
      Set<Class<?>> allClasses = cim.getClasses();
      Iterator i$;
      Class cls;
      Iterator i$;
      if (objectFactory == null) {
         objectFactory = new ObjectFactoryImpl();
         i$ = allClasses.iterator();

         label181:
         while(i$.hasNext()) {
            cls = (Class)i$.next();

            try {
               if (null != cls) {
                  Method[] ms;
                  try {
                     ms = cls.getMethods();
                  } catch (NoClassDefFoundError var26) {
                     ppp("Warning: Can't link and determine methods of " + cls);
                     ms = new Method[0];
                  }

                  Method[] arr$ = ms;
                  int len$ = ms.length;

                  for(int i$ = 0; i$ < len$; ++i$) {
                     Method m = arr$[i$];
                     IAnnotation a = annotationFinder.findAnnotation(m, IObjectFactoryAnnotation.class);
                     if (null != a) {
                        if (!ITestObjectFactory.class.isAssignableFrom(m.getReturnType())) {
                           throw new TestNGException("Return type of " + m + " is not IObjectFactory");
                        }

                        try {
                           Object instance = cls.newInstance();
                           if (m.getParameterTypes().length > 0 && m.getParameterTypes()[0].equals(ITestContext.class)) {
                              objectFactory = (ITestObjectFactory)m.invoke(instance, testContext);
                           } else {
                              objectFactory = (ITestObjectFactory)m.invoke(instance);
                           }
                           break label181;
                        } catch (Exception var27) {
                           throw new TestNGException("Error creating object factory: " + cls, var27);
                        }
                     }
                  }
               }
            } catch (NoClassDefFoundError var28) {
               Utils.log("[TestNGClassFinder]", 1, "Unable to read methods on class " + cls.getName() + " - unable to resolve class reference " + var28.getMessage());
               i$ = xmlTest.getXmlClasses().iterator();

               while(i$.hasNext()) {
                  XmlClass xmlClass = (XmlClass)i$.next();
                  if (xmlClass.loadClasses() && xmlClass.getName().equals(cls.getName())) {
                     throw var28;
                  }
               }
            }
         }
      }

      i$ = allClasses.iterator();

      while(true) {
         List allInstances;
         while(i$.hasNext()) {
            cls = (Class)i$.next();
            if (null == cls) {
               ppp("FOUND NULL CLASS IN FOLLOWING ARRAY:");
               int i = 0;
               i$ = allClasses.iterator();

               while(i$.hasNext()) {
                  Class c = (Class)i$.next();
                  ppp("  " + i + ": " + c);
               }
            } else if (isTestNGClass(cls, annotationFinder)) {
               allInstances = (List)instanceMap.get(cls);
               Object thisInstance = null != allInstances ? allInstances.get(0) : null;
               if (null == thisInstance && Modifier.isAbstract(cls.getModifiers())) {
                  Utils.log("", 5, "[WARN] Found an abstract class with no valid instance attached: " + cls);
               } else {
                  IClass ic = this.findOrCreateIClass(this.m_testContext, cls, cim.getXmlClass(cls), thisInstance, xmlTest, annotationFinder, (ITestObjectFactory)objectFactory);
                  if (null != ic) {
                     Object[] theseInstances = ic.getInstances(false);
                     if (theseInstances.length == 0) {
                        theseInstances = ic.getInstances(true);
                     }

                     Object instance = theseInstances[0];
                     this.putIClass(cls, ic);
                     ConstructorOrMethod factoryMethod = ClassHelper.findDeclaredFactoryMethod(cls, annotationFinder);
                     if (factoryMethod != null && factoryMethod.getEnabled()) {
                        FactoryMethod fm = new FactoryMethod(factoryMethod, instance, xmlTest, annotationFinder, this.m_testContext);
                        ClassInfoMap moreClasses = new ClassInfoMap();
                        Object[] instances = fm.invoke();
                        int len$;
                        int i$;
                        if (instances.length > 0 && instances[0] != null) {
                           Class elementClass = instances[0].getClass();
                           if (IInstanceInfo.class.isAssignableFrom(elementClass)) {
                              Object[] arr$ = instances;
                              len$ = instances.length;

                              for(i$ = 0; i$ < len$; ++i$) {
                                 Object o = arr$[i$];
                                 IInstanceInfo ii = (IInstanceInfo)o;
                                 this.addInstance(ii.getInstanceClass(), ii.getInstance());
                                 moreClasses.addClass(ii.getInstanceClass());
                              }
                           } else {
                              for(int i = 0; i < instances.length; ++i) {
                                 Object o = instances[i];
                                 if (o == null) {
                                    throw new TestNGException("The factory " + fm + " returned a null instance" + "at index " + i);
                                 }

                                 this.addInstance(o.getClass(), o);
                                 if (!this.classExists(o.getClass())) {
                                    moreClasses.addClass(o.getClass());
                                 }
                              }
                           }
                        }

                        if (moreClasses.getSize() > 0) {
                           TestNGClassFinder finder = new TestNGClassFinder(moreClasses, this.m_instanceMap, xmlTest, configuration, this.m_testContext);
                           IClass[] moreIClasses = finder.findTestClasses();
                           IClass[] arr$ = moreIClasses;
                           len$ = moreIClasses.length;

                           for(i$ = 0; i$ < len$; ++i$) {
                              IClass ic2 = arr$[i$];
                              this.putIClass(ic2.getRealClass(), ic2);
                           }
                        }
                     }
                  }
               }
            } else {
               Utils.log("TestNGClassFinder", 3, "SKIPPING CLASS " + cls + " no TestNG annotations found");
            }
         }

         i$ = this.m_instanceMap.keySet().iterator();

         while(i$.hasNext()) {
            cls = (Class)i$.next();
            allInstances = (List)this.m_instanceMap.get(cls);
            i$ = allInstances.iterator();

            while(i$.hasNext()) {
               Object instance = i$.next();
               IClass ic = this.getIClass(cls);
               if (null != ic) {
                  ic.addInstance(instance);
               }
            }
         }

         return;
      }
   }

   public static boolean isTestNGClass(Class c, IAnnotationFinder annotationFinder) {
      Class[] allAnnotations = AnnotationHelper.getAllAnnotations();

      try {
         Class[] arr$ = allAnnotations;
         int len$ = allAnnotations.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Class annotation = arr$[i$];

            for(Class cls = c; cls != null; cls = cls.getSuperclass()) {
               Method[] arr$ = cls.getMethods();
               int len$ = arr$.length;

               int len$;
               for(len$ = 0; len$ < len$; ++len$) {
                  Method m = arr$[len$];
                  IAnnotation ma = annotationFinder.findAnnotation(m, annotation);
                  if (null != ma) {
                     return true;
                  }
               }

               IAnnotation a = annotationFinder.findAnnotation(cls, annotation);
               if (null != a) {
                  return true;
               }

               Constructor[] arr$ = cls.getConstructors();
               len$ = arr$.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  Constructor ctor = arr$[i$];
                  IAnnotation ca = annotationFinder.findAnnotation(ctor, annotation);
                  if (null != ca) {
                     return true;
                  }
               }
            }
         }

         return false;
      } catch (NoClassDefFoundError var14) {
         Utils.log("[TestNGClassFinder]", 1, "Unable to read methods on class " + c.getName() + " - unable to resolve class reference " + var14.getMessage());
         return false;
      }
   }

   private void addInstance(Class clazz, Object o) {
      List<Object> list = (List)this.m_instanceMap.get(clazz);
      if (null == list) {
         list = Lists.newArrayList();
         this.m_instanceMap.put(clazz, list);
      }

      list.add(o);
   }

   public static void ppp(String s) {
      System.out.println("[TestNGClassFinder] " + s);
   }
}

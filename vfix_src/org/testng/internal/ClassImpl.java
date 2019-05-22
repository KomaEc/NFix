package org.testng.internal;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import org.testng.IClass;
import org.testng.ISuite;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.ITestObjectFactory;
import org.testng.TestNGException;
import org.testng.collections.Lists;
import org.testng.collections.Objects;
import org.testng.internal.annotations.IAnnotationFinder;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlTest;

public class ClassImpl implements IClass {
   private static final long serialVersionUID = 1118178273317520344L;
   private transient Class m_class = null;
   private transient Object m_defaultInstance = null;
   private XmlTest m_xmlTest = null;
   private transient IAnnotationFinder m_annotationFinder = null;
   private transient List<Object> m_instances = Lists.newArrayList();
   private transient Map<Class, IClass> m_classes = null;
   private int m_instanceCount;
   private long[] m_instanceHashCodes;
   private transient Object m_instance;
   private ITestObjectFactory m_objectFactory;
   private String m_testName = null;
   private XmlClass m_xmlClass;
   private ITestContext m_testContext;
   private final boolean m_hasParentModule;

   public ClassImpl(ITestContext context, Class cls, XmlClass xmlClass, Object instance, Map<Class, IClass> classes, XmlTest xmlTest, IAnnotationFinder annotationFinder, ITestObjectFactory objectFactory) {
      this.m_testContext = context;
      this.m_class = cls;
      this.m_classes = classes;
      this.m_xmlClass = xmlClass;
      this.m_xmlTest = xmlTest;
      this.m_annotationFinder = annotationFinder;
      this.m_instance = instance;
      this.m_objectFactory = objectFactory;
      if (instance instanceof ITest) {
         this.m_testName = ((ITest)instance).getTestName();
      }

      this.m_hasParentModule = Utils.isStringNotEmpty(this.m_testContext.getSuite().getParentModule());
   }

   private static void ppp(String s) {
      System.out.println("[ClassImpl] " + s);
   }

   public String getTestName() {
      return this.m_testName;
   }

   public String getName() {
      return this.m_class.getName();
   }

   public Class getRealClass() {
      return this.m_class;
   }

   public int getInstanceCount() {
      return this.m_instanceCount;
   }

   public long[] getInstanceHashCodes() {
      return this.m_instanceHashCodes;
   }

   public XmlTest getXmlTest() {
      return this.m_xmlTest;
   }

   public XmlClass getXmlClass() {
      return this.m_xmlClass;
   }

   private Object getDefaultInstance() {
      if (this.m_defaultInstance == null) {
         if (this.m_instance != null) {
            this.m_defaultInstance = this.m_instance;
         } else {
            Object instance = this.getInstanceFromGuice();
            if (instance != null) {
               this.m_defaultInstance = instance;
            } else {
               this.m_defaultInstance = ClassHelper.createInstance(this.m_class, this.m_classes, this.m_xmlTest, this.m_annotationFinder, this.m_objectFactory);
            }
         }
      }

      return this.m_defaultInstance;
   }

   private Object getInstanceFromGuice() {
      Injector injector = this.m_testContext.getInjector((IClass)this);
      return injector == null ? null : injector.getInstance(this.m_class);
   }

   public Injector getParentInjector() {
      ISuite suite = this.m_testContext.getSuite();
      Injector injector = suite.getParentInjector();
      if (injector == null) {
         String stageString = suite.getGuiceStage();
         Stage stage;
         if (Utils.isStringNotEmpty(stageString)) {
            stage = Stage.valueOf(stageString);
         } else {
            stage = Stage.DEVELOPMENT;
         }

         if (this.m_hasParentModule) {
            Class<Module> parentModule = ClassHelper.forName(suite.getParentModule());
            if (parentModule == null) {
               throw new TestNGException("Cannot load parent Guice module class: " + parentModule);
            }

            Module module = this.newModule(parentModule);
            injector = Guice.createInjector(stage, module);
         } else {
            injector = Guice.createInjector(stage);
         }

         suite.setParentInjector(injector);
      }

      return injector;
   }

   private Module newModule(Class<Module> module) {
      try {
         Constructor<Module> moduleConstructor = module.getDeclaredConstructor(ITestContext.class);
         return (Module)ClassHelper.newInstance(moduleConstructor, this.m_testContext);
      } catch (NoSuchMethodException var3) {
         return (Module)ClassHelper.newInstance(module);
      }
   }

   public Object[] getInstances(boolean create) {
      Object[] result = new Object[0];
      if (this.m_xmlTest.isJUnit()) {
         if (create) {
            result = new Object[]{ClassHelper.createInstance(this.m_class, this.m_classes, this.m_xmlTest, this.m_annotationFinder, this.m_objectFactory)};
         }
      } else {
         result = new Object[]{this.getDefaultInstance()};
      }

      if (this.m_instances.size() > 0) {
         result = this.m_instances.toArray(new Object[this.m_instances.size()]);
      }

      this.m_instanceCount = this.m_instances.size();
      this.m_instanceHashCodes = new long[this.m_instanceCount];

      for(int i = 0; i < this.m_instanceCount; ++i) {
         this.m_instanceHashCodes[i] = (long)this.m_instances.get(i).hashCode();
      }

      return result;
   }

   public String toString() {
      return Objects.toStringHelper(this.getClass()).add("class", this.m_class.getName()).toString();
   }

   public void addInstance(Object instance) {
      this.m_instances.add(instance);
   }
}

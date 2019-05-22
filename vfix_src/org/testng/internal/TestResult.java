package org.testng.internal;

import java.util.List;
import java.util.Set;
import org.testng.IAttributes;
import org.testng.IClass;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.collections.Objects;

public class TestResult implements ITestResult {
   private static final long serialVersionUID = 6273017418233324556L;
   private IClass m_testClass = null;
   private ITestNGMethod m_method = null;
   private int m_status = -1;
   private Throwable m_throwable = null;
   private long m_startMillis = 0L;
   private long m_endMillis = 0L;
   private String m_name = null;
   private String m_host;
   private transient Object[] m_parameters = new Object[0];
   private transient Object m_instance;
   private String m_instanceName;
   private ITestContext m_context;
   private IAttributes m_attributes = new Attributes();

   public TestResult() {
   }

   public TestResult(IClass testClass, Object instance, ITestNGMethod method, Throwable throwable, long start, long end, ITestContext context) {
      this.init(testClass, instance, method, throwable, start, end, context);
   }

   public void init(IClass testClass, Object instance, ITestNGMethod method, Throwable throwable, long start, long end, ITestContext context) {
      this.m_testClass = testClass;
      this.m_throwable = throwable;
      this.m_instanceName = this.m_testClass.getName();
      if (null == this.m_throwable) {
         this.m_status = 1;
      }

      this.m_startMillis = start;
      this.m_endMillis = end;
      this.m_method = method;
      this.m_context = context;
      this.m_instance = instance;
      if (this.m_instance == null) {
         this.m_name = this.m_method.getMethodName();
      } else if (this.m_instance instanceof ITest) {
         this.m_name = ((ITest)this.m_instance).getTestName();
      } else {
         String string = this.m_instance.toString();
         this.m_name = this.getMethod().getMethodName();

         try {
            if (!Object.class.getMethod("toString").equals(this.m_instance.getClass().getMethod("toString"))) {
               this.m_instanceName = string.startsWith("class ") ? string.substring("class ".length()) : string;
               this.m_name = this.m_name + " on " + this.m_instanceName;
            }
         } catch (NoSuchMethodException var12) {
         }
      }

   }

   private static void ppp(String s) {
      System.out.println("[TestResult] " + s);
   }

   public void setEndMillis(long millis) {
      this.m_endMillis = millis;
   }

   public String getTestName() {
      return this.m_instance instanceof ITest ? ((ITest)this.m_instance).getTestName() : null;
   }

   public String getName() {
      return this.m_name;
   }

   public ITestNGMethod getMethod() {
      return this.m_method;
   }

   public void setMethod(ITestNGMethod method) {
      this.m_method = method;
   }

   public int getStatus() {
      return this.m_status;
   }

   public void setStatus(int status) {
      this.m_status = status;
   }

   public boolean isSuccess() {
      return 1 == this.m_status;
   }

   public IClass getTestClass() {
      return this.m_testClass;
   }

   public void setTestClass(IClass testClass) {
      this.m_testClass = testClass;
   }

   public Throwable getThrowable() {
      return this.m_throwable;
   }

   public void setThrowable(Throwable throwable) {
      this.m_throwable = throwable;
   }

   public long getEndMillis() {
      return this.m_endMillis;
   }

   public long getStartMillis() {
      return this.m_startMillis;
   }

   public String toString() {
      List<String> output = Reporter.getOutput(this);
      String result = Objects.toStringHelper(this.getClass()).omitNulls().omitEmptyStrings().add("name", this.getName()).add("status", this.toString(this.m_status)).add("method", (Object)this.m_method).add("output", output != null && output.size() > 0 ? (String)output.get(0) : null).toString();
      return result;
   }

   private String toString(int status) {
      switch(status) {
      case 1:
         return "SUCCESS";
      case 2:
         return "FAILURE";
      case 3:
         return "SKIP";
      case 4:
         return "SUCCESS WITHIN PERCENTAGE";
      case 16:
         return "STARTED";
      default:
         throw new RuntimeException();
      }
   }

   public String getHost() {
      return this.m_host;
   }

   public void setHost(String host) {
      this.m_host = host;
   }

   public Object[] getParameters() {
      return this.m_parameters;
   }

   public void setParameters(Object[] parameters) {
      this.m_parameters = parameters;
   }

   public Object getInstance() {
      return this.m_instance;
   }

   public Object getAttribute(String name) {
      return this.m_attributes.getAttribute(name);
   }

   public void setAttribute(String name, Object value) {
      this.m_attributes.setAttribute(name, value);
   }

   public Set<String> getAttributeNames() {
      return this.m_attributes.getAttributeNames();
   }

   public Object removeAttribute(String name) {
      return this.m_attributes.removeAttribute(name);
   }

   public ITestContext getTestContext() {
      return this.m_context;
   }

   public void setContext(ITestContext context) {
      this.m_context = context;
   }

   public int compareTo(ITestResult comparison) {
      if (this.getStartMillis() > comparison.getStartMillis()) {
         return 1;
      } else {
         return this.getStartMillis() < comparison.getStartMillis() ? -1 : 0;
      }
   }

   public String getInstanceName() {
      return this.m_instanceName;
   }
}

package org.testng.remote.strprotocol;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.collections.Lists;
import org.testng.internal.Utils;

public class TestResultMessage implements IStringMessage {
   protected int m_messageType;
   protected String m_suiteName;
   protected String m_testName;
   protected String m_testClassName;
   protected String m_testMethodName;
   protected String m_stackTrace;
   protected long m_startMillis;
   protected long m_endMillis;
   protected String[] m_parameters;
   protected String[] m_paramTypes;
   private String m_testDescription;
   private int m_invocationCount;
   private int m_currentInvocationCount;
   private String m_instanceName;

   public TestResultMessage(int resultType, String suiteName, String testName, String className, String methodName, String testDescriptor, String instanceName, String[] params, long startMillis, long endMillis, String stackTrace, int invocationCount, int currentInvocationCount) {
      this.m_parameters = new String[0];
      this.m_paramTypes = new String[0];
      this.init(resultType, suiteName, testName, className, methodName, stackTrace, startMillis, endMillis, this.extractParams(params), this.extractParamTypes(params), testDescriptor, instanceName, invocationCount, currentInvocationCount);
   }

   public TestResultMessage(String suiteName, String testName, ITestResult result) {
      this.m_parameters = new String[0];
      this.m_paramTypes = new String[0];
      Throwable throwable = result.getThrowable();
      String stackTrace = null;
      StringWriter sw;
      PrintWriter pw;
      if (2 != result.getStatus() && 4 != result.getStatus()) {
         if (3 == result.getStatus() && throwable != null && SkipException.class.isAssignableFrom(throwable.getClass())) {
            stackTrace = throwable.getMessage();
         } else if (throwable != null) {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            stackTrace = sw.toString();
         }
      } else {
         sw = new StringWriter();
         pw = new PrintWriter(sw);
         if (null != throwable) {
            throwable.printStackTrace(pw);
            stackTrace = sw.getBuffer().toString();
         } else {
            stackTrace = "unknown stack trace";
         }
      }

      this.init(1000 + result.getStatus(), suiteName, testName, result.getTestClass().getName(), result.getMethod().getMethod().getName(), MessageHelper.replaceUnicodeCharactersWithAscii(stackTrace), result.getStartMillis(), result.getEndMillis(), this.toString(result.getParameters(), result.getMethod().getMethod().getParameterTypes()), this.toString(result.getMethod().getMethod().getParameterTypes()), MessageHelper.replaceUnicodeCharactersWithAscii(result.getName()), MessageHelper.replaceUnicodeCharactersWithAscii(result.getInstanceName()), result.getMethod().getInvocationCount(), result.getMethod().getCurrentInvocationCount());
   }

   public TestResultMessage(ITestContext testCtx, ITestResult result) {
      this(testCtx.getSuite().getName(), testCtx.getCurrentXmlTest().getName(), result);
   }

   private void init(int resultType, String suiteName, String testName, String className, String methodName, String stackTrace, long startMillis, long endMillis, String[] parameters, String[] types, String testDescription, String instanceName, int invocationCount, int currentInvocationCount) {
      this.m_messageType = resultType;
      this.m_suiteName = suiteName;
      this.m_testName = testName;
      this.m_testClassName = className;
      this.m_testMethodName = methodName;
      this.m_stackTrace = stackTrace;
      this.m_startMillis = startMillis;
      this.m_endMillis = endMillis;
      this.m_parameters = parameters;
      this.m_paramTypes = types;
      this.m_testDescription = testDescription;
      this.m_invocationCount = invocationCount;
      this.m_currentInvocationCount = currentInvocationCount;
      this.m_instanceName = instanceName;
   }

   public int getResult() {
      return this.m_messageType;
   }

   public String getMessageAsString() {
      StringBuffer buf = new StringBuffer();
      StringBuffer parambuf = new StringBuffer();
      if (null != this.m_parameters && this.m_parameters.length > 0) {
         for(int j = 0; j < this.m_parameters.length; ++j) {
            if (j > 0) {
               parambuf.append('\u0004');
            }

            parambuf.append(this.m_paramTypes[j] + ":" + this.m_parameters[j]);
         }
      }

      buf.append(this.m_messageType).append('\u0001').append(this.m_suiteName).append('\u0001').append(this.m_testName).append('\u0001').append(this.m_testClassName).append('\u0001').append(this.m_testMethodName).append('\u0001').append(parambuf).append('\u0001').append(this.m_startMillis).append('\u0001').append(this.m_endMillis).append('\u0001').append(MessageHelper.replaceNewLine(this.m_stackTrace)).append('\u0001').append(MessageHelper.replaceNewLine(this.m_testDescription));
      return buf.toString();
   }

   public String getSuiteName() {
      return this.m_suiteName;
   }

   public String getTestClass() {
      return this.m_testClassName;
   }

   public String getMethod() {
      return this.m_testMethodName;
   }

   public String getName() {
      return this.m_testName;
   }

   public String getStackTrace() {
      return this.m_stackTrace;
   }

   public long getEndMillis() {
      return this.m_endMillis;
   }

   public long getStartMillis() {
      return this.m_startMillis;
   }

   public String[] getParameters() {
      return this.m_parameters;
   }

   public String[] getParameterTypes() {
      return this.m_paramTypes;
   }

   public String getTestDescription() {
      return this.m_testDescription;
   }

   public String toDisplayString() {
      StringBuffer buf = new StringBuffer(this.m_testName != null ? this.m_testName : this.m_testMethodName);
      if (null != this.m_parameters && this.m_parameters.length > 0) {
         buf.append("(");

         for(int i = 0; i < this.m_parameters.length; ++i) {
            if (i > 0) {
               buf.append(", ");
            }

            if ("java.lang.String".equals(this.m_paramTypes[i]) && !"null".equals(this.m_parameters[i]) && !"\"\"".equals(this.m_parameters[i])) {
               buf.append("\"").append(this.m_parameters[i]).append("\"");
            } else {
               buf.append(this.m_parameters[i]);
            }
         }

         buf.append(")");
      }

      return buf.toString();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         TestResultMessage that;
         label57: {
            that = (TestResultMessage)o;
            if (this.m_suiteName != null) {
               if (this.m_suiteName.equals(that.m_suiteName)) {
                  break label57;
               }
            } else if (that.m_suiteName == null) {
               break label57;
            }

            return false;
         }

         label50: {
            if (this.m_testName != null) {
               if (this.m_testName.equals(that.m_testName)) {
                  break label50;
               }
            } else if (that.m_testName == null) {
               break label50;
            }

            return false;
         }

         if (this.m_testClassName != null) {
            if (!this.m_testClassName.equals(that.m_testClassName)) {
               return false;
            }
         } else if (that.m_testClassName != null) {
            return false;
         }

         String toDisplayString = this.toDisplayString();
         if (toDisplayString != null) {
            if (!toDisplayString.equals(that.toDisplayString())) {
               return false;
            }
         } else if (that.toDisplayString() != null) {
            return false;
         }

         return true;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.m_suiteName != null ? this.m_suiteName.hashCode() : 0;
      result = 29 * result + (this.m_testName != null ? this.m_testName.hashCode() : 0);
      result = 29 * result + this.m_testClassName.hashCode();
      result = 29 * result + this.toDisplayString().hashCode();
      return result;
   }

   String[] toString(Object[] objects, Class<?>[] objectClasses) {
      if (null == objects) {
         return new String[0];
      } else {
         List<String> result = Lists.newArrayList(objects.length);
         Object[] arr$ = objects;
         int len$ = objects.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Object o = arr$[i$];
            if (null == o) {
               result.add("null");
            } else if (!o.getClass().isArray()) {
               String tostring = o.toString();
               if (Utils.isStringEmpty(tostring)) {
                  result.add("\"\"");
               } else {
                  result.add(MessageHelper.replaceNewLine(tostring));
               }
            } else {
               String[] strArray;
               if (o.getClass().getComponentType().isPrimitive()) {
                  strArray = this.primitiveArrayToString(o);
               } else {
                  strArray = this.toString((Object[])((Object[])o), (Class[])null);
               }

               StringBuilder sb = new StringBuilder("[");

               for(int i = 0; i < strArray.length; ++i) {
                  sb.append(strArray[i]);
                  if (i + 1 < strArray.length) {
                     sb.append(",");
                  }
               }

               sb.append("]");
               result.add(sb.toString());
            }
         }

         return (String[])result.toArray(new String[result.size()]);
      }
   }

   private String[] primitiveArrayToString(Object o) {
      List<String> results = Lists.newArrayList();
      int len$;
      int i$;
      if (o instanceof byte[]) {
         byte[] array = (byte[])((byte[])o);
         byte[] arr$ = array;
         len$ = array.length;

         for(i$ = 0; i$ < len$; ++i$) {
            byte anArray = arr$[i$];
            results.add(String.valueOf(anArray));
         }
      } else if (o instanceof boolean[]) {
         boolean[] array = (boolean[])((boolean[])o);
         boolean[] arr$ = array;
         len$ = array.length;

         for(i$ = 0; i$ < len$; ++i$) {
            boolean anArray = arr$[i$];
            results.add(String.valueOf(anArray));
         }
      } else if (o instanceof char[]) {
         char[] array = (char[])((char[])o);
         char[] arr$ = array;
         len$ = array.length;

         for(i$ = 0; i$ < len$; ++i$) {
            char anArray = arr$[i$];
            results.add(String.valueOf(anArray));
         }
      } else if (o instanceof double[]) {
         double[] array = (double[])((double[])o);
         double[] arr$ = array;
         len$ = array.length;

         for(i$ = 0; i$ < len$; ++i$) {
            double anArray = arr$[i$];
            results.add(String.valueOf(anArray));
         }
      } else if (o instanceof float[]) {
         float[] array = (float[])((float[])o);
         float[] arr$ = array;
         len$ = array.length;

         for(i$ = 0; i$ < len$; ++i$) {
            float anArray = arr$[i$];
            results.add(String.valueOf(anArray));
         }
      } else if (o instanceof short[]) {
         short[] array = (short[])((short[])o);
         short[] arr$ = array;
         len$ = array.length;

         for(i$ = 0; i$ < len$; ++i$) {
            short anArray = arr$[i$];
            results.add(String.valueOf(anArray));
         }
      } else if (o instanceof int[]) {
         int[] array = (int[])((int[])o);
         int[] arr$ = array;
         len$ = array.length;

         for(i$ = 0; i$ < len$; ++i$) {
            int anArray = arr$[i$];
            results.add(String.valueOf(anArray));
         }
      } else if (o instanceof long[]) {
         long[] array = (long[])((long[])o);
         long[] arr$ = array;
         len$ = array.length;

         for(i$ = 0; i$ < len$; ++i$) {
            long anArray = arr$[i$];
            results.add(String.valueOf(anArray));
         }
      }

      return (String[])results.toArray(new String[results.size()]);
   }

   private String[] toString(Class<?>[] classes) {
      if (null == classes) {
         return new String[0];
      } else {
         List<String> result = Lists.newArrayList(classes.length);
         Class[] arr$ = classes;
         int len$ = classes.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Class<?> cls = arr$[i$];
            result.add(cls.getName());
         }

         return (String[])result.toArray(new String[result.size()]);
      }
   }

   private String[] extractParamTypes(String[] params) {
      List<String> result = Lists.newArrayList(params.length);
      String[] arr$ = params;
      int len$ = params.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String s = arr$[i$];
         result.add(s.substring(0, s.indexOf(58)));
      }

      return (String[])result.toArray(new String[result.size()]);
   }

   private String[] extractParams(String[] params) {
      List<String> result = Lists.newArrayList(params.length);
      String[] arr$ = params;
      int len$ = params.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String s = arr$[i$];
         result.add(MessageHelper.replaceNewLineReplacer(s.substring(s.indexOf(58) + 1)));
      }

      return (String[])result.toArray(new String[result.size()]);
   }

   public int getInvocationCount() {
      return this.m_invocationCount;
   }

   public int getCurrentInvocationCount() {
      return this.m_currentInvocationCount;
   }

   public String toString() {
      return "[TestResultMessage suite:" + this.m_suiteName + " test:" + this.m_testName + " method:" + this.m_testMethodName + "]";
   }

   public void setParameters(String[] params) {
      this.m_parameters = this.extractParams(params);
      this.m_paramTypes = this.extractParamTypes(params);
   }

   public String getInstanceName() {
      return this.m_instanceName;
   }
}

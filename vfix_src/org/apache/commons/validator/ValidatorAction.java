package org.apache.commons.validator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.util.ValidatorUtils;

public class ValidatorAction implements Serializable {
   private static final Log log;
   private String name = null;
   private String classname = null;
   private Class validationClass = null;
   private String method = null;
   private Method validationMethod = null;
   private String methodParams = "java.lang.Object,org.apache.commons.validator.ValidatorAction,org.apache.commons.validator.Field";
   private Class[] parameterClasses = null;
   private String depends = null;
   private String msg = null;
   private String jsFunctionName = null;
   private String jsFunction = null;
   private String javascript = null;
   private Object instance = null;
   private List dependencyList = Collections.synchronizedList(new ArrayList());
   private List methodParameterList = new ArrayList();
   // $FF: synthetic field
   static Class class$org$apache$commons$validator$ValidatorAction;

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getClassname() {
      return this.classname;
   }

   public void setClassname(String classname) {
      this.classname = classname;
   }

   public String getMethod() {
      return this.method;
   }

   public void setMethod(String method) {
      this.method = method;
   }

   public String getMethodParams() {
      return this.methodParams;
   }

   public void setMethodParams(String methodParams) {
      this.methodParams = methodParams;
      this.methodParameterList.clear();
      StringTokenizer st = new StringTokenizer(methodParams, ",");

      while(st.hasMoreTokens()) {
         String value = st.nextToken().trim();
         if (value != null && value.length() > 0) {
            this.methodParameterList.add(value);
         }
      }

   }

   public String getDepends() {
      return this.depends;
   }

   public void setDepends(String depends) {
      this.depends = depends;
      this.dependencyList.clear();
      StringTokenizer st = new StringTokenizer(depends, ",");

      while(st.hasMoreTokens()) {
         String depend = st.nextToken().trim();
         if (depend != null && depend.length() > 0) {
            this.dependencyList.add(depend);
         }
      }

   }

   public String getMsg() {
      return this.msg;
   }

   public void setMsg(String msg) {
      this.msg = msg;
   }

   public String getJsFunctionName() {
      return this.jsFunctionName;
   }

   public void setJsFunctionName(String jsFunctionName) {
      this.jsFunctionName = jsFunctionName;
   }

   public void setJsFunction(String jsFunction) {
      if (this.javascript != null) {
         throw new IllegalStateException("Cannot call setJsFunction() after calling setJavascript()");
      } else {
         this.jsFunction = jsFunction;
      }
   }

   public String getJavascript() {
      return this.javascript;
   }

   public void setJavascript(String javascript) {
      if (this.jsFunction != null) {
         throw new IllegalStateException("Cannot call setJavascript() after calling setJsFunction()");
      } else {
         this.javascript = javascript;
      }
   }

   protected void init() {
      this.loadJavascriptFunction();
   }

   protected synchronized void loadJavascriptFunction() {
      if (!this.javascriptAlreadyLoaded()) {
         if (log.isTraceEnabled()) {
            log.trace("  Loading function begun");
         }

         if (this.jsFunction == null) {
            this.jsFunction = this.generateJsFunction();
         }

         String javascriptFileName = this.formatJavascriptFileName();
         if (log.isTraceEnabled()) {
            log.trace("  Loading js function '" + javascriptFileName + "'");
         }

         this.javascript = this.readJavascriptFile(javascriptFileName);
         if (log.isTraceEnabled()) {
            log.trace("  Loading javascript function completed");
         }

      }
   }

   private String readJavascriptFile(String javascriptFileName) {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      if (classLoader == null) {
         classLoader = this.getClass().getClassLoader();
      }

      InputStream is = classLoader.getResourceAsStream(javascriptFileName);
      if (is == null) {
         is = this.getClass().getResourceAsStream(javascriptFileName);
      }

      if (is == null) {
         log.debug("  Unable to read javascript name " + javascriptFileName);
         return null;
      } else {
         StringBuffer buffer = new StringBuffer();
         BufferedReader reader = new BufferedReader(new InputStreamReader(is));

         String function;
         try {
            function = null;

            while((function = reader.readLine()) != null) {
               buffer.append(function + "\n");
            }
         } catch (IOException var16) {
            log.error("Error reading javascript file.", var16);
         } finally {
            try {
               reader.close();
            } catch (IOException var15) {
               log.error("Error closing stream to javascript file.", var15);
            }

         }

         function = buffer.toString();
         return function.equals("") ? null : function;
      }
   }

   private String formatJavascriptFileName() {
      String name = this.jsFunction.substring(1);
      if (!this.jsFunction.startsWith("/")) {
         name = this.jsFunction.replace('.', '/') + ".js";
      }

      return name;
   }

   private boolean javascriptAlreadyLoaded() {
      return this.javascript != null;
   }

   private String generateJsFunction() {
      StringBuffer jsName = new StringBuffer("org.apache.commons.validator.javascript");
      jsName.append(".validate");
      jsName.append(this.name.substring(0, 1).toUpperCase());
      jsName.append(this.name.substring(1, this.name.length()));
      return jsName.toString();
   }

   public boolean isDependency(String validatorName) {
      return this.dependencyList.contains(validatorName);
   }

   public List getDependencyList() {
      return Collections.unmodifiableList(this.dependencyList);
   }

   public String toString() {
      StringBuffer results = new StringBuffer("ValidatorAction: ");
      results.append(this.name);
      results.append("\n");
      return results.toString();
   }

   boolean executeValidationMethod(Field field, Map params, ValidatorResults results, int pos) throws ValidatorException {
      params.put("org.apache.commons.validator.ValidatorAction", this);

      try {
         ClassLoader loader = this.getClassLoader(params);
         this.loadValidationClass(loader);
         this.loadParameterClasses(loader);
         this.loadValidationMethod();
         Object[] paramValues = this.getParameterValues(params);
         if (field.isIndexed()) {
            this.handleIndexedField(field, pos, paramValues);
         }

         Object result = null;

         try {
            result = this.validationMethod.invoke(this.getValidationClassInstance(), paramValues);
         } catch (IllegalArgumentException var11) {
            throw new ValidatorException(var11.getMessage());
         } catch (IllegalAccessException var12) {
            throw new ValidatorException(var12.getMessage());
         } catch (InvocationTargetException var13) {
            if (var13.getTargetException() instanceof Exception) {
               throw (Exception)var13.getTargetException();
            }

            if (var13.getTargetException() instanceof Error) {
               throw (Error)var13.getTargetException();
            }
         }

         boolean valid = this.isValid(result);
         if (!valid || valid && !this.onlyReturnErrors(params)) {
            results.add(field, this.name, valid, result);
         }

         return valid;
      } catch (Exception var14) {
         if (var14 instanceof ValidatorException) {
            throw (ValidatorException)var14;
         } else {
            log.error("Unhandled exception thrown during validation: " + var14.getMessage(), var14);
            results.add(field, this.name, false);
            return false;
         }
      }
   }

   private void loadValidationMethod() throws ValidatorException {
      if (this.validationMethod == null) {
         try {
            this.validationMethod = this.validationClass.getMethod(this.method, this.parameterClasses);
         } catch (NoSuchMethodException var2) {
            throw new ValidatorException("No such validation method: " + var2.getMessage());
         }
      }
   }

   private void loadValidationClass(ClassLoader loader) throws ValidatorException {
      if (this.validationClass == null) {
         try {
            this.validationClass = loader.loadClass(this.classname);
         } catch (ClassNotFoundException var3) {
            throw new ValidatorException(var3.getMessage());
         }
      }
   }

   private void loadParameterClasses(ClassLoader loader) throws ValidatorException {
      if (this.parameterClasses == null) {
         this.parameterClasses = new Class[this.methodParameterList.size()];

         for(int i = 0; i < this.methodParameterList.size(); ++i) {
            String paramClassName = (String)this.methodParameterList.get(i);

            try {
               this.parameterClasses[i] = loader.loadClass(paramClassName);
            } catch (ClassNotFoundException var5) {
               throw new ValidatorException(var5.getMessage());
            }
         }

      }
   }

   private Object[] getParameterValues(Map params) {
      Object[] paramValue = new Object[this.methodParameterList.size()];

      for(int i = 0; i < this.methodParameterList.size(); ++i) {
         String paramClassName = (String)this.methodParameterList.get(i);
         paramValue[i] = params.get(paramClassName);
      }

      return paramValue;
   }

   private Object getValidationClassInstance() throws ValidatorException {
      if (Modifier.isStatic(this.validationMethod.getModifiers())) {
         this.instance = null;
      } else if (this.instance == null) {
         try {
            this.instance = this.validationClass.newInstance();
         } catch (InstantiationException var4) {
            String msg = "Couldn't create instance of " + this.classname + ".  " + var4.getMessage();
            throw new ValidatorException(msg);
         } catch (IllegalAccessException var5) {
            String msg = "Couldn't create instance of " + this.classname + ".  " + var5.getMessage();
            throw new ValidatorException(msg);
         }
      }

      return this.instance;
   }

   private void handleIndexedField(Field field, int pos, Object[] paramValues) throws ValidatorException {
      int beanIndex = this.methodParameterList.indexOf("java.lang.Object");
      int fieldIndex = this.methodParameterList.indexOf("org.apache.commons.validator.Field");
      Object[] indexedList = field.getIndexedProperty(paramValues[beanIndex]);
      paramValues[beanIndex] = indexedList[pos];
      Field indexedField = (Field)field.clone();
      indexedField.setKey(ValidatorUtils.replace(indexedField.getKey(), "[]", "[" + pos + "]"));
      paramValues[fieldIndex] = indexedField;
   }

   private boolean isValid(Object result) {
      if (result instanceof Boolean) {
         Boolean valid = (Boolean)result;
         return valid;
      } else {
         return result != null;
      }
   }

   private ClassLoader getClassLoader(Map params) {
      Validator v = (Validator)params.get("org.apache.commons.validator.Validator");
      return v.getClassLoader();
   }

   private boolean onlyReturnErrors(Map params) {
      Validator v = (Validator)params.get("org.apache.commons.validator.Validator");
      return v.getOnlyReturnErrors();
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      log = LogFactory.getLog(class$org$apache$commons$validator$ValidatorAction == null ? (class$org$apache$commons$validator$ValidatorAction = class$("org.apache.commons.validator.ValidatorAction")) : class$org$apache$commons$validator$ValidatorAction);
   }
}

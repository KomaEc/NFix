package groovy.util;

import groovy.lang.GroovyObjectSupport;
import groovy.lang.GroovyRuntimeException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.management.Attribute;
import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class GroovyMBean extends GroovyObjectSupport {
   private final MBeanServerConnection server;
   private final ObjectName name;
   private MBeanInfo beanInfo;
   private final boolean ignoreErrors;
   private final Map operations;

   public GroovyMBean(MBeanServerConnection server, String objectName) throws JMException, IOException {
      this(server, objectName, false);
   }

   public GroovyMBean(MBeanServerConnection server, String objectName, boolean ignoreErrors) throws JMException, IOException {
      this(server, new ObjectName(objectName), ignoreErrors);
   }

   public GroovyMBean(MBeanServerConnection server, ObjectName name) throws JMException, IOException {
      this(server, name, false);
   }

   public GroovyMBean(MBeanServerConnection server, ObjectName name, boolean ignoreErrors) throws JMException, IOException {
      this.operations = new HashMap();
      this.server = server;
      this.name = name;
      this.ignoreErrors = ignoreErrors;
      this.beanInfo = server.getMBeanInfo(name);
      MBeanOperationInfo[] operationInfos = this.beanInfo.getOperations();

      for(int i = 0; i < operationInfos.length; ++i) {
         MBeanOperationInfo info = operationInfos[i];
         String[] signature = this.createSignature(info);
         String operationKey = this.createOperationKey(info.getName(), signature.length);
         this.operations.put(operationKey, signature);
      }

   }

   public MBeanServerConnection server() {
      return this.server;
   }

   public ObjectName name() {
      return this.name;
   }

   public MBeanInfo info() {
      return this.beanInfo;
   }

   public Object getProperty(String property) {
      try {
         return this.server.getAttribute(this.name, property);
      } catch (MBeanException var3) {
         this.throwExceptionWithTarget("Could not access property: " + property + ". Reason: ", var3);
      } catch (Exception var4) {
         if (!this.ignoreErrors) {
            this.throwException("Could not access property: " + property + ". Reason: ", var4);
         }
      }

      return null;
   }

   public void setProperty(String property, Object value) {
      try {
         this.server.setAttribute(this.name, new Attribute(property, value));
      } catch (MBeanException var4) {
         this.throwExceptionWithTarget("Could not set property: " + property + ". Reason: ", var4);
      } catch (Exception var5) {
         this.throwException("Could not set property: " + property + ". Reason: ", var5);
      }

   }

   public Object invokeMethod(String method, Object arguments) {
      Object[] argArray = null;
      if (arguments instanceof Object[]) {
         argArray = (Object[])((Object[])arguments);
      } else {
         argArray = new Object[]{arguments};
      }

      String operationKey = this.createOperationKey(method, argArray.length);
      String[] signature = (String[])((String[])this.operations.get(operationKey));
      if (signature != null) {
         try {
            return this.server.invoke(this.name, method, argArray, signature);
         } catch (MBeanException var7) {
            this.throwExceptionWithTarget("Could not invoke method: " + method + ". Reason: ", var7);
         } catch (Exception var8) {
            this.throwException("Could not invoke method: " + method + ". Reason: ", var8);
         }

         return null;
      } else {
         return super.invokeMethod(method, arguments);
      }
   }

   protected String[] createSignature(MBeanOperationInfo info) {
      MBeanParameterInfo[] params = info.getSignature();
      String[] answer = new String[params.length];

      for(int i = 0; i < params.length; ++i) {
         answer[i] = params[i].getType();
      }

      return answer;
   }

   protected String createOperationKey(String operation, int params) {
      return operation + "_" + params;
   }

   public Collection listAttributeNames() {
      ArrayList list = new ArrayList();

      try {
         MBeanAttributeInfo[] attrs = this.beanInfo.getAttributes();

         for(int i = 0; i < attrs.length; ++i) {
            MBeanAttributeInfo attr = attrs[i];
            list.add(attr.getName());
         }
      } catch (Exception var5) {
         this.throwException("Could not list attribute names. Reason: ", var5);
      }

      return list;
   }

   public List listAttributeValues() {
      List list = new ArrayList();
      Collection names = this.listAttributeNames();
      Iterator iterator = names.iterator();

      while(iterator.hasNext()) {
         String name = (String)iterator.next();

         try {
            Object val = this.getProperty(name);
            if (val != null) {
               list.add(name + " : " + val.toString());
            }
         } catch (Exception var6) {
            this.throwException("Could not list attribute values. Reason: ", var6);
         }
      }

      return list;
   }

   public Collection listAttributeDescriptions() {
      ArrayList list = new ArrayList();

      try {
         MBeanAttributeInfo[] attrs = this.beanInfo.getAttributes();

         for(int i = 0; i < attrs.length; ++i) {
            MBeanAttributeInfo attr = attrs[i];
            list.add(this.describeAttribute(attr));
         }
      } catch (Exception var5) {
         this.throwException("Could not list attribute descriptions. Reason: ", var5);
      }

      return list;
   }

   protected String describeAttribute(MBeanAttributeInfo attr) {
      StringBuffer buf = new StringBuffer();
      buf.append("(");
      if (attr.isReadable()) {
         buf.append("r");
      }

      if (attr.isWritable()) {
         buf.append("w");
      }

      buf.append(") ").append(attr.getType()).append(" ").append(attr.getName());
      return buf.toString();
   }

   public String describeAttribute(String attributeName) {
      String ret = "Attribute not found";

      try {
         MBeanAttributeInfo[] attributes = this.beanInfo.getAttributes();

         for(int i = 0; i < attributes.length; ++i) {
            MBeanAttributeInfo attribute = attributes[i];
            if (attribute.getName().equals(attributeName)) {
               return this.describeAttribute(attribute);
            }
         }
      } catch (Exception var6) {
         this.throwException("Could not describe attribute '" + attributeName + "'. Reason: ", var6);
      }

      return ret;
   }

   public Collection listOperationNames() {
      ArrayList list = new ArrayList();

      try {
         MBeanOperationInfo[] operations = this.beanInfo.getOperations();

         for(int i = 0; i < operations.length; ++i) {
            MBeanOperationInfo operation = operations[i];
            list.add(operation.getName());
         }
      } catch (Exception var5) {
         this.throwException("Could not list operation names. Reason: ", var5);
      }

      return list;
   }

   public Collection listOperationDescriptions() {
      ArrayList list = new ArrayList();

      try {
         MBeanOperationInfo[] operations = this.beanInfo.getOperations();

         for(int i = 0; i < operations.length; ++i) {
            MBeanOperationInfo operation = operations[i];
            list.add(this.describeOperation(operation));
         }
      } catch (Exception var5) {
         this.throwException("Could not list operation descriptions. Reason: ", var5);
      }

      return list;
   }

   public List describeOperation(String operationName) {
      ArrayList list = new ArrayList();

      try {
         MBeanOperationInfo[] operations = this.beanInfo.getOperations();

         for(int i = 0; i < operations.length; ++i) {
            MBeanOperationInfo operation = operations[i];
            if (operation.getName().equals(operationName)) {
               list.add(this.describeOperation(operation));
            }
         }
      } catch (Exception var6) {
         this.throwException("Could not describe operations matching name '" + operationName + "'. Reason: ", var6);
      }

      return list;
   }

   protected String describeOperation(MBeanOperationInfo operation) {
      StringBuffer buf = new StringBuffer();
      buf.append(operation.getReturnType()).append(" ").append(operation.getName()).append("(");
      MBeanParameterInfo[] params = operation.getSignature();

      for(int j = 0; j < params.length; ++j) {
         MBeanParameterInfo param = params[j];
         if (j != 0) {
            buf.append(", ");
         }

         buf.append(param.getType()).append(" ").append(param.getName());
      }

      buf.append(")");
      return buf.toString();
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append("MBean Name:").append("\n  ").append(this.name.getCanonicalName()).append("\n  ");
      Iterator iterator;
      if (!this.listAttributeDescriptions().isEmpty()) {
         buf.append("\nAttributes:");
         iterator = this.listAttributeDescriptions().iterator();

         while(iterator.hasNext()) {
            buf.append("\n  ").append((String)iterator.next());
         }
      }

      if (!this.listOperationDescriptions().isEmpty()) {
         buf.append("\nOperations:");
         iterator = this.listOperationDescriptions().iterator();

         while(iterator.hasNext()) {
            buf.append("\n  ").append((String)iterator.next());
         }
      }

      return buf.toString();
   }

   private void throwException(String m, Exception e) {
      if (!this.ignoreErrors) {
         throw new GroovyRuntimeException(m + e, e);
      }
   }

   private void throwExceptionWithTarget(String m, MBeanException e) {
      if (!this.ignoreErrors) {
         throw new GroovyRuntimeException(m + e, e.getTargetException());
      }
   }
}

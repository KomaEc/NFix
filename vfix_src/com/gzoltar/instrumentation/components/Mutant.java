package com.gzoltar.instrumentation.components;

import com.gzoltar.instrumentation.testing.TestResult;
import com.gzoltar.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import com.gzoltar.shaded.org.apache.commons.lang3.builder.HashCodeBuilder;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Mutant implements Serializable {
   private static final long serialVersionUID = -5056775092470979747L;
   private int id;
   private String packageName;
   private String className;
   private String methodName;
   private int lineNumber;
   private String operator;
   private String description;
   private String directory;
   private Map<String, TestResult> testResults = new HashMap();

   public Mutant() {
   }

   public Mutant(Integer var1, String var2, String var3, String var4, Integer var5, String var6, String var7, String var8) {
      this.id = var1;
      this.packageName = var2 == null ? "" : var2;
      this.className = var3;
      if (var4.equals("<init>")) {
         this.methodName = var3;
      } else if (var4.equals("<clinit>")) {
         this.methodName = "";
      } else {
         this.methodName = var4;
      }

      this.lineNumber = var5;
      this.operator = var6;
      this.description = var7;
      this.directory = var8;
   }

   public int getId() {
      return this.id;
   }

   public String getFullClassName() {
      return this.packageName.isEmpty() ? this.className : this.packageName + "." + this.className;
   }

   public String getJavaFile() {
      return this.packageName.isEmpty() ? (this.className.contains("$") ? this.className.substring(0, this.className.indexOf("$")) : this.className) + ".java" : (this.packageName + ".").replace(".", System.getProperty("file.separator")) + this.className + ".java";
   }

   public String getClassFile() {
      return this.packageName.isEmpty() ? this.className + ".class" : (this.packageName + ".").replace(".", System.getProperty("file.separator")) + this.className + ".class";
   }

   public String getDirectory() {
      return this.directory;
   }

   public String getComponentName() {
      return this.packageName + "[" + (this.className.contains("$") ? this.className.substring(0, this.className.indexOf("$")) : this.className) + ".java<" + (this.packageName.isEmpty() ? this.className : this.packageName + "." + this.className) + (this.methodName.equals("") ? "" : "{" + this.methodName) + "#" + this.lineNumber;
   }

   public void addTest(TestResult var1) {
      this.testResults.put(var1.getName(), var1);
   }

   public void addTests(List<TestResult> var1) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         TestResult var2 = (TestResult)var3.next();
         this.addTest(var2);
      }

   }

   public TestResult getTestResult(String var1) {
      return (TestResult)this.testResults.get(var1);
   }

   public Map<String, TestResult> getTestResults() {
      return this.testResults;
   }

   public int hashCode() {
      HashCodeBuilder var1;
      (var1 = new HashCodeBuilder()).append(this.id);
      var1.append((Object)this.getComponentName());
      var1.append((Object)this.operator);
      var1.append((Object)this.description);
      return var1.toHashCode();
   }

   public boolean equals(Object var1) {
      if (var1 instanceof Mutant) {
         Mutant var3 = (Mutant)var1;
         EqualsBuilder var2;
         (var2 = new EqualsBuilder()).append(this.id, var3.id);
         var2.append((Object)this.getComponentName(), (Object)var3.getComponentName());
         var2.append((Object)this.operator, (Object)var3.operator);
         var2.append((Object)this.description, (Object)var3.description);
         return var2.isEquals();
      } else {
         return false;
      }
   }

   public String toString() {
      return this.getId() + " : " + this.getComponentName();
   }
}

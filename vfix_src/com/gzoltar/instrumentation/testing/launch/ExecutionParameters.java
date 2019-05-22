package com.gzoltar.instrumentation.testing.launch;

import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.components.Component;
import java.io.Serializable;

public class ExecutionParameters implements Serializable {
   private static final long serialVersionUID = -7431786397274258151L;
   private final String classPath;
   private final String[] targetPackages;
   private final String[] targetClasses;
   private final String[] testPackages;
   private final String[] testClasses;
   private final String javaAgentPath;
   private final String runtimeJarPath;
   private final Logger.LogLevel logLevel;
   private final Boolean isInstrumentTestClasses;
   private final Boolean isInstrumentDeprecatedClasses;
   private final Boolean isInstrumentDeprecatedMethods;
   private final Boolean uniqueLineNumbers;
   private final Component.Granularity granularity;
   private final Integer testCaseTimeout;
   private Integer mutantID = -1;

   public ExecutionParameters(String var1, String[] var2, String[] var3, String[] var4, String[] var5, String var6, String var7, Logger.LogLevel var8, Boolean var9, Boolean var10, Boolean var11, Boolean var12, Component.Granularity var13, Integer var14) {
      this.classPath = var1;
      this.targetPackages = var2;
      this.targetClasses = var3;
      this.testPackages = var4;
      this.testClasses = var5;
      this.javaAgentPath = var6;
      this.runtimeJarPath = var7;
      this.logLevel = var8;
      this.isInstrumentTestClasses = var9;
      this.isInstrumentDeprecatedClasses = var10;
      this.isInstrumentDeprecatedMethods = var11;
      this.uniqueLineNumbers = var12;
      this.granularity = var13;
      this.testCaseTimeout = var14;
   }

   public String getClassPath() {
      return this.classPath;
   }

   public String[] getTargetPackages() {
      return this.targetPackages;
   }

   public String[] getTargetClasses() {
      return this.targetClasses;
   }

   public String[] getTestPackages() {
      return this.testPackages;
   }

   public String[] getTestClasses() {
      return this.testClasses;
   }

   public String getJavaAgentPath() {
      return this.javaAgentPath;
   }

   public String getRuntimeJarPath() {
      return this.runtimeJarPath;
   }

   public Logger.LogLevel getLogLevel() {
      return this.logLevel;
   }

   public Boolean isInstrumentTestClasses() {
      return this.isInstrumentTestClasses;
   }

   public Boolean isInstrumentDeprecatedClasses() {
      return this.isInstrumentDeprecatedClasses;
   }

   public Boolean isInstrumentDeprecatedMethods() {
      return this.isInstrumentDeprecatedMethods;
   }

   public Boolean uniqueLineNumbers() {
      return this.uniqueLineNumbers;
   }

   public Component.Granularity getGranularity() {
      return this.granularity;
   }

   public Integer getTestCaseTimeout() {
      return this.testCaseTimeout;
   }

   public Integer getMutantID() {
      return this.mutantID;
   }

   public void setMutantID(Integer var1) {
      this.mutantID = var1;
   }
}

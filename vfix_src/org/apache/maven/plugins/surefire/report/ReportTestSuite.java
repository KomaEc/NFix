package org.apache.maven.plugins.surefire.report;

import java.util.ArrayList;
import java.util.List;

public class ReportTestSuite {
   private List<ReportTestCase> testCases = new ArrayList();
   private int numberOfErrors;
   private int numberOfFailures;
   private int numberOfSkipped;
   private Integer numberOfTests;
   private String name;
   private String fullClassName;
   private String packageName;
   private float timeElapsed;

   public List<ReportTestCase> getTestCases() {
      return this.testCases;
   }

   public int getNumberOfErrors() {
      return this.numberOfErrors;
   }

   public void setNumberOfErrors(int numberOfErrors) {
      this.numberOfErrors = numberOfErrors;
   }

   public int getNumberOfFailures() {
      return this.numberOfFailures;
   }

   public void setNumberOfFailures(int numberOfFailures) {
      this.numberOfFailures = numberOfFailures;
   }

   public int getNumberOfSkipped() {
      return this.numberOfSkipped;
   }

   public void setNumberOfSkipped(int numberOfSkipped) {
      this.numberOfSkipped = numberOfSkipped;
   }

   public int getNumberOfTests() {
      if (this.numberOfTests != null) {
         return this.numberOfTests;
      } else {
         return this.testCases != null ? this.testCases.size() : 0;
      }
   }

   public void setNumberOfTests(int numberOfTests) {
      this.numberOfTests = numberOfTests;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getFullClassName() {
      return this.fullClassName;
   }

   public void setFullClassName(String fullClassName) {
      this.fullClassName = fullClassName;
      int lastDotPosition = fullClassName.lastIndexOf(".");
      this.name = fullClassName.substring(lastDotPosition + 1, fullClassName.length());
      if (lastDotPosition < 0) {
         this.packageName = "";
      } else {
         this.packageName = fullClassName.substring(0, lastDotPosition);
      }

   }

   public String getPackageName() {
      return this.packageName;
   }

   public void setPackageName(String packageName) {
      this.packageName = packageName;
   }

   public float getTimeElapsed() {
      return this.timeElapsed;
   }

   public void setTimeElapsed(float timeElapsed) {
      this.timeElapsed = timeElapsed;
   }

   public void setTestCases(List<ReportTestCase> testCases) {
      this.testCases = testCases;
   }

   public String toString() {
      return this.fullClassName + " [" + this.getNumberOfTests() + "/" + this.getNumberOfFailures() + "/" + this.getNumberOfErrors() + "/" + this.getNumberOfSkipped() + "]";
   }
}

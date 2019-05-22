package org.apache.maven.plugins.surefire.report;

import java.util.HashMap;
import java.util.Map;

public class ReportTestCase {
   private String fullClassName;
   private String className;
   private String fullName;
   private String name;
   private float time;
   private Map<String, Object> failure;

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getFullClassName() {
      return this.fullClassName;
   }

   public void setFullClassName(String name) {
      this.fullClassName = name;
   }

   public String getClassName() {
      return this.className;
   }

   public void setClassName(String name) {
      this.className = name;
   }

   public float getTime() {
      return this.time;
   }

   public void setTime(float time) {
      this.time = time;
   }

   public Map<String, Object> getFailure() {
      return this.failure;
   }

   public String getFullName() {
      return this.fullName;
   }

   public void setFullName(String fullName) {
      this.fullName = fullName;
   }

   public void addFailure(String message, String type) {
      this.failure = new HashMap();
      this.failure.put("message", message);
      this.failure.put("type", type);
   }

   public String toString() {
      return this.fullName;
   }
}

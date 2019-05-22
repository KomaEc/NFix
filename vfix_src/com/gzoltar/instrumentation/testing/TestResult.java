package com.gzoltar.instrumentation.testing;

import com.gzoltar.instrumentation.components.Component;
import com.gzoltar.instrumentation.components.ComponentCount;
import com.gzoltar.shaded.org.apache.commons.lang3.builder.CompareToBuilder;
import com.gzoltar.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import com.gzoltar.shaded.org.apache.commons.lang3.builder.HashCodeBuilder;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TestResult implements Serializable, Comparable<TestResult> {
   private static final long serialVersionUID = 9169514619158982488L;
   private String name = null;
   private boolean successful = true;
   private long runtime = 0L;
   private String trace = null;
   private Set<String> coveredComponents = new HashSet();
   private Map<String, int[]> probesCoverage = null;

   public TestResult(String var1) {
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public boolean wasSuccessful() {
      return this.successful;
   }

   public void setSuccessful(boolean var1) {
      this.successful = var1;
   }

   public long getRuntime() {
      return this.runtime;
   }

   public void setRuntime(long var1) {
      this.runtime = var1;
   }

   public String getTrace() {
      return this.trace;
   }

   public void setTrace(String var1) {
      this.trace = var1;
   }

   public Set<String> getCoveredComponents() {
      return this.coveredComponents;
   }

   public void setCoveredComponents(Set<String> var1) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         String var2 = (String)var3.next();
         this.coveredComponents.add(var2);
      }

   }

   public void removeCoveredComponent(ComponentCount var1) {
      this.removeCoveredComponent(var1.getComponent().getLabel());
   }

   public void removeCoveredComponent(String var1) {
      this.coveredComponents.remove(var1);
   }

   public Map<String, int[]> getProbesCoverage() {
      return this.probesCoverage;
   }

   public void setProbesCoverage(Map<String, int[]> var1) {
      this.probesCoverage = new HashMap(var1);
   }

   public boolean covers(ComponentCount var1) {
      return this.covers(var1.getComponent());
   }

   private boolean covers(Component var1) {
      return this.coveredComponents != null && !this.coveredComponents.isEmpty() ? this.coveredComponents.contains(var1.getLabel()) : false;
   }

   public int hashCode() {
      HashCodeBuilder var1;
      (var1 = new HashCodeBuilder()).append((Object)this.name);
      return var1.toHashCode();
   }

   public boolean equals(Object var1) {
      if (var1 instanceof TestResult) {
         TestResult var3 = (TestResult)var1;
         EqualsBuilder var2;
         (var2 = new EqualsBuilder()).append((Object)this.name, (Object)var3.name);
         return var2.isEquals();
      } else {
         return false;
      }
   }

   public int compareTo(TestResult var1) {
      if (var1 instanceof TestResult) {
         CompareToBuilder var2;
         (var2 = new CompareToBuilder()).append((Object)this.name, (Object)var1.name);
         return var2.toComparison();
      } else {
         return -1;
      }
   }
}

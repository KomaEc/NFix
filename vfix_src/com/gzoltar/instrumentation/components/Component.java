package com.gzoltar.instrumentation.components;

import com.gzoltar.instrumentation.testing.TestResult;
import com.gzoltar.shaded.org.apache.commons.lang3.builder.CompareToBuilder;
import com.gzoltar.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import com.gzoltar.shaded.org.apache.commons.lang3.builder.HashCodeBuilder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Component implements Serializable, Comparable<Component> {
   private static final long serialVersionUID = -3364702664487577856L;
   public static final String PACKAGE_SEPARATOR = "[";
   public static final String FILE_SEPARATOR = "<";
   public static final String CLASS_SEPARATOR = "{";
   public static final String METHOD_SEPARATOR = "#";
   private Map<String, Double> suspiciousness_values;
   protected String label = null;
   private Set<String> testResults = null;
   private List<Mutant> mutants = null;
   private int lineNumber;
   // $FF: synthetic field
   static final boolean $assertionsDisabled = !Component.class.desiredAssertionStatus();

   protected Component() {
   }

   public Component(String var1, int var2) {
      this.label = var1;
      this.lineNumber = var2;
      this.suspiciousness_values = new HashMap();
      this.testResults = new HashSet();
      this.mutants = new ArrayList();
   }

   public Component clone() {
      Component var1 = new Component();
      this.suspiciousness_values = new HashMap();
      var1.label = this.label;
      var1.testResults = new HashSet();
      var1.mutants = this.mutants;
      return var1;
   }

   public Map<String, Double> getSuspiciousnessValues() {
      return this.suspiciousness_values;
   }

   public Double getSuspiciousnessValue(String var1) {
      return (Double)this.suspiciousness_values.get(var1.toUpperCase());
   }

   public void setSuspiciousnessValues(Map<String, Double> var1) {
      this.suspiciousness_values = var1;
   }

   public void setSuspiciousnessValue(String var1, Double var2) {
      this.suspiciousness_values.put(var1.toUpperCase(), var2);
   }

   public String getLabel() {
      return this.label;
   }

   public String getClassLabel() {
      int var1 = this.label.indexOf("<") + 1;
      int var2 = this.label.contains("$") ? this.label.indexOf("$") : this.label.indexOf("{");
      return this.label.substring(var1, var2);
   }

   public String getLineNumberLabel() {
      int var1 = this.label.indexOf("#") + 1;
      return this.label.substring(var1, this.label.length());
   }

   public void addTest(TestResult var1) {
      this.testResults.add(var1.getName());
   }

   public boolean isCoveredBy(TestResult var1) {
      return this.testResults.contains(var1.getName());
   }

   public Set<String> getTestResults() {
      return this.testResults;
   }

   public boolean hasMutants() {
      return !this.mutants.isEmpty();
   }

   public void addMutant(Mutant var1) {
      this.mutants.add(var1);
   }

   public List<Mutant> getMutants() {
      return this.mutants;
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public void setLineNumber(int var1) {
      this.lineNumber = var1;
   }

   public int hashCode() {
      HashCodeBuilder var1;
      (var1 = new HashCodeBuilder()).append((Object)this.label);
      var1.append(this.lineNumber);
      return var1.toHashCode();
   }

   public boolean equals(Object var1) {
      if (var1 instanceof Component) {
         Component var3 = (Component)var1;
         EqualsBuilder var2;
         (var2 = new EqualsBuilder()).append((Object)this.label, (Object)var3.label);
         var2.append(this.lineNumber, var3.lineNumber);
         return var2.isEquals();
      } else {
         return false;
      }
   }

   public int compareTo(Component var1) {
      if (var1 instanceof Component) {
         CompareToBuilder var2;
         (var2 = new CompareToBuilder()).append((Object)this.label, (Object)var1.label);
         var2.append((Object)this.label, (Object)var1.lineNumber);
         return var2.toComparison();
      } else {
         return -1;
      }
   }

   public String toString(boolean var1, boolean var2) {
      String var3 = this.getLabel();
      Iterator var5;
      if (!var1) {
         int var6 = var3.indexOf("<");
         int var8 = var3.indexOf("{");
         var3 = var3.substring(var6 + 1, var8);
         var3 = var3 + "#" + this.lineNumber;
         String var7;
         if (var2) {
            for(var5 = this.suspiciousness_values.keySet().iterator(); var5.hasNext(); var3 = var3 + "," + String.format("%.32f", this.suspiciousness_values.get(var7))) {
               var7 = (String)var5.next();
            }
         }

         return var3;
      } else {
         String var4;
         if (var2) {
            for(var5 = this.suspiciousness_values.keySet().iterator(); var5.hasNext(); var3 = var3 + "," + String.format("%.32f", this.suspiciousness_values.get(var4))) {
               var4 = (String)var5.next();
            }
         }

         return var3;
      }
   }

   public static enum Granularity {
      STATEMENT,
      METHOD,
      CLASS;
   }
}

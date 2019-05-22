package com.gzoltar.instrumentation.spectra;

import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.components.Component;
import com.gzoltar.instrumentation.components.ComponentCount;
import com.gzoltar.instrumentation.testing.TestResult;
import com.gzoltar.instrumentation.utils.Utils;
import com.gzoltar.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import com.gzoltar.shaded.org.apache.commons.lang3.builder.HashCodeBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Spectra implements Serializable {
   private static final long serialVersionUID = -6952154560000931185L;
   private static transient Spectra instance = null;
   private Map<String, ComponentCount> components = null;
   private Map<String, TestResult> testResults = null;
   private transient Component.Granularity granularity;

   private Spectra() {
      this.granularity = Component.Granularity.STATEMENT;
      this.components = new HashMap();
      this.testResults = new HashMap();
   }

   public static Spectra getInstance() {
      if (instance == null) {
         instance = new Spectra();
      }

      return instance;
   }

   public Spectra clone() {
      Spectra var1 = new Spectra();
      Iterator var2 = this.components.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         var1.components.put(var3, ((ComponentCount)this.components.get(var3)).clone());
      }

      instance = var1;
      return var1;
   }

   public void setComponents(Map<String, ComponentCount> var1) {
      this.components = var1;
   }

   public void setComponentCounts(Collection<ComponentCount> var1) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         ComponentCount var2 = (ComponentCount)var3.next();
         this.registerComponent(var2);
      }

   }

   public Collection<ComponentCount> getComponents() {
      return this.components.values();
   }

   public Map<String, ComponentCount> getComponentsMap() {
      return this.components;
   }

   public List<ComponentCount> getComponentsOrderedBySuspiciousness(final String var1) {
      ArrayList var2;
      Collections.sort(var2 = new ArrayList(this.components.values()), new Comparator<ComponentCount>(this) {
         // $FF: synthetic method
         public final int compare(Object var1x, Object var2) {
            ComponentCount var10001 = (ComponentCount)var1x;
            ComponentCount var3 = (ComponentCount)var2;
            ComponentCount var5 = var10001;
            Component var6 = var5.getComponent();
            Component var8 = var3.getComponent();
            if (var6.getSuspiciousnessValue(var1) > var8.getSuspiciousnessValue(var1)) {
               return -1;
            } else if (var6.getSuspiciousnessValue(var1) < var8.getSuspiciousnessValue(var1)) {
               return 1;
            } else {
               int var4 = Integer.valueOf(var6.getLabel().substring(var6.getLabel().indexOf("#") + 1, var6.getLabel().length()));
               int var7 = Integer.valueOf(var8.getLabel().substring(var8.getLabel().indexOf("#") + 1, var8.getLabel().length()));
               return Integer.compare(var4, var7);
            }
         }
      });
      return var2;
   }

   public List<ComponentCount> getComponentsSortedByLineNumber() {
      ArrayList var1;
      Collections.sort(var1 = new ArrayList(this.components.values()), new Comparator<ComponentCount>(this) {
         // $FF: synthetic method
         public final int compare(Object var1, Object var2) {
            ComponentCount var10000 = (ComponentCount)var1;
            ComponentCount var4 = (ComponentCount)var2;
            int var3 = Integer.valueOf(var10000.getComponent().getLineNumberLabel());
            int var5 = Integer.valueOf(var4.getComponent().getLineNumberLabel());
            return Integer.compare(var3, var5);
         }
      });
      return var1;
   }

   public ComponentCount getComponentCount(String var1) {
      return (ComponentCount)this.components.get(var1);
   }

   public Component getComponent(String var1) {
      return ((ComponentCount)this.components.get(var1)).getComponent();
   }

   public int getNumberOfComponents() {
      return this.components.size();
   }

   public void registerComponent(ComponentCount var1) {
      this.components.put(var1.getComponent().getLabel(), var1);
   }

   public TestResult getTestResult(String var1) {
      return (TestResult)this.testResults.get(var1);
   }

   public Map<String, TestResult> getTestResultsMap() {
      return this.testResults;
   }

   public Set<String> getTestNames() {
      return this.testResults.keySet();
   }

   public Collection<TestResult> getTestResults() {
      ArrayList var1;
      (var1 = new ArrayList()).addAll(this.testResults.values());
      Collections.sort(var1);
      return var1;
   }

   public int getNumberOfTestResults() {
      return this.testResults.size();
   }

   public void addTestResult(TestResult var1) {
      this.testResults.put(var1.getName(), var1);
   }

   public void addTestResults(List<TestResult> var1) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         TestResult var2 = (TestResult)var3.next();
         this.addTestResult(var2);
      }

   }

   public boolean hasFailingTestCases() {
      Iterator var1 = this.getTestResults().iterator();

      do {
         if (!var1.hasNext()) {
            return false;
         }
      } while(((TestResult)var1.next()).wasSuccessful());

      return true;
   }

   public void setGranularity(Component.Granularity var1) {
      this.granularity = var1;
   }

   public Component.Granularity getGranularity() {
      return this.granularity;
   }

   public void normalizeSuspiciousnessValues(String var1) {
      List var2;
      if (!(var2 = this.getComponentsOrderedBySuspiciousness(var1)).isEmpty()) {
         if (var2.size() == 1) {
            if (((ComponentCount)var2.get(0)).getComponent().getSuspiciousnessValue(var1) > 0.0D) {
               ((ComponentCount)var2.get(0)).getComponent().setSuspiciousnessValue(var1, 1.0D);
            } else {
               if (((ComponentCount)var2.get(0)).getComponent().getSuspiciousnessValue(var1) < 0.0D) {
                  ((ComponentCount)var2.get(0)).getComponent().setSuspiciousnessValue(var1, 0.0D);
               }

            }
         } else {
            double var3 = ((ComponentCount)var2.get(0)).getComponent().getSuspiciousnessValue(var1);
            double var5;
            if ((var5 = ((ComponentCount)var2.get(var2.size() - 1)).getComponent().getSuspiciousnessValue(var1)) < 0.0D) {
               var5 = 0.0D;
            }

            ComponentCount var7;
            for(Iterator var8 = this.components.values().iterator(); var8.hasNext(); var7.getComponent().setSuspiciousnessValue(var1, Utils.normalize(var5, var3, var7.getComponent().getSuspiciousnessValue(var1)))) {
               if ((var7 = (ComponentCount)var8.next()).getComponent().getSuspiciousnessValue(var1) < 0.0D) {
                  var7.getComponent().setSuspiciousnessValue(var1, 0.0D);
               }
            }

         }
      }
   }

   public void printComponents(String var1, String var2, boolean var3, boolean var4) {
      if (this.components.isEmpty()) {
         Logger.getInstance().warn("No components to return");
         File var8;
         if ((var8 = new File(var1 + System.getProperty("file.separator") + var2)).exists()) {
            var8.delete();
         }

      } else {
         try {
            (new File(var1)).mkdirs();
            PrintWriter var7;
            (var7 = new PrintWriter(var1 + System.getProperty("file.separator") + var2, "UTF-8")).print("Component");
            if (var4) {
               ComponentCount var10000 = this.components.values().iterator().hasNext() ? (ComponentCount)this.components.values().iterator().next() : null;
               ComponentCount var9 = var10000;
               if (var10000 != null) {
                  Iterator var5 = var9.getComponent().getSuspiciousnessValues().keySet().iterator();

                  while(var5.hasNext()) {
                     var2 = (String)var5.next();
                     var7.print("," + var2);
                  }
               }
            }

            var7.println();
            Iterator var10 = this.getComponentsSortedByLineNumber().iterator();

            while(var10.hasNext()) {
               ComponentCount var11 = (ComponentCount)var10.next();
               var7.println(var11.getComponent().toString(var3, var4));
            }

            var7.close();
         } catch (Exception var6) {
            Logger.getInstance().err("", var6);
         }
      }
   }

   public void printTestCases(String var1, String var2) {
      if (this.testResults.isEmpty()) {
         Logger.getInstance().warn("No tests to return");
         File var6;
         if ((var6 = new File(var1 + System.getProperty("file.separator") + var2)).exists()) {
            var6.delete();
         }

      } else {
         try {
            (new File(var1)).mkdirs();
            PrintWriter var5;
            (var5 = new PrintWriter(var1 + System.getProperty("file.separator") + var2, "UTF-8")).println("Test,Pass-Fail,Runtime(ns)");
            Iterator var7 = this.getTestResults().iterator();

            while(var7.hasNext()) {
               TestResult var3 = (TestResult)var7.next();
               var5.println(var3.getName() + "," + (var3.wasSuccessful() ? "PASS" : "FAIL") + "," + var3.getRuntime());
            }

            var5.close();
         } catch (Exception var4) {
            Logger.getInstance().err("", var4);
         }
      }
   }

   public void serialize(String var1) {
      try {
         File var3;
         if ((var3 = new File(var1)).exists()) {
            var3.delete();
            var3.createNewFile();
         }

         ObjectOutputStream var4;
         (var4 = new ObjectOutputStream(new FileOutputStream(var3))).writeObject(this.components);
         var4.writeObject(this.testResults);
         var4.close();
      } catch (IOException var2) {
         Logger.getInstance().err("", var2);
      }
   }

   public void deserialize(String var1) {
      try {
         File var3;
         if ((var3 = new File(var1)).exists()) {
            ObjectInputStream var4 = new ObjectInputStream(new FileInputStream(var3));
            this.components = (Map)var4.readObject();
            this.testResults = (Map)var4.readObject();
            var4.close();
         }
      } catch (ClassNotFoundException | IOException var2) {
         Logger.getInstance().err("", var2);
      }
   }

   public int hashCode() {
      HashCodeBuilder var1;
      (var1 = new HashCodeBuilder()).append((Object)this.components);
      var1.append((Object)this.testResults);
      return var1.toHashCode();
   }

   public boolean equals(Object var1) {
      if (var1 instanceof Spectra) {
         Spectra var3 = (Spectra)var1;
         EqualsBuilder var2;
         (var2 = new EqualsBuilder()).append((Object)this.components, (Object)var3.components);
         var2.append((Object)this.testResults, (Object)var3.testResults);
         return var2.isEquals();
      } else {
         return false;
      }
   }
}

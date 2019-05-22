package com.gzoltar.client.rmi;

import com.gzoltar.client.statistics.OutputStatisticsVariable;
import com.gzoltar.client.statistics.StatisticsVariables;
import com.gzoltar.client.utils.ClassType;
import com.gzoltar.instrumentation.components.ComponentCount;
import com.gzoltar.instrumentation.spectra.Spectra;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response implements Serializable {
   private static final long serialVersionUID = -723421557681212058L;
   private List<String> listOfClasses = null;
   private Map<String, ClassType.Type> listOfTestsClasses = null;
   private List<ComponentCount> listOfComponents = null;
   private Spectra spectra = null;
   private Map<String, OutputStatisticsVariable> statistics = null;

   public void setListOfClasses(List<String> var1) {
      this.listOfClasses = var1;
   }

   public List<String> getListOfClasses() {
      return this.listOfClasses;
   }

   public void setListOfTestClasses(Map<String, ClassType.Type> var1) {
      this.listOfTestsClasses = var1;
   }

   public Map<String, ClassType.Type> getListOfTestClasses() {
      return this.listOfTestsClasses;
   }

   public void setListOfComponents(Collection<ComponentCount> var1) {
      if (this.listOfComponents == null) {
         this.listOfComponents = new ArrayList();
      }

      this.listOfComponents.addAll(var1);
   }

   public List<ComponentCount> getListOfComponents() {
      return this.listOfComponents;
   }

   public void setSpectra(Spectra var1) {
      this.spectra = var1;
   }

   public Spectra getSpectra() {
      return this.spectra;
   }

   private void addOutputStatisticsVariable(StatisticsVariables var1, Object var2) {
      if (this.statistics == null) {
         this.statistics = new HashMap();
      }

      OutputStatisticsVariable var3 = new OutputStatisticsVariable(var1.name(), var2);
      this.statistics.put(var1.name(), var3);
   }

   public void addOutputStatisticsVariable(StatisticsVariables var1, String var2) {
      this.addOutputStatisticsVariable(var1, (Object)var2);
   }

   public void addOutputStatisticsVariable(StatisticsVariables var1, Integer var2) {
      this.addOutputStatisticsVariable(var1, (Object)var2);
   }

   public void addOutputStatisticsVariable(StatisticsVariables var1, Double var2) {
      this.addOutputStatisticsVariable(var1, (Object)var2);
   }

   public Map<String, OutputStatisticsVariable> getStatistics() {
      return this.statistics;
   }
}

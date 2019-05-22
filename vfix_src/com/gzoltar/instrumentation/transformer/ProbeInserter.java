package com.gzoltar.instrumentation.transformer;

import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.components.Component;
import com.gzoltar.instrumentation.components.ComponentCount;
import com.gzoltar.instrumentation.runtime.RuntimeData;
import com.gzoltar.instrumentation.spectra.Spectra;
import com.gzoltar.shaded.javassist.CtClass;
import java.util.ArrayList;
import java.util.List;

public class ProbeInserter {
   private String className;
   private List<Component> probeStatements = new ArrayList();

   public ProbeInserter(String var1) {
      this.className = var1;
   }

   public int getNumProbes() {
      return this.probeStatements.size();
   }

   public List<Component> getProbeStatements() {
      return this.probeStatements;
   }

   public String getHitArrayDeclaration() {
      return "int[] $__gz_counters = null;";
   }

   public String getInitFieldCall() {
      return "$__gz_counters = " + RuntimeData.class.getCanonicalName() + ".getHitArray(\"" + this.className + "\"," + this.getNumProbes() + ");";
   }

   public String getInitMethodCall() {
      return "$__gz_init();";
   }

   public String getBodyInitMethod() {
      return "if ($__gz_counters == null) { $__gz_counters = " + RuntimeData.class.getCanonicalName() + ".getHitArray(\"" + this.className + "\"," + this.getNumProbes() + "); }";
   }

   public String getBodyGetResetCountersMethod() {
      return "public static int[] $__gz_get_and_reset_counters() { int[] counters = $__gz_counters;$__gz_counters = new int[$__gz_counters.length];return counters; }";
   }

   public int[] getAndClearCoverage() {
      return RuntimeData.getMapHitArrayAndClear(this.className);
   }

   public int newStatementProbe(CtClass var1, String var2, int var3) {
      StringBuilder var4;
      (var4 = new StringBuilder()).append(var1.getPackageName() == null ? "" : var1.getPackageName());
      var4.append("[");
      var4.append(var1.getClassFile().getSourceFile());
      var4.append("<");
      var4.append(var1.getName());
      if (Spectra.getInstance().getGranularity() == Component.Granularity.METHOD || Spectra.getInstance().getGranularity() == Component.Granularity.STATEMENT) {
         var4.append("{");
         var4.append(var2);
      }

      var4.append("#");
      var4.append(var3);

      int var5;
      for(var5 = 0; var5 < this.probeStatements.size(); ++var5) {
         if (((Component)this.probeStatements.get(var5)).getLabel().equals(var4.toString())) {
            return var5;
         }
      }

      var5 = this.probeStatements.size();
      if (Spectra.getInstance().getComponent(var4.toString()) == null) {
         Component var6 = new Component(var4.toString(), var3);
         Spectra.getInstance().registerComponent(new ComponentCount(var6, 0));
         this.probeStatements.add(var6);
      }

      Logger.getInstance().debug("          STATEMENT: " + var4);
      return var5;
   }
}

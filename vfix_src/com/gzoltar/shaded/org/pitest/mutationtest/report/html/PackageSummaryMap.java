package com.gzoltar.shaded.org.pitest.mutationtest.report.html;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class PackageSummaryMap {
   private final Map<String, PackageSummaryData> packageSummaryData = new TreeMap();

   private PackageSummaryData getPackageSummaryData(String packageName) {
      PackageSummaryData psData;
      if (this.packageSummaryData.containsKey(packageName)) {
         psData = (PackageSummaryData)this.packageSummaryData.get(packageName);
      } else {
         psData = new PackageSummaryData(packageName);
         this.packageSummaryData.put(packageName, psData);
      }

      return psData;
   }

   public PackageSummaryData update(String packageName, MutationTestSummaryData data) {
      PackageSummaryData psd = this.getPackageSummaryData(packageName);
      psd.addSummaryData(data);
      return psd;
   }

   public Collection<PackageSummaryData> values() {
      return this.packageSummaryData.values();
   }
}

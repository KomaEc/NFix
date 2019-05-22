package com.gzoltar.shaded.org.pitest.mutationtest.report.html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PackageSummaryData implements Comparable<PackageSummaryData> {
   private final String packageName;
   private final Map<String, MutationTestSummaryData> fileNameToSummaryData = new HashMap();

   public PackageSummaryData(String packageName) {
      this.packageName = packageName;
   }

   public void addSummaryData(MutationTestSummaryData data) {
      MutationTestSummaryData existing = (MutationTestSummaryData)this.fileNameToSummaryData.get(data.getFileName());
      if (existing == null) {
         this.fileNameToSummaryData.put(data.getFileName(), data);
      } else {
         existing.add(data);
      }

   }

   public MutationTestSummaryData getForSourceFile(String filename) {
      return (MutationTestSummaryData)this.fileNameToSummaryData.get(filename);
   }

   public MutationTotals getTotals() {
      MutationTotals mt = new MutationTotals();
      Iterator i$ = this.fileNameToSummaryData.values().iterator();

      while(i$.hasNext()) {
         MutationTestSummaryData each = (MutationTestSummaryData)i$.next();
         mt.add(each.getTotals());
      }

      return mt;
   }

   public String getPackageName() {
      return this.packageName;
   }

   public String getPackageDirectory() {
      return this.packageName;
   }

   public List<MutationTestSummaryData> getSummaryData() {
      ArrayList<MutationTestSummaryData> values = new ArrayList(this.fileNameToSummaryData.values());
      Collections.sort(values, new MutationTestSummaryDataFileNameComparator());
      return values;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.packageName == null ? 0 : this.packageName.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         PackageSummaryData other = (PackageSummaryData)obj;
         if (this.packageName == null) {
            if (other.packageName != null) {
               return false;
            }
         } else if (!this.packageName.equals(other.packageName)) {
            return false;
         }

         return true;
      }
   }

   public int compareTo(PackageSummaryData arg0) {
      return this.packageName.compareTo(arg0.packageName);
   }
}

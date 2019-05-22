package com.gzoltar.shaded.org.pitest.testapi;

import java.util.Collections;
import java.util.List;

public class TestGroupConfig {
   private final List<String> excludedGroups;
   private final List<String> includedGroups;

   public TestGroupConfig(List<String> excludedGroups, List<String> includedGroups) {
      this.excludedGroups = excludedGroups != null ? excludedGroups : Collections.emptyList();
      this.includedGroups = includedGroups != null ? includedGroups : Collections.emptyList();
   }

   public TestGroupConfig() {
      this((List)null, (List)null);
   }

   public List<String> getExcludedGroups() {
      return this.excludedGroups;
   }

   public List<String> getIncludedGroups() {
      return this.includedGroups;
   }

   public String toString() {
      return "TestGroupConfig [excludedGroups=" + this.excludedGroups + ", includedGroups=" + this.includedGroups + "]";
   }
}

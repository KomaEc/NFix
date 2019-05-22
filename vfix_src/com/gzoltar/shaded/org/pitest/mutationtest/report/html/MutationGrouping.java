package com.gzoltar.shaded.org.pitest.mutationtest.report.html;

import com.gzoltar.shaded.org.pitest.mutationtest.MutationResult;
import java.util.List;

public class MutationGrouping {
   private final int id;
   private final String title;
   private final List<MutationResult> mutations;

   public MutationGrouping(int id, String title, List<MutationResult> mutations) {
      this.title = title;
      this.mutations = mutations;
      this.id = id;
   }

   public String getTitle() {
      return this.title;
   }

   public List<MutationResult> getMutations() {
      return this.mutations;
   }

   public int getId() {
      return this.id;
   }
}

package com.gzoltar.shaded.org.pitest.mutationtest.report.html;

import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.mutationtest.DetectionStatus;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResult;
import java.util.Collections;
import java.util.List;

public class Line {
   private final long number;
   private final String text;
   private final LineStatus lineCovered;
   private final List<MutationResult> mutations;

   public Line(long number, String text, LineStatus lineCovered, List<MutationResult> mutations) {
      this.number = number;
      this.text = text;
      this.lineCovered = lineCovered;
      this.mutations = mutations;
      Collections.sort(mutations, new ResultComparator());
   }

   public long getNumber() {
      return this.number;
   }

   public String getText() {
      return this.text;
   }

   public LineStatus getLineCovered() {
      return this.lineCovered;
   }

   public List<MutationResult> getMutations() {
      return this.mutations;
   }

   public Option<DetectionStatus> detectionStatus() {
      return (Option)(this.mutations.isEmpty() ? Option.none() : Option.some(((MutationResult)this.mutations.get(0)).getStatus()));
   }

   public int getNumberOfMutations() {
      return this.mutations.size();
   }

   public String getNumberOfMutationsForDisplay() {
      return this.getNumberOfMutations() > 0 ? "" + this.getNumberOfMutations() : "";
   }

   public LineStyle getStyles() {
      return new LineStyle(this);
   }
}

package com.gzoltar.shaded.org.pitest.mutationtest.report.html;

import com.gzoltar.shaded.org.pitest.mutationtest.DetectionStatus;

public class LineStyle {
   private final Line line;

   public LineStyle(Line line) {
      this.line = line;
   }

   public String getLineCoverage() {
      switch(this.line.getLineCovered()) {
      case Covered:
         return "covered";
      case NotCovered:
         return "uncovered";
      default:
         return "na";
      }
   }

   public String getCode() {
      switch(this.line.getLineCovered()) {
      case Covered:
         return "covered";
      case NotCovered:
         return "uncovered";
      default:
         return "";
      }
   }

   public String getMutation() {
      if (this.line.detectionStatus().hasNone()) {
         return "";
      } else {
         DetectionStatus status = (DetectionStatus)this.line.detectionStatus().value();
         if (!status.isDetected()) {
            return "survived";
         } else {
            return ConfidenceMap.hasHighConfidence(status) ? "killed" : "uncertain";
         }
      }
   }

   public String getText() {
      return this.getLineCoverage();
   }
}

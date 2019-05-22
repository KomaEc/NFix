package com.gzoltar.shaded.org.pitest.mutationtest.report.html;

public class MutationTotals {
   private long numberOfFiles = 0L;
   private long numberOfLines = 0L;
   private long numberOfLinesCovered = 0L;
   private long numberOfMutations = 0L;
   private long numberOfMutationsDetected = 0L;

   public long getNumberOfFiles() {
      return this.numberOfFiles;
   }

   public void addFiles(long files) {
      this.numberOfFiles += files;
   }

   public long getNumberOfLines() {
      return this.numberOfLines;
   }

   public void addLines(long lines) {
      this.numberOfLines += lines;
   }

   public long getNumberOfLinesCovered() {
      return this.numberOfLinesCovered;
   }

   public void addLinesCovered(long linesCovered) {
      this.numberOfLinesCovered += linesCovered;
   }

   public long getNumberOfMutations() {
      return this.numberOfMutations;
   }

   public void addMutations(long mutations) {
      this.numberOfMutations += mutations;
   }

   public long getNumberOfMutationsDetected() {
      return this.numberOfMutationsDetected;
   }

   public void addMutationsDetetcted(long mutationsKilled) {
      this.numberOfMutationsDetected += mutationsKilled;
   }

   public int getLineCoverage() {
      return this.numberOfLines == 0L ? 100 : Math.round(100.0F * (float)this.numberOfLinesCovered / (float)this.numberOfLines);
   }

   public int getMutationCoverage() {
      return this.numberOfMutations == 0L ? 100 : Math.round(100.0F * (float)this.numberOfMutationsDetected / (float)this.numberOfMutations);
   }

   public void add(MutationTotals data) {
      this.add(data.getNumberOfLines(), data.getNumberOfFiles(), data);
   }

   private void add(long lines, long files, MutationTotals data) {
      this.addFiles(files);
      this.addLines(lines);
      this.addLinesCovered(data.getNumberOfLinesCovered());
      this.addMutations(data.getNumberOfMutations());
      this.addMutationsDetetcted(data.getNumberOfMutationsDetected());
   }
}

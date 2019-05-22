package soot.jimple.spark.ondemand.genericutil;

public class Averager {
   private double curAverage;
   private long numSamples;

   public void addSample(double sample) {
      this.curAverage = (this.curAverage * (double)this.numSamples + sample) / (double)(this.numSamples + 1L);
      ++this.numSamples;
   }

   public double getCurAverage() {
      return this.curAverage;
   }

   public long getNumSamples() {
      return this.numSamples;
   }
}

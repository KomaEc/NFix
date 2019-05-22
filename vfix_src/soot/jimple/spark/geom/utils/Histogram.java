package soot.jimple.spark.geom.utils;

import java.io.PrintStream;

public class Histogram {
   private int[] limits;
   private int count = 0;
   private int[] results = null;

   public Histogram(int[] limits) {
      this.limits = limits;
      this.results = new int[limits.length + 1];

      for(int i = 0; i <= limits.length; ++i) {
         this.results[i] = 0;
      }

   }

   public void printResult(PrintStream output) {
      if (this.count == 0) {
         output.println("No samples are inserted, no output!");
      } else {
         output.println("Samples : " + this.count);

         for(int i = 0; i < this.results.length; ++i) {
            if (i == 0) {
               output.print("<=" + this.limits[0] + ": " + this.results[i]);
            } else if (i == this.results.length - 1) {
               output.print(">" + this.limits[this.limits.length - 1] + ": " + this.results[i]);
            } else {
               output.print(this.limits[i - 1] + "< x <=" + this.limits[i] + ": " + this.results[i]);
            }

            output.printf(", percentage = %.2f\n", (double)this.results[i] * 100.0D / (double)this.count);
         }

      }
   }

   public void printResult(PrintStream output, String title) {
      output.println(title);
      this.printResult(output);
   }

   public void printResult(PrintStream output, String title, Histogram other) {
      output.println(title);
      if (this.count == 0) {
         output.println("No samples are inserted, no output!");
      } else {
         output.println("Samples : " + this.count + " (" + other.count + ")");

         for(int i = 0; i < this.results.length; ++i) {
            if (i == 0) {
               output.printf("<= %d: %d (%d)", this.limits[0], this.results[i], other.results[i]);
            } else if (i == this.results.length - 1) {
               output.printf("> %d: %d (%d)", this.limits[this.limits.length - 1], this.results[i], other.results[i]);
            } else {
               output.printf("%d < x <= %d: %d (%d)", this.limits[i - 1], this.limits[i], this.results[i], other.results[i]);
            }

            output.printf(", percentage = %.2f%% (%.2f%%) \n", (double)this.results[i] * 100.0D / (double)this.count, (double)other.results[i] * 100.0D / (double)other.count);
         }

         output.println();
      }
   }

   public void addNumber(int num) {
      ++this.count;
      int i = false;

      int var10002;
      int i;
      for(i = 0; i < this.limits.length; ++i) {
         if (num <= this.limits[i]) {
            var10002 = this.results[i]++;
            break;
         }
      }

      if (i == this.limits.length) {
         var10002 = this.results[i]++;
      }

   }

   public void merge(Histogram other) {
      for(int i = 0; i <= this.limits.length; ++i) {
         int[] var10000 = this.results;
         var10000[i] += other.results[i];
      }

      this.count += other.count;
   }

   public int getTotalNumofSamples() {
      return this.count;
   }

   public void scaleToSamples(int usrSamples) {
      double ratio = (double)usrSamples / (double)this.count;
      this.count = 0;

      for(int i = 0; i <= this.limits.length; ++i) {
         this.results[i] = (int)Math.round((double)this.results[i] * ratio);
         this.count += this.results[i];
      }

   }

   public int getResult(int inx) {
      return inx >= this.limits.length ? 0 : this.results[inx];
   }
}

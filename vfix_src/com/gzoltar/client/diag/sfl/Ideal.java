package com.gzoltar.client.diag.sfl;

public class Ideal implements SFL {
   public double compute(double var1, double var3, double var5, double var7) {
      return var3 > 0.0D ? 0.0D : var7 / (var7 + var5);
   }
}

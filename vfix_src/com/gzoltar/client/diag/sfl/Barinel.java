package com.gzoltar.client.diag.sfl;

public class Barinel implements SFL {
   public double compute(double var1, double var3, double var5, double var7) {
      double var9;
      return var5 + var7 == 0.0D ? 0.0D : Math.pow(var9 = var5 / (var5 + var7), var5) * Math.pow(1.0D - var9, 11.0D);
   }
}

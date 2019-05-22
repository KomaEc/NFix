package com.gzoltar.client.diag.sfl;

public class Tarantula implements SFL {
   public double compute(double var1, double var3, double var5, double var7) {
      double var9 = var7 + var3 == 0.0D ? 0.0D : var7 / (var7 + var3);
      double var11 = var5 + var1 == 0.0D ? 0.0D : var5 / (var5 + var1);
      return var9 + var11 == 0.0D ? 0.0D : var9 / (var9 + var11);
   }
}

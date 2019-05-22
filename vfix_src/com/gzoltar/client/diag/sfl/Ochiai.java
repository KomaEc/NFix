package com.gzoltar.client.diag.sfl;

public class Ochiai implements SFL {
   public double compute(double var1, double var3, double var5, double var7) {
      return var7 + var5 != 0.0D && var7 + var3 != 0.0D ? var7 / Math.sqrt((var7 + var3) * (var7 + var5)) : 0.0D;
   }
}

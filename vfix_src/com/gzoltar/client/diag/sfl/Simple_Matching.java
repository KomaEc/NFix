package com.gzoltar.client.diag.sfl;

public class Simple_Matching implements SFL {
   public double compute(double var1, double var3, double var5, double var7) {
      return var7 + var3 + var5 + var1 == 0.0D ? 0.0D : (var7 + var1) / (var7 + var3 + var5 + var1);
   }
}

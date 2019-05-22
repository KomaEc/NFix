package com.gzoltar.client.diag.sfl;

import com.gzoltar.client.Properties;

public class DStar implements SFL {
   public double compute(double var1, double var3, double var5, double var7) {
      return var5 + var3 != 0.0D && var7 != 0.0D ? Math.pow(var7, (double)Properties.STAR) / (var5 + var3) : 0.0D;
   }
}

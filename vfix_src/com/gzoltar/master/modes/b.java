package com.gzoltar.master.modes;

final class b implements Comparable<b> {
   public final String a;
   public final String b;
   public final String c;
   public final String d;

   public b(String var1, String var2, String var3, String var4) {
      this.a = var1;
      this.b = var2.contains("$") ? var2.substring(var2.indexOf("$") + 1, var2.length()) : var2;
      this.c = var3;
      this.d = var4;
   }
}

package com.gzoltar.shaded.jline;

import com.gzoltar.shaded.jline.internal.Log;

public class OSvTerminal extends TerminalSupport {
   public Class<?> sttyClass = null;
   public Object stty = null;

   public OSvTerminal() {
      super(true);
      this.setAnsiSupported(true);

      try {
         if (this.stty == null) {
            this.sttyClass = Class.forName("com.cloudius.util.Stty");
            this.stty = this.sttyClass.newInstance();
         }
      } catch (Exception var2) {
         Log.warn("Failed to load com.cloudius.util.Stty", var2);
      }

   }

   public void init() throws Exception {
      super.init();
      if (this.stty != null) {
         this.sttyClass.getMethod("com.gzoltar.shaded.jlineMode").invoke(this.stty);
      }

   }

   public void restore() throws Exception {
      if (this.stty != null) {
         this.sttyClass.getMethod("reset").invoke(this.stty);
      }

      super.restore();
      System.out.println();
   }
}

package com.gzoltar.master.modes;

import com.gzoltar.instrumentation.Logger;
import java.util.TimerTask;

final class a extends TimerTask {
   // $FF: synthetic field
   private Process a;

   a(Process var1) {
      this.a = var1;
      super();
   }

   public final void run() {
      this.a.destroy();
      Logger.getInstance().err("Process terminated - timeout");
   }
}

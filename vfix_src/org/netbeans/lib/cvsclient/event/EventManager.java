package org.netbeans.lib.cvsclient.event;

import java.io.File;
import org.netbeans.lib.cvsclient.ClientServices;

public class EventManager {
   private CVSListener[] listeners;
   private boolean fireEnhancedEventSet = true;
   private final ClientServices services;

   public EventManager(ClientServices var1) {
      this.services = var1;
   }

   public ClientServices getClientServices() {
      return this.services;
   }

   public synchronized void addCVSListener(CVSListener var1) {
      if (this.listeners != null && this.listeners.length != 0) {
         CVSListener[] var2 = new CVSListener[this.listeners.length + 1];

         for(int var3 = 0; var3 < this.listeners.length; ++var3) {
            var2[var3] = this.listeners[var3];
         }

         this.listeners = var2;
      } else {
         this.listeners = new CVSListener[1];
      }

      this.listeners[this.listeners.length - 1] = var1;
   }

   public synchronized void removeCVSListener(CVSListener var1) {
      if (this.listeners.length == 1) {
         this.listeners = null;
      } else {
         CVSListener[] var2 = new CVSListener[this.listeners.length - 1];

         label24:
         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (this.listeners[var3] == var1) {
               int var4 = var3 + 1;

               while(true) {
                  if (var4 >= this.listeners.length) {
                     break label24;
                  }

                  var2[var4 - 1] = this.listeners[var4];
                  ++var4;
               }
            }

            var2[var3] = this.listeners[var3];
         }

         this.listeners = var2;
      }

   }

   public void fireCVSEvent(CVSEvent var1) {
      if (this.listeners != null && this.listeners.length != 0) {
         File var2;
         if (var1 instanceof FileInfoEvent) {
            var2 = ((FileInfoEvent)var1).getInfoContainer().getFile();
            if (this.services.getGlobalOptions().isExcluded(var2)) {
               return;
            }
         }

         var2 = null;
         CVSListener[] var6;
         synchronized(this.listeners) {
            var6 = new CVSListener[this.listeners.length];
            System.arraycopy(this.listeners, 0, var6, 0, var6.length);
         }

         for(int var3 = 0; var3 < var6.length; ++var3) {
            var1.fireEvent(var6[var3]);
         }

      }
   }

   public boolean isFireEnhancedEventSet() {
      return this.fireEnhancedEventSet;
   }

   public void setFireEnhancedEventSet(boolean var1) {
      this.fireEnhancedEventSet = var1;
   }
}

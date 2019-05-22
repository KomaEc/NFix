package org.netbeans.lib.cvsclient.command.checkout;

import java.util.StringTokenizer;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.FileInfoEvent;

public class ModuleListBuilder implements Builder {
   private ModuleListInformation moduleInformation;
   private final EventManager eventManager;
   private final CheckoutCommand checkoutCommand;

   public ModuleListBuilder(EventManager var1, CheckoutCommand var2) {
      this.eventManager = var1;
      this.checkoutCommand = var2;
   }

   public void outputDone() {
      if (this.moduleInformation != null) {
         this.eventManager.fireCVSEvent(new FileInfoEvent(this, this.moduleInformation));
         this.moduleInformation = null;
      }

   }

   public void parseLine(String var1, boolean var2) {
      var1 = var1.replace('\t', ' ');
      if (!var1.startsWith(" ")) {
         this.processModule(var1, true);
      } else {
         this.processModule(var1, false);
      }

   }

   protected void processModule(String var1, boolean var2) {
      StringTokenizer var3 = new StringTokenizer(var1, " ", false);
      String var4;
      if (var2) {
         this.outputDone();
         this.moduleInformation = new ModuleListInformation();
         var4 = var3.nextToken();
         this.moduleInformation.setModuleName(var4);
         if (this.checkoutCommand.isShowModulesWithStatus()) {
            String var5 = var3.nextToken();
            this.moduleInformation.setModuleStatus(var5);
         }
      }

      while(var3.hasMoreTokens()) {
         var4 = var3.nextToken();
         if (var4.startsWith("-")) {
            this.moduleInformation.setType(var4);
         } else {
            this.moduleInformation.addPath(var4);
         }
      }

   }

   public void parseEnhancedMessage(String var1, Object var2) {
   }
}

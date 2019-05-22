package org.netbeans.lib.cvsclient.commandLine.command;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.ResourceBundle;

abstract class AbstractCommandProvider implements CommandProvider {
   public String getName() {
      String var1 = this.getClass().getName();
      int var2 = var1.lastIndexOf(46);
      return var2 > 0 ? var1.substring(var2 + 1) : var1;
   }

   public String getUsage() {
      return ResourceBundle.getBundle(CommandProvider.class.getPackage().getName() + ".Bundle").getString(this.getName() + ".usage");
   }

   public void printShortDescription(PrintStream var1) {
      String var2 = ResourceBundle.getBundle(CommandProvider.class.getPackage().getName() + ".Bundle").getString(this.getName() + ".shortDescription");
      var1.print(var2);
   }

   public void printLongDescription(PrintStream var1) {
      String var2 = ResourceBundle.getBundle(CommandProvider.class.getPackage().getName() + ".Bundle").getString(this.getName() + ".longDescription");
      var1.println(MessageFormat.format(var2, this.getUsage()));
   }
}

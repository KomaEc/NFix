package org.netbeans.lib.cvsclient.command.checkout;

import java.io.File;
import org.netbeans.lib.cvsclient.command.FileInfoContainer;

public class ModuleListInformation extends FileInfoContainer {
   private String moduleName;
   private String moduleStatus;
   private final StringBuffer paths = new StringBuffer();
   private String type;

   public String getModuleName() {
      return this.moduleName;
   }

   public void setModuleName(String var1) {
      this.moduleName = var1;
   }

   public String getModuleStatus() {
      return this.moduleStatus;
   }

   public void setModuleStatus(String var1) {
      this.moduleStatus = var1;
   }

   public String getPaths() {
      return this.paths.toString();
   }

   public void addPath(String var1) {
      if (this.paths.length() > 0) {
         this.paths.append(' ');
      }

      this.paths.append(var1);
   }

   public File getFile() {
      return null;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String var1) {
      this.type = var1;
   }
}

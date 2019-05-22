package org.netbeans.lib.cvsclient.event;

public class ModuleExpansionEvent extends CVSEvent {
   private String module;

   public ModuleExpansionEvent(Object var1, String var2) {
      super(var1);
      this.module = var2;
   }

   public String getModule() {
      return this.module;
   }

   protected void fireEvent(CVSListener var1) {
      var1.moduleExpanded(this);
   }
}

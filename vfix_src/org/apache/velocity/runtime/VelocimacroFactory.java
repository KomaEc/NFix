package org.apache.velocity.runtime;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.directive.Macro;
import org.apache.velocity.runtime.directive.VelocimacroProxy;
import org.apache.velocity.runtime.log.LogDisplayWrapper;

public class VelocimacroFactory {
   private final RuntimeServices rsvc;
   private final LogDisplayWrapper log;
   private VelocimacroManager vmManager = null;
   private boolean replaceAllowed = false;
   private boolean addNewAllowed = true;
   private boolean templateLocal = false;
   private boolean autoReloadLibrary = false;
   private Vector macroLibVec = null;
   private Map libModMap;

   public VelocimacroFactory(RuntimeServices rsvc) {
      this.rsvc = rsvc;
      this.log = new LogDisplayWrapper(rsvc.getLog(), "Velocimacro : ", rsvc.getBoolean("velocimacro.messages.on", true));
      this.libModMap = new HashMap();
      this.vmManager = new VelocimacroManager(rsvc);
   }

   public void initVelocimacro() {
      synchronized(this) {
         this.log.trace("initialization starting.");
         this.setReplacementPermission(true);
         this.vmManager.setNamespaceUsage(false);
         Object libfiles = this.rsvc.getProperty("velocimacro.library");
         if (libfiles == null) {
            this.log.debug("\"velocimacro.library\" is not set.  Trying default library: VM_global_library.vm");
            if (this.rsvc.getLoaderNameForResource("VM_global_library.vm") != null) {
               libfiles = "VM_global_library.vm";
            } else {
               this.log.debug("Default library not found.");
            }
         }

         if (libfiles != null) {
            if (libfiles instanceof Vector) {
               this.macroLibVec = (Vector)libfiles;
            } else if (libfiles instanceof String) {
               this.macroLibVec = new Vector();
               this.macroLibVec.addElement(libfiles);
            }

            for(int i = 0; i < this.macroLibVec.size(); ++i) {
               String lib = (String)this.macroLibVec.elementAt(i);
               if (StringUtils.isNotEmpty(lib)) {
                  this.vmManager.setRegisterFromLib(true);
                  this.log.debug("adding VMs from VM library : " + lib);

                  try {
                     Template template = this.rsvc.getTemplate(lib);
                     VelocimacroFactory.Twonk twonk = new VelocimacroFactory.Twonk();
                     twonk.template = template;
                     twonk.modificationTime = template.getLastModified();
                     this.libModMap.put(lib, twonk);
                  } catch (Exception var8) {
                     this.log.error(true, "Velocimacro : Error using VM library : " + lib, var8);
                  }

                  this.log.trace("VM library registration complete.");
                  this.vmManager.setRegisterFromLib(false);
               }
            }
         }

         this.setAddMacroPermission(true);
         if (!this.rsvc.getBoolean("velocimacro.permissions.allow.inline", true)) {
            this.setAddMacroPermission(false);
            this.log.info("allowInline = false : VMs can NOT be defined inline in templates");
         } else {
            this.log.debug("allowInline = true : VMs can be defined inline in templates");
         }

         this.setReplacementPermission(false);
         if (this.rsvc.getBoolean("velocimacro.permissions.allow.inline.to.replace.global", false)) {
            this.setReplacementPermission(true);
            this.log.info("allowInlineToOverride = true : VMs defined inline may replace previous VM definitions");
         } else {
            this.log.debug("allowInlineToOverride = false : VMs defined inline may NOT replace previous VM definitions");
         }

         this.vmManager.setNamespaceUsage(true);
         this.setTemplateLocalInline(this.rsvc.getBoolean("velocimacro.permissions.allow.inline.local.scope", false));
         if (this.getTemplateLocalInline()) {
            this.log.info("allowInlineLocal = true : VMs defined inline will be local to their defining template only.");
         } else {
            this.log.debug("allowInlineLocal = false : VMs defined inline will be global in scope if allowed.");
         }

         this.vmManager.setTemplateLocalInlineVM(this.getTemplateLocalInline());
         this.setAutoload(this.rsvc.getBoolean("velocimacro.library.autoreload", false));
         if (this.getAutoload()) {
            this.log.info("autoload on : VM system will automatically reload global library macros");
         } else {
            this.log.debug("autoload off : VM system will not automatically reload global library macros");
         }

         this.log.trace("Velocimacro : initialization complete.");
      }
   }

   public boolean addVelocimacro(String name, String macroBody, String[] argArray, String sourceTemplate) {
      if (name != null && macroBody != null && argArray != null && sourceTemplate != null) {
         if (!this.canAddVelocimacro(name, sourceTemplate)) {
            return false;
         } else {
            synchronized(this) {
               this.vmManager.addVM(name, macroBody, argArray, sourceTemplate);
            }

            StringBuffer msg = new StringBuffer("added ");
            Macro.macroToString(msg, argArray);
            msg.append(" : source = ").append(sourceTemplate);
            this.log.info(msg.toString());
            return true;
         }
      } else {
         this.log.warn("VM addition rejected : programmer error : arg null");
         return false;
      }
   }

   private synchronized boolean canAddVelocimacro(String name, String sourceTemplate) {
      if (this.getAutoload() && this.macroLibVec != null) {
         for(int i = 0; i < this.macroLibVec.size(); ++i) {
            String lib = (String)this.macroLibVec.elementAt(i);
            if (lib.equals(sourceTemplate)) {
               return true;
            }
         }
      }

      if (!this.addNewAllowed) {
         this.log.warn("VM addition rejected : " + name + " : inline VMs not allowed.");
         return false;
      } else if (!this.templateLocal && this.isVelocimacro(name, sourceTemplate) && !this.replaceAllowed) {
         this.log.warn("VM addition rejected : " + name + " : inline not allowed to replace existing VM");
         return false;
      } else {
         return true;
      }
   }

   public boolean isVelocimacro(String vm, String sourceTemplate) {
      synchronized(this) {
         return this.vmManager.get(vm, sourceTemplate) != null;
      }
   }

   public Directive getVelocimacro(String vmName, String sourceTemplate) {
      VelocimacroProxy vp = null;
      synchronized(this) {
         vp = this.vmManager.get(vmName, sourceTemplate);
         if (vp != null && this.getAutoload()) {
            String lib = this.vmManager.getLibraryName(vmName, sourceTemplate);
            if (lib != null) {
               try {
                  VelocimacroFactory.Twonk tw = (VelocimacroFactory.Twonk)this.libModMap.get(lib);
                  if (tw != null) {
                     Template template = tw.template;
                     long tt = tw.modificationTime;
                     long ft = template.getResourceLoader().getLastModified(template);
                     if (ft > tt) {
                        this.log.debug("auto-reloading VMs from VM library : " + lib);
                        tw.modificationTime = ft;
                        template = this.rsvc.getTemplate(lib);
                        tw.template = template;
                        tw.modificationTime = template.getLastModified();
                     }
                  }
               } catch (Exception var13) {
                  this.log.error(true, "Velocimacro : Error using VM library : " + lib, var13);
               }

               vp = this.vmManager.get(vmName, sourceTemplate);
            }
         }

         return vp;
      }
   }

   public boolean dumpVMNamespace(String namespace) {
      return this.vmManager.dumpNamespace(namespace);
   }

   private void setTemplateLocalInline(boolean b) {
      this.templateLocal = b;
   }

   private boolean getTemplateLocalInline() {
      return this.templateLocal;
   }

   private boolean setAddMacroPermission(boolean addNewAllowed) {
      boolean b = this.addNewAllowed;
      this.addNewAllowed = addNewAllowed;
      return b;
   }

   private boolean setReplacementPermission(boolean arg) {
      boolean b = this.replaceAllowed;
      this.replaceAllowed = arg;
      return b;
   }

   private void setAutoload(boolean b) {
      this.autoReloadLibrary = b;
   }

   private boolean getAutoload() {
      return this.autoReloadLibrary;
   }

   private static class Twonk {
      public Template template;
      public long modificationTime;

      private Twonk() {
      }

      // $FF: synthetic method
      Twonk(Object x0) {
         this();
      }
   }
}

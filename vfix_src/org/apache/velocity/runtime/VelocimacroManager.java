package org.apache.velocity.runtime;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Hashtable;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.directive.VelocimacroProxy;
import org.apache.velocity.runtime.parser.node.SimpleNode;

public class VelocimacroManager {
   private final RuntimeServices rsvc;
   private static String GLOBAL_NAMESPACE = "";
   private boolean registerFromLib = false;
   private final Hashtable namespaceHash = new Hashtable();
   private final Hashtable libraryMap = new Hashtable();
   private boolean namespacesOn = true;
   private boolean inlineLocalMode = false;

   VelocimacroManager(RuntimeServices rsvc) {
      this.rsvc = rsvc;
      this.addNamespace(GLOBAL_NAMESPACE);
   }

   public boolean addVM(String vmName, String macroBody, String[] argArray, String namespace) {
      VelocimacroManager.MacroEntry me = new VelocimacroManager.MacroEntry(vmName, macroBody, argArray, namespace);
      me.setFromLibrary(this.registerFromLib);
      boolean isLib = true;
      if (this.registerFromLib) {
         this.libraryMap.put(namespace, namespace);
      } else {
         isLib = this.libraryMap.containsKey(namespace);
      }

      if (!isLib && this.usingNamespaces(namespace)) {
         Hashtable local = this.getNamespace(namespace, true);
         local.put(vmName, me);
         return true;
      } else {
         VelocimacroManager.MacroEntry exist = (VelocimacroManager.MacroEntry)this.getNamespace(GLOBAL_NAMESPACE).get(vmName);
         if (exist != null) {
            me.setFromLibrary(exist.getFromLibrary());
         }

         this.getNamespace(GLOBAL_NAMESPACE).put(vmName, me);
         return true;
      }
   }

   public VelocimacroProxy get(String vmName, String namespace) {
      if (this.usingNamespaces(namespace)) {
         Hashtable local = this.getNamespace(namespace, false);
         if (local != null) {
            VelocimacroManager.MacroEntry me = (VelocimacroManager.MacroEntry)local.get(vmName);
            if (me != null) {
               return me.createVelocimacro(namespace);
            }
         }
      }

      VelocimacroManager.MacroEntry me = (VelocimacroManager.MacroEntry)this.getNamespace(GLOBAL_NAMESPACE).get(vmName);
      return me != null ? me.createVelocimacro(namespace) : null;
   }

   public boolean dumpNamespace(String namespace) {
      synchronized(this) {
         if (this.usingNamespaces(namespace)) {
            Hashtable h = (Hashtable)this.namespaceHash.remove(namespace);
            if (h == null) {
               return false;
            } else {
               h.clear();
               return true;
            }
         } else {
            return false;
         }
      }
   }

   public void setNamespaceUsage(boolean namespaceOn) {
      this.namespacesOn = namespaceOn;
   }

   public void setRegisterFromLib(boolean registerFromLib) {
      this.registerFromLib = registerFromLib;
   }

   public void setTemplateLocalInlineVM(boolean inlineLocalMode) {
      this.inlineLocalMode = inlineLocalMode;
   }

   private Hashtable getNamespace(String namespace) {
      return this.getNamespace(namespace, false);
   }

   private Hashtable getNamespace(String namespace, boolean addIfNew) {
      Hashtable h = (Hashtable)this.namespaceHash.get(namespace);
      if (h == null && addIfNew) {
         h = this.addNamespace(namespace);
      }

      return h;
   }

   private Hashtable addNamespace(String namespace) {
      Hashtable h = new Hashtable();
      Object oh;
      if ((oh = this.namespaceHash.put(namespace, h)) != null) {
         this.namespaceHash.put(namespace, oh);
         return null;
      } else {
         return h;
      }
   }

   private boolean usingNamespaces(String namespace) {
      if (!this.namespacesOn) {
         return false;
      } else {
         return this.inlineLocalMode;
      }
   }

   public String getLibraryName(String vmName, String namespace) {
      if (this.usingNamespaces(namespace)) {
         Hashtable local = this.getNamespace(namespace, false);
         if (local != null) {
            VelocimacroManager.MacroEntry me = (VelocimacroManager.MacroEntry)local.get(vmName);
            if (me != null) {
               return null;
            }
         }
      }

      VelocimacroManager.MacroEntry me = (VelocimacroManager.MacroEntry)this.getNamespace(GLOBAL_NAMESPACE).get(vmName);
      return me != null ? me.getSourceTemplate() : null;
   }

   private class MacroEntry {
      private final String vmName;
      private final String[] argArray;
      private final String macroBody;
      private final String sourceTemplate;
      private SimpleNode nodeTree;
      private boolean fromLibrary;

      private MacroEntry(String vmName, String macroBody, String[] argArray, String sourceTemplate) {
         this.nodeTree = null;
         this.fromLibrary = false;
         this.vmName = vmName;
         this.argArray = argArray;
         this.macroBody = macroBody;
         this.sourceTemplate = sourceTemplate;
      }

      public void setFromLibrary(boolean fromLibrary) {
         this.fromLibrary = fromLibrary;
      }

      public boolean getFromLibrary() {
         return this.fromLibrary;
      }

      public SimpleNode getNodeTree() {
         return this.nodeTree;
      }

      public String getSourceTemplate() {
         return this.sourceTemplate;
      }

      VelocimacroProxy createVelocimacro(String namespace) {
         VelocimacroProxy vp = new VelocimacroProxy();
         vp.setName(this.vmName);
         vp.setArgArray(this.argArray);
         vp.setMacrobody(this.macroBody);
         vp.setNodeTree(this.nodeTree);
         vp.setNamespace(namespace);
         return vp;
      }

      void setup(InternalContextAdapter ica) {
         if (this.nodeTree == null) {
            this.parseTree(ica);
         }

      }

      void parseTree(InternalContextAdapter ica) {
         try {
            BufferedReader br = new BufferedReader(new StringReader(this.macroBody));
            this.nodeTree = VelocimacroManager.this.rsvc.parse(br, "VM:" + this.vmName, true);
            this.nodeTree.init(ica, (Object)null);
         } catch (Exception var3) {
            VelocimacroManager.this.rsvc.getLog().error("VelocimacroManager.parseTree() failed on VM '" + this.vmName + "'", var3);
         }

      }

      // $FF: synthetic method
      MacroEntry(String x1, String x2, String[] x3, String x4, Object x5) {
         this(x1, x2, x3, x4);
      }
   }
}

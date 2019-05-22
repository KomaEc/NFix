package com.mks.api.commands.ide;

import com.mks.api.CmdRunnerCreator;
import com.mks.api.commands.SICommands;
import com.mks.api.response.APIException;
import com.mks.api.response.Field;
import com.mks.api.response.InvalidCommandOptionException;
import com.mks.api.response.Item;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;
import com.mks.api.response.impl.SimpleResponseFactory;
import com.mks.api.response.modifiable.ModifiableField;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public final class SandboxInfo implements ISandboxInfo, Item {
   private Map fields;
   private String typeInfo;

   private SandboxInfo(String aSandboxName, String host, String port, String aType, String aSIProjectName, String aTypeInfo, String parent, boolean isPending, String configPath) {
      Boolean isVariant = Boolean.FALSE;
      Boolean isBuild = Boolean.FALSE;
      Boolean isNormal = Boolean.FALSE;
      if ("variant".equals(aType)) {
         isVariant = Boolean.TRUE;
      } else if ("build".equals(aType)) {
         isBuild = Boolean.TRUE;
      } else {
         isNormal = Boolean.TRUE;
      }

      this.fields = new LinkedHashMap();
      this.fields.put("sandboxName", aSandboxName);
      this.fields.put("sandboxParent", parent);
      this.fields.put("server", host);
      this.fields.put("serverPort", port);
      this.fields.put("projectName", new File(aSIProjectName));
      this.fields.put("isVariant", isVariant);
      this.fields.put("isBuild", isBuild);
      this.fields.put("isNormal", isNormal);
      this.fields.put("isPending", new Boolean(isPending));
      this.fields.put("isSubsandbox", new Boolean(parent != null));
      this.fields.put("fullConfigSyntax", configPath);
      this.fields.put("developmentPath", this.isVariant() ? aTypeInfo : null);
      this.typeInfo = aTypeInfo;
   }

   public String getSandboxName() {
      return (String)this.fields.get("sandboxName");
   }

   public File getSandboxFile() {
      return new File(this.getSandboxName());
   }

   public File getSandboxLocation() {
      return this.getSandboxFile().getParentFile();
   }

   public String getHostname() {
      return (String)this.fields.get("server");
   }

   public String getPort() {
      return (String)this.fields.get("serverPort");
   }

   public boolean isVariant() {
      return (Boolean)this.fields.get("isVariant");
   }

   public boolean isBuild() {
      return (Boolean)this.fields.get("isBuild");
   }

   public File getProject() {
      return (File)this.fields.get("projectName");
   }

   public String getTypeInfo() {
      return this.typeInfo;
   }

   public boolean isSubsandbox() {
      return (Boolean)this.fields.get("isSubsandbox");
   }

   public String getParentName() {
      return (String)this.fields.get("sandboxParent");
   }

   public boolean isPending() {
      return (Boolean)this.fields.get("isPending");
   }

   public boolean isRelatedTo(CmdRunnerCreator session, File sandboxRoot) throws APIException {
      if (this.getSandboxFile().equals(sandboxRoot)) {
         return true;
      } else {
         return this.getParentName() == null ? false : getSandboxInfo(session, new File(this.getParentName())).isRelatedTo(session, sandboxRoot);
      }
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof SandboxInfo)) {
         return false;
      } else {
         SandboxInfo sbx = (SandboxInfo)obj;
         if (!this.getSandboxName().equals(sbx.getSandboxName())) {
            return false;
         } else if (!this.getHostname().equals(sbx.getHostname())) {
            return false;
         } else if (!this.getPort().equals(sbx.getPort())) {
            return false;
         } else if (!this.getProject().equals(sbx.getProject())) {
            return false;
         } else {
            if (this.typeInfo == null) {
               if (sbx.typeInfo != null) {
                  return false;
               }
            } else if (!this.typeInfo.equals(sbx.typeInfo)) {
               return false;
            }

            if (this.getParentName() == null) {
               if (sbx.getParentName() != null) {
                  return false;
               }
            } else if (!this.getParentName().equals(sbx.getParentName())) {
               return false;
            }

            return true;
         }
      }
   }

   public int hashCode() {
      return this.getSandboxName().hashCode();
   }

   public String getConfigPath() {
      return (String)this.fields.get("fullConfigSyntax");
   }

   public String getDevPath() {
      return (String)this.fields.get("developmentPath");
   }

   public String getContext() {
      return this.getParentName();
   }

   public String getContext(String key) {
      return null;
   }

   public Enumeration getContextKeys() {
      return new Enumeration() {
         public boolean hasMoreElements() {
            return false;
         }

         public Object nextElement() {
            return null;
         }
      };
   }

   public String getDisplayId() {
      return null;
   }

   public String getId() {
      return this.getSandboxName();
   }

   public String getModelType() {
      return "si.SandboxInfo";
   }

   public boolean contains(String id) {
      return this.fields.containsKey(id);
   }

   public Field getField(String id) {
      if (!this.contains(id)) {
         throw new NoSuchElementException();
      } else {
         SimpleResponseFactory factory = SimpleResponseFactory.getResponseFactory();
         ModifiableField field = factory.createField(id);
         field.setValue(this.fields.get(id));
         return field;
      }
   }

   public int getFieldListSize() {
      return this.fields.size();
   }

   public Iterator getFields() {
      if (this.fields.isEmpty()) {
         return Collections.EMPTY_LIST.iterator();
      } else {
         List itemFields = new ArrayList(this.fields.size());
         Iterator ids = this.fields.keySet().iterator();

         while(ids.hasNext()) {
            itemFields.add(this.getField((String)ids.next()));
         }

         return itemFields.iterator();
      }
   }

   private static SandboxInfo construct(Response r) throws APIException {
      WorkItem currentWorkItem = r.getWorkItems().next();
      String sandboxName = currentWorkItem.getField("sandboxName").getValueAsString().replace('\\', '/');
      String server = currentWorkItem.getField("server").getValueAsString();
      String serverPort = currentWorkItem.getField("serverPort").getValueAsString();
      String siProjectName = currentWorkItem.getField("projectName").getValueAsString();
      String parentSandbox = currentWorkItem.getField("sandboxParent").getValueAsString();
      String configPath = currentWorkItem.getField("fullConfigSyntax").getValueAsString();
      String pendingStatus = currentWorkItem.getField("pendingType").getValueAsString();
      boolean isPending = false;
      if (pendingStatus != null && pendingStatus.indexOf("pending") >= 0) {
         isPending = true;
      }

      String type = "normal";
      String typeInfo = "";
      String projectType = currentWorkItem.getField("projectType").getValueAsString();
      if (projectType.equals("Variant")) {
         type = "variant";
         typeInfo = currentWorkItem.getField("developmentPath").getItem().getId();
      } else if (projectType.equals("Build")) {
         type = "build";
         typeInfo = currentWorkItem.getField("revision").getItem().getId();
      }

      return new SandboxInfo(sandboxName, server, serverPort, type, siProjectName, typeInfo, parentSandbox, isPending, configPath);
   }

   public static SandboxInfo getSandboxInfo(CmdRunnerCreator session, File sandboxPath) throws APIException {
      return getSandboxInfo(new SICommands(session, false), sandboxPath);
   }

   static SandboxInfo getSandboxInfo(SICommands si, File sandboxPath) throws APIException {
      InvalidCommandOptionException ex;
      try {
         ex = null;
         Response sandboxInfo;
         if (sandboxPath.isFile()) {
            sandboxInfo = si.getSandboxInfoFromSandbox(sandboxPath.getAbsolutePath());
         } else {
            sandboxInfo = si.getSandboxInfo(sandboxPath.getAbsolutePath());
         }

         return construct(sandboxInfo);
      } catch (InvalidCommandOptionException var6) {
         ex = var6;
         boolean missingSandbox = false;

         try {
            missingSandbox = "sandbox".equals(ex.getField("command-option").getValue());
         } catch (NoSuchElementException var5) {
         }

         if (missingSandbox) {
            return null;
         } else {
            throw var6;
         }
      }
   }
}

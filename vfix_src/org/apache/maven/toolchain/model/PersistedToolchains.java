package org.apache.maven.toolchain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PersistedToolchains implements Serializable {
   private List<ToolchainModel> toolchains;
   private String modelEncoding = "UTF-8";

   public void addToolchain(ToolchainModel toolchainModel) {
      if (!(toolchainModel instanceof ToolchainModel)) {
         throw new ClassCastException("PersistedToolchains.addToolchains(toolchainModel) parameter must be instanceof " + ToolchainModel.class.getName());
      } else {
         this.getToolchains().add(toolchainModel);
      }
   }

   public String getModelEncoding() {
      return this.modelEncoding;
   }

   public List<ToolchainModel> getToolchains() {
      if (this.toolchains == null) {
         this.toolchains = new ArrayList();
      }

      return this.toolchains;
   }

   public void removeToolchain(ToolchainModel toolchainModel) {
      if (!(toolchainModel instanceof ToolchainModel)) {
         throw new ClassCastException("PersistedToolchains.removeToolchains(toolchainModel) parameter must be instanceof " + ToolchainModel.class.getName());
      } else {
         this.getToolchains().remove(toolchainModel);
      }
   }

   public void setModelEncoding(String modelEncoding) {
      this.modelEncoding = modelEncoding;
   }

   public void setToolchains(List<ToolchainModel> toolchains) {
      this.toolchains = toolchains;
   }
}

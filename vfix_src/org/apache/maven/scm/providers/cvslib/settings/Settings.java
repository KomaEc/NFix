package org.apache.maven.scm.providers.cvslib.settings;

import java.io.Serializable;
import java.util.Properties;

public class Settings implements Serializable {
   private String changeLogCommandDateFormat = "yyyy-MM-dd HH:mm:ssZ";
   private boolean useCvsrc = false;
   private int compressionLevel = 3;
   private boolean traceCvsCommand = false;
   private String temporaryFilesDirectory;
   private Properties cvsVariables;
   private boolean useForceTag = true;
   private String modelEncoding = "UTF-8";

   public void addCvsVariable(String key, String value) {
      this.getCvsVariables().put(key, value);
   }

   public String getChangeLogCommandDateFormat() {
      return this.changeLogCommandDateFormat;
   }

   public int getCompressionLevel() {
      return this.compressionLevel;
   }

   public Properties getCvsVariables() {
      if (this.cvsVariables == null) {
         this.cvsVariables = new Properties();
      }

      return this.cvsVariables;
   }

   public String getModelEncoding() {
      return this.modelEncoding;
   }

   public String getTemporaryFilesDirectory() {
      return this.temporaryFilesDirectory;
   }

   public boolean isTraceCvsCommand() {
      return this.traceCvsCommand;
   }

   public boolean isUseCvsrc() {
      return this.useCvsrc;
   }

   public boolean isUseForceTag() {
      return this.useForceTag;
   }

   public void setChangeLogCommandDateFormat(String changeLogCommandDateFormat) {
      this.changeLogCommandDateFormat = changeLogCommandDateFormat;
   }

   public void setCompressionLevel(int compressionLevel) {
      this.compressionLevel = compressionLevel;
   }

   public void setCvsVariables(Properties cvsVariables) {
      this.cvsVariables = cvsVariables;
   }

   public void setModelEncoding(String modelEncoding) {
      this.modelEncoding = modelEncoding;
   }

   public void setTemporaryFilesDirectory(String temporaryFilesDirectory) {
      this.temporaryFilesDirectory = temporaryFilesDirectory;
   }

   public void setTraceCvsCommand(boolean traceCvsCommand) {
      this.traceCvsCommand = traceCvsCommand;
   }

   public void setUseCvsrc(boolean useCvsrc) {
      this.useCvsrc = useCvsrc;
   }

   public void setUseForceTag(boolean useForceTag) {
      this.useForceTag = useForceTag;
   }
}

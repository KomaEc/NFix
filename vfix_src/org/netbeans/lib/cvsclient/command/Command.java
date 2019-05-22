package org.netbeans.lib.cvsclient.command;

import java.io.File;
import org.netbeans.lib.cvsclient.ClientServices;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.event.BinaryMessageEvent;
import org.netbeans.lib.cvsclient.event.CVSListener;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.FileAddedEvent;
import org.netbeans.lib.cvsclient.event.FileInfoEvent;
import org.netbeans.lib.cvsclient.event.FileRemovedEvent;
import org.netbeans.lib.cvsclient.event.FileToRemoveEvent;
import org.netbeans.lib.cvsclient.event.FileUpdatedEvent;
import org.netbeans.lib.cvsclient.event.MessageEvent;
import org.netbeans.lib.cvsclient.event.ModuleExpansionEvent;
import org.netbeans.lib.cvsclient.event.TerminationEvent;
import org.netbeans.lib.cvsclient.response.ErrorResponse;

public abstract class Command implements CVSListener, Cloneable {
   protected String localDirectory;
   private GlobalOptions globalOptions;
   private boolean failed = false;
   private String displayName;

   public void execute(ClientServices var1, EventManager var2) throws CommandException, CommandAbortedException, AuthenticationException {
      this.setLocalDirectory(var1.getLocalPath());
      this.globalOptions = var1.getGlobalOptions();
   }

   public abstract String getCVSCommand();

   public abstract String getCVSArguments();

   public abstract boolean setCVSCommand(char var1, String var2);

   public abstract void resetCVSCommand();

   public abstract String getOptString();

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }

   public boolean hasFailed() {
      return this.failed;
   }

   public void messageSent(MessageEvent var1) {
      if (var1.isError() && var1.getSource() instanceof ErrorResponse || var1.getSource() == this) {
         this.failed = true;
      }

   }

   public void messageSent(BinaryMessageEvent var1) {
   }

   public void fileAdded(FileAddedEvent var1) {
   }

   public void fileToRemove(FileToRemoveEvent var1) {
   }

   public void fileRemoved(FileRemovedEvent var1) {
   }

   public void fileUpdated(FileUpdatedEvent var1) {
   }

   public void fileInfoGenerated(FileInfoEvent var1) {
   }

   public void commandTerminated(TerminationEvent var1) {
   }

   public void moduleExpanded(ModuleExpansionEvent var1) {
   }

   public final String getLocalDirectory() {
      return this.localDirectory;
   }

   /** @deprecated */
   public final String getLocalPath() {
      return this.localDirectory;
   }

   public final GlobalOptions getGlobalOptions() {
      return this.globalOptions;
   }

   public final String getRelativeToLocalPathInUnixStyle(File var1) {
      String var2 = var1.getAbsolutePath();
      int var3 = this.localDirectory.length() + 1;
      if (var3 >= var2.length()) {
         return ".";
      } else {
         String var4 = var2.substring(var3);
         return var4.replace('\\', '/');
      }
   }

   protected final void setLocalDirectory(String var1) {
      this.localDirectory = var1;
   }

   protected static final String getTrimmedString(String var0) {
      if (var0 == null) {
         return null;
      } else {
         var0 = var0.trim();
         return var0.length() == 0 ? null : var0;
      }
   }

   public void setDisplayName(String var1) {
      this.displayName = var1;
   }

   public String getDisplayName() {
      return this.displayName;
   }
}

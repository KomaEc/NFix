package com.mks.api.ext;

import com.mks.api.CmdRunner;
import com.mks.api.CmdRunnerCreator;
import com.mks.api.Command;
import com.mks.api.IntegrationPoint;
import com.mks.api.IntegrationPointFactory;
import com.mks.api.VersionNumber;
import com.mks.api.response.APIException;
import com.mks.api.response.APIInternalError;
import com.mks.api.response.Response;
import com.mks.api.response.WorkItem;
import com.mks.api.util.APIVersion;
import java.util.Iterator;

public abstract class SimpleIntegrationCommand implements VersionedIntegrationCommand {
   private VersionNumber apiVersion;
   private IntegrationPoint localIP;
   private CmdRunnerCreator cmdFactory;

   /** @deprecated */
   protected SimpleIntegrationCommand() {
   }

   protected SimpleIntegrationCommand(int apiMajorNumber, int apiMinorNumber) {
      this.apiVersion = new APIVersion(apiMajorNumber, apiMinorNumber);
   }

   public final VersionNumber getAPIExecutionVersion() {
      return this.apiVersion;
   }

   protected final VersionNumber getAPIRequestVersion() {
      return this.localIP == null ? null : this.localIP.getAPIRequestVersion();
   }

   public void setDefaultHostname(String host) {
      if (this.cmdFactory == null) {
         throw new APIInternalError("CmdRunnerCreator not initialized");
      } else {
         this.cmdFactory.setDefaultHostname(host);
      }
   }

   public void setDefaultImpersonationUser(String impUser) {
      if (this.cmdFactory == null) {
         throw new APIInternalError("CmdRunnerCreator not initialized");
      } else {
         this.cmdFactory.setDefaultImpersonationUser(impUser);
      }
   }

   public void setDefaultPassword(String pass) {
      if (this.cmdFactory == null) {
         throw new APIInternalError("CmdRunnerCreator not initialized");
      } else {
         this.cmdFactory.setDefaultPassword(pass);
      }
   }

   public void setDefaultPort(int port) {
      if (this.cmdFactory == null) {
         throw new APIInternalError("CmdRunnerCreator not initialized");
      } else {
         this.cmdFactory.setDefaultPort(port);
      }
   }

   public void setDefaultUsername(String user) {
      if (this.cmdFactory == null) {
         throw new APIInternalError("CmdRunnerCreator not initialized");
      } else {
         this.cmdFactory.setDefaultUsername(user);
      }
   }

   public IntegrationPoint getLocalIntegrationPoint() {
      return this.localIP;
   }

   protected CmdRunnerCreator getLocalCmdRunnerCreator() {
      return this.cmdFactory;
   }

   protected int getExitCode() {
      return 0;
   }

   public final int execute(IntegrationPoint localIP, CmdRunnerCreator cmdFactory, ResponseWriter apiout, CommandOptions options, CommandSelection selection) throws APIException {
      int var6;
      try {
         this.localIP = localIP;
         this.cmdFactory = cmdFactory;
         this.apiVersion = cmdFactory.getAPIRequestVersion();
         this.execute(apiout, options, selection);
         var6 = this.getExitCode();
      } finally {
         this.release();
      }

      return var6;
   }

   protected abstract void execute(ResponseWriter var1, CommandOptions var2, CommandSelection var3) throws APIException;

   private void release() {
      Iterator it = this.cmdFactory.getCmdRunners();

      while(it.hasNext()) {
         CmdRunner c = (CmdRunner)it.next();

         try {
            c.interrupt();
            c.release();
         } catch (APIException var4) {
            IntegrationPointFactory.getLogger().exception("ERROR", var4);
         }
      }

   }

   public Response runLocalCommand(String[] command) throws APIException {
      Response response = null;
      APIException cmdEx = null;
      CmdRunner c = this.cmdFactory.createCmdRunner();

      try {
         response = c.execute(command);
      } catch (APIException var12) {
         cmdEx = var12;
         response = var12.getResponse();
         if (response != null && response.getWorkItemListSize() == 1) {
            try {
               WorkItem wi = response.getWorkItems().next();
               if (wi.getAPIException() != null) {
                  throw wi.getAPIException();
               }
            } catch (APIException var11) {
               cmdEx = var11;
            }
         }
      } finally {
         c.setDefaultPassword((String)null);
      }

      if (cmdEx != null) {
         throw cmdEx;
      } else {
         return response;
      }
   }

   public final Response runLocalCommand(Command command) throws APIException {
      return this.runLocalCommand(command.toStringArray());
   }

   public Response invokeLocalCommand(String[] command) throws APIException {
      return this.invokeLocalCommand(command, true);
   }

   public Response invokeLocalCommand(Command command) throws APIException {
      return this.invokeLocalCommand(command, true);
   }

   public Response invokeLocalCommand(String[] command, boolean enableCache) throws APIException {
      CmdRunner c = this.cmdFactory.createCmdRunner();

      Response var4;
      try {
         var4 = c.executeWithInterim(command, enableCache);
      } finally {
         c.setDefaultPassword((String)null);
      }

      return var4;
   }

   public Response invokeLocalCommand(Command command, boolean enableCache) throws APIException {
      CmdRunner c = this.cmdFactory.createCmdRunner();

      Response var4;
      try {
         var4 = c.executeWithInterim(command, enableCache);
      } finally {
         c.setDefaultPassword((String)null);
      }

      return var4;
   }
}

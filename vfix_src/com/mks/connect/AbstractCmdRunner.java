package com.mks.connect;

import com.mks.api.CmdRunner;
import com.mks.api.CmdRunnerCreator;
import com.mks.api.Command;
import com.mks.api.IntegrationPointFactory;
import com.mks.api.Session;
import com.mks.api.response.APIConnectionException;
import com.mks.api.response.APIError;
import com.mks.api.response.APIException;
import com.mks.api.response.APIExceptionFactory;
import com.mks.api.response.CommandAlreadyRunningException;
import com.mks.api.response.IncompatibleVersionException;
import com.mks.api.response.InvalidCommandRunnerStateException;
import com.mks.api.response.Response;
import com.mks.api.response.UnsupportedApplicationException;
import com.mks.api.response.impl.ApplicationConnectionError;
import com.mks.api.response.impl.UnsupportedApplicationError;
import com.mks.api.response.impl.UnsupportedVersionError;
import com.mks.api.response.impl.XMLResponseHandler;
import com.mks.api.response.modifiable.ModifiableResponse;
import com.mks.api.util.MKSLogger;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractCmdRunner implements CmdRunner {
   private static final String EXECUTE_MSG = "Executing the command : {0}";
   private static final String EXECUTION_TIME_MSG = "Total time to execute \"{0}\": {1}ms";
   protected static final String FLAG_OPTION_MSG = "Flags/Options:";
   protected static final String SELECTION_MSG = "Selection:";
   protected static final String CACHE_RESPONSE_MSG = "Setting the cacheResponse flag to : {0}";
   protected static final String COMPLETE_RESPONSE_MSG = "Setting the setCompleteResponse flag to : {0}";
   private CmdRunnerCreator session;
   protected boolean interrupted;
   protected BlimpInputStream bis;
   protected String defaultHost;
   protected int defaultPort;
   protected String defaultUser;
   protected String defaultPass;
   protected String impUser;
   protected boolean released;
   protected Response interimResponse;
   protected MKSLogger apiLogger;

   protected AbstractCmdRunner(CmdRunnerCreator session) {
      this.session = session;
      this.apiLogger = IntegrationPointFactory.getLogger();
   }

   public Session getSession() {
      return this.session instanceof Session ? (Session)this.session : null;
   }

   protected abstract BlimpInputStream createBlimpStream(String[] var1, boolean var2);

   public void interrupt() throws APIException {
      try {
         if (this.interimResponse != null) {
            this.interimResponse.interrupt();
            this.interimResponse = null;
         }

         if (this.bis != null) {
            this.bis.close();
         }

      } catch (IOException var2) {
         this.apiLogger.exception((Object)this, "API", 0, var2);
         throw new InvalidCommandRunnerStateException(var2);
      }
   }

   protected void setInterrupted() {
      this.interrupted = true;
   }

   protected boolean isInterrupted() {
      return this.interrupted;
   }

   protected void resetInterrupt() {
      this.interrupted = false;
   }

   public boolean isFinished() {
      return this.bis == null || this.bis.isFinished();
   }

   protected final Response executeCommand(String[] args, boolean interimResponse, boolean cacheResults, boolean generateSubRtns) throws APIException {
      if (!this.isInterrupted() && !this.released) {
         if (!this.isFinished()) {
            throw new CommandAlreadyRunningException();
         } else {
            List tmpList = new LinkedList(Arrays.asList(args));
            if (args.length > 1) {
               int idx = 2;
               if (this.impUser != null) {
                  tmpList.add(idx, "--impersonateuser=" + this.impUser);
               }

               if (this.getDefaultHostname() != null) {
                  tmpList.add(idx, "--hostname=" + this.getDefaultHostname());
               }

               if (this.getDefaultPort() > 0) {
                  tmpList.add(idx, "--port=" + this.getDefaultPort());
               }

               if (this.getDefaultUsername() != null) {
                  tmpList.add(idx, "--user=" + this.getDefaultUsername());
               }

               if (this.getDefaultPassword() != null) {
                  tmpList.add(idx, "--password=" + this.getDefaultPassword());
               }
            }

            String[] cmd = (String[])tmpList.toArray(new String[tmpList.size()]);
            StringBuffer cc = new StringBuffer(cmd[0]);

            for(int i = 1; i < cmd.length; ++i) {
               if (!cmd[i].startsWith("--password=")) {
                  cc.append(" ");
                  cc.append(cmd[i]);
               }
            }

            this.executePreCondition(cmd);
            long startTime = System.currentTimeMillis();
            String msg = MessageFormat.format("Executing the command : {0}", cc);
            this.apiLogger.message((Object)this, "API", 0, msg);
            Response response = this.executeCommand(cc, cmd, interimResponse, cacheResults, generateSubRtns);
            long endTime = System.currentTimeMillis();
            long diffTime = endTime - startTime;
            msg = MessageFormat.format("Total time to execute \"{0}\": {1}ms", cc, new Long(diffTime));
            this.apiLogger.message((Object)this, "API", 10, msg);
            if (!interimResponse && response.getAPIException() != null) {
               throw response.getAPIException();
            } else {
               return response;
            }
         }
      } else {
         throw new InvalidCommandRunnerStateException();
      }
   }

   protected void executePreCondition(String[] cmd) throws APIException {
   }

   protected Response executeCommand(StringBuffer cmdline, String[] cmd, boolean interimResponse, boolean cacheResults, boolean generateSubRtns) throws APIException {
      String appName = cmd[0];
      this.bis = this.createBlimpStream(cmd, generateSubRtns);
      Response response = null;
      XMLResponseHandler xrh = new XMLResponseHandler(this.bis, "UTF-8");

      try {
         if (!interimResponse) {
            response = xrh.getResponse((CmdRunner)null, cmdline.toString(), true);
            ((ModifiableResponse)response).setApplicationName(appName);
            if (response != null && response.getAPIException() != null) {
               APIException ex = response.getAPIException();
               throw ex;
            }
         } else {
            response = xrh.getResponse(this, cmdline.toString(), false);
            ((ModifiableResponse)response).setApplicationName(appName);
            ((ModifiableResponse)response).setUseInterim(interimResponse);
            ((ModifiableResponse)response).setCacheContents(cacheResults);
            this.interimResponse = response;
         }
      } catch (ApplicationConnectionError var22) {
         this.apiLogger.exception((Object)this, "API", 0, var22);
         APIConnectionException ace = (APIConnectionException)APIExceptionFactory.createAPIException("APIConnectionException", var22.getMessage());
         throw ace;
      } catch (UnsupportedVersionError var23) {
         this.apiLogger.exception((Object)this, "API", 0, var23);
         APIException ex = APIExceptionFactory.createAPIException("IncompatibleVersionException", var23.getMessage());
         IncompatibleVersionException ive = (IncompatibleVersionException)ex;
         throw ive;
      } catch (UnsupportedApplicationError var24) {
         this.apiLogger.exception((Object)this, "API", 0, var24);
         UnsupportedApplicationException uae = (UnsupportedApplicationException)APIExceptionFactory.createAPIException("UnsupportedApplicationException", (String)null);
         uae.addField("applicationName", appName);
         throw uae;
      } finally {
         try {
            if (this.interimResponse == null && this.bis != null) {
               this.bis.close();
               this.bis = null;
            }
         } catch (IOException var21) {
            this.apiLogger.exception((Object)this, "API", 0, var21);
         }

      }

      return response;
   }

   public final Response executeWithInterim(String[] args, boolean cacheResults) throws APIException {
      return this.executeCommand(args, true, cacheResults, false);
   }

   public final Response executeWithInterim(Command cmd, boolean cacheResults) throws APIException {
      return this.executeCommand(cmd.toStringArray(), true, cacheResults, cmd.getGenerateSubRoutines());
   }

   public final Response execute(String[] args) throws APIException {
      return this.executeCommand(args, false, true, false);
   }

   public final Response execute(Command c) throws APIException {
      return this.executeCommand(c.toStringArray(), false, true, c.getGenerateSubRoutines());
   }

   public void release() throws APIException {
      if (this.isFinished()) {
         if (this.bis != null) {
            try {
               if (this.interimResponse != null) {
                  this.interimResponse.interrupt();
                  this.interimResponse = null;
               }

               if (this.bis != null) {
                  this.bis.close();
                  this.bis = null;
               }
            } catch (IOException var2) {
               this.apiLogger.exception((Object)this, "API", 0, var2);
            }
         }

         if (!this.released && this.session instanceof CmdRunnerCreatorImpl) {
            ((CmdRunnerCreatorImpl)this.session).removeCmdRunner(this);
         }

         this.released = true;
      } else {
         throw new CommandAlreadyRunningException();
      }
   }

   public String getDefaultHostname() {
      return this.defaultHost;
   }

   public void setDefaultHostname(String host) {
      this.defaultHost = host;
   }

   public int getDefaultPort() {
      return this.defaultPort;
   }

   public void setDefaultPort(int port) {
      this.defaultPort = port;
   }

   public String getDefaultUsername() {
      return this.defaultUser;
   }

   public void setDefaultUsername(String user) {
      this.defaultUser = user;
   }

   public String getDefaultPassword() {
      return this.defaultPass;
   }

   public void setDefaultPassword(String pass) {
      this.defaultPass = pass;
   }

   public void setDefaultImpersonationUser(String impUser) {
      this.impUser = impUser;
   }

   public String getDefaultImpersonationUser() {
      return this.impUser;
   }

   public String executeWithXML(String[] cmd) throws APIException {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      if (!this.isInterrupted() && !this.released) {
         if (!this.isFinished()) {
            throw new CommandAlreadyRunningException();
         } else {
            List tmpList = new LinkedList(Arrays.asList(cmd));
            if (cmd.length > 1) {
               int idx = 2;
               if (this.impUser != null) {
                  tmpList.add(idx, "--impersonateuser=" + this.impUser);
               }

               if (this.getDefaultHostname() != null) {
                  tmpList.add(idx, "--hostname=" + this.getDefaultHostname());
               }

               if (this.getDefaultPort() > 0) {
                  tmpList.add(idx, "--port=" + this.getDefaultPort());
               }

               if (this.getDefaultUsername() != null) {
                  tmpList.add(idx, "--user=" + this.getDefaultUsername());
               }

               if (this.getDefaultPassword() != null) {
                  tmpList.add(idx, "--password=" + this.getDefaultPassword());
               }
            }

            String[] cmdArr = (String[])tmpList.toArray(new String[tmpList.size()]);
            String appName = cmdArr[0];
            StringBuffer cc = new StringBuffer(cmdArr[0]);

            for(int i = 1; i < cmdArr.length; ++i) {
               if (!cmdArr[i].startsWith("--password=")) {
                  cc.append(" ");
                  cc.append(cmdArr[i]);
               }
            }

            String msg = MessageFormat.format("Executing the command : {0}", cc);
            this.apiLogger.message((Object)this, "API", 0, msg);
            this.bis = this.createBlimpStream(cmdArr, false);

            try {
               byte[] arr = new byte[1024];
               boolean var29 = true;

               int length;
               while((length = this.bis.read(arr, 0, arr.length)) != -1) {
                  os.write(arr, 0, length);
               }
            } catch (ApplicationConnectionError var22) {
               this.apiLogger.exception((Object)this, "API", 0, var22);
               APIConnectionException ace = (APIConnectionException)APIExceptionFactory.createAPIException("APIConnectionException", var22.getMessage());
               throw ace;
            } catch (UnsupportedApplicationError var23) {
               this.apiLogger.exception((Object)this, "API", 0, var23);
               UnsupportedApplicationException uae = (UnsupportedApplicationException)APIExceptionFactory.createAPIException("UnsupportedApplicationException", (String)null);
               uae.addField("applicationName", appName);
               throw uae;
            } catch (IOException var24) {
               throw new APIError(var24);
            } finally {
               try {
                  this.bis.close();
                  this.bis = null;
                  os.flush();
               } catch (IOException var20) {
               }

            }

            try {
               return os.toString("UTF-8");
            } catch (UnsupportedEncodingException var21) {
               throw new APIError(var21);
            }
         }
      } else {
         throw new InvalidCommandRunnerStateException();
      }
   }
}

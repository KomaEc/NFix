package org.apache.maven.scm.provider.integrity;

import com.mks.api.CmdRunner;
import com.mks.api.Command;
import com.mks.api.IntegrationPoint;
import com.mks.api.IntegrationPointFactory;
import com.mks.api.Session;
import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import java.io.IOException;
import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.util.StringUtils;

public class APISession {
   public static final String VERSION = IntegrationPointFactory.getAPIVersion().substring(0, IntegrationPointFactory.getAPIVersion().indexOf(32));
   public static final int MAJOR_VERSION;
   public static final int MINOR_VERSION;
   private ScmLogger logger;
   private String hostName;
   private int port = 0;
   private String userName;
   private String password;
   private IntegrationPoint ip;
   private Session session;
   private boolean terminated;

   public APISession(ScmLogger logger) {
      logger.info("MKS Integrity API Version: " + VERSION);
      this.logger = logger;
   }

   public Response connect(String host, int portNum, String user, String paswd) throws APIException {
      this.terminated = false;
      this.ip = IntegrationPointFactory.getInstance().createLocalIntegrationPoint(MAJOR_VERSION, MINOR_VERSION);
      this.ip.setAutoStartIntegrityClient(true);
      if (null != paswd && paswd.length() > 0) {
         this.logger.info("Creating session for " + user + "/" + StringUtils.repeat("*", paswd.length()));
         this.session = this.ip.createSession(user, paswd);
         this.logger.info("Attempting to establish connection using " + user + "@" + host + ":" + portNum);
      } else {
         this.logger.info("Using a common session.  Connection information is obtained from client preferences");
         this.session = this.ip.getCommonSession();
      }

      Command ping = new Command("si", "connect");
      CmdRunner cmdRunner = this.session.createCmdRunner();
      if (null != host && host.length() > 0) {
         cmdRunner.setDefaultHostname(host);
      }

      if (portNum > 0) {
         cmdRunner.setDefaultPort(portNum);
      }

      if (null != user && user.length() > 0) {
         cmdRunner.setDefaultUsername(user);
      }

      if (null != paswd && paswd.length() > 0) {
         cmdRunner.setDefaultPassword(paswd);
      }

      Response res = cmdRunner.execute(ping);
      this.logger.debug(res.getCommandString() + " returned exit code " + res.getExitCode());
      this.hostName = res.getConnectionHostname();
      this.port = res.getConnectionPort();
      this.userName = res.getConnectionUsername();
      this.password = paswd;
      cmdRunner.release();
      this.logger.info("Successfully established connection " + this.userName + "@" + this.hostName + ":" + this.port);
      return res;
   }

   public Response runCommand(Command cmd) throws APIException {
      CmdRunner cmdRunner = this.session.createCmdRunner();
      cmdRunner.setDefaultHostname(this.hostName);
      cmdRunner.setDefaultPort(this.port);
      cmdRunner.setDefaultUsername(this.userName);
      if (null != this.password && this.password.length() > 0) {
         cmdRunner.setDefaultPassword(this.password);
      }

      Response res = cmdRunner.execute(cmd);
      this.logger.debug(res.getCommandString() + " returned exit code " + res.getExitCode());
      cmdRunner.release();
      return res;
   }

   public Response runCommandAs(Command cmd, String impersonateUser) throws APIException {
      CmdRunner cmdRunner = this.session.createCmdRunner();
      cmdRunner.setDefaultHostname(this.hostName);
      cmdRunner.setDefaultPort(this.port);
      cmdRunner.setDefaultUsername(this.userName);
      if (null != this.password && this.password.length() > 0) {
         cmdRunner.setDefaultPassword(this.password);
      }

      cmdRunner.setDefaultImpersonationUser(impersonateUser);
      Response res = cmdRunner.execute(cmd);
      this.logger.debug(res.getCommandString() + " returned exit code " + res.getExitCode());
      cmdRunner.release();
      return res;
   }

   public void terminate() {
      if (!this.terminated) {
         try {
            if (null != this.session) {
               this.session.release();
            }

            if (null != this.ip) {
               this.ip.release();
            }

            this.terminated = true;
            this.logger.info("Successfully disconnected connection " + this.userName + "@" + this.hostName + ":" + this.port);
         } catch (APIException var2) {
            this.logger.debug("Caught API Exception when releasing session!");
            var2.printStackTrace();
         } catch (IOException var3) {
            this.logger.debug("Caught IO Exception when releasing session!");
            var3.printStackTrace();
         }
      }

   }

   public String getHostName() {
      return this.hostName;
   }

   public int getPort() {
      return this.port;
   }

   public String getUserName() {
      return this.userName;
   }

   public String getPassword() {
      return null != this.password && this.password.length() > 0 ? this.password : "";
   }

   public ScmLogger getLogger() {
      return this.logger;
   }

   static {
      MAJOR_VERSION = Integer.parseInt(VERSION.substring(0, VERSION.indexOf(46)));
      MINOR_VERSION = Integer.parseInt(VERSION.substring(VERSION.indexOf(46) + 1, VERSION.length()));
   }
}

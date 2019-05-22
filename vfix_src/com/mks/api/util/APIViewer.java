package com.mks.api.util;

import com.mks.api.CmdRunner;
import com.mks.api.IntegrationPoint;
import com.mks.api.IntegrationPointFactory;
import com.mks.api.Session;
import com.mks.api.response.APIError;
import com.mks.api.response.APIException;
import com.mks.api.response.ApplicationConnectionException;
import com.mks.api.response.InvalidHostException;
import com.mks.api.response.InvalidIntegrationPointException;
import com.mks.api.response.Response;
import com.mks.api.response.UnsupportedApplicationException;
import com.mks.connect.AbstractCmdRunner;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

public class APIViewer {
   protected CmdRunner init() throws APIException {
      String ipHost = System.getProperty("com.mks.api.ip.hostname");
      boolean ipSecure = Boolean.getBoolean("com.mks.api.ip.secure");
      String tmpStr = System.getProperty("com.mks.api.ip.port");
      int ipPort = 0;
      if (tmpStr != null && tmpStr.trim().length() > 0) {
         ipPort = Integer.parseInt(tmpStr);
      }

      boolean isCommon = Boolean.getBoolean("com.mks.api.session.common");
      String sessionUser = System.getProperty("com.mks.api.session.username");
      String sessionPass = System.getProperty("com.mks.api.session.password");
      String defaultHost = System.getProperty("com.mks.api.default.hostname");
      int defaultPort = 0;
      tmpStr = System.getProperty("com.mks.api.default.port");
      if (tmpStr != null && tmpStr.trim().length() > 0) {
         defaultPort = Integer.parseInt(tmpStr);
      }

      String defaultUser = System.getProperty("com.mks.api.default.username");
      String defaultPass = System.getProperty("com.mks.api.default.password");
      int majorVersion = 0;
      int minorVersion = 0;
      tmpStr = System.getProperty("com.mks.api.version.major");
      if (tmpStr != null && tmpStr.trim().length() > 0) {
         majorVersion = Integer.parseInt(tmpStr);
      }

      tmpStr = System.getProperty("com.mks.api.version.minor");
      if (tmpStr != null && tmpStr.trim().length() > 0) {
         minorVersion = Integer.parseInt(tmpStr);
      }

      IntegrationPointFactory ipf = IntegrationPointFactory.getInstance();
      IntegrationPoint ip = null;
      if (ipHost != null && (!ipHost.equals("localhost") || ipPort != 0)) {
         ip = ipf.createIntegrationPoint(ipHost, ipPort, ipSecure, majorVersion, minorVersion);
      } else {
         ip = ipf.createLocalIntegrationPoint(majorVersion, minorVersion);
      }

      Session session = null;
      if (isCommon) {
         session = ip.getCommonSession(sessionUser, sessionPass);
      } else {
         session = ip.createSession(sessionUser, sessionPass);
      }

      CmdRunner cr = session.createCmdRunner();
      if (defaultHost != null && defaultHost.trim().length() > 0) {
         cr.setDefaultHostname(defaultHost.trim());
      }

      if (defaultPort > 0) {
         cr.setDefaultPort(defaultPort);
      }

      if (defaultUser != null && defaultUser.trim().length() > 0) {
         cr.setDefaultUsername(defaultUser.trim());
      }

      if (defaultPass != null && defaultPass.trim().length() > 0) {
         cr.setDefaultPassword(defaultPass.trim());
      }

      return cr;
   }

   public static void main(String[] args) {
      if (args.length < 2) {
         System.out.println("Syntax: APIViewer -Dapiviewer.config.file=<Configuration File><Integrity Application> <Integrity Application Command> [options] [selection]");
         System.exit(1);
      }

      CmdRunner cr = null;

      try {
         String cf = System.getProperty("apiviewer.config.file");
         File f = cf != null ? new File(cf) : null;
         if (f != null) {
            if (!f.exists() || !f.canRead()) {
               System.out.println("Cannot open configuration file: " + f);
               System.exit(1);
            }

            Properties p = new Properties(System.getProperties());
            p.load(new FileInputStream(f));
            System.setProperties(p);
         }

         APIViewer apiViewer = new APIViewer();
         cr = apiViewer.init();
         AbstractCmdRunner cri;
         if (Boolean.getBoolean("com.mks.api.xml.output")) {
            cri = (AbstractCmdRunner)cr;
            System.out.println(cri.executeWithXML(args));
         } else {
            cri = null;
            Response response;
            if (Boolean.getBoolean("com.mks.api.response.interim")) {
               response = cr.executeWithInterim(args, Boolean.getBoolean("com.mks.api.response.cache"));
            } else {
               response = cr.execute(args);
            }

            ResponseUtil.printResponse(response, 1, (PrintStream)System.out);
         }
      } catch (IOException var443) {
         System.out.println("Could not load the configuration file.");
         System.out.println(var443.getMessage());
         var443.printStackTrace();
      } catch (IllegalArgumentException var444) {
         System.out.println("Could not load the configuration file.");
         System.out.println(var444.getMessage());
         var444.printStackTrace();
      } catch (UnsupportedApplicationException var445) {
         ResponseUtil.printAPIException("UnsupportedApplicationException:", var445, 0, (PrintStream)System.out);
      } catch (ApplicationConnectionException var446) {
         ResponseUtil.printAPIException("ApplicationConnectionException:", var446, 0, (PrintStream)System.out);
      } catch (InvalidHostException var447) {
         ResponseUtil.printAPIException("InvalidHostException:", var447, 0, (PrintStream)System.out);
      } catch (InvalidIntegrationPointException var448) {
         ResponseUtil.printAPIException("InvalidIntegrationPointException:", var448, 0, (PrintStream)System.out);
      } catch (APIException var449) {
         if (var449.getResponse() != null) {
            ResponseUtil.printResponse(var449.getResponse(), 1, (PrintStream)System.out);
         } else {
            ResponseUtil.printAPIException("APIException:", var449, 0, (PrintStream)System.out);
         }
      } catch (APIError var450) {
         ResponseUtil.printAPIError(var450, 0, (PrintStream)System.out);
      } finally {
         Session session = cr == null ? null : cr.getSession();

         try {
            if (cr != null) {
               cr.release();
            }
         } catch (Exception var442) {
         }

         try {
            if (session != null) {
               IntegrationPoint ip = session.getIntegrationPoint();

               try {
                  session.release();
               } finally {
                  ip.release();
               }
            }
         } catch (Exception var441) {
         }

      }

   }
}

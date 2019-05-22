package com.mks.api.util;

import com.mks.api.CmdRunner;
import com.mks.api.Command;
import com.mks.api.IntegrationPoint;
import com.mks.api.IntegrationPointFactory;
import com.mks.api.Option;
import com.mks.api.Session;
import com.mks.api.response.APIException;
import com.mks.api.response.Response;
import java.io.IOException;

public class GenerateAPIStats {
   private static String host;
   private static int port;
   private static String user;
   private static String pass;
   private static String command_domain;
   private static String command;
   private static int test_count;
   private static String impersonated_user = null;

   GenerateAPIStats(int testCase) {
      try {
         switch(testCase) {
         case 1:
            this.testCommandClean();
            break;
         case 2:
            this.testCommandSlowClean();
            break;
         case 3:
            this.testCommandSlowDirty();
            break;
         default:
            throw new IllegalArgumentException("Illegal test case.");
         }
      } catch (APIException var3) {
         System.out.print(var3.getMessage());
      }

   }

   private Session connect() throws APIException {
      IntegrationPointFactory ipf = IntegrationPointFactory.getInstance();
      IntegrationPoint ip = ipf.createIntegrationPoint(host, port, false, 0, 0);
      return ip.createSession(user, pass);
   }

   private void disconnect(Session session) throws APIException {
      if (session != null) {
         try {
            session.release();
         } catch (IOException var3) {
            var3.printStackTrace();
         }
      }

   }

   private void testCommandClean() throws APIException {
      Session session = null;
      Response response = null;
      CmdRunner runner = null;

      try {
         session = this.connect();
         runner = session.createCmdRunner();

         for(int i = 0; i < test_count; ++i) {
            response = this.executeCommand(runner);
         }
      } finally {
         if (response != null) {
            response.release();
         }

         if (runner != null) {
            runner.release();
         }

         this.disconnect(session);
      }

   }

   private void testCommandSlowClean() throws APIException {
      Session session = null;
      Response response = null;
      CmdRunner runner = null;

      for(int i = 0; i < test_count; ++i) {
         try {
            session = this.connect();
            runner = session.createCmdRunner();
            response = this.executeCommand(runner);
         } finally {
            if (response != null) {
               response.release();
            }

            if (runner != null) {
               runner.release();
            }

            this.disconnect(session);
         }
      }

   }

   private void testCommandSlowDirty() throws APIException {
      Session session = null;
      Response response = null;
      CmdRunner runner = null;

      for(int i = 0; i < test_count; ++i) {
         try {
            session = this.connect();
            runner = session.createCmdRunner();
            this.executeCommand(runner);
         } finally {
            ;
         }
      }

   }

   private Response executeCommand(CmdRunner runner) throws APIException {
      Command cmd = new Command(command_domain, command);
      if (impersonated_user != null) {
         cmd.addOption(new Option("impersonateuser", impersonated_user));
      }

      Response response = runner.execute(cmd);
      APIException apie = response.getAPIException();
      if (apie != null) {
         throw apie;
      } else {
         return response;
      }
   }

   public static void main(String[] args) {
      int testCase = false;
      if (args != null && args.length >= 8) {
         host = args[0];
         port = new Integer(args[1]);
         user = args[2];
         pass = args[3];
         command_domain = args[4];
         command = args[5];
         test_count = new Integer(args[6]);
         int testCase = new Integer(args[7]);
         if (args[8] != null) {
            impersonated_user = args[8];
         }

         new GenerateAPIStats(testCase);
      } else {
         throw new IllegalArgumentException();
      }
   }
}

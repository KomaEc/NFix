package com.mks.connect;

import com.mks.api.IntegrationPoint;
import com.mks.api.IntegrationPointFactory;
import com.mks.api.response.APIException;
import com.mks.api.response.ICLaunchException;
import org.apache.commons.httpclient.HttpClient;

class ClientCmdRunnerImpl extends HttpCmdRunnerImpl {
   private static final String BITS_KEY = "sun.arch.data.model";
   private static final String BITS_VALUE_64 = "64";
   private static final String NATIVE_LIBRARY = "apiclientrunner";
   private static final String NATIVE_LIBRARY64 = "apiclientrunner64";
   private static boolean isInitialized = false;

   ClientCmdRunnerImpl(UserApplicationSessionImpl uas, HttpClient client) {
      super(uas, client);
      if (!isInitialized) {
         throw new UnsatisfiedLinkError("Cannot load apiclientrunner");
      }
   }

   private void checkIntegrityClientForLaunch() throws BlimpException, APIException {
      IntegrationPoint ip = this.uas.getIntegrationPoint();
      ClientCmdRunnerImpl.NativeReturn nr = icInitialize(ip.getAutoStartIntegrityClient());
      if (nr == null) {
         throw new BlimpException("Got nothing from icInitialize!!");
      } else if (nr.port == 0) {
         throw new BlimpException("Integrity Client port not found");
      } else {
         ((IntegrationPointImpl)ip).setPort(nr.port);
         this.uas.setAuthenticationCookie(nr.cookie);
         UserApplicationSessionImpl.configureHttpClient(this.httpClient, this.httpClient.getHostConfiguration(), ip);
      }
   }

   protected void executePreCondition(String[] cmd) throws APIException {
      try {
         this.checkIntegrityClientForLaunch();
      } catch (BlimpException var3) {
         throw new APIException(var3);
      }
   }

   protected native boolean isIntegrityClientRunning();

   protected static synchronized native ClientCmdRunnerImpl.NativeReturn icInitialize(boolean var0) throws BlimpException, ICLaunchException;

   static {
      try {
         if ("64".equals(System.getProperty("sun.arch.data.model"))) {
            System.loadLibrary("apiclientrunner64");
         } else {
            System.loadLibrary("apiclientrunner");
         }

         isInitialized = true;
      } catch (UnsatisfiedLinkError var1) {
         IntegrationPointFactory.getLogger().exception("API", 0, var1);
      }

   }

   public static class NativeReturn {
      public int port;
      public String cookie;

      public NativeReturn(int port, String cookie) {
         this.port = port;
         this.cookie = cookie;
      }
   }
}

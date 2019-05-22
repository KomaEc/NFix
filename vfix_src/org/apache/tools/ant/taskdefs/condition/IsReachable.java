package org.apache.tools.ant.taskdefs.condition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;

public class IsReachable extends ProjectComponent implements Condition {
   private static final int SECOND = 1000;
   private String host;
   private String url;
   public static final int DEFAULT_TIMEOUT = 30;
   private int timeout = 30;
   public static final String ERROR_NO_HOSTNAME = "No hostname defined";
   public static final String ERROR_BAD_TIMEOUT = "Invalid timeout value";
   private static final String WARN_UNKNOWN_HOST = "Unknown host: ";
   public static final String ERROR_ON_NETWORK = "network error to ";
   public static final String ERROR_BOTH_TARGETS = "Both url and host have been specified";
   public static final String MSG_NO_REACHABLE_TEST = "cannot do a proper reachability test on this Java version";
   public static final String ERROR_BAD_URL = "Bad URL ";
   public static final String ERROR_NO_HOST_IN_URL = "No hostname in URL ";
   public static final String METHOD_NAME = "isReachable";
   private static Class[] parameterTypes;
   // $FF: synthetic field
   static Class class$java$net$InetAddress;

   public void setHost(String host) {
      this.host = host;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public void setTimeout(int timeout) {
      this.timeout = timeout;
   }

   private boolean empty(String string) {
      return string == null || string.length() == 0;
   }

   public boolean eval() throws BuildException {
      if (this.empty(this.host) && this.empty(this.url)) {
         throw new BuildException("No hostname defined");
      } else if (this.timeout < 0) {
         throw new BuildException("Invalid timeout value");
      } else {
         String target = this.host;
         if (!this.empty(this.url)) {
            if (!this.empty(this.host)) {
               throw new BuildException("Both url and host have been specified");
            }

            try {
               URL realURL = new URL(this.url);
               target = realURL.getHost();
               if (this.empty(target)) {
                  throw new BuildException("No hostname in URL " + this.url);
               }
            } catch (MalformedURLException var12) {
               throw new BuildException("Bad URL " + this.url, var12);
            }
         }

         this.log("Probing host " + target, 3);

         InetAddress address;
         try {
            address = InetAddress.getByName(target);
         } catch (UnknownHostException var11) {
            this.log("Unknown host: " + target);
            return false;
         }

         this.log("Host address = " + address.getHostAddress(), 3);
         Method reachableMethod = null;

         boolean reachable;
         try {
            reachableMethod = (class$java$net$InetAddress == null ? (class$java$net$InetAddress = class$("java.net.InetAddress")) : class$java$net$InetAddress).getMethod("isReachable", parameterTypes);
            Object[] params = new Object[]{new Integer(this.timeout * 1000)};

            try {
               reachable = (Boolean)reachableMethod.invoke(address, params);
            } catch (IllegalAccessException var8) {
               throw new BuildException("When calling " + reachableMethod);
            } catch (InvocationTargetException var9) {
               Throwable nested = var9.getTargetException();
               this.log("network error to " + target + ": " + nested.toString());
               reachable = false;
            }
         } catch (NoSuchMethodException var10) {
            this.log("Not found: InetAddress.isReachable", 3);
            this.log("cannot do a proper reachability test on this Java version");
            reachable = true;
         }

         this.log("host is" + (reachable ? "" : " not") + " reachable", 3);
         return reachable;
      }
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      parameterTypes = new Class[]{Integer.TYPE};
   }
}

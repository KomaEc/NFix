package org.jacoco.agent;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public final class AgentJar {
   private static final String RESOURCE = "/jacocoagent.jar";
   private static final String ERRORMSG = String.format("The resource %s has not been found. Please see /org.jacoco.agent/README.TXT for details.", "/jacocoagent.jar");

   private AgentJar() {
   }

   public static URL getResource() {
      URL url = AgentJar.class.getResource("/jacocoagent.jar");
      if (url == null) {
         throw new AssertionError(ERRORMSG);
      } else {
         return url;
      }
   }

   public static InputStream getResourceAsStream() {
      InputStream stream = AgentJar.class.getResourceAsStream("/jacocoagent.jar");
      if (stream == null) {
         throw new AssertionError(ERRORMSG);
      } else {
         return stream;
      }
   }

   public static File extractToTempLocation() throws IOException {
      File agentJar = File.createTempFile("jacocoagent", ".jar");
      agentJar.deleteOnExit();
      extractTo(agentJar);
      return agentJar;
   }

   public static void extractTo(File destination) throws IOException {
      InputStream inputJarStream = getResourceAsStream();
      FileOutputStream outputJarStream = null;

      try {
         outputJarStream = new FileOutputStream(destination);
         byte[] buffer = new byte[8192];

         int bytesRead;
         while((bytesRead = inputJarStream.read(buffer)) != -1) {
            outputJarStream.write(buffer, 0, bytesRead);
         }
      } finally {
         safeClose(inputJarStream);
         safeClose(outputJarStream);
      }

   }

   private static void safeClose(Closeable closeable) {
      try {
         if (closeable != null) {
            closeable.close();
         }
      } catch (IOException var2) {
      }

   }
}

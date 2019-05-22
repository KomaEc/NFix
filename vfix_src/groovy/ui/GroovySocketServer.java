package groovy.ui;

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

public class GroovySocketServer implements Runnable {
   private URL url;
   private GroovyShell groovy;
   private boolean isScriptFile;
   private String scriptFilenameOrText;
   private boolean autoOutput;

   public GroovySocketServer(GroovyShell groovy, boolean isScriptFile, String scriptFilenameOrText, boolean autoOutput, int port) {
      this.groovy = groovy;
      this.isScriptFile = isScriptFile;
      this.scriptFilenameOrText = scriptFilenameOrText;
      this.autoOutput = autoOutput;

      try {
         this.url = new URL("http", InetAddress.getLocalHost().getHostAddress(), port, "/");
         System.out.println("groovy is listening on port " + port);
      } catch (IOException var7) {
         var7.printStackTrace();
      }

      (new Thread(this)).start();
   }

   public void run() {
      try {
         ServerSocket serverSocket = new ServerSocket(this.url.getPort());

         while(true) {
            Script script;
            if (this.isScriptFile) {
               GroovyMain gm = new GroovyMain();
               script = this.groovy.parse(DefaultGroovyMethods.getText(gm.huntForTheScriptFile(this.scriptFilenameOrText)));
            } else {
               script = this.groovy.parse(this.scriptFilenameOrText);
            }

            new GroovySocketServer.GroovyClientConnection(script, this.autoOutput, serverSocket.accept());
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }
   }

   class GroovyClientConnection implements Runnable {
      private Script script;
      private Socket socket;
      private BufferedReader reader;
      private PrintWriter writer;
      private boolean autoOutputFlag;

      GroovyClientConnection(Script script, boolean autoOutput, Socket socket) throws IOException {
         this.script = script;
         this.autoOutputFlag = autoOutput;
         this.socket = socket;
         this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         this.writer = new PrintWriter(socket.getOutputStream());
         (new Thread(this, "Groovy client connection - " + socket.getInetAddress().getHostAddress())).start();
      }

      public void run() {
         try {
            String line = null;
            this.script.setProperty("out", this.writer);
            this.script.setProperty("socket", this.socket);
            this.script.setProperty("init", Boolean.TRUE);

            for(; (line = this.reader.readLine()) != null; this.writer.flush()) {
               this.script.setProperty("line", line);
               Object o = this.script.run();
               this.script.setProperty("init", Boolean.FALSE);
               if (o != null) {
                  if ("success".equals(o)) {
                     break;
                  }

                  if (this.autoOutputFlag) {
                     this.writer.println(o);
                  }
               }
            }
         } catch (IOException var51) {
            var51.printStackTrace();
         } finally {
            try {
               this.writer.flush();
               this.writer.close();
            } finally {
               try {
                  this.socket.close();
               } catch (IOException var49) {
                  var49.printStackTrace();
               }

            }
         }

      }
   }
}

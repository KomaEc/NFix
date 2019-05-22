package bsh.util;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Label;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;

public class AWTRemoteApplet extends Applet {
   OutputStream out;
   InputStream in;

   public void init() {
      this.setLayout(new BorderLayout());

      try {
         URL var1 = this.getDocumentBase();
         Socket var2 = new Socket(var1.getHost(), var1.getPort() + 1);
         this.out = var2.getOutputStream();
         this.in = var2.getInputStream();
      } catch (IOException var3) {
         this.add("Center", new Label("Remote Connection Failed", 1));
         return;
      }

      AWTConsole var4 = new AWTConsole(this.in, this.out);
      this.add("Center", var4);
   }
}

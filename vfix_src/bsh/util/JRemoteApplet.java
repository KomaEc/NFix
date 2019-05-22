package bsh.util;

import java.awt.BorderLayout;
import java.awt.Label;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import javax.swing.JApplet;

public class JRemoteApplet extends JApplet {
   OutputStream out;
   InputStream in;

   public void init() {
      this.getContentPane().setLayout(new BorderLayout());

      try {
         URL var1 = this.getDocumentBase();
         Socket var2 = new Socket(var1.getHost(), var1.getPort() + 1);
         this.out = var2.getOutputStream();
         this.in = var2.getInputStream();
      } catch (IOException var3) {
         this.getContentPane().add("Center", new Label("Remote Connection Failed", 1));
         return;
      }

      JConsole var4 = new JConsole(this.in, this.out);
      this.getContentPane().add("Center", var4);
   }
}

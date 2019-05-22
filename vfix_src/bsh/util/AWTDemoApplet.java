package bsh.util;

import bsh.Interpreter;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Component;

public class AWTDemoApplet extends Applet {
   public void init() {
      this.setLayout(new BorderLayout());
      AWTConsole var1 = new AWTConsole();
      this.add("Center", (Component)var1);
      Interpreter var2 = new Interpreter(var1);
      (new Thread(var2)).start();
   }
}

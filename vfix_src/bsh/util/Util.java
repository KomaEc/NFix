package bsh.util;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.Window;

public class Util {
   static Window splashScreen;
   // $FF: synthetic field
   static Class class$bsh$Interpreter;

   public static void startSplashScreen() {
      short var0 = 275;
      short var1 = 148;
      Window var2 = new Window(new Frame());
      var2.pack();
      BshCanvas var3 = new BshCanvas();
      var3.setSize(var0, var1);
      Toolkit var4 = Toolkit.getDefaultToolkit();
      Dimension var5 = var4.getScreenSize();
      var2.setBounds(var5.width / 2 - var0 / 2, var5.height / 2 - var1 / 2, var0, var1);
      var2.add("Center", var3);
      Image var6 = var4.getImage((class$bsh$Interpreter == null ? (class$bsh$Interpreter = class$("bsh.Interpreter")) : class$bsh$Interpreter).getResource("/bsh/util/lib/splash.gif"));
      MediaTracker var7 = new MediaTracker(var3);
      var7.addImage(var6, 0);

      try {
         var7.waitForAll();
      } catch (Exception var9) {
      }

      Graphics var8 = var3.getBufferedGraphics();
      var8.drawImage(var6, 0, 0, var3);
      var2.setVisible(true);
      var2.toFront();
      splashScreen = var2;
   }

   public static void endSplashScreen() {
      if (splashScreen != null) {
         splashScreen.dispose();
      }

   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}

package bsh.util;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.TargetError;
import java.awt.BorderLayout;
import javax.swing.JApplet;

public class JDemoApplet extends JApplet {
   public void init() {
      String var1 = this.getParameter("debug");
      if (var1 != null && var1.equals("true")) {
         Interpreter.DEBUG = true;
      }

      String var2 = this.getParameter("type");
      if (var2 != null && var2.equals("desktop")) {
         try {
            (new Interpreter()).eval("desktop()");
         } catch (TargetError var5) {
            var5.printStackTrace();
            System.out.println(var5.getTarget());
            var5.getTarget().printStackTrace();
         } catch (EvalError var6) {
            System.out.println(var6);
            var6.printStackTrace();
         }
      } else {
         this.getContentPane().setLayout(new BorderLayout());
         JConsole var3 = new JConsole();
         this.getContentPane().add("Center", var3);
         Interpreter var4 = new Interpreter(var3);
         (new Thread(var4)).start();
      }

   }
}

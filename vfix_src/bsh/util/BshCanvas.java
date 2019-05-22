package bsh.util;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.This;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JComponent;

public class BshCanvas extends JComponent {
   This ths;
   Image imageBuffer;

   public BshCanvas() {
   }

   public BshCanvas(This var1) {
      this.ths = var1;
   }

   public void paintComponent(Graphics var1) {
      if (this.imageBuffer != null) {
         var1.drawImage(this.imageBuffer, 0, 0, this);
      }

      if (this.ths != null) {
         try {
            this.ths.invokeMethod("paint", new Object[]{var1});
         } catch (EvalError var3) {
            if (Interpreter.DEBUG) {
               Interpreter.debug("BshCanvas: method invocation error:" + var3);
            }
         }
      }

   }

   public Graphics getBufferedGraphics() {
      Dimension var1 = this.getSize();
      this.imageBuffer = this.createImage(var1.width, var1.height);
      return this.imageBuffer.getGraphics();
   }

   public void setBounds(int var1, int var2, int var3, int var4) {
      this.setPreferredSize(new Dimension(var3, var4));
      this.setMinimumSize(new Dimension(var3, var4));
      super.setBounds(var1, var2, var3, var4);
   }
}

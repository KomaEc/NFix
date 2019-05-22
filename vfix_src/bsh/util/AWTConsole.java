package bsh.util;

import bsh.ConsoleInterface;
import bsh.Interpreter;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.peer.TextComponentPeer;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Vector;

public class AWTConsole extends TextArea implements ConsoleInterface, Runnable, KeyListener {
   private OutputStream outPipe;
   private InputStream inPipe;
   private InputStream in;
   private PrintStream out;
   private StringBuffer line;
   private String startedLine;
   private int textLength;
   private Vector history;
   private int histLine;

   public Reader getIn() {
      return new InputStreamReader(this.in);
   }

   public PrintStream getOut() {
      return this.out;
   }

   public PrintStream getErr() {
      return this.out;
   }

   public AWTConsole(int var1, int var2, InputStream var3, OutputStream var4) {
      super(var1, var2);
      this.line = new StringBuffer();
      this.textLength = 0;
      this.history = new Vector();
      this.histLine = 0;
      this.setFont(new Font("Monospaced", 0, 14));
      this.setEditable(false);
      this.addKeyListener(this);
      this.outPipe = var4;
      if (this.outPipe == null) {
         this.outPipe = new PipedOutputStream();

         try {
            this.in = new PipedInputStream((PipedOutputStream)this.outPipe);
         } catch (IOException var6) {
            this.print("Console internal error...");
         }
      }

      this.inPipe = var3;
      (new Thread(this)).start();
      this.requestFocus();
   }

   public void keyPressed(KeyEvent var1) {
      this.type(var1.getKeyCode(), var1.getKeyChar(), var1.getModifiers());
      var1.consume();
   }

   public AWTConsole() {
      this(12, 80, (InputStream)null, (OutputStream)null);
   }

   public AWTConsole(InputStream var1, OutputStream var2) {
      this(12, 80, var1, var2);
   }

   public void type(int var1, char var2, int var3) {
      switch(var1) {
      case 8:
         if (this.line.length() > 0) {
            this.line.setLength(this.line.length() - 1);
            this.replaceRange("", this.textLength - 1, this.textLength);
            --this.textLength;
         }
         break;
      case 9:
         this.line.append("    ");
         this.append("    ");
         this.textLength += 4;
         break;
      case 10:
         this.enter();
         break;
      case 38:
         this.historyUp();
         break;
      case 40:
         this.historyDown();
         break;
      case 67:
         if ((var3 & 2) > 0) {
            this.line.append("^C");
            this.append("^C");
            this.textLength += 2;
         } else {
            this.doChar(var2);
         }
         break;
      case 85:
         if ((var3 & 2) > 0) {
            int var4 = this.line.length();
            this.replaceRange("", this.textLength - var4, this.textLength);
            this.line.setLength(0);
            this.histLine = 0;
            this.textLength = this.getText().length();
         } else {
            this.doChar(var2);
         }
         break;
      default:
         this.doChar(var2);
      }

   }

   private void doChar(char var1) {
      if (var1 >= ' ' && var1 <= '~') {
         this.line.append(var1);
         this.append(String.valueOf(var1));
         ++this.textLength;
      }

   }

   private void enter() {
      String var1;
      if (this.line.length() == 0) {
         var1 = ";\n";
      } else {
         var1 = this.line + "\n";
         this.history.addElement(this.line.toString());
      }

      this.line.setLength(0);
      this.histLine = 0;
      this.append("\n");
      this.textLength = this.getText().length();
      this.acceptLine(var1);
      this.setCaretPosition(this.textLength);
   }

   public void setCaretPosition(int var1) {
      ((TextComponentPeer)this.getPeer()).setCaretPosition(var1 + this.countNLs());
   }

   private int countNLs() {
      String var1 = this.getText();
      int var2 = 0;

      for(int var3 = 0; var3 < var1.length(); ++var3) {
         if (var1.charAt(var3) == '\n') {
            ++var2;
         }
      }

      return var2;
   }

   private void historyUp() {
      if (this.history.size() != 0) {
         if (this.histLine == 0) {
            this.startedLine = this.line.toString();
         }

         if (this.histLine < this.history.size()) {
            ++this.histLine;
            this.showHistoryLine();
         }

      }
   }

   private void historyDown() {
      if (this.histLine != 0) {
         --this.histLine;
         this.showHistoryLine();
      }
   }

   private void showHistoryLine() {
      String var1;
      if (this.histLine == 0) {
         var1 = this.startedLine;
      } else {
         var1 = (String)this.history.elementAt(this.history.size() - this.histLine);
      }

      this.replaceRange(var1, this.textLength - this.line.length(), this.textLength);
      this.line = new StringBuffer(var1);
      this.textLength = this.getText().length();
   }

   private void acceptLine(String var1) {
      if (this.outPipe == null) {
         this.print("Console internal error...");
      } else {
         try {
            this.outPipe.write(var1.getBytes());
            this.outPipe.flush();
         } catch (IOException var3) {
            this.outPipe = null;
            throw new RuntimeException("Console pipe broken...");
         }
      }

   }

   public void println(Object var1) {
      this.print(var1 + "\n");
   }

   public void error(Object var1) {
      this.print(var1, Color.red);
   }

   public void print(Object var1, Color var2) {
      this.print("*** " + String.valueOf(var1));
   }

   public synchronized void print(Object var1) {
      this.append(String.valueOf(var1));
      this.textLength = this.getText().length();
   }

   private void inPipeWatcher() throws IOException {
      if (this.inPipe == null) {
         PipedOutputStream var1 = new PipedOutputStream();
         this.out = new PrintStream(var1);
         this.inPipe = new PipedInputStream(var1);
      }

      byte[] var3 = new byte[256];

      int var2;
      while((var2 = this.inPipe.read(var3)) != -1) {
         this.print(new String(var3, 0, var2));
      }

      this.println("Console: Input closed...");
   }

   public void run() {
      try {
         this.inPipeWatcher();
      } catch (IOException var2) {
         this.println("Console: I/O Error...");
      }

   }

   public static void main(String[] var0) {
      AWTConsole var1 = new AWTConsole();
      final Frame var2 = new Frame("Bsh Console");
      var2.add(var1, "Center");
      var2.pack();
      var2.show();
      var2.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent var1) {
            var2.dispose();
         }
      });
      Interpreter var3 = new Interpreter(var1);
      var3.run();
   }

   public String toString() {
      return "BeanShell AWTConsole";
   }

   public void keyTyped(KeyEvent var1) {
   }

   public void keyReleased(KeyEvent var1) {
   }
}

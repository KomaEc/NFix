package bsh.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class JConsole extends JScrollPane implements GUIConsoleInterface, Runnable, KeyListener, MouseListener, ActionListener, PropertyChangeListener {
   private static final String CUT = "Cut";
   private static final String COPY = "Copy";
   private static final String PASTE = "Paste";
   private OutputStream outPipe;
   private InputStream inPipe;
   private InputStream in;
   private PrintStream out;
   private int cmdStart;
   private Vector history;
   private String startedLine;
   private int histLine;
   private JPopupMenu menu;
   private JTextPane text;
   private DefaultStyledDocument doc;
   NameCompletion nameCompletion;
   final int SHOW_AMBIG_MAX;
   private boolean gotUp;
   String ZEROS;

   public InputStream getInputStream() {
      return this.in;
   }

   public Reader getIn() {
      return new InputStreamReader(this.in);
   }

   public PrintStream getOut() {
      return this.out;
   }

   public PrintStream getErr() {
      return this.out;
   }

   public JConsole() {
      this((InputStream)null, (OutputStream)null);
   }

   public JConsole(InputStream var1, OutputStream var2) {
      this.cmdStart = 0;
      this.history = new Vector();
      this.histLine = 0;
      this.SHOW_AMBIG_MAX = 10;
      this.gotUp = true;
      this.ZEROS = "000";
      this.text = new JTextPane(this.doc = new DefaultStyledDocument()) {
         public void cut() {
            if (JConsole.this.text.getCaretPosition() < JConsole.this.cmdStart) {
               super.copy();
            } else {
               super.cut();
            }

         }

         public void paste() {
            JConsole.this.forceCaretMoveToEnd();
            super.paste();
         }
      };
      Font var3 = new Font("Monospaced", 0, 14);
      this.text.setText("");
      this.text.setFont(var3);
      this.text.setMargin(new Insets(7, 5, 7, 5));
      this.text.addKeyListener(this);
      this.setViewportView(this.text);
      this.menu = new JPopupMenu("JConsole\tMenu");
      this.menu.add(new JMenuItem("Cut")).addActionListener(this);
      this.menu.add(new JMenuItem("Copy")).addActionListener(this);
      this.menu.add(new JMenuItem("Paste")).addActionListener(this);
      this.text.addMouseListener(this);
      UIManager.addPropertyChangeListener(this);
      this.outPipe = var2;
      if (this.outPipe == null) {
         this.outPipe = new PipedOutputStream();

         try {
            this.in = new PipedInputStream((PipedOutputStream)this.outPipe);
         } catch (IOException var7) {
            this.print("Console internal\terror (1)...", (Color)Color.red);
         }
      }

      this.inPipe = var1;
      if (this.inPipe == null) {
         PipedOutputStream var4 = new PipedOutputStream();
         this.out = new PrintStream(var4);

         try {
            this.inPipe = new JConsole.BlockingPipedInputStream(var4);
         } catch (IOException var6) {
            this.print((Object)("Console internal error: " + var6));
         }
      }

      (new Thread(this)).start();
      this.requestFocus();
   }

   public void requestFocus() {
      super.requestFocus();
      this.text.requestFocus();
   }

   public void keyPressed(KeyEvent var1) {
      this.type(var1);
      this.gotUp = false;
   }

   public void keyTyped(KeyEvent var1) {
      this.type(var1);
   }

   public void keyReleased(KeyEvent var1) {
      this.gotUp = true;
      this.type(var1);
   }

   private synchronized void type(KeyEvent var1) {
      switch(var1.getKeyCode()) {
      case 8:
      case 37:
      case 127:
         if (this.text.getCaretPosition() <= this.cmdStart) {
            var1.consume();
         }
         break;
      case 9:
         if (var1.getID() == 402) {
            String var2 = this.text.getText().substring(this.cmdStart);
            this.doCommandCompletion(var2);
         }

         var1.consume();
         break;
      case 10:
         if (var1.getID() == 401 && this.gotUp) {
            this.enter();
            this.resetCommandStart();
            this.text.setCaretPosition(this.cmdStart);
         }

         var1.consume();
         this.text.repaint();
         break;
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 21:
      case 22:
      case 23:
      case 24:
      case 25:
      case 26:
      case 28:
      case 29:
      case 30:
      case 31:
      case 32:
      case 33:
      case 34:
      case 35:
      case 41:
      case 42:
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 59:
      case 60:
      case 61:
      case 62:
      case 63:
      case 64:
      case 65:
      case 66:
      case 68:
      case 69:
      case 70:
      case 71:
      case 72:
      case 73:
      case 74:
      case 75:
      case 76:
      case 77:
      case 78:
      case 79:
      case 80:
      case 81:
      case 82:
      case 83:
      case 84:
      case 86:
      case 87:
      case 88:
      case 89:
      case 90:
      case 91:
      case 92:
      case 93:
      case 94:
      case 95:
      case 96:
      case 97:
      case 98:
      case 99:
      case 100:
      case 101:
      case 102:
      case 103:
      case 104:
      case 105:
      case 106:
      case 107:
      case 108:
      case 109:
      case 110:
      case 111:
      case 124:
      case 125:
      case 126:
      case 128:
      case 129:
      case 130:
      case 131:
      case 132:
      case 133:
      case 134:
      case 135:
      case 136:
      case 137:
      case 138:
      case 139:
      case 140:
      case 141:
      case 142:
      case 143:
      case 144:
      case 146:
      case 147:
      case 148:
      case 149:
      case 150:
      case 151:
      case 152:
      case 153:
      case 156:
      default:
         if ((var1.getModifiers() & 14) == 0) {
            this.forceCaretMoveToEnd();
         }

         if (var1.paramString().indexOf("Backspace") != -1 && this.text.getCaretPosition() <= this.cmdStart) {
            var1.consume();
         }
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      case 27:
      case 112:
      case 113:
      case 114:
      case 115:
      case 116:
      case 117:
      case 118:
      case 119:
      case 120:
      case 121:
      case 122:
      case 123:
      case 145:
      case 154:
      case 155:
      case 157:
         break;
      case 36:
         this.text.setCaretPosition(this.cmdStart);
         var1.consume();
         break;
      case 38:
         if (var1.getID() == 401) {
            this.historyUp();
         }

         var1.consume();
         break;
      case 39:
         this.forceCaretMoveToStart();
         break;
      case 40:
         if (var1.getID() == 401) {
            this.historyDown();
         }

         var1.consume();
         break;
      case 67:
         if (this.text.getSelectedText() == null) {
            if ((var1.getModifiers() & 2) > 0 && var1.getID() == 401) {
               this.append("^C");
            }

            var1.consume();
         }
         break;
      case 85:
         if ((var1.getModifiers() & 2) > 0) {
            this.replaceRange("", this.cmdStart, this.textLength());
            this.histLine = 0;
            var1.consume();
         }
      }

   }

   private void doCommandCompletion(String var1) {
      if (this.nameCompletion != null) {
         int var2;
         for(var2 = var1.length() - 1; var2 >= 0 && (Character.isJavaIdentifierPart(var1.charAt(var2)) || var1.charAt(var2) == '.'); --var2) {
         }

         var1 = var1.substring(var2 + 1);
         if (var1.length() >= 2) {
            String[] var3 = this.nameCompletion.completeName(var1);
            if (var3.length == 0) {
               Toolkit.getDefaultToolkit().beep();
            } else {
               String var4;
               if (var3.length == 1 && !var3.equals(var1)) {
                  var4 = var3[0].substring(var1.length());
                  this.append(var4);
               } else {
                  var4 = this.text.getText();
                  String var5 = var4.substring(this.cmdStart);

                  for(var2 = this.cmdStart; var4.charAt(var2) != '\n' && var2 > 0; --var2) {
                  }

                  String var6 = var4.substring(var2 + 1, this.cmdStart);
                  StringBuffer var7 = new StringBuffer("\n");

                  for(var2 = 0; var2 < var3.length && var2 < 10; ++var2) {
                     var7.append(var3[var2] + "\n");
                  }

                  if (var2 == 10) {
                     var7.append("...\n");
                  }

                  this.print(var7, (Color)Color.gray);
                  this.print((Object)var6);
                  this.append(var5);
               }
            }
         }
      }
   }

   private void resetCommandStart() {
      this.cmdStart = this.textLength();
   }

   private void append(String var1) {
      int var2 = this.textLength();
      this.text.select(var2, var2);
      this.text.replaceSelection(var1);
   }

   private String replaceRange(Object var1, int var2, int var3) {
      String var4 = var1.toString();
      this.text.select(var2, var3);
      this.text.replaceSelection(var4);
      return var4;
   }

   private void forceCaretMoveToEnd() {
      if (this.text.getCaretPosition() < this.cmdStart) {
         this.text.setCaretPosition(this.textLength());
      }

      this.text.repaint();
   }

   private void forceCaretMoveToStart() {
      if (this.text.getCaretPosition() < this.cmdStart) {
      }

      this.text.repaint();
   }

   private void enter() {
      String var1 = this.getCmd();
      if (var1.length() == 0) {
         var1 = ";\n";
      } else {
         this.history.addElement(var1);
         var1 = var1 + "\n";
      }

      this.append("\n");
      this.histLine = 0;
      this.acceptLine(var1);
      this.text.repaint();
   }

   private String getCmd() {
      String var1 = "";

      try {
         var1 = this.text.getText(this.cmdStart, this.textLength() - this.cmdStart);
      } catch (BadLocationException var3) {
         System.out.println("Internal JConsole Error: " + var3);
      }

      return var1;
   }

   private void historyUp() {
      if (this.history.size() != 0) {
         if (this.histLine == 0) {
            this.startedLine = this.getCmd();
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

      this.replaceRange(var1, this.cmdStart, this.textLength());
      this.text.setCaretPosition(this.textLength());
      this.text.repaint();
   }

   private void acceptLine(String var1) {
      StringBuffer var2 = new StringBuffer();
      int var3 = var1.length();

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = Integer.toString(var1.charAt(var4), 16);
         var5 = this.ZEROS.substring(0, 4 - var5.length()) + var5;
         var2.append("\\u" + var5);
      }

      var1 = var2.toString();
      if (this.outPipe == null) {
         this.print("Console internal\terror: cannot output ...", (Color)Color.red);
      } else {
         try {
            this.outPipe.write(var1.getBytes());
            this.outPipe.flush();
         } catch (IOException var6) {
            this.outPipe = null;
            throw new RuntimeException("Console pipe broken...");
         }
      }

   }

   public void println(Object var1) {
      this.print((Object)(var1 + "\n"));
      this.text.repaint();
   }

   public void print(final Object var1) {
      this.invokeAndWait(new Runnable() {
         public void run() {
            JConsole.this.append(String.valueOf(var1));
            JConsole.this.resetCommandStart();
            JConsole.this.text.setCaretPosition(JConsole.this.cmdStart);
         }
      });
   }

   public void println() {
      this.print((Object)"\n");
      this.text.repaint();
   }

   public void error(Object var1) {
      this.print(var1, Color.red);
   }

   public void println(Icon var1) {
      this.print(var1);
      this.println();
      this.text.repaint();
   }

   public void print(final Icon var1) {
      if (var1 != null) {
         this.invokeAndWait(new Runnable() {
            public void run() {
               JConsole.this.text.insertIcon(var1);
               JConsole.this.resetCommandStart();
               JConsole.this.text.setCaretPosition(JConsole.this.cmdStart);
            }
         });
      }
   }

   public void print(Object var1, Font var2) {
      this.print(var1, var2, (Color)null);
   }

   public void print(Object var1, Color var2) {
      this.print(var1, (Font)null, var2);
   }

   public void print(final Object var1, final Font var2, final Color var3) {
      this.invokeAndWait(new Runnable() {
         public void run() {
            AttributeSet var1x = JConsole.this.getStyle();
            JConsole.this.setStyle(var2, var3);
            JConsole.this.append(String.valueOf(var1));
            JConsole.this.resetCommandStart();
            JConsole.this.text.setCaretPosition(JConsole.this.cmdStart);
            JConsole.this.setStyle(var1x, true);
         }
      });
   }

   public void print(Object var1, String var2, int var3, Color var4) {
      this.print(var1, var2, var3, var4, false, false, false);
   }

   public void print(final Object var1, final String var2, final int var3, final Color var4, final boolean var5, final boolean var6, final boolean var7) {
      this.invokeAndWait(new Runnable() {
         public void run() {
            AttributeSet var1x = JConsole.this.getStyle();
            JConsole.this.setStyle(var2, var3, var4, var5, var6, var7);
            JConsole.this.append(String.valueOf(var1));
            JConsole.this.resetCommandStart();
            JConsole.this.text.setCaretPosition(JConsole.this.cmdStart);
            JConsole.this.setStyle(var1x, true);
         }
      });
   }

   private AttributeSet setStyle(Font var1) {
      return this.setStyle(var1, (Color)null);
   }

   private AttributeSet setStyle(Color var1) {
      return this.setStyle((Font)null, var1);
   }

   private AttributeSet setStyle(Font var1, Color var2) {
      return var1 != null ? this.setStyle(var1.getFamily(), var1.getSize(), var2, var1.isBold(), var1.isItalic(), StyleConstants.isUnderline(this.getStyle())) : this.setStyle((String)null, -1, var2);
   }

   private AttributeSet setStyle(String var1, int var2, Color var3) {
      SimpleAttributeSet var4 = new SimpleAttributeSet();
      if (var3 != null) {
         StyleConstants.setForeground(var4, var3);
      }

      if (var1 != null) {
         StyleConstants.setFontFamily(var4, var1);
      }

      if (var2 != -1) {
         StyleConstants.setFontSize(var4, var2);
      }

      this.setStyle((AttributeSet)var4);
      return this.getStyle();
   }

   private AttributeSet setStyle(String var1, int var2, Color var3, boolean var4, boolean var5, boolean var6) {
      SimpleAttributeSet var7 = new SimpleAttributeSet();
      if (var3 != null) {
         StyleConstants.setForeground(var7, var3);
      }

      if (var1 != null) {
         StyleConstants.setFontFamily(var7, var1);
      }

      if (var2 != -1) {
         StyleConstants.setFontSize(var7, var2);
      }

      StyleConstants.setBold(var7, var4);
      StyleConstants.setItalic(var7, var5);
      StyleConstants.setUnderline(var7, var6);
      this.setStyle((AttributeSet)var7);
      return this.getStyle();
   }

   private void setStyle(AttributeSet var1) {
      this.setStyle(var1, false);
   }

   private void setStyle(AttributeSet var1, boolean var2) {
      this.text.setCharacterAttributes(var1, var2);
   }

   private AttributeSet getStyle() {
      return this.text.getCharacterAttributes();
   }

   public void setFont(Font var1) {
      super.setFont(var1);
      if (this.text != null) {
         this.text.setFont(var1);
      }

   }

   private void inPipeWatcher() throws IOException {
      byte[] var1 = new byte[256];

      int var2;
      while((var2 = this.inPipe.read(var1)) != -1) {
         this.print((Object)(new String(var1, 0, var2)));
      }

      this.println((Object)"Console: Input\tclosed...");
   }

   public void run() {
      try {
         this.inPipeWatcher();
      } catch (IOException var2) {
         this.print("Console: I/O Error: " + var2 + "\n", (Color)Color.red);
      }

   }

   public String toString() {
      return "BeanShell console";
   }

   public void mouseClicked(MouseEvent var1) {
   }

   public void mousePressed(MouseEvent var1) {
      if (var1.isPopupTrigger()) {
         this.menu.show((Component)var1.getSource(), var1.getX(), var1.getY());
      }

   }

   public void mouseReleased(MouseEvent var1) {
      if (var1.isPopupTrigger()) {
         this.menu.show((Component)var1.getSource(), var1.getX(), var1.getY());
      }

      this.text.repaint();
   }

   public void mouseEntered(MouseEvent var1) {
   }

   public void mouseExited(MouseEvent var1) {
   }

   public void propertyChange(PropertyChangeEvent var1) {
      if (var1.getPropertyName().equals("lookAndFeel")) {
         SwingUtilities.updateComponentTreeUI(this.menu);
      }

   }

   public void actionPerformed(ActionEvent var1) {
      String var2 = var1.getActionCommand();
      if (var2.equals("Cut")) {
         this.text.cut();
      } else if (var2.equals("Copy")) {
         this.text.copy();
      } else if (var2.equals("Paste")) {
         this.text.paste();
      }

   }

   private void invokeAndWait(Runnable var1) {
      if (!SwingUtilities.isEventDispatchThread()) {
         try {
            SwingUtilities.invokeAndWait(var1);
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      } else {
         var1.run();
      }

   }

   public void setNameCompletion(NameCompletion var1) {
      this.nameCompletion = var1;
   }

   public void setWaitFeedback(boolean var1) {
      if (var1) {
         this.setCursor(Cursor.getPredefinedCursor(3));
      } else {
         this.setCursor(Cursor.getPredefinedCursor(0));
      }

   }

   private int textLength() {
      return this.text.getDocument().getLength();
   }

   public static class BlockingPipedInputStream extends PipedInputStream {
      boolean closed;

      public BlockingPipedInputStream(PipedOutputStream var1) throws IOException {
         super(var1);
      }

      public synchronized int read() throws IOException {
         if (this.closed) {
            throw new IOException("stream closed");
         } else {
            while(super.in < 0) {
               this.notifyAll();

               try {
                  this.wait(750L);
               } catch (InterruptedException var2) {
                  throw new InterruptedIOException();
               }
            }

            int var1 = this.buffer[super.out++] & 255;
            if (super.out >= this.buffer.length) {
               super.out = 0;
            }

            if (super.in == super.out) {
               super.in = -1;
            }

            return var1;
         }
      }

      public void close() throws IOException {
         this.closed = true;
         super.close();
      }
   }
}

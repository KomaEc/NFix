package com.gzoltar.shaded.jline;

import com.gzoltar.shaded.jline.internal.Configuration;
import com.gzoltar.shaded.jline.internal.Log;
import com.gzoltar.shaded.org.fusesource.jansi.internal.Kernel32;
import com.gzoltar.shaded.org.fusesource.jansi.internal.WindowsSupport;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class WindowsTerminal extends TerminalSupport {
   public static final String DIRECT_CONSOLE = WindowsTerminal.class.getName() + ".directConsole";
   public static final String ANSI = WindowsTerminal.class.getName() + ".ansi";
   private boolean directConsole;
   private int originalMode;

   public WindowsTerminal() throws Exception {
      super(true);
   }

   public void init() throws Exception {
      super.init();
      this.setAnsiSupported(Configuration.getBoolean(ANSI, true));
      this.setDirectConsole(Configuration.getBoolean(DIRECT_CONSOLE, true));
      this.originalMode = this.getConsoleMode();
      this.setConsoleMode(this.originalMode & ~WindowsTerminal.ConsoleMode.ENABLE_ECHO_INPUT.code);
      this.setEchoEnabled(false);
   }

   public void restore() throws Exception {
      this.setConsoleMode(this.originalMode);
      super.restore();
   }

   public int getWidth() {
      int w = this.getWindowsTerminalWidth();
      return w < 1 ? 80 : w;
   }

   public int getHeight() {
      int h = this.getWindowsTerminalHeight();
      return h < 1 ? 24 : h;
   }

   public void setEchoEnabled(boolean enabled) {
      if (enabled) {
         this.setConsoleMode(this.getConsoleMode() | WindowsTerminal.ConsoleMode.ENABLE_ECHO_INPUT.code | WindowsTerminal.ConsoleMode.ENABLE_LINE_INPUT.code | WindowsTerminal.ConsoleMode.ENABLE_PROCESSED_INPUT.code | WindowsTerminal.ConsoleMode.ENABLE_WINDOW_INPUT.code);
      } else {
         this.setConsoleMode(this.getConsoleMode() & ~(WindowsTerminal.ConsoleMode.ENABLE_LINE_INPUT.code | WindowsTerminal.ConsoleMode.ENABLE_ECHO_INPUT.code | WindowsTerminal.ConsoleMode.ENABLE_PROCESSED_INPUT.code | WindowsTerminal.ConsoleMode.ENABLE_WINDOW_INPUT.code));
      }

      super.setEchoEnabled(enabled);
   }

   public void setDirectConsole(boolean flag) {
      this.directConsole = flag;
      Log.debug("Direct console: ", flag);
   }

   public Boolean getDirectConsole() {
      return this.directConsole;
   }

   public InputStream wrapInIfNeeded(InputStream in) throws IOException {
      return this.directConsole && this.isSystemIn(in) ? new InputStream() {
         private byte[] buf = null;
         int bufIdx = 0;

         public int read() throws IOException {
            while(this.buf == null || this.bufIdx == this.buf.length) {
               this.buf = WindowsTerminal.this.readConsoleInput();
               this.bufIdx = 0;
            }

            int c = this.buf[this.bufIdx] & 255;
            ++this.bufIdx;
            return c;
         }
      } : super.wrapInIfNeeded(in);
   }

   protected boolean isSystemIn(InputStream in) throws IOException {
      if (in == null) {
         return false;
      } else if (in == System.in) {
         return true;
      } else {
         return in instanceof FileInputStream && ((FileInputStream)in).getFD() == FileDescriptor.in;
      }
   }

   public String getOutputEncoding() {
      int codepage = this.getConsoleOutputCodepage();
      String charsetMS = "ms" + codepage;
      if (Charset.isSupported(charsetMS)) {
         return charsetMS;
      } else {
         String charsetCP = "cp" + codepage;
         if (Charset.isSupported(charsetCP)) {
            return charsetCP;
         } else {
            Log.debug("can't figure out the Java Charset of this code page (" + codepage + ")...");
            return super.getOutputEncoding();
         }
      }
   }

   private int getConsoleMode() {
      return WindowsSupport.getConsoleMode();
   }

   private void setConsoleMode(int mode) {
      WindowsSupport.setConsoleMode(mode);
   }

   private byte[] readConsoleInput() {
      Kernel32.INPUT_RECORD[] events = null;

      try {
         events = WindowsSupport.readConsoleInput(1);
      } catch (IOException var7) {
         Log.debug("read Windows console input error: ", var7);
      }

      if (events == null) {
         return new byte[0];
      } else {
         StringBuilder sb = new StringBuilder();

         for(int i = 0; i < events.length; ++i) {
            Kernel32.KEY_EVENT_RECORD keyEvent = events[i].keyEvent;
            if (keyEvent.keyDown) {
               int k;
               if (keyEvent.uchar <= 0) {
                  String escapeSequence = null;
                  switch(keyEvent.keyCode) {
                  case 33:
                     escapeSequence = "\u001b[5~";
                     break;
                  case 34:
                     escapeSequence = "\u001b[6~";
                     break;
                  case 35:
                     escapeSequence = "\u001b[4~";
                     break;
                  case 36:
                     escapeSequence = "\u001b[1~";
                     break;
                  case 37:
                     escapeSequence = "\u001b[D";
                     break;
                  case 38:
                     escapeSequence = "\u001b[A";
                     break;
                  case 39:
                     escapeSequence = "\u001b[C";
                     break;
                  case 40:
                     escapeSequence = "\u001b[B";
                  case 41:
                  case 42:
                  case 43:
                  case 44:
                  default:
                     break;
                  case 45:
                     escapeSequence = "\u001b[2~";
                     break;
                  case 46:
                     escapeSequence = "\u001b[3~";
                  }

                  if (escapeSequence != null) {
                     for(k = 0; k < keyEvent.repeatCount; ++k) {
                        sb.append(escapeSequence);
                     }
                  }
               } else {
                  int altState = Kernel32.KEY_EVENT_RECORD.LEFT_ALT_PRESSED | Kernel32.KEY_EVENT_RECORD.RIGHT_ALT_PRESSED;
                  k = Kernel32.KEY_EVENT_RECORD.LEFT_CTRL_PRESSED | Kernel32.KEY_EVENT_RECORD.RIGHT_CTRL_PRESSED;
                  if ((keyEvent.uchar >= '@' && keyEvent.uchar <= '_' || keyEvent.uchar >= 'a' && keyEvent.uchar <= 'z') && (keyEvent.controlKeyState & altState) != 0 && (keyEvent.controlKeyState & k) == 0) {
                     sb.append('\u001b');
                  }

                  sb.append(keyEvent.uchar);
               }
            } else if (keyEvent.keyCode == 18 && keyEvent.uchar > 0) {
               sb.append(keyEvent.uchar);
            }
         }

         return sb.toString().getBytes();
      }
   }

   private int getConsoleOutputCodepage() {
      return Kernel32.GetConsoleOutputCP();
   }

   private int getWindowsTerminalWidth() {
      return WindowsSupport.getWindowsTerminalWidth();
   }

   private int getWindowsTerminalHeight() {
      return WindowsSupport.getWindowsTerminalHeight();
   }

   public static enum ConsoleMode {
      ENABLE_LINE_INPUT(2),
      ENABLE_ECHO_INPUT(4),
      ENABLE_PROCESSED_INPUT(1),
      ENABLE_WINDOW_INPUT(8),
      ENABLE_MOUSE_INPUT(16),
      ENABLE_PROCESSED_OUTPUT(1),
      ENABLE_WRAP_AT_EOL_OUTPUT(2);

      public final int code;

      private ConsoleMode(int code) {
         this.code = code;
      }
   }
}

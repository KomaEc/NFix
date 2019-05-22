package com.mks.connect;

import com.mks.api.IntegrationPointFactory;
import com.mks.api.util.MKSLogger;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;

public abstract class BlimpInputStream extends InputStream {
   private static final String UNKNOWN_COMMAND = "Unknown Blimp command request: command code={0}";
   private static final String UNEXPECTED_COMMAND = "Unsupported Blimp command request: {0}";
   protected static final String UNEXPECTED_HTTP_STATUS = "Unexpected HTTP status: {0}";
   protected static final String INVALID_APP = "Invalid App Name: {0}";
   private AbstractCmdRunner cmdRunner;
   private InputStream input;
   private String inputLocale = Locale.getDefault().toString();
   private int numLeftForCmdStdout;
   private boolean finished = false;
   protected boolean generateSubRtns;
   private String[] args;
   private String appName;
   private MKSLogger apiLogger;

   protected BlimpInputStream(AbstractCmdRunner cmdRunner, String[] cmd) {
      this.cmdRunner = cmdRunner;
      this.args = cmd;
      this.appName = cmd[0];
      this.apiLogger = IntegrationPointFactory.getLogger();
   }

   protected void setCodePage(String inputLocale) {
      this.inputLocale = inputLocale;
   }

   protected void setGenerateSubRoutines(boolean generateSubRtns) {
      this.generateSubRtns = generateSubRtns;
   }

   protected abstract void blimpInterrupt(String var1) throws IOException;

   private void interrupt() throws IOException {
      this.blimpInterrupt(this.appName);
      this.cmdRunner.setInterrupted();

      try {
         while(!this.isFinished() && this.read() != -1 && this.cmdRunner.isInterrupted()) {
         }
      } catch (IOException var5) {
      } finally {
         this.cmdRunner.resetInterrupt();
      }

   }

   public void close() throws IOException {
      try {
         if (!this.isFinished()) {
            this.interrupt();
         }
      } finally {
         this.cleanup();
         this.numLeftForCmdStdout = -1;
      }

   }

   public boolean isFinished() {
      return this.finished;
   }

   public int read() throws IOException {
      byte[] buffer = new byte[1];
      int numRead = this.read(buffer, 0, 1);
      return numRead == -1 ? -1 : buffer[0];
   }

   public int read(byte[] buf) throws IOException {
      return this.read(buf, 0, buf.length);
   }

   public int read(byte[] buffer, int offset, int length) throws IOException {
      int numRead;
      for(numRead = this.readFromCmdStdout(buffer, offset, length); numRead == 0; numRead = this.readFromCmdStdout(buffer, offset, length)) {
         this.handleNextBlimpCommand();
      }

      return numRead;
   }

   private int readFromCmdStdout(byte[] buffer, int offset, int length) throws IOException {
      if (this.numLeftForCmdStdout <= 0) {
         return this.numLeftForCmdStdout;
      } else {
         int numToRead = Math.min(length, this.numLeftForCmdStdout);
         int numRead = this.readNoEof(buffer, offset, numToRead);
         this.numLeftForCmdStdout -= numRead;
         return numRead;
      }
   }

   private InputStream getInputStream() throws IOException {
      if (this.input == null) {
         this.input = this.blimpInitiate(this.appName);
      }

      return this.input;
   }

   protected abstract InputStream blimpInitiate(String var1) throws IOException;

   protected abstract void blimpTerminate(InputStream var1) throws IOException;

   protected abstract InputStream blimpResponse(InputStream var1) throws IOException;

   private void handleNextBlimpCommand() throws IOException {
      int cmd = this.readNoEof();
      int dataLen = this.readDataLength();
      this.apiLogger.message("DEBUG", 10, this + ": got cmd: " + cmd + ", length: " + dataLen);
      switch(cmd) {
      case 0:
         this.cmdGetStatus();
         break;
      case 1:
         this.cmdGetEnv();
         break;
      case 2:
         this.cmdGetArgs();
         break;
      case 3:
         this.cmdGetCwd();
         break;
      case 4:
         this.cmdWriteStdout(dataLen, false);
         break;
      case 5:
         this.cmdWriteStderr(dataLen, false);
         break;
      case 6:
         this.cmdReadLine(dataLen);
         break;
      case 7:
         this.cmdReadMultiLine(dataLen);
         break;
      case 8:
         this.cmdReadSecret(dataLen);
         break;
      case 9:
         this.cmdExit();
         break;
      case 10:
         this.cmdResetIntr();
         break;
      case 11:
         this.cmdWriteStdout(dataLen, true);
         break;
      case 12:
         this.cmdWriteStderr(dataLen, true);
         break;
      case 13:
         this.cmdExec(dataLen);
         break;
      case 14:
         this.cmdReadFile(dataLen);
         break;
      case 15:
         this.cmdWriteFile(dataLen);
         break;
      case 16:
         this.cmdGetCwdAndArgs();
         break;
      default:
         throw new BlimpException(MessageFormat.format("Unknown Blimp command request: command code={0}", String.valueOf(cmd)));
      }

   }

   private void cmdGetStatus() throws IOException {
      this.writeResponse();
   }

   private void cmdGetEnv() throws IOException {
      this.interrupt();
      this.writeResponse();
      throw new BlimpException(MessageFormat.format("Unsupported Blimp command request: {0}", "cmdGetEnv"));
   }

   private void cmdGetArgs() throws IOException {
      this.writeResponse(this.args);
   }

   private void cmdGetCwd() throws IOException {
      String directory = System.getProperty("user.dir");
      this.writeResponse(directory);
   }

   private void cmdGetCwdAndArgs() throws IOException {
      String cwd = System.getProperty("user.dir");
      String[] cwdAndArgs = new String[this.args.length + 1];
      System.arraycopy(this.args, 0, cwdAndArgs, 1, this.args.length);
      cwdAndArgs[0] = cwd;
      this.writeResponse(cwdAndArgs);
   }

   private void cmdWriteStdout(int dataLength, boolean addNewLine) throws IOException {
      this.numLeftForCmdStdout = dataLength;
   }

   private void cmdWriteStderr(int dataLength, boolean addNewLine) throws IOException {
      if (dataLength != 0) {
         int bufferSize = Math.min(dataLength, 1024);
         byte[] buffer = new byte[bufferSize];
         int numRead = false;

         int numRead;
         for(int totalNumRead = 0; totalNumRead < dataLength; totalNumRead += numRead) {
            int numToRead = Math.min(dataLength - totalNumRead, bufferSize);
            numRead = this.readNoEof(buffer, 0, numToRead);
            this.apiLogger.message(new String(buffer, 0, numRead));
         }

      }
   }

   private void cmdReadLine(int dataLength) throws IOException {
      this.interrupt();
      this.writeResponse();
      throw new BlimpException(MessageFormat.format("Unsupported Blimp command request: {0}", "cmdReadLine"));
   }

   private void cmdReadMultiLine(int dataLength) throws IOException {
      this.interrupt();
      this.writeResponse();
      throw new BlimpException(MessageFormat.format("Unsupported Blimp command request: {0}", "cmdReadMultiLine"));
   }

   private void cmdReadSecret(int dataLength) throws IOException {
      this.interrupt();
      this.writeResponse();
      throw new BlimpException(MessageFormat.format("Unsupported Blimp command request: {0}", "cmdReadSecret"));
   }

   private void cmdResetIntr() throws IOException {
      this.cmdRunner.resetInterrupt();
      this.writeResponse();
   }

   private File cmdFile(int datalen) throws IOException {
      byte[] bytes = new byte[datalen];

      for(int i = 0; i < datalen; ++i) {
         int c = this.readNoEof();
         if (c == 0) {
            break;
         }

         bytes[i] = (byte)c;
      }

      String filename = new String(bytes, this.inputLocale);
      File f = new File(filename);
      if (!f.isAbsolute()) {
         f = new File(new File(System.getProperty("user.dir")), filename);
      }

      return f;
   }

   private void cmdReadFile(int datalen) throws IOException {
      File f = this.cmdFile(datalen);
      if (!f.exists()) {
         this.writeResponse(2);
      } else if (!f.canRead()) {
         this.writeResponse(3);
      } else {
         long size = f.length();
         long mtime = f.lastModified() / 1000L;
         byte[] data = new byte[]{(byte)((int)(size >> 56)), (byte)((int)(size >> 48)), (byte)((int)(size >> 40)), (byte)((int)(size >> 32)), (byte)((int)(size >> 24)), (byte)((int)(size >> 16)), (byte)((int)(size >> 8)), (byte)((int)size), (byte)((int)(mtime >> 24)), (byte)((int)(mtime >> 16)), (byte)((int)(mtime >> 8)), (byte)((int)mtime)};
         InputStream response = new SequenceInputStream(new ByteArrayInputStream(data), new FileInputStream(f));
         this.writeResponse((InputStream)response, data.length);
      }
   }

   private void cmdWriteFile(int datalen) throws IOException {
      File f = this.cmdFile(datalen);
      File parent = f.getParentFile();
      if (parent != null && !parent.exists()) {
         this.writeResponse(2);
      } else if ((!f.exists() || f.canWrite()) && (parent == null || parent.canWrite())) {
         FileOutputStream os = null;

         try {
            byte[] data = new byte[12];
            int pos = 0;

            int num;
            for(int length = data.length; (num = this.readNoEof(data, pos, length)) > 0; length -= num) {
               pos += num;
            }

            byte[] buf = new byte[1024];
            long fileLength = (long)((data[0] & 255) << 56 | (data[1] & 255) << 48 | (data[2] & 255) << 40 | (data[3] & 255) << 32 | (data[4] & 255) << 24 | (data[5] & 255) << 16 | (data[6] & 255) << 8 | data[7] & 255);
            long mtime = (long)((data[8] & 255) << 24 | (data[9] & 255) << 16 | (data[10] & 255) << 8 | data[11] & 255);
            mtime *= 1000L;

            for(os = new FileOutputStream(f); fileLength > 0L && (num = this.readNoEof(buf, 0, Math.min(buf.length, (int)fileLength))) > 0; fileLength -= (long)num) {
               os.write(buf, 0, num);
            }

            f.setLastModified(mtime);
         } finally {
            try {
               this.writeResponse();
            } finally {
               if (os != null) {
                  os.close();
               }

            }
         }

      } else {
         this.writeResponse(3);
      }
   }

   private void cmdExit() throws IOException {
      this.readNoEof();
      this.numLeftForCmdStdout = -1;
      this.cleanup();
   }

   private void cmdExec(int datalen) throws IOException {
      ArrayList ar = new ArrayList();
      StringBuffer sb = new StringBuffer();

      int i;
      for(int i = 0; i < datalen; ++i) {
         i = this.readNoEof();
         if (i == 0) {
            ar.add(sb.toString());
            sb = new StringBuffer();
         } else {
            sb.append((char)i);
         }
      }

      String[] args = new String[ar.size()];

      for(i = 0; i < ar.size(); ++i) {
         args[i] = (String)ar.get(i);
      }

      this.apiLogger.message("DEBUG", 10, "Execing: " + ar);

      try {
         Process p = Runtime.getRuntime().exec(args);
         new BlimpInputStream.InputStreamGobbler(p.getErrorStream(), "ERROR");
         new BlimpInputStream.InputStreamGobbler(p.getInputStream(), "OUTPUT");
         byte[] resp = new byte[]{(byte)p.waitFor()};
         this.writeResponse(resp);
      } catch (InterruptedException var7) {
         this.writeResponse(new byte[]{-128});
      }

   }

   protected void cleanup() throws IOException {
      this.finished = true;

      try {
         if (this.input != null) {
            try {
               this.blimpTerminate(this.input);
            } finally {
               this.input = null;
            }
         }
      } finally {
         super.close();
      }

   }

   private int readNoEof() throws IOException {
      int b = this.getInputStream().read();
      if (b == -1) {
         throw new EOFException();
      } else {
         return b;
      }
   }

   private int readNoEof(byte[] buffer, int offset, int length) throws IOException {
      if (length == 0) {
         return 0;
      } else {
         int numRead = this.getInputStream().read(buffer, offset, length);
         if (numRead == -1) {
            throw new EOFException();
         } else {
            return numRead;
         }
      }
   }

   private int readDataLength() throws IOException {
      int length = 0;

      for(int i = 2; i >= 0; --i) {
         int c = this.readNoEof();
         length += c << i * 8;
      }

      return length;
   }

   private void writeResponse() throws IOException {
      this.writeResponse(new byte[0]);
   }

   private void writeResponse(String[] responseArgs) throws IOException {
      String response = "";

      for(int i = 0; i < responseArgs.length; ++i) {
         response = response + responseArgs[i] + '\u0000';
      }

      this.writeResponse(response.getBytes(this.inputLocale));
   }

   private void writeResponse(int errorCode) throws IOException {
      this.writeResponse(new byte[0], errorCode);
   }

   private void writeResponse(String message) throws IOException {
      String response = message + '\u0000';
      this.writeResponse(response.getBytes(this.inputLocale));
   }

   private void writeResponse(byte[] response) throws IOException {
      this.writeResponse((byte[])response, 0);
   }

   private void writeResponse(byte[] response, int errorCode) throws IOException {
      this.writeResponse(new ByteArrayInputStream(response), response.length, errorCode);
   }

   private void writeResponse(InputStream response, int length) throws IOException {
      this.writeResponse(response, length, 0);
   }

   private void writeResponse(InputStream response, int dataLen, int errorCode) throws IOException {
      if (this.input != null) {
         try {
            this.blimpTerminate(this.input);
         } finally {
            this.input = null;
         }
      }

      byte[] fullResponse = new byte[4];
      if (this.cmdRunner.isInterrupted()) {
         fullResponse[0] = 1;
      } else {
         fullResponse[0] = (byte)errorCode;
      }

      fullResponse[1] = (byte)(dataLen >> 16);
      fullResponse[2] = (byte)(dataLen >> 8);
      fullResponse[3] = (byte)dataLen;
      InputStream resp = new SequenceInputStream(new ByteArrayInputStream(fullResponse), response);
      this.input = this.blimpResponse(resp);
   }

   private class InputStreamGobbler extends Thread {
      private BufferedReader br;
      private String prefix;

      public InputStreamGobbler(InputStream is, String prefix) {
         this.br = new BufferedReader(new InputStreamReader(is));
         this.prefix = prefix;
         this.start();
      }

      public void run() {
         while(true) {
            while(true) {
               try {
                  String str = this.br.readLine();
                  if (str != null) {
                     BlimpInputStream.this.apiLogger.message("DEBUG", 10, this.prefix + ": " + str);
                     continue;
                  }
               } catch (EOFException var2) {
               } catch (IOException var3) {
                  BlimpInputStream.this.apiLogger.exception("DEBUG", var3);
                  continue;
               } catch (Throwable var4) {
                  BlimpInputStream.this.apiLogger.exception("DEBUG", var4);
                  continue;
               }

               return;
            }
         }
      }
   }
}

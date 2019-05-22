package org.apache.tools.ant.taskdefs;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.filters.util.ChainReaderHelper;
import org.apache.tools.ant.util.ConcatFileInputStream;
import org.apache.tools.ant.util.KeepAliveOutputStream;
import org.apache.tools.ant.util.LazyFileOutputStream;
import org.apache.tools.ant.util.LeadPipeInputStream;
import org.apache.tools.ant.util.OutputStreamFunneler;
import org.apache.tools.ant.util.ReaderInputStream;
import org.apache.tools.ant.util.StringUtils;
import org.apache.tools.ant.util.TeeOutputStream;

public class Redirector {
   private static final String DEFAULT_ENCODING = System.getProperty("file.encoding");
   private File[] input;
   private File[] out;
   private File[] error;
   private boolean logError;
   private Redirector.PropertyOutputStream baos;
   private Redirector.PropertyOutputStream errorBaos;
   private String outputProperty;
   private String errorProperty;
   private String inputString;
   private boolean append;
   private boolean alwaysLog;
   private boolean createEmptyFiles;
   private ProjectComponent managingTask;
   private OutputStream outputStream;
   private OutputStream errorStream;
   private InputStream inputStream;
   private PrintStream outPrintStream;
   private PrintStream errorPrintStream;
   private Vector outputFilterChains;
   private Vector errorFilterChains;
   private Vector inputFilterChains;
   private String outputEncoding;
   private String errorEncoding;
   private String inputEncoding;
   private boolean appendProperties;
   private ThreadGroup threadGroup;
   private boolean logInputString;

   public Redirector(Task managingTask) {
      this((ProjectComponent)managingTask);
   }

   public Redirector(ProjectComponent managingTask) {
      this.logError = false;
      this.baos = null;
      this.errorBaos = null;
      this.append = false;
      this.alwaysLog = false;
      this.createEmptyFiles = true;
      this.outputStream = null;
      this.errorStream = null;
      this.inputStream = null;
      this.outPrintStream = null;
      this.errorPrintStream = null;
      this.outputEncoding = DEFAULT_ENCODING;
      this.errorEncoding = DEFAULT_ENCODING;
      this.inputEncoding = DEFAULT_ENCODING;
      this.appendProperties = true;
      this.threadGroup = new ThreadGroup("redirector");
      this.logInputString = true;
      this.managingTask = managingTask;
   }

   public void setInput(File input) {
      this.setInput(input == null ? null : new File[]{input});
   }

   public synchronized void setInput(File[] input) {
      this.input = input;
   }

   public synchronized void setInputString(String inputString) {
      this.inputString = inputString;
   }

   public void setLogInputString(boolean logInputString) {
      this.logInputString = logInputString;
   }

   void setInputStream(InputStream inputStream) {
      this.inputStream = inputStream;
   }

   public void setOutput(File out) {
      this.setOutput(out == null ? null : new File[]{out});
   }

   public synchronized void setOutput(File[] out) {
      this.out = out;
   }

   public synchronized void setOutputEncoding(String outputEncoding) {
      if (outputEncoding == null) {
         throw new IllegalArgumentException("outputEncoding must not be null");
      } else {
         this.outputEncoding = outputEncoding;
      }
   }

   public synchronized void setErrorEncoding(String errorEncoding) {
      if (errorEncoding == null) {
         throw new IllegalArgumentException("errorEncoding must not be null");
      } else {
         this.errorEncoding = errorEncoding;
      }
   }

   public synchronized void setInputEncoding(String inputEncoding) {
      if (inputEncoding == null) {
         throw new IllegalArgumentException("inputEncoding must not be null");
      } else {
         this.inputEncoding = inputEncoding;
      }
   }

   public synchronized void setLogError(boolean logError) {
      this.logError = logError;
   }

   public synchronized void setAppendProperties(boolean appendProperties) {
      this.appendProperties = appendProperties;
   }

   public void setError(File error) {
      this.setError(error == null ? null : new File[]{error});
   }

   public synchronized void setError(File[] error) {
      this.error = error;
   }

   public synchronized void setOutputProperty(String outputProperty) {
      if (outputProperty == null || !outputProperty.equals(this.outputProperty)) {
         this.outputProperty = outputProperty;
         this.baos = null;
      }

   }

   public synchronized void setAppend(boolean append) {
      this.append = append;
   }

   public synchronized void setAlwaysLog(boolean alwaysLog) {
      this.alwaysLog = alwaysLog;
   }

   public synchronized void setCreateEmptyFiles(boolean createEmptyFiles) {
      this.createEmptyFiles = createEmptyFiles;
   }

   public synchronized void setErrorProperty(String errorProperty) {
      if (errorProperty == null || !errorProperty.equals(this.errorProperty)) {
         this.errorProperty = errorProperty;
         this.errorBaos = null;
      }

   }

   public synchronized void setInputFilterChains(Vector inputFilterChains) {
      this.inputFilterChains = inputFilterChains;
   }

   public synchronized void setOutputFilterChains(Vector outputFilterChains) {
      this.outputFilterChains = outputFilterChains;
   }

   public synchronized void setErrorFilterChains(Vector errorFilterChains) {
      this.errorFilterChains = errorFilterChains;
   }

   private void setPropertyFromBAOS(ByteArrayOutputStream baos, String propertyName) throws IOException {
      BufferedReader in = new BufferedReader(new StringReader(Execute.toString(baos)));
      String line = null;

      StringBuffer val;
      for(val = new StringBuffer(); (line = in.readLine()) != null; val.append(line)) {
         if (val.length() != 0) {
            val.append(StringUtils.LINE_SEP);
         }
      }

      this.managingTask.getProject().setNewProperty(propertyName, val.toString());
   }

   public synchronized void createStreams() {
      String logHead;
      if (this.out != null && this.out.length > 0) {
         logHead = "Output " + (this.append ? "appended" : "redirected") + " to ";
         this.outputStream = this.foldFiles(this.out, logHead, 3);
      }

      KeepAliveOutputStream keepAliveError;
      if (this.outputProperty != null) {
         if (this.baos == null) {
            this.baos = new Redirector.PropertyOutputStream(this.outputProperty);
            this.managingTask.log("Output redirected to property: " + this.outputProperty, 3);
         }

         keepAliveError = new KeepAliveOutputStream(this.baos);
         this.outputStream = (OutputStream)(this.outputStream == null ? keepAliveError : new TeeOutputStream(this.outputStream, keepAliveError));
      } else {
         this.baos = null;
      }

      if (this.error != null && this.error.length > 0) {
         logHead = "Error " + (this.append ? "appended" : "redirected") + " to ";
         this.errorStream = this.foldFiles(this.error, logHead, 3);
      } else if (!this.logError && this.outputStream != null) {
         long funnelTimeout = 0L;
         OutputStreamFunneler funneler = new OutputStreamFunneler(this.outputStream, funnelTimeout);

         try {
            this.outputStream = funneler.getFunnelInstance();
            this.errorStream = funneler.getFunnelInstance();
         } catch (IOException var11) {
            throw new BuildException("error splitting output/error streams", var11);
         }
      }

      if (this.errorProperty != null) {
         if (this.errorBaos == null) {
            this.errorBaos = new Redirector.PropertyOutputStream(this.errorProperty);
            this.managingTask.log("Error redirected to property: " + this.errorProperty, 3);
         }

         keepAliveError = new KeepAliveOutputStream(this.errorBaos);
         this.errorStream = (OutputStream)(this.error != null && this.error.length != 0 ? new TeeOutputStream(this.errorStream, keepAliveError) : keepAliveError);
      } else {
         this.errorBaos = null;
      }

      LogOutputStream errorLog;
      if (this.alwaysLog || this.outputStream == null) {
         errorLog = new LogOutputStream(this.managingTask, 2);
         this.outputStream = (OutputStream)(this.outputStream == null ? errorLog : new TeeOutputStream(errorLog, this.outputStream));
      }

      if (this.alwaysLog || this.errorStream == null) {
         errorLog = new LogOutputStream(this.managingTask, 1);
         this.errorStream = (OutputStream)(this.errorStream == null ? errorLog : new TeeOutputStream(errorLog, this.errorStream));
      }

      ChainReaderHelper helper;
      ReaderInputStream errPumpIn;
      LeadPipeInputStream snk;
      Object reader;
      Thread t;
      if (this.outputFilterChains != null && this.outputFilterChains.size() > 0 || !this.outputEncoding.equalsIgnoreCase(this.inputEncoding)) {
         try {
            snk = new LeadPipeInputStream();
            snk.setManagingComponent(this.managingTask);
            reader = new InputStreamReader(snk, this.inputEncoding);
            if (this.outputFilterChains != null && this.outputFilterChains.size() > 0) {
               helper = new ChainReaderHelper();
               helper.setProject(this.managingTask.getProject());
               helper.setPrimaryReader((Reader)reader);
               helper.setFilterChains(this.outputFilterChains);
               reader = helper.getAssembledReader();
            }

            errPumpIn = new ReaderInputStream((Reader)reader, this.outputEncoding);
            t = new Thread(this.threadGroup, new StreamPumper(errPumpIn, this.outputStream, true), "output pumper");
            t.setPriority(10);
            this.outputStream = new PipedOutputStream(snk);
            t.start();
         } catch (IOException var10) {
            throw new BuildException("error setting up output stream", var10);
         }
      }

      if (this.errorFilterChains != null && this.errorFilterChains.size() > 0 || !this.errorEncoding.equalsIgnoreCase(this.inputEncoding)) {
         try {
            snk = new LeadPipeInputStream();
            snk.setManagingComponent(this.managingTask);
            reader = new InputStreamReader(snk, this.inputEncoding);
            if (this.errorFilterChains != null && this.errorFilterChains.size() > 0) {
               helper = new ChainReaderHelper();
               helper.setProject(this.managingTask.getProject());
               helper.setPrimaryReader((Reader)reader);
               helper.setFilterChains(this.errorFilterChains);
               reader = helper.getAssembledReader();
            }

            errPumpIn = new ReaderInputStream((Reader)reader, this.errorEncoding);
            t = new Thread(this.threadGroup, new StreamPumper(errPumpIn, this.errorStream, true), "error pumper");
            t.setPriority(10);
            this.errorStream = new PipedOutputStream(snk);
            t.start();
         } catch (IOException var9) {
            throw new BuildException("error setting up error stream", var9);
         }
      }

      if (this.input != null && this.input.length > 0) {
         this.managingTask.log("Redirecting input from file" + (this.input.length == 1 ? "" : "s"), 3);

         try {
            this.inputStream = new ConcatFileInputStream(this.input);
         } catch (IOException var8) {
            throw new BuildException(var8);
         }

         ((ConcatFileInputStream)this.inputStream).setManagingComponent(this.managingTask);
      } else if (this.inputString != null) {
         StringBuffer buf = new StringBuffer("Using input ");
         if (this.logInputString) {
            buf.append('"').append(this.inputString).append('"');
         } else {
            buf.append("string");
         }

         this.managingTask.log(buf.toString(), 3);
         this.inputStream = new ByteArrayInputStream(this.inputString.getBytes());
      }

      if (this.inputStream != null && this.inputFilterChains != null && this.inputFilterChains.size() > 0) {
         ChainReaderHelper helper = new ChainReaderHelper();
         helper.setProject(this.managingTask.getProject());

         try {
            helper.setPrimaryReader(new InputStreamReader(this.inputStream, this.inputEncoding));
         } catch (IOException var7) {
            throw new BuildException("error setting up input stream", var7);
         }

         helper.setFilterChains(this.inputFilterChains);
         this.inputStream = new ReaderInputStream(helper.getAssembledReader(), this.inputEncoding);
      }

   }

   public synchronized ExecuteStreamHandler createHandler() throws BuildException {
      this.createStreams();
      return new PumpStreamHandler(this.outputStream, this.errorStream, this.inputStream);
   }

   protected synchronized void handleOutput(String output) {
      if (this.outPrintStream == null) {
         this.outPrintStream = new PrintStream(this.outputStream);
      }

      this.outPrintStream.print(output);
   }

   protected synchronized int handleInput(byte[] buffer, int offset, int length) throws IOException {
      return this.inputStream == null ? this.managingTask.getProject().defaultInput(buffer, offset, length) : this.inputStream.read(buffer, offset, length);
   }

   protected synchronized void handleFlush(String output) {
      if (this.outPrintStream == null) {
         this.outPrintStream = new PrintStream(this.outputStream);
      }

      this.outPrintStream.print(output);
      this.outPrintStream.flush();
   }

   protected synchronized void handleErrorOutput(String output) {
      if (this.errorPrintStream == null) {
         this.errorPrintStream = new PrintStream(this.errorStream);
      }

      this.errorPrintStream.print(output);
   }

   protected synchronized void handleErrorFlush(String output) {
      if (this.errorPrintStream == null) {
         this.errorPrintStream = new PrintStream(this.errorStream);
      }

      this.errorPrintStream.print(output);
   }

   public synchronized OutputStream getOutputStream() {
      return this.outputStream;
   }

   public synchronized OutputStream getErrorStream() {
      return this.errorStream;
   }

   public synchronized InputStream getInputStream() {
      return this.inputStream;
   }

   public synchronized void complete() throws IOException {
      System.out.flush();
      System.err.flush();
      if (this.inputStream != null) {
         this.inputStream.close();
      }

      this.outputStream.flush();
      this.outputStream.close();
      this.errorStream.flush();
      this.errorStream.close();

      while(this.threadGroup.activeCount() > 0) {
         try {
            this.managingTask.log("waiting for " + this.threadGroup.activeCount() + " Threads:", 4);
            Thread[] thread = new Thread[this.threadGroup.activeCount()];
            this.threadGroup.enumerate(thread);

            for(int i = 0; i < thread.length && thread[i] != null; ++i) {
               try {
                  this.managingTask.log(thread[i].toString(), 4);
               } catch (NullPointerException var4) {
               }
            }

            this.wait(1000L);
         } catch (InterruptedException var5) {
         }
      }

      this.setProperties();
      this.inputStream = null;
      this.outputStream = null;
      this.errorStream = null;
      this.outPrintStream = null;
      this.errorPrintStream = null;
   }

   public synchronized void setProperties() {
      if (this.baos != null) {
         try {
            this.baos.close();
         } catch (IOException var3) {
         }
      }

      if (this.errorBaos != null) {
         try {
            this.errorBaos.close();
         } catch (IOException var2) {
         }
      }

   }

   private OutputStream foldFiles(File[] file, String logHead, int loglevel) {
      OutputStream result = new LazyFileOutputStream(file[0], this.append, this.createEmptyFiles);
      this.managingTask.log(logHead + file[0], loglevel);
      char[] c = new char[logHead.length()];
      Arrays.fill(c, ' ');
      String indent = new String(c);

      for(int i = 1; i < file.length; ++i) {
         this.outputStream = new TeeOutputStream(this.outputStream, new LazyFileOutputStream(file[i], this.append, this.createEmptyFiles));
         this.managingTask.log(indent + file[i], loglevel);
      }

      return result;
   }

   private class PropertyOutputStream extends ByteArrayOutputStream {
      private String property;
      private boolean closed = false;

      PropertyOutputStream(String property) {
         this.property = property;
      }

      public void close() throws IOException {
         if (!this.closed && (!Redirector.this.append || !Redirector.this.appendProperties)) {
            Redirector.this.setPropertyFromBAOS(this, this.property);
            this.closed = true;
         }

      }
   }
}

package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.system;

import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common.LoggerAppender;
import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common.SLF4JPrintStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;

public final class SLF4JPrintStreamImpl extends PrintStream implements SLF4JPrintStream {
   private final PrintStream originalPrintStream;
   private final SLF4JPrintStreamDelegate delegate;

   SLF4JPrintStreamImpl(PrintStream originalPrintStream, SLF4JPrintStreamDelegate delegate) {
      super(new ByteArrayOutputStream());
      this.originalPrintStream = originalPrintStream;
      this.delegate = delegate;
   }

   public synchronized void println(String string) {
      this.delegate.delegatePrintln(string);
   }

   public synchronized void println(Object object) {
      this.delegate.delegatePrintln(String.valueOf(object));
   }

   public synchronized void println() {
      this.delegate.delegatePrintln("");
   }

   public synchronized void println(boolean bool) {
      this.delegate.delegatePrintln(String.valueOf(bool));
   }

   public synchronized void println(char character) {
      this.delegate.delegatePrintln(String.valueOf(character));
   }

   public synchronized void println(char[] charArray) {
      this.delegate.delegatePrintln(String.valueOf(charArray));
   }

   public synchronized void println(double doub) {
      this.delegate.delegatePrintln(String.valueOf(doub));
   }

   public synchronized void println(float floa) {
      this.delegate.delegatePrintln(String.valueOf(floa));
   }

   public synchronized void println(int integer) {
      this.delegate.delegatePrintln(String.valueOf(integer));
   }

   public synchronized void println(long lon) {
      this.delegate.delegatePrintln(String.valueOf(lon));
   }

   public synchronized PrintStream append(char character) {
      this.delegate.delegatePrint(String.valueOf(character));
      return this;
   }

   public synchronized PrintStream append(CharSequence csq, int start, int end) {
      this.delegate.delegatePrint(csq.subSequence(start, end).toString());
      return this;
   }

   public synchronized PrintStream append(CharSequence csq) {
      this.delegate.delegatePrint(csq.toString());
      return this;
   }

   public boolean checkError() {
      return this.originalPrintStream.checkError();
   }

   protected void setError() {
      this.originalPrintStream.println("WARNING - calling setError on SLFJPrintStream does nothing");
   }

   public void close() {
      this.originalPrintStream.close();
   }

   public void flush() {
      this.originalPrintStream.flush();
   }

   public synchronized PrintStream format(Locale locale, String format, Object... args) {
      String string = String.format(locale, format, args);
      this.delegate.delegatePrint(string);
      return this;
   }

   public synchronized PrintStream format(String format, Object... args) {
      return this.format(Locale.getDefault(), format, args);
   }

   public synchronized void print(boolean bool) {
      this.delegate.delegatePrint(String.valueOf(bool));
   }

   public synchronized void print(char character) {
      this.delegate.delegatePrint(String.valueOf(character));
   }

   public synchronized void print(char[] charArray) {
      this.delegate.delegatePrint(String.valueOf(charArray));
   }

   public synchronized void print(double doubl) {
      this.delegate.delegatePrint(String.valueOf(doubl));
   }

   public synchronized void print(float floa) {
      this.delegate.delegatePrint(String.valueOf(floa));
   }

   public synchronized void print(int integer) {
      this.delegate.delegatePrint(String.valueOf(integer));
   }

   public synchronized void print(long lon) {
      this.delegate.delegatePrint(String.valueOf(lon));
   }

   public synchronized void print(Object object) {
      this.delegate.delegatePrint(String.valueOf(object));
   }

   public synchronized void print(String string) {
      this.delegate.delegatePrint(String.valueOf(string));
   }

   public synchronized PrintStream printf(Locale locale, String format, Object... args) {
      return this.format(locale, format, args);
   }

   public synchronized PrintStream printf(String format, Object... args) {
      return this.format(format, args);
   }

   public void write(byte[] buf, int off, int len) {
      this.originalPrintStream.write(buf, off, len);
   }

   public void write(int integer) {
      this.originalPrintStream.write(integer);
   }

   public void write(byte[] bytes) throws IOException {
      this.originalPrintStream.write(bytes);
   }

   public void registerLoggerAppender(Object loggerAppenderObject) {
      LoggerAppender loggerAppender = LoggerAppenderProxy.wrap(loggerAppenderObject);
      this.delegate.registerLoggerAppender(loggerAppender);
   }

   public void deregisterLoggerAppender() {
      this.delegate.deregisterLoggerAppender();
   }

   public PrintStream getOriginalPrintStream() {
      return this.originalPrintStream;
   }
}

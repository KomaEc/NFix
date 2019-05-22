package org.codehaus.groovy.control;

import com.thoughtworks.xstream.XStream;
import groovy.lang.GroovyClassLoader;
import groovyjarjarantlr.MismatchedCharException;
import groovyjarjarantlr.MismatchedTokenException;
import groovyjarjarantlr.NoViableAltException;
import groovyjarjarantlr.NoViableAltForCharException;
import groovyjarjarantlr.Token;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.codehaus.groovy.GroovyBugError;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.control.io.FileReaderSource;
import org.codehaus.groovy.control.io.ReaderSource;
import org.codehaus.groovy.control.io.StringReaderSource;
import org.codehaus.groovy.control.io.URLReaderSource;
import org.codehaus.groovy.control.messages.Message;
import org.codehaus.groovy.control.messages.SimpleMessage;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.syntax.Reduction;
import org.codehaus.groovy.syntax.SyntaxException;
import org.codehaus.groovy.tools.Utilities;

public class SourceUnit extends ProcessingUnit {
   private ParserPlugin parserPlugin;
   protected ReaderSource source;
   protected String name;
   protected Reduction cst;
   protected ModuleNode ast;

   public SourceUnit(String name, ReaderSource source, CompilerConfiguration flags, GroovyClassLoader loader, ErrorCollector er) {
      super(flags, loader, er);
      this.name = name;
      this.source = source;
   }

   public SourceUnit(File source, CompilerConfiguration configuration, GroovyClassLoader loader, ErrorCollector er) {
      this(source.getPath(), (ReaderSource)(new FileReaderSource(source, configuration)), configuration, loader, er);
   }

   public SourceUnit(URL source, CompilerConfiguration configuration, GroovyClassLoader loader, ErrorCollector er) {
      this(source.getPath(), (ReaderSource)(new URLReaderSource(source, configuration)), configuration, loader, er);
   }

   public SourceUnit(String name, String source, CompilerConfiguration configuration, GroovyClassLoader loader, ErrorCollector er) {
      this(name, (ReaderSource)(new StringReaderSource(source, configuration)), configuration, loader, er);
   }

   public String getName() {
      return this.name;
   }

   public Reduction getCST() {
      return this.cst;
   }

   public ModuleNode getAST() {
      return this.ast;
   }

   public boolean failedWithUnexpectedEOF() {
      if (this.getErrorCollector().hasErrors()) {
         Message last = this.getErrorCollector().getLastError();
         Throwable cause = null;
         if (last instanceof SyntaxErrorMessage) {
            cause = ((SyntaxErrorMessage)last).getCause().getCause();
         }

         if (cause != null) {
            if (cause instanceof NoViableAltException) {
               return this.isEofToken(((NoViableAltException)cause).token);
            }

            char badChar;
            if (cause instanceof NoViableAltForCharException) {
               badChar = ((NoViableAltForCharException)cause).foundChar;
               return badChar == '\uffff';
            }

            if (cause instanceof MismatchedCharException) {
               badChar = (char)((MismatchedCharException)cause).foundChar;
               return badChar == '\uffff';
            }

            if (cause instanceof MismatchedTokenException) {
               return this.isEofToken(((MismatchedTokenException)cause).token);
            }
         }
      }

      return false;
   }

   protected boolean isEofToken(Token token) {
      return token.getType() == 1;
   }

   public static SourceUnit create(String name, String source) {
      CompilerConfiguration configuration = new CompilerConfiguration();
      configuration.setTolerance(1);
      return new SourceUnit(name, source, configuration, (GroovyClassLoader)null, new ErrorCollector(configuration));
   }

   public static SourceUnit create(String name, String source, int tolerance) {
      CompilerConfiguration configuration = new CompilerConfiguration();
      configuration.setTolerance(tolerance);
      return new SourceUnit(name, source, configuration, (GroovyClassLoader)null, new ErrorCollector(configuration));
   }

   public void parse() throws CompilationFailedException {
      if (this.phase > 2) {
         throw new GroovyBugError("parsing is already complete");
      } else {
         if (this.phase == 1) {
            this.nextPhase();
         }

         Reader reader = null;

         try {
            reader = this.source.getReader();
            this.parserPlugin = this.getConfiguration().getPluginFactory().createParserPlugin();
            this.cst = this.parserPlugin.parseCST(this, reader);
            reader.close();
         } catch (IOException var11) {
            this.getErrorCollector().addFatalError(new SimpleMessage(var11.getMessage(), this));
         } finally {
            if (reader != null) {
               try {
                  reader.close();
               } catch (IOException var10) {
               }
            }

         }

      }
   }

   public void convert() throws CompilationFailedException {
      if (this.phase == 2 && this.phaseComplete) {
         this.gotoPhase(3);
      }

      if (this.phase != 3) {
         throw new GroovyBugError("SourceUnit not ready for convert()");
      } else {
         try {
            this.ast = this.parserPlugin.buildAST(this, this.classLoader, this.cst);
            this.ast.setDescription(this.name);
         } catch (SyntaxException var2) {
            this.getErrorCollector().addError(new SyntaxErrorMessage(var2, this));
         }

         String property = (String)AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
               return System.getProperty("groovy.ast");
            }
         });
         if ("xml".equals(property)) {
            this.saveAsXML(this.name, this.ast);
         }

      }
   }

   private void saveAsXML(String name, ModuleNode ast) {
      XStream xstream = new XStream();

      try {
         xstream.toXML(ast, new FileWriter(name + ".xml"));
         System.out.println("Written AST to " + name + ".xml");
      } catch (Exception var5) {
         System.out.println("Couldn't write to " + name + ".xml");
         var5.printStackTrace();
      }

   }

   public String getSample(int line, int column, Janitor janitor) {
      String sample = null;
      String text = this.source.getLine(line, janitor);
      if (text != null) {
         if (column > 0) {
            String marker = Utilities.repeatString(" ", column - 1) + "^";
            if (column > 40) {
               int start = column - 30 - 1;
               int end = column + 10 > text.length() ? text.length() : column + 10 - 1;
               sample = "   " + text.substring(start, end) + Utilities.eol() + "   " + marker.substring(start, marker.length());
            } else {
               sample = "   " + text + Utilities.eol() + "   " + marker;
            }
         } else {
            sample = text;
         }
      }

      return sample;
   }

   public void addException(Exception e) throws CompilationFailedException {
      this.getErrorCollector().addException(e, this);
   }

   public void addError(SyntaxException se) throws CompilationFailedException {
      this.getErrorCollector().addError(se, this);
   }
}

package org.apache.oro.text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Vector;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Util;

public final class MatchActionProcessor {
   private Pattern __fieldSeparator;
   private PatternCompiler __compiler;
   private PatternMatcher __matcher;
   private Vector __patterns;
   private Vector __actions;
   private MatchAction __defaultAction;

   public MatchActionProcessor(PatternCompiler var1, PatternMatcher var2) {
      this.__fieldSeparator = null;
      this.__patterns = new Vector();
      this.__actions = new Vector();
      this.__defaultAction = new DefaultMatchAction();
      this.__compiler = var1;
      this.__matcher = var2;
   }

   public MatchActionProcessor() {
      this(new Perl5Compiler(), new Perl5Matcher());
   }

   public void addAction(String var1, int var2, MatchAction var3) throws MalformedPatternException {
      if (var1 != null) {
         this.__patterns.addElement(this.__compiler.compile(var1, var2));
      } else {
         this.__patterns.addElement((Object)null);
      }

      this.__actions.addElement(var3);
   }

   public void addAction(String var1, int var2) throws MalformedPatternException {
      this.addAction(var1, var2, this.__defaultAction);
   }

   public void addAction(String var1) throws MalformedPatternException {
      this.addAction(var1, 0);
   }

   public void addAction(String var1, MatchAction var2) throws MalformedPatternException {
      this.addAction(var1, 0, var2);
   }

   public void setFieldSeparator(String var1, int var2) throws MalformedPatternException {
      if (var1 == null) {
         this.__fieldSeparator = null;
      } else {
         this.__fieldSeparator = this.__compiler.compile(var1, var2);
      }
   }

   public void setFieldSeparator(String var1) throws MalformedPatternException {
      this.setFieldSeparator(var1, 0);
   }

   public void processMatches(InputStream var1, OutputStream var2, String var3) throws IOException {
      this.processMatches((Reader)(new InputStreamReader(var1, var3)), (Writer)(new OutputStreamWriter(var2)));
   }

   public void processMatches(InputStream var1, OutputStream var2) throws IOException {
      this.processMatches((Reader)(new InputStreamReader(var1)), (Writer)(new OutputStreamWriter(var2)));
   }

   public void processMatches(Reader var1, Writer var2) throws IOException {
      LineNumberReader var3 = new LineNumberReader(var1);
      PrintWriter var4 = new PrintWriter(var2);
      MatchActionInfo var5 = new MatchActionInfo();
      ArrayList var6 = new ArrayList();
      var5.matcher = this.__matcher;
      var5.fieldSeparator = this.__fieldSeparator;
      var5.input = var3;
      var5.output = var4;
      var5.fields = null;
      int var7 = this.__patterns.size();
      var5.lineNumber = 0;

      while((var5.line = var3.readLine()) != null) {
         var5.charLine = var5.line.toCharArray();

         for(int var8 = 0; var8 < var7; ++var8) {
            Object var9 = this.__patterns.elementAt(var8);
            MatchAction var11;
            if (var9 != null) {
               Pattern var10 = (Pattern)this.__patterns.elementAt(var8);
               if (this.__matcher.contains(var5.charLine, var10)) {
                  var5.match = this.__matcher.getMatch();
                  var5.lineNumber = var3.getLineNumber();
                  var5.pattern = var10;
                  if (this.__fieldSeparator != null) {
                     var6.clear();
                     Util.split(var6, this.__matcher, this.__fieldSeparator, var5.line);
                     var5.fields = var6;
                  } else {
                     var5.fields = null;
                  }

                  var11 = (MatchAction)this.__actions.elementAt(var8);
                  var11.processMatch(var5);
               }
            } else {
               var5.match = null;
               var5.lineNumber = var3.getLineNumber();
               if (this.__fieldSeparator != null) {
                  var6.clear();
                  Util.split(var6, this.__matcher, this.__fieldSeparator, var5.line);
                  var5.fields = var6;
               } else {
                  var5.fields = null;
               }

               var11 = (MatchAction)this.__actions.elementAt(var8);
               var11.processMatch(var5);
            }
         }
      }

      var4.flush();
      var3.close();
   }
}

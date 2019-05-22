package org.codehaus.groovy.antlr.java;

import groovyjarjarantlr.collections.AST;
import groovyjarjarcommonscli.CommandLine;
import groovyjarjarcommonscli.Options;
import groovyjarjarcommonscli.PosixParser;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.antlr.AntlrASTProcessor;
import org.codehaus.groovy.antlr.SourceBuffer;
import org.codehaus.groovy.antlr.UnicodeEscapingReader;
import org.codehaus.groovy.antlr.parser.GroovyLexer;
import org.codehaus.groovy.antlr.parser.GroovyRecognizer;
import org.codehaus.groovy.antlr.treewalker.MindMapPrinter;
import org.codehaus.groovy.antlr.treewalker.NodePrinter;
import org.codehaus.groovy.antlr.treewalker.PreOrderTraversal;
import org.codehaus.groovy.antlr.treewalker.SourceCodeTraversal;
import org.codehaus.groovy.antlr.treewalker.SourcePrinter;
import org.codehaus.groovy.antlr.treewalker.Visitor;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

public class Java2GroovyMain {
   public static void main(String[] args) {
      try {
         Options options = new Options();
         PosixParser cliParser = new PosixParser();
         CommandLine cli = cliParser.parse(options, args);
         String[] filenames = cli.getArgs();
         if (filenames.length == 0) {
            System.err.println("Needs at least one filename");
         }

         List filenameList = Arrays.asList(filenames);
         Iterator i = filenameList.iterator();

         while(i.hasNext()) {
            String filename = (String)i.next();
            File f = new File(filename);
            String text = DefaultGroovyMethods.getText(f);
            System.out.println(convert(filename, text, true, true));
         }
      } catch (Throwable var10) {
         var10.printStackTrace();
      }

   }

   public static String convert(String filename, String input) throws Exception {
      return convert(filename, input, false, false);
   }

   public static String convert(String filename, String input, boolean withHeader, boolean withNewLines) throws Exception {
      JavaRecognizer parser = getJavaParser(input);
      String[] tokenNames = parser.getTokenNames();
      parser.compilationUnit();
      AST ast = parser.getAST();
      if ("mindmap".equals(System.getProperty("groovyjarjarantlr.ast"))) {
         try {
            PrintStream out = new PrintStream(new FileOutputStream(filename + ".mm"));
            Visitor visitor = new MindMapPrinter(out, tokenNames);
            AntlrASTProcessor treewalker = new PreOrderTraversal(visitor);
            treewalker.process(ast);
         } catch (FileNotFoundException var12) {
            System.out.println("Cannot create " + filename + ".mm");
         }
      }

      modifyJavaASTintoGroovyAST(tokenNames, ast);
      String[] groovyTokenNames = getGroovyTokenNames(input);
      groovifyFatJavaLikeGroovyAST(ast, groovyTokenNames);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      Visitor visitor = new SourcePrinter(new PrintStream(baos), groovyTokenNames, withNewLines);
      AntlrASTProcessor traverser = new SourceCodeTraversal(visitor);
      traverser.process(ast);
      String header = "";
      if (withHeader) {
         header = "/*\n  Automatically Converted from Java Source \n  \n  by java2groovy v0.0.1   Copyright Jeremy Rayner 2007\n  \n  !! NOT FIT FOR ANY PURPOSE !! \n  'java2groovy' cannot be used to convert one working program into another  */\n\n";
      }

      return header + new String(baos.toByteArray());
   }

   private static void groovifyFatJavaLikeGroovyAST(AST ast, String[] groovyTokenNames) {
      Visitor groovifier = new Groovifier(groovyTokenNames);
      AntlrASTProcessor groovifierTraverser = new PreOrderTraversal(groovifier);
      groovifierTraverser.process(ast);
   }

   private static void modifyJavaASTintoGroovyAST(String[] tokenNames, AST ast) {
      Visitor preJava2groovyConverter = new PreJava2GroovyConverter(tokenNames);
      AntlrASTProcessor preJava2groovyTraverser = new PreOrderTraversal(preJava2groovyConverter);
      preJava2groovyTraverser.process(ast);
      Visitor java2groovyConverter = new Java2GroovyConverter(tokenNames);
      AntlrASTProcessor java2groovyTraverser = new PreOrderTraversal(java2groovyConverter);
      java2groovyTraverser.process(ast);
   }

   private static JavaRecognizer getJavaParser(String input) {
      JavaRecognizer parser = null;
      SourceBuffer sourceBuffer = new SourceBuffer();
      UnicodeEscapingReader unicodeReader = new UnicodeEscapingReader(new StringReader(input), sourceBuffer);
      JavaLexer lexer = new JavaLexer(unicodeReader);
      unicodeReader.setLexer(lexer);
      parser = JavaRecognizer.make(lexer);
      parser.setSourceBuffer(sourceBuffer);
      return parser;
   }

   public static String mindmap(String input) throws Exception {
      JavaRecognizer parser = getJavaParser(input);
      String[] tokenNames = parser.getTokenNames();
      parser.compilationUnit();
      AST ast = parser.getAST();
      modifyJavaASTintoGroovyAST(tokenNames, ast);
      String[] groovyTokenNames = getGroovyTokenNames(input);
      groovifyFatJavaLikeGroovyAST(ast, groovyTokenNames);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      Visitor visitor = new MindMapPrinter(new PrintStream(baos), groovyTokenNames);
      AntlrASTProcessor traverser = new SourceCodeTraversal(visitor);
      traverser.process(ast);
      return new String(baos.toByteArray());
   }

   public static String nodePrinter(String input) throws Exception {
      JavaRecognizer parser = getJavaParser(input);
      String[] tokenNames = parser.getTokenNames();
      parser.compilationUnit();
      AST ast = parser.getAST();
      modifyJavaASTintoGroovyAST(tokenNames, ast);
      String[] groovyTokenNames = getGroovyTokenNames(input);
      groovifyFatJavaLikeGroovyAST(ast, groovyTokenNames);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      Visitor visitor = new NodePrinter(new PrintStream(baos), groovyTokenNames);
      AntlrASTProcessor traverser = new SourceCodeTraversal(visitor);
      traverser.process(ast);
      return new String(baos.toByteArray());
   }

   private static String[] getGroovyTokenNames(String input) {
      GroovyRecognizer groovyParser = null;
      SourceBuffer groovySourceBuffer = new SourceBuffer();
      UnicodeEscapingReader groovyUnicodeReader = new UnicodeEscapingReader(new StringReader(input), groovySourceBuffer);
      GroovyLexer groovyLexer = new GroovyLexer(groovyUnicodeReader);
      groovyUnicodeReader.setLexer(groovyLexer);
      groovyParser = GroovyRecognizer.make(groovyLexer);
      return groovyParser.getTokenNames();
   }
}

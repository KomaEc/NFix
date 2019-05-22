package org.codehaus.groovy.tools.groovydoc;

import groovyjarjarantlr.RecognitionException;
import groovyjarjarantlr.TokenStreamException;
import groovyjarjarantlr.collections.AST;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.codehaus.groovy.antlr.AntlrASTProcessor;
import org.codehaus.groovy.antlr.SourceBuffer;
import org.codehaus.groovy.antlr.UnicodeEscapingReader;
import org.codehaus.groovy.antlr.java.Groovifier;
import org.codehaus.groovy.antlr.java.Java2GroovyConverter;
import org.codehaus.groovy.antlr.java.JavaLexer;
import org.codehaus.groovy.antlr.java.JavaRecognizer;
import org.codehaus.groovy.antlr.parser.GroovyLexer;
import org.codehaus.groovy.antlr.parser.GroovyRecognizer;
import org.codehaus.groovy.antlr.treewalker.PreOrderTraversal;
import org.codehaus.groovy.antlr.treewalker.SourceCodeTraversal;
import org.codehaus.groovy.antlr.treewalker.Visitor;
import org.codehaus.groovy.groovydoc.GroovyClassDoc;
import org.codehaus.groovy.groovydoc.GroovyRootDoc;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

public class GroovyRootDocBuilder {
   private static final char FS = '/';
   private List<LinkArgument> links;
   private final GroovyDocTool tool;
   private final String[] sourcepaths;
   private final SimpleGroovyRootDoc rootDoc;
   private final Properties properties;

   public GroovyRootDocBuilder(GroovyDocTool tool, String[] sourcepaths, List<LinkArgument> links, Properties properties) {
      this.tool = tool;
      this.sourcepaths = sourcepaths;
      this.links = links;
      this.rootDoc = new SimpleGroovyRootDoc("root");
      this.properties = properties;
   }

   public Map<String, GroovyClassDoc> getClassDocsFromSingleSource(String packagePath, String file, String src) throws RecognitionException, TokenStreamException {
      if (file.indexOf(".java") > 0) {
         return this.parseJava(packagePath, file, src);
      } else {
         return file.indexOf(".sourcefile") > 0 ? this.parseJava(packagePath, file, src) : this.parseGroovy(packagePath, file, src);
      }
   }

   private Map<String, GroovyClassDoc> parseJava(String packagePath, String file, String src) throws RecognitionException, TokenStreamException {
      SourceBuffer sourceBuffer = new SourceBuffer();
      JavaRecognizer parser = this.getJavaParser(src, sourceBuffer);
      String[] tokenNames = parser.getTokenNames();

      try {
         parser.compilationUnit();
      } catch (OutOfMemoryError var14) {
         System.out.println("Out of memory while processing: " + packagePath + "/" + file);
         throw var14;
      }

      AST ast = parser.getAST();
      Visitor java2groovyConverter = new Java2GroovyConverter(tokenNames);
      AntlrASTProcessor java2groovyTraverser = new PreOrderTraversal(java2groovyConverter);
      java2groovyTraverser.process(ast);
      Visitor groovifier = new Groovifier(tokenNames, false);
      AntlrASTProcessor groovifierTraverser = new PreOrderTraversal(groovifier);
      groovifierTraverser.process(ast);
      Visitor visitor = new SimpleGroovyClassDocAssembler(packagePath, file, sourceBuffer, this.links, this.properties, false);
      AntlrASTProcessor traverser = new SourceCodeTraversal(visitor);
      traverser.process(ast);
      return ((SimpleGroovyClassDocAssembler)visitor).getGroovyClassDocs();
   }

   private Map<String, GroovyClassDoc> parseGroovy(String packagePath, String file, String src) throws RecognitionException, TokenStreamException {
      SourceBuffer sourceBuffer = new SourceBuffer();
      GroovyRecognizer parser = this.getGroovyParser(src, sourceBuffer);

      try {
         parser.compilationUnit();
      } catch (OutOfMemoryError var9) {
         System.out.println("Out of memory while processing: " + packagePath + "/" + file);
         throw var9;
      }

      AST ast = parser.getAST();
      Visitor visitor = new SimpleGroovyClassDocAssembler(packagePath, file, sourceBuffer, this.links, this.properties, true);
      AntlrASTProcessor traverser = new SourceCodeTraversal(visitor);
      traverser.process(ast);
      return ((SimpleGroovyClassDocAssembler)visitor).getGroovyClassDocs();
   }

   private JavaRecognizer getJavaParser(String input, SourceBuffer sourceBuffer) {
      UnicodeEscapingReader unicodeReader = new UnicodeEscapingReader(new StringReader(input), sourceBuffer);
      JavaLexer lexer = new JavaLexer(unicodeReader);
      unicodeReader.setLexer(lexer);
      JavaRecognizer parser = JavaRecognizer.make(lexer);
      parser.setSourceBuffer(sourceBuffer);
      return parser;
   }

   private GroovyRecognizer getGroovyParser(String input, SourceBuffer sourceBuffer) {
      UnicodeEscapingReader unicodeReader = new UnicodeEscapingReader(new StringReader(input), sourceBuffer);
      GroovyLexer lexer = new GroovyLexer(unicodeReader);
      unicodeReader.setLexer(lexer);
      GroovyRecognizer parser = GroovyRecognizer.make(lexer);
      parser.setSourceBuffer(sourceBuffer);
      return parser;
   }

   public void buildTree(List<String> filenames) throws IOException, RecognitionException, TokenStreamException {
      this.setOverview();
      List<File> sourcepathFiles = new ArrayList();
      if (this.sourcepaths != null) {
         String[] arr$ = this.sourcepaths;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String sourcepath = arr$[i$];
            sourcepathFiles.add((new File(sourcepath)).getAbsoluteFile());
         }
      }

      Iterator i$ = filenames.iterator();

      while(true) {
         while(i$.hasNext()) {
            String filename = (String)i$.next();
            File srcFile = new File(filename);
            if (srcFile.exists()) {
               this.processFile(filename, srcFile, true);
            } else {
               Iterator i$ = sourcepathFiles.iterator();

               while(i$.hasNext()) {
                  File spath = (File)i$.next();
                  srcFile = new File(spath, filename);
                  if (srcFile.exists()) {
                     this.processFile(filename, srcFile, false);
                     break;
                  }
               }
            }
         }

         return;
      }
   }

   private void setOverview() {
      String path = this.properties.getProperty("overviewFile");
      if (path != null && path.length() > 0) {
         try {
            String content = DefaultGroovyMethods.getText(new File(path));
            this.calcThenSetOverviewDescription(content);
         } catch (IOException var3) {
            System.err.println("Unable to load overview file: " + var3.getMessage());
         }
      }

   }

   private void processFile(String filename, File srcFile, boolean isAbsolute) throws IOException {
      String src = DefaultGroovyMethods.getText(srcFile);
      String packagePath = isAbsolute ? "DefaultPackage" : this.tool.getPath(filename).replace('\\', '/');
      String file = this.tool.getFile(filename);
      SimpleGroovyPackageDoc packageDoc = null;
      if (!isAbsolute) {
         packageDoc = (SimpleGroovyPackageDoc)this.rootDoc.packageNamed(packagePath);
      }

      if (!filename.endsWith("package.html") && !filename.endsWith("package-info.java") && !filename.endsWith("package-info.groovy")) {
         try {
            Map<String, GroovyClassDoc> classDocs = this.getClassDocsFromSingleSource(packagePath, file, src);
            this.rootDoc.putAllClasses(classDocs);
            if (isAbsolute) {
               Iterator<Entry<String, GroovyClassDoc>> iterator = classDocs.entrySet().iterator();
               if (iterator.hasNext()) {
                  Entry<String, GroovyClassDoc> docEntry = (Entry)iterator.next();
                  String fullPath = ((GroovyClassDoc)docEntry.getValue()).getFullPathName();
                  int slash = fullPath.lastIndexOf(47);
                  if (slash > 0) {
                     packagePath = fullPath.substring(0, slash);
                  }

                  packageDoc = (SimpleGroovyPackageDoc)this.rootDoc.packageNamed(packagePath);
               }
            }

            if (packageDoc == null) {
               packageDoc = new SimpleGroovyPackageDoc(packagePath);
            }

            packageDoc.putAll(classDocs);
            this.rootDoc.put(packagePath, packageDoc);
         } catch (RecognitionException var13) {
            System.err.println("ignored due to RecognitionException: " + filename + " [" + var13.getMessage() + "]");
         } catch (TokenStreamException var14) {
            System.err.println("ignored due to TokenStreamException: " + filename + " [" + var14.getMessage() + "]");
         }

      } else {
         if (packageDoc == null) {
            packageDoc = new SimpleGroovyPackageDoc(packagePath);
         }

         this.processPackageInfo(src, filename, packageDoc);
         this.rootDoc.put(packagePath, packageDoc);
      }
   }

   void processPackageInfo(String src, String filename, SimpleGroovyPackageDoc packageDoc) {
      String description = this.calcThenSetPackageDescription(src, filename, packageDoc);
      this.calcThenSetSummary(description, packageDoc);
   }

   private String calcThenSetPackageDescription(String src, String filename, SimpleGroovyPackageDoc packageDoc) {
      String description;
      if (filename.endsWith(".html")) {
         description = this.scrubOffExcessiveTags(src);
         description = this.pruneTagFromFront(description, "p");
         description = this.pruneTagFromEnd(description, "/p");
      } else {
         description = this.trimPackageAndComments(src);
      }

      description = this.replaceTags(description, packageDoc);
      packageDoc.setDescription(description);
      return description;
   }

   private String replaceTags(String orig, SimpleGroovyPackageDoc packageDoc) {
      String result = orig.replaceAll("(?m)^\\s*\\*", "");
      result = this.replaceAllTags(result, "", "", SimpleGroovyClassDoc.LINK_REGEX, packageDoc);
      result = this.replaceAllTags(result, "<TT>", "</TT>", SimpleGroovyClassDoc.CODE_REGEX, packageDoc);
      result = this.replaceAllTags(result + "@endMarker", "<DL><DT><B>$1:</B></DT><DD>", "</DD></DL>", SimpleGroovyClassDoc.TAG_REGEX, packageDoc);
      result = result.substring(0, result.length() - 10);
      return SimpleGroovyClassDoc.decodeSpecialSymbols(result);
   }

   private String replaceAllTags(String self, String s1, String s2, Pattern regex, SimpleGroovyPackageDoc packageDoc) {
      Matcher matcher = regex.matcher(self);
      if (!matcher.find()) {
         return self;
      } else {
         matcher.reset();
         StringBuffer sb = new StringBuffer();

         while(true) {
            while(matcher.find()) {
               String tagname = matcher.group(1);
               if (!tagname.equals("see") && !tagname.equals("link")) {
                  if (!tagname.equals("interface")) {
                     matcher.appendReplacement(sb, s1 + SimpleGroovyClassDoc.encodeSpecialSymbols(matcher.group(2)) + s2);
                  }
               } else {
                  matcher.appendReplacement(sb, s1 + SimpleGroovyClassDoc.getDocUrl(SimpleGroovyClassDoc.encodeSpecialSymbols(matcher.group(2)), false, this.links, packageDoc.getRelativeRootPath(), this.rootDoc, (SimpleGroovyClassDoc)null) + s2);
               }
            }

            matcher.appendTail(sb);
            return sb.toString();
         }
      }
   }

   private void calcThenSetSummary(String src, SimpleGroovyPackageDoc packageDoc) {
      packageDoc.setSummary(SimpleGroovyDoc.calculateFirstSentence(src));
   }

   private void calcThenSetOverviewDescription(String src) {
      String description = this.scrubOffExcessiveTags(src);
      this.rootDoc.setDescription(description);
   }

   private String trimPackageAndComments(String src) {
      return src.replaceFirst("(?sm)^package.*", "").replaceFirst("(?sm)/.*\\*\\*(.*)\\*/", "$1").replaceAll("(?m)^\\s*\\*", "");
   }

   private String scrubOffExcessiveTags(String src) {
      String description = this.pruneTagFromFront(src, "html");
      description = this.pruneTagFromFront(description, "/head");
      description = this.pruneTagFromFront(description, "body");
      description = this.pruneTagFromEnd(description, "/html");
      return this.pruneTagFromEnd(description, "/body");
   }

   private String pruneTagFromFront(String description, String tag) {
      int index = Math.max(this.indexOfTag(description, tag.toLowerCase()), this.indexOfTag(description, tag.toUpperCase()));
      return index < 0 ? description : description.substring(index);
   }

   private String pruneTagFromEnd(String description, String tag) {
      int index = Math.max(description.lastIndexOf("<" + tag.toLowerCase() + ">"), description.lastIndexOf("<" + tag.toUpperCase() + ">"));
      return index < 0 ? description : description.substring(0, index);
   }

   private int indexOfTag(String text, String tag) {
      int pos = text.indexOf("<" + tag + ">");
      if (pos > 0) {
         pos += tag.length() + 2;
      }

      return pos;
   }

   public GroovyRootDoc getRootDoc() {
      this.rootDoc.resolve();
      return this.rootDoc;
   }
}

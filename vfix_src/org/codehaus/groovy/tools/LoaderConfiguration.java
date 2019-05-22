package org.codehaus.groovy.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoaderConfiguration {
   private static final String MAIN_PREFIX = "main is";
   private static final String LOAD_PREFIX = "load";
   private static final String PROP_PREFIX = "property";
   private List classPath = new ArrayList();
   private String main;
   private boolean requireMain = true;
   private static final char WILDCARD = '*';
   private static final String ALL_WILDCARD = "**";
   private static final String MATCH_FILE_NAME = "\\\\E[^/]+?\\\\Q";
   private static final String MATCH_ALL = "\\\\E.+?\\\\Q";

   public void configure(InputStream is) throws IOException {
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      int lineNumber = 0;

      while(true) {
         String line = reader.readLine();
         if (line == null) {
            if (this.requireMain && this.main == null) {
               throw new IOException("missing main class definition in config file");
            }

            return;
         }

         line = line.trim();
         ++lineNumber;
         if (!line.startsWith("#") && line.length() != 0) {
            String params;
            if (line.startsWith("load")) {
               params = line.substring("load".length()).trim();
               params = this.assignProperties(params);
               this.loadFilteredPath(params);
            } else if (line.startsWith("main is")) {
               if (this.main != null) {
                  throw new IOException("duplicate definition of main in line " + lineNumber + " : " + line);
               }

               this.main = line.substring("main is".length()).trim();
            } else {
               if (!line.startsWith("property")) {
                  throw new IOException("unexpected line in " + lineNumber + " : " + line);
               }

               params = line.substring("property".length()).trim();
               int index = params.indexOf(61);
               if (index == -1) {
                  throw new IOException("unexpected property format - expecting name=value" + lineNumber + " : " + line);
               }

               String propName = params.substring(0, index);
               String propValue = this.assignProperties(params.substring(index + 1));
               System.setProperty(propName, propValue);
            }
         }
      }
   }

   private String assignProperties(String str) {
      int propertyIndexStart = 0;
      int propertyIndexEnd = 0;
      boolean requireProperty = false;

      String result;
      for(result = ""; propertyIndexStart < str.length(); propertyIndexStart = propertyIndexEnd) {
         int i1 = str.indexOf("${", propertyIndexStart);
         int i2 = str.indexOf("!{", propertyIndexStart);
         if (i1 == -1) {
            propertyIndexStart = i2;
         } else if (i2 == -1) {
            propertyIndexStart = i1;
         } else {
            propertyIndexStart = Math.min(i1, i2);
         }

         requireProperty = propertyIndexStart == i2;
         if (propertyIndexStart == -1) {
            break;
         }

         result = result + str.substring(propertyIndexEnd, propertyIndexStart);
         propertyIndexEnd = str.indexOf("}", propertyIndexStart);
         if (propertyIndexEnd == -1) {
            break;
         }

         String propertyKey = str.substring(propertyIndexStart + 2, propertyIndexEnd);
         String propertyValue = System.getProperty(propertyKey);
         if (propertyValue == null) {
            if (requireProperty) {
               throw new IllegalArgumentException("Variable " + propertyKey + " in groovy-starter.conf references a non-existent System property! Try passing the property to the VM using -D" + propertyKey + "=myValue in JAVA_OPTS");
            }

            return null;
         }

         propertyValue = this.getSlashyPath(propertyValue);
         propertyValue = this.correctDoubleSlash(propertyValue, propertyIndexEnd, str);
         result = result + propertyValue;
         ++propertyIndexEnd;
      }

      if (propertyIndexStart != -1 && propertyIndexStart < str.length()) {
         if (propertyIndexEnd == -1) {
            result = result + str.substring(propertyIndexStart);
         }
      } else {
         result = result + str.substring(propertyIndexEnd);
      }

      return result;
   }

   private String correctDoubleSlash(String propertyValue, int propertyIndexEnd, String str) {
      int index = propertyIndexEnd + 1;
      if (index < str.length() && str.charAt(index) == '/' && propertyValue.endsWith("/") && propertyValue.length() > 0) {
         propertyValue = propertyValue.substring(0, propertyValue.length() - 1);
      }

      return propertyValue;
   }

   private void loadFilteredPath(String filter) {
      if (filter != null) {
         filter = this.getSlashyPath(filter);
         int starIndex = filter.indexOf(42);
         if (starIndex == -1) {
            this.addFile(new File(filter));
         } else {
            boolean recursive = filter.indexOf("**") != -1;
            if (filter.lastIndexOf(47) < starIndex) {
               starIndex = filter.lastIndexOf(47) + 1;
            }

            String startDir = filter.substring(0, starIndex - 1);
            File root = new File(startDir);
            filter = Pattern.quote(filter);
            filter = filter.replaceAll("\\*\\*", "\\\\E.+?\\\\Q");
            filter = filter.replaceAll("\\*", "\\\\E[^/]+?\\\\Q");
            Pattern pattern = Pattern.compile(filter);
            File[] files = root.listFiles();
            if (files != null) {
               this.findMatchingFiles(files, pattern, recursive);
            }

         }
      }
   }

   private void findMatchingFiles(File[] files, Pattern pattern, boolean recursive) {
      for(int i = 0; i < files.length; ++i) {
         File file = files[i];
         String fileString = this.getSlashyPath(file.getPath());
         Matcher m = pattern.matcher(fileString);
         if (m.matches() && file.isFile()) {
            this.addFile(file);
         }

         if (file.isDirectory() && recursive) {
            File[] dirFiles = file.listFiles();
            if (dirFiles != null) {
               this.findMatchingFiles(dirFiles, pattern, true);
            }
         }
      }

   }

   private String getSlashyPath(String path) {
      String changedPath = path;
      if (File.separatorChar != '/') {
         changedPath = path.replace(File.separatorChar, '/');
      }

      return changedPath;
   }

   private boolean parentPathDoesExist(String path) {
      File dir = (new File(path)).getParentFile();
      return dir.exists();
   }

   private String getParentPath(String filter) {
      int index = filter.lastIndexOf(47);
      return index == -1 ? "" : filter.substring(index + 1);
   }

   public void addFile(File file) {
      if (file != null && file.exists()) {
         try {
            this.classPath.add(file.toURI().toURL());
         } catch (MalformedURLException var3) {
            throw new AssertionError("converting an existing file to an url should have never thrown an exception!");
         }
      }

   }

   public void addFile(String filename) {
      if (filename != null) {
         this.addFile(new File(filename));
      }

   }

   public void addClassPath(String path) {
      String[] paths = path.split(File.pathSeparator);
      String[] arr$ = paths;
      int len$ = paths.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String cpPath = arr$[i$];
         if (cpPath.endsWith("*")) {
            File dir = new File(cpPath.substring(0, cpPath.length() - 1));
            File[] files = dir.listFiles();
            if (files != null) {
               File[] arr$ = files;
               int len$ = files.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  File file = arr$[i$];
                  if (file.isFile() && file.getName().endsWith(".jar")) {
                     this.addFile(file);
                  }
               }
            }
         } else {
            this.addFile(new File(cpPath));
         }
      }

   }

   public URL[] getClassPathUrls() {
      return (URL[])((URL[])this.classPath.toArray(new URL[this.classPath.size()]));
   }

   public String getMainClass() {
      return this.main;
   }

   public void setMainClass(String classname) {
      this.main = classname;
      this.requireMain = false;
   }

   public void setRequireMain(boolean requireMain) {
      this.requireMain = requireMain;
   }
}

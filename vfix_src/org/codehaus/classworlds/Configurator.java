package org.codehaus.classworlds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Configurator {
   public static final String MAIN_PREFIX = "main is";
   public static final String SET_PREFIX = "set";
   public static final String IMPORT_PREFIX = "import";
   public static final String LOAD_PREFIX = "load";
   public static final String OPTIONALLY_PREFIX = "optionally";
   private Launcher launcher;
   private ClassWorld world;
   private Map configuredRealms;

   public Configurator(Launcher launcher) {
      this.launcher = launcher;
      this.configuredRealms = new HashMap();
   }

   public Configurator(ClassWorld world) {
      this.setClassWorld(world);
   }

   public void setClassWorld(ClassWorld world) {
      this.world = world;
      this.configuredRealms = new HashMap();
   }

   public void configure(InputStream is) throws IOException, MalformedURLException, ConfigurationException, DuplicateRealmException, NoSuchRealmException {
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      if (this.world == null) {
         this.world = new ClassWorld();
      }

      ClassLoader foreignClassLoader = null;
      if (this.launcher != null) {
         foreignClassLoader = this.launcher.getSystemClassLoader();
      }

      ClassRealm curRealm = null;
      String line = null;
      int lineNo = 0;
      boolean mainSet = false;

      while(true) {
         line = reader.readLine();
         if (line == null) {
            this.associateRealms();
            if (this.launcher != null) {
               this.launcher.setWorld(this.world);
            }

            reader.close();
            return;
         }

         ++lineNo;
         line = line.trim();
         if (!this.canIgnore(line)) {
            String constituent;
            int fromLoc;
            String property;
            String propertiesFileName;
            if (line.startsWith("main is")) {
               if (mainSet) {
                  throw new ConfigurationException("Duplicate main configuration", lineNo, line);
               }

               constituent = line.substring("main is".length()).trim();
               fromLoc = constituent.indexOf("from");
               if (fromLoc < 0) {
                  throw new ConfigurationException("Missing from clause", lineNo, line);
               }

               property = constituent.substring(0, fromLoc).trim();
               propertiesFileName = constituent.substring(fromLoc + 4).trim();
               if (this.launcher != null) {
                  this.launcher.setAppMain(property, propertiesFileName);
               }

               mainSet = true;
            } else if (line.startsWith("set")) {
               constituent = line.substring("set".length()).trim();
               fromLoc = constituent.indexOf(" using") + 1;
               property = null;
               propertiesFileName = null;
               if (fromLoc > 0) {
                  property = constituent.substring(0, fromLoc).trim();
                  propertiesFileName = this.filter(constituent.substring(fromLoc + 5).trim());
                  constituent = propertiesFileName;
               }

               String defaultValue = null;
               int defaultLoc = constituent.indexOf(" default") + 1;
               if (defaultLoc > 0) {
                  defaultValue = constituent.substring(defaultLoc + 7).trim();
                  if (property == null) {
                     property = constituent.substring(0, defaultLoc).trim();
                  } else {
                     propertiesFileName = constituent.substring(0, defaultLoc).trim();
                  }
               }

               String value = System.getProperty(property);
               if (value == null) {
                  if (propertiesFileName != null) {
                     File propertiesFile = new File(propertiesFileName);
                     if (propertiesFile.exists()) {
                        Properties properties = new Properties();

                        try {
                           properties.load(new FileInputStream(propertiesFileName));
                           value = properties.getProperty(property);
                        } catch (Exception var20) {
                        }
                     }
                  }

                  if (value == null && defaultValue != null) {
                     value = defaultValue;
                  }

                  if (value != null) {
                     value = this.filter(value);
                     System.setProperty(property, value);
                  }
               }
            } else if (line.startsWith("[")) {
               int rbrack = line.indexOf("]");
               if (rbrack < 0) {
                  throw new ConfigurationException("Invalid realm specifier", lineNo, line);
               }

               String realmName = line.substring(1, rbrack);
               curRealm = this.world.newRealm(realmName, foreignClassLoader);
               this.configuredRealms.put(realmName, curRealm);
            } else if (line.startsWith("import")) {
               if (curRealm == null) {
                  throw new ConfigurationException("Unhandled import", lineNo, line);
               }

               constituent = line.substring("import".length()).trim();
               fromLoc = constituent.indexOf("from");
               if (fromLoc < 0) {
                  throw new ConfigurationException("Missing from clause", lineNo, line);
               }

               property = constituent.substring(0, fromLoc).trim();
               propertiesFileName = constituent.substring(fromLoc + 4).trim();
               curRealm.importFrom(propertiesFileName, property);
            } else {
               File file;
               if (line.startsWith("load")) {
                  constituent = line.substring("load".length()).trim();
                  constituent = this.filter(constituent);
                  if (constituent.indexOf("*") >= 0) {
                     this.loadGlob(constituent, curRealm);
                  } else {
                     file = new File(constituent);
                     if (file.exists()) {
                        curRealm.addConstituent(file.toURL());
                     } else {
                        try {
                           curRealm.addConstituent(new URL(constituent));
                        } catch (MalformedURLException var19) {
                           throw new FileNotFoundException(constituent);
                        }
                     }
                  }
               } else {
                  if (!line.startsWith("optionally")) {
                     throw new ConfigurationException("Unhandled configuration", lineNo, line);
                  }

                  constituent = line.substring("optionally".length()).trim();
                  constituent = this.filter(constituent);
                  if (constituent.indexOf("*") >= 0) {
                     this.loadGlob(constituent, curRealm, true);
                  } else {
                     file = new File(constituent);
                     if (file.exists()) {
                        curRealm.addConstituent(file.toURL());
                     } else {
                        try {
                           curRealm.addConstituent(new URL(constituent));
                        } catch (MalformedURLException var18) {
                        }
                     }
                  }
               }
            }
         }
      }
   }

   protected void associateRealms() {
      List sortRealmNames = new ArrayList(this.configuredRealms.keySet());
      Comparator comparator = new Comparator() {
         public int compare(Object o1, Object o2) {
            String g1 = (String)o1;
            String g2 = (String)o2;
            return g1.compareTo(g2);
         }
      };
      Collections.sort(sortRealmNames, comparator);
      Iterator i = sortRealmNames.iterator();

      while(i.hasNext()) {
         String realmName = (String)i.next();
         int j = realmName.lastIndexOf(46);
         if (j > 0) {
            String parentRealmName = realmName.substring(0, j);
            ClassRealm parentRealm = (ClassRealm)this.configuredRealms.get(parentRealmName);
            if (parentRealm != null) {
               ClassRealm realm = (ClassRealm)this.configuredRealms.get(realmName);
               realm.setParent(parentRealm);
            }
         }
      }

   }

   protected void loadGlob(String line, ClassRealm realm) throws MalformedURLException, FileNotFoundException {
      this.loadGlob(line, realm, false);
   }

   protected void loadGlob(String line, ClassRealm realm, boolean optionally) throws MalformedURLException, FileNotFoundException {
      File globFile = new File(line);
      File dir = globFile.getParentFile();
      if (!dir.exists()) {
         if (!optionally) {
            throw new FileNotFoundException(dir.toString());
         }
      } else {
         String localName = globFile.getName();
         int starLoc = localName.indexOf("*");
         final String prefix = localName.substring(0, starLoc);
         final String suffix = localName.substring(starLoc + 1);
         File[] matches = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
               if (!name.startsWith(prefix)) {
                  return false;
               } else {
                  return name.endsWith(suffix);
               }
            }
         });

         for(int i = 0; i < matches.length; ++i) {
            realm.addConstituent(matches[i].toURL());
         }

      }
   }

   protected String filter(String text) throws ConfigurationException {
      String result = "";
      int cur = 0;
      int textLen = text.length();
      int propStart = true;
      int propStop = true;
      String propName = null;

      int propStop;
      for(String propValue = null; cur < textLen; cur = propStop + 1) {
         int propStart = text.indexOf("${", cur);
         if (propStart < 0) {
            break;
         }

         result = result + text.substring(cur, propStart);
         propStop = text.indexOf("}", propStart);
         if (propStop < 0) {
            throw new ConfigurationException("Unterminated property: " + text.substring(propStart));
         }

         propName = text.substring(propStart + 2, propStop);
         propValue = System.getProperty(propName);
         if (propValue == null) {
            throw new ConfigurationException("No such property: " + propName);
         }

         result = result + propValue;
      }

      result = result + text.substring(cur);
      return result;
   }

   private boolean canIgnore(String line) {
      return line.length() == 0 || line.startsWith("#");
   }
}

package soot.util.cfgcmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.Singletons;

public class AltClassLoader extends ClassLoader {
   private static final Logger logger = LoggerFactory.getLogger(AltClassLoader.class);
   private static final boolean DEBUG = false;
   private String[] locations;
   private final Map<String, Class<?>> alreadyFound = new HashMap();
   private final Map<String, String> nameToMangledName = new HashMap();
   private final Map<String, String> mangledNameToName = new HashMap();

   public AltClassLoader(Singletons.Global g) {
   }

   public static AltClassLoader v() {
      return G.v().soot_util_cfgcmd_AltClassLoader();
   }

   public void setAltClassPath(String altClassPath) {
      List<String> locationList = new LinkedList();
      StringTokenizer tokens = new StringTokenizer(altClassPath, File.pathSeparator, false);

      while(tokens.hasMoreTokens()) {
         String location = tokens.nextToken();
         locationList.add(location);
      }

      this.locations = new String[locationList.size()];
      this.locations = (String[])locationList.toArray(this.locations);
   }

   public void setAltClasses(String[] classNames) {
      this.nameToMangledName.clear();
      String[] var2 = classNames;
      int var3 = classNames.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String origName = var2[var4];
         String mangledName = mangleName(origName);
         this.nameToMangledName.put(origName, mangledName);
         this.mangledNameToName.put(mangledName, origName);
      }

   }

   private static String mangleName(String origName) throws IllegalArgumentException {
      char dot = true;
      char dotReplacement = true;
      StringBuffer mangledName = new StringBuffer(origName);
      int replacements = 0;
      int lastDot = origName.lastIndexOf(46);

      for(int nextDot = lastDot; (nextDot = origName.lastIndexOf(46, nextDot - 1)) >= 0; ++replacements) {
         mangledName.setCharAt(nextDot, '_');
      }

      if (replacements <= 0) {
         throw new IllegalArgumentException("AltClassLoader.mangleName()'s crude classname mangling cannot deal with " + origName);
      } else {
         return mangledName.toString();
      }
   }

   protected Class<?> findClass(String maybeMangledName) throws ClassNotFoundException {
      Class<?> result = (Class)this.alreadyFound.get(maybeMangledName);
      if (result != null) {
         return result;
      } else {
         String name = (String)this.mangledNameToName.get(maybeMangledName);
         if (name == null) {
            name = maybeMangledName;
         }

         String pathTail = "/" + name.replace('.', File.separatorChar) + ".class";
         String[] var5 = this.locations;
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String element = var5[var7];
            String path = element + pathTail;

            try {
               FileInputStream stream = new FileInputStream(path);
               byte[] classBytes = new byte[stream.available()];
               stream.read(classBytes);
               this.replaceAltClassNames(classBytes);
               result = this.defineClass(maybeMangledName, classBytes, 0, classBytes.length);
               this.alreadyFound.put(maybeMangledName, result);
               stream.close();
               return result;
            } catch (IOException var12) {
            } catch (ClassFormatError var13) {
            }
         }

         throw new ClassNotFoundException("Unable to find class" + name + " in alternate classpath");
      }
   }

   public Class<?> loadClass(String name) throws ClassNotFoundException {
      String nameForParent = (String)this.nameToMangledName.get(name);
      if (nameForParent == null) {
         nameForParent = name;
      }

      return super.loadClass(nameForParent, false);
   }

   private void replaceAltClassNames(byte[] classBytes) {
      Iterator var2 = this.nameToMangledName.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<String, String> entry = (Entry)var2.next();
         String origName = (String)entry.getKey();
         origName = origName.replace('.', '/');
         String mangledName = (String)entry.getValue();
         mangledName = mangledName.replace('.', '/');
         findAndReplace(classBytes, stringToUtf8Pattern(origName), stringToUtf8Pattern(mangledName));
         findAndReplace(classBytes, stringToTypeStringPattern(origName), stringToTypeStringPattern(mangledName));
      }

   }

   private static byte[] stringToUtf8Pattern(String s) {
      byte[] origBytes = s.getBytes();
      int length = origBytes.length;
      byte CONSTANT_Utf8 = true;
      byte[] result = new byte[length + 3];
      result[0] = 1;
      result[1] = (byte)(length & '\uff00');
      result[2] = (byte)(length & 255);

      for(int i = 0; i < length; ++i) {
         result[i + 3] = origBytes[i];
      }

      return result;
   }

   private static byte[] stringToTypeStringPattern(String s) {
      byte[] origBytes = s.getBytes();
      int length = origBytes.length;
      byte[] result = new byte[length + 2];
      result[0] = 76;

      for(int i = 0; i < length; ++i) {
         result[i + 1] = origBytes[i];
      }

      result[length + 1] = 59;
      return result;
   }

   private static void findAndReplace(byte[] text, byte[] pattern, byte[] replacement) throws IllegalArgumentException {
      int patternLength = pattern.length;
      if (patternLength != replacement.length) {
         throw new IllegalArgumentException("findAndReplace(): The lengths of the pattern and replacement must match.");
      } else {
         for(int match = 0; (match = findMatch(text, pattern, match)) >= 0; match += patternLength) {
            replace(text, replacement, match);
         }

      }
   }

   private static int findMatch(byte[] text, byte[] pattern, int start) {
      int textLength = text.length;
      int patternLength = pattern.length;

      label24:
      for(int base = start; base < textLength; ++base) {
         int t = base;

         for(int p = 0; p < patternLength; ++p) {
            if (text[t] != pattern[p]) {
               continue label24;
            }

            ++t;
         }

         return base;
      }

      return -1;
   }

   private static void replace(byte[] text, byte[] replacement, int start) {
      int t = start;

      for(int p = 0; p < replacement.length; ++p) {
         text[t] = replacement[p];
         ++t;
      }

   }

   public static void main(String[] argv) throws ClassNotFoundException {
      v().setAltClassPath(argv[0]);

      for(int i = 1; i < argv.length; ++i) {
         v().setAltClasses(new String[]{argv[i]});
         logger.debug("main() loadClass(" + argv[i] + ")");
         v().loadClass(argv[i]);
      }

   }
}

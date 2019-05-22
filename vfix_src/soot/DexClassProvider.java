package soot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.ClassDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.dexpler.DexFileProvider;
import soot.dexpler.Util;
import soot.options.Options;

public class DexClassProvider implements ClassProvider {
   private static final Logger logger = LoggerFactory.getLogger(DexClassProvider.class);

   public static Set<String> classesOfDex(DexBackedDexFile dexFile) {
      Set<String> classes = new HashSet();
      Iterator var2 = dexFile.getClasses().iterator();

      while(var2.hasNext()) {
         ClassDef c = (ClassDef)var2.next();
         String name = Util.dottedClassName(c.getType());
         classes.add(name);
      }

      return classes;
   }

   public ClassSource find(String className) {
      this.ensureDexIndex();
      Map<String, File> index = SourceLocator.v().dexClassIndex();
      File file = (File)index.get(className);
      return file == null ? null : new DexClassSource(className, file);
   }

   protected void ensureDexIndex() {
      Map<String, File> index = SourceLocator.v().dexClassIndex();
      if (index == null) {
         Map<String, File> index = new HashMap();
         this.buildDexIndex(index, SourceLocator.v().classPath());
         SourceLocator.v().setDexClassIndex(index);
      }

      if (SourceLocator.v().getDexClassPathExtensions() != null) {
         this.buildDexIndex(SourceLocator.v().dexClassIndex(), new ArrayList(SourceLocator.v().getDexClassPathExtensions()));
         SourceLocator.v().clearDexClassPathExtensions();
      }

   }

   private void buildDexIndex(Map<String, File> index, List<String> classPath) {
      Iterator var3 = classPath.iterator();

      while(var3.hasNext()) {
         String path = (String)var3.next();

         try {
            Iterator var5 = DexFileProvider.v().getDexFromSource(new File(path)).iterator();

            while(var5.hasNext()) {
               DexFileProvider.DexContainer container = (DexFileProvider.DexContainer)var5.next();
               Iterator var7 = classesOfDex(container.getBase()).iterator();

               while(var7.hasNext()) {
                  String className = (String)var7.next();
                  if (!index.containsKey(className)) {
                     index.put(className, container.getFilePath());
                  } else if (Options.v().verbose()) {
                     logger.debug("" + String.format("Warning: Duplicate of class '%s' found in dex file '%s' from source '%s'. Omitting class.", className, container.getDexName(), container.getFilePath().getCanonicalPath()));
                  }
               }
            }
         } catch (IOException var9) {
            logger.warn("IO error while processing dex file '" + path + "'");
            logger.debug("Exception: " + var9);
         } catch (Exception var10) {
            logger.warn("exception while processing dex file '" + path + "'");
            logger.debug("Exception: " + var10);
         }
      }

   }
}

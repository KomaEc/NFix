package soot.jimple.toolkits.base;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.PhaseOptions;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.options.Options;
import soot.util.Chain;

public class RenameDuplicatedClasses extends SceneTransformer {
   private static final Logger logger = LoggerFactory.getLogger(RenameDuplicatedClasses.class);
   private static final String FIXED_CLASS_NAME_SPERATOR = "-";

   public RenameDuplicatedClasses(Singletons.Global g) {
   }

   public static RenameDuplicatedClasses v() {
      return G.v().soot_jimple_toolkits_base_RenameDuplicatedClasses();
   }

   protected void internalTransform(String phaseName, Map<String, String> options) {
      if (!this.isFileSystemCaseSensitive()) {
         String fixedClassNamesStr = PhaseOptions.getString(options, "fixedClassNames");
         String[] classNames = fixedClassNamesStr.split("-");
         List<String> fixedClassNames = Arrays.asList(classNames);
         this.duplicatedCheck(fixedClassNames);
         if (Options.v().verbose()) {
            logger.debug("The fixed class names are: " + fixedClassNames);
         }

         Chain<SootClass> sootClasses = Scene.v().getClasses();
         Map<String, String> lowerCaseClassNameToReal = new HashMap();
         int count = 0;
         Iterator iter = sootClasses.snapshotIterator();

         while(iter.hasNext()) {
            SootClass sootClass = (SootClass)iter.next();
            String className = sootClass.getName();
            if (lowerCaseClassNameToReal.containsKey(className.toLowerCase())) {
               if (fixedClassNames.contains(className)) {
                  sootClass = Scene.v().getSootClass((String)lowerCaseClassNameToReal.get(className.toLowerCase()));
                  className = (String)lowerCaseClassNameToReal.get(className.toLowerCase());
               }

               String newClassName = className + count++;
               sootClass.rename(newClassName);
               logger.debug("Rename duplicated class " + className + " to class " + newClassName);
            } else {
               lowerCaseClassNameToReal.put(className.toLowerCase(), className);
            }
         }

      }
   }

   public void duplicatedCheck(List<String> classNames) {
      Set<String> classNameSet = new HashSet();
      Iterator var3 = classNames.iterator();

      while(var3.hasNext()) {
         String className = (String)var3.next();
         if (classNameSet.contains(className.toLowerCase())) {
            throw new RuntimeException("The fixed class names cannot contain duplicated class names.");
         }

         classNameSet.add(className.toLowerCase());
      }

   }

   public boolean isFileSystemCaseSensitive() {
      File dir = new File(".");
      File[] files = dir.listFiles();
      if (files == null) {
         return false;
      } else {
         File[] var3 = files;
         int var4 = files.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File file = var3[var5];
            if (file.isFile()) {
               String lowerCaseFilePath = file.getAbsolutePath().toLowerCase();
               String upperCaseFilePath = file.getAbsolutePath().toUpperCase();
               File lowerCaseFile = new File(lowerCaseFilePath);
               File upperCaseFile = new File(upperCaseFilePath);
               if (!lowerCaseFile.exists() || !upperCaseFile.exists()) {
                  return true;
               }
            }
         }

         return false;
      }
   }
}

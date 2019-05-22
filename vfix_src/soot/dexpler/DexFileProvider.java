package soot.dexpler;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.jf.dexlib2.DexFileFactory;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.iface.MultiDexContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.CompilationDeathException;
import soot.G;
import soot.Scene;
import soot.Singletons;
import soot.options.Options;

public class DexFileProvider {
   private static final Logger logger = LoggerFactory.getLogger(DexFileProvider.class);
   private static final Comparator<DexFileProvider.DexContainer> DEFAULT_PRIORITIZER = new Comparator<DexFileProvider.DexContainer>() {
      public int compare(DexFileProvider.DexContainer o1, DexFileProvider.DexContainer o2) {
         String s1 = o1.getDexName();
         String s2 = o2.getDexName();
         if (s1.equals("classes.dex")) {
            return 1;
         } else if (s2.equals("classes.dex")) {
            return -1;
         } else {
            boolean s1StartsClasses = s1.startsWith("classes");
            boolean s2StartsClasses = s2.startsWith("classes");
            if (s1StartsClasses && !s2StartsClasses) {
               return 1;
            } else {
               return s2StartsClasses && !s1StartsClasses ? -1 : s1.compareTo(s2);
            }
         }
      }
   };
   private final Map<String, Map<String, DexFileProvider.DexContainer>> dexMap = new HashMap();

   public DexFileProvider(Singletons.Global g) {
   }

   public static DexFileProvider v() {
      return G.v().soot_dexpler_DexFileProvider();
   }

   public List<DexFileProvider.DexContainer> getDexFromSource(File dexSource) throws IOException {
      return this.getDexFromSource(dexSource, DEFAULT_PRIORITIZER);
   }

   public List<DexFileProvider.DexContainer> getDexFromSource(File dexSource, Comparator<DexFileProvider.DexContainer> prioritizer) throws IOException {
      ArrayList<DexFileProvider.DexContainer> resultList = new ArrayList();
      List<File> allSources = this.allSourcesFromFile(dexSource);
      this.updateIndex(allSources);
      Iterator var5 = allSources.iterator();

      while(var5.hasNext()) {
         File theSource = (File)var5.next();
         resultList.addAll(((Map)this.dexMap.get(theSource.getCanonicalPath())).values());
      }

      if (resultList.size() > 1) {
         Collections.sort(resultList, Collections.reverseOrder(prioritizer));
      }

      return resultList;
   }

   public DexFileProvider.DexContainer getDexFromSource(File dexSource, String dexName) throws IOException {
      List<File> allSources = this.allSourcesFromFile(dexSource);
      this.updateIndex(allSources);
      Iterator var4 = allSources.iterator();

      DexFileProvider.DexContainer dexFile;
      do {
         if (!var4.hasNext()) {
            throw new CompilationDeathException("Dex file with name '" + dexName + "' not found in " + dexSource);
         }

         File theSource = (File)var4.next();
         dexFile = (DexFileProvider.DexContainer)((Map)this.dexMap.get(theSource.getCanonicalPath())).get(dexName);
      } while(dexFile == null);

      return dexFile;
   }

   private List<File> allSourcesFromFile(File dexSource) throws IOException {
      if (dexSource.isDirectory()) {
         List<File> dexFiles = this.getAllDexFilesInDirectory(dexSource);
         if (dexFiles.size() > 1 && !Options.v().process_multiple_dex()) {
            File file = (File)dexFiles.get(0);
            logger.warn("Multiple dex files detected, only processing '" + file.getCanonicalPath() + "'. Use '-process-multiple-dex' option to process them all.");
            return Collections.singletonList(file);
         } else {
            return dexFiles;
         }
      } else {
         String ext = Files.getFileExtension(dexSource.getName()).toLowerCase();
         return (ext.equals("jar") || ext.equals("zip")) && !Options.v().search_dex_in_archives() ? Collections.EMPTY_LIST : Collections.singletonList(dexSource);
      }
   }

   private void updateIndex(List<File> dexSources) throws IOException {
      Iterator var2 = dexSources.iterator();

      while(var2.hasNext()) {
         File theSource = (File)var2.next();
         String key = theSource.getCanonicalPath();
         Map<String, DexFileProvider.DexContainer> dexFiles = (Map)this.dexMap.get(key);
         if (dexFiles == null) {
            try {
               dexFiles = this.mappingForFile(theSource);
               this.dexMap.put(key, dexFiles);
            } catch (IOException var7) {
               throw new CompilationDeathException("Error parsing dex source", var7);
            }
         }
      }

   }

   private Map<String, DexFileProvider.DexContainer> mappingForFile(File dexSourceFile) throws IOException {
      int api = Scene.v().getAndroidAPIVersion();
      boolean multiple_dex = Options.v().process_multiple_dex();
      MultiDexContainer<? extends DexBackedDexFile> dexContainer = DexFileFactory.loadDexContainer(dexSourceFile, Opcodes.forApi(api));
      List<String> dexEntryNameList = dexContainer.getDexEntryNames();
      int dexFileCount = dexEntryNameList.size();
      if (dexFileCount < 1) {
         if (Options.v().verbose()) {
            logger.debug("" + String.format("Warning: No dex file found in '%s'", dexSourceFile));
         }

         return Collections.emptyMap();
      } else {
         Map<String, DexFileProvider.DexContainer> dexMap = new HashMap(dexFileCount);
         ListIterator entryNameIterator = dexEntryNameList.listIterator(dexFileCount);

         while(true) {
            while(entryNameIterator.hasPrevious()) {
               String entryName = (String)entryNameIterator.previous();
               DexBackedDexFile entry = (DexBackedDexFile)dexContainer.getEntry(entryName);
               entryName = this.deriveDexName(entryName);
               logger.debug("" + String.format("Found dex file '%s' with %d classes in '%s'", entryName, entry.getClasses().size(), dexSourceFile.getCanonicalPath()));
               if (multiple_dex) {
                  ((Map)dexMap).put(entryName, new DexFileProvider.DexContainer(entry, entryName, dexSourceFile));
               } else if (((Map)dexMap).isEmpty() && (entryName.equals("classes.dex") || !entryNameIterator.hasPrevious())) {
                  dexMap = Collections.singletonMap(entryName, new DexFileProvider.DexContainer(entry, entryName, dexSourceFile));
                  if (dexFileCount > 1) {
                     logger.warn("Multiple dex files detected, only processing '" + entryName + "'. Use '-process-multiple-dex' option to process them all.");
                  }
               }
            }

            return Collections.unmodifiableMap((Map)dexMap);
         }
      }
   }

   private String deriveDexName(String entryName) {
      return (new File(entryName)).getName();
   }

   private List<File> getAllDexFilesInDirectory(File path) {
      Queue<File> toVisit = new ArrayDeque();
      Set<File> visited = new HashSet();
      List<File> ret = new ArrayList();
      toVisit.add(path);

      while(!toVisit.isEmpty()) {
         File cur = (File)toVisit.poll();
         if (!visited.contains(cur)) {
            visited.add(cur);
            if (cur.isDirectory()) {
               toVisit.addAll(Arrays.asList(cur.listFiles()));
            } else if (cur.isFile() && cur.getName().endsWith(".dex")) {
               ret.add(cur);
            }
         }
      }

      return ret;
   }

   public static final class DexContainer {
      private final DexBackedDexFile base;
      private final String name;
      private final File filePath;

      public DexContainer(DexBackedDexFile base, String name, File filePath) {
         this.base = base;
         this.name = name;
         this.filePath = filePath;
      }

      public DexBackedDexFile getBase() {
         return this.base;
      }

      public String getDexName() {
         return this.name;
      }

      public File getFilePath() {
         return this.filePath;
      }
   }
}

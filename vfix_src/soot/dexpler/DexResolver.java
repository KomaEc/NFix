package soot.dexpler;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import soot.G;
import soot.Singletons;
import soot.SootClass;
import soot.javaToJimple.IInitialResolver;
import soot.tagkit.SourceFileTag;

public class DexResolver {
   protected Map<File, DexlibWrapper> cache = new TreeMap();

   public DexResolver(Singletons.Global g) {
   }

   public static DexResolver v() {
      return G.v().soot_dexpler_DexResolver();
   }

   public IInitialResolver.Dependencies resolveFromFile(File file, String className, SootClass sc) {
      DexlibWrapper wrapper = this.initializeDexFile(file);
      IInitialResolver.Dependencies deps = wrapper.makeSootClass(sc, className);
      addSourceFileTag(sc, "dalvik_source_" + file.getName());
      return deps;
   }

   protected DexlibWrapper initializeDexFile(File file) {
      DexlibWrapper wrapper = (DexlibWrapper)this.cache.get(file);
      if (wrapper == null) {
         wrapper = new DexlibWrapper(file);
         this.cache.put(file, wrapper);
         wrapper.initialize();
      }

      return wrapper;
   }

   protected static void addSourceFileTag(SootClass sc, String fileName) {
      SourceFileTag tag = null;
      if (!sc.hasTag("SourceFileTag")) {
         tag = new SourceFileTag();
         sc.addTag(tag);
         tag.setSourceFile(fileName);
      }
   }
}

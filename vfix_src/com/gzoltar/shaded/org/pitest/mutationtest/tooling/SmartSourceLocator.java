package com.gzoltar.shaded.org.pitest.mutationtest.tooling;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FArray;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.mutationtest.SourceLocator;
import java.io.File;
import java.io.Reader;
import java.util.Collection;
import java.util.Iterator;

public class SmartSourceLocator implements SourceLocator {
   private static final int MAX_DEPTH = 4;
   private final Collection<SourceLocator> children;

   public SmartSourceLocator(Collection<File> roots) {
      Collection<File> childDirs = FCollection.flatMap(roots, this.collectChildren(0));
      childDirs.addAll(roots);
      F<File, SourceLocator> fileToSourceLocator = new F<File, SourceLocator>() {
         public SourceLocator apply(File a) {
            return new DirectorySourceLocator(a);
         }
      };
      this.children = FCollection.map(childDirs, fileToSourceLocator);
   }

   private F<File, Collection<File>> collectChildren(final int depth) {
      return new F<File, Collection<File>>() {
         public Collection<File> apply(File a) {
            return SmartSourceLocator.this.collectDirectories(a, depth);
         }
      };
   }

   private Collection<File> collectDirectories(File root, int depth) {
      Collection<File> childDirs = listFirstLevelDirectories(root);
      if (depth < 4) {
         childDirs.addAll(FCollection.flatMap(childDirs, this.collectChildren(depth + 1)));
      }

      return childDirs;
   }

   private static Collection<File> listFirstLevelDirectories(File root) {
      F<File, Boolean> p = new F<File, Boolean>() {
         public Boolean apply(File a) {
            return a.isDirectory();
         }
      };
      return FArray.filter(root.listFiles(), p);
   }

   public Option<Reader> locate(Collection<String> classes, String fileName) {
      Iterator i$ = this.children.iterator();

      Option reader;
      do {
         if (!i$.hasNext()) {
            return Option.none();
         }

         SourceLocator each = (SourceLocator)i$.next();
         reader = each.locate(classes, fileName);
      } while(!reader.hasSome());

      return reader;
   }
}

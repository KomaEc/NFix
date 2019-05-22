package com.gzoltar.shaded.org.pitest.mutationtest.tooling;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.mutationtest.SourceLocator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Collection;
import java.util.List;

public class DirectorySourceLocator implements SourceLocator {
   private final File root;
   private final F<File, Option<Reader>> fileToReader;

   DirectorySourceLocator(File root, F<File, Option<Reader>> fileToReader) {
      this.root = root;
      this.fileToReader = fileToReader;
   }

   public DirectorySourceLocator(File root) {
      this(root, new DirectorySourceLocator.FileToReader());
   }

   public Option<Reader> locate(Collection<String> classes, String fileName) {
      List<Reader> matches = FCollection.flatMap(classes, this.classNameToSourceFileReader(fileName));
      return (Option)(matches.isEmpty() ? Option.none() : Option.some(matches.iterator().next()));
   }

   private F<String, Iterable<Reader>> classNameToSourceFileReader(final String fileName) {
      return new F<String, Iterable<Reader>>() {
         public Iterable<Reader> apply(String className) {
            if (className.contains(".")) {
               File f = new File(className.replace(".", File.separator));
               return DirectorySourceLocator.this.locate(f.getParent() + File.separator + fileName);
            } else {
               return DirectorySourceLocator.this.locate(fileName);
            }
         }
      };
   }

   private Option<Reader> locate(String fileName) {
      File f = new File(this.root + File.separator + fileName);
      return (Option)this.fileToReader.apply(f);
   }

   private static class FileToReader implements F<File, Option<Reader>> {
      private FileToReader() {
      }

      public Option<Reader> apply(File f) {
         if (f.exists()) {
            try {
               return Option.some(new FileReader(f));
            } catch (FileNotFoundException var3) {
               return Option.none();
            }
         } else {
            return Option.none();
         }
      }

      // $FF: synthetic method
      FileToReader(Object x0) {
         this();
      }
   }
}

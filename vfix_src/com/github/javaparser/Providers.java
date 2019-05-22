package com.github.javaparser;

import com.github.javaparser.utils.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Providers {
   public static final Charset UTF8 = Charset.forName("utf-8");

   private Providers() {
   }

   public static Provider provider(Reader reader) {
      return new StreamProvider((Reader)Utils.assertNotNull(reader));
   }

   public static Provider provider(InputStream input, Charset encoding) {
      Utils.assertNotNull(input);
      Utils.assertNotNull(encoding);

      try {
         return new StreamProvider(input, encoding.name());
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public static Provider provider(InputStream input) {
      return provider(input, UTF8);
   }

   public static Provider provider(File file, Charset encoding) throws FileNotFoundException {
      return provider((InputStream)(new FileInputStream((File)Utils.assertNotNull(file))), (Charset)Utils.assertNotNull(encoding));
   }

   public static Provider provider(File file) throws FileNotFoundException {
      return provider((File)Utils.assertNotNull(file), UTF8);
   }

   public static Provider provider(Path path, Charset encoding) throws IOException {
      return provider(Files.newInputStream((Path)Utils.assertNotNull(path)), (Charset)Utils.assertNotNull(encoding));
   }

   public static Provider provider(Path path) throws IOException {
      return provider((Path)Utils.assertNotNull(path), UTF8);
   }

   public static Provider provider(String source) {
      return new StringProvider((String)Utils.assertNotNull(source));
   }

   public static Provider resourceProvider(ClassLoader classLoader, String pathToResource, Charset encoding) throws IOException {
      InputStream resourceAsStream = classLoader.getResourceAsStream(pathToResource);
      if (resourceAsStream == null) {
         throw new IOException("Cannot find " + pathToResource);
      } else {
         return provider(resourceAsStream, encoding);
      }
   }

   public static Provider resourceProvider(String pathToResource, Charset encoding) throws IOException {
      ClassLoader classLoader = Provider.class.getClassLoader();
      return resourceProvider(classLoader, pathToResource, encoding);
   }

   public static Provider resourceProvider(String pathToResource) throws IOException {
      return resourceProvider(pathToResource, UTF8);
   }
}

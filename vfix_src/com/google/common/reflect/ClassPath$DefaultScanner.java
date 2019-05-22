package com.google.common.reflect;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@VisibleForTesting
final class ClassPath$DefaultScanner extends ClassPath.Scanner {
   private final SetMultimap<ClassLoader, String> resources = MultimapBuilder.hashKeys().linkedHashSetValues().build();

   ImmutableSet<ClassPath.ResourceInfo> getResources() {
      ImmutableSet.Builder<ClassPath.ResourceInfo> builder = ImmutableSet.builder();
      Iterator var2 = this.resources.entries().iterator();

      while(var2.hasNext()) {
         Entry<ClassLoader, String> entry = (Entry)var2.next();
         builder.add((Object)ClassPath.ResourceInfo.of((String)entry.getValue(), (ClassLoader)entry.getKey()));
      }

      return builder.build();
   }

   protected void scanJarFile(ClassLoader classloader, JarFile file) {
      Enumeration entries = file.entries();

      while(entries.hasMoreElements()) {
         JarEntry entry = (JarEntry)entries.nextElement();
         if (!entry.isDirectory() && !entry.getName().equals("META-INF/MANIFEST.MF")) {
            this.resources.get(classloader).add(entry.getName());
         }
      }

   }

   protected void scanDirectory(ClassLoader classloader, File directory) throws IOException {
      Set<File> currentPath = new HashSet();
      currentPath.add(directory.getCanonicalFile());
      this.scanDirectory(directory, classloader, "", currentPath);
   }

   private void scanDirectory(File directory, ClassLoader classloader, String packagePrefix, Set<File> currentPath) throws IOException {
      File[] files = directory.listFiles();
      if (files == null) {
         ClassPath.access$100().warning("Cannot read directory " + directory);
      } else {
         File[] var6 = files;
         int var7 = files.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            File f = var6[var8];
            String name = f.getName();
            if (f.isDirectory()) {
               File deref = f.getCanonicalFile();
               if (currentPath.add(deref)) {
                  this.scanDirectory(deref, classloader, packagePrefix + name + "/", currentPath);
                  currentPath.remove(deref);
               }
            } else {
               String resourceName = packagePrefix + name;
               if (!resourceName.equals("META-INF/MANIFEST.MF")) {
                  this.resources.get(classloader).add(resourceName);
               }
            }
         }

      }
   }
}

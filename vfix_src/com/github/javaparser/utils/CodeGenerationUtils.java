package com.github.javaparser.utils;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class CodeGenerationUtils {
   private CodeGenerationUtils() {
   }

   public static String getterName(Class<?> type, String name) {
      if (name.startsWith("is")) {
         return name;
      } else {
         return type.equals(Boolean.class) ? "is" + Utils.capitalize(name) : "get" + Utils.capitalize(name);
      }
   }

   public static String getterToPropertyName(String getterName) {
      if (getterName.startsWith("is")) {
         return Utils.decapitalize(getterName.substring("is".length()));
      } else if (getterName.startsWith("get")) {
         return Utils.decapitalize(getterName.substring("get".length()));
      } else if (getterName.startsWith("has")) {
         return Utils.decapitalize(getterName.substring("has".length()));
      } else {
         throw new IllegalArgumentException("Unexpected getterName '" + getterName + "'");
      }
   }

   public static String setterName(String fieldName) {
      return fieldName.startsWith("is") ? "set" + fieldName.substring(2) : "set" + Utils.capitalize(fieldName);
   }

   public static String optionalOf(String text, boolean isOptional) {
      return isOptional ? f("Optional.of(%s)", text) : "Optional.empty()";
   }

   public static String f(String format, Object... params) {
      return String.format(format, params);
   }

   public static Path fileInPackageAbsolutePath(String root, String pkg, String file) {
      pkg = packageToPath(pkg);
      return Paths.get(root, pkg, file).normalize();
   }

   public static Path fileInPackageAbsolutePath(Path root, String pkg, String file) {
      return fileInPackageAbsolutePath(root.toString(), pkg, file);
   }

   public static Path fileInPackageRelativePath(String pkg, String file) {
      pkg = packageToPath(pkg);
      return Paths.get(pkg, file).normalize();
   }

   public static String packageToPath(String pkg) {
      return pkg.replace('.', File.separatorChar);
   }

   public static Path packageAbsolutePath(String root, String pkg) {
      pkg = packageToPath(pkg);
      return Paths.get(root, pkg).normalize();
   }

   public static Path packageAbsolutePath(Path root, String pkg) {
      return packageAbsolutePath(root.toString(), pkg);
   }

   public static Path classLoaderRoot(Class<?> c) {
      try {
         return Paths.get(c.getProtectionDomain().getCodeSource().getLocation().toURI());
      } catch (URISyntaxException var2) {
         throw new AssertionError("Bug in JavaParser, please report.", var2);
      }
   }

   public static Path mavenModuleRoot(Class<?> c) {
      return classLoaderRoot(c).resolve(Paths.get("..", "..")).normalize();
   }

   public static Path subtractPaths(Path full, Path difference) {
      while(true) {
         if (difference != null) {
            if (difference.getFileName().equals(full.getFileName())) {
               difference = difference.getParent();
               full = full.getParent();
               continue;
            }

            throw new RuntimeException(f("'%s' could not be subtracted from '%s'", difference, full));
         }

         return full;
      }
   }
}

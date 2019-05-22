package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.Os;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.StringUtils;

public class FileUtils {
   private static final int ONE_KB = 1024;
   private static final int ONE_MB = 1048576;
   private static final int ONE_GB = 1073741824;
   private static final long FILE_COPY_BUFFER_SIZE = 31457280L;
   private static final String FS = System.getProperty("file.separator");
   private static final String[] INVALID_CHARACTERS_FOR_WINDOWS_FILE_NAME = new String[]{":", "*", "?", "\"", "<", ">", "|"};

   protected FileUtils() {
   }

   @Nonnull
   public static String[] getDefaultExcludes() {
      return DirectoryScanner.DEFAULTEXCLUDES;
   }

   @Nonnull
   public static List<String> getDefaultExcludesAsList() {
      return Arrays.asList(getDefaultExcludes());
   }

   @Nonnull
   public static String getDefaultExcludesAsString() {
      return StringUtils.join((Object[])DirectoryScanner.DEFAULTEXCLUDES, ",");
   }

   @Nonnull
   public static String dirname(@Nonnull String filename) {
      int i = filename.lastIndexOf(File.separator);
      return i >= 0 ? filename.substring(0, i) : "";
   }

   @Nonnull
   public static String filename(@Nonnull String filename) {
      int i = filename.lastIndexOf(File.separator);
      return i >= 0 ? filename.substring(i + 1) : filename;
   }

   @Nonnull
   public static String extension(@Nonnull String filename) {
      int lastSep = filename.lastIndexOf(File.separatorChar);
      int lastDot;
      if (lastSep < 0) {
         lastDot = filename.lastIndexOf(46);
      } else {
         lastDot = filename.substring(lastSep + 1).lastIndexOf(46);
         if (lastDot >= 0) {
            lastDot += lastSep + 1;
         }
      }

      return lastDot >= 0 && lastDot > lastSep ? filename.substring(lastDot + 1) : "";
   }

   public static boolean fileExists(@Nonnull String fileName) {
      File file = new File(fileName);
      return file.exists();
   }

   @Nonnull
   public static String fileRead(@Nonnull String file) throws IOException {
      return fileRead((String)file, (String)null);
   }

   @Nonnull
   private static String fileRead(@Nonnull String file, @Nullable String encoding) throws IOException {
      return fileRead(new File(file), encoding);
   }

   @Nonnull
   public static String fileRead(@Nonnull File file) throws IOException {
      return fileRead((File)file, (String)null);
   }

   @Nonnull
   public static String fileRead(@Nonnull File file, @Nullable String encoding) throws IOException {
      StringBuilder buf = new StringBuilder();
      InputStreamReader reader = null;

      try {
         if (encoding != null) {
            reader = new InputStreamReader(new FileInputStream(file), encoding);
         } else {
            reader = new InputStreamReader(new FileInputStream(file));
         }

         char[] b = new char[512];

         int count;
         while((count = reader.read(b)) > 0) {
            buf.append(b, 0, count);
         }
      } finally {
         IOUtil.close((Reader)reader);
      }

      return buf.toString();
   }

   @Nonnull
   public static String[] fileReadArray(@Nonnull File file) throws IOException {
      List<String> files = loadFile(file);
      return (String[])files.toArray(new String[files.size()]);
   }

   public static void fileAppend(@Nonnull String fileName, @Nonnull String data) throws IOException {
      fileAppend(fileName, (String)null, data);
   }

   public static void fileAppend(@Nonnull String fileName, @Nullable String encoding, @Nonnull String data) throws IOException {
      FileOutputStream out = null;

      try {
         out = new FileOutputStream(fileName, true);
         if (encoding != null) {
            out.write(data.getBytes(encoding));
         } else {
            out.write(data.getBytes());
         }
      } finally {
         IOUtil.close((OutputStream)out);
      }

   }

   public static void fileWrite(@Nonnull String fileName, @Nonnull String data) throws IOException {
      fileWrite((String)fileName, (String)null, data);
   }

   public static void fileWrite(@Nonnull String fileName, @Nullable String encoding, @Nonnull String data) throws IOException {
      File file = new File(fileName);
      fileWrite(file, encoding, data);
   }

   public static void fileWrite(@Nonnull File file, @Nullable String encoding, @Nonnull String data) throws IOException {
      OutputStreamWriter writer = null;

      try {
         OutputStream out = new FileOutputStream(file);
         if (encoding != null) {
            writer = new OutputStreamWriter(out, encoding);
         } else {
            writer = new OutputStreamWriter(out);
         }

         writer.write(data);
      } finally {
         IOUtil.close((Writer)writer);
      }

   }

   public static void fileWriteArray(@Nonnull File file, @Nullable String... data) throws IOException {
      fileWriteArray(file, (String)null, data);
   }

   public static void fileWriteArray(@Nonnull File file, @Nullable String encoding, @Nullable String... data) throws IOException {
      OutputStreamWriter writer = null;

      try {
         OutputStream out = new FileOutputStream(file);
         if (encoding != null) {
            writer = new OutputStreamWriter(out, encoding);
         } else {
            writer = new OutputStreamWriter(out);
         }

         for(int i = 0; data != null && i < data.length; ++i) {
            writer.write(data[i]);
            if (i < data.length) {
               writer.write("\n");
            }
         }
      } finally {
         IOUtil.close((Writer)writer);
      }

   }

   public static void fileDelete(@Nonnull String fileName) {
      File file = new File(fileName);
      file.delete();
   }

   public static String[] getFilesFromExtension(@Nonnull String directory, @Nonnull String... extensions) {
      List<String> files = new ArrayList();
      File currentDir = new File(directory);
      String[] unknownFiles = currentDir.list();
      if (unknownFiles == null) {
         return new String[0];
      } else {
         String[] foundFiles = unknownFiles;
         int len$ = unknownFiles.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String unknownFile = foundFiles[i$];
            String currentFileName = directory + System.getProperty("file.separator") + unknownFile;
            File currentFile = new File(currentFileName);
            if (currentFile.isDirectory()) {
               if (!currentFile.getName().equals("CVS")) {
                  String[] fetchFiles = getFilesFromExtension(currentFileName, extensions);
                  files = blendFilesToList((List)files, fetchFiles);
               }
            } else {
               String add = currentFile.getAbsolutePath();
               if (isValidFile(add, extensions)) {
                  ((List)files).add(add);
               }
            }
         }

         foundFiles = new String[((List)files).size()];
         ((List)files).toArray(foundFiles);
         return foundFiles;
      }
   }

   @Nonnull
   private static List<String> blendFilesToList(@Nonnull List<String> v, @Nonnull String... files) {
      Collections.addAll(v, files);
      return v;
   }

   private static boolean isValidFile(@Nonnull String file, @Nonnull String... extensions) {
      String extension = extension(file);
      String[] arr$ = extensions;
      int len$ = extensions.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String extension1 = arr$[i$];
         if (extension1.equals(extension)) {
            return true;
         }
      }

      return false;
   }

   public static void mkdir(@Nonnull String dir) {
      File file = new File(dir);
      if (Os.isFamily("windows") && !isValidWindowsFileName(file)) {
         throw new IllegalArgumentException("The file (" + dir + ") cannot contain any of the following characters: \n" + StringUtils.join((Object[])INVALID_CHARACTERS_FOR_WINDOWS_FILE_NAME, " "));
      } else {
         if (!file.exists()) {
            file.mkdirs();
         }

      }
   }

   public static boolean contentEquals(@Nonnull File file1, @Nonnull File file2) throws IOException {
      boolean file1Exists = file1.exists();
      if (file1Exists != file2.exists()) {
         return false;
      } else if (!file1Exists) {
         return true;
      } else if (!file1.isDirectory() && !file2.isDirectory()) {
         InputStream input1 = null;
         FileInputStream input2 = null;

         boolean var5;
         try {
            input1 = new FileInputStream(file1);
            input2 = new FileInputStream(file2);
            var5 = IOUtil.contentEquals(input1, input2);
         } finally {
            IOUtil.close((InputStream)input1);
            IOUtil.close((InputStream)input2);
         }

         return var5;
      } else {
         return false;
      }
   }

   @Nullable
   public static File toFile(@Nullable URL url) {
      if (url != null && url.getProtocol().equalsIgnoreCase("file")) {
         String filename = url.getFile().replace('/', File.separatorChar);
         int pos = -1;

         while((pos = filename.indexOf(37, pos + 1)) >= 0) {
            if (pos + 2 < filename.length()) {
               String hexStr = filename.substring(pos + 1, pos + 3);
               char ch = (char)Integer.parseInt(hexStr, 16);
               filename = filename.substring(0, pos) + ch + filename.substring(pos + 3);
            }
         }

         return new File(filename);
      } else {
         return null;
      }
   }

   @Nonnull
   public static URL[] toURLs(@Nonnull File... files) throws IOException {
      URL[] urls = new URL[files.length];

      for(int i = 0; i < urls.length; ++i) {
         urls[i] = files[i].toURL();
      }

      return urls;
   }

   @Nonnull
   public static String removeExtension(@Nonnull String filename) {
      String ext = extension(filename);
      if ("".equals(ext)) {
         return filename;
      } else {
         int index = filename.lastIndexOf(ext) - 1;
         return filename.substring(0, index);
      }
   }

   @Nonnull
   public static String getExtension(@Nonnull String filename) {
      return extension(filename);
   }

   public static void copyFileToDirectory(@Nonnull File source, @Nonnull File destinationDirectory) throws IOException {
      if (destinationDirectory.exists() && !destinationDirectory.isDirectory()) {
         throw new IllegalArgumentException("Destination is not a directory");
      } else {
         copyFile(source, new File(destinationDirectory, source.getName()));
      }
   }

   private static void copyFileToDirectoryIfModified(@Nonnull File source, @Nonnull File destinationDirectory) throws IOException {
      if (destinationDirectory.exists() && !destinationDirectory.isDirectory()) {
         throw new IllegalArgumentException("Destination is not a directory");
      } else {
         copyFileIfModified(source, new File(destinationDirectory, source.getName()));
      }
   }

   public static void copyFile(@Nonnull File source, @Nonnull File destination) throws IOException {
      String message;
      if (!source.exists()) {
         message = "File " + source + " does not exist";
         throw new IOException(message);
      } else if (!source.getCanonicalPath().equals(destination.getCanonicalPath())) {
         mkdirsFor(destination);
         doCopyFile(source, destination);
         if (source.length() != destination.length()) {
            message = "Failed to copy full contents from " + source + " to " + destination;
            throw new IOException(message);
         }
      }
   }

   private static void mkdirsFor(@Nonnull File destination) {
      if (destination.getParentFile() != null && !destination.getParentFile().exists()) {
         destination.getParentFile().mkdirs();
      }

   }

   private static void doCopyFile(@Nonnull File source, @Nonnull File destination) throws IOException {
      FileInputStream fis = null;
      FileOutputStream fos = null;
      FileChannel input = null;
      FileChannel output = null;

      try {
         fis = new FileInputStream(source);
         fos = new FileOutputStream(destination);
         input = fis.getChannel();
         output = fos.getChannel();
         long size = input.size();

         long count;
         for(long pos = 0L; pos < size; pos += output.transferFrom(input, pos, count)) {
            count = size - pos > 31457280L ? 31457280L : size - pos;
         }
      } finally {
         IOUtil.close((Channel)output);
         IOUtil.close((OutputStream)fos);
         IOUtil.close((Channel)input);
         IOUtil.close((InputStream)fis);
      }

   }

   private static boolean copyFileIfModified(@Nonnull File source, @Nonnull File destination) throws IOException {
      if (destination.lastModified() < source.lastModified()) {
         copyFile(source, destination);
         return true;
      } else {
         return false;
      }
   }

   public static void copyURLToFile(@Nonnull URL source, @Nonnull File destination) throws IOException {
      copyStreamToFile(source.openStream(), destination);
   }

   private static void copyStreamToFile(@Nonnull @WillClose InputStream source, @Nonnull File destination) throws IOException {
      FileOutputStream output = null;

      try {
         if (destination.getParentFile() != null && !destination.getParentFile().exists()) {
            destination.getParentFile().mkdirs();
         }

         if (destination.exists() && !destination.canWrite()) {
            String message = "Unable to open file " + destination + " for writing.";
            throw new IOException(message);
         }

         output = new FileOutputStream(destination);
         IOUtil.copy((InputStream)source, (OutputStream)output);
      } finally {
         IOUtil.close(source);
         IOUtil.close((OutputStream)output);
      }

   }

   public static String normalize(String path) {
      String normalized = path;

      while(true) {
         int index = normalized.indexOf("//");
         if (index < 0) {
            while(true) {
               index = normalized.indexOf("/./");
               if (index < 0) {
                  while(true) {
                     index = normalized.indexOf("/../");
                     if (index < 0) {
                        return normalized;
                     }

                     if (index == 0) {
                        return null;
                     }

                     int index2 = normalized.lastIndexOf(47, index - 1);
                     normalized = normalized.substring(0, index2) + normalized.substring(index + 3);
                  }
               }

               normalized = normalized.substring(0, index) + normalized.substring(index + 2);
            }
         }

         normalized = normalized.substring(0, index) + normalized.substring(index + 1);
      }
   }

   public static File resolveFile(File baseFile, String filename) {
      String filenm = filename;
      if ('/' != File.separatorChar) {
         filenm = filename.replace('/', File.separatorChar);
      }

      if ('\\' != File.separatorChar) {
         filenm = filename.replace('\\', File.separatorChar);
      }

      if (filenm.startsWith(File.separator) || Os.isFamily("windows") && filenm.indexOf(":") > 0) {
         File file = new File(filenm);

         try {
            file = file.getCanonicalFile();
         } catch (IOException var9) {
         }

         return file;
      } else {
         char[] chars = filename.toCharArray();
         StringBuilder sb = new StringBuilder();
         int start = 0;
         if ('\\' == File.separatorChar) {
            sb.append(filenm.charAt(0));
            ++start;
         }

         for(int i = start; i < chars.length; ++i) {
            boolean doubleSeparator = File.separatorChar == chars[i] && File.separatorChar == chars[i - 1];
            if (!doubleSeparator) {
               sb.append(chars[i]);
            }
         }

         filenm = sb.toString();
         File file = (new File(baseFile, filenm)).getAbsoluteFile();

         try {
            file = file.getCanonicalFile();
         } catch (IOException var8) {
         }

         return file;
      }
   }

   public static void forceDelete(String file) throws IOException {
      forceDelete(new File(file));
   }

   public static void forceDelete(@Nonnull File file) throws IOException {
      if (file.isDirectory()) {
         deleteDirectory(file);
      } else {
         boolean filePresent = file.getCanonicalFile().exists();
         if (!deleteFile(file) && filePresent) {
            String message = "File " + file + " unable to be deleted.";
            throw new IOException(message);
         }
      }

   }

   private static boolean deleteFile(@Nonnull File file) throws IOException {
      if (file.isDirectory()) {
         throw new IOException("File " + file + " isn't a file.");
      } else if (!file.delete()) {
         if (Os.isFamily("windows")) {
            file = file.getCanonicalFile();
            System.gc();
         }

         try {
            Thread.sleep(10L);
            return file.delete();
         } catch (InterruptedException var2) {
            return file.delete();
         }
      } else {
         return true;
      }
   }

   public static void forceMkdir(@Nonnull File file) throws IOException {
      if (Os.isFamily("windows") && !isValidWindowsFileName(file)) {
         throw new IllegalArgumentException("The file (" + file.getAbsolutePath() + ") cannot contain any of the following characters: \n" + StringUtils.join((Object[])INVALID_CHARACTERS_FOR_WINDOWS_FILE_NAME, " "));
      } else {
         String message;
         if (file.exists()) {
            if (file.isFile()) {
               message = "File " + file + " exists and is " + "not a directory. Unable to create directory.";
               throw new IOException(message);
            }
         } else if (!file.mkdirs()) {
            message = "Unable to create directory " + file;
            throw new IOException(message);
         }

      }
   }

   public static void deleteDirectory(@Nonnull String directory) throws IOException {
      deleteDirectory(new File(directory));
   }

   public static void deleteDirectory(@Nonnull File directory) throws IOException {
      if (directory.exists()) {
         if (!directory.delete()) {
            cleanDirectory(directory);
            if (!directory.delete()) {
               String message = "Directory " + directory + " unable to be deleted.";
               throw new IOException(message);
            }
         }
      }
   }

   public static void cleanDirectory(@Nonnull File directory) throws IOException {
      String message;
      if (!directory.exists()) {
         message = directory + " does not exist";
         throw new IllegalArgumentException(message);
      } else if (!directory.isDirectory()) {
         message = directory + " is not a directory";
         throw new IllegalArgumentException(message);
      } else {
         IOException exception = null;
         File[] files = directory.listFiles();
         if (files != null) {
            File[] arr$ = files;
            int len$ = files.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               File file = arr$[i$];

               try {
                  forceDelete(file);
               } catch (IOException var8) {
                  exception = var8;
               }
            }

            if (null != exception) {
               throw exception;
            }
         }
      }
   }

   public static long sizeOfDirectory(@Nonnull String directory) {
      return sizeOfDirectory(new File(directory));
   }

   public static long sizeOfDirectory(@Nonnull File directory) {
      String message;
      if (!directory.exists()) {
         message = directory + " does not exist";
         throw new IllegalArgumentException(message);
      } else if (!directory.isDirectory()) {
         message = directory + " is not a directory";
         throw new IllegalArgumentException(message);
      } else {
         long size = 0L;
         File[] files = directory.listFiles();
         if (files == null) {
            throw new IllegalArgumentException("Problems reading directory");
         } else {
            File[] arr$ = files;
            int len$ = files.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               File file = arr$[i$];
               if (file.isDirectory()) {
                  size += sizeOfDirectory(file);
               } else {
                  size += file.length();
               }
            }

            return size;
         }
      }
   }

   @Nonnull
   public static List<File> getFiles(@Nonnull File directory, @Nullable String includes, @Nullable String excludes) throws IOException {
      return getFiles(directory, includes, excludes, true);
   }

   @Nonnull
   public static List<File> getFiles(@Nonnull File directory, @Nullable String includes, @Nullable String excludes, boolean includeBasedir) throws IOException {
      List<String> fileNames = getFileNames(directory, includes, excludes, includeBasedir);
      List<File> files = new ArrayList();
      Iterator i$ = fileNames.iterator();

      while(i$.hasNext()) {
         String filename = (String)i$.next();
         files.add(new File(filename));
      }

      return files;
   }

   @Nonnull
   public static List<String> getFileNames(@Nonnull File directory, @Nullable String includes, @Nullable String excludes, boolean includeBasedir) throws IOException {
      return getFileNames(directory, includes, excludes, includeBasedir, true);
   }

   @Nonnull
   private static List<String> getFileNames(@Nonnull File directory, @Nullable String includes, @Nullable String excludes, boolean includeBasedir, boolean isCaseSensitive) throws IOException {
      return getFileAndDirectoryNames(directory, includes, excludes, includeBasedir, isCaseSensitive, true, false);
   }

   @Nonnull
   public static List<String> getDirectoryNames(@Nonnull File directory, @Nullable String includes, @Nullable String excludes, boolean includeBasedir) throws IOException {
      return getDirectoryNames(directory, includes, excludes, includeBasedir, true);
   }

   @Nonnull
   public static List<String> getDirectoryNames(@Nonnull File directory, @Nullable String includes, @Nullable String excludes, boolean includeBasedir, boolean isCaseSensitive) throws IOException {
      return getFileAndDirectoryNames(directory, includes, excludes, includeBasedir, isCaseSensitive, false, true);
   }

   @Nonnull
   public static List<String> getFileAndDirectoryNames(File directory, @Nullable String includes, @Nullable String excludes, boolean includeBasedir, boolean isCaseSensitive, boolean getFiles, boolean getDirectories) {
      DirectoryScanner scanner = new DirectoryScanner();
      scanner.setBasedir(directory);
      if (includes != null) {
         scanner.setIncludes(StringUtils.split(includes, ","));
      }

      if (excludes != null) {
         scanner.setExcludes(StringUtils.split(excludes, ","));
      }

      scanner.setCaseSensitive(isCaseSensitive);
      scanner.scan();
      List<String> list = new ArrayList();
      String[] directories;
      String[] arr$;
      int len$;
      int i$;
      String directory1;
      if (getFiles) {
         directories = scanner.getIncludedFiles();
         arr$ = directories;
         len$ = directories.length;

         for(i$ = 0; i$ < len$; ++i$) {
            directory1 = arr$[i$];
            if (includeBasedir) {
               list.add(directory + FS + directory1);
            } else {
               list.add(directory1);
            }
         }
      }

      if (getDirectories) {
         directories = scanner.getIncludedDirectories();
         arr$ = directories;
         len$ = directories.length;

         for(i$ = 0; i$ < len$; ++i$) {
            directory1 = arr$[i$];
            if (includeBasedir) {
               list.add(directory + FS + directory1);
            } else {
               list.add(directory1);
            }
         }
      }

      return list;
   }

   public static void copyDirectory(File sourceDirectory, File destinationDirectory) throws IOException {
      copyDirectory(sourceDirectory, destinationDirectory, "**", (String)null);
   }

   public static void copyDirectory(@Nonnull File sourceDirectory, @Nonnull File destinationDirectory, @Nullable String includes, @Nullable String excludes) throws IOException {
      if (sourceDirectory.exists()) {
         List<File> files = getFiles(sourceDirectory, includes, excludes);
         Iterator i$ = files.iterator();

         while(i$.hasNext()) {
            File file = (File)i$.next();
            copyFileToDirectory(file, destinationDirectory);
         }

      }
   }

   public static void copyDirectoryStructure(@Nonnull File sourceDirectory, @Nonnull File destinationDirectory) throws IOException {
      copyDirectoryStructure(sourceDirectory, destinationDirectory, destinationDirectory, false);
   }

   private static void copyDirectoryStructure(@Nonnull File sourceDirectory, @Nonnull File destinationDirectory, File rootDestinationDirectory, boolean onlyModifiedFiles) throws IOException {
      if (sourceDirectory == null) {
         throw new IOException("source directory can't be null.");
      } else if (destinationDirectory == null) {
         throw new IOException("destination directory can't be null.");
      } else if (sourceDirectory.equals(destinationDirectory)) {
         throw new IOException("source and destination are the same directory.");
      } else if (!sourceDirectory.exists()) {
         throw new IOException("Source directory doesn't exists (" + sourceDirectory.getAbsolutePath() + ").");
      } else {
         File[] files = sourceDirectory.listFiles();
         if (files != null) {
            String sourcePath = sourceDirectory.getAbsolutePath();
            File[] arr$ = files;
            int len$ = files.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               File file = arr$[i$];
               if (!file.equals(rootDestinationDirectory)) {
                  String dest = file.getAbsolutePath();
                  dest = dest.substring(sourcePath.length() + 1);
                  File destination = new File(destinationDirectory, dest);
                  if (file.isFile()) {
                     destination = destination.getParentFile();
                     if (onlyModifiedFiles) {
                        copyFileToDirectoryIfModified(file, destination);
                     } else {
                        copyFileToDirectory(file, destination);
                     }
                  } else {
                     if (!file.isDirectory()) {
                        throw new IOException("Unknown file type: " + file.getAbsolutePath());
                     }

                     if (!destination.exists() && !destination.mkdirs()) {
                        throw new IOException("Could not create destination directory '" + destination.getAbsolutePath() + "'.");
                     }

                     copyDirectoryStructure(file, destination, rootDestinationDirectory, onlyModifiedFiles);
                  }
               }
            }

         }
      }
   }

   public static void rename(@Nonnull File from, @Nonnull File to) throws IOException {
      if (to.exists() && !to.delete()) {
         throw new IOException("Failed to delete " + to + " while trying to rename " + from);
      } else {
         File parent = to.getParentFile();
         if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("Failed to create directory " + parent + " while trying to rename " + from);
         } else {
            if (!from.renameTo(to)) {
               copyFile(from, to);
               if (!from.delete()) {
                  throw new IOException("Failed to delete " + from + " while trying to rename it.");
               }
            }

         }
      }
   }

   public static File createTempFile(@Nonnull String prefix, @Nonnull String suffix, @Nullable File parentDir) {
      String parent = System.getProperty("java.io.tmpdir");
      if (parentDir != null) {
         parent = parentDir.getPath();
      }

      DecimalFormat fmt = new DecimalFormat("#####");
      SecureRandom secureRandom = new SecureRandom();
      long secureInitializer = secureRandom.nextLong();
      Random rand = new Random(secureInitializer + Runtime.getRuntime().freeMemory());

      File result;
      do {
         result = new File(parent, prefix + fmt.format((long)positiveRandom(rand)) + suffix);
      } while(result.exists());

      return result;
   }

   private static int positiveRandom(Random rand) {
      int a;
      for(a = rand.nextInt(); a == Integer.MIN_VALUE; a = rand.nextInt()) {
      }

      return Math.abs(a);
   }

   public static void copyFile(@Nonnull File from, @Nonnull File to, @Nullable String encoding, @Nullable FileUtils.FilterWrapper... wrappers) throws IOException {
      copyFile(from, to, encoding, wrappers, false);
   }

   public static void copyFile(@Nonnull File from, @Nonnull File to, @Nullable String encoding, @Nullable FileUtils.FilterWrapper[] wrappers, boolean overwrite) throws IOException {
      if (wrappers != null && wrappers.length > 0) {
         Reader fileReader = null;
         Object fileWriter = null;

         try {
            if (encoding != null && encoding.length() >= 1) {
               FileInputStream instream = new FileInputStream(from);
               FileOutputStream outstream = new FileOutputStream(to);
               fileReader = new BufferedReader(new InputStreamReader(instream, encoding));
               fileWriter = new OutputStreamWriter(outstream, encoding);
            } else {
               fileReader = new BufferedReader(new FileReader(from));
               fileWriter = new FileWriter(to);
            }

            Reader reader = fileReader;
            FileUtils.FilterWrapper[] arr$ = wrappers;
            int len$ = wrappers.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               FileUtils.FilterWrapper wrapper = arr$[i$];
               reader = wrapper.getReader((Reader)reader);
            }

            IOUtil.copy((Reader)reader, (Writer)fileWriter);
         } finally {
            IOUtil.close((Reader)fileReader);
            IOUtil.close((Writer)fileWriter);
         }
      } else if (to.lastModified() < from.lastModified() || overwrite) {
         copyFile(from, to);
      }

   }

   @Nonnull
   public static List<String> loadFile(@Nonnull File file) throws IOException {
      List<String> lines = new ArrayList();
      if (file.exists()) {
         FileReader fileReader = new FileReader(file);

         try {
            BufferedReader reader = new BufferedReader(fileReader);

            for(String line = reader.readLine(); line != null; line = reader.readLine()) {
               line = line.trim();
               if (!line.startsWith("#") && line.length() != 0) {
                  lines.add(line);
               }
            }

            reader.close();
         } finally {
            fileReader.close();
         }
      }

      return lines;
   }

   private static boolean isValidWindowsFileName(@Nonnull File f) {
      if (Os.isFamily("windows")) {
         if (StringUtils.indexOfAny(f.getName(), INVALID_CHARACTERS_FOR_WINDOWS_FILE_NAME) != -1) {
            return false;
         }

         if (f.getParentFile() != null) {
            return isValidWindowsFileName(f.getParentFile());
         }
      }

      return true;
   }

   public abstract static class FilterWrapper {
      public abstract Reader getReader(Reader var1);
   }
}

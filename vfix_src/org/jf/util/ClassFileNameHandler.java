package org.jf.util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ClassFileNameHandler {
   private static final int MAX_FILENAME_LENGTH = 255;
   private static final int NUMERIC_SUFFIX_RESERVE = 6;
   private final int NO_VALUE = -1;
   private final int CASE_INSENSITIVE = 0;
   private final int CASE_SENSITIVE = 1;
   private int forcedCaseSensitivity = -1;
   private ClassFileNameHandler.DirectoryEntry top;
   private String fileExtension;
   private boolean modifyWindowsReservedFilenames;
   private static Pattern reservedFileNameRegex = Pattern.compile("^(CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9])(\\..*)?$", 2);

   public ClassFileNameHandler(File path, String fileExtension) {
      this.top = new ClassFileNameHandler.DirectoryEntry(path);
      this.fileExtension = fileExtension;
      this.modifyWindowsReservedFilenames = isWindows();
   }

   public ClassFileNameHandler(File path, String fileExtension, boolean caseSensitive, boolean modifyWindowsReservedFilenames) {
      this.top = new ClassFileNameHandler.DirectoryEntry(path);
      this.fileExtension = fileExtension;
      this.forcedCaseSensitivity = caseSensitive ? 1 : 0;
      this.modifyWindowsReservedFilenames = modifyWindowsReservedFilenames;
   }

   private int getMaxFilenameLength() {
      return 249;
   }

   public File getUniqueFilenameForClass(String className) {
      if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
         int packageElementCount = 1;

         for(int i = 1; i < className.length() - 1; ++i) {
            if (className.charAt(i) == '/') {
               ++packageElementCount;
            }
         }

         String[] packageElements = new String[packageElementCount];
         int elementIndex = 0;
         int elementStart = 1;

         for(int i = 1; i < className.length() - 1; ++i) {
            if (className.charAt(i) == '/') {
               if (i - elementStart == 0) {
                  throw new RuntimeException("Not a valid dalvik class name");
               }

               packageElements[elementIndex++] = className.substring(elementStart, i);
               ++i;
               elementStart = i;
            }
         }

         if (elementStart >= className.length() - 1) {
            throw new RuntimeException("Not a valid dalvik class name");
         } else {
            packageElements[elementIndex] = className.substring(elementStart, className.length() - 1);
            return this.addUniqueChild(this.top, packageElements, 0);
         }
      } else {
         throw new RuntimeException("Not a valid dalvik class name");
      }
   }

   @Nonnull
   private File addUniqueChild(@Nonnull ClassFileNameHandler.DirectoryEntry parent, @Nonnull String[] packageElements, int packageElementIndex) {
      if (packageElementIndex == packageElements.length - 1) {
         ClassFileNameHandler.FileEntry fileEntry = new ClassFileNameHandler.FileEntry(parent, packageElements[packageElementIndex] + this.fileExtension);
         parent.addChild(fileEntry);
         String physicalName = fileEntry.getPhysicalName();

         assert physicalName != null;

         return new File(parent.file, physicalName);
      } else {
         ClassFileNameHandler.DirectoryEntry directoryEntry = new ClassFileNameHandler.DirectoryEntry(parent, packageElements[packageElementIndex]);
         directoryEntry = (ClassFileNameHandler.DirectoryEntry)parent.addChild(directoryEntry);
         return this.addUniqueChild(directoryEntry, packageElements, packageElementIndex + 1);
      }
   }

   private static int utf8Length(String str) {
      int utf8Length = 0;

      int c;
      for(int i = 0; i < str.length(); i += Character.charCount(c)) {
         c = str.codePointAt(i);
         utf8Length += utf8Length(c);
      }

      return utf8Length;
   }

   private static int utf8Length(int codePoint) {
      if (codePoint < 128) {
         return 1;
      } else if (codePoint < 2048) {
         return 2;
      } else {
         return codePoint < 65536 ? 3 : 4;
      }
   }

   @Nonnull
   static String shortenPathComponent(@Nonnull String pathComponent, int bytesToRemove) {
      ++bytesToRemove;

      int[] codePoints;
      try {
         IntBuffer intBuffer = ByteBuffer.wrap(pathComponent.getBytes("UTF-32BE")).asIntBuffer();
         codePoints = new int[intBuffer.limit()];
         intBuffer.get(codePoints);
      } catch (UnsupportedEncodingException var9) {
         throw new RuntimeException(var9);
      }

      int midPoint = codePoints.length / 2;
      int firstEnd = midPoint;
      int secondStart = midPoint + 1;
      int bytesRemoved = utf8Length(codePoints[midPoint]);
      if (codePoints.length % 2 == 0 && bytesRemoved < bytesToRemove) {
         bytesRemoved += utf8Length(codePoints[secondStart]);
         ++secondStart;
      }

      while(bytesRemoved < bytesToRemove && (firstEnd > 0 || secondStart < codePoints.length)) {
         if (firstEnd > 0) {
            --firstEnd;
            bytesRemoved += utf8Length(codePoints[firstEnd]);
         }

         if (bytesRemoved < bytesToRemove && secondStart < codePoints.length) {
            bytesRemoved += utf8Length(codePoints[secondStart]);
            ++secondStart;
         }
      }

      StringBuilder sb = new StringBuilder();

      int i;
      for(i = 0; i < firstEnd; ++i) {
         sb.appendCodePoint(codePoints[i]);
      }

      sb.append('#');

      for(i = secondStart; i < codePoints.length; ++i) {
         sb.appendCodePoint(codePoints[i]);
      }

      return sb.toString();
   }

   private static boolean isWindows() {
      return System.getProperty("os.name").startsWith("Windows");
   }

   private static boolean isReservedFileName(String className) {
      return reservedFileNameRegex.matcher(className).matches();
   }

   private static String addSuffixBeforeExtension(String pathElement, String suffix) {
      int extensionStart = pathElement.lastIndexOf(46);
      StringBuilder newName = new StringBuilder(pathElement.length() + suffix.length() + 1);
      if (extensionStart < 0) {
         newName.append(pathElement);
         newName.append(suffix);
      } else {
         newName.append(pathElement.subSequence(0, extensionStart));
         newName.append(suffix);
         newName.append(pathElement.subSequence(extensionStart, pathElement.length()));
      }

      return newName.toString();
   }

   private class FileEntry extends ClassFileNameHandler.FileSystemEntry {
      private FileEntry(@Nullable ClassFileNameHandler.DirectoryEntry parent, @Nonnull String logicalName) {
         super(parent, logicalName, null);
      }

      protected String makePhysicalName(int suffix) {
         return suffix > 0 ? ClassFileNameHandler.addSuffixBeforeExtension(this.getNormalizedName(true), '.' + Integer.toString(suffix)) : this.getNormalizedName(true);
      }

      // $FF: synthetic method
      FileEntry(ClassFileNameHandler.DirectoryEntry x1, String x2, Object x3) {
         this(x1, x2);
      }
   }

   private class DirectoryEntry extends ClassFileNameHandler.FileSystemEntry {
      @Nullable
      private File file = null;
      private int caseSensitivity;
      private final Multimap<String, ClassFileNameHandler.FileSystemEntry> children;

      public DirectoryEntry(@Nonnull File path) {
         super((ClassFileNameHandler.DirectoryEntry)null, path.getName(), null);
         this.caseSensitivity = ClassFileNameHandler.this.forcedCaseSensitivity;
         this.children = ArrayListMultimap.create();
         this.file = path;
         this.physicalName = this.file.getName();
      }

      public DirectoryEntry(@Nullable ClassFileNameHandler.DirectoryEntry parent, @Nonnull String logicalName) {
         super(parent, logicalName, null);
         this.caseSensitivity = ClassFileNameHandler.this.forcedCaseSensitivity;
         this.children = ArrayListMultimap.create();
      }

      public synchronized ClassFileNameHandler.FileSystemEntry addChild(ClassFileNameHandler.FileSystemEntry entry) {
         String normalizedChildName = entry.getNormalizedName(false);
         Collection<ClassFileNameHandler.FileSystemEntry> entries = this.children.get(normalizedChildName);
         if (entry instanceof ClassFileNameHandler.DirectoryEntry) {
            Iterator var4 = entries.iterator();

            while(var4.hasNext()) {
               ClassFileNameHandler.FileSystemEntry childEntry = (ClassFileNameHandler.FileSystemEntry)var4.next();
               if (childEntry.logicalName.equals(entry.logicalName)) {
                  return childEntry;
               }
            }
         }

         entry.setSuffix(entries.size());
         entries.add(entry);
         return entry;
      }

      protected String makePhysicalName(int suffix) {
         return suffix > 0 ? this.getNormalizedName(true) + "." + Integer.toString(suffix) : this.getNormalizedName(true);
      }

      public void setSuffix(int suffix) {
         super.setSuffix(suffix);
         String physicalName = this.getPhysicalName();
         if (this.parent != null && physicalName != null) {
            this.file = new File(this.parent.file, physicalName);
         }

      }

      protected boolean isCaseSensitive() {
         if (this.getPhysicalName() != null && this.file != null) {
            if (this.caseSensitivity != -1) {
               return this.caseSensitivity == 1;
            } else {
               File path = this.file;
               if (path.exists() && path.isFile() && !path.delete()) {
                  throw new ExceptionWithContext("Can't delete %s to make it into a directory", new Object[]{path.getAbsolutePath()});
               } else if (!path.exists() && !path.mkdirs()) {
                  throw new ExceptionWithContext("Couldn't create directory %s", new Object[]{path.getAbsolutePath()});
               } else {
                  try {
                     boolean result = this.testCaseSensitivity(path);
                     this.caseSensitivity = result ? 1 : 0;
                     return result;
                  } catch (IOException var3) {
                     return false;
                  }
               }
            }
         } else {
            throw new IllegalStateException("Must call setSuffix() first");
         }
      }

      private boolean testCaseSensitivity(File path) throws IOException {
         int num = 1;

         File f;
         File f2;
         do {
            do {
               f = new File(path, "test." + num);
               f2 = new File(path, "TEST." + num++);
            } while(f.exists());
         } while(f2.exists());

         boolean var7;
         try {
            try {
               FileWriter writer = new FileWriter(f);
               writer.write("test");
               writer.flush();
               writer.close();
            } catch (IOException var30) {
               try {
                  f.delete();
               } catch (Exception var29) {
               }

               throw var30;
            }

            boolean var34;
            if (f2.exists()) {
               var34 = false;
               return var34;
            }

            if (f2.createNewFile()) {
               var34 = true;
               return var34;
            }

            try {
               CharBuffer buf = CharBuffer.allocate(32);
               FileReader reader = new FileReader(f2);

               while(reader.read(buf) != -1 && buf.length() < 4) {
               }

               if (buf.length() == 4 && buf.toString().equals("test")) {
                  var7 = false;
                  return var7;
               }

               assert false;

               var7 = false;
            } catch (FileNotFoundException var31) {
               boolean var6 = true;
               return var6;
            }
         } finally {
            try {
               f.delete();
            } catch (Exception var28) {
            }

            try {
               f2.delete();
            } catch (Exception var27) {
            }

         }

         return var7;
      }
   }

   private abstract class FileSystemEntry {
      @Nullable
      public final ClassFileNameHandler.DirectoryEntry parent;
      @Nonnull
      public final String logicalName;
      @Nullable
      protected String physicalName;

      private FileSystemEntry(@Nullable ClassFileNameHandler.DirectoryEntry parent, @Nonnull String logicalName) {
         this.physicalName = null;
         this.parent = parent;
         this.logicalName = logicalName;
      }

      @Nonnull
      public String getNormalizedName(boolean preserveCase) {
         String elementName = this.logicalName;
         if (!preserveCase && this.parent != null && !this.parent.isCaseSensitive()) {
            elementName = elementName.toLowerCase();
         }

         if (ClassFileNameHandler.this.modifyWindowsReservedFilenames && ClassFileNameHandler.isReservedFileName(elementName)) {
            elementName = ClassFileNameHandler.addSuffixBeforeExtension(elementName, "#");
         }

         int utf8Length = ClassFileNameHandler.utf8Length(elementName);
         if (utf8Length > ClassFileNameHandler.this.getMaxFilenameLength()) {
            elementName = ClassFileNameHandler.shortenPathComponent(elementName, utf8Length - ClassFileNameHandler.this.getMaxFilenameLength());
         }

         return elementName;
      }

      @Nullable
      public String getPhysicalName() {
         return this.physicalName;
      }

      public void setSuffix(int suffix) {
         if (suffix >= 0 && suffix <= 99999) {
            if (this.physicalName != null) {
               throw new IllegalStateException("The suffix can only be set once");
            } else {
               this.physicalName = this.makePhysicalName(suffix);
            }
         } else {
            throw new IllegalArgumentException("suffix must be in [0, 100000)");
         }
      }

      protected abstract String makePhysicalName(int var1);

      // $FF: synthetic method
      FileSystemEntry(ClassFileNameHandler.DirectoryEntry x1, String x2, Object x3) {
         this(x1, x2);
      }
   }
}

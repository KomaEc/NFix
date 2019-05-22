package org.apache.tools.ant.taskdefs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.Restrict;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
import org.apache.tools.ant.types.resources.selectors.Type;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.StringUtils;

public class Checksum extends MatchingTask implements Condition {
   private File file = null;
   private File todir;
   private String algorithm = "MD5";
   private String provider = null;
   private String fileext;
   private String property;
   private Map allDigests = new HashMap();
   private Map relativeFilePaths = new HashMap();
   private String totalproperty;
   private boolean forceOverwrite;
   private String verifyProperty;
   private Checksum.FileUnion resources = null;
   private Hashtable includeFileMap = new Hashtable();
   private MessageDigest messageDigest;
   private boolean isCondition;
   private int readBufferSize = 8192;
   private MessageFormat format = Checksum.FormatElement.getDefault().getFormat();

   public void setFile(File file) {
      this.file = file;
   }

   public void setTodir(File todir) {
      this.todir = todir;
   }

   public void setAlgorithm(String algorithm) {
      this.algorithm = algorithm;
   }

   public void setProvider(String provider) {
      this.provider = provider;
   }

   public void setFileext(String fileext) {
      this.fileext = fileext;
   }

   public void setProperty(String property) {
      this.property = property;
   }

   public void setTotalproperty(String totalproperty) {
      this.totalproperty = totalproperty;
   }

   public void setVerifyproperty(String verifyProperty) {
      this.verifyProperty = verifyProperty;
   }

   public void setForceOverwrite(boolean forceOverwrite) {
      this.forceOverwrite = forceOverwrite;
   }

   public void setReadBufferSize(int size) {
      this.readBufferSize = size;
   }

   public void setFormat(Checksum.FormatElement e) {
      this.format = e.getFormat();
   }

   public void setPattern(String p) {
      this.format = new MessageFormat(p);
   }

   public void addFileset(FileSet set) {
      this.add(set);
   }

   public void add(ResourceCollection rc) {
      if (rc != null) {
         this.resources = this.resources == null ? new Checksum.FileUnion() : this.resources;
         this.resources.add(rc);
      }
   }

   public void execute() throws BuildException {
      this.isCondition = false;
      boolean value = this.validateAndExecute();
      if (this.verifyProperty != null) {
         this.getProject().setNewProperty(this.verifyProperty, value ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
      }

   }

   public boolean eval() throws BuildException {
      this.isCondition = true;
      return this.validateAndExecute();
   }

   private boolean validateAndExecute() throws BuildException {
      String savedFileExt = this.fileext;
      if (this.file == null && (this.resources == null || this.resources.size() == 0)) {
         throw new BuildException("Specify at least one source - a file or a resource collection.");
      } else if (this.resources != null && !this.resources.isFilesystemOnly()) {
         throw new BuildException("Can only calculate checksums for file-based resources.");
      } else if (this.file != null && this.file.exists() && this.file.isDirectory()) {
         throw new BuildException("Checksum cannot be generated for directories");
      } else if (this.file != null && this.totalproperty != null) {
         throw new BuildException("File and Totalproperty cannot co-exist.");
      } else if (this.property != null && this.fileext != null) {
         throw new BuildException("Property and FileExt cannot co-exist.");
      } else {
         if (this.property != null) {
            if (this.forceOverwrite) {
               throw new BuildException("ForceOverwrite cannot be used when Property is specified");
            }

            int ct = 0;
            if (this.resources != null) {
               ct += this.resources.size();
            }

            if (this.file != null) {
               ++ct;
            }

            if (ct > 1) {
               throw new BuildException("Multiple files cannot be used when Property is specified");
            }
         }

         if (this.verifyProperty != null) {
            this.isCondition = true;
         }

         if (this.verifyProperty != null && this.forceOverwrite) {
            throw new BuildException("VerifyProperty and ForceOverwrite cannot co-exist.");
         } else if (this.isCondition && this.forceOverwrite) {
            throw new BuildException("ForceOverwrite cannot be used when conditions are being used.");
         } else {
            this.messageDigest = null;
            if (this.provider != null) {
               try {
                  this.messageDigest = MessageDigest.getInstance(this.algorithm, this.provider);
               } catch (NoSuchAlgorithmException var11) {
                  throw new BuildException(var11, this.getLocation());
               } catch (NoSuchProviderException var12) {
                  throw new BuildException(var12, this.getLocation());
               }
            } else {
               try {
                  this.messageDigest = MessageDigest.getInstance(this.algorithm);
               } catch (NoSuchAlgorithmException var10) {
                  throw new BuildException(var10, this.getLocation());
               }
            }

            if (this.messageDigest == null) {
               throw new BuildException("Unable to create Message Digest", this.getLocation());
            } else {
               if (this.fileext == null) {
                  this.fileext = "." + this.algorithm;
               } else if (this.fileext.trim().length() == 0) {
                  throw new BuildException("File extension when specified must not be an empty string");
               }

               boolean var15;
               try {
                  File src;
                  if (this.resources != null) {
                     for(Iterator i = this.resources.iterator(); i.hasNext(); this.addToIncludeFileMap(src)) {
                        FileResource fr = (FileResource)i.next();
                        src = fr.getFile();
                        if (this.totalproperty != null || this.todir != null) {
                           this.relativeFilePaths.put(src, fr.getName().replace(File.separatorChar, '/'));
                        }
                     }
                  }

                  if (this.file != null) {
                     if (this.totalproperty != null || this.todir != null) {
                        this.relativeFilePaths.put(this.file, this.file.getName().replace(File.separatorChar, '/'));
                     }

                     this.addToIncludeFileMap(this.file);
                  }

                  var15 = this.generateChecksums();
               } finally {
                  this.fileext = savedFileExt;
                  this.includeFileMap.clear();
               }

               return var15;
            }
         }
      }
   }

   private void addToIncludeFileMap(File file) throws BuildException {
      if (!file.exists()) {
         String message = "Could not find file " + file.getAbsolutePath() + " to generate checksum for.";
         this.log(message);
         throw new BuildException(message, this.getLocation());
      } else {
         if (this.property == null) {
            File checksumFile = this.getChecksumFile(file);
            if (!this.forceOverwrite && !this.isCondition && file.lastModified() <= checksumFile.lastModified()) {
               this.log(file + " omitted as " + checksumFile + " is up to date.", 3);
               if (this.totalproperty != null) {
                  String checksum = this.readChecksum(checksumFile);
                  byte[] digest = decodeHex(checksum.toCharArray());
                  this.allDigests.put(file, digest);
               }
            } else {
               this.includeFileMap.put(file, checksumFile);
            }
         } else {
            this.includeFileMap.put(file, this.property);
         }

      }
   }

   private File getChecksumFile(File file) {
      File directory;
      if (this.todir != null) {
         String path = (String)this.relativeFilePaths.get(file);
         if (path == null) {
            throw new BuildException("Internal error: relativeFilePaths could not match file" + file + "\n" + "please file a bug report on this");
         }

         directory = (new File(this.todir, path)).getParentFile();
         directory.mkdirs();
      } else {
         directory = file.getParentFile();
      }

      File checksumFile = new File(directory, file.getName() + this.fileext);
      return checksumFile;
   }

   private boolean generateChecksums() throws BuildException {
      boolean checksumMatches = true;
      FileInputStream fis = null;
      FileOutputStream fos = null;
      byte[] buf = new byte[this.readBufferSize];

      try {
         Enumeration e = this.includeFileMap.keys();

         while(true) {
            while(true) {
               while(e.hasMoreElements()) {
                  this.messageDigest.reset();
                  File src = (File)e.nextElement();
                  if (!this.isCondition) {
                     this.log("Calculating " + this.algorithm + " checksum for " + src, 3);
                  }

                  fis = new FileInputStream(src);
                  DigestInputStream dis = new DigestInputStream(fis, this.messageDigest);

                  while(dis.read(buf, 0, this.readBufferSize) != -1) {
                  }

                  dis.close();
                  fis.close();
                  fis = null;
                  byte[] fileDigest = this.messageDigest.digest();
                  if (this.totalproperty != null) {
                     this.allDigests.put(src, fileDigest);
                  }

                  String checksum = this.createDigestString(fileDigest);
                  Object destination = this.includeFileMap.get(src);
                  if (destination instanceof String) {
                     String prop = (String)destination;
                     if (this.isCondition) {
                        checksumMatches = checksumMatches && checksum.equals(this.property);
                     } else {
                        this.getProject().setNewProperty(prop, checksum);
                     }
                  } else if (destination instanceof File) {
                     File existingFile;
                     if (this.isCondition) {
                        existingFile = (File)destination;
                        if (!existingFile.exists()) {
                           checksumMatches = false;
                        } else {
                           try {
                              String suppliedChecksum = this.readChecksum(existingFile);
                              checksumMatches = checksumMatches && checksum.equals(suppliedChecksum);
                           } catch (BuildException var17) {
                              checksumMatches = false;
                           }
                        }
                     } else {
                        existingFile = (File)destination;
                        fos = new FileOutputStream(existingFile);
                        fos.write(this.format.format(new Object[]{checksum, src.getName()}).getBytes());
                        fos.write(StringUtils.LINE_SEP.getBytes());
                        fos.close();
                        fos = null;
                     }
                  }
               }

               if (this.totalproperty != null) {
                  Set keys = this.allDigests.keySet();
                  Object[] keyArray = keys.toArray();
                  Arrays.sort(keyArray);
                  this.messageDigest.reset();

                  for(int i = 0; i < keyArray.length; ++i) {
                     File src = (File)keyArray[i];
                     byte[] digest = (byte[])((byte[])this.allDigests.get(src));
                     this.messageDigest.update(digest);
                     String fileName = (String)this.relativeFilePaths.get(src);
                     this.messageDigest.update(fileName.getBytes());
                  }

                  String totalChecksum = this.createDigestString(this.messageDigest.digest());
                  this.getProject().setNewProperty(this.totalproperty, totalChecksum);
               }

               return checksumMatches;
            }
         }
      } catch (Exception var18) {
         throw new BuildException(var18, this.getLocation());
      } finally {
         FileUtils.close((InputStream)fis);
         FileUtils.close((OutputStream)fos);
      }
   }

   private String createDigestString(byte[] fileDigest) {
      StringBuffer checksumSb = new StringBuffer();

      for(int i = 0; i < fileDigest.length; ++i) {
         String hexStr = Integer.toHexString(255 & fileDigest[i]);
         if (hexStr.length() < 2) {
            checksumSb.append("0");
         }

         checksumSb.append(hexStr);
      }

      return checksumSb.toString();
   }

   public static byte[] decodeHex(char[] data) throws BuildException {
      int l = data.length;
      if ((l & 1) != 0) {
         throw new BuildException("odd number of characters.");
      } else {
         byte[] out = new byte[l >> 1];
         int i = 0;

         for(int j = 0; j < l; ++i) {
            int f = Character.digit(data[j++], 16) << 4;
            f |= Character.digit(data[j++], 16);
            out[i] = (byte)(f & 255);
         }

         return out;
      }
   }

   private String readChecksum(File f) {
      BufferedReader diskChecksumReader = null;

      String var4;
      try {
         diskChecksumReader = new BufferedReader(new FileReader(f));
         Object[] result = this.format.parse(diskChecksumReader.readLine());
         if (result == null || result.length == 0 || result[0] == null) {
            throw new BuildException("failed to find a checksum");
         }

         var4 = (String)result[0];
      } catch (IOException var9) {
         throw new BuildException("Couldn't read checksum file " + f, var9);
      } catch (ParseException var10) {
         throw new BuildException("Couldn't read checksum file " + f, var10);
      } finally {
         FileUtils.close((Reader)diskChecksumReader);
      }

      return var4;
   }

   public static class FormatElement extends EnumeratedAttribute {
      private static HashMap formatMap = new HashMap();
      private static final String CHECKSUM = "CHECKSUM";
      private static final String MD5SUM = "MD5SUM";
      private static final String SVF = "SVF";

      public static Checksum.FormatElement getDefault() {
         Checksum.FormatElement e = new Checksum.FormatElement();
         e.setValue("CHECKSUM");
         return e;
      }

      public MessageFormat getFormat() {
         return (MessageFormat)formatMap.get(this.getValue());
      }

      public String[] getValues() {
         return new String[]{"CHECKSUM", "MD5SUM", "SVF"};
      }

      static {
         formatMap.put("CHECKSUM", new MessageFormat("{0}"));
         formatMap.put("MD5SUM", new MessageFormat("{0} *{1}"));
         formatMap.put("SVF", new MessageFormat("MD5 ({1}) = {0}"));
      }
   }

   private static class FileUnion extends Restrict {
      private Union u = new Union();

      FileUnion() {
         super.add((ResourceCollection)this.u);
         super.add((ResourceSelector)Type.FILE);
      }

      public void add(ResourceCollection rc) {
         this.u.add(rc);
      }
   }
}

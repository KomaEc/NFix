package org.apache.maven.plugin.surefire.booterclient;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;

public class ChecksumCalculator {
   private static final String HEX = "0123456789ABCDEF";
   private final List<Object> checksumItems = new ArrayList();

   private void appendObject(Object item) {
      this.checksumItems.add(item);
   }

   public void add(boolean value) {
      this.checksumItems.add(value ? Boolean.TRUE : Boolean.FALSE);
   }

   public void add(int value) {
      this.checksumItems.add(value);
   }

   public void add(double value) {
      this.checksumItems.add(value);
   }

   public void add(Map<?, ?> map) {
      if (map != null) {
         this.appendObject(map.toString());
      }

   }

   public void add(String string) {
      this.appendObject(string);
   }

   public void add(File workingDirectory) {
      this.appendObject(workingDirectory);
   }

   public void add(ArtifactRepository localRepository) {
      this.appendObject(localRepository);
   }

   public void add(List<?> items) {
      if (items != null) {
         Iterator i$ = items.iterator();

         while(i$.hasNext()) {
            Object item = i$.next();
            this.appendObject(item);
         }
      } else {
         this.appendObject((Object)null);
      }

   }

   public void add(Object[] items) {
      if (items != null) {
         Object[] arr$ = items;
         int len$ = items.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Object item = arr$[i$];
            this.appendObject(item);
         }
      } else {
         this.appendObject((Object)null);
      }

   }

   public void add(Artifact artifact) {
      this.appendObject(artifact != null ? artifact.getId() : null);
   }

   public void add(Boolean aBoolean) {
      this.appendObject(aBoolean);
   }

   private static String asHexString(byte[] bytes) {
      if (bytes == null) {
         return null;
      } else {
         StringBuilder result = new StringBuilder(2 * bytes.length);
         byte[] arr$ = bytes;
         int len$ = bytes.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            byte b = arr$[i$];
            result.append("0123456789ABCDEF".charAt((b & 240) >> 4)).append("0123456789ABCDEF".charAt(b & 15));
         }

         return result.toString();
      }
   }

   private String getConfig() {
      StringBuilder result = new StringBuilder();
      Iterator i$ = this.checksumItems.iterator();

      while(i$.hasNext()) {
         Object checksumItem = i$.next();
         result.append(checksumItem != null ? checksumItem.toString() : "null");
      }

      return result.toString();
   }

   public String getSha1() {
      try {
         MessageDigest md = MessageDigest.getInstance("SHA-1");
         String configValue = this.getConfig();
         md.update(configValue.getBytes("iso-8859-1"), 0, configValue.length());
         byte[] sha1hash = md.digest();
         return asHexString(sha1hash);
      } catch (NoSuchAlgorithmException var4) {
         throw new RuntimeException(var4);
      } catch (UnsupportedEncodingException var5) {
         throw new RuntimeException(var5);
      }
   }
}

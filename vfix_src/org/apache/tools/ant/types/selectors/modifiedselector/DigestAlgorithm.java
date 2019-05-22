package org.apache.tools.ant.types.selectors.modifiedselector;

import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import org.apache.tools.ant.BuildException;

public class DigestAlgorithm implements Algorithm {
   private String algorithm = "MD5";
   private String provider = null;
   private MessageDigest messageDigest = null;
   private int readBufferSize = 8192;

   public void setAlgorithm(String algorithm) {
      this.algorithm = algorithm;
   }

   public void setProvider(String provider) {
      this.provider = provider;
   }

   public void initMessageDigest() {
      if (this.messageDigest == null) {
         if (this.provider != null && !"".equals(this.provider) && !"null".equals(this.provider)) {
            try {
               this.messageDigest = MessageDigest.getInstance(this.algorithm, this.provider);
            } catch (NoSuchAlgorithmException var3) {
               throw new BuildException(var3);
            } catch (NoSuchProviderException var4) {
               throw new BuildException(var4);
            }
         } else {
            try {
               this.messageDigest = MessageDigest.getInstance(this.algorithm);
            } catch (NoSuchAlgorithmException var2) {
               throw new BuildException(var2);
            }
         }

      }
   }

   public boolean isValid() {
      return "SHA".equalsIgnoreCase(this.algorithm) || "MD5".equalsIgnoreCase(this.algorithm);
   }

   public String getValue(File file) {
      this.initMessageDigest();
      String checksum = null;

      try {
         if (!file.canRead()) {
            return null;
         } else {
            FileInputStream fis = null;
            byte[] buf = new byte[this.readBufferSize];

            try {
               this.messageDigest.reset();
               fis = new FileInputStream(file);
               DigestInputStream dis = new DigestInputStream(fis, this.messageDigest);

               while(dis.read(buf, 0, this.readBufferSize) != -1) {
               }

               dis.close();
               fis.close();
               fis = null;
               byte[] fileDigest = this.messageDigest.digest();
               StringBuffer checksumSb = new StringBuffer();

               for(int i = 0; i < fileDigest.length; ++i) {
                  String hexStr = Integer.toHexString(255 & fileDigest[i]);
                  if (hexStr.length() < 2) {
                     checksumSb.append("0");
                  }

                  checksumSb.append(hexStr);
               }

               checksum = checksumSb.toString();
               return checksum;
            } catch (Exception var10) {
               return null;
            }
         }
      } catch (Exception var11) {
         return null;
      }
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append("<DigestAlgorithm:");
      buf.append("algorithm=").append(this.algorithm);
      buf.append(";provider=").append(this.provider);
      buf.append(">");
      return buf.toString();
   }
}

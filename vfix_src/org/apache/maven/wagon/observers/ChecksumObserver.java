package org.apache.maven.wagon.observers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.maven.wagon.events.TransferEvent;
import org.apache.maven.wagon.events.TransferListener;

public class ChecksumObserver implements TransferListener {
   private MessageDigest digester;
   private String actualChecksum;

   public ChecksumObserver() throws NoSuchAlgorithmException {
      this("MD5");
   }

   public ChecksumObserver(String algorithm) throws NoSuchAlgorithmException {
      this.digester = null;
      this.digester = MessageDigest.getInstance(algorithm);
   }

   public void transferInitiated(TransferEvent transferEvent) {
   }

   public void transferStarted(TransferEvent transferEvent) {
      this.actualChecksum = null;
      this.digester.reset();
   }

   public void transferProgress(TransferEvent transferEvent, byte[] buffer, int length) {
      this.digester.update(buffer, 0, length);
   }

   public void transferCompleted(TransferEvent transferEvent) {
      this.actualChecksum = this.encode(this.digester.digest());
   }

   public void transferError(TransferEvent transferEvent) {
      this.digester.reset();
      this.actualChecksum = null;
   }

   public void debug(String message) {
   }

   public String getActualChecksum() {
      return this.actualChecksum;
   }

   protected String encode(byte[] binaryData) {
      if (binaryData.length != 16 && binaryData.length != 20) {
         int bitLength = binaryData.length * 8;
         throw new IllegalArgumentException("Unrecognised length for binary data: " + bitLength + " bits");
      } else {
         String retValue = "";

         for(int i = 0; i < binaryData.length; ++i) {
            String t = Integer.toHexString(binaryData[i] & 255);
            if (t.length() == 1) {
               retValue = retValue + "0" + t;
            } else {
               retValue = retValue + t;
            }
         }

         return retValue.trim();
      }
   }
}

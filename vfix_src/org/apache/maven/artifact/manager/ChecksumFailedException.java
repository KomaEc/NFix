package org.apache.maven.artifact.manager;

import org.apache.maven.wagon.TransferFailedException;

public class ChecksumFailedException extends TransferFailedException {
   public ChecksumFailedException(String s) {
      super(s);
   }

   public ChecksumFailedException(String message, Throwable cause) {
      super(message, cause);
   }
}

package org.netbeans.lib.cvsclient.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import org.netbeans.lib.cvsclient.util.BugLog;
import org.netbeans.lib.cvsclient.util.ByteArray;

public class WriteRcsDiffFilePreprocessor implements WriteTextFilePreprocessor {
   private static final int CHUNK_SIZE = 32768;
   private static final int READ_REMAINING = -2;
   private String lineEnding = System.getProperty("line.separator");

   public String getLineEnding() {
      return this.lineEnding;
   }

   public void setLineEnding(String var1) {
      this.lineEnding = var1;
   }

   public void copyTextFileToLocation(InputStream var1, File var2, OutputStreamProvider var3) throws IOException {
      WriteRcsDiffFilePreprocessor.ReadInfo var4 = null;
      BufferedOutputStream var5 = null;
      WriteRcsDiffFilePreprocessor.ReadInfo var6 = null;
      File var7 = null;

      try {
         var6 = new WriteRcsDiffFilePreprocessor.ReadInfo(new BufferedInputStream(var1));
         var4 = new WriteRcsDiffFilePreprocessor.ReadInfo(new BufferedInputStream(new FileInputStream(var2)));
         File var8 = new File(var2.getParent(), "CVS");
         var7 = File.createTempFile(".#merg", "cvs", var8);
         var5 = new BufferedOutputStream(new FileOutputStream(var7));
         boolean var9 = false;
         boolean var10 = false;
         byte[] var11 = var6.readLine();

         while(true) {
            if (var11 == null || var11.length <= 0) {
               this.readToLine(-2, var4, var5);
               if (var7 != null) {
                  var4.close();
                  var5.close();
                  BufferedInputStream var12 = null;
                  OutputStream var13 = var3.createOutputStream();

                  try {
                     var12 = new BufferedInputStream(new FileInputStream(var7));

                     while(true) {
                        int var14 = var12.read();
                        if (var14 == -1) {
                           return;
                        }

                        var13.write(var14);
                     }
                  } finally {
                     if (var12 != null) {
                        try {
                           var12.close();
                        } catch (IOException var51) {
                        }
                     }

                     try {
                        var13.close();
                     } catch (IOException var50) {
                     }

                  }
               }
               break;
            }

            int var55;
            int var56;
            if (var11[0] == 100) {
               var55 = getStart(var11);
               var56 = getLength(var11);
               if (var55 < 0 || var56 <= 0) {
                  BugLog.getInstance().bug("wrong parsing.." + new String(var11));
                  throw new IOException();
               }

               this.readToLine(var55 - 1, var4, var5);
               this.readToLine(var55 - 1 + var56, var4, (OutputStream)null);
            } else if (var11[0] == 97) {
               var55 = getStart(var11);
               var56 = getLength(var11);
               if (var55 < 0 || var56 <= 0) {
                  BugLog.getInstance().bug("wrong parsing.." + new String(var11));
                  throw new IOException();
               }

               this.readToLine(var55, var4, var5);
               var6.setLineNumber(0);
               this.readToLine(var56, var6, var5);
            }

            var11 = var6.readLine();
         }
      } catch (Exception var53) {
         BugLog.getInstance().showException(var53);
      } finally {
         if (var6 != null) {
            try {
               var6.close();
            } catch (IOException var49) {
            }
         }

         if (var4 != null) {
            try {
               var4.close();
            } catch (IOException var48) {
            }
         }

         if (var5 != null) {
            try {
               var5.close();
            } catch (IOException var47) {
            }
         }

      }

   }

   private void readToLine(int var1, WriteRcsDiffFilePreprocessor.ReadInfo var2, OutputStream var3) throws IOException {
      while(var2.getLineNumber() < var1 || var1 == -2) {
         byte[] var4 = var2.readLine();
         if (var4 == null) {
            return;
         }

         if (var3 != null) {
            var3.write(var4);
            var3.write(this.getLineEnding().getBytes());
         }
      }

   }

   private static int indexOf(byte[] var0, byte var1) {
      return indexOf(var0, var1, 0);
   }

   private static int indexOf(byte[] var0, byte var1, int var2) {
      int var3 = -1;

      for(int var4 = var2; var4 < var0.length; ++var4) {
         if (var0[var4] == var1) {
            var3 = var4;
            break;
         }
      }

      return var3;
   }

   private static int getStart(byte[] var0) {
      int var1 = indexOf(var0, (byte)32);
      if (var1 > 0) {
         String var2 = new String(var0, 1, var1 - 1);

         try {
            int var3 = Integer.parseInt(var2);
            return var3;
         } catch (NumberFormatException var4) {
            return -1;
         }
      } else {
         return -1;
      }
   }

   private static int getLength(byte[] var0) {
      int var1 = indexOf(var0, (byte)32);
      if (var1 > 0) {
         int var2 = indexOf(var0, (byte)32, var1 + 1);
         if (var2 < 0) {
            var2 = var0.length;
         }

         String var3 = new String(var0, var1 + 1, var2 - var1 - 1);

         try {
            int var4 = Integer.parseInt(var3);
            return var4;
         } catch (NumberFormatException var5) {
            return -1;
         }
      } else {
         return -1;
      }
   }

   private static class ReadInfo {
      private static final boolean crLines = "\r".equals(System.getProperty("line.separator"));
      private PushbackInputStream in;
      private int readLength;
      private int startIndex;
      private int lineNumber;
      private ByteArray line;

      public ReadInfo(InputStream var1) {
         this.in = new PushbackInputStream(var1, 1);
         this.readLength = -1;
         this.startIndex = 0;
         this.lineNumber = 0;
         this.line = new ByteArray();
      }

      public int getLineNumber() {
         return this.lineNumber;
      }

      public void setLineNumber(int var1) {
         this.lineNumber = var1;
      }

      public byte[] readLine() throws IOException {
         this.line.reset();
         boolean var1 = false;

         while(true) {
            int var2 = this.in.read();
            if (var2 == -1) {
               var1 = true;
               break;
            }

            if (var2 == 10) {
               ++this.lineNumber;
               break;
            }

            if (var2 == 13) {
               int var3 = this.in.read();
               if (var3 == 10) {
                  ++this.lineNumber;
                  break;
               }

               this.in.unread(var3);
               if (crLines) {
                  ++this.lineNumber;
                  break;
               }
            }

            this.line.add((byte)var2);
         }

         byte[] var4 = this.line.getBytes();
         if (var1 && var4.length == 0) {
            var4 = null;
         }

         return var4;
      }

      public void close() throws IOException {
         if (this.in != null) {
            this.in.close();
         }

      }
   }
}

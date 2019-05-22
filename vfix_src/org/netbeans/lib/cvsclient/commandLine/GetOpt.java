package org.netbeans.lib.cvsclient.commandLine;

public class GetOpt {
   private String[] theArgs = null;
   private int argCount = 0;
   private String optString = null;
   public boolean optErr = false;
   public static final int optEOF = -1;
   private int optIndex = 0;
   private String optArg = null;
   private int optPosition = 1;

   public GetOpt(String[] var1, String var2) {
      this.theArgs = var1;
      this.argCount = this.theArgs.length;
      this.optString = var2;
   }

   public int processArg(String var1, int var2) {
      try {
         int var3 = Integer.parseInt(var1);
         return var3;
      } catch (NumberFormatException var5) {
         if (this.optErr) {
            System.err.println("processArg cannot process " + var1 + " as an integer");
         }

         return var2;
      }
   }

   public int tryArg(int var1, int var2) {
      try {
         int var3 = this.processArg(this.theArgs[var1], var2);
         return var3;
      } catch (ArrayIndexOutOfBoundsException var5) {
         if (this.optErr) {
            System.err.println("tryArg: no theArgs[" + var1 + "]");
         }

         return var2;
      }
   }

   public long processArg(String var1, long var2) {
      try {
         long var4 = Long.parseLong(var1);
         return var4;
      } catch (NumberFormatException var7) {
         if (this.optErr) {
            System.err.println("processArg cannot process " + var1 + " as a long");
         }

         return var2;
      }
   }

   public long tryArg(int var1, long var2) {
      try {
         long var4 = this.processArg(this.theArgs[var1], var2);
         return var4;
      } catch (ArrayIndexOutOfBoundsException var7) {
         if (this.optErr) {
            System.err.println("tryArg: no theArgs[" + var1 + "]");
         }

         return var2;
      }
   }

   public double processArg(String var1, double var2) {
      try {
         double var4 = Double.valueOf(var1);
         return var4;
      } catch (NumberFormatException var7) {
         if (this.optErr) {
            System.err.println("processArg cannot process " + var1 + " as a double");
         }

         return var2;
      }
   }

   public double tryArg(int var1, double var2) {
      try {
         double var4 = this.processArg(this.theArgs[var1], var2);
         return var4;
      } catch (ArrayIndexOutOfBoundsException var7) {
         if (this.optErr) {
            System.err.println("tryArg: no theArgs[" + var1 + "]");
         }

         return var2;
      }
   }

   public float processArg(String var1, float var2) {
      try {
         float var3 = Float.valueOf(var1);
         return var3;
      } catch (NumberFormatException var5) {
         if (this.optErr) {
            System.err.println("processArg cannot process " + var1 + " as a float");
         }

         return var2;
      }
   }

   public float tryArg(int var1, float var2) {
      try {
         float var3 = this.processArg(this.theArgs[var1], var2);
         return var3;
      } catch (ArrayIndexOutOfBoundsException var5) {
         if (this.optErr) {
            System.err.println("tryArg: no theArgs[" + var1 + "]");
         }

         return var2;
      }
   }

   public boolean processArg(String var1, boolean var2) {
      return Boolean.valueOf(var1);
   }

   public boolean tryArg(int var1, boolean var2) {
      try {
         boolean var3 = this.processArg(this.theArgs[var1], var2);
         return var3;
      } catch (ArrayIndexOutOfBoundsException var5) {
         if (this.optErr) {
            System.err.println("tryArg: no theArgs[" + var1 + "]");
         }

         return var2;
      }
   }

   public String tryArg(int var1, String var2) {
      try {
         String var3 = this.theArgs[var1];
         return var3;
      } catch (ArrayIndexOutOfBoundsException var5) {
         if (this.optErr) {
            System.err.println("tryArg: no theArgs[" + var1 + "]");
         }

         return var2;
      }
   }

   private static void writeError(String var0, char var1) {
      System.err.println("GetOpt: " + var0 + " -- " + var1);
   }

   public int optIndexGet() {
      return this.optIndex;
   }

   public void optIndexSet(int var1) {
      this.optIndex = var1;
   }

   public String optArgGet() {
      return this.optArg;
   }

   public int getopt() {
      this.optArg = null;
      if (this.theArgs != null && this.optString != null) {
         if (this.optIndex >= 0 && this.optIndex < this.argCount) {
            String var1 = this.theArgs[this.optIndex];
            int var2 = var1.length();
            if (var2 > 1 && var1.charAt(0) == '-') {
               if (var1.equals("--")) {
                  ++this.optIndex;
                  return -1;
               } else {
                  char var3 = var1.charAt(this.optPosition);
                  int var4 = this.optString.indexOf(var3);
                  if (var4 != -1 && var3 != ':') {
                     if (var4 < this.optString.length() - 1 && this.optString.charAt(var4 + 1) == ':') {
                        if (this.optPosition != var2 - 1) {
                           this.optArg = var1.substring(this.optPosition + 1);
                           this.optPosition = var2 - 1;
                        } else {
                           ++this.optIndex;
                           if (this.optIndex < this.argCount && (this.theArgs[this.optIndex].charAt(0) != '-' || this.theArgs[this.optIndex].length() >= 2 && (this.optString.indexOf(this.theArgs[this.optIndex].charAt(1)) == -1 || this.theArgs[this.optIndex].charAt(1) == ':'))) {
                              this.optArg = this.theArgs[this.optIndex];
                           } else {
                              if (this.optErr) {
                                 writeError("option requires an argument", var3);
                              }

                              this.optArg = null;
                              var3 = ':';
                           }
                        }
                     }
                  } else {
                     if (this.optErr) {
                        writeError("illegal option", var3);
                     }

                     var3 = '?';
                  }

                  ++this.optPosition;
                  if (this.optPosition >= var2) {
                     ++this.optIndex;
                     this.optPosition = 1;
                  }

                  return var3;
               }
            } else {
               return -1;
            }
         } else {
            return -1;
         }
      } else {
         return -1;
      }
   }

   public static void main(String[] var0) {
      GetOpt var1 = new GetOpt(var0, "Uab:f:h:w:");
      var1.optErr = true;
      boolean var2 = true;
      boolean var3 = false;
      int var4 = 0;
      boolean var5 = false;
      String var6 = "out";
      int var7 = 80;
      double var8 = 1.0D;

      int var11;
      while((var11 = var1.getopt()) != -1) {
         if ((char)var11 == 'U') {
            var3 = true;
         } else if ((char)var11 == 'a') {
            ++var4;
         } else if ((char)var11 == 'b') {
            var5 = var1.processArg(var1.optArgGet(), var5);
         } else if ((char)var11 == 'f') {
            var6 = var1.optArgGet();
         } else if ((char)var11 == 'h') {
            var8 = var1.processArg(var1.optArgGet(), var8);
         } else if ((char)var11 == 'w') {
            var7 = var1.processArg(var1.optArgGet(), var7);
         } else {
            System.exit(1);
         }
      }

      if (var3) {
         System.out.println("Usage: -a -b bool -f file -h height -w width");
         System.exit(0);
      }

      System.out.println("These are all the command line arguments before processing with GetOpt:");

      int var10;
      for(var10 = 0; var10 < var0.length; ++var10) {
         System.out.print(" " + var0[var10]);
      }

      System.out.println();
      System.out.println("-U " + var3);
      System.out.println("-a " + var4);
      System.out.println("-b " + var5);
      System.out.println("-f " + var6);
      System.out.println("-h " + var8);
      System.out.println("-w " + var7);

      for(var10 = var1.optIndexGet(); var10 < var0.length; ++var10) {
         System.out.println("normal argument " + var10 + " is " + var0[var10]);
      }

   }
}

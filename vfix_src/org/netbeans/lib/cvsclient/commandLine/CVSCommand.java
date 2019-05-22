package org.netbeans.lib.cvsclient.commandLine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ResourceBundle;
import org.netbeans.lib.cvsclient.CVSRoot;
import org.netbeans.lib.cvsclient.Client;
import org.netbeans.lib.cvsclient.admin.StandardAdminHandler;
import org.netbeans.lib.cvsclient.command.BasicCommand;
import org.netbeans.lib.cvsclient.command.Command;
import org.netbeans.lib.cvsclient.command.CommandAbortedException;
import org.netbeans.lib.cvsclient.command.CommandException;
import org.netbeans.lib.cvsclient.command.GlobalOptions;
import org.netbeans.lib.cvsclient.commandLine.command.CommandProvider;
import org.netbeans.lib.cvsclient.connection.AuthenticationException;
import org.netbeans.lib.cvsclient.connection.Connection;
import org.netbeans.lib.cvsclient.connection.ConnectionFactory;
import org.netbeans.lib.cvsclient.connection.PServerConnection;
import org.netbeans.lib.cvsclient.connection.PasswordsFile;
import org.netbeans.lib.cvsclient.connection.StandardScrambler;
import org.netbeans.lib.cvsclient.event.CVSListener;

public class CVSCommand {
   private static final String HELP_OPTIONS = "--help-options";
   private static final String HELP_COMMANDS = "--help-commands";
   private static final String HELP_SYNONYMS = "--help-synonyms";
   private String repository;
   private String localPath;
   private Connection connection;
   private Client client;
   private GlobalOptions globalOptions;
   private int port = 0;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public boolean executeCommand(Command var1, PrintStream var2) throws CommandException, AuthenticationException {
      this.client.setErrorStream(var2);
      return this.client.executeCommand(var1, this.globalOptions);
   }

   public void setRepository(String var1) {
      this.repository = var1;
   }

   public void setLocalPath(String var1) {
      this.localPath = var1;
   }

   public void setGlobalOptions(GlobalOptions var1) {
      this.globalOptions = var1;
   }

   private void connect(CVSRoot var1, String var2) throws IllegalArgumentException, AuthenticationException, CommandAbortedException {
      this.connection = ConnectionFactory.getConnection(var1);
      if ("pserver".equals(var1.getMethod())) {
         ((PServerConnection)this.connection).setEncodedPassword(var2);
         if (this.port > 0) {
            ((PServerConnection)this.connection).setPort(this.port);
         }
      }

      this.connection.open();
      this.client = new Client(this.connection, new StandardAdminHandler());
      this.client.setLocalPath(this.localPath);
   }

   private void addListener(CVSListener var1) {
      if (this.client != null) {
         this.client.getEventManager().addCVSListener(var1);
      }

   }

   private void close(PrintStream var1) {
      try {
         this.connection.close();
      } catch (IOException var3) {
         var1.println("Unable to close connection: " + var3);
      }

   }

   private static String getCVSRoot(String var0) {
      String var1 = null;
      BufferedReader var2 = null;
      if (var0 == null) {
         var0 = System.getProperty("user.dir");
      }

      try {
         File var3 = new File(var0);
         File var4 = new File(var3, "CVS/Root");
         if (var4.exists()) {
            var2 = new BufferedReader(new FileReader(var4));
            var1 = var2.readLine();
         }
      } catch (IOException var14) {
      } finally {
         try {
            if (var2 != null) {
               var2.close();
            }
         } catch (IOException var13) {
            System.err.println("Warning: could not close CVS/Root file!");
         }

      }

      if (var1 == null) {
         var1 = System.getProperty("cvs.root");
      }

      return var1;
   }

   private static int processGlobalOptions(String[] var0, GlobalOptions var1, PrintStream var2) {
      String var3 = var1.getOptString();
      GetOpt var4 = new GetOpt(var0, var3);
      boolean var5 = true;
      boolean var6 = false;

      int var8;
      while((var8 = var4.getopt()) != -1) {
         boolean var7 = var1.setCVSCommand((char)var8, var4.optArgGet());
         if (!var7) {
            var6 = true;
         }
      }

      if (var6) {
         showUsage(var2);
         return -10;
      } else {
         return var4.optIndexGet();
      }
   }

   private static void showUsage(PrintStream var0) {
      String var1 = ResourceBundle.getBundle(CVSCommand.class.getPackage().getName() + ".Bundle").getString("MSG_HelpUsage");
      var0.println(MessageFormat.format(var1, "--help-options", "--help-commands", "--help-synonyms"));
   }

   private static boolean performLogin(String var0, String var1, String var2, int var3, GlobalOptions var4) {
      PServerConnection var5 = new PServerConnection();
      var5.setUserName(var0);
      String var6 = null;

      try {
         BufferedReader var7 = new BufferedReader(new InputStreamReader(System.in));
         System.out.print("Enter password: ");
         var6 = var7.readLine();
      } catch (IOException var12) {
         System.err.println("Could not read password: " + var12);
         return false;
      }

      String var13 = StandardScrambler.getInstance().scramble(var6);
      var5.setEncodedPassword(var13);
      var5.setHostName(var1);
      var5.setRepository(var2);
      var5.setPort(var3);

      try {
         var5.verify();
      } catch (AuthenticationException var11) {
         System.err.println("Could not login to host " + var1);
         return false;
      }

      String var8 = var4.getCVSRoot();

      try {
         PasswordsFile.storePassword(var8, var13);
         System.err.println("Logged in successfully to " + var2 + " on host " + var1);
         return true;
      } catch (IOException var10) {
         System.err.println("Error: could not write password file.");
         return false;
      }
   }

   private static String lookupPassword(String var0, String var1, PrintStream var2) {
      File var3 = new File(System.getProperty("cvs.passfile", System.getProperty("user.home") + "/.cvspass"));
      BufferedReader var4 = null;
      String var5 = null;

      label119: {
         Object var7;
         try {
            var4 = new BufferedReader(new FileReader(var3));

            String var6;
            do {
               if ((var6 = var4.readLine()) == null) {
                  break label119;
               }

               if (var6.startsWith("/1 ")) {
                  var6 = var6.substring("/1 ".length());
               }

               if (var6.startsWith(var0 + " ")) {
                  var5 = var6.substring(var0.length() + 1);
                  break label119;
               }
            } while(!var6.startsWith(var1 + " "));

            var5 = var6.substring(var1.length() + 1);
            break label119;
         } catch (IOException var17) {
            var2.println("Could not read password for host: " + var17);
            var7 = null;
         } finally {
            if (var4 != null) {
               try {
                  var4.close();
               } catch (IOException var16) {
                  var2.println("Warning: could not close password file.");
               }
            }

         }

         return (String)var7;
      }

      if (var5 == null) {
         var2.println("Didn't find password for CVSROOT '" + var0 + "'.");
      }

      return var5;
   }

   public static void main(String[] var0) {
      if (processCommand(var0, (File[])null, System.getProperty("user.dir"), System.out, System.err)) {
         System.exit(0);
      } else {
         System.exit(1);
      }

   }

   public static boolean processCommand(String[] var0, File[] var1, String var2, PrintStream var3, PrintStream var4) {
      return processCommand(var0, var1, var2, 0, var3, var4);
   }

   public static boolean processCommand(String[] var0, File[] var1, String var2, int var3, PrintStream var4, PrintStream var5) {
      if (!$assertionsDisabled && var4 == null) {
         throw new AssertionError("The output stream must be defined.");
      } else if (!$assertionsDisabled && var5 == null) {
         throw new AssertionError("The error stream must be defined.");
      } else {
         if (var0.length > 0) {
            if ("--help-options".equals(var0[0])) {
               printHelpOptions(var4);
               return true;
            }

            if ("--help-commands".equals(var0[0])) {
               printHelpCommands(var4);
               return true;
            }

            if ("--help-synonyms".equals(var0[0])) {
               printHelpSynonyms(var4);
               return true;
            }
         }

         try {
            var2 = (new File(var2)).getCanonicalPath();
         } catch (IOException var29) {
         }

         GlobalOptions var6 = new GlobalOptions();
         var6.setCVSRoot(getCVSRoot(var2));
         boolean var7 = true;

         int var34;
         try {
            var34 = processGlobalOptions(var0, var6, var5);
            if (var34 == -10) {
               return true;
            }
         } catch (IllegalArgumentException var28) {
            var5.println("Invalid argument: " + var28);
            return false;
         }

         if (var6.isShowHelp()) {
            printHelp(var34, var0, var4, var5);
            return true;
         } else if (var6.isShowVersion()) {
            printVersion(var4, var5);
            return true;
         } else if (var6.getCVSRoot() == null) {
            var5.println("No CVS root is set. Use the cvs.root property, e.g. java -Dcvs.root=\":pserver:user@host:/usr/cvs\" or start the application in a directory containing a CVS subdirectory or use the -d command switch.");
            return false;
         } else {
            CVSRoot var8 = null;
            String var9 = var6.getCVSRoot();

            try {
               var8 = CVSRoot.parse(var9);
            } catch (IllegalArgumentException var27) {
               var5.println("Incorrect format for CVSRoot: " + var9 + "\nThe correct format is: " + "[:method:][[user][:password]@][hostname:[port]]/path/to/repository" + "\nwhere \"method\" is pserver.");
               return false;
            }

            if (var34 >= var0.length) {
               showUsage(var5);
               return false;
            } else {
               String var10 = var0[var34];
               if (var10.equals("login")) {
                  if ("pserver".equals(var8.getMethod())) {
                     return performLogin(var8.getUserName(), var8.getHostName(), var8.getRepository(), var8.getPort(), var6);
                  } else {
                     var5.println("login does not apply for connection type '" + var8.getMethod() + "'");
                     return false;
                  }
               } else {
                  Command var11 = null;

                  try {
                     CommandFactory var10000 = CommandFactory.getDefault();
                     ++var34;
                     var11 = var10000.createCommand(var10, var0, var34, var6, var2);
                  } catch (IllegalArgumentException var26) {
                     var5.println("Illegal argument: " + var26.getMessage());
                     return false;
                  }

                  if (var1 != null && var11 instanceof BasicCommand) {
                     ((BasicCommand)var11).setFiles(var1);
                  }

                  String var12 = null;
                  if ("pserver".equals(var8.getMethod())) {
                     var12 = var8.getPassword();
                     if (var12 != null) {
                        var12 = StandardScrambler.getInstance().scramble(var12);
                     } else {
                        if (var3 > 0) {
                           var8.setPort(var3);
                        }

                        var12 = lookupPassword(var9, var8.toString(), var5);
                        if (var12 == null) {
                           var12 = StandardScrambler.getInstance().scramble("");
                        }
                     }
                  }

                  CVSCommand var13 = new CVSCommand();
                  var13.setGlobalOptions(var6);
                  var13.setRepository(var8.getRepository());
                  if (var3 > 0) {
                     var13.port = var3;
                  }

                  var13.setLocalPath(var2);

                  boolean var15;
                  try {
                     var13.connect(var8, var12);
                     Object var14;
                     if (var11 instanceof ListenerProvider) {
                        var14 = ((ListenerProvider)var11).createCVSListener(var4, var5);
                     } else {
                        var14 = new BasicListener(var4, var5);
                     }

                     var13.addListener((CVSListener)var14);
                     var15 = var13.executeCommand(var11, var5);
                     boolean var16 = var15;
                     return var16;
                  } catch (AuthenticationException var30) {
                     var5.println(var30.getLocalizedMessage());
                     var15 = false;
                     return var15;
                  } catch (CommandAbortedException var31) {
                     var5.println("Error: " + var31);
                     Thread.currentThread().interrupt();
                     var15 = false;
                  } catch (Exception var32) {
                     var5.println("Error: " + var32);
                     var32.printStackTrace(var5);
                     var15 = false;
                     return var15;
                  } finally {
                     if (var13 != null) {
                        var13.close(var5);
                     }

                  }

                  return var15;
               }
            }
         }
      }
   }

   private static void printHelpOptions(PrintStream var0) {
      String var1 = ResourceBundle.getBundle(CVSCommand.class.getPackage().getName() + ".Bundle").getString("MSG_HelpOptions");
      var0.println(var1);
   }

   private static void printHelpCommands(PrintStream var0) {
      String var1 = ResourceBundle.getBundle(CVSCommand.class.getPackage().getName() + ".Bundle").getString("MSG_CVSCommands");
      var0.println(var1);
      CommandProvider[] var2 = CommandFactory.getDefault().getCommandProviders();
      Arrays.sort(var2, new CVSCommand.CommandProvidersComparator());
      int var3 = 0;

      int var4;
      for(var4 = 0; var4 < var2.length; ++var4) {
         int var5 = var2[var4].getName().length();
         if (var3 < var5) {
            var3 = var5;
         }
      }

      var3 += 2;

      for(var4 = 0; var4 < var2.length; ++var4) {
         var0.print("\t" + var2[var4].getName());
         char[] var6 = new char[var3 - var2[var4].getName().length()];
         Arrays.fill(var6, ' ');
         var0.print(new String(var6));
         var2[var4].printShortDescription(var0);
         var0.println();
      }

   }

   private static void printHelpSynonyms(PrintStream var0) {
      String var1 = ResourceBundle.getBundle(CVSCommand.class.getPackage().getName() + ".Bundle").getString("MSG_CVSSynonyms");
      var0.println(var1);
      CommandProvider[] var2 = CommandFactory.getDefault().getCommandProviders();
      Arrays.sort(var2, new CVSCommand.CommandProvidersComparator());
      int var3 = 0;

      int var4;
      for(var4 = 0; var4 < var2.length; ++var4) {
         int var5 = var2[var4].getName().length();
         if (var3 < var5) {
            var3 = var5;
         }
      }

      var3 += 2;

      for(var4 = 0; var4 < var2.length; ++var4) {
         String[] var8 = var2[var4].getSynonyms();
         if (var8.length > 0) {
            var0.print("\t" + var2[var4].getName());
            char[] var6 = new char[var3 - var2[var4].getName().length()];
            Arrays.fill(var6, ' ');
            var0.print(new String(var6));

            for(int var7 = 0; var7 < var8.length; ++var7) {
               var0.print(var8[var7] + " ");
            }

            var0.println();
         }
      }

   }

   private static void printHelp(int var0, String[] var1, PrintStream var2, PrintStream var3) {
      if (var0 >= var1.length) {
         showUsage(var2);
      } else {
         String var4 = var1[var0];
         CommandProvider var5 = CommandFactory.getDefault().getCommandProvider(var4);
         if (var5 == null) {
            printUnknownCommand(var4, var3);
         } else {
            var5.printLongDescription(var2);
         }
      }

   }

   private static void printVersion(PrintStream var0, PrintStream var1) {
      String var2 = CVSCommand.class.getPackage().getSpecificationVersion();
      var0.println("Java Concurrent Versions System (JavaCVS) " + var2 + " (client)");
   }

   private static void printUnknownCommand(String var0, PrintStream var1) {
      String var2 = ResourceBundle.getBundle(CVSCommand.class.getPackage().getName() + ".Bundle").getString("MSG_UnknownCommand");
      var1.println(MessageFormat.format(var2, var0));
      printHelpCommands(var1);
   }

   static {
      $assertionsDisabled = !CVSCommand.class.desiredAssertionStatus();
   }

   private static final class CommandProvidersComparator implements Comparator {
      private CommandProvidersComparator() {
      }

      public int compare(Object var1, Object var2) {
         if (var1 instanceof CommandProvider && var2 instanceof CommandProvider) {
            return ((CommandProvider)var1).getName().compareTo(((CommandProvider)var2).getName());
         } else {
            throw new IllegalArgumentException("Can not compare objects " + var1 + " and " + var2);
         }
      }

      // $FF: synthetic method
      CommandProvidersComparator(Object var1) {
         this();
      }
   }
}

package org.apache.tools.ant.taskdefs.cvslib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.taskdefs.AbstractCvsTask;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.util.FileUtils;

public class ChangeLogTask extends AbstractCvsTask {
   private File usersFile;
   private Vector cvsUsers = new Vector();
   private File inputDir;
   private File destFile;
   private Date startDate;
   private Date endDate;
   private final Vector filesets = new Vector();

   public void setDir(File inputDir) {
      this.inputDir = inputDir;
   }

   public void setDestfile(File destFile) {
      this.destFile = destFile;
   }

   public void setUsersfile(File usersFile) {
      this.usersFile = usersFile;
   }

   public void addUser(CvsUser user) {
      this.cvsUsers.addElement(user);
   }

   public void setStart(Date start) {
      this.startDate = start;
   }

   public void setEnd(Date endDate) {
      this.endDate = endDate;
   }

   public void setDaysinpast(int days) {
      long time = System.currentTimeMillis() - (long)days * 24L * 60L * 60L * 1000L;
      this.setStart(new Date(time));
   }

   public void addFileset(FileSet fileSet) {
      this.filesets.addElement(fileSet);
   }

   public void execute() throws BuildException {
      File savedDir = this.inputDir;

      try {
         this.validate();
         Properties userList = new Properties();
         this.loadUserlist(userList);
         int i = 0;

         for(int size = this.cvsUsers.size(); i < size; ++i) {
            CvsUser user = (CvsUser)this.cvsUsers.get(i);
            user.validate();
            userList.put(user.getUserID(), user.getDisplayname());
         }

         this.setCommand("log");
         if (this.getTag() != null) {
            CvsVersion myCvsVersion = new CvsVersion();
            myCvsVersion.setProject(this.getProject());
            myCvsVersion.setTaskName("cvsversion");
            myCvsVersion.setCvsRoot(this.getCvsRoot());
            myCvsVersion.setCvsRsh(this.getCvsRsh());
            myCvsVersion.setPassfile(this.getPassFile());
            myCvsVersion.setDest(this.inputDir);
            myCvsVersion.execute();
            if (myCvsVersion.supportsCvsLogWithSOption()) {
               this.addCommandArgument("-S");
            }
         }

         if (null != this.startDate) {
            SimpleDateFormat outputDate = new SimpleDateFormat("yyyy-MM-dd");
            String dateRange = ">=" + outputDate.format(this.startDate);
            this.addCommandArgument("-d");
            this.addCommandArgument(dateRange);
         }

         if (!this.filesets.isEmpty()) {
            Enumeration e = this.filesets.elements();

            while(e.hasMoreElements()) {
               FileSet fileSet = (FileSet)e.nextElement();
               DirectoryScanner scanner = fileSet.getDirectoryScanner(this.getProject());
               String[] files = scanner.getIncludedFiles();

               for(int i = 0; i < files.length; ++i) {
                  this.addCommandArgument(files[i]);
               }
            }
         }

         ChangeLogParser parser = new ChangeLogParser();
         RedirectingStreamHandler handler = new RedirectingStreamHandler(parser);
         this.log(this.getCommand(), 3);
         this.setDest(this.inputDir);
         this.setExecuteStreamHandler(handler);
         boolean var15 = false;

         try {
            var15 = true;
            super.execute();
            var15 = false;
         } finally {
            if (var15) {
               String errors = handler.getErrors();
               if (null != errors) {
                  this.log(errors, 0);
               }

            }
         }

         String errors = handler.getErrors();
         if (null != errors) {
            this.log(errors, 0);
         }

         CVSEntry[] entrySet = parser.getEntrySetAsArray();
         CVSEntry[] filteredEntrySet = this.filterEntrySet(entrySet);
         this.replaceAuthorIdWithName(userList, filteredEntrySet);
         this.writeChangeLog(filteredEntrySet);
      } finally {
         this.inputDir = savedDir;
      }
   }

   private void validate() throws BuildException {
      if (null == this.inputDir) {
         this.inputDir = this.getProject().getBaseDir();
      }

      String message;
      if (null == this.destFile) {
         message = "Destfile must be set.";
         throw new BuildException("Destfile must be set.");
      } else if (!this.inputDir.exists()) {
         message = "Cannot find base dir " + this.inputDir.getAbsolutePath();
         throw new BuildException(message);
      } else if (null != this.usersFile && !this.usersFile.exists()) {
         message = "Cannot find user lookup list " + this.usersFile.getAbsolutePath();
         throw new BuildException(message);
      }
   }

   private void loadUserlist(Properties userList) throws BuildException {
      if (null != this.usersFile) {
         try {
            userList.load(new FileInputStream(this.usersFile));
         } catch (IOException var3) {
            throw new BuildException(var3.toString(), var3);
         }
      }

   }

   private CVSEntry[] filterEntrySet(CVSEntry[] entrySet) {
      Vector results = new Vector();

      for(int i = 0; i < entrySet.length; ++i) {
         CVSEntry cvsEntry = entrySet[i];
         Date date = cvsEntry.getDate();
         if (null != date && (null == this.startDate || !this.startDate.after(date)) && (null == this.endDate || !this.endDate.before(date))) {
            results.addElement(cvsEntry);
         }
      }

      CVSEntry[] resultArray = new CVSEntry[results.size()];
      results.copyInto(resultArray);
      return resultArray;
   }

   private void replaceAuthorIdWithName(Properties userList, CVSEntry[] entrySet) {
      for(int i = 0; i < entrySet.length; ++i) {
         CVSEntry entry = entrySet[i];
         if (userList.containsKey(entry.getAuthor())) {
            entry.setAuthor(userList.getProperty(entry.getAuthor()));
         }
      }

   }

   private void writeChangeLog(CVSEntry[] entrySet) throws BuildException {
      FileOutputStream output = null;

      try {
         output = new FileOutputStream(this.destFile);
         PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"));
         ChangeLogWriter serializer = new ChangeLogWriter();
         serializer.printChangeLog(writer, entrySet);
      } catch (UnsupportedEncodingException var9) {
         this.getProject().log(var9.toString(), 0);
      } catch (IOException var10) {
         throw new BuildException(var10.toString(), var10);
      } finally {
         FileUtils.close((OutputStream)output);
      }

   }
}

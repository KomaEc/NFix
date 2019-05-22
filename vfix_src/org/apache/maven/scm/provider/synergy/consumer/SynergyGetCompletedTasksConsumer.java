package org.apache.maven.scm.provider.synergy.consumer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import org.apache.maven.scm.log.ScmLogger;
import org.apache.maven.scm.provider.synergy.util.SynergyTask;
import org.apache.maven.scm.util.AbstractConsumer;

public class SynergyGetCompletedTasksConsumer extends AbstractConsumer {
   static final String CCMDATEFORMAT_PROPERTY = "maven.scm.synergy.ccmDateFormat";
   static final String LANGUAGE_PROPERTY = "maven.scm.synergy.language";
   static final String COUNTRY_PROPERTY = "maven.scm.synergy.country";
   private String ccmDateFormat = "EEE MMM dd HH:mm:ss yyyy";
   private String language = "en";
   private String country = "US";
   public static final String OUTPUT_FORMAT = "%displayname#####%owner#####%completion_date#####%task_synopsis#####";
   private List<SynergyTask> entries = new ArrayList();

   public List<SynergyTask> getTasks() {
      return this.entries;
   }

   public SynergyGetCompletedTasksConsumer(ScmLogger logger) {
      super(logger);
      String dateFormat = System.getProperty("maven.scm.synergy.ccmDateFormat");
      if (dateFormat != null && !dateFormat.equals("")) {
         this.ccmDateFormat = dateFormat;
      }

      if (logger.isDebugEnabled()) {
         logger.debug("dateFormat = " + this.ccmDateFormat);
      }

      String language = System.getProperty("maven.scm.synergy.language");
      if (language != null && !language.equals("")) {
         this.language = language;
      }

      if (logger.isDebugEnabled()) {
         logger.debug("language = " + this.language);
      }

      String country = System.getProperty("maven.scm.synergy.country");
      if (country != null && !country.equals("")) {
         this.country = country;
      }

      if (logger.isDebugEnabled()) {
         logger.debug("country = " + this.country);
      }

   }

   public void consumeLine(String line) {
      if (this.getLogger().isDebugEnabled()) {
         this.getLogger().debug("Consume: " + line);
      }

      StringTokenizer tokenizer = new StringTokenizer(line.trim(), "#####");
      if (tokenizer.countTokens() == 4) {
         SynergyTask task = new SynergyTask();
         task.setNumber(Integer.parseInt(tokenizer.nextToken()));
         task.setUsername(tokenizer.nextToken());

         try {
            task.setModifiedTime((new SimpleDateFormat(this.ccmDateFormat, new Locale(this.language, this.country))).parse(tokenizer.nextToken()));
         } catch (ParseException var5) {
            if (this.getLogger().isErrorEnabled()) {
               this.getLogger().error("Wrong date format", var5);
            }
         }

         task.setComment(tokenizer.nextToken());
         this.entries.add(task);
      } else {
         if (this.getLogger().isErrorEnabled()) {
            this.getLogger().error("Invalid token count in SynergyGetCompletedTasksConsumer [" + tokenizer.countTokens() + "]");
         }

         if (this.getLogger().isDebugEnabled()) {
            while(tokenizer.hasMoreElements()) {
               this.getLogger().debug(tokenizer.nextToken());
            }
         }
      }

   }
}

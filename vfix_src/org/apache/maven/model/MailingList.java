package org.apache.maven.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MailingList implements Serializable {
   private String name;
   private String subscribe;
   private String unsubscribe;
   private String post;
   private String archive;
   private List<String> otherArchives;

   public void addOtherArchive(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("MailingList.addOtherArchives(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getOtherArchives().add(string);
      }
   }

   public String getArchive() {
      return this.archive;
   }

   public String getName() {
      return this.name;
   }

   public List<String> getOtherArchives() {
      if (this.otherArchives == null) {
         this.otherArchives = new ArrayList();
      }

      return this.otherArchives;
   }

   public String getPost() {
      return this.post;
   }

   public String getSubscribe() {
      return this.subscribe;
   }

   public String getUnsubscribe() {
      return this.unsubscribe;
   }

   public void removeOtherArchive(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("MailingList.removeOtherArchives(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getOtherArchives().remove(string);
      }
   }

   public void setArchive(String archive) {
      this.archive = archive;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setOtherArchives(List<String> otherArchives) {
      this.otherArchives = otherArchives;
   }

   public void setPost(String post) {
      this.post = post;
   }

   public void setSubscribe(String subscribe) {
      this.subscribe = subscribe;
   }

   public void setUnsubscribe(String unsubscribe) {
      this.unsubscribe = unsubscribe;
   }
}

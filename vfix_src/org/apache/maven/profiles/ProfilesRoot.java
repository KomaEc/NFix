package org.apache.maven.profiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProfilesRoot implements Serializable {
   private List<Profile> profiles;
   private List<String> activeProfiles;
   private String modelEncoding = "UTF-8";

   public void addActiveProfile(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("ProfilesRoot.addActiveProfiles(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getActiveProfiles().add(string);
      }
   }

   public void addProfile(Profile profile) {
      if (!(profile instanceof Profile)) {
         throw new ClassCastException("ProfilesRoot.addProfiles(profile) parameter must be instanceof " + Profile.class.getName());
      } else {
         this.getProfiles().add(profile);
      }
   }

   public List<String> getActiveProfiles() {
      if (this.activeProfiles == null) {
         this.activeProfiles = new ArrayList();
      }

      return this.activeProfiles;
   }

   public String getModelEncoding() {
      return this.modelEncoding;
   }

   public List<Profile> getProfiles() {
      if (this.profiles == null) {
         this.profiles = new ArrayList();
      }

      return this.profiles;
   }

   public void removeActiveProfile(String string) {
      if (!(string instanceof String)) {
         throw new ClassCastException("ProfilesRoot.removeActiveProfiles(string) parameter must be instanceof " + String.class.getName());
      } else {
         this.getActiveProfiles().remove(string);
      }
   }

   public void removeProfile(Profile profile) {
      if (!(profile instanceof Profile)) {
         throw new ClassCastException("ProfilesRoot.removeProfiles(profile) parameter must be instanceof " + Profile.class.getName());
      } else {
         this.getProfiles().remove(profile);
      }
   }

   public void setActiveProfiles(List<String> activeProfiles) {
      this.activeProfiles = activeProfiles;
   }

   public void setModelEncoding(String modelEncoding) {
      this.modelEncoding = modelEncoding;
   }

   public void setProfiles(List<Profile> profiles) {
      this.profiles = profiles;
   }
}

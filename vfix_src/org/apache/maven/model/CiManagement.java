package org.apache.maven.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CiManagement implements Serializable {
   private String system;
   private String url;
   private List<Notifier> notifiers;

   public void addNotifier(Notifier notifier) {
      if (!(notifier instanceof Notifier)) {
         throw new ClassCastException("CiManagement.addNotifiers(notifier) parameter must be instanceof " + Notifier.class.getName());
      } else {
         this.getNotifiers().add(notifier);
      }
   }

   public List<Notifier> getNotifiers() {
      if (this.notifiers == null) {
         this.notifiers = new ArrayList();
      }

      return this.notifiers;
   }

   public String getSystem() {
      return this.system;
   }

   public String getUrl() {
      return this.url;
   }

   public void removeNotifier(Notifier notifier) {
      if (!(notifier instanceof Notifier)) {
         throw new ClassCastException("CiManagement.removeNotifiers(notifier) parameter must be instanceof " + Notifier.class.getName());
      } else {
         this.getNotifiers().remove(notifier);
      }
   }

   public void setNotifiers(List<Notifier> notifiers) {
      this.notifiers = notifiers;
   }

   public void setSystem(String system) {
      this.system = system;
   }

   public void setUrl(String url) {
      this.url = url;
   }
}

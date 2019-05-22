package org.codehaus.plexus.component.repository;

public class ComponentProfileDescriptor {
   private String componentFactoryId;
   private String lifecycleHandlerId;
   private String componentManagerId;
   private String componentComposerId;

   public String getComponentFactoryId() {
      return this.componentFactoryId;
   }

   public void setComponentFactoryId(String componentFactoryId) {
      this.componentFactoryId = componentFactoryId;
   }

   public String getLifecycleHandlerId() {
      return this.lifecycleHandlerId;
   }

   public void setLifecycleHandlerId(String lifecycleHandlerId) {
      this.lifecycleHandlerId = lifecycleHandlerId;
   }

   public String getComponentManagerId() {
      return this.componentManagerId;
   }

   public void setComponentManagerId(String componentManagerId) {
      this.componentManagerId = componentManagerId;
   }

   public String getComponentComposerId() {
      return this.componentComposerId;
   }

   public void setComponentComposerId(String componentComposerId) {
      this.componentComposerId = componentComposerId;
   }
}

package com.mks.api.response.impl;

import com.mks.api.common.XMLResponseDef;
import com.mks.api.response.modifiable.ModifiableItem;
import java.util.Enumeration;
import java.util.Properties;

public class ItemImpl extends AbstractFieldContainer implements ModifiableItem {
   protected Properties contextProperties = new Properties();

   ItemImpl(String id, String context, String modelType) {
      this.addContext(XMLResponseDef.XML_ID_ATTR, id);
      this.addContext(XMLResponseDef.XML_MODELTYPE_ATTR, modelType);
      this.addContext(XMLResponseDef.XML_CONTEXT_ATTR, context);
   }

   public String getId() {
      return this.getContext(XMLResponseDef.XML_ID_ATTR);
   }

   public String getContext() {
      return this.getContext(XMLResponseDef.XML_CONTEXT_ATTR);
   }

   public String getModelType() {
      return this.getContext(XMLResponseDef.XML_MODELTYPE_ATTR);
   }

   public String getDisplayId() {
      String displayId = this.getContext(XMLResponseDef.XML_DISPLAYID_ATTR);
      if (displayId == null) {
         displayId = this.getId();
      }

      return displayId;
   }

   public String getContext(String key) {
      return key == null ? null : this.contextProperties.getProperty(key);
   }

   public String toString() {
      return this.getDisplayId();
   }

   void addContext(String key, String context) {
      if (key != null && context != null) {
         this.contextProperties.setProperty(key, context);
      }
   }

   public Enumeration getContextKeys() {
      return this.contextProperties.propertyNames();
   }
}

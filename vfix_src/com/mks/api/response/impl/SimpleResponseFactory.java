package com.mks.api.response.impl;

import com.mks.api.response.modifiable.ModifiableField;
import com.mks.api.response.modifiable.ModifiableItem;
import com.mks.api.response.modifiable.ModifiableItemList;
import com.mks.api.response.modifiable.ModifiableResponse;
import com.mks.api.response.modifiable.ModifiableResult;
import com.mks.api.response.modifiable.ModifiableSubRoutine;
import com.mks.api.response.modifiable.ModifiableValueList;
import com.mks.api.response.modifiable.ModifiableWorkItem;
import com.mks.api.response.modifiable.ResponseFactory;
import java.util.Enumeration;
import java.util.Properties;

public final class SimpleResponseFactory implements ResponseFactory {
   private static SimpleResponseFactory singleton = null;

   public static synchronized SimpleResponseFactory getResponseFactory() {
      if (singleton == null) {
         singleton = new SimpleResponseFactory();
      }

      return singleton;
   }

   private SimpleResponseFactory() {
   }

   public ModifiableResponse createResponse() {
      return this.createResponse((String)null, (String)null);
   }

   public ModifiableResponse createResponse(String appName, String commandName) {
      return new ResponseImpl(new ResponseContainer(), appName, commandName);
   }

   public ModifiableWorkItem createWorkItem(String id, String context, String modelType) {
      return this.createWorkItem(id, context, modelType, (Properties)null);
   }

   public ModifiableWorkItem createWorkItem(String id, String context, String modelType, Properties properties) {
      WorkItemImpl wi = new WorkItemImpl(id, context, modelType);
      if (properties != null) {
         Enumeration e = properties.propertyNames();

         while(e.hasMoreElements()) {
            String key = (String)e.nextElement();
            wi.addContext(key, properties.getProperty(key));
         }
      }

      return wi;
   }

   public ModifiableSubRoutine createSubRoutine(String name) {
      return new SubRoutineImpl(new ResponseContainer(), name);
   }

   public ModifiableItem createItem(String name, String context, String modelType) {
      return this.createItem(name, context, modelType, (Properties)null);
   }

   public ModifiableItem createItem(String name, String context, String modelType, Properties properties) {
      ItemImpl item = new ItemImpl(name, context, modelType);
      if (properties != null) {
         Enumeration e = properties.propertyNames();

         while(e.hasMoreElements()) {
            String key = (String)e.nextElement();
            item.addContext(key, properties.getProperty(key));
         }
      }

      return item;
   }

   public ModifiableResult createResult() {
      return new ResultImpl();
   }

   public ModifiableItemList createItemList() {
      return new ItemListImpl();
   }

   public ModifiableValueList createValueList() {
      return new ValueListImpl();
   }

   public ModifiableField createField(String name) {
      return new FieldImpl(name);
   }

   public ModifiableField createField(String name, String displayName) {
      return new FieldImpl(name, displayName);
   }
}

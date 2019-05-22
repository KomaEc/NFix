package com.mks.api.response.impl;

import com.mks.api.CmdRunner;
import com.mks.api.response.modifiable.ModifiableField;
import com.mks.api.response.modifiable.ModifiableItem;
import com.mks.api.response.modifiable.ModifiableItemList;
import com.mks.api.response.modifiable.ModifiableResponse;
import com.mks.api.response.modifiable.ModifiableResult;
import com.mks.api.response.modifiable.ModifiableSubRoutine;
import com.mks.api.response.modifiable.ModifiableValueList;
import com.mks.api.response.modifiable.ModifiableWorkItem;
import com.mks.api.response.modifiable.ResponseFactory;

class XMLResponseFactory implements ResponseFactory {
   private static XMLResponseFactory instance = new XMLResponseFactory();

   static XMLResponseFactory getInstance() {
      return instance;
   }

   private XMLResponseFactory() {
   }

   public ModifiableResponse createResponse() {
      return this.createResponse((String)null, (String)null);
   }

   public ModifiableXMLResponse createXMLResponse(CmdRunner cmdRunner, String appName, String commandName) {
      return new XMLResponseImpl(cmdRunner, new XMLResponseContainer(), appName, commandName);
   }

   public ModifiableResponse createResponse(String appName, String commandName) {
      return this.createXMLResponse((CmdRunner)null, appName, commandName);
   }

   public ModifiableWorkItem createWorkItem(String id, String context, String modelType) {
      return new WorkItemImpl(id, context, modelType);
   }

   public ModifiableSubRoutine createSubRoutine(String name) {
      return this.createXMLSubRoutine(name);
   }

   public ModifiableXMLSubRoutine createXMLSubRoutine(String name) {
      return new XMLSubRoutineImpl(new XMLResponseContainer(), name);
   }

   public ModifiableItem createItem(String name, String context, String modelType) {
      return new ItemImpl(name, context, modelType);
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

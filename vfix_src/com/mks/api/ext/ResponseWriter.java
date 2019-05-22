package com.mks.api.ext;

import com.mks.api.response.APIException;
import com.mks.api.response.Field;
import com.mks.api.response.InterruptedException;
import com.mks.api.response.Item;
import com.mks.api.response.ItemList;
import com.mks.api.response.Response;
import com.mks.api.response.Result;
import com.mks.api.response.SubRoutine;
import com.mks.api.response.SubRoutineIterator;
import com.mks.api.response.WorkItem;
import com.mks.api.response.WorkItemIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public abstract class ResponseWriter {
   public abstract void writeConnection(String var1, int var2, String var3);

   public abstract void startSubRoutine(String var1);

   public abstract void endSubRoutine();

   public abstract void startWorkItem(String var1, String var2, String var3);

   public abstract void startWorkItem(Object var1);

   public abstract void endWorkItem();

   protected abstract void writeItemField(String var1, String var2, String var3, String var4, String var5, List var6);

   protected abstract void writeObjectField(String var1, String var2, Object var3, String var4);

   public abstract void writeItemField(String var1, String var2, String var3, String var4, String var5, Map var6);

   public abstract void writeItemField(String var1, String var2, Object var3, Map var4);

   public abstract void writeItemListField(String var1, String var2, Collection var3);

   public final void writeField(String field, Object value) {
      this.writeField(field, (String)null, value, (String)null);
   }

   public final void writeField(String field, String displayName, Object value) {
      this.writeField(field, displayName, value, (String)null);
   }

   public final void writeField(String field, String displayName, Object value, String displayValue) {
      if (value instanceof Map) {
         this.writeItemField(field, displayName, "fieldmap", (String)null, "Map", (Map)((Map)value));
      } else {
         ArrayList itemFields;
         Iterator it;
         if (value instanceof ItemList) {
            ItemList list = (ItemList)value;
            itemFields = new ArrayList(list.getItemListSize());
            it = list.getItems();

            while(it.hasNext()) {
               itemFields.add(it.next());
            }

            this.writeItemListField(field, displayName, itemFields);
         } else if (value instanceof Item) {
            Item item = (Item)value;
            itemFields = new ArrayList(item.getFieldListSize());
            it = item.getFields();

            while(it.hasNext()) {
               Field itemField = (Field)it.next();
               itemFields.add(itemField);
            }

            this.writeItemField(field, displayName, item.getId(), item.getContext(), item.getModelType(), (List)itemFields);
         } else {
            this.writeObjectField(field, displayName, value, displayValue);
         }
      }

   }

   public final void writeItemField(String field, String itemID, String context, String itemType, Map itemFields) {
      this.writeItemField(field, field, itemID, context, itemType, itemFields);
   }

   public final void writeItemField(String field, Object item, Map itemFields) {
      this.writeItemField(field, field, item, itemFields);
   }

   public abstract void startItemField(String var1, String var2, String var3, String var4, String var5);

   public final void startItemField(String field, String itemID, String context, String modelType) {
      this.startItemField(field, field, itemID, context, modelType);
   }

   public final void startItemField(String field, Object item) {
      this.startItemField(field, field, item);
   }

   public abstract void startItemField(String var1, String var2, Object var3);

   public abstract void endItemField();

   public final void writeItemListField(String field, Collection items) {
      this.writeItemListField(field, field, items);
   }

   public final void writeField(String field, int value) {
      this.writeObjectField(field, (String)null, new Integer(value), (String)null);
   }

   public final void writeField(String field, boolean value) {
      this.writeObjectField(field, (String)null, value, (String)null);
   }

   public final void writeField(String field, float value) {
      this.writeObjectField(field, (String)null, new Float(value), (String)null);
   }

   public final void writeField(String field, double value) {
      this.writeObjectField(field, (String)null, new Double(value), (String)null);
   }

   public final void writeField(String field, long value) {
      this.writeObjectField(field, (String)null, new Long(value), (String)null);
   }

   public abstract void writeResult(String var1, String var2, String var3, String var4, Map var5);

   public final void writeResult(String message, Object resultant) {
      this.writeResult(message, resultant, (Map)null);
   }

   public abstract void writeResult(String var1, Object var2, Map var3);

   public abstract void writeException(Exception var1);

   public final void writeAPIObj(Response response) throws InterruptedException {
      if (response != null) {
         this.write(response.getSubRoutines());
         this.writeConnection(response.getConnectionHostname(), response.getConnectionPort(), response.getConnectionUsername());
         this.write(response.getWorkItems());
         Result result = response.getResult();
         if (result != null) {
            this.writeAPIObj(result);
         }

         APIException ex = response.getAPIException();
         if (ex != null) {
            this.writeException(ex);
         }

      }
   }

   public final void writeAsSubRoutine(Response response) throws InterruptedException {
      if (response != null) {
         this.startSubRoutine(response.getCommandName());
         this.writeAPIObj(response);
         this.endSubRoutine();
      }
   }

   private void write(SubRoutineIterator sri) {
      while(sri.hasNext()) {
         try {
            this.writeAPIObj(sri.next());
         } catch (APIException var3) {
            this.writeAPIObj(sri.getLast());
         }
      }

   }

   private void write(WorkItemIterator wii) {
      while(wii.hasNext()) {
         try {
            this.writeAPIObj(wii.next());
         } catch (APIException var3) {
            this.writeAPIObj(wii.getLast());
         }
      }

   }

   public final void writeAPIObj(SubRoutine sr) {
      if (sr != null) {
         this.startSubRoutine(sr.getRoutine());
         this.write(sr.getSubRoutines());
         this.write(sr.getWorkItems());
         Result result = sr.getResult();
         if (result != null) {
            this.writeAPIObj(result);
         }

         APIException ex = sr.getAPIException();
         if (ex != null) {
            this.writeException(ex);
         }

         this.endSubRoutine();
      }
   }

   public final void writeAPIObj(WorkItem wi) {
      this.writeAPIObj(wi, (Field)null);
   }

   public final void writeAPIObj(WorkItem wi, Field field) {
      if (wi != null) {
         this.startWorkItem(wi);
         this.write(wi.getSubRoutines());
         Iterator it = wi.getFields();

         while(it.hasNext()) {
            this.writeAPIObj((Field)it.next());
         }

         if (field != null) {
            this.writeAPIObj(field);
         }

         Result result = wi.getResult();
         if (result != null) {
            this.writeAPIObj(result);
         }

         APIException ex = wi.getAPIException();
         if (ex != null) {
            this.writeException(ex);
         }

         this.endWorkItem();
      }
   }

   public final void writeAPIObj(Field f) {
      if (f != null) {
         this.writeField(f.getName(), f.getDisplayName(), f.getValue(), f.getValueAsString());
      }

   }

   public final void writeAPIObj(Result r) {
      if (r != null) {
         Item resultant = null;
         HashMap resultFields = new HashMap(r.getFieldListSize());

         try {
            resultant = r.getPrimaryValue();
         } catch (NoSuchElementException var6) {
         }

         Iterator it = r.getFields();

         while(it.hasNext()) {
            Field f = (Field)it.next();
            if (f.getValue() != resultant) {
               resultFields.put(f.getName(), f.getValue());
            }
         }

         this.writeResult(r.getMessage(), resultant, resultFields);
      }
   }
}

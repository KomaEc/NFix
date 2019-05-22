package org.apache.commons.validator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.apache.commons.collections.FastHashMap;

public class Form implements Serializable {
   protected String name = null;
   protected List lFields = new ArrayList();
   /** @deprecated */
   protected FastHashMap hFields = new FastHashMap();
   protected String inherit = null;
   private boolean processed = false;

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void addField(Field f) {
      this.lFields.add(f);
      this.hFields.put(f.getKey(), f);
   }

   public List getFields() {
      return Collections.unmodifiableList(this.lFields);
   }

   public Field getField(String fieldName) {
      return (Field)this.hFields.get(fieldName);
   }

   public boolean containsField(String fieldName) {
      return this.hFields.containsKey(fieldName);
   }

   protected void merge(Form depends) {
      List templFields = new ArrayList();
      Map temphFields = new FastHashMap();
      Iterator dependsIt = depends.getFields().iterator();

      while(dependsIt.hasNext()) {
         Field defaultField = (Field)dependsIt.next();
         if (defaultField != null) {
            String fieldKey = defaultField.getKey();
            if (!this.containsField(fieldKey)) {
               templFields.add(defaultField);
               temphFields.put(fieldKey, defaultField);
            } else {
               Field old = this.getField(fieldKey);
               this.hFields.remove(fieldKey);
               this.lFields.remove(old);
               templFields.add(old);
               temphFields.put(fieldKey, old);
            }
         }
      }

      this.lFields.addAll(0, templFields);
      this.hFields.putAll(temphFields);
   }

   protected void process(Map globalConstants, Map constants, Map forms) {
      if (!this.isProcessed()) {
         int n = 0;
         if (this.isExtending()) {
            Form parent = (Form)forms.get(this.inherit);
            if (parent != null) {
               if (!parent.isProcessed()) {
                  parent.process(constants, globalConstants, forms);
               }

               Iterator i = parent.getFields().iterator();

               while(i.hasNext()) {
                  Field f = (Field)i.next();
                  if (this.hFields.get(f.getKey()) == null) {
                     this.lFields.add(n, f);
                     this.hFields.put(f.getKey(), f);
                     ++n;
                  }
               }
            }
         }

         this.hFields.setFast(true);
         ListIterator i = this.lFields.listIterator(n);

         while(i.hasNext()) {
            Field f = (Field)i.next();
            f.process(globalConstants, constants);
         }

         this.processed = true;
      }
   }

   public String toString() {
      StringBuffer results = new StringBuffer();
      results.append("Form: ");
      results.append(this.name);
      results.append("\n");
      Iterator i = this.lFields.iterator();

      while(i.hasNext()) {
         results.append("\tField: \n");
         results.append(i.next());
         results.append("\n");
      }

      return results.toString();
   }

   ValidatorResults validate(Map params, Map actions, int page) throws ValidatorException {
      return this.validate(params, actions, page, (String)null);
   }

   ValidatorResults validate(Map params, Map actions, int page, String fieldName) throws ValidatorException {
      ValidatorResults results = new ValidatorResults();
      params.put("org.apache.commons.validator.ValidatorResults", results);
      if (fieldName != null) {
         Field field = (Field)this.hFields.get(fieldName);
         if (field == null) {
            throw new ValidatorException("Unknown field " + fieldName + " in form " + this.getName());
         }

         params.put("org.apache.commons.validator.Field", field);
         if (field.getPage() <= page) {
            results.merge(field.validate(params, actions));
         }
      } else {
         Iterator fields = this.lFields.iterator();

         while(fields.hasNext()) {
            Field field = (Field)fields.next();
            params.put("org.apache.commons.validator.Field", field);
            if (field.getPage() <= page) {
               results.merge(field.validate(params, actions));
            }
         }
      }

      return results;
   }

   public boolean isProcessed() {
      return this.processed;
   }

   public String getExtends() {
      return this.inherit;
   }

   public void setExtends(String inherit) {
      this.inherit = inherit;
   }

   public boolean isExtending() {
      return this.inherit != null;
   }

   protected Map getFieldMap() {
      return this.hFields;
   }
}

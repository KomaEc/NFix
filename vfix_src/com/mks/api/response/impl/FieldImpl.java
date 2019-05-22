package com.mks.api.response.impl;

import com.mks.api.common.XMLResponseDef;
import com.mks.api.response.APIError;
import com.mks.api.response.Item;
import com.mks.api.response.modifiable.ModifiableField;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class FieldImpl implements ModifiableField {
   private String name;
   private String displayName;
   private Object value;
   private String displayValue;
   private String dataType;
   private String modelType;
   private static final String INVALID_DATATYPE = "Resultant field must have a \"com.mks.api.response.Item\" datatype.";

   FieldImpl(String name) {
      this(name, name);
   }

   FieldImpl(String name, String displayName) {
      this.name = name;
      this.displayName = displayName;
      this.displayValue = null;
   }

   public String getModelType() {
      return this.modelType;
   }

   public void setModelType(String modelType) {
      this.modelType = modelType;
   }

   public void setDataType(String dataType) {
      if (this.name.equalsIgnoreCase(XMLResponseDef.XML_RESULT_FIELD) && !dataType.equals("com.mks.api.response.Item")) {
         throw new APIError("Resultant field must have a \"com.mks.api.response.Item\" datatype.");
      } else {
         this.dataType = dataType;
      }
   }

   public String getName() {
      return this.name;
   }

   public Object getValue() {
      return this.value;
   }

   public void setValue(Object value) {
      this.value = value;
   }

   public String getDataType() {
      return this.dataType;
   }

   public Boolean getBoolean() {
      if (this.dataType == null) {
         return null;
      } else if (this.dataType.equals("java.lang.Boolean")) {
         return (Boolean)this.value;
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public Date getDateTime() {
      if (this.dataType == null) {
         return null;
      } else if (this.dataType.equals("java.util.Date")) {
         return (Date)this.value;
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public Double getDouble() {
      if (this.dataType == null) {
         return null;
      } else if (this.dataType.equals("java.lang.Double")) {
         return (Double)this.value;
      } else {
         throw new UnsupportedOperationException();
      }
   }

   /** @deprecated */
   public Float getFloat() {
      if (this.dataType == null) {
         return null;
      } else if (this.dataType.equals("java.lang.Float")) {
         return (Float)this.value;
      } else if (this.dataType.equals("java.lang.Double")) {
         return new Float(((Double)this.value).floatValue());
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public Integer getInteger() {
      if (this.dataType == null) {
         return null;
      } else if (this.dataType.equals("java.lang.Integer")) {
         return (Integer)this.value;
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public Long getLong() {
      if (this.dataType == null) {
         return null;
      } else if (this.dataType.equals("java.lang.Long")) {
         return (Long)this.value;
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public String getString() {
      if (this.dataType == null) {
         return null;
      } else if (this.dataType.equals("java.lang.String")) {
         return (String)this.value;
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public String getValueAsString() {
      if (this.displayValue != null) {
         return this.displayValue;
      } else if (this.value == null) {
         return null;
      } else if (this.value instanceof List) {
         StringBuffer sb = new StringBuffer();

         for(Iterator it = ((List)this.value).iterator(); it.hasNext(); sb.append(it.next().toString())) {
            if (sb.length() > 0) {
               sb.append(',');
            }
         }

         return sb.toString();
      } else {
         return this.value.toString();
      }
   }

   public List getList() {
      if (this.dataType == null) {
         return null;
      } else if (!this.dataType.equals("com.mks.api.response.ItemList") && !this.dataType.equals("com.mks.api.response.ValueList")) {
         throw new UnsupportedOperationException();
      } else {
         return (List)this.value;
      }
   }

   public Item getItem() {
      if (this.dataType == null) {
         return null;
      } else if (this.dataType.equals("com.mks.api.response.Item")) {
         return (Item)this.value;
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public void setDisplayValue(String value) {
      this.displayValue = value;
   }

   public String getDisplayName() {
      return this.displayName;
   }
}

package com.mks.api.response.impl;

import com.mks.api.response.modifiable.ModifiableValueList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ValueListImpl extends ArrayList implements ModifiableValueList {
   private String dataType;
   private Map displayValues = new HashMap();

   ValueListImpl() {
   }

   public String getDataType() {
      return this.dataType;
   }

   public void setDataType(String dataType) {
      this.dataType = dataType;
   }

   public void setDisplayValueOf(Object value, String displayValue) {
      if (displayValue != null && value != null) {
         this.displayValues.put(value, displayValue);
      }
   }

   public String getDisplayValueOf(Object value) {
      return (String)this.displayValues.get(value);
   }
}

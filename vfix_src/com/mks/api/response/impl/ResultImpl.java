package com.mks.api.response.impl;

import com.mks.api.IntegrationPointFactory;
import com.mks.api.common.XMLResponseDef;
import com.mks.api.response.Field;
import com.mks.api.response.Item;
import com.mks.api.response.modifiable.ModifiableResult;
import com.mks.api.util.MKSLogger;
import java.util.NoSuchElementException;

public class ResultImpl extends AbstractFieldContainer implements ModifiableResult {
   private String message;

   protected ResultImpl() {
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String msg) {
      this.message = msg;
   }

   public Item getPrimaryValue() {
      Item value = null;
      MKSLogger apiLogger = IntegrationPointFactory.getLogger();
      String msg;
      if (this.contains(XMLResponseDef.XML_RESULT_FIELD)) {
         Field f = this.getField(XMLResponseDef.XML_RESULT_FIELD);
         if (f.getDataType().equals("com.mks.api.response.Item")) {
            return f.getItem();
         }

         String msg = "Result value is not an Item.  It is: " + f.getDataType();
         apiLogger.message((Object)this, "API", 0, msg);
      } else {
         msg = "Result field not present!";
         apiLogger.message((Object)this, "API", 0, msg);
      }

      msg = "No primary value for the Result.";
      apiLogger.message((Object)this, "API", 0, msg);
      throw new NoSuchElementException(msg);
   }
}

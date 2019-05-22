package com.mks.api.response.impl;

import com.mks.api.response.Field;
import com.mks.api.response.modifiable.ModifiableFieldContainer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public abstract class AbstractFieldContainer implements ModifiableFieldContainer {
   private List fields = new ArrayList();
   private Set fieldIds = new HashSet();

   AbstractFieldContainer() {
   }

   public void add(Field field) {
      if (field != null) {
         this.fields.add(field);
         this.fieldIds.add(field.getName().toLowerCase());
      }

   }

   public Field getField(String name) {
      Iterator it = this.fields.iterator();

      Field f;
      do {
         if (!it.hasNext()) {
            throw new NoSuchElementException(name);
         }

         f = (Field)it.next();
      } while(!f.getName().equalsIgnoreCase(name));

      return f;
   }

   public Iterator getFields() {
      return Collections.unmodifiableList(this.fields).iterator();
   }

   public int getFieldListSize() {
      return this.fields.size();
   }

   public boolean contains(String id) {
      return this.fieldIds.contains(id.toLowerCase());
   }
}

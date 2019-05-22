package com.mks.api.response.impl;

import com.mks.api.CmdRunner;
import com.mks.api.IntegrationPointFactory;
import com.mks.api.common.XMLResponseDef;
import com.mks.api.response.APIException;
import com.mks.api.response.APIExceptionFactory;
import com.mks.api.response.APIInternalError;
import com.mks.api.response.Field;
import com.mks.api.response.Item;
import com.mks.api.response.Response;
import com.mks.api.response.Result;
import com.mks.api.response.SubRoutine;
import com.mks.api.response.ValueList;
import com.mks.api.response.WorkItem;
import com.mks.api.response.modifiable.ModifiableField;
import com.mks.api.response.modifiable.ModifiableItem;
import com.mks.api.response.modifiable.ModifiableResult;
import com.mks.api.response.modifiable.ModifiableValueList;
import com.mks.api.response.modifiable.ModifiableWorkItem;
import com.mks.api.util.MKSLogger;
import com.mks.connect.InvalidAppException;
import com.mks.connect.VersionMismatchException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class XMLResponseHandler {
   private static final String READ_START_TAG_MSG = "Read in start tag: <{0}>";
   private static final String CREATING_RESPONSE_MSG = "Creating Response({0}, {1})";
   private static final String CREATING_WORK_ITEM_MSG = "Creating WorkItem({0}, {1}, {2})";
   private static final String CREATING_FIELD_MSG = "Creating Field({0})";
   private static final String SETTING_FIELD_VALUE_MSG = "Setting the Field value to: \"{0}\"";
   private static final String CREATING_ITEM_MSG = "Creating Item({0}, {1}, {2})";
   private static final String PARSING_VALUE_MSG = "Parsing a {0} value: {1}";
   private static final String UNKNOWN_DATA_TYPE_MSG = "Unknown value dataType: {0}";
   private static final String CREATING_LIST_MSG = "Creating a List of type: \"{0}\"";
   private static final String CREATING_RESULT_MSG = "Creating Result()";
   private static final String SETTING_RESULT_DATA_MSG = "Setting Result message value to: \"{0}\"";
   private static final String CREATING_SUB_ROUTINE_MSG = "Creating SubRoutine({0})";
   private static final String CREATING_API_EXCEPTION_MSG = "Creating APIException({0})";
   private static final String SETTING_API_EXCEPTION_DATA_MSG = "Setting APIException message value to: \"{0}\"";
   private static final String PARSING_EXIT_CODE_MSG = "Parsing exit code value: \"{0}\"";
   private static final String DOUBLE_EXCEPTION_MSG = "Trying to parse another Exception tag at the {0} level.";
   private static final String INVALID_DATA_STREAM_ERROR_MSG = "Invalid data stream received from the IntegrationPoint.";
   private ModifiableXMLResponse response;
   private XmlPullParser xpp;
   private InputStream is;
   private XMLResponseFactory responseFactory;
   private int eventType;
   private boolean readResponseConnection;
   private boolean readResponseWorkItemsSelectionType;
   private boolean readWorkItems;
   private boolean readPreSubRoutines;
   private boolean readPostSubRoutines;
   private boolean isInterrupted;
   private MKSLogger apiLogger;

   public XMLResponseHandler(InputStream is, String encoding) {
      this.is = is;
      this.responseFactory = XMLResponseFactory.getInstance();
      this.apiLogger = IntegrationPointFactory.getLogger();

      try {
         XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
         factory.setValidating(false);
         factory.setNamespaceAware(false);
         this.xpp = factory.newPullParser();
         this.xpp.setInput(this.is, encoding);
      } catch (XmlPullParserException var4) {
         this.apiLogger.exception((Object)this, "API", 0, var4);
         APIExceptionFactory.createAPIException("APIInternalError", (String)null);
      }

   }

   public Response getResponse(CmdRunner cmdRunner, String cc, boolean readCompletely) {
      ApplicationConnectionError ace;
      while(this.eventType != 2 && this.eventType != 1) {
         try {
            this.eventType = this.xpp.next();
         } catch (XmlPullParserException var10) {
            this.apiLogger.exception((Object)this, "API", 0, var10);
            APIExceptionFactory.createAPIException("APIInternalError", (String)null);
         } catch (InvalidAppException var11) {
            this.apiLogger.exception((Object)this, "API", 0, var11);
            throw new UnsupportedApplicationError();
         } catch (VersionMismatchException var12) {
            this.apiLogger.exception((Object)this, "API", 0, var12);
            throw new UnsupportedVersionError(var12.getMessage());
         } catch (IOException var13) {
            this.apiLogger.exception((Object)this, "API", 0, var13);
            ace = new ApplicationConnectionError(var13.getMessage());
            throw ace;
         }
      }

      String tagName;
      if (this.eventType == 2) {
         tagName = this.xpp.getName();
         String msg = MessageFormat.format("Read in start tag: <{0}>", tagName);
         this.apiLogger.message((Object)this, "API", 0, msg);
         if (tagName.equals(XMLResponseDef.XML_ROOT_TAG)) {
            String app = this.xpp.getAttributeValue((String)null, XMLResponseDef.XML_COMMAND_APP_ATTR);
            String command = this.xpp.getAttributeValue((String)null, XMLResponseDef.XML_COMMAND_ATTR);
            msg = MessageFormat.format("Creating Response({0}, {1})", app, command);
            this.apiLogger.message((Object)this, "API", 5, msg);
            this.response = this.responseFactory.createXMLResponse(cmdRunner, app, command);
            this.response.setXMLResponseHandler(this);
            ((ResponseImpl)this.response).setCommandString(cc);

            try {
               this.xpp.next();
               this.findStartTag();
            } catch (XmlPullParserException var14) {
               this.apiLogger.exception((Object)this, "API", 0, var14);
               APIExceptionFactory.createAPIException("APIInternalError", (String)null);
            } catch (IOException var15) {
               this.apiLogger.exception((Object)this, "API", 0, var15);
               ApplicationConnectionError ace = new ApplicationConnectionError(var15.getMessage());
               throw ace;
            }
         }

         if (readCompletely) {
            try {
               SubRoutine sr;
               for(tagName = null; (sr = this.getSubRoutine()) != null; this.eventType = this.xpp.next()) {
                  this.response.add(sr);
               }

               this.readResponseConnectionAttributes();
               WorkItem wi;
               if (this.readResponseWorkItemsTagAttributes()) {
                  for(ace = null; (wi = this.getWorkItem()) != null; this.eventType = this.xpp.next()) {
                     this.response.add(wi);
                  }
               }

               this.response.setResult(this.getResult(true));
               this.response.setAPIException(this.getException());
               this.response.setExitCode(this.getExitCode());
            } catch (XmlPullParserException var16) {
               this.apiLogger.exception((Object)this, "API", 0, var16);
               APIExceptionFactory.createAPIException("APIInternalError", (String)null);
            } catch (IOException var17) {
               this.apiLogger.exception((Object)this, "API", 0, var17);
               ace = new ApplicationConnectionError(var17.getMessage());
               throw ace;
            }
         }

         return this.response;
      } else {
         tagName = "Invalid data stream received from the IntegrationPoint.";
         APIInternalError err = new APIInternalError(tagName);
         this.apiLogger.exception((Object)this, "API", 0, err);
         throw err;
      }
   }

   protected void readResponseConnectionAttributes() {
      if (!this.readResponseConnection) {
         String tag = this.xpp.getName();
         if (tag != null && tag.equals(XMLResponseDef.XML_CONNECTION_TAG)) {
            this.response.setConnectionHostname(this.xpp.getAttributeValue((String)null, XMLResponseDef.XML_CONNECTION_HOST_ATTR));

            try {
               this.response.setConnectionPort(Integer.parseInt(this.xpp.getAttributeValue((String)null, XMLResponseDef.XML_CONNECTION_PORT_ATTR)));
            } catch (NumberFormatException var6) {
               this.apiLogger.exception((Object)this, "API", 0, var6);
               APIExceptionFactory.createAPIException("APIInternalError", (String)null);
            }

            this.response.setConnectionUsername(this.xpp.getAttributeValue((String)null, XMLResponseDef.XML_CONNECTION_USER_ATTR));

            try {
               this.eventType = this.xpp.next();
               this.findStartTag();
            } catch (XmlPullParserException var4) {
               this.apiLogger.exception((Object)this, "API", 0, var4);
               APIExceptionFactory.createAPIException("APIInternalError", (String)null);
            } catch (IOException var5) {
               this.apiLogger.exception((Object)this, "API", 0, var5);
               ApplicationConnectionError ace = new ApplicationConnectionError(var5.getMessage());
               throw ace;
            }

            this.readResponseConnection = true;
            this.readPreSubRoutines = true;
         }

      }
   }

   protected boolean readResponseWorkItemsTagAttributes() {
      if (this.readResponseWorkItemsSelectionType) {
         return true;
      } else {
         String tag = this.xpp.getName();
         if (tag != null && tag.equals(XMLResponseDef.XML_SELECTION_TAG)) {
            String selectionType = this.xpp.getAttributeValue((String)null, XMLResponseDef.XML_SELECTION_TYPE_ATTR);
            this.response.setWorkItemSelectionType(selectionType);

            try {
               this.eventType = this.xpp.next();
               this.findStartTag();
            } catch (XmlPullParserException var5) {
               this.apiLogger.exception((Object)this, "API", 0, var5);
               APIExceptionFactory.createAPIException("APIInternalError", (String)null);
            } catch (IOException var6) {
               this.apiLogger.exception((Object)this, "API", 0, var6);
               ApplicationConnectionError ace = new ApplicationConnectionError(var6.getMessage());
               throw ace;
            }

            this.readResponseWorkItemsSelectionType = true;
            this.readResponseConnection = true;
            this.readPreSubRoutines = true;
            return true;
         } else {
            return false;
         }
      }
   }

   protected WorkItem getWorkItem() {
      int et = this.findStartTag();
      String tag = this.xpp.getName();
      if (et != 1 && tag != null && tag.equals(XMLResponseDef.XML_WORKITEM_TAG)) {
         ModifiableWorkItem workItem = null;

         try {
            String id = this.xpp.getAttributeValue((String)null, XMLResponseDef.XML_ID_ATTR);
            String context = this.xpp.getAttributeValue((String)null, XMLResponseDef.XML_CONTEXT_ATTR);
            String modelType = this.xpp.getAttributeValue((String)null, XMLResponseDef.XML_MODELTYPE_ATTR);
            String msg = MessageFormat.format("Creating WorkItem({0}, {1}, {2})", id, context, modelType);
            this.apiLogger.message((Object)this, "API", 5, msg);
            workItem = this.responseFactory.createWorkItem(id, context, modelType);
            if (workItem instanceof ItemImpl) {
               this.addItemContextAttributes((ItemImpl)workItem);
            }

            boolean workItemExceptionRead = false;
            int eventType = Integer.MIN_VALUE;

            while(true) {
               eventType = this.xpp.next();
               String tagName = this.xpp.getName();
               if (eventType == 3 && tagName.equals(XMLResponseDef.XML_WORKITEM_TAG)) {
                  break;
               }

               if (eventType == 2) {
                  if (tagName.equals(XMLResponseDef.XML_SUBOPERATION_TAG)) {
                     workItem.add(this.getSubRoutine());
                  } else if (tagName.equals(XMLResponseDef.XML_FIELD_TAG)) {
                     workItem.add(this.getField());
                  } else if (tagName.equals(XMLResponseDef.XML_RESULT_TAG)) {
                     workItem.setResult(this.getResult(false));
                  } else if (tagName.equals(XMLResponseDef.XML_EXCEPTION_TAG)) {
                     if (!workItemExceptionRead) {
                        workItem.setAPIException(this.getException());
                        workItemExceptionRead = true;
                     } else {
                        String logMsg = MessageFormat.format("Trying to parse another Exception tag at the {0} level.", "WorkItem");
                        this.apiLogger.message((Object)this, "API", 0, logMsg);
                     }
                  }
               }
            }
         } catch (XmlPullParserException var12) {
            this.apiLogger.exception((Object)this, "API", 0, var12);
            APIExceptionFactory.createAPIException("APIInternalError", (String)null);
         } catch (IOException var13) {
            this.apiLogger.exception((Object)this, "API", 0, var13);
            ApplicationConnectionError ace = new ApplicationConnectionError(var13.getMessage());
            throw ace;
         }

         return workItem;
      } else {
         return null;
      }
   }

   protected Field getField() {
      String tag = this.xpp.getName();
      if (tag != null && tag.equals(XMLResponseDef.XML_FIELD_TAG)) {
         ModifiableField field = null;

         try {
            String name = this.xpp.getAttributeValue((String)null, XMLResponseDef.XML_NAME_ATTR);
            String displayName = this.xpp.getAttributeValue((String)null, XMLResponseDef.XML_DISP_NAME_ATTR);
            String msg = MessageFormat.format("Creating Field({0})", name);
            this.apiLogger.message((Object)this, "API", 5, msg);
            if (displayName != null) {
               field = this.responseFactory.createField(name, displayName);
            } else {
               field = this.responseFactory.createField(name);
            }

            int eventType = Integer.MIN_VALUE;

            while(true) {
               eventType = this.xpp.next();
               String tagName = this.xpp.getName();
               if (eventType == 3 && tagName.equals(XMLResponseDef.XML_FIELD_TAG)) {
                  break;
               }

               if (eventType == 2) {
                  if (tagName.equals(XMLResponseDef.XML_VALUE_TAG)) {
                     Object[] values = this.getValue(field);
                     msg = MessageFormat.format("Setting the Field value to: \"{0}\"", values[0]);
                     this.apiLogger.message((Object)this, "API", 10, msg);
                     field.setValue(values[0]);
                     if (values.length > 1) {
                        field.setDisplayValue(values[1].toString());
                     }
                  } else if (tagName.equals(XMLResponseDef.XML_LIST_TAG)) {
                     field.setValue(this.getList(field));
                  } else if (tagName.equals(XMLResponseDef.XML_SUBITEM_TAG)) {
                     field.setValue(this.getItem());
                     field.setDataType("com.mks.api.response.Item");
                  }
               }
            }
         } catch (XmlPullParserException var9) {
            this.apiLogger.exception((Object)this, "API", 0, var9);
            APIExceptionFactory.createAPIException("APIInternalError", (String)null);
         } catch (IOException var10) {
            this.apiLogger.exception((Object)this, "API", 0, var10);
            ApplicationConnectionError ace = new ApplicationConnectionError(var10.getMessage());
            throw ace;
         }

         return field;
      } else {
         return null;
      }
   }

   protected Item getItem() {
      String tag = this.xpp.getName();
      if (tag != null && tag.equals(XMLResponseDef.XML_SUBITEM_TAG)) {
         String id = this.xpp.getAttributeValue((String)null, XMLResponseDef.XML_ID_ATTR);
         String context = this.xpp.getAttributeValue((String)null, XMLResponseDef.XML_CONTEXT_ATTR);
         String modelType = this.xpp.getAttributeValue((String)null, XMLResponseDef.XML_MODELTYPE_ATTR);
         String msg = MessageFormat.format("Creating Item({0}, {1}, {2})", id, context, modelType);
         this.apiLogger.message((Object)this, "API", 5, msg);
         ModifiableItem item = this.responseFactory.createItem(id, context, modelType);
         if (item instanceof ItemImpl) {
            this.addItemContextAttributes((ItemImpl)item);
         }

         while(true) {
            int eventType = Integer.MIN_VALUE;

            try {
               eventType = this.xpp.next();
            } catch (XmlPullParserException var10) {
               this.apiLogger.exception((Object)this, "API", 0, var10);
               APIExceptionFactory.createAPIException("APIInternalError", (String)null);
            } catch (IOException var11) {
               this.apiLogger.exception((Object)this, "API", 0, var11);
               ApplicationConnectionError ace = new ApplicationConnectionError(var11.getMessage());
               throw ace;
            }

            String tagName = this.xpp.getName();
            if (eventType == 3 && tagName.equals(XMLResponseDef.XML_SUBITEM_TAG)) {
               return item;
            }

            if (eventType == 2 && tagName.equals(XMLResponseDef.XML_FIELD_TAG)) {
               item.add(this.getField());
            }
         }
      } else {
         return null;
      }
   }

   protected Object[] getValue(ModifiableField f) {
      String tag = this.xpp.getName();
      if (tag != null && tag.equals(XMLResponseDef.XML_VALUE_TAG)) {
         Object tokValObj = null;
         String dispObj = null;

         try {
            String dataType = this.xpp.getAttributeValue((String)null, XMLResponseDef.XML_DATATYPE_ATTR);
            String modelType = this.xpp.getAttributeValue((String)null, XMLResponseDef.XML_MODELTYPE_ATTR);
            int eventType = Integer.MIN_VALUE;
            String prevTag = tag;
            eventType = this.xpp.next();
            String currTag = this.xpp.getName();

            while(true) {
               if (eventType == 4) {
                  String text = this.xpp.getText();
                  eventType = this.xpp.next();
                  currTag = this.xpp.getName();
                  if (eventType == 3) {
                     if ((currTag.equals(XMLResponseDef.XML_VALUE_TAG) || currTag.equals(XMLResponseDef.XML_TOKEN_VALUE_TAG)) && prevTag.equals(currTag)) {
                        tokValObj = this.parseValue(text, dataType, modelType, f);
                     } else if (currTag.equals(XMLResponseDef.XML_DISPLAY_VALUE_TAG) && prevTag.equals(currTag)) {
                        dispObj = text;
                     }
                  }
               }

               if (currTag.equals(XMLResponseDef.XML_VALUE_TAG)) {
                  break;
               }

               prevTag = currTag;
               eventType = this.xpp.next();
               currTag = this.xpp.getName();
            }
         } catch (XmlPullParserException var11) {
            this.apiLogger.exception((Object)this, "API", 0, var11);
            APIExceptionFactory.createAPIException("APIInternalError", (String)null);
         } catch (IOException var12) {
            this.apiLogger.exception((Object)this, "API", 0, var12);
            ApplicationConnectionError ace = new ApplicationConnectionError(var12.getMessage());
            throw ace;
         }

         return dispObj == null ? new Object[]{tokValObj} : new Object[]{tokValObj, dispObj};
      } else {
         return new Object[]{null};
      }
   }

   private Object parseValue(String value, String dataType, String modelType, ModifiableField f) {
      Object objValue = null;
      f.setModelType(modelType);
      String msg = MessageFormat.format("Parsing a {0} value: {1}", dataType, value);
      this.apiLogger.message((Object)this, "API", 10, msg);
      if (dataType == null) {
         objValue = null;
      } else if (dataType.equals(XMLResponseDef.XML_DATATYPE_STRING)) {
         objValue = value;
      } else if (dataType.equals(XMLResponseDef.XML_DATATYPE_BOOLEAN)) {
         objValue = new Boolean(value);
      } else if (dataType.equals(XMLResponseDef.XML_DATATYPE_TIMESTAMP)) {
         SimpleDateFormat sdf = new SimpleDateFormat();
         sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
         sdf.applyPattern("yyyy-MM-dd'T'HH:mm:ss");
         objValue = sdf.parse(value, new ParsePosition(0));
      } else if (dataType.equals(XMLResponseDef.XML_DATATYPE_DOUBLE)) {
         try {
            objValue = new Double(value);
         } catch (NumberFormatException var11) {
            this.apiLogger.exception((Object)this, "API", 0, var11);
            APIExceptionFactory.createAPIException("APIInternalError", (String)null);
         }
      } else if (dataType.equals(XMLResponseDef.XML_DATATYPE_FLOAT)) {
         try {
            objValue = new Float(value);
         } catch (NumberFormatException var10) {
            this.apiLogger.exception((Object)this, "API", 0, var10);
            APIExceptionFactory.createAPIException("APIInternalError", (String)null);
         }
      } else if (dataType.equals(XMLResponseDef.XML_DATATYPE_INT)) {
         try {
            objValue = new Integer(value);
         } catch (NumberFormatException var9) {
            this.apiLogger.exception((Object)this, "API", 0, var9);
            APIExceptionFactory.createAPIException("APIInternalError", (String)null);
         }
      } else if (dataType.equals(XMLResponseDef.XML_DATATYPE_LONG)) {
         try {
            objValue = new Long(value);
         } catch (NumberFormatException var8) {
            this.apiLogger.exception((Object)this, "API", 0, var8);
            APIExceptionFactory.createAPIException("APIInternalError", (String)null);
         }
      } else {
         msg = MessageFormat.format("Unknown value dataType: {0}", dataType);
         this.apiLogger.message((Object)this, "API", 0, msg);
         APIExceptionFactory.createAPIException("APIInternalError", msg);
      }

      if (objValue != null) {
         f.setDataType(objValue.getClass().getName());
      } else {
         f.setDataType((String)null);
      }

      return objValue;
   }

   protected List getList(ModifiableField f) {
      String tag = this.xpp.getName();
      if (tag != null && tag.equals(XMLResponseDef.XML_LIST_TAG)) {
         String type = this.xpp.getAttributeValue((String)null, XMLResponseDef.XML_ELEMENT_TYPE_ATTR);
         if (type == null) {
            type = "value";
         }

         String msg = MessageFormat.format("Creating a List of type: \"{0}\"", type);
         this.apiLogger.message((Object)this, "API", 5, msg);
         List list = null;
         if (type != null && type.equals(XMLResponseDef.XML_ITEMLIST_ATTR)) {
            list = this.responseFactory.createItemList();
         } else {
            list = this.responseFactory.createValueList();
         }

         while(true) {
            int eventType = Integer.MIN_VALUE;

            try {
               eventType = this.xpp.next();
            } catch (XmlPullParserException var10) {
               this.apiLogger.exception((Object)this, "API", 0, var10);
               APIExceptionFactory.createAPIException("APIInternalError", (String)null);
            } catch (IOException var11) {
               this.apiLogger.exception((Object)this, "API", 0, var11);
               ApplicationConnectionError ace = new ApplicationConnectionError(var11.getMessage());
               throw ace;
            }

            String tagName = this.xpp.getName();
            if (eventType == 3 && tagName.equals(XMLResponseDef.XML_LIST_TAG)) {
               if (type != null && type.equals(XMLResponseDef.XML_ITEMLIST_ATTR)) {
                  f.setDataType("com.mks.api.response.ItemList");
               } else {
                  f.setDataType("com.mks.api.response.ValueList");
               }

               return (List)list;
            }

            if (eventType == 2) {
               if (tagName.equals(XMLResponseDef.XML_VALUE_TAG)) {
                  Object[] valueSet = this.getValue(f);
                  Object value = valueSet[0];
                  if (((ValueList)list).getDataType() == null && value != null) {
                     ((ModifiableValueList)list).setDataType(value.getClass().getName());
                  }

                  ((List)list).add(value);
                  if (list instanceof ModifiableValueList && valueSet.length > 1) {
                     ((ModifiableValueList)list).setDisplayValueOf(value, (String)valueSet[1]);
                  }
               } else if (tagName.equals(XMLResponseDef.XML_SUBITEM_TAG)) {
                  ((List)list).add(this.getItem());
               }
            }
         }
      } else {
         return null;
      }
   }

   protected Result getResult(boolean mainResult) {
      int et = this.findStartTag();
      String tag = this.xpp.getName();
      if (et != 1 && tag != null && tag.equals(XMLResponseDef.XML_RESULT_TAG)) {
         this.apiLogger.message((Object)this, "API", 5, "Creating Result()");
         ModifiableResult result = this.responseFactory.createResult();

         try {
            int eventType = Integer.MIN_VALUE;

            while(true) {
               eventType = this.xpp.next();
               String tagName = this.xpp.getName();
               if (eventType == 3 && tagName.equals(XMLResponseDef.XML_RESULT_TAG)) {
                  break;
               }

               if (eventType == 2) {
                  if (tagName.equals(XMLResponseDef.XML_FIELD_TAG)) {
                     result.add(this.getField());
                  } else if (tagName.equals(XMLResponseDef.XML_MESSAGE_TAG)) {
                     String msg = this.xpp.nextText();
                     String logMsg = MessageFormat.format("Setting Result message value to: \"{0}\"", msg);
                     this.apiLogger.message((Object)this, "API", 10, logMsg);
                     result.setMessage(msg);
                  }
               }
            }
         } catch (XmlPullParserException var9) {
            this.apiLogger.exception((Object)this, "API", 0, var9);
            APIExceptionFactory.createAPIException("APIInternalError", (String)null);
         } catch (IOException var10) {
            this.apiLogger.exception((Object)this, "API", 0, var10);
            ApplicationConnectionError ace = new ApplicationConnectionError(var10.getMessage());
            throw ace;
         }

         if (mainResult) {
            this.readPreSubRoutines = true;
            this.readPostSubRoutines = true;
         }

         return result;
      } else {
         return null;
      }
   }

   protected SubRoutine getSubRoutine() {
      int et = this.findStartTag();
      String tag = this.xpp.getName();
      if (et != 1 && tag != null && tag.equals(XMLResponseDef.XML_SUBOPERATION_TAG)) {
         ModifiableXMLSubRoutine subRoutine = null;

         try {
            String routine = this.xpp.getAttributeValue((String)null, XMLResponseDef.XML_SUBOPERATION_ATTR);
            String msg = MessageFormat.format("Creating SubRoutine({0})", routine);
            this.apiLogger.message((Object)this, "API", 5, msg);
            subRoutine = this.responseFactory.createXMLSubRoutine(routine);
            subRoutine.setXMLResponseHandler(this);
            boolean subRoutineExceptionRead = false;

            while(true) {
               int eventType = Integer.MIN_VALUE;
               eventType = this.xpp.next();
               String tagName = this.xpp.getName();
               if (eventType == 3 && tagName.equals(XMLResponseDef.XML_SUBOPERATION_TAG)) {
                  break;
               }

               if (eventType == 2) {
                  if (tagName.equals(XMLResponseDef.XML_SUBOPERATION_TAG)) {
                     subRoutine.add(this.getSubRoutine());
                  } else {
                     String logMsg;
                     if (tagName.equals(XMLResponseDef.XML_SELECTION_TAG)) {
                        logMsg = this.xpp.getAttributeValue((String)null, XMLResponseDef.XML_SELECTION_TYPE_ATTR);
                        subRoutine.setWorkItemSelectionType(logMsg);
                     } else if (tagName.equals(XMLResponseDef.XML_WORKITEM_TAG)) {
                        subRoutine.add(this.getWorkItem());
                     } else if (tagName.equals(XMLResponseDef.XML_RESULT_TAG)) {
                        subRoutine.setResult(this.getResult(false));
                     } else if (tagName.equals(XMLResponseDef.XML_EXCEPTION_TAG)) {
                        if (!subRoutineExceptionRead) {
                           subRoutine.setAPIException(this.getException());
                           subRoutineExceptionRead = true;
                        } else {
                           logMsg = MessageFormat.format("Trying to parse another Exception tag at the {0} level.", "SubRoutine");
                           this.apiLogger.message((Object)this, "API", 0, logMsg);
                        }
                     }
                  }
               }
            }
         } catch (XmlPullParserException var10) {
            this.apiLogger.exception((Object)this, "API", 0, var10);
            APIExceptionFactory.createAPIException("APIInternalError", (String)null);
         } catch (IOException var11) {
            this.apiLogger.exception((Object)this, "API", 0, var11);
            ApplicationConnectionError ace = new ApplicationConnectionError(var11.getMessage());
            throw ace;
         }

         return subRoutine;
      } else {
         return null;
      }
   }

   protected APIException getException() {
      int et = this.findStartTag();
      String tag = this.xpp.getName();
      if (et != 1 && tag != null && tag.equals(XMLResponseDef.XML_EXCEPTION_TAG)) {
         String exName = this.xpp.getAttributeValue((String)null, XMLResponseDef.XML_EXCEPTIONCLASS_ATTR);
         String logMsg = MessageFormat.format("Creating APIException({0})", exName);
         this.apiLogger.message((Object)this, "API", 5, logMsg);
         APIException apiException = APIExceptionFactory.createAPIException(exName, (Response)this.response);

         try {
            int eventType = Integer.MIN_VALUE;

            while(true) {
               eventType = this.xpp.next();
               String tagName = this.xpp.getName();
               if (eventType == 3 && tagName.equals(XMLResponseDef.XML_EXCEPTION_TAG)) {
                  break;
               }

               if (eventType == 2) {
                  if (tagName.equals(XMLResponseDef.XML_FIELD_TAG)) {
                     apiException.add(this.getField());
                  } else if (tagName.equals(XMLResponseDef.XML_MESSAGE_TAG)) {
                     String msg = this.xpp.nextText();
                     logMsg = MessageFormat.format("Setting APIException message value to: \"{0}\"", msg);
                     this.apiLogger.message((Object)this, "API", 10, logMsg);
                     apiException.setMessage(msg);
                  }
               }
            }
         } catch (XmlPullParserException var9) {
            this.apiLogger.exception((Object)this, "API", 0, var9);
            APIExceptionFactory.createAPIException("APIInternalError", (String)null);
         } catch (IOException var10) {
            this.apiLogger.exception((Object)this, "API", 0, var10);
            ApplicationConnectionError ace = new ApplicationConnectionError(var10.getMessage());
            throw ace;
         }

         return apiException;
      } else {
         return null;
      }
   }

   protected int getExitCode() {
      int et = this.findStartTag();
      int exitCode = Integer.MIN_VALUE;
      String tag = this.xpp.getName();
      if (et != 1 && tag != null && tag.equals(XMLResponseDef.XML_EXITCODE_TAG)) {
         try {
            String strExitCode = this.xpp.nextText();
            String msg = MessageFormat.format("Parsing exit code value: \"{0}\"", strExitCode);
            this.apiLogger.message((Object)this, "API", 10, msg);
            exitCode = Integer.parseInt(strExitCode);

            for(et = this.xpp.getEventType(); et != 1; et = this.xpp.next()) {
            }
         } catch (NumberFormatException var6) {
            this.apiLogger.exception((Object)this, "API", 0, var6);
            APIExceptionFactory.createAPIException("APIInternalError", (String)null);
         } catch (XmlPullParserException var7) {
            this.apiLogger.exception((Object)this, "API", 0, var7);
            APIExceptionFactory.createAPIException("APIInternalError", (String)null);
         } catch (IOException var8) {
            this.apiLogger.exception((Object)this, "API", 0, var8);
            ApplicationConnectionError ace = new ApplicationConnectionError(var8.getMessage());
            throw ace;
         }

         return exitCode;
      } else {
         return exitCode;
      }
   }

   protected void interrupt() {
      if (!this.isInterrupted) {
         try {
            this.is.close();
            this.isInterrupted = true;
            this.readPreSubRoutines = true;
            this.readWorkItems = true;
            this.readPostSubRoutines = true;
         } catch (IOException var2) {
            this.apiLogger.exception((Object)this, "API", 0, var2);
         }

      }
   }

   private int findStartTag() {
      if (this.isInterrupted) {
         return 1;
      } else {
         try {
            int et = this.xpp.getEventType();

            while(et != 2 && et != 1) {
               et = this.xpp.next();
               if (et == 3 && this.xpp.getName().equals(XMLResponseDef.XML_SELECTION_TAG)) {
                  this.readWorkItems = true;
               }
            }

            return et;
         } catch (XmlPullParserException var3) {
            this.apiLogger.exception((Object)this, "API", 0, var3);
            APIExceptionFactory.createAPIException("APIInternalError", (String)null);
            return 1;
         } catch (IOException var4) {
            this.apiLogger.exception((Object)this, "API", 0, var4);
            ApplicationConnectionError ace = new ApplicationConnectionError(var4.getMessage());
            throw ace;
         }
      }
   }

   private void addItemContextAttributes(ItemImpl item) {
      int numAttributes = this.xpp.getAttributeCount();

      for(int i = 0; i < numAttributes; ++i) {
         String attributeName = this.xpp.getAttributeName(i);
         String attributeValue = this.xpp.getAttributeValue(i);
         item.addContext(attributeName, attributeValue);
      }

   }

   protected boolean getReadPreSubRoutines() {
      return this.readPreSubRoutines;
   }

   protected boolean getReadWorkItems() {
      return this.readWorkItems;
   }

   protected boolean getReadPostSubRoutines() {
      return this.readPostSubRoutines;
   }

   protected boolean isInterrupted() {
      return this.isInterrupted;
   }
}

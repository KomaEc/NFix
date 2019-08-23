/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.pdfbox.pdmodel.interactive.form;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;

import org.apache.pdfbox.pdmodel.common.COSArrayList;

/**
 * Radio button fields contain a set of related buttons that can each be on or off.
 *
 * @author sug
 */
public final class PDRadioButton extends PDButton
{
    /**
     * A Ff flag.
     */
    public static final int FLAG_RADIOS_IN_UNISON = 1 << 25;

    /**
     * @param theAcroForm The acroForm for this field.
     * @param field The field that makes up the radio collection.
     *
     * {@inheritDoc}
     */
    public PDRadioButton(PDAcroForm theAcroForm, COSDictionary field)
    {
        super(theAcroForm,field);
    }

    /**
     * From the PDF Spec <br/>
     * If set, a group of radio buttons within a radio button field that
     * use the same value for the on state will turn on and off in unison; that is if
     * one is checked, they are all checked. If clear, the buttons are mutually exclusive
     * (the same behavior as HTML radio buttons).
     *
     * @param radiosInUnison The new flag for radiosInUnison.
     */
    public void setRadiosInUnison(boolean radiosInUnison)
    {
        getDictionary().setFlag( COSName.FF, FLAG_RADIOS_IN_UNISON, radiosInUnison );
    }

    /**
     *
     * @return true If the flag is set for radios in unison.
     */
    public boolean isRadiosInUnison()
    {
        return getDictionary().getFlag( COSName.FF, FLAG_RADIOS_IN_UNISON );
    }

    /**
     * This setValue method iterates the collection of radiobuttons
     * and checks or unchecks each radiobutton according to the
     * given value.
     * If the value is not represented by any of the radiobuttons,
     * then none will be checked.
     *
     * {@inheritDoc}
     */
    @Override
    public void setValue(String value) throws IOException
    {
        getDictionary().setString( COSName.V, value );
        List kids = getKids();
        for (Object kid : kids)
        {
            PDField field = (PDField) kid;
            if ( field instanceof PDCheckbox )
            {
                PDCheckbox btn = (PDCheckbox)field;
                if( btn.getOnValue().equals(value) )
                {
                    btn.check();
                }
                else
                {
                    btn.unCheck();
                }
            }
        }
    }

    /**
     * getValue gets the fields value to as a string.
     *
     * @return The string value of this field.
     *
     * @throws IOException If there is an error getting the value.
     */
    @Override
    public String getValue() throws IOException
    {
        String retval = null;
        List kids = getKids();
        for (Object kid : kids)
        {
            PDField field = (PDField) kid;
            if ( field instanceof PDCheckbox )
            {
                PDCheckbox btn = (PDCheckbox)field;
                if( btn.isChecked() )
                {
                    retval = btn.getOnValue();
                }
            }
        }
        if( retval == null )
        {
            retval = getDictionary().getNameAsString( COSName.V );
        }
        return retval;
    }


    /**
     * This will return a list of PDField objects that are part of this radio collection.
     *
     * @see PDField#getWidget()
     * @return A list of PDWidget objects.
     * @throws IOException if there is an error while creating the children objects.
     */
    @SuppressWarnings("unchecked")
    public List getKids() throws IOException
    {
        COSArray kids = (COSArray)getDictionary().getDictionaryObject(COSName.KIDS);
        if( kids != null )
        {
            List kidsList = new ArrayList();
            for (int i = 0; i < kids.size(); i++)
            {
                PDField field = PDFieldFactory.createField( getAcroForm(), (COSDictionary)kids.getObject(i) );
                if (field != null)
                {
                    kidsList.add( field );
                }
            }
            return new COSArrayList( kidsList, kids );
        }
        else
        {
            return new ArrayList();
        }
    }
}
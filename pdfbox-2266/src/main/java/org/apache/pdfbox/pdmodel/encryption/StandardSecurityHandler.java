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
package org.apache.pdfbox.pdmodel.encryption;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
 
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocument;

/**
 * The standard security handler. This security handler protects document with password.
 * @see StandardProtectionPolicy to see how to protect document with this security handler.
 * @author Ben Litchfield
 * @author Benoit Guillon
 * @author Manuel Kasper
 */
public final class StandardSecurityHandler extends SecurityHandler
{
    /** Type of security handler. */
    public static final String FILTER = "Standard";

    /** Protection policy class for this handler. */
    public static final Class<?> PROTECTION_POLICY_CLASS = StandardProtectionPolicy.class;

    /** Standard padding for encryption. */
    public static final byte[] ENCRYPT_PADDING =
    {
        (byte)0x28, (byte)0xBF, (byte)0x4E, (byte)0x5E, (byte)0x4E,
        (byte)0x75, (byte)0x8A, (byte)0x41, (byte)0x64, (byte)0x00,
        (byte)0x4E, (byte)0x56, (byte)0xFF, (byte)0xFA, (byte)0x01,
        (byte)0x08, (byte)0x2E, (byte)0x2E, (byte)0x00, (byte)0xB6,
        (byte)0xD0, (byte)0x68, (byte)0x3E, (byte)0x80, (byte)0x2F,
        (byte)0x0C, (byte)0xA9, (byte)0xFE, (byte)0x64, (byte)0x53,
        (byte)0x69, (byte)0x7A
    };

    // hashes used for Algorithm 2.B, depending on remainder from E modulo 3
    private static final String[] HASHES_2B = new String[] {"SHA-256", "SHA-384", "SHA-512"};

    private static final int DEFAULT_VERSION = 1;
    private static final int DEFAULT_REVISION = 3;

    private int revision = DEFAULT_REVISION;
    private StandardProtectionPolicy policy;
    private RC4Cipher rc4 = new RC4Cipher();

    /**
     * Constructor.
     */
    public StandardSecurityHandler()
    {
    }

    /**
     * Constructor used for encryption.
     *
     * @param p The protection policy.
     */
    public StandardSecurityHandler(StandardProtectionPolicy p)
    {
        policy = p;
        keyLength = policy.getEncryptionKeyLength();
    }

    /**
     * Computes the version number of the StandardSecurityHandler
     * regarding the encryption key length.
     * See PDF Spec 1.6 p 93
     *
     * @return The computed cersion number.
     */
    private int computeVersionNumber()
    {
        if(keyLength == 40)
        {
            return DEFAULT_VERSION;
        }
        else if(keyLength == 256)
        {
            return 5;
        }

        return 2;
    }

    /**
     * Computes the revision version of the StandardSecurityHandler to
     * use regarding the version number and the permissions bits set.
     * See PDF Spec 1.6 p98
     *
     * @return The computed revision number.
     */
    private int computeRevisionNumber()
    {
        if(version < 2 && !policy.getPermissions().hasAnyRevision3PermissionSet())
        {
            return 2;
        }
        if (version == 5)
        {
            return 6;    // note about revision 5: "Shall not be used. This value was used by a deprecated Adobe extension."
        }
        if ( version == 2 || version == 3 || policy.getPermissions().hasAnyRevision3PermissionSet())
        {
            return 3;
        }
        return 4;
    }

    /**
     * Decrypt the document.
     *
     * @param doc The document to be decrypted.
     * @param decryptionMaterial Information used to decrypt the document.
     *
     * @throws IOException If there is an error accessing data.
     */
    public void decryptDocument(PDDocument doc, DecryptionMaterial decryptionMaterial)
        throws IOException
    {
        document = doc;

        PDEncryption dictionary = document.getEncryption();
        COSArray documentIDArray = document.getDocument().getDocumentID();
        
        prepareForDecryption(dictionary, documentIDArray, decryptionMaterial);
        
        proceedDecryption();
    }

    /**
     * Prepares everything to decrypt the document.
     *
     * Called from {@link #decryptDocument(PDDocument, DecryptionMaterial)}.
     * Only if decryption of single objects is needed this should be called instead.
     *
     * @param encryption  encryption dictionary
     * @param documentIDArray  document id
     * @param decryptionMaterial Information used to decrypt the document.
     *
     * @throws IOException If there is an error accessing data.
     */
    public void prepareForDecryption(PDEncryption encryption, COSArray documentIDArray,
                                     DecryptionMaterial decryptionMaterial)
                                     throws IOException
    {
        if(!(decryptionMaterial instanceof StandardDecryptionMaterial))
        {
            throw new IOException("Decryption material is not compatible with the document");
        }
        decryptMetadata = encryption.isEncryptMetaData();

        StandardDecryptionMaterial material = (StandardDecryptionMaterial)decryptionMaterial;

        String password = material.getPassword();
        if(password == null)
        {
            password = "";
        }

        int dicPermissions = encryption.getPermissions();
        int dicRevision = encryption.getRevision();
        int dicLength = encryption.getVersion() == 1 ? 5 : encryption.getLength() / 8;

        //some documents may have not document id, see
        //test\encryption\encrypted_doc_no_id.pdf
        byte[] documentIDBytes = null;
        if( documentIDArray != null && documentIDArray.size() >= 1 )
        {
            COSString id = (COSString)documentIDArray.getObject( 0 );
            documentIDBytes = id.getBytes();
        }
        else
        {
            documentIDBytes = new byte[0];
        }

        // we need to know whether the meta data was encrypted for password calculation
        boolean encryptMetadata = encryption.isEncryptMetaData();
        
        byte[] userKey = encryption.getUserKey();
        byte[] ownerKey = encryption.getOwnerKey();
        byte[] ue = null, oe = null;

        String passwordCharset = "ISO-8859-1";
        if (dicRevision == 6)
        {
            passwordCharset = "UTF-8";
            ue = encryption.getUserEncryptionKey();
            oe = encryption.getOwnerEncryptionKey();
        }

        if( isOwnerPassword(password.getBytes(passwordCharset), userKey, ownerKey,
                                 dicPermissions, documentIDBytes, dicRevision,
                                 dicLength, encryptMetadata) )
        {
            currentAccessPermission = AccessPermission.getOwnerAccessPermission();
            
            byte[] computedPassword;
            if (dicRevision == 6)
            {
                computedPassword = password.getBytes(passwordCharset);
            }
            else
            {
                computedPassword = getUserPassword(password.getBytes(passwordCharset),
                        ownerKey, dicRevision, dicLength );
            }
            
            encryptionKey =
                computeEncryptedKey(
                    computedPassword,
                    ownerKey, userKey, oe, ue,
                    dicPermissions,
                    documentIDBytes,
                    dicRevision,
                    dicLength,
                    encryptMetadata, true );
        }
        else if( isUserPassword(password.getBytes(passwordCharset), userKey, ownerKey,
                           dicPermissions, documentIDBytes, dicRevision,
                           dicLength, encryptMetadata) )
        {
            currentAccessPermission = new AccessPermission( dicPermissions );
            encryptionKey =
                computeEncryptedKey(
                    password.getBytes(passwordCharset),
                    ownerKey, userKey, oe, ue,
                    dicPermissions,
                    documentIDBytes,
                    dicRevision,
                    dicLength,
                    encryptMetadata, false );
        }
        else
        {
            throw new IOException("Cannot decrypt PDF, the password is incorrect");
        }

        if (dicRevision == 6)
        {
            // Algorithm 13: validate permissions ("Perms" field)
            try
            {
                Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
                cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(encryptionKey, "AES"));
                byte[] perms = cipher.doFinal(encryption.getPerms());
                
                if (perms[9] != 'a' || perms[10] != 'd' || perms[11] != 'b')
                {
                    throw new IOException("Verification of permissions failed (constant)");
                }

                int permsP = perms[0] & 0xFF | perms[1] & 0xFF << 8 | perms[2] & 0xFF << 16 |
                        perms[3] & 0xFF << 24;
                
                if (permsP != dicPermissions)
                {
                    throw new IOException("Verification of permissions failed (" + permsP +
                            " != " + dicPermissions + ")");
                }
                
                if (encryptMetadata && perms[8] != 'T' || !encryptMetadata && perms[8] != 'F')
                {
                    throw new IOException("Verification of permissions failed (EncryptMetadata)");
                }
            }
            catch (GeneralSecurityException e)
            {
                throw new IOException(e);
            }
        }

        // detect whether AES encryption is used. This assumes that the encryption algo is 
        // stored in the PDCryptFilterDictionary
        PDCryptFilterDictionary stdCryptFilterDictionary = encryption.getStdCryptFilterDictionary();

        if (stdCryptFilterDictionary != null)
        {
            COSName cryptFilterMethod = stdCryptFilterDictionary.getCryptFilterMethod();
            if (cryptFilterMethod != null) 
            {
                setAES("AESV2".equalsIgnoreCase(cryptFilterMethod.getName()) ||
                       "AESV3".equalsIgnoreCase(cryptFilterMethod.getName()));
            }
        }
    }
    
    /**
     * Prepare document for encryption.
     *
     * @param doc The documeent to encrypt.
     *
     * @throws IOException If there is an error accessing data.
     */
    public void prepareDocumentForEncryption(PDDocument doc) throws IOException
    {
        document = doc;
        PDEncryption encryptionDictionary = document.getEncryption();
        if(encryptionDictionary == null)
        {
            encryptionDictionary = new PDEncryption();
        }
        version = computeVersionNumber();
        revision = computeRevisionNumber();
        encryptionDictionary.setFilter(FILTER);
        encryptionDictionary.setVersion(version);
        encryptionDictionary.setRevision(revision);
        encryptionDictionary.setLength(keyLength);

        String ownerPassword = policy.getOwnerPassword();
        String userPassword = policy.getUserPassword();
        if( ownerPassword == null )
        {
            ownerPassword = "";
        }
        if( userPassword == null )
        {
            userPassword = "";
        }
 
        // If no owner password is set, use the user password instead.
        if (ownerPassword.isEmpty())
        {
            ownerPassword = userPassword;
        }

        int permissionInt = policy.getPermissions().getPermissionBytes();

        encryptionDictionary.setPermissions(permissionInt);

        int length = keyLength/8;

        if (revision == 6)
        {
            try
            {
                SecureRandom rnd = new SecureRandom();
                Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");

                // make a random 256-bit file encryption key
                encryptionKey = new byte[32];
                rnd.nextBytes(encryptionKey);
                
                // Algorithm 8a: Compute U
                byte[] userPasswordBytes = truncate127(userPassword.getBytes("UTF-8"));
                byte[] userValidationSalt = new byte[8];
                byte[] userKeySalt = new byte[8];
                rnd.nextBytes(userValidationSalt);
                rnd.nextBytes(userKeySalt);
                byte[] hashU = computeHash2B(concat(userPasswordBytes, userValidationSalt),
                        userPasswordBytes, null);
                byte[] u = concat(hashU, userValidationSalt, userKeySalt);
                
                // Algorithm 8b: Compute UE
                byte[] hashUE = computeHash2B(concat(userPasswordBytes, userKeySalt),
                        userPasswordBytes, null);
                cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(hashUE, "AES"),
                        new IvParameterSpec(new byte[16]));
                byte[] ue = cipher.doFinal(encryptionKey);
                
                // Algorithm 9a: Compute O
                byte[] ownerPasswordBytes = truncate127(ownerPassword.getBytes("UTF-8"));
                byte[] ownerValidationSalt = new byte[8];
                byte[] ownerKeySalt = new byte[8];
                rnd.nextBytes(ownerValidationSalt);
                rnd.nextBytes(ownerKeySalt);
                byte[] hashO = computeHash2B(concat(ownerPasswordBytes, ownerValidationSalt, u),
                        ownerPasswordBytes, u);
                byte[] o = concat(hashO, ownerValidationSalt, ownerKeySalt);
                
                // Algorithm 9b: Compute OE
                byte[] hashOE = computeHash2B(concat(ownerPasswordBytes, ownerKeySalt, u),
                        ownerPasswordBytes, u);
                cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(hashOE, "AES"),
                        new IvParameterSpec(new byte[16]));
                byte[] oe = cipher.doFinal(encryptionKey);
                
                // Set keys and other required constants in encryption dictionary
                encryptionDictionary.setUserKey(u);
                encryptionDictionary.setUserEncryptionKey(ue);
                encryptionDictionary.setOwnerKey(o);
                encryptionDictionary.setOwnerEncryptionKey(oe);
                
                PDCryptFilterDictionary cryptFilterDictionary = new PDCryptFilterDictionary();
                cryptFilterDictionary.setCryptFilterMethod(COSName.AESV3);
                cryptFilterDictionary.setLength(keyLength);
                encryptionDictionary.setStdCryptFilterDictionary(cryptFilterDictionary);
                encryptionDictionary.setStreamFilterName(COSName.STD_CF);
                encryptionDictionary.setStringFilterName(COSName.STD_CF);
                setAES(true);
                
                // Algorithm 10: compute "Perms" value
                byte[] perms = new byte[16];
                perms[0] = (byte)permissionInt;
                perms[1] = (byte)(permissionInt >>> 8);
                perms[2] = (byte)(permissionInt >>> 16);
                perms[3] = (byte)(permissionInt >>> 24);
                perms[4] = perms[5] = perms[6] = perms[7] = (byte)0xFF;
                perms[8] = 'T';    // we always use EncryptMetadata == true
                perms[9] = 'a';
                perms[10] = 'd';
                perms[11] = 'b';
                for (int i = 12; i <= 15; i++)
                {
                    perms[i] = (byte)rnd.nextInt();
                }
                
                cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptionKey, "AES"),
                        new IvParameterSpec(new byte[16]));

                byte[] permsEnc = cipher.doFinal(perms);
                
                encryptionDictionary.setPerms(permsEnc);
            }
            catch (GeneralSecurityException e)
            {
                throw new IOException(e);
            }
        }
        else
        {
            COSArray idArray = document.getDocument().getDocumentID();

            //check if the document has an id yet.  If it does not then
            //generate one
            if( idArray == null || idArray.size() < 2 )
            {
                MessageDigest md = MessageDigests.getMD5();
                BigInteger time = BigInteger.valueOf( System.currentTimeMillis() );
                md.update( time.toByteArray() );
                md.update( ownerPassword.getBytes("ISO-8859-1") );
                md.update( userPassword.getBytes("ISO-8859-1") );
                md.update( document.getDocument().toString().getBytes() );

                byte[] id = md.digest( this.toString().getBytes("ISO-8859-1") );
                COSString idString = new COSString();
                idString.append( id );

                idArray = new COSArray();
                idArray.add( idString );
                idArray.add( idString );
                document.getDocument().setDocumentID( idArray );
            }

            COSString id = (COSString)idArray.getObject( 0 );

            byte[] ownerBytes = computeOwnerPassword(
                ownerPassword.getBytes("ISO-8859-1"),
                userPassword.getBytes("ISO-8859-1"), revision, length);

            byte[] userBytes = computeUserPassword(
                userPassword.getBytes("ISO-8859-1"),
                ownerBytes, permissionInt, id.getBytes(), revision, length, true);

            encryptionKey = computeEncryptedKey(userPassword.getBytes("ISO-8859-1"), ownerBytes,
                    null, null, null, permissionInt, id.getBytes(), revision, length, true, false);

            encryptionDictionary.setOwnerKey(ownerBytes);
            encryptionDictionary.setUserKey(userBytes);
        }

        document.setEncryptionDictionary( encryptionDictionary );
        document.getDocument().setEncryptionDictionary(encryptionDictionary.getCOSDictionary());
    }

    /**
     * Check for owner password.
     *
     * @param ownerPassword The owner password.
     * @param user The u entry of the encryption dictionary.
     * @param owner The o entry of the encryption dictionary.
     * @param permissions The set of permissions on the document.
     * @param id The document id.
     * @param encRevision The encryption algorithm revision.
     * @param length The encryption key length.
     * @param encryptMetadata The encryption metadata
     *
     * @return True If the ownerPassword param is the owner password.
     *
     * @throws IOException If there is an error accessing data.
     */
    public boolean isOwnerPassword(byte[] ownerPassword, byte[] user, byte[] owner,
                                   int permissions, byte[] id, int encRevision, int length,
                                   boolean encryptMetadata) throws IOException
    {
        if (encRevision == 6)
        {            
            ownerPassword = truncate127(ownerPassword);
            
            byte[] oHash = new byte[32];
            byte[] oValidationSalt = new byte[8];
            System.arraycopy(owner, 0, oHash, 0, 32);
            System.arraycopy(owner, 32, oValidationSalt, 0, 8);
            
            byte[] hash = computeHash2A(ownerPassword, oValidationSalt, user);
            return Arrays.equals(hash, oHash);
        }
        else if (encRevision == 5)
        {
            // Shall not be used. This value was used by a deprecated Adobe extension.
            throw new IOException("Unsupported Encryption Revision " + encRevision);
        }
        else
        {
            byte[] userPassword = getUserPassword( ownerPassword, owner, encRevision, length );
            return isUserPassword( userPassword, user, owner, permissions, id, encRevision, length,
                                   encryptMetadata );
        }
    }

    /**
     * Get the user password based on the owner password.
     *
     * @param ownerPassword The plaintext owner password.
     * @param owner The o entry of the encryption dictionary.
     * @param encRevision The encryption revision number.
     * @param length The key length.
     *
     * @return The u entry of the encryption dictionary.
     *
     * @throws IOException If there is an error accessing data while generating the user password.
     */
    public byte[] getUserPassword( byte[] ownerPassword,  byte[] owner, int encRevision,
                                   long length ) throws IOException
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();

        byte[] ownerPadded = truncateOrPad( ownerPassword );

        MessageDigest md = MessageDigests.getMD5();
        md.update( ownerPadded );
        byte[] digest = md.digest();

        if( encRevision == 3 || encRevision == 4 )
        {
            for( int i=0; i<50; i++ )
            {
                md.reset();
                md.update( digest );
                digest = md.digest();
            }
        }

        byte[] rc4Key = new byte[ (int)length ];
        System.arraycopy( digest, 0, rc4Key, 0, (int)length );

        if( encRevision == 2 )
        {
            rc4.setKey( rc4Key );
            rc4.write( owner, result );
        }
        else if( encRevision == 3 || encRevision == 4)
        {
            byte[] iterationKey = new byte[ rc4Key.length ];
            byte[] otemp = new byte[ owner.length ]; //sm
            System.arraycopy( owner, 0, otemp, 0, owner.length ); //sm
            rc4.write( owner, result);//sm

            for( int i=19; i>=0; i-- )
            {
                System.arraycopy( rc4Key, 0, iterationKey, 0, rc4Key.length );
                for( int j=0; j< iterationKey.length; j++ )
                {
                    iterationKey[j] = (byte)(iterationKey[j] ^ (byte)i);
                }
                rc4.setKey( iterationKey );
                result.reset();  //sm
                rc4.write( otemp, result ); //sm
                otemp = result.toByteArray(); //sm
            }
        }
        return result.toByteArray();
    }

    /**
     * Compute the encryption key.
     *
     * @param password The password to compute the encrypted key.
     * @param o The O entry of the encryption dictionary.
     * @param u The U entry of the encryption dictionary.
     * @param oe The OE entry of the encryption dictionary.
     * @param ue The UE entry of the encryption dictionary.
     * @param permissions The permissions for the document.
     * @param id The document id.
     * @param encRevision The revision of the encryption algorithm.
     * @param length The length of the encryption key.
     * @param encryptMetadata The encryption metadata
     * @param isOwnerPassword whether the password given is the owner password (for revision 6)
     *
     * @return The encrypted key bytes.
     *
     * @throws IOException If there is an error with encryption.
     */
    public byte[] computeEncryptedKey(byte[] password, byte[] o, byte[] u, byte[] oe, byte[] ue,
                                      int permissions, byte[] id, int encRevision, int length,
                                      boolean encryptMetadata, boolean isOwnerPassword)
                                      throws IOException
    {
        byte[] result = new byte[ length ];
        
        if (encRevision == 6)
        {
            //Algorithm 2.A, based on SHA-2 and AES
            
            byte[] hash, fileKeyEnc;
            if (isOwnerPassword)
            {
                byte[] oKeySalt = new byte[8];
                System.arraycopy(o, 40, oKeySalt, 0, 8);
                hash = computeHash2A(password, oKeySalt, u);
                fileKeyEnc = oe;
            }
            else
            {
                byte[] uKeySalt = new byte[8];
                System.arraycopy(u, 40, uKeySalt, 0, 8);
                hash = computeHash2A(password, uKeySalt, null);
                fileKeyEnc = ue;
            }
            
            try
            {
                Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
                cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(hash, "AES"),
                        new IvParameterSpec(new byte[16]));
                result = cipher.doFinal(fileKeyEnc);
            }
            catch (GeneralSecurityException e)
            {
                throw new IOException(e);
            }
        }
        else
        {
            //Algorithm 2, based on MD5

            //PDFReference 1.4 pg 78
            byte[] padded = truncateOrPad( password );

            MessageDigest md = MessageDigests.getMD5();
            md.update( padded );

            md.update( o );

            md.update( (byte)permissions );
            md.update( (byte)(permissions >>> 8));
            md.update( (byte)(permissions >>> 16));
            md.update( (byte)(permissions >>> 24));

            md.update( id );

            //(Security handlers of revision 4 or greater) If document metadata is not being
            // encrypted, pass 4 bytes with the value 0xFFFFFFFF to the MD5 hash function.
            //see 7.6.3.3 Algorithm 2 Step f of PDF 32000-1:2008
            if( encRevision == 4 && !encryptMetadata)
            {
                md.update(new byte[]{(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff});
            }
            byte[] digest = md.digest();

            if( encRevision == 3 || encRevision == 4)
            {
                for( int i=0; i<50; i++ )
                {
                    md.reset();
                    md.update( digest, 0, length );
                    digest = md.digest();
                }
            }

            System.arraycopy( digest, 0, result, 0, length );
        }

        return result;
    }

    /**
     * This will compute the user password hash.
     *
     * @param password The plain text password.
     * @param owner The owner password hash.
     * @param permissions The document permissions.
     * @param id The document id.
     * @param encRevision The revision of the encryption.
     * @param length The length of the encryption key.
     * @param encryptMetadata The encryption metadata
     *
     * @return The user password.
     *
     * @throws IOException if the password could not be computed
     */
    public byte[] computeUserPassword(byte[] password, byte[] owner, int permissions,
                                      byte[] id, int encRevision, int length,
                                      boolean encryptMetadata) throws IOException
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] encryptionKey = computeEncryptedKey( password, owner, null, null, null, permissions,
                id, encRevision, length, encryptMetadata, true );

        if( encRevision == 2 )
        {
            rc4.setKey( encryptionKey );
            rc4.write( ENCRYPT_PADDING, result );
        }
        else if( encRevision == 3 || encRevision == 4 )
        {
            MessageDigest md = MessageDigests.getMD5();
            md.update( ENCRYPT_PADDING );

            md.update( id );
            result.write( md.digest() );

            byte[] iterationKey = new byte[ encryptionKey.length ];
            for( int i=0; i<20; i++ )
            {
                System.arraycopy( encryptionKey, 0, iterationKey, 0, iterationKey.length );
                for( int j=0; j< iterationKey.length; j++ )
                {
                    iterationKey[j] = (byte)(iterationKey[j] ^ i);
                }
                rc4.setKey( iterationKey );
                ByteArrayInputStream input = new ByteArrayInputStream( result.toByteArray() );
                result.reset();
                rc4.write( input, result );
            }

            byte[] finalResult = new byte[32];
            System.arraycopy( result.toByteArray(), 0, finalResult, 0, 16 );
            System.arraycopy( ENCRYPT_PADDING, 0, finalResult, 16, 16 );
            result.reset();
            result.write( finalResult );
        }
        return result.toByteArray();
    }

    /**
     * Compute the owner entry in the encryption dictionary.
     *
     * @param ownerPassword The plaintext owner password.
     * @param userPassword The plaintext user password.
     * @param encRevision The revision number of the encryption algorithm.
     * @param length The length of the encryption key.
     *
     * @return The o entry of the encryption dictionary.
     *
     * @throws IOException if the owner password could not be computed
     */
    public byte[] computeOwnerPassword(byte[] ownerPassword, byte[] userPassword,
                                       int encRevision,  int length ) throws IOException
    {
        byte[] ownerPadded = truncateOrPad( ownerPassword );

        MessageDigest md = MessageDigests.getMD5();
        md.update( ownerPadded );
        byte[] digest = md.digest();

        if( encRevision == 3 || encRevision == 4)
        {
            for( int i=0; i<50; i++ )
            {
                md.reset();
                md.update( digest, 0, length );
                digest = md.digest();
            }
        }
        if( encRevision == 2 && length != 5 )
        {
            throw new IOException("Expected length=5 actual=" + length );
        }

        byte[] rc4Key = new byte[ length ];
        System.arraycopy( digest, 0, rc4Key, 0, length );
        byte[] paddedUser = truncateOrPad( userPassword );

        rc4.setKey( rc4Key );
        ByteArrayOutputStream encrypted = new ByteArrayOutputStream();
        rc4.write( new ByteArrayInputStream( paddedUser ), encrypted );

        if( encRevision == 3 || encRevision == 4 )
        {
            byte[] iterationKey = new byte[ rc4Key.length ];
            for( int i=1; i<20; i++ )
            {
                System.arraycopy( rc4Key, 0, iterationKey, 0, rc4Key.length );
                for( int j=0; j< iterationKey.length; j++ )
                {
                    iterationKey[j] = (byte)(iterationKey[j] ^ (byte)i);
                }
                rc4.setKey( iterationKey );
                ByteArrayInputStream input = new ByteArrayInputStream( encrypted.toByteArray() );
                encrypted.reset();
                rc4.write( input, encrypted );
            }
        }

        return encrypted.toByteArray();
    }

    /**
     * This will take the password and truncate or pad it as necessary.
     *
     * @param password The password to pad or truncate.
     *
     * @return The padded or truncated password.
     */
    private byte[] truncateOrPad( byte[] password )
    {
        byte[] padded = new byte[ ENCRYPT_PADDING.length ];
        int bytesBeforePad = Math.min( password.length, padded.length );
        System.arraycopy( password, 0, padded, 0, bytesBeforePad );
        System.arraycopy( ENCRYPT_PADDING, 0, padded, bytesBeforePad,
                          ENCRYPT_PADDING.length-bytesBeforePad );
        return padded;
    }

    /**
     * Check if a plaintext password is the user password.
     *
     * @param password The plaintext password.
     * @param user The u entry of the encryption dictionary.
     * @param owner The o entry of the encryption dictionary.
     * @param permissions The permissions set in the the PDF.
     * @param id The document id used for encryption.
     * @param encRevision The revision of the encryption algorithm.
     * @param length The length of the encryption key.
     * @param encryptMetadata The encryption metadata
     *
     * @return true If the plaintext password is the user password.
     *
     * @throws IOException If there is an error accessing data.
     */
    public boolean isUserPassword(byte[] password, byte[] user, byte[] owner, int permissions,
                                  byte[] id, int encRevision, int length, boolean encryptMetadata)
                                  throws IOException
    {
        byte[] passwordBytes = computeUserPassword( password, owner, permissions, id, encRevision,
                                                    length, encryptMetadata );
        if( encRevision == 2 )
        {
            return Arrays.equals(user, passwordBytes);
        }
        else if( encRevision == 3 || encRevision == 4 )
        {
            // compare first 16 bytes only
            return Arrays.equals(Arrays.copyOf(user, 16), Arrays.copyOf(passwordBytes, 16));
        }
        else if (encRevision == 6)
        {
            password = truncate127(password);
            
            byte[] uHash = new byte[32];
            byte[] uValidationSalt = new byte[8];
            System.arraycopy(user, 0, uHash, 0, 32);
            System.arraycopy(user, 32, uValidationSalt, 0, 8);
            
            byte[] hash = computeHash2A(password, uValidationSalt, null);
            return Arrays.equals(hash, uHash);
        }
        else
        {
            throw new IOException( "Unknown Encryption Revision " + encRevision );
        }
    }

    /**
     * Check if a plaintext password is the user password.
     *
     * @param password The plaintext password.
     * @param user The u entry of the encryption dictionary.
     * @param owner The o entry of the encryption dictionary.
     * @param permissions The permissions set in the the PDF.
     * @param id The document id used for encryption.
     * @param encRevision The revision of the encryption algorithm.
     * @param length The length of the encryption key.
     * @param encryptMetadata The encryption metadata
     *
     * @return true If the plaintext password is the user password.
     *
     * @throws IOException If there is an error accessing data.
     */
    public boolean isUserPassword(String password, byte[] user, byte[] owner, int permissions,
                                  byte[] id, int encRevision,  int length, boolean encryptMetadata)
                                  throws IOException
    {
        if (encRevision == 6)
        {
            return isUserPassword(password.getBytes("UTF-8"), user, owner, permissions, id,
                    encRevision, length, encryptMetadata);
        }
        else
        {
            return isUserPassword(password.getBytes("ISO-8859-1"), user, owner, permissions, id,
                    encRevision, length, encryptMetadata);
        }
    }

    /**
     * Check for owner password.
     *
     * @param password The owner password.
     * @param user The u entry of the encryption dictionary.
     * @param owner The o entry of the encryption dictionary.
     * @param permissions The set of permissions on the document.
     * @param id The document id.
     * @param encRevision The encryption algorithm revision.
     * @param length The encryption key length.
     * @param encryptMetadata The encryption metadata
     *
     * @return True If the ownerPassword param is the owner password.
     *
     * @throws IOException If there is an error accessing data.
     */
    public boolean isOwnerPassword(String password, byte[] user, byte[] owner, int permissions,
                                   byte[] id, int encRevision, int length, boolean encryptMetadata)
                                   throws IOException
    {
        return isOwnerPassword(password.getBytes("ISO-8859-1"), user,owner,permissions, id,
                               encRevision, length, encryptMetadata);
    }

    // Algorithm 2.A from ISO 32000-1
    private byte[] computeHash2A(byte[] password, byte[] salt, byte[] u) throws IOException
    {
        password = truncate127(password);
        
        if (u == null)
        {
            u = new byte[0];
        }
        else if (u.length < 48)
        {
            throw new IOException("Bad U length");
        }
        else if (u.length > 48)
        {
            // must truncate
            byte[] uTrunc = new byte[48];
            System.arraycopy(u, 0, uTrunc, 0, 48);
            u = uTrunc;
        }
        
        byte[] input = concat(password, salt, u);
        return computeHash2B(input, password, u);
    }
    
    // Algorithm 2.B from ISO 32000-2
    private static byte[] computeHash2B(byte[] input, byte[] password, byte[] userKey)
            throws IOException
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] k = md.digest(input);
            
            byte[] e = null;
            for (int round = 0; round < 64 || ((int)e[e.length-1] & 0xFF) > round - 32; round++)
            {                
                byte[] k1;
                if (userKey != null && userKey.length >= 48)
                {
                    k1 = new byte[64*(password.length + k.length + 48)];
                }
                else
                {
                    k1 = new byte[64*(password.length + k.length)];
                }
                
                int pos = 0;
                for (int i = 0; i < 64; i++)
                {
                    System.arraycopy(password, 0, k1, pos, password.length);
                    pos += password.length;
                    System.arraycopy(k, 0, k1, pos, k.length);
                    pos += k.length;
                    if (userKey != null && userKey.length >= 48)
                    {
                        System.arraycopy(userKey, 0, k1, pos, 48);
                        pos += 48;
                    }
                }
                
                byte[] kFirst = new byte[16];
                byte[] kSecond = new byte[16];
                System.arraycopy(k, 0, kFirst, 0, 16);
                System.arraycopy(k, 16, kSecond, 0, 16);
                
                Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
                SecretKeySpec keySpec = new SecretKeySpec(kFirst, "AES");
                IvParameterSpec ivSpec = new IvParameterSpec(kSecond);
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
                e = cipher.doFinal(k1);
                
                byte[] eFirst = new byte[16];
                System.arraycopy(e, 0, eFirst, 0, 16);
                BigInteger bi = new BigInteger(1, eFirst);
                BigInteger remainder = bi.mod(new BigInteger("3"));
                String nextHash = HASHES_2B[remainder.intValue()];
                
                md = MessageDigest.getInstance(nextHash);
                k = md.digest(e);
            }
            
            if (k.length > 32)
            {
                byte[] kTrunc = new byte[32];
                System.arraycopy(k, 0, kTrunc, 0, 32);
                return kTrunc;
            }
            else
            {
                return k;
            }            
        }
        catch (GeneralSecurityException e)
        {
            throw new IOException(e);
        }
    }
    
    private static byte[] concat(byte[] a, byte[] b)
    {
        byte[] o = new byte[a.length + b.length];
        System.arraycopy(a, 0, o, 0, a.length);
        System.arraycopy(b, 0, o, a.length, b.length);
        return o;
    }
    
    private static byte[] concat(byte[] a, byte[] b, byte[] c)
    {
        byte[] o = new byte[a.length + b.length + c.length];
        System.arraycopy(a, 0, o, 0, a.length);
        System.arraycopy(b, 0, o, a.length, b.length);
        System.arraycopy(c, 0, o, a.length + b.length, c.length);
        return o;
    }
    
    private static byte[] truncate127(byte[] in)
    {
        if (in.length <= 127)
        {
            return in;
        }
        byte[] trunc = new byte[127];
        System.arraycopy(in, 0, trunc, 0, 127);
        return trunc;
    }
}

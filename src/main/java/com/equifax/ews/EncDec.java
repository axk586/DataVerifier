package com.equifax.ews;

import org.apache.beam.sdk.io.FileSystems;
import org.apache.beam.sdk.util.MimeTypes;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.PBESecretKeyDecryptor;
import org.bouncycastle.openpgp.operator.PublicKeyDataDecryptorFactory;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPDigestCalculatorProviderBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyDataDecryptorFactoryBuilder;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.security.Security;
import java.util.Base64;
import java.util.Iterator;

public class EncDec {
    public static void main(String[] args) throws Exception {
        decryptFile();
    }

    public void encryptFile() {

    }

    public static void decryptFile() throws Exception {

        String inputFile = "C:\\Dev\\EWS-Project\\BigFiles\\Billable_570.txt.gpg";
        String privatekey = "C:\\Users\\axk586\\Downloads\\0x0B9BC7E5-sec.asc";
        String publicKey = "C:\\Users\\axk586\\Downloads\\0x0B9BC7E5-pub.asc";
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            OutputStream plainTextFileIs = null;
            try {
                System.out.println("570 mb file");
                long start_time = System.currentTimeMillis();
                System.out.println("Start time " + start_time);
                File inpFile = new File(inputFile);
                InputStream inpFileStream = new FileInputStream(inpFile);
                InputStream in = org.bouncycastle.openpgp.PGPUtil.getDecoderStream(inpFileStream);

                PGPObjectFactory pgpF = new PGPObjectFactory(in);
                PGPEncryptedDataList enc;
                Object o = pgpF.nextObject();
                //
                // the first object might be a PGP marker packet.
                //
                if (o instanceof PGPEncryptedDataList) {
                    enc = (PGPEncryptedDataList) o;
                } else {
                    enc = (PGPEncryptedDataList) pgpF.nextObject();
                }
                //
                // find the secret key
                //
                if (enc != null) {
                    Iterator<PGPPublicKeyEncryptedData> it = enc.getEncryptedDataObjects();

                    PGPPrivateKey sKey = null;
                    PGPPublicKeyEncryptedData pbe = null;

                    File privateFile = new File(privatekey);
                    InputStream inpstream = new FileInputStream(privateFile);

                    while (sKey == null && it.hasNext()) {
                        pbe = it.next();
                        sKey = findSecretKey(inpstream, pbe.getKeyID(), "OneTwoThree".toCharArray());
                    }

                    if (sKey == null) {
                        throw new IllegalArgumentException("Secret key for message not found.");
                    }

                    PublicKeyDataDecryptorFactory b = new JcePublicKeyDataDecryptorFactoryBuilder().setProvider("BC")
                            .setContentProvider("BC").build(sKey);

                    if (b != null && pbe != null) {

                        InputStream clear = pbe.getDataStream(b);
                        if (clear != null) {
                            PGPObjectFactory plainFact = new PGPObjectFactory(clear);

                            Object message = plainFact.nextObject();

                            if (message instanceof PGPCompressedData) {
                                PGPCompressedData cData = (PGPCompressedData) message;
                                PGPObjectFactory pgpFact = new PGPObjectFactory(cData.getDataStream(), null);

                                message = pgpFact.nextObject();
                            }

                            if (message instanceof PGPLiteralData) {

//                                WritableByteChannel writableByteChannel = FileSystems
//                                        .create(FileSystems.matchNewResource(outDecryptedFile, false), MimeTypes.TEXT);
                                try {
//                                    plainTextFileIs = Channels.newOutputStream(writableByteChannel);

                                    PGPLiteralData ld = (PGPLiteralData) message;
                                    InputStream inputStream = ld.getInputStream();
//
                                    File file = new File("src/main/resources/Billable_570.txt");
                                    try(OutputStream outputStream = new FileOutputStream(file)){
                                        IOUtils.copy(inputStream, outputStream);
                                    }
                                } finally {
                                    if (plainTextFileIs != null)
                                        plainTextFileIs.close();
                                }

                            } else if (message instanceof PGPOnePassSignatureList) {
                                throw new PGPException("Encrypted message contains a signed message - not literal data.");
                            } else {
                                throw new PGPException("Message is not a simple encrypted file - type unknown.");
                            }

                            if (pbe.isIntegrityProtected() && !pbe.verify()) {
                                throw new PGPException("Message failed integrity check");
                            }
                        }
                        long end_time = System.currentTimeMillis();
                        System.out.println("End time " + end_time);
                        long totalTime = end_time - start_time;
                        System.out.println("Total time in second = " +totalTime/1000 +" seconds");
                    }
                } else {
                    throw new Exception(
                            "Error in PGP File encryption/decryption. A different key might have used for enc/desc ");
                }
            } catch (Exception ex) {
                throw ex;
            } finally {

            }
    }

    private static PGPPrivateKey findSecretKey(InputStream keyIn, long keyID, char[] pass)
            throws IOException, PGPException {

        PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(
                org.bouncycastle.openpgp.PGPUtil.getDecoderStream(keyIn));
        PGPSecretKey pgpSecKey = pgpSec.getSecretKey(keyID);

        if (pgpSecKey == null) {
            return null;
        }

        PBESecretKeyDecryptor a = new JcePBESecretKeyDecryptorBuilder(
                new JcaPGPDigestCalculatorProviderBuilder().setProvider("BC").build()).setProvider("BC").build(pass);

        return pgpSecKey.extractPrivateKey(a);
    }
}

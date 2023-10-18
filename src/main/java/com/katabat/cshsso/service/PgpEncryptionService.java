package com.katabat.cshsso.service;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.security.Security;

import java.util.Base64;
import java.util.Date;

import org.bouncycastle.bcpg.ArmoredOutputStream;

import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;

import org.springframework.stereotype.Service;


/**
 * DOCUMENT ME!
 *
 * @author   $author$
 * @version  $Revision$, $Date$
 */
@Service public class PgpEncryptionService {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  static {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   data             DOCUMENT ME!
   * @param   publicKeyString  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   *
   * @throws  Exception  DOCUMENT ME!
   */
  public String encryptAndBase64Encode(String data, String publicKeyString) throws Exception {
    byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
    byte[] encryptedData  = encrypt(data, publicKeyBytes);

    return Base64.getEncoder().encodeToString(encryptedData);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  static private PGPPublicKey readPublicKey(InputStream input) throws Exception {
    PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(PGPUtil.getDecoderStream(input),
        new JcaKeyFingerprintCalculator());

    for (PGPPublicKeyRing keyRing : pgpPub) {
      for (PGPPublicKey key : keyRing) {
        if (key.isEncryptionKey()) {
          return key;
        }
      }
    }

    throw new IllegalArgumentException("Can't find encryption key in key ring.");
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  private byte[] encrypt(String data, byte[] publicKeyBytes) throws Exception {
    InputStream  keyIn  = new ByteArrayInputStream(publicKeyBytes);
    PGPPublicKey pubKey = readPublicKey(keyIn);

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try(ArmoredOutputStream armoredOut = new ArmoredOutputStream(out)) {
      PGPEncryptedDataGenerator encGen = new PGPEncryptedDataGenerator(
          new JcePGPDataEncryptorBuilder(PGPEncryptedData.CAST5).setWithIntegrityPacket(true).setSecureRandom(
            new java.security.SecureRandom()).setProvider("BC"));

      encGen.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(pubKey).setProvider("BC"));

      try(ByteArrayOutputStream bytesOut = new ByteArrayOutputStream()) {
        PGPCompressedDataGenerator compressedDataGenerator = new PGPCompressedDataGenerator(PGPCompressedData.ZIP);

        try {
          PGPLiteralDataGenerator literalDataGenerator = new PGPLiteralDataGenerator();

          try(OutputStream literalOut = literalDataGenerator.open(compressedDataGenerator.open(bytesOut),
                    PGPLiteralData.BINARY, PGPLiteralData.CONSOLE, data.getBytes().length, new Date())) {
            literalOut.write(data.getBytes());
          }
        } finally {
          compressedDataGenerator.close();
        }

        byte[] bytes = bytesOut.toByteArray();

        try(OutputStream encryptedOut = encGen.open(armoredOut, bytes.length)) {
          encryptedOut.write(bytes);
        }
      }
    }

    return out.toByteArray();
  }
}

package com.opd.phonenumberapi;

import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Service
public class DataCryptService {
    private final SecretKey key;
    private final IvParameterSpec ivk;
    private final String algorithm = "AES/CBC/PKCS5Padding";
    private final byte[] byteKeyCrypt;

    private final Path IVpath = Path.of("IV");

    private final Path keyPath = Path.of("KEY");
    public DataCryptService() {
        try {
            this.byteKeyCrypt = putKeyFile();
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        this.ivk = new IvParameterSpec(byteKeyCrypt);
        try {
            this.key = readKey();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private SecretKey readKey() throws IOException {
        byte[] encoded = Files.readAllBytes(keyPath);
        return new SecretKeySpec(encoded, "AES");
    }

    public SecretKey getKey(byte[] iv) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator  = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = new SecureRandom(iv);
        keyGenerator.init(128,secureRandom);
        return keyGenerator.generateKey();
    }
    private byte[] putKeyFile() throws IOException, NoSuchAlgorithmException {
        if (!Files.isRegularFile(IVpath) || !Files.isRegularFile(keyPath)){
            createKeyFile();
            System.out.println("The key was not found, a new key was generated");
            System.exit(1);
        }
        return readIV();
    }
    public byte[] readIV() throws IOException{
        return Files.readAllBytes(IVpath);
    }
    public void createKeyFile() throws IOException, NoSuchAlgorithmException {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);

        SecretKey key = getKey(iv);
        try (FileOutputStream stream = new FileOutputStream(IVpath.toString())) {
            stream.write(iv);
        }
        try (FileOutputStream stream = new FileOutputStream(keyPath.toString())) {
            stream.write(key.getEncoded());
        }

    }

    public String encrypt(String info) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivk);
        byte[] cryptText = cipher.doFinal(info.getBytes());
        return Base64.getEncoder().encodeToString(cryptText);
    }
    public String decrypt(String info) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, ivk);
        byte[] decryptText = cipher.doFinal(Base64.getDecoder().decode(info));
        return new String(decryptText);
    }
}
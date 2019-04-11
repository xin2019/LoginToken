package Model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.*;
import java.security.interfaces.RSAPublicKey;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;

public class RSAUtils {
//    private static final KeyPair keyPair=initKey();
    private static String RSAKeyStore = "F:/RSAKey.txt";

    /**
     * 初始化方法，产生key pair，提供provider和random
     * @return KeyPair instance
     */
    private static KeyPair initKey() {
        try{
            //添加provider
            Provider provider=new org.bouncycastle.jce.provider.BouncyCastleProvider();
            Security.addProvider(provider);

            //产生用于安全加密的随机数
            SecureRandom secureRandom=new SecureRandom();
            KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance("RSA",provider);
            keyPairGenerator.initialize(1024,secureRandom);
            KeyPair keyPair=keyPairGenerator.generateKeyPair();
//            System.out.println(keyPair.getPrivate());
//            System.out.println(keyPair.getPublic());
           saveKeyPair(keyPair);
            return keyPair;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

     /**
     * 产生public key
     * @return public key字符串
     */
    public static String generateBase64PublicKey(){
        RSAPublicKey rsaPublicKey=(RSAPublicKey) RSAUtils.initKey().getPublic();
        // encodeBase64(): Encodes binary data using the base64
        // algorithm but does not chunk the output.
        // getEncoded():返回key的原始编码形式
        return new String(Base64.encodeBase64(rsaPublicKey.getEncoded()));
    }

    /**
     * 解密数据
     * @param string 需要解密的字符串
     * @return 破解之后的字符串
     */
    public static String decryptBase64(String string){
        // decodeBase64():将Base64数据解码为"八位字节”数据
        return new String(decrypt(Base64.decodeBase64(string)));
    }


    public static byte[] decrypt(byte[] byteArray) {
        try {
            Provider provider=new org.bouncycastle.jce.provider.BouncyCastleProvider();
            Security.addProvider(provider);
            // Cipher: 提供加密和解密功能的实例
            // transformation: "algorithm/mode/padding"
            Cipher cipher=Cipher.getInstance("RSA/None/PKCS1Padding", provider);
            PrivateKey privateKey=getKeyPair().getPrivate();
            // 初始化
            cipher.init(Cipher.DECRYPT_MODE,privateKey);
            // doFinal(): 加密或者解密数据
            byte[] plainText=cipher.doFinal(byteArray);
            return plainText;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void saveKeyPair(KeyPair kp) throws Exception {


        FileOutputStream fos = new FileOutputStream(RSAKeyStore);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
// 生成密钥
        oos.writeObject(kp);
        oos.close();
        fos.close();
    }

    public static KeyPair getKeyPair() throws Exception {
        FileInputStream fis = new FileInputStream(RSAKeyStore);
        ObjectInputStream oos = new ObjectInputStream(fis);
        KeyPair kp = (KeyPair) oos.readObject();
        oos.close();
        fis.close();
        return kp;
    }
}

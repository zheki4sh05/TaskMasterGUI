package Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegConf {
    public static final int LOGIN_MIN =5;
    public static final int PASS_MIN=5;
    public static String createHash(String originalStr){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            StringBuilder sb = new StringBuilder();
            byte[] hash = md.digest(originalStr.getBytes());
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return originalStr;
        }
    }
}

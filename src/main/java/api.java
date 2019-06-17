import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class api {
    private static String result = "";

    public synchronized static void inserBlank(String str) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        StringEntity params = null;
        try {
            HttpPost request = new HttpPost("https://testreader.atu.kz/api/blanks.php?token=" + TokenforSite() + "&text=" + URLEncoder.encode(str, "UTF-8"));
            //params = new StringEntity("{\"text\":\"" + str + "\"} ");
            //params.setContentType("application/json;charset=UTF-8");
            //request.setEntity(params);
            System.out.println(str);
            HttpResponse response = httpClient.execute(request);
            System.out.println(request);
            System.out.println(response.getStatusLine());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
    public static String TokenforSite(){
        String countName = "";
        final Date currentTime = new Date();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        System.out.println(sdf.format(currentTime));
        try {
            countName = getHash(sdf.format(currentTime));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return countName;
    }
    public static String getHash(String str) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        //String s="f78spx";
        //String s="muffin break";
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.reset();
        // передаем в MessageDigest байт-код строки
        m.update(str.getBytes("utf-8"));
        // получаем MD5-хеш строки без лидирующих нулей
        String s2 = new BigInteger(1, m.digest()).toString(16);
        StringBuilder sb = new StringBuilder(32);
        // дополняем нулями до 32 символов, в случае необходимости
        //System.out.println(32 - s2.length());
        for (int i = 0, count = 32 - s2.length(); i < count; i++) {
            sb.append("0");
        }
        // возвращаем MD5-хеш
        return sb.append(s2).toString();
    }
}

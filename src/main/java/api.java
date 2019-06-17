import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class api {
    private static String result = "";

    public synchronized static void inserBlank(String str) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        StringEntity params = null;
        try {
            HttpPost request = new HttpPost("https://testreader.atu.kz/api/blanks.php?text=" + URLEncoder.encode(str, "UTF-8"));
            //params = new StringEntity("{\"text\":\"" + str + "\"} ");
            //params.setContentType("application/json;charset=UTF-8");
            //request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            System.out.println(params);
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
}

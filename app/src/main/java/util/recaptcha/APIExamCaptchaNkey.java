package util.recaptcha;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIExamCaptchaNkey {

    String jsonKey;

    public String getNkey(){

        String clientId = CaptchaInfomation.ID;//애플리케이션 클라이언트 아이디값";
        String clientSecret = CaptchaInfomation.SECRET;//애플리케이션 클라이언트 시크릿값";
        try {
            String code = "0"; // 키 발급시 0,  캡차 이미지 비교시 1로 세팅
            String apiURL = "https://openapi.naver.com/v1/captcha/nkey?code=" + code;
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            //json으로 넘겨받은 key값을 추출
            JSONObject jsonObject = new JSONObject(response.toString());
            jsonKey = jsonObject.getString("key");

        } catch (Exception e) {
            //response.append( "err : " + e.getMessage().toString() );
        }

        return jsonKey;

    }//getNkey()

}

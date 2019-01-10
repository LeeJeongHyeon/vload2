package util.recaptcha;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class APIExamCaptchaImage {

    public Bitmap getCaptchaImage(){
        String clientId = CaptchaInfomation.ID;//애플리케이션 클라이언트 아이디값";
        String clientSecret = CaptchaInfomation.SECRET;//애플리케이션 클라이언트 시크릿값";
        Bitmap bitmap = null;

        try {
            String key = CaptchaInfomation.result_key; // https://openapi.naver.com/v1/captcha/nkey 호출로 받은 키값
            String apiURL = "https://openapi.naver.com/v1/captcha/ncaptcha.bin?key=" + key;
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                InputStream is = con.getInputStream();
                int read = 0;
                byte[] bytes = new byte[1024];
                // 랜덤한 이름으로 파일 생성
                String tempname = Long.valueOf(new Date().getTime()).toString();
                File f = new File(Environment.getExternalStorageDirectory() + "/" + tempname + ".jpg");
                f.createNewFile();
                OutputStream outputStream = new FileOutputStream(f);
                while ((read =is.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + tempname + ".jpg");

                is.close();
                f.delete();//내부저장소에 생성된 이미지 파일 삭제
            } else {  // 에러 발생

                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
            }
        } catch (Exception e) {

        }

        return bitmap;
    }

}

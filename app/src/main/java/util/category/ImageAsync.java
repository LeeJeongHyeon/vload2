package util.category;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.net.URL;

import vmp.company.vexmall.R;

public class ImageAsync extends AsyncTask<String, Void, Bitmap> {
    final private String TAG = LogTag.getTag(this);

    CategoryVO vo;
    ImageView imageView;
    Context context;

    public ImageAsync(Context context, CategoryVO vo, ImageView imageView){
        this.context = context;
        this.vo = vo;
        this.imageView = imageView;
    }
    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bm = null;
        try {
            URL img_url = new URL(vo.getImagePath());
            BufferedInputStream bis = new BufferedInputStream(img_url.openStream());
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch(Exception e){
            e.printStackTrace();
            Log.i("MY", "err:"+e.getMessage());
        }

        //onPostExecute로 bitmap반환
        if(bm != null) {
            return bm;

        }else{
            //이미지가 없는 경우에는 기본 이미지를 반환
            Bitmap bitmap = BitmapFactory.decodeResource(
                    context.getResources(), R.drawable.category_default_img);
            return bitmap;
        }
        //return bm;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    public CategoryVO getVo() {
        return vo;
    }

    public void setVo(CategoryVO vo) {
        this.vo = vo;
    }

}

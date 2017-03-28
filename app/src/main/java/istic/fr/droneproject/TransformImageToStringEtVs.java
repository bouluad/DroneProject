package istic.fr.droneproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.View;

import java.io.ByteArrayOutputStream;



/**
 * Created by yousra on 28/03/17.
 */

public class TransformImageToStringEtVs{
      private Context context;

    public TransformImageToStringEtVs(Context context) {
        this.context = context;
    }

    public  String transformImageToString(int i){
        Bitmap largeIcone= BitmapFactory.decodeResource(context.getResources(),i);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        largeIcone.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
       String encodedImage = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return encodedImage;

    }

     public Bitmap transformStringToImage(String image) {
         byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);
         Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
         return  bitmap;
     }
}

package istic.fr.droneproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;



/**
 * Created by yousra on 28/03/17.
 */

public class TransformImageToStringEtVs extends Fragment {

    public  String transform(int i){
        Bitmap largeIconeau= BitmapFactory.decodeResource(this.getResources(),i);
        ByteArrayOutputStream streameau = new ByteArrayOutputStream();
        largeIconeau.compress(Bitmap.CompressFormat.JPEG, 100, streameau);
        byte[] byteFormateau = streameau.toByteArray();
       String encodedImage = Base64.encodeToString(byteFormateau, Base64.NO_WRAP);
        return encodedImage;

    }
}

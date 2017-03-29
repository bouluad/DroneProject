package istic.fr.droneproject.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.View;

import java.io.ByteArrayOutputStream;

import istic.fr.droneproject.R;
import istic.fr.droneproject.model.Vehicule;


/**
 * Created by yousra on 28/03/17.
 */

public class TransformImageToStringEtVs{
      private Context context;

    public TransformImageToStringEtVs(Context context) {
        this.context = context;
    }

    public  String transformImageToString(int RdotIDDotDrawablename){
        Bitmap largeIcone= BitmapFactory.decodeResource(context.getResources(),RdotIDDotDrawablename);
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

     public int FindImageIdByVehicule(Vehicule vehicule){
         int drawable;
         switch (vehicule.categorie) {
             case COMMANDEMENT:
                 drawable = R.drawable.vh_com;
                 break;

             case SAUVETAGE:
                 drawable = R.drawable.vh_hu;
                 break;

             case INCENDIE:
                 drawable = R.drawable.vh_in;
                 break;

             case RISQUE_PARTICULIER:
                 drawable = R.drawable.vh_rp;
                 break;

             case EAU:
                 drawable = R.drawable.vh_eau;
                 break;

             default:
                 drawable = R.drawable.vh_eau;
         }
         return drawable;
     }

    public int FindColorByVehicule(Vehicule vehicule){
        int color;
        switch (vehicule.categorie) {
            case COMMANDEMENT:
                color = Color.MAGENTA;
                break;

            case SAUVETAGE:
                color = Color.GREEN;
                break;

            case INCENDIE:
                color = Color.RED;
                break;

            case RISQUE_PARTICULIER:
                color = Color.YELLOW;
                break;

            case EAU:
                color = Color.BLUE;
                break;

            default:
                color = Color.BLACK;
        }
        return color;
    }
}

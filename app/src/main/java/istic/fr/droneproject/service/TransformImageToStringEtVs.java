package istic.fr.droneproject.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import istic.fr.droneproject.R;
import istic.fr.droneproject.model.Categorie;
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

    public int FindColorByVehicule(Categorie categorie){
        int color;
        switch (categorie) {
            case COMMANDEMENT:
                color = Color.rgb(164,87,142);
                break;

            case SAUVETAGE:
                color = Color.rgb(143,224,64);
                break;

            case INCENDIE:
                color = Color.rgb(242,61,45);
                break;

            case RISQUE_PARTICULIER:
                color = Color.rgb(250,193,41);
                break;

            case EAU:
                color = Color.rgb(130,140,174);
                break;

            default:
                color = Color.BLACK;
        }
        return color;
    }
}

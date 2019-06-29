package com.example.firstapp.ui.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;

import static android.graphics.Bitmap.Config.ARGB_8888;
import static android.graphics.Bitmap.createBitmap;

public class ImageEncryption {
    //public Bitmap Encrypt(String backGround, String secret) {
    public Bitmap Encrypt (Bitmap backGroundImage, Bitmap secretImage) {
        //Bitmap backGroundImage = BitmapFactory.decodeFile(backGround);
        //Bitmap secretImage = BitmapFactory.decodeFile(secret);
        Bitmap encryptImage = createBitmap(backGroundImage.getWidth(), backGroundImage.getHeight(), ARGB_8888);//backGroundImage ;//Bitmap.createBitmap(backGroundImage); //BitmapFactory.decodeFile(backGround);
        int p, a, b, c, d, e, f, g, h;

        if (backGroundImage.getHeight() != secretImage.getHeight() || backGroundImage.getWidth() != secretImage.getWidth())
        {
            return backGroundImage;
        }
        for (int i = 0; i < backGroundImage.getHeight(); i++) {
            for (int j = 0; j < backGroundImage.getWidth(); j++) {
                p = backGroundImage.getPixel(j, i);
                a = (p >> 24) & 0xff;
                b = (p >> 16) & 0xff;
                c = (p  >> 8) & 0xff;
                d = (p & 0x0ff);
                p = secretImage.getPixel(j, i);
                e = (p >> 24) & 0xff;
                f = (p >> 16) & 0xff;
                g = (p  >> 8) & 0xff;
                h = (p & 0x0ff);
                a = (16 * (a / 16) + e / 16) << 24;
                b = (16 * (b / 16) + f / 16) << 16;
                c = (16 * (c / 16) + g / 16) <<8;
                d = (16 * (d / 16) + h / 16);

                try {
                    encryptImage.setPixel(j, i, a+b+c+d);//0x008577);
                } catch (IllegalStateException z){
                    z.printStackTrace();
                }
            }
        }
        return encryptImage;
    }

    public Bitmap Decrypt (Bitmap encryptImage) {
        Bitmap decryptImage = createBitmap(encryptImage.getWidth(), encryptImage.getHeight(), ARGB_8888);
        int p, a, b, c, d;

        for (int i = 0; i < encryptImage.getHeight(); i++)
        {
            for (int j = 0; j < encryptImage.getWidth(); j++)
            {
                p = encryptImage.getPixel(j, i);
                a = (p >> 24) & 0xff;
                b = (p >> 16) & 0xff;
                c = (p  >> 8) & 0xff;
                d = (p & 0x0ff);
                a = (16 * (a % 16)) << 24;
                b = (16 * (b % 16)) << 16;
                c = (16 * (c % 16)) << 8;
                d = 16 * (d % 16);
                decryptImage.setPixel(j,i, a + b + c + d);
            }
        }
        return decryptImage;
    }
}

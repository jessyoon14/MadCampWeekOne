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
        int p, a, b, c, d, e, f;

        if (backGroundImage.getHeight() != secretImage.getHeight() || backGroundImage.getWidth() != secretImage.getWidth())
        {
            return backGroundImage;
        }

        for (int i = 0; i < backGroundImage.getHeight(); i++) {
            for (int j = 0; j < backGroundImage.getWidth(); j++) {
                p = backGroundImage.getPixel(j, i);
                a = (p & 0xff0000) >> 16;
                b = (p & 0x00ff00) >> 8;
                c = (p & 0x0000ff);
                p = secretImage.getPixel(j, i);
                d = (p & 0xff0000) >> 16;
                e = (p & 0x00ff00) >> 8;
                f = (p & 0x0000ff);
                a = (16 * (a / 16) + d / 16) << 16;
                b = (16 * (b / 16) + e / 16) << 8;
                c = 16 * (c / 16) + f / 16;

                try {
                    encryptImage.setPixel(j, i, a+b+c);//0x008577);
                } catch (IllegalStateException z){
                    z.printStackTrace();
                }
            }
        }
        return encryptImage;
    }

    public Bitmap Decrypt (Bitmap encryptImage) {
        Bitmap decryptImage = createBitmap(encryptImage.getWidth(), encryptImage.getHeight(), ARGB_8888);
        int p, a, b, c;



        for (int i = 0; i < encryptImage.getHeight(); i++)
        {
            for (int j = 0; j < encryptImage.getWidth(); j++)
            {
                p = encryptImage.getPixel(j, i);
                a = (p & 0xff0000) >> 16;
                b = (p & 0x00ff00) >> 8;
                c = (p & 0x0000ff);
                a = (16 * (a % 16)) << 16;
                b = (16 * (b % 16)) << 8;
                c = 16 * (c % 16);
                decryptImage.setPixel(j,i, a + b + c);
            }
        }
        return decryptImage;
    }
}

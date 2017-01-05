package com.meiku.dev.utils;
import android.graphics.Bitmap;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Hashtable;

/**
 * 
 * 版权所有：2015-美库网
 * 项目名称：mrrck   
 *
 * 类描述：二维码生成工具类
 * 类名称：com.meiku.dev.utils.QRcodeUtil     
 * 创建人：曙光
 * 创建时间：2015-11-3 下午5:38:26   
 * @version V3.0
 */
public class QRcodeUtil {

    /**
     * @Description: 生成二维码
     * @Title: qr_code 
     * @param qrCode
     * @param qrCode1
     * @return
     * @throws WriterException
     */
    public static Bitmap qr_code(String qrCode, BarcodeFormat qrCode1) throws WriterException {
        MultiFormatWriter writer = new MultiFormatWriter();
        Hashtable<EncodeHintType, String> hst = new Hashtable<EncodeHintType, String>();
        hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix matrix = writer.encode(qrCode, qrCode1, 500, 500, hst);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    }else{
                        pixels[y * width + x] = 0xffffffff;
                    }
                }

            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 
     * @Description: 生成二维码
     * @Title: createQRCode 
     * @param str
     * @param widthAndHeight  600
     * @return
     * @throws WriterException
     */
    public static Bitmap createQRCode(String str,int widthAndHeight) throws WriterException {
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix matrix = new MultiFormatWriter().encode(str,
                BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }else{
                    pixels[y * width + x] = 0xffffffff;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }



}

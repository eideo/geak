package com.github.xsocket.geak.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QRCodeUtils {

  private static final int BLACK = 0xFF000000;
  private static final int WHITE = 0xFFFFFFFF;
  
  public static BitMatrix encode(String content, int size) throws WriterException {
    // 输出二维码的样式参数
    Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
    hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
    hints.put(EncodeHintType.MARGIN, 1);

    MultiFormatWriter writer = new MultiFormatWriter();

    return writer.encode(content, BarcodeFormat.QR_CODE, size, size, hints);
  }

  public static BufferedImage toBufferedImage(BitMatrix matrix) {
    int width = matrix.getWidth();
    int height = matrix.getHeight();
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
      }
    }
    return image;
  }

  public static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
    BufferedImage image = toBufferedImage(matrix);
    if (!ImageIO.write(image, format, stream)) {
      throw new IOException("Could not write an image of format " + format);
    }
  }
  
  public static void writeToStream(String content, int size, String format, OutputStream stream) 
      throws IOException, WriterException {
    BitMatrix matrix = encode(content, size);
    writeToStream(matrix, format, stream);
  }
}

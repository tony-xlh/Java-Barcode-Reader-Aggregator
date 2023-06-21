package barcodereader;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;

public class ZXingBarcodeReader extends AbstractBarcodeReader {
  private QRCodeMultiReader reader;
  ZXingBarcodeReader(){
    reader = new QRCodeMultiReader();
  }
  @Override
  DecodingResult decodeBytes(byte[] imageBytes) {
    DecodingResult decodingResult = new DecodingResult();
    List<BarcodeResult> barcodeResults = new ArrayList<BarcodeResult>();
    long startTime = System.nanoTime();
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
    try {
      Map<DecodeHintType,?> hints = new HashMap<>();
      hints.put(DecodeHintType.TRY_HARDER,null);
      BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
      LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
      BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
      Result[] results = reader.decodeMultiple(bitmap, hints);
      for (Result result:results) {
        BarcodeResult barcodeResult = new BarcodeResult();
        barcodeResult.barcodeText = result.getText();
        barcodeResult.barcodeFormat = result.getBarcodeFormat().name();
        if (result.getRawBytes() != null) {
          barcodeResult.barcodeBytes = Base64.getEncoder().encodeToString(result.getRawBytes());;
        }else{
          barcodeResult.barcodeBytes = "";
        }
        Rectangle rect = getRectangleFromPoints(result.getResultPoints());
        barcodeResult.x1 = rect.x;
        barcodeResult.x2 = rect.x + rect.width;
        barcodeResult.x3 = rect.x + rect.width;
        barcodeResult.x4 = rect.x;
        barcodeResult.y1 = rect.y;
        barcodeResult.y2 = rect.y;
        barcodeResult.y3 = rect.y + rect.height;
        barcodeResult.y4 = rect.y + rect.height;
        barcodeResults.add(barcodeResult);
      }
    } catch (IOException | NotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    long endTime = System.nanoTime();
    double milliseconds = (endTime-startTime)*1e-6;
    decodingResult.elapsedTime = milliseconds;
    decodingResult.results = barcodeResults;
    return decodingResult;
  }

  private Rectangle getRectangleFromPoints(ResultPoint[] points){
    float minX,minY,maxX,maxY;
    minX = points[0].getX();
    minY = points[0].getY();
    maxX = 0;
    maxY = 0;
    for (ResultPoint point : points) {
      minX = Math.min(minX, point.getX());
      minY = Math.min(minY, point.getY());
      maxX = Math.max(maxX, point.getX());
      maxY = Math.max(maxY, point.getY());
    }
    Rectangle rect = new Rectangle((int)minX,(int)minY,(int)(maxX-minX),(int)(maxY-minY));
    return rect;
  }
}



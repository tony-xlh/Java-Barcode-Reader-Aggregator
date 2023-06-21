package barcodereader;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import com.accusoft.barcodexpress.*;
public class BarcodeXpressBarcodeReader extends AbstractBarcodeReader {
  BarcodeReader reader;
  BarcodeXpressBarcodeReader (){
    BarcodeXpress barcodeXpress = new BarcodeXpress();

    // The SetSolutionName, SetSolutionKey and possibly the SetOEMLicenseKey method must be
    // called to distribute the runtime.  Note that the SolutionName, SolutionKey and
    // OEMLicenseKey values shown below are only examples.
    // BarcodeXpress.setSolutionName("YourSolutionName");
    // BarcodeXpress.setSolutionKey(1234,1234,1234,1234);
    // BarcodeXpress.setOemLicenseKey("2.0.YourOEMLicenseKeyGoesHere");

    reader = barcodeXpress.getReader();
    List<BarcodeType> barcodeTypes = new ArrayList<>();
    barcodeTypes.add(BarcodeType.QRCODE);
    // Set scan barcode types
    reader.setBarcodeTypes(barcodeTypes.toArray(new BarcodeType[barcodeTypes.size()]));
  }
  @Override
  DecodingResult decodeBytes(byte[] imageBytes) {
    System.out.println("decode accusoft");
    DecodingResult decodingResult = new DecodingResult();
    List<BarcodeResult> barcodeResults = new ArrayList<BarcodeResult>();
    long startTime = System.nanoTime();
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
    try {
      BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
      Result[] results = reader.analyze(bufferedImage);
      for (int i = 0; i < results.length; i++) {
        Result result = results[i];
          
        BarcodeResult barcodeResult = new BarcodeResult();
        barcodeResult.barcodeText = result.getValue().replace("** UNLICENSED accusoft.com", "**");
        barcodeResult.barcodeFormat = result.getType().name();
        barcodeResult.confidence = result.getConfidence();
        barcodeResult.barcodeBytes = Base64.getEncoder().encodeToString(result.getData());
        barcodeResult.x1 = result.getPoint1().x;
        barcodeResult.x2 = result.getPoint2().x;
        barcodeResult.x3 = result.getPoint3().x;
        barcodeResult.x4 = result.getPoint4().x;
        barcodeResult.y1 = result.getPoint1().y;
        barcodeResult.y2 = result.getPoint2().y;
        barcodeResult.y3 = result.getPoint3().y;
        barcodeResult.y4 = result.getPoint4().y;
        barcodeResults.add(barcodeResult);
      }
    } catch (IOException | BarcodeException e) {
      e.printStackTrace();
    }
    long endTime = System.nanoTime();
    double milliseconds = (endTime-startTime)*1e-6;
    decodingResult.results = barcodeResults;
    decodingResult.elapsedTime = milliseconds;
    return decodingResult;
  }
}

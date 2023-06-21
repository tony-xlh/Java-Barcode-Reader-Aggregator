package barcodereader;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import boofcv.abst.fiducial.QrCodeDetector;
import boofcv.alg.fiducial.qrcode.QrCode;
import boofcv.factory.fiducial.ConfigQrCode;
import boofcv.factory.fiducial.FactoryFiducial;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.GrayU8;

public class BoofCVBarcodeReader extends AbstractBarcodeReader {
  private QrCodeDetector<GrayU8> detector;
  BoofCVBarcodeReader(){
    ConfigQrCode config = new ConfigQrCode();
//		config.considerTransposed = false; // by default, it will consider incorrectly encoded markers. Faster if false
		detector = FactoryFiducial.qrcode(config, GrayU8.class);
  }
  @Override
  DecodingResult decodeBytes(byte[] imageBytes) {
    DecodingResult decodingResult = new DecodingResult();
    List<BarcodeResult> barcodeResults = new ArrayList<BarcodeResult>();
    long startTime = System.nanoTime();
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
    try {
      BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
      GrayU8 gray = ConvertBufferedImage.convertFrom(bufferedImage, (GrayU8)null);
      detector.process(gray);
      List<QrCode> detections = detector.getDetections();
      for (QrCode qrCode : detections) {
        BarcodeResult barcodeResult = new BarcodeResult();
        barcodeResult.barcodeText = qrCode.message;
        barcodeResult.barcodeFormat = "QRCode";
        barcodeResult.barcodeBytes = Base64.getEncoder().encodeToString(qrCode.rawbits);
        barcodeResult.x1 = (int) qrCode.bounds.vertexes.get(0).x;
        barcodeResult.x2 = (int) qrCode.bounds.vertexes.get(1).x;
        barcodeResult.x3 = (int) qrCode.bounds.vertexes.get(2).x;
        barcodeResult.x4 = (int) qrCode.bounds.vertexes.get(3).x;
        barcodeResult.y1 = (int) qrCode.bounds.vertexes.get(0).y;
        barcodeResult.y2 = (int) qrCode.bounds.vertexes.get(1).y;
        barcodeResult.y3 = (int) qrCode.bounds.vertexes.get(2).y;
        barcodeResult.y4 = (int) qrCode.bounds.vertexes.get(3).y;
        barcodeResults.add(barcodeResult);
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    long endTime = System.nanoTime();
    double milliseconds = (endTime-startTime)*1e-6;
    decodingResult.elapsedTime = milliseconds;
    decodingResult.results = barcodeResults;
		return decodingResult;
  }
  
}

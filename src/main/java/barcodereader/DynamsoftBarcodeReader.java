package barcodereader;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.dynamsoft.dbr.BarcodeReader;
import com.dynamsoft.dbr.BarcodeReaderException;
import com.dynamsoft.dbr.TextResult;

public class DynamsoftBarcodeReader extends AbstractBarcodeReader {
  private BarcodeReader reader;
  DynamsoftBarcodeReader(){
    try {
      reader = new BarcodeReader();
    } catch (BarcodeReaderException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  } 
  @Override
  DecodingResult decodeBytes(byte[] imageBytes) {
    DecodingResult decodingResult = new DecodingResult();
    long startTime = System.nanoTime();
    try {
      
      TextResult[] results = reader.decodeFileInMemory(imageBytes, "");
      
      List<BarcodeResult> barcodeResults = new ArrayList<BarcodeResult>();
      for (TextResult result:results) {
          BarcodeResult barcodeResult = new BarcodeResult();
          barcodeResult.barcodeText = result.barcodeText;
          barcodeResult.barcodeFormat = result.barcodeFormatString;
          barcodeResult.barcodeBytes = Base64.getEncoder().encodeToString(result.barcodeBytes);
          barcodeResult.x1 = result.localizationResult.resultPoints[0].x;
          barcodeResult.x2 = result.localizationResult.resultPoints[1].x;
          barcodeResult.x3 = result.localizationResult.resultPoints[2].x;
          barcodeResult.x4 = result.localizationResult.resultPoints[3].x;
          barcodeResult.y1 = result.localizationResult.resultPoints[0].y;
          barcodeResult.y2 = result.localizationResult.resultPoints[1].y;
          barcodeResult.y3 = result.localizationResult.resultPoints[2].y;
          barcodeResult.y4 = result.localizationResult.resultPoints[3].y;
          barcodeResults.add(barcodeResult);
      }
      decodingResult.results = barcodeResults;
    } catch (BarcodeReaderException e) {
      e.printStackTrace();
    }
    long endTime = System.nanoTime();
    decodingResult.elapsedTime = endTime - startTime;
    return decodingResult;
  }
  
}

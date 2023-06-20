package barcodereader;

public abstract class AbstractBarcodeReader {
	abstract DecodingResult decodeBytes(byte[] imageBytes);
}

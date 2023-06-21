package barcodereader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

@SuppressWarnings("serial")
public class BarcodeReadingServlet extends HttpServlet   {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        System.out.println("read barcodes");
        StringBuilder responseStrBuilder = new StringBuilder();
        DecodingResult decodingResult = new DecodingResult();
        Gson gson = new Gson();
        try {
            BufferedReader streamReader = new BufferedReader( new InputStreamReader(request.getInputStream(), "UTF-8"));
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);
            
            DecodingRequestBody body = gson.fromJson(responseStrBuilder.toString(), DecodingRequestBody.class);
            byte[] bytes = Base64.getDecoder().decode(body.base64);
            AbstractBarcodeReader reader;
            System.out.println(body.SDK);
            if (body.SDK.equals("Dynamsoft")) {
              reader = new DynamsoftBarcodeReader();
            }else{
              reader = new ZXingBarcodeReader();
            }
            decodingResult = reader.decodeBytes(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.getWriter().write(gson.toJson(decodingResult));
    }
}

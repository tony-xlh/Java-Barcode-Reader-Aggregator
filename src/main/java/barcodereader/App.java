package barcodereader;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import com.dynamsoft.dbr.*;


public class App {

    public static void main(String[] args) {
        try {
            BarcodeReader.initLicense("DLS2eyJoYW5kc2hha2VDb2RlIjoiMjAwMDAxLTE2NDk4Mjk3OTI2MzUiLCJvcmdhbml6YXRpb25JRCI6IjIwMDAwMSIsInNlc3Npb25QYXNzd29yZCI6IndTcGR6Vm05WDJrcEQ5YUoifQ==");
        } catch (BarcodeReaderException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        // Create and configure a ThreadPool.
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setName("server");

        // Create a Server instance.
        Server server = new Server(threadPool);

        // Create a ServerConnector to accept connections from clients.
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(51041);

        // Add the Connector to the Server
        server.addConnector(connector);
        
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirAllowed(true);
        resourceHandler.setResourceBase(".");
        
        
        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServletWithMapping(BarcodeReadingServlet.class, "/readBarcodes");

        // Configure handlers
        HandlerCollection handlerList = new HandlerCollection();
        handlerList.setHandlers(new Handler[]{resourceHandler,servletHandler});
        server.setHandler(handlerList);
        
        // Start the Server so it starts accepting connections from clients.
        try {
            server.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

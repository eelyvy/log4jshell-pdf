package org.sandbox.api;

import com.sun.net.httpserver.HttpServer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Properties;

public class API {

    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/api/parse", (request -> {
            System.out.println("Processing request from: " + request.getRemoteAddress());
            if ("POST".equals(request.getRequestMethod())) {
                try (InputStream requestBody = request.getRequestBody()) {
                    String responseBody = parse(requestBody);
                    request.sendResponseHeaders(200, responseBody.getBytes().length);
                    OutputStream output = request.getResponseBody();
                    output.write(responseBody.getBytes());
                    output.flush();
                } catch (IOException e) {
                    request.sendResponseHeaders(500, -1);
                }
            } else {
                request.sendResponseHeaders(405, -1);// 405 Method Not Allowed
            }
            request.close();
        }));

        server.setExecutor(null); // creates a default executor
        setJNDIProperties();
        registerShutdownHook();
        System.out.println("Server started on port: " + PORT);
        server.start();
    }

    private static String parse(InputStream pdf) throws IOException {
        PDDocument doc = PDDocument.load(pdf);
        return new PDFTextStripper().getText(doc);
    }

    // for demonstration
    private static void setJNDIProperties() {
        Properties properties = System.getProperties();
        properties.setProperty("com.sun.jndi.ldap.object.trustURLCodebase", "true");
    }

    private static void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("Shutting down the Extractor server ...")));
    }
}

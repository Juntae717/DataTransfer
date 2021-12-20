package com.assignment.datatransfer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataTransferClient {
    private static final Logger logger  = Logger.getLogger(DataTransferClient.class.getName());

    private void connectAPI(final String API_URL, final OutputStream outputStream) {
        HttpURLConnection conn = null;

        try {
            URL url = new URL(API_URL);

            conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestProperty("Content-Type", "image/jpeg");
            conn.setRequestMethod("GET");

            conn.setDoOutput(false);

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                readImageData(outputStream, conn.getInputStream());
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "ERROR", e.toString());
        }
    }

    private void readImageData(final OutputStream outputStream ,final InputStream inputStream) {
        try {
            int len;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1)  {
                outputStream.write(buffer,0,len);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "ERROR", e.toString());
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "ERROR", e.toString());
            }
        }
    }

    public static void main(String[] args) {
        Socket socket = null;

        try {
            while(true) {
                socket = new Socket("192.168.0.91", 4432);
                DataTransferClient dataTransferClient = new DataTransferClient();
                dataTransferClient.connectAPI("http://192.168.0.240/cgi-bin/snapshot.jpg", socket.getOutputStream());
                System.out.println("File Transfer Completed");

                Thread.sleep(5000);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "ERROR", e.toString());
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "ERROR", e.toString());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "ERROR", e.toString());
            }
        }
    }
}

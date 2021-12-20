package com.assignment.datatransfer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataTransferServer {
    private static final Logger logger  = Logger.getLogger(DataTransferServer.class.getName());

    private static final int PORT_NUMBER = 4432;

    private static final String FILE_PATH = "D:\\DataTransfer_Image\\";
    private static final String FILE_EXTENSION = ".jpg";
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat ("yyyyMMddHHmmSS");

    public static void main(String[] args) {
        logger.info(":::                                                :::");
        logger.info(":::       DataTransfer Server Process Start        :::");
        logger.info(":::                                                :::");

        try(ServerSocket server = new ServerSocket(PORT_NUMBER)) {
            while(true) {
                Socket socket = server.accept();
                Date time = new Date();
                String fileName = FORMAT.format(time);

                try {
                    FileOutputStream fileOutputStream =  new FileOutputStream(FILE_PATH + fileName + FILE_EXTENSION);
                    InputStream inputStream = socket.getInputStream();

                    int len;
                    byte[] buffer = new byte[1024];
                    while((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                    }

                    System.out.println("File Transfer Completed");
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "ERROR", e.toString());
                }
            }
        } catch(IOException e) {
            logger.log(Level.SEVERE, "ERROR", e.toString());
        }
    }
}

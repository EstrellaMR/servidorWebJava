
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class WebServer {
    public static void main(String argv[]) throws Exception {
        String requestMessageLine;
        String requestMessageLine2;
        String fileName;
        String path2 = System.getProperty("user.dir");
        
        System.out.println(path2);
        
        ServerSocket listenSocket = new ServerSocket(6788);
        Socket connectionSocket = listenSocket.accept();
        
        BufferedReader inFromClient =new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        
        requestMessageLine = inFromClient.readLine();
        requestMessageLine2 = inFromClient.readLine();
        
        System.out.println(requestMessageLine);
        
        StringTokenizer tokenizedLine =new StringTokenizer(requestMessageLine);
        
        if (tokenizedLine.nextToken().equals("GET")){
            fileName = tokenizedLine.nextToken();
            System.out.println(fileName);
            
            if (fileName.startsWith("/") == true )
                fileName = fileName.substring(1);
            
            System.out.println(fileName);
            File file = new File(fileName);
            System.out.println(file);
            int numOfBytes = (int) file.length();
            System.out.println(numOfBytes);
            FileInputStream inFile = new FileInputStream (fileName);
            System.out.println(fileName);
            System.out.println(inFile);
            byte[] fileInBytes = new byte[numOfBytes];
            inFile.read(fileInBytes);
            outToClient.writeBytes("HTTP/1.0 200 Document Follows\r\n");
            
            if (fileName.endsWith(".jpg"))
                outToClient.writeBytes("Content-Type: image/jpeg\r\n");
            if (fileName.endsWith(".gif"))
                outToClient.writeBytes("Content-Type: image/gif\r\n");
            outToClient.writeBytes("Content-Length: " + numOfBytes + "\r\n");
            outToClient.writeBytes("\r\n");
            outToClient.write(fileInBytes, 0, numOfBytes);
            connectionSocket.close();
        }
            else System.out.println("Bad Request Message");
    }
}

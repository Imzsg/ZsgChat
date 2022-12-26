import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
  private static List<Socket> clientList = new ArrayList<>();

  public static void main(String[] args) throws IOException {
    ServerSocket serverSocket = new ServerSocket(25565);
    System.out.println("服务端已启动！");
    while (true) {
      Socket socket = serverSocket.accept();
      clientList.add(socket);
      InputStream inputStream = socket.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      CliendHandle cliendHandle = new CliendHandle(socket, reader, clientList);
      cliendHandle.start();
    }
  }
}

class CliendHandle extends Thread {
  private Socket socket;
  private BufferedReader reader;
  private List<Socket> list;

  public CliendHandle(Socket socket, BufferedReader reader, List<Socket> list) {
    this.reader = reader;
    this.socket = socket;
    this.list = list;
  }

  public void run() {
    try {
      while (true) {
        String message = reader.readLine();
        System.out.println(message);
        for (Socket socket : list) {
          PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
          writer.println(message);
          writer.flush();
        }
      }
    } catch (Exception e) {
      System.out.println("客户端退出连接");
    }
  }
}

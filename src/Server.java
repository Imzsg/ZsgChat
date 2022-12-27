import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
  private static List<Socket> clientList = new ArrayList<>();
  private static List<PrintWriter> writerList = new ArrayList<>();

  public static void main(String[] args) throws IOException {
    ServerSocket serverSocket = new ServerSocket(25565);
    System.out.println("服务端已启动！");
    while (true) {
      Socket socket = serverSocket.accept();
      clientList.add(socket);
      InputStream inputStream = socket.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
      writerList.add(writer);
      ClientHandle clientHandle = new ClientHandle(socket, reader, clientList, writerList);
      clientHandle.start();
    }
  }
}

class ClientHandle extends Thread {
  private Socket socket;
  private BufferedReader reader;
  private List<Socket> list;
  private List<PrintWriter> writerList;

  public ClientHandle(Socket socket, BufferedReader reader, List<Socket> list, List<PrintWriter> writerList) {
    this.reader = reader;
    this.socket = socket;
    this.list = list;
    this.writerList = writerList;
  }

  public void run() {
    try {
      while (true) {
        String message = reader.readLine();
        if (message != null) {
          System.out.println(message);
        } else {
          socket.close();
          list.remove(socket);
        }
        synchronized (writerList) {
          for (PrintWriter writer : writerList) {
            writer.println(message);
            writer.flush();
          }
        }
      }
    } catch (Exception e) {
      System.out.println("客户端退出连接");
    }finally{
      try {
        reader.close();
        socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      writerList.remove(writerList);
      list.remove(socket);
    }
  }
}

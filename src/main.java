import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class main {
  public static void main(String[] args) throws IOException {
    Socket socket = new Socket("123.56.12.65", 25565);
    System.out.println("请输入宁的用户名：");
    Scanner scan = new Scanner(System.in);
    String name = scan.nextLine();
    GetMessage getMessage = new GetMessage(socket);
    getMessage.start();

    System.out.println("请输入你要发送的消息：");
    while (true) {
      Scanner scanner = new Scanner(System.in);
      String i = scanner.nextLine();
      OutputStream out = socket.getOutputStream();
      PrintWriter writer = new PrintWriter(out);
      writer.println("<" + name + ">" + i);
      writer.flush();
    }
  }
}

class GetMessage extends Thread {
  private Socket socket;

  public GetMessage(Socket socket) {
    this.socket = socket;
  }

  public void run() {
    while (true) {
      try {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String message = reader.readLine();
        System.out.println(message);

      } catch (Exception e) {
        System.out.println("异常，可能是服务端关闭");
        break;
      }
    }
  }
}

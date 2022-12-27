import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class main {
  public static void main(String[] args) throws IOException {
    Socket socket = new Socket("127.0.0.1", 25565);
    System.out.println("�����������û�����");
    Scanner scan = new Scanner(System.in);
    String name = scan.nextLine();
    GetMessage getMessage = new GetMessage(socket);
    getMessage.start();

    System.out.println("��������Ҫ���͵���Ϣ��");
    Scanner scanner = new Scanner(System.in);
    while (true) {
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
        if (message != null) {
          System.out.println(message);
        } else {
          System.out.println("����������null");
          break;
        }
      } catch (Exception e) {
        System.out.println("�쳣�������Ƿ���˹ر�");
        break;
      }
    }
  }
}

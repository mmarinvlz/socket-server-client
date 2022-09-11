import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
  static Scanner scanner = null;
  private static int port = 8089;

  public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
    scanner = new Scanner(System.in);
    String selectedOption, cont = "s";
    while (cont.equalsIgnoreCase("s")) {
      System.out.println("\nDigite 1 para agregar nueva cuenta");
      System.out.println("Digite 2 para buscar una cuenta");

      System.out.print("\nDigite la opción deseada: ");
      selectedOption = scanner.nextLine();
      if (selectedOption.equals("1")) {
        Client.CreateRecord();
      } else if (selectedOption.equals("2")) {
        Client.SearchRecord();
      }
      System.out.print("Digite S si desea continuar, N si no lo desea: ");
      cont = scanner.nextLine();
    }
    scanner.close();
  }

  private static void CreateRecord() throws IOException, ClassNotFoundException, InterruptedException {
    InetAddress host = InetAddress.getLocalHost();
    Socket socket = null;
    ObjectOutputStream objectOutputStream = null;
    ObjectInputStream objectInputStream = null;
    scanner = new Scanner(System.in);
    String account, value, newRow;
    System.out.println("\nCrear cuenta\n");
    System.out.print("Ingrese el número de cuenta: ");
    account = scanner.nextLine();
    System.out.print("Ingrese el Valor: ");
    value = scanner.nextLine();
    newRow = "1," + account + "," + value;
    // Se establece conexión con el socket.
    socket = new Socket(host.getHostName(), port);
    objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    objectOutputStream.writeObject(newRow);
    objectInputStream = new ObjectInputStream(socket.getInputStream());
    String message = (String) objectInputStream.readObject();
    System.out.println("\nRespuesta del Servidor: '" + message + "'");
    System.out.print("\n");
    objectInputStream.close();
    objectOutputStream.close();
  }

  private static void SearchRecord() throws IOException, ClassNotFoundException {
    InetAddress host = InetAddress.getLocalHost();
    Socket socket = null;
    ObjectOutputStream objectOutputStream = null;
    ObjectInputStream objectInputStream = null;
    String ID;
    scanner = new Scanner(System.in);

    System.out.println("\nBuscar cuenta");
    System.out.print("\nIngrese el número de cuenta: ");
    ID = scanner.nextLine();
    // Se establece conexión con el socket.
    socket = new Socket(host.getHostName(), port);
    objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

    objectOutputStream.writeObject("2," + ID);
    objectInputStream = new ObjectInputStream(socket.getInputStream());
    String response = (String) objectInputStream.readObject();
    if (!response.isEmpty()) {
      System.out.print("\nRegistro encontrado:");
      System.out.print(response);
    } else {
      System.out.print("\nNo se encontraron registros.");
    }
    System.out.println("\n");
  }
}
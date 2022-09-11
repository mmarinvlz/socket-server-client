import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class Server {
  private static ServerSocket serverSocket;
  private static int port = 8089;

  public static void main(String args[]) throws IOException, ClassNotFoundException {
    // Instanciamos el socket
    serverSocket = new ServerSocket(port);
    String selectedOption = null;
    // Dejamos el socket abierto, para cerrarlo se debe eviar "salir"
    while (true) {
      // Creamos socket
      System.out.println("Esperando solicitud del cliente...");
      Socket socket = serverSocket.accept();
      // Instanciamos el input stream para obtener el dato del cliente
      ObjectInputStream ObjectInputStream = new ObjectInputStream(socket.getInputStream());
      String message = (String) ObjectInputStream.readObject();
      // Evaluamos la opción seleccionada por el usuario
      Integer option = Integer.parseInt(message.split(",")[0]);

      if (option == 1) {
        System.out.println("Agregando cuenta...");
        selectedOption = Server.CreateRecord(message.substring(2));
      } else if (option == 2) {
        System.out.println("Buscando cuenta...");
        selectedOption = Server.SearchRecord(message.substring(2));
      } else {
        System.out.println("Opción erronea.");
      }

      // Instanciamos el output del stream para dar respuesta al cliente
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
      objectOutputStream.writeObject(selectedOption);
      ObjectInputStream.close();
      objectOutputStream.close();
      socket.close();
    }
  }

  private static String CreateRecord(String record) throws IOException {
    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("datos.txt", true));
    try {
      // Registro de datos en el archivo "datos.txt"
      bufferedWriter.write(record);
      bufferedWriter.flush();
      bufferedWriter.newLine();
      bufferedWriter.close();
    } catch (Exception e) {
      return "NO-OK";
    }
    return "Registro grabado OK";
  }

  private static String SearchRecord(String id) throws IOException {
    String record, account = "";
    BufferedReader bufferedReader = new BufferedReader(new FileReader("datos.txt"));
    while ((record = bufferedReader.readLine()) != null) {
      StringTokenizer stringTokenizer = new StringTokenizer(record, ",");
      if (record.contains(id)) {
        account += " " + stringTokenizer.nextToken() + " " + stringTokenizer.nextToken() + " \n";
      }
    }
    bufferedReader.close();
    return account;
  }
}
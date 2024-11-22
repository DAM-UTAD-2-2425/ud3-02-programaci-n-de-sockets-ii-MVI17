package cliente;

import java.io.*;
import java.net.Socket;

/**
 * TODO: Complementa esta clase para que genere la conexión TCP con el servidor
 * para enviar un boleto, recibir la respuesta y finalizar la sesión
 */
public class ClienteTCP {
	private Socket socket;
	private PrintWriter salida;
	private BufferedReader entrada;

	/**
	 * Constructor
	 */
	public ClienteTCP(String ip, int puerto) {
		try {
			this.socket = new Socket(ip, puerto);
			this.salida = new PrintWriter(socket.getOutputStream(), true);
			this.entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.err.println("Error al intentar conectar con el servidor: " + e.getMessage());
		}
	}

	/**
	 * @param combinacion que se desea enviar
	 * @return respuesta del servidor con la respuesta del boleto
	 */
	public String comprobarBoleto(int[] combinacion) {
		try {
			StringBuilder sb = new StringBuilder();
			for (int num : combinacion) {
				sb.append(num).append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			salida.println(sb.toString());

			return entrada.readLine();
		} catch (IOException e) {
			return "Error al comunicarse con el servidor: " + e.getMessage();
		}
	}

	/**
	 * Sirve para finalizar la conexión de Cliente y Servidor
	 */
	public void finSesion() {
		try {
			salida.println("FIN");
			socket.close();
		} catch (IOException e) {
			System.err.println("Error al finalizar la conexión: " + e.getMessage());
		}
	}
}

package servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * TODO: Complementa esta clase para que acepte conexiones TCP con clientes para
 * recibir un boleto, generar la respuesta y finalizar la sesión
 */
public class ServidorTCP {
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private BufferedReader entrada;
	private PrintWriter salida;
	private String[] respuesta;
	private int[] combinacion;
	private int reintegro;
	private int complementario;

	/**
	 * Constructor
	 */
	public ServidorTCP(int puerto) {
		try {
			this.serverSocket = new ServerSocket(puerto);
			System.out.println("Servidor iniciado en el puerto " + puerto);

			this.respuesta = new String[9];
			this.respuesta[0] = "Boleto inválido - Números repetidos";
			this.respuesta[1] = "Boleto inválido - números incorrectos (1-49)";
			this.respuesta[2] = "6 aciertos";
			this.respuesta[3] = "5 aciertos + complementario";
			this.respuesta[4] = "5 aciertos";
			this.respuesta[5] = "4 aciertos";
			this.respuesta[6] = "3 aciertos";
			this.respuesta[7] = "Reintegro";
			this.respuesta[8] = "Sin premio";

			generarCombinacion();
			imprimirCombinacion();

			this.clientSocket = serverSocket.accept();
			this.entrada = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			this.salida = new PrintWriter(clientSocket.getOutputStream(), true);
		} catch (IOException e) {
			System.err.println("Error al iniciar el servidor: " + e.getMessage());
		}
	}

	/**
	 * @return Lee la combinación enviada por el cliente
	 */
	public String leerCombinacion() {
		if (entrada == null) {
			return "Error: conexión no establecida.";
		}
		try {
			return entrada.readLine();
		} catch (IOException e) {
			return "Error al leer la combinación: " + e.getMessage();
		}
	}

	/**
	 * @param linea La combinación de números enviada por el cliente.
	 * @return La respuesta adecuada según la combinación del cliente.
	 */
	public String comprobarBoleto(String linea) {
		try {
			String[] partes = linea.split(",");
			if (partes.length != 6) {
				return respuesta[1];
			}

			int[] boleto = new int[6];
			for (int i = 0; i < 6; i++) {
				boleto[i] = Integer.parseInt(partes[i]);
			}

			if (Arrays.stream(boleto).distinct().count() != 6) {
				return respuesta[0];
			}
			for (int num : boleto) {
				if (num < 1 || num > 49) {
					return respuesta[1];
				}
			}

			int aciertos = 0;
			boolean tieneComplementario = false;
			for (int num : boleto) {
				if (Arrays.stream(combinacion).anyMatch(n -> n == num)) {
					aciertos++;
				}
				if (num == complementario) {
					tieneComplementario = true;
				}
			}

			if (aciertos == 6) {
				return respuesta[2]; // 6 aciertos
			} else if (aciertos == 5 && tieneComplementario) {
				return respuesta[3]; // 5 aciertos + complementario
			} else if (aciertos == 5) {
				return respuesta[4]; // 5 aciertos
			} else if (aciertos == 4) {
				return respuesta[5]; // 4 aciertos
			} else if (aciertos == 3) {
				return respuesta[6]; // 3 aciertos
			} else if (Arrays.stream(boleto).anyMatch(n -> n == reintegro)) {
				return respuesta[7]; // Reintegro
			} else {
				return respuesta[8]; // Sin premio
			}
		} catch (Exception e) {
			return "Error al comprobar el boleto: " + e.getMessage();
		}
	}

	/**
	 * Envía la respuesta al cliente.
	 */
	public void enviarRespuesta(String respuesta) {
		salida.println(respuesta);
	}

	/**
	 * Cierra la conexión con el cliente y el servidor.
	 */
	public void finSesion() {
		try {
			clientSocket.close();
			serverSocket.close();
		} catch (IOException e) {
			System.err.println("Error al cerrar el servidor: " + e.getMessage());
		}
	}

	/**
	 * Genera una combinación aleatoria de 6 números y el reintegro y
	 * complementario. NO MODIFICADO
	 */
	private void generarCombinacion() {
		Set<Integer> numeros = new TreeSet<>();
		Random aleatorio = new Random();
		while (numeros.size() < 6) {
			numeros.add(aleatorio.nextInt(49) + 1);
		}
		int i = 0;
		this.combinacion = new int[6];
		for (Integer elto : numeros) {
			this.combinacion[i++] = elto;
		}
		this.reintegro = aleatorio.nextInt(49) + 1;
		this.complementario = aleatorio.nextInt(49) + 1;
	}

	/**
	 * Imprime la combinación ganadora, el complementario y el reintegro.
	 */
	private void imprimirCombinacion() {
		System.out.print("Combinación ganadora: ");
		for (Integer elto : this.combinacion)
			System.out.print(elto + " ");
		System.out.println("");
		System.out.println("Complementario:       " + this.complementario);
		System.out.println("Reintegro:            " + this.reintegro);
	}
}

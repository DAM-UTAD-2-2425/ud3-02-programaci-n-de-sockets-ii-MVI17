package cliente;

import java.util.Scanner;


/**
 * No debes tocar este código
 */
public class PrimitivaCliente {
	public static void main(String[] args) {
		Scanner sc = new Scanner (System.in);
		ClienteTCP canal = new ClienteTCP("localhost",5555);
		int [] combi = new int [6];
		System.out.println("Bienvenido a la Lotería Primitiva");
		do {
			System.out.println("Escriba su combinación de 6 números (uno por línea):");
			int num;
			for (int i = 0; i < combi.length; i++) {
				combi[i] = sc.nextInt();
			}
			System.out.println(canal.comprobarBoleto(combi));
			System.out.print("- Desea volver a jugar? (s/n)");
		} while (sc.next().toLowerCase().equals("s"));
		canal.finSesion();
	}
}

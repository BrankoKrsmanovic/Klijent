package logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {
	static Socket soketZaKomunikaciju = null;
	static BufferedReader serverInput = null;
	static PrintStream serverOutput = null;
	static BufferedReader unosSaTastature = null;

	public static void main(String[] args) {

		try {
			soketZaKomunikaciju = new Socket("localhost", 7001);

			serverInput = new BufferedReader(new InputStreamReader(soketZaKomunikaciju.getInputStream()));
			serverOutput = new PrintStream(soketZaKomunikaciju.getOutputStream());
			unosSaTastature = new BufferedReader(new InputStreamReader(System.in));

			new Thread(new Client()).start();

			String input;
			PrintWriter out = null;

			while (true) {
				input = serverInput.readLine();
				if (input == null) {
					// out.close();
					break;
				}

				System.out.println(input);

				if (input.startsWith("Dovidjenja")) {
					// out.close();
					break;
				}
				if (input.startsWith("Data/"))
					out = new PrintWriter(new BufferedWriter(new FileWriter(input)));

				if (input.startsWith("[KALKULACIJE]")) {
					String kalkulacije[] = input.split("]");
					//System.out.println(kalkulacije.length);
					if (kalkulacije.length > 1) {
						String sveKalkulacije[] = kalkulacije[1].split("\t");
						for (int i = 0; i < sveKalkulacije.length; i++)
							out.println(sveKalkulacije[i]);
					}
					out.close();
				}
			}

			soketZaKomunikaciju.close();

		} catch (IOException e) {
			System.out.println("Server is down");
			// e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			String message;
			while (true) {
				message = unosSaTastature.readLine();
				serverOutput.println(message);
				if (message.equalsIgnoreCase("***quit") || message.equalsIgnoreCase("N"))
					break;
			}
		} catch (IOException e) {

		}
	}
}

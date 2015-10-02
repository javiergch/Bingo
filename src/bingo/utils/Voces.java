package bingo.utils;

import java.io.InputStream;

public class Voces {

	public static void reproducir(Integer numero) {
		InputStream stream = Voces.class.getClassLoader().getResourceAsStream("bingo/numeros/" + numero + ".WAV");
		new PlayWave(stream).start();
	}
	
}

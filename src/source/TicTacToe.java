package source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TicTacToe {
	final static char MAX = 'x';
	final static char MIN = 'o'; 
	
	static int jogada;
	
	
	public static void main(String[] args){
		Game g = new Game();
		int algoritmo = setAlgoritmo();
		while(!g.fim()){
			System.out.println(g.display());
			
			getJogada();
			g.mudarVez('x',jogada);
			
			if(g.fim()){
				break;
			}
			
			float v = Integer.MAX_VALUE;
			Game prox = null;
			for (Game next : g.prox()){
				int val = 0;
				if(algoritmo == 2)
					val = next.alphabeta(next,Integer.MIN_VALUE,Integer.MAX_VALUE,'x');
				else if(algoritmo == 1)
					val = next.minimax(next, 'x');
				else{
					System.out.println("Erro na escolha do algoritmo.");
					System.exit(1);
				}
			    if (val <= v) {
					v = val;
					prox = next;
			    }
			}
			g = prox;
			
			System.out.println(g.display());
		}
		
		System.out.println(g.display());
		char vencedor = g.getGanhador();
		if(vencedor == 'e')
			System.out.println("Empate!");
		else
			System.out.println("O jogador " + vencedor  + " ganhou");
		
	}	
	
	public static void getJogada(){
		System.out.println("Digite um número de 1 a 9: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
	         jogada = Integer.parseInt(br.readLine());
	      } catch (IOException ioe) {
	         System.out.println("Número inválido!");
	         System.exit(1);
	      }
		if (jogada < 1 || jogada > 9) {
			System.out.println("A jogada deve ser um número entre 1 e 9");
		}
	}
	
	static int setAlgoritmo(){
		int algoritmo = 0;
		System.out.println("Digite 1 para MINIMAX ou 2 para Poda ALPHA-BETA: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
	         algoritmo = Integer.parseInt(br.readLine());
	      } catch (IOException ioe) {
	         System.out.println("Número inválido!");
	         System.exit(1);
	      }
		if (jogada != 1 || jogada != 2) {
			System.out.println("Você deve escolher entre 1 ou 2.");
		}
		return algoritmo;
	}
}

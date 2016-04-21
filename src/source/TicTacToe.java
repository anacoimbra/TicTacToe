package source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TicTacToe {
	final static char MAX = 'x';
	final static char MIN = 'o'; 
	
	static int jogada;
	
	/**
	 * Metodo para iniciar e realizar o calculo na raiz da
	 * arvore do minimax
	 * @param args
	 */
	public static void main(String[] args){
		Game g = new Game();
		// Obtem algoritmo
		int algoritmo = setAlgoritmo();
		// Enquanto o jogo não estiver acabado
		while(!g.fim()){
			// Imprime o tabuleiro
			System.out.println(g.display());
			
			// Obtem posicao da jogada
			getJogada();
			
			// Computa qual é o jogador e passa a vez para a proxima iteração
			g.mudarVez('x',jogada);
			
			// Se o jogo acabar depois de trocar a vez, sair do while
			if(g.fim()){
				break;
			}
			
			float v = Integer.MAX_VALUE;
			Game prox = null;
			
			/**
			 * Configuracoes:
			 * x - MAX
			 * o - MIN
			 */
			
			// Para cada no filho da raiz
			for (Game next : g.prox()){
				int val = 0;
				// Se algoritmo = 2, fazer alphabeta
				if(algoritmo == 2)
					val = next.alphabeta(next,Integer.MIN_VALUE,Integer.MAX_VALUE,'x');
				// Se algoritmo = 1, fazer minimax
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
			// Mostra tabuleiro apos a jogada de MIN
			System.out.println(g.display());
		}
		
		// Mostra o tabuleiro final
		System.out.println(g.display());
		// Obtem vencedor
		char vencedor = g.getGanhador();
		// Se vencedor = e, aconteceu empate
		if(vencedor == 'e')
			System.out.println("Empate!");
		else
			System.out.println("O jogador " + vencedor  + " ganhou");
		
	}	
	
	/**
	 * Recebe do teclado um valor de 1 a 9 para ser a posicao da jogada
	 */
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
	
	/**
	 * Recebe do teclado o numero correspondente ao algoritmo escolhido
	 * 1 - Minimax
	 * 2 - Minimax com poda alpha-beta
	 * @return algoritmo escolhido
	 */
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

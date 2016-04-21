package source;

import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.synth.SynthSeparatorUI;

public class Game {
	static float[] valores = { -100, 100 };
	public char[] estado = new char[9];
	public char jogador;

	/**
	 * Inicia jogo com MAX = x e o tabuleiro vazio
	 */
	public Game() {
		for (int i = 0; i < estado.length; i++) {
			estado[i] = ' ';
		}
		jogador = 'x';
	}

	/**
	 * Inicia jogo com jogador j
	 * e tabuleiro e
	 * 
	 * @param e
	 * @param j
	 */
	public Game(char[] e, char j) {
		estado = e;
		jogador = j;
	}

	/**
	 * Mostra tabuleiro na tela
	 * @return string do tabuleiro
	 */
	public String display() {
		String board = "-------------\n";
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board += "| " + estado[i * 3 + j] + " ";
			}
			board += "|\n-------------\n";
		}
		return board;
	}

	/**
	 * Obtem todos os nos filhos do nó
	 * que estiver em 'this'
	 * @return Lista de filhos
	 */
	List<Game> prox() {
		List<Game> resp = new ArrayList<Game>();
		char prox_jogador;
		if (jogador == 'x')
			prox_jogador = 'o';
		else
			prox_jogador = 'x';
		for (int i = 1; i <= 9; i++) {
			char[] e = getTabuleiro(jogador, i);
			if (e != null)
				resp.add(new Game(e, prox_jogador));
		}
		return resp;
	}

	/**
	 * Retporna tabuleiro de acordo com jogador e posicao da jogada
	 * @param player
	 * @param n
	 * @return tabuleiro resp
	 */
	public char[] getTabuleiro(char player, int n) {
		if (n > 0 && n < 10 && estado[n - 1] == ' ') {
			char[] resp = new char[9];
			for (int i = 0; i < 9; i++) {
				resp[i] = estado[i];
			}
			resp[n - 1] = player;
			return resp;
		} else {
			return null;
		}
	}

	/**
	 * Verifica se tabuleiro esta cheio
	 * @return true se estiver cheio e false se não estiver
	 */
	boolean cheio() {
		for (int i = 0; i < estado.length; i++) {
			if (estado[i] == ' ')
				return false;
		}
		return true;
	}

	/**
	 * Realiza a jogada de acordo com o jogador da vez
	 * e com a posicao da jogada
	 * @param vez
	 * @param jogada
	 * @return tabuleiro com o resultado
	 */
	public char[] jogar(char vez, int jogada) {
		if (jogada > 0 && jogada < 10 && estado[jogada - 1] == ' ') {
			char[] resp = new char[9];
			for (int i = 0; i < 9; i++) {
				resp[i] = estado[i];
			}
			resp[jogada - 1] = vez;
			return resp;
		} else {
			return null;
		}
	}

	/**
	 * Muda de jogador se for possivel
	 * @param vez
	 * @param jogada
	 */
	public void mudarVez(char vez, int jogada) {
		char[] newEstado = jogar(vez, jogada);
		if (newEstado != null) {
			estado = newEstado;
		}
		if (vez == 'x') {
			jogador = 'o';
		} else {
			jogador = 'x';
		}
	}
	
	/**
	 * Algoritmo alpha-beta pruning
	 * @param g
	 * @param alpha
	 * @param beta
	 * @param vez
	 * @return
	 */
	public int alphabeta(Game g, int alpha, int beta, char vez){
		// Se g for terminal, retona a heuristica
		if(g.fim()){
			char ganhador = g.getGanhador();
			if(ganhador == 'x')
				return 1;
			else if(ganhador == 'o'){
				return -1;
			}else{
				return 0;
			}
		}
		// Se for a vez de MAX
		if(vez == 'x'){
			// v = -inf
			int v = Integer.MIN_VALUE;
			// Para cada no filho de g
			for(Game next : g.prox()){
				// v = max(v, alphabeta(filho, alpha, beta, min)
				v = Integer.max(v, alphabeta(next, alpha, beta, 'o'));
				// alpha = max(alpha, v)
				alpha = Integer.max(alpha, v);
				// se beta <= alpha => poda em beta
				if(beta <= alpha){
					break;
				}
			}
			return v;
		}else{
			// v = inf
			int v = Integer.MAX_VALUE;
			// Para cada filho de g
			for(Game next : g.prox()){
				// v = min (v, alphabeta(filho,alpha,beta,max))
				v = Integer.min(v, alphabeta(next, alpha, beta, 'x'));
				// beta = min(beta,v)
				beta = Integer.min(beta, v);
				// se beta <= alpha => poda em alpha
				if(beta <= alpha){
					break;
				}
			}
			return v;
		}
	}

	/**
	 * Algoritmo minimax
	 * @param g
	 * @param vez
	 * @return
	 */
	public int minimax(Game g, char vez){
		// Se g é terminal => retornar heuristica de g
		if(g.fim()){
			char ganhador = g.getGanhador();
			if(ganhador == 'x')
				return 1;
			else if(ganhador == 'o')
				return -1;
			else
				return 0;
		}
		
		// Se for a vez de MAX
		if(vez == 'x'){
			// best = -inf
			int best = Integer.MIN_VALUE;
			// para cada filho de g
			for(Game next : g.prox()){
				// v = minimax(filho, min)
				int v = minimax(next, 'o');
				//best = max(best,v)
				best = Integer.max(best, v);
			}
			return best;
		}else{ // Se for a vez de min
			// best = inf
			int best = Integer.MAX_VALUE;
			// Para cada filho de g
			for(Game next : g.prox()){
				// v = minimax(filho, max)
				int v = minimax(next, 'x');
				// best = min(best, v)
				best = Integer.min(best, v);
			}
			return best;
		}
	}
	
	/**
	 * Obtem linhas verticais, horizontais e diagonais
	 * que podem conter valores que determinam se, e quem
	 * ganhou o jogo
	 * @return lista de linhas,colunas e diagonais com seus valores do tabuleiro
	 */
	public ArrayList<char[]> linhas() {
		ArrayList<char[]> resp = new ArrayList<>();
		// horizontal
		for (int i = 0; i < 3; i++) {
			char[] row = new char[3];
			row[0] = estado[i * 3];
			row[1] = estado[i * 3 + 1];
			row[2] = estado[i * 3 + 2];
			resp.add(row);
		}

		// vertical
		for (int i = 0; i < 3; i++) {
			char[] column = new char[3];
			column[0] = estado[i];
			column[1] = estado[i + 3];
			column[2] = estado[i + 6];
			resp.add(column);
		}

		// diagonal
		char[] diag1 = { estado[0], estado[4], estado[8] };
		char[] diag2 = { estado[2], estado[4], estado[6] };
		
		resp.add(diag1);
		resp.add(diag2);

		return resp;
	}

	/**
	 * Verifica nas linhas obtidas em linhas() se alguma delas
	 * tem algum vencedor
	 * x - max ganhou
	 * o - max perdeu
	 * e - empate
	 * 0 - o jogo ainda nao acabou
	 * @return char do vencedor
	 */
	char getGanhador() {
		for (char[] linha : linhas()) {
			if (linha[0] == linha[1] && linha[1] == linha[2]) {
				if (linha[0] == 'x') {
					return 'x';
				} else if (linha[0] == 'o') {
					return 'o';
				}
			}
		}
		if (cheio()) {
			return 'e';
		}
		return 0;
	}


	/**
	 * Verifica se existe um ganhador ou foi empate
	 * para saber se o jogo já acabou.
	 * @return
	 */
	boolean fim() {
		char g = getGanhador();
		return g != 0;
	}
}

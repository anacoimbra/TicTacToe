package source;

import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.synth.SynthSeparatorUI;

public class Game {
	static float[] valores = { -100, 100 };
	public char[] estado = new char[9];
	public char jogador;

	public Game() {
		for (int i = 0; i < estado.length; i++) {
			estado[i] = ' ';
		}
		jogador = 'x';
	}

	public Game(char[] e, char j) {
		estado = e;
		jogador = j;
	}

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

	boolean cheio() {
		for (int i = 0; i < estado.length; i++) {
			if (estado[i] == ' ')
				return false;
		}
		return true;
	}

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
	
	public int alphabeta(Game g, int alpha, int beta, char vez){
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
		if(vez == 'x'){
			int v = Integer.MIN_VALUE;
			for(Game next : g.prox()){
				v = Integer.max(v, alphabeta(next, alpha, beta, 'o'));
				alpha = Integer.max(alpha, v);
				if(beta <= alpha){
					break;
				}
			}
			return v;
		}else{
			int v = Integer.MAX_VALUE;
			for(Game next : g.prox()){
				v = Integer.min(v, alphabeta(next, alpha, beta, 'x'));
				beta = Integer.min(beta, v);
				if(beta <= alpha){
					break;
				}
			}
			return v;
		}
	}

	public int minimax(Game g, char vez){
		if(g.fim()){
			char ganhador = g.getGanhador();
			if(ganhador == 'x')
				return 1;
			else if(ganhador == 'o')
				return -1;
			else
				return 0;
		}
		
		if(vez == 'x'){
			int best = Integer.MIN_VALUE;
			for(Game next : g.prox()){
				int v = minimax(next, 'o');
				best = Integer.max(best, v);
			}
			return best;
		}else{
			int best = Integer.MAX_VALUE;
			for(Game next : g.prox()){
				int v = minimax(next, 'x');
				best = Integer.min(best, v);
			}
			return best;
		}
	}
	
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


	boolean fim() {
		char g = getGanhador();
		return g != 0;
	}
}

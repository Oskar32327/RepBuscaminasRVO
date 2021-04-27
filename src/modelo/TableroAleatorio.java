package modelo;

import java.util.ArrayList;

import utiles.RespuestaColocacion;
import utiles.Utiles;

public class TableroAleatorio extends Tablero {

	private final int casillasAlrededor = 8;
	private boolean terminado = false;

	// Constructor aleatorio
	public TableroAleatorio(int lado, int minas) {
		super(lado);
		ArrayList<Coordenada> posiciones = generaAleatorio(minas, lado);
		disponerTablero(posiciones);
		
		
	}

	// constructor no aleatorio
	public TableroAleatorio(int lado, ArrayList<Coordenada> posiciones) {
		super(lado);
		disponerTablero(posiciones);
	}

	private void disponerTablero(ArrayList<Coordenada> posiciones) {
		colocarMinas(posiciones);
		contarMinasAlrededor(posiciones);
		
		
	}

	public void contarMinasAlrededor(ArrayList<Coordenada> posiciones) {

		for (Coordenada coordenada : posiciones) {

			for (int i = 0; i < casillasAlrededor; i++) {
				int[] posicion = Utiles.damePosicionAlrededor(i);
				int xAuxiliar = coordenada.getPosX() + posicion[0];
				int yAuxiliar = coordenada.getPosY() + posicion[1];
				if (isIntoLimits(xAuxiliar, yAuxiliar)) {
//					System.out.println("X " +xAuxiliar+ "Y " +yAuxiliar);
					Casilla casilla = getCasilla(new Coordenada(xAuxiliar, yAuxiliar));
					if (!casilla.isMina()) {

						casilla.setMinasAlrededor(casilla.getMinasAlrededor() + 1);
					}
				}

			}

		}
	}

	private boolean isIntoLimits(int xAuxiliar, int yAuxiliar) {
		return xAuxiliar >= 0 && yAuxiliar >= 0 && xAuxiliar < getAlto() && yAuxiliar < getAncho();
	}

	private void colocarMinas(ArrayList<Coordenada> posiciones) {
		for (Coordenada coordenada : posiciones) {
			ponerMina(coordenada);
		}
	}

	private void ponerMina(Coordenada coordenada) {
		getCasilla(coordenada).setMina(true);
	}

	public ArrayList<Coordenada> generaAleatorio(int minas, int longitud) {
		assert minas > 0 && longitud > 0;
		assert minas < longitud * longitud;
//		long inicio = System.currentTimeMillis();
		ArrayList<Coordenada> coordenadas = new ArrayList<Coordenada>();
		for (int i = 0; i < minas; i++) {
			Coordenada coord;
			do {
				coord = dameCoordenada(longitud);
			} while (existeCoord(coord, coordenadas));
			coordenadas.add(coord);
		}
//		 long fin = System.currentTimeMillis();
//		 System.out.println("en milis "+(fin-inicio));
		return coordenadas;

	}

	private Coordenada dameCoordenada(int lado) {
		return new Coordenada(Utiles.dameNumero(lado), Utiles.dameNumero(lado));
	}

	private boolean existeCoord(Coordenada coord, ArrayList<Coordenada> coordenadas) {
		for (int i = 0; i < coordenadas.size(); i++) {
			if (coord.equals(coordenadas.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	public void desvelarCasillas(Coordenada lugar) {
        if (getCasilla(lugar).isVelada()&&!getCasilla(lugar).isMarcada()) {
            getCasilla(lugar).setVelada(false);
            if (getCasilla(lugar).isMina()) {
                this.terminado = true;
            } else {
                if (getCasilla(lugar).getMinasAlrededor() == 0) {
                    // proceso recursivo
                    recursivoDesvelarCasillas(lugar);
                }
            }
        }else {
            //si alrededor tiene tantas casillas marcadas como minas alrededor tiene la propia casilla
            //si el caso anterior es negativo NADA QUE HACER
            //si es positivo
            //repito el proceso de arriba
            if(getCasilla(lugar).getMinasAlrededor() == getCasilla(lugar).getMarcadasAlrededor()) {
                recursivoDesvelarCasillas(lugar);

            }
        }
    }

    private void recursivoDesvelarCasillas(Coordenada lugar) {
        int alrededor = 8;
        for (int i = 0; i < alrededor; i++) {
            int[] coordenada = Utiles.damePosicionAlrededor(i);
            int posX = lugar.getPosX() + coordenada[0];
            int posY = lugar.getPosY() + coordenada[1];
            Coordenada lugarRelativo = new Coordenada(posX,posY);

            if (lugarRelativo.isInToLimits(getAncho(),getAlto())) {
                desvelarCasillas(lugarRelativo);
            }
        }
    }
    public boolean[][] getCasillasDesveladas(){
        boolean resultados[][] = new boolean[getAlto()][getAncho()];
        for (int i = 0; i < resultados.length; i++) {
            for (int j = 0; j < resultados[i].length; j++) {
            resultados[i][j]= getCasilla(new Coordenada(i,j)).isVelada();
            }
        }
        return resultados;
    }

//	public ArrayList<RespuestaColocacion> desvelarCasillas(Coordenada coordenada) {
//		ArrayList<RespuestaColocacion> arrayRespuestasDesveladas= new ArrayList<>();
//		
//		if (getCasilla(coordenada).isVelada()) {
//			getCasilla(coordenada).setVelada(false);
//
//			if (!getCasilla(coordenada).isMina() && getCasilla(coordenada).getMinasAlrededor() == 0) {
//				for (int i = 0; i < casillasAlrededor; i++) {
//					int[] posicion = Utiles.damePosicionAlrededor(i);
//					int xAuxiliar = coordenada.getPosX() + posicion[0];
//					int yAuxiliar = coordenada.getPosY() + posicion[1];
//					if (isIntoLimits(xAuxiliar, yAuxiliar)) {
//						
//						Coordenada coord =new Coordenada(xAuxiliar, yAuxiliar);
//						
//						if(getCasilla(coord).isVelada()) {
//							getCasilla(coord).setVelada(false);
//							int minasAlrededor= getCasilla(coord).getMinasAlrededor();
//							arrayRespuestasDesveladas.add(new RespuestaColocacion(true, String.valueOf(minasAlrededor), coord));
//						}
//						
//					}
//
//				}
//
//			}
//		}
//		return arrayRespuestasDesveladas;
//
//	}
	
	

}

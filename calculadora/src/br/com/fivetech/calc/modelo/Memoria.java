package br.com.fivetech.calc.modelo;

import java.util.ArrayList;
import java.util.List;

public class Memoria {

    private enum TipoComando{
      ZERAR, MUDARSINAL, NUMERO, DIVISAO, MULTIPLICACAO, SUBTRACAO, SOMA, IGUAL, VIRGULA
    };

    private static Memoria instancia = new Memoria();

    private final List<MemoriaObservador> observadores = new ArrayList<>();

    private TipoComando ultimaOperacao = null;
    private boolean substituir = false;
    private String textoAtual = "";
    private String textoBuffer = ""; //buffer

    private Memoria(){ }

    public static Memoria getInstancia(){
        return instancia;
    }

    public void adicionarObservador(MemoriaObservador observador){
        observadores.add(observador);
    }

    public String getTextoAtual(){
        return textoAtual.isEmpty() ? "0" : textoAtual;
    }

    public void processarComando(String texto){

        TipoComando tipoComando = detectarTipoComando(texto);
        //System.out.println(tipoComando);
        if (tipoComando == null){
            return;
        } else if(tipoComando == TipoComando.ZERAR){
            textoAtual = "";
            textoBuffer = "";
            substituir = false;
            ultimaOperacao = null;
        } else if (tipoComando == TipoComando.MUDARSINAL && textoAtual.contains("-")){
            textoAtual = textoAtual.substring(1);
        } else if (tipoComando == TipoComando.MUDARSINAL && !textoAtual.contains("-")) {
            textoAtual = "-" + textoAtual;
        } else if (tipoComando == TipoComando.NUMERO
                || tipoComando == TipoComando.VIRGULA) {
            textoAtual = substituir ? texto : textoAtual + texto;
            substituir = false;
        } else {
            substituir = true;
            textoAtual = obterResultadoDaOperacao();
            textoBuffer = textoAtual;
            ultimaOperacao = tipoComando;
        }


        observadores.forEach(o -> o.valorAlterado(getTextoAtual()));
    }

    private String obterResultadoDaOperacao() {
        if(ultimaOperacao == null || ultimaOperacao == TipoComando.IGUAL){
            return textoAtual;
        }

        double numeroBuffer = Double.parseDouble(textoBuffer.replace(",", "."));
        double numeroAtual = Double.parseDouble(textoAtual.replace(",", "."));

        double resultado = 0;

        if (ultimaOperacao == TipoComando.SOMA){
            resultado = numeroBuffer + numeroAtual;
        } else if (ultimaOperacao == TipoComando.SUBTRACAO) {
            resultado = numeroBuffer - numeroAtual;
        }else if (ultimaOperacao == TipoComando.MULTIPLICACAO) {
            resultado = numeroBuffer * numeroAtual;
        }else if (ultimaOperacao == TipoComando.DIVISAO) {
            resultado = numeroBuffer / numeroAtual;
        }

        String textoResultado = Double.toString(resultado).replace(".",",");
        boolean inteiro = textoResultado.endsWith(",0");
        return inteiro ? textoResultado.replace(",0", "") : textoResultado;
    }

    private TipoComando detectarTipoComando(String texto) {
        if (textoAtual.isEmpty() && texto == "0"){ //eliminar a repeticao do zero
            return null;
        }

        try {
            Integer.parseInt(texto);
            return TipoComando.NUMERO;
        } catch (NumberFormatException e){
            //quando nao for numero:
            if ("AC".equals(texto)){
                return TipoComando.ZERAR;
            } else if ("/".equals(texto)) {
                return TipoComando.DIVISAO;
            } else if ("*".equals(texto)) {
                return TipoComando.MULTIPLICACAO;
            } else if ("+".equals(texto)) {
                return TipoComando.SOMA;
            } else if ("-".equals(texto)) {
                return TipoComando.SUBTRACAO;
            } else if ("=".equals(texto)) {
                return TipoComando.IGUAL;
            }else if ("±".equals(texto)) {
                return TipoComando.MUDARSINAL;
            } else if (",".equals(texto) && !textoAtual.contains(",")) {
                return TipoComando.VIRGULA;
            }
        }
        return null;
    }
}























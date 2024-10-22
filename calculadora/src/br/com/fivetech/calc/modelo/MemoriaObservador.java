package br.com.fivetech.calc.modelo;

@FunctionalInterface
public interface MemoriaObservador {

    void valorAlterado(String novoValor);
}

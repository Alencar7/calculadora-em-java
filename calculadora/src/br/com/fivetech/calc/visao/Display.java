package br.com.fivetech.calc.visao;

import br.com.fivetech.calc.modelo.Memoria;
import br.com.fivetech.calc.modelo.MemoriaObservador;

import javax.swing.*;
import java.awt.*;

public class Display extends JPanel implements MemoriaObservador {

    private final JLabel label;


    public Display(){
        Memoria.getInstancia().adicionarObservador(this);

        setBackground(new Color(46,49, 55));
        label = new JLabel(Memoria.getInstancia().getTextoAtual());
        label.setForeground(Color.WHITE);
        label.setFont(new Font("courier", Font.PLAIN, 30));

        setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 50));

        add(label);
        //setBackground(Color.GREEN);
    }

    public void valorAlterado(String novoValor){
        label.setText(novoValor);
    }
}

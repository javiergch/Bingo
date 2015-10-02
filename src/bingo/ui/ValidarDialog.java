package bingo.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import bingo.modelo.Bingo;

@SuppressWarnings("serial")
public class ValidarDialog extends JDialog {
	private Bingo bingo;
	private JPanel nortePanel, surPanel;
	private JScrollPane centroPanel;
	private JTextArea textArea;
	
	public ValidarDialog(JFrame parent, Bingo bingo) {		
		this.bingo = bingo;
		
		initDialog(parent);
		initComponents();
	}

	private void initDialog(JFrame parent) {
		setTitle("Validar cartón");
		setSize(480, 320);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(parent);
		setModal(true);
	}

	private void initComponents() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				
		nortePanel = initNortePanel();
		centroPanel = initCentroPanel();
		surPanel = initSurPanel();
		
		panel.add(nortePanel, BorderLayout.NORTH);
		panel.add(centroPanel, BorderLayout.CENTER);
		panel.add(surPanel, BorderLayout.SOUTH);
		
		getContentPane().add(panel, BorderLayout.CENTER);

		//getContentPane().add(nortePanel, BorderLayout.NORTH);
		//getContentPane().add(centroPanel, BorderLayout.CENTER);
		//getContentPane().add(surPanel, BorderLayout.SOUTH);
	}

	private JPanel initNortePanel() {
		JPanel nortePanel = new JPanel(new GridLayout(0,1,5,5));
		nortePanel.setBorder(BorderFactory.createEmptyBorder(1, 0, 10, 0));
		JLabel label = new JLabel("Introduce los números de la línea o del bingo para comprobar si ya han salido:", SwingConstants.LEFT);
		nortePanel.add(label);

		return nortePanel;
	}

	private JScrollPane initCentroPanel() {
		textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea);
		
		return scrollPane;
	}

	private JPanel initSurPanel() {
		JPanel surPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,1,1));
		surPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		
		JButton buttonValidar = new JButton("Validar");
		buttonValidar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onButtonValidarActionPerformed(e);
			}
		});
		
		JButton buttonCancelar = new JButton("Cancelar");
		buttonCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onButtonCancelarActionPerformed(e);
			}
		});
		
		surPanel.add(buttonValidar);
		surPanel.add(buttonCancelar);

		return surPanel;
	}

	protected void onButtonValidarActionPerformed(ActionEvent e) {		
		List<Integer> numeros = new ArrayList<Integer>();
		Boolean validar = true;
		Boolean resultado = false;
		
		try {
			String[] lineas = textArea.getText().split("\\n"); 
			for (int i = 0; i < lineas.length; i++) {
				numeros.add(Integer.parseInt(lineas[i]));
			}
		} catch(NumberFormatException ex) {
			validar = false;
			System.out.println(ex.getMessage());
		}
		
		if(validar)
			resultado = bingo.validar(numeros);
		else
			JOptionPane.showMessageDialog(this, "No se permite texto","Validar incorrecto", JOptionPane.ERROR_MESSAGE);
		
		if(resultado) {
			JOptionPane.showMessageDialog(this, "Los números introducidos están en el panel","Validar correcto", JOptionPane.INFORMATION_MESSAGE);
		}
		else if(validar) {
			JOptionPane.showMessageDialog(this, "Los números introducidos NO se encuentran en el panel","Validar incorrecto", JOptionPane.ERROR_MESSAGE);
		}
	}

	protected void onButtonCancelarActionPerformed(ActionEvent e) {
		dispose();
	}
}
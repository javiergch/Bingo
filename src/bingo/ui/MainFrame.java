package bingo.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bingo.modelo.Bingo;
import bingo.modelo.Bola;
import bingo.utils.Voces;
import dad.swingutils.MyConfirmOptionPaneDialog;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	private JBola[] panelBolas;
	private JBola ultimaBola;
	private JPanel centroPanel, derechaPanel, ultimaBolaPanel;
	private JButton buttonIniciar, buttonDetener, buttonReiniciar;
	private Bingo bingo;
	private JSlider sliderTimer;
	private Timer timer;

	public MainFrame() {
		initFrame();
		initComponents();
	}

	private void initFrame() {
		setTitle("Bingo");
		setSize(1015,635);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				onWindowClosing(e);
			}
		});
	}

	private void initComponents() {
		bingo = new Bingo();
		ultimaBola = new JBola();
		ultimaBola.setMarcado(true);
		
		
		
		centroPanel = initCentroPanel();
		derechaPanel = initDerechaPanel();
		
		getContentPane().add(centroPanel, BorderLayout.CENTER);
		getContentPane().add(derechaPanel, BorderLayout.EAST);
	}


	private JPanel initCentroPanel() {
		JPanel centroPanel = new JPanel();
		centroPanel.setBorder(BorderFactory.createTitledBorder("Panel"));
		centroPanel.setLayout(new GridLayout(0,10));	
		
		panelBolas = new JBola[90];
		for (int i = 0; i < panelBolas.length; i++) {
			JBola bola = new JBola();
			bola.setNumero(i+1);
			bola.setMarcado(false);		
			
			panelBolas[i] = bola;
			centroPanel.add(bola);
		}

		return centroPanel;
	}

	private JPanel initDerechaPanel() {
		JPanel derechaPanel = new JPanel();
		derechaPanel.setLayout(new BorderLayout());
		
		//
		ultimaBolaPanel = new JPanel();
		ultimaBolaPanel.setBorder(BorderFactory.createTitledBorder("Última bola"));
		ultimaBolaPanel.setLayout(new GridLayout());
		///
		
		//
		JPanel controlesPanel = new JPanel(new GridLayout(0,1,5,5));
		JButton buttonValidar = new JButton("Validar cartón");
		buttonValidar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onButtonValidarActionPerformed(e);
			}
		});
		
		buttonIniciar = new JButton("Iniciar");
		buttonIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onButtonIniciarActionPerformed(e);
			}
		});
		
		buttonDetener = new JButton("Detener");
		buttonDetener.setEnabled(false);
		buttonDetener.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onButtonDetenerActionPerformed(e);
			}
		});
		
		buttonReiniciar = new JButton("Reiniciar");
		buttonReiniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onButtonReiniciarActionPerformed(e);
			}
		});
		
		JButton buttonSalir = new JButton("Salir");
		buttonSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onButtonSalirActionPerformed(e);
			}
		});
			
		sliderTimer = new JSlider();
		sliderTimer.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				onSliderTimerStateChanged(e);
			}
		});
		sliderTimer.setOpaque(false);
		sliderTimer.setMinimum(1);
		sliderTimer.setMaximum(10);
		sliderTimer.setValue(5);
		sliderTimer.setPaintLabels(true);
		sliderTimer.setPaintTicks(true);
		sliderTimer.setMajorTickSpacing(1);
		
		controlesPanel.add(new JLabel());
		controlesPanel.add(new JLabel());
		controlesPanel.add(buttonValidar);
		controlesPanel.add(sliderTimer);
		controlesPanel.add(buttonIniciar);
		controlesPanel.add(buttonDetener);
		controlesPanel.add(buttonReiniciar);
		controlesPanel.add(buttonSalir);
		///
		
		
		derechaPanel.add(ultimaBolaPanel, BorderLayout.CENTER);
		derechaPanel.add(controlesPanel, BorderLayout.SOUTH);
		
		return derechaPanel;
	}

	protected void onSliderTimerStateChanged(ChangeEvent e) {		
		try { timer.setDelay(sliderTimer.getValue()*1000); } catch (NullPointerException ex) { }
	}
	
	protected void onTimerActionPerformed(ActionEvent e) {		
		sacarBola();		
	}

	protected void onButtonValidarActionPerformed(ActionEvent e) {
		onButtonDetenerActionPerformed(null);
		new ValidarDialog(this, bingo).setVisible(true);
	}

	protected void onButtonIniciarActionPerformed(ActionEvent e) {
		buttonIniciar.setEnabled(false);
		buttonDetener.setEnabled(true);
		
		timer = new Timer(sliderTimer.getValue()*1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onTimerActionPerformed(e);
			}
		});	

		timer.start();

		ultimaBolaPanel.add(ultimaBola);
		ultimaBolaPanel.revalidate();
		sacarBola();
	}
	
	private void sacarBola() {
		if(bingo.quedanBolas()) {
			Bola bola = bingo.sacarBola();
			Integer numero = bola.getNumero();
			panelBolas[numero-1].setMarcado(true);
			ultimaBola.setNumero(numero);
			Voces.reproducir(numero);
			
			//System.out.println("Bolas en panel : " + bingo.getPanel().getBolas());
		}
		else {
			timer.stop();
			buttonDetener.setEnabled(false);
			JOptionPane.showMessageDialog(this, "No quedan bolas en el bombo","Fin de la partida", JOptionPane.INFORMATION_MESSAGE);
		}
		
	}

	protected void onButtonDetenerActionPerformed(ActionEvent e) {	
		try {
		buttonDetener.setEnabled(false);
		buttonIniciar.setEnabled(true);
		timer.stop();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	protected void onButtonReiniciarActionPerformed(ActionEvent e) {		
		buttonDetener.setEnabled(false);
		buttonIniciar.setEnabled(true);
		
		try {
			timer.stop();
		} catch (NullPointerException ex) {
			System.out.println("Error en Timer: " + ex.getMessage());
		}
		
		for (int i = 0; i < panelBolas.length; i++) {
			panelBolas[i].setMarcado(false);
		}
		
		ultimaBolaPanel.removeAll();
		ultimaBolaPanel.repaint();;

		bingo.reiniciar();
	}

	protected void onButtonSalirActionPerformed(ActionEvent e) {
		onWindowClosing(null);
	}
	
	protected void onWindowClosing(WindowEvent e) {
		int respuesta = new MyConfirmOptionPaneDialog("Salir", "¿Desea salir del bingo?").yesNoConfirmDialog();
		
		if(respuesta == JOptionPane.YES_OPTION)	{
			try {
				timer.stop();
			} catch (NullPointerException ex) { }
			finally {
				dispose();
			}
		}
	}
}
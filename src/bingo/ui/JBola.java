package bingo.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class JBola extends JComponent {

	private Integer numero;
	private Boolean marcado;

	public JBola() {
		super();
		this.numero = 0;
		this.marcado = false;
		setFont(new Font("Arial", Font.PLAIN, 18));
		setForeground(Color.BLACK);
		setBackground(Color.WHITE);
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
		repaint();
	}

	public Boolean getMarcado() {
		return marcado;
	}

	public void setMarcado(Boolean marcado) {
		this.marcado = marcado;
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int x = 5;
		int y = 5;
		int width = getWidth() - 2 * x;
		int height = getHeight() - 2 * y;
		
		if (width > height) {
			width = height;
			x = (getWidth() - width) / 2;
			g2.setFont(getFont().deriveFont((float)(getHeight() / 1.6)));
		} else {
			height = width;
			y = (getHeight() - height) / 2;
			g2.setFont(getFont().deriveFont((float)(getWidth() / 1.6)));
		}
		
		if (marcado) {
			g2.setColor(getBackground());
			g2.fillOval(x, y, width, height);
			g2.setColor(getForeground());
			g2.drawOval(x, y, width, height);
		} else {
			g2.setColor(getForeground());
			g2.fillOval(x, y, width, height);
			g2.setColor(getBackground()); 
		}
		
		String texto = "" + numero;
		
		int textWidth = g2.getFontMetrics().stringWidth(texto);
		int textHeight = g2.getFontMetrics().getHeight();
		
		int xTexto = (getWidth() - textWidth) / 2;
		int yTexto = (getHeight() / 2) + (textHeight / 3);
		
		g2.drawString(texto, xTexto, yTexto);		
	}

}



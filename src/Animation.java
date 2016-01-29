
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")

public class Animation extends JPanel
{	
	public static int dimension = 700;
	
	public A0 observer = new A0();
	
	public void Run()
	{
		JFrame F = new JFrame();
		
		//F.add(new Animation());
		Animation animation = new Animation();
		F.add(animation);
		
		F.setSize(dimension,dimension);
		F.setVisible(true);
		F.setResizable(false);
		F.setTitle("Projekt na Systemy Wieloagentowe");		
		F.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	int i = 0;
	int j = dimension/2-100;
	int m = (dimension-1024)/2;
	int k = 0;
	
	int draw = 0;
	
	public void paintComponent(Graphics g)
	{
		// Wczytujemy obrazy
		BufferedImage Picture = LoadImage("Images\\lazik.png");
		BufferedImage Background = LoadImage("Images\\Trawa.jpg");
		
		// Ustalamy po³o¿enie i skalê obrazów
		AffineTransform at = AffineTransform.getTranslateInstance(j,j);
		AffineTransform at2 = AffineTransform.getTranslateInstance(m,m);
		at.scale(1,1);
		
		if (observer.GetReadyToMove() == true)
		{
			// Ruch do przodu
			if(observer.moveLogicTable[0] == true)
			{
				draw = 1;
				j--;
			}
			// Ruch do ty³u
			if(observer.moveLogicTable[1] == true)
			{
				draw = 1;
				j++;
			}
			// Skrêt w prawo
			if(observer.moveLogicTable[2] == true)
			{
				draw = 1;
				k--;
			}
			// Skrêt w lewo
			if(observer.moveLogicTable[3] == true)
			{
				draw = 1;
				k++;
			}
		}
		else
		{
			// Brak ruchu
			if (observer.moveLogicTable[0] == false && observer.moveLogicTable[1] == false 
					&& observer.moveLogicTable[2] == false && observer.moveLogicTable[3] == false)
			{
				draw = 1;
			}	
		}
		
		
				
		// Inicjalizacja modu³u grafiki 2D
		Graphics2D g2d = (Graphics2D) g;
		
		// W³¹czenie wyg³adzenia i interpolacji
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		
		if (draw == 1)
		{
			// Przesuwanie obrazami w góre lub dó³
			at2 = AffineTransform.getTranslateInstance((dimension-1024)/2,m);
			at = AffineTransform.getTranslateInstance(dimension/2-100,j);
			
			// Obracanie obrazami w lewo i prawo
			at2.rotate(Math.toRadians(k), Background.getWidth()/2, Background.getHeight()/2);
			at.rotate(Math.toRadians(i), Picture.getWidth()/2, Picture.getHeight()/2);
			
			// Malowanie obrazów		
			g2d.drawImage(Background, at2, null);
			g2d.drawImage(Picture, at, null);
			repaint();
		}
		
	}
	
	// Weryfikacja istnienia obrazków
	BufferedImage LoadImage(String FileName)
	{
		BufferedImage img = null;
		
		try{
		img = ImageIO.read(new File(FileName));
		}catch(IOException e){
			System.err.println("Blad odczytu obrazka");
			e.printStackTrace();
		}
		
		return img;		
	}
	
	//****************************************************************************************
	
	// Tworzymy now¹ klase wewnêtrzn¹ implementuj¹c¹ KeyListener
	public class Control implements KeyListener
	{
		@Override
		public void keyPressed(KeyEvent e) 
		{
			System.out.println("Animation: " + observer.GetReadyToWork());
			if (observer.GetReadyToWork() == true)
			{
				int keyCode = e.getKeyCode();
				
				switch(keyCode)
				{
				case KeyEvent.VK_UP:
					observer.SetValueInMoveLogoicTable(true, false, false, false);
					//System.out.println("UP");
					break;
				case KeyEvent.VK_DOWN:
					observer.SetValueInMoveLogoicTable(false, true, false, false);
					//System.out.println("DOWN");
					break;
				case KeyEvent.VK_RIGHT:
					observer.SetValueInMoveLogoicTable(false, false, true, false);
					//System.out.println("RIGHT");
					break;
				case KeyEvent.VK_LEFT:
					observer.SetValueInMoveLogoicTable(false, false, false, true);
					//System.out.println("LEFT");
					break;
				case KeyEvent.VK_SPACE:
					observer.SetValueInMoveLogoicTable(false, false, false, false);
					//System.out.println("STOP");
					break;
				}
			}			
		}

		@Override
		public void keyReleased(KeyEvent e) 
		{
			int keyCode = e.getKeyCode();
			
			switch(keyCode)
			{
			case KeyEvent.VK_UP:
				System.out.println("UP");
				break;
			case KeyEvent.VK_DOWN:
				System.out.println("DOWN");
				break;
			case KeyEvent.VK_RIGHT:
				System.out.println("RIGHT");
				break;
			case KeyEvent.VK_LEFT:
				System.out.println("LEFT");
				break;
			case KeyEvent.VK_SPACE:
				System.out.println("STOP");
				break;
			}	
		}

		@Override
		public void keyTyped(KeyEvent e) 
		{
			// TODO Auto-generated method stub			
		}		
	}
	
	// Konstuktor inicjalizuje nam klase Control w naszej klasie
	public Animation()
	{
		KeyListener listener = new Control();
		addKeyListener(listener);
		setFocusable(true);
	}

}
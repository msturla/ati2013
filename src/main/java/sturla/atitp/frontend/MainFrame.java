package sturla.atitp.frontend;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.sanselan.ImageReadException;

import sturla.atitp.app.ImageLoader;
import sturla.atitp.frontend.imageops.ImageOperation;
import sturla.atitp.frontend.imageops.ImageOperationParameters;
import sturla.atitp.frontend.menus.arithmeticMenu.ArithmeticMenu;
import sturla.atitp.frontend.menus.edgeDetectorsMenu.EdgeDetectorsMenu;
import sturla.atitp.frontend.menus.extraMenu.ExtraMenu;
import sturla.atitp.frontend.menus.fileMenu.FileMenu;
import sturla.atitp.frontend.menus.masksMenu.MasksMenu;
import sturla.atitp.frontend.menus.noiseMenu.NoiseMenu;
import sturla.atitp.frontend.menus.umbralizationMenu.UmbralizationMenu;
import sturla.atitp.imageprocessing.Point;
import sturla.atitp.imageprocessing.edgeDetector.LeclercEdgeDetector;
import sturla.atitp.imageprocessing.edgeDetector.LorentzEdgeDetector;
import sturla.atitp.imageprocessing.synthesization.SynthesizationType;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -4569949105927376584L;

	private JPanel panel;
	public ImageLabelContainer currentImageLabel;
	// used for binary operations
	public ImageLabelContainer secondImageLabel;
	public ImageLabelContainer resultImageLabel;
	
	public File lastImageFile;

	public ImageOperation currOperation;

	private JSlider parameterSlider1;
	private JTextArea parameterField1;
	public JSlider parameterSlider2;
	public JTextArea parameterField2;

	public JTextField coordX1;
	private JTextField coordY1;
	public JTextField coordX2;
	private JTextField coordY2;
	public JTextField maskSize;
	public JTextField value1;
	public JTextField value2;
	
	private JTextField rectWidth;
	private JTextField rectHeight;

	private JButton doItButton;
	public DraggableComponent rectangle;

	public JRadioButton leclercRadioButton;
	public JRadioButton lorentzRadioButton;

	public JRadioButton absRadioButton;
	public JRadioButton avgRadioButton;
	public JRadioButton minRadioButton;
	public JRadioButton maxRadioButton;
	
	public boolean onRectangleMoveDo = true;

	private List<Point> thetas;

	public MainFrame() {
		initUI();
	}

	private void initUI() {
		panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(null);
		initMenu();
		initTextFields();
		initSliders();
		initEdgeDetectorRadioButtions();
		initSynthetizationRadioButtons();
		setTitle("ATI: TP 1");
		setSize(2000, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void initEdgeDetectorRadioButtions() {
		leclercRadioButton = new JRadioButton("Leclerc", true);
		lorentzRadioButton = new JRadioButton("Lorentz");

		leclercRadioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				leclercRadioButton.setSelected(true);
				lorentzRadioButton.setSelected(!leclercRadioButton.isSelected());
			}
		});

		lorentzRadioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				lorentzRadioButton.setSelected(true);
				leclercRadioButton.setSelected(!lorentzRadioButton.isSelected());
			}
		});		
	}

	private void initSynthetizationRadioButtons() {
		maxRadioButton = new JRadioButton("Maximum");
		minRadioButton = new JRadioButton("Minimum");
		avgRadioButton = new JRadioButton("Average");
		absRadioButton = new JRadioButton("Norm 2", true);

		maxRadioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				maxRadioButton.setSelected(true);
				minRadioButton.setSelected(!maxRadioButton.isSelected());
				avgRadioButton.setSelected(!maxRadioButton.isSelected());
				absRadioButton.setSelected(!maxRadioButton.isSelected());
			}
		});

		minRadioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				minRadioButton.setSelected(true);
				maxRadioButton.setSelected(!minRadioButton.isSelected());
				avgRadioButton.setSelected(!minRadioButton.isSelected());
				absRadioButton.setSelected(!minRadioButton.isSelected());
			}
		});
		
		avgRadioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				avgRadioButton.setSelected(true);
				maxRadioButton.setSelected(!avgRadioButton.isSelected());
				minRadioButton.setSelected(!avgRadioButton.isSelected());
				absRadioButton.setSelected(!avgRadioButton.isSelected());
			}
		});

		absRadioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				absRadioButton.setSelected(true);
				maxRadioButton.setSelected(!absRadioButton.isSelected());
				minRadioButton.setSelected(!absRadioButton.isSelected());
				avgRadioButton.setSelected(!absRadioButton.isSelected());
			}
		});		
	}

	private void initTextFields() {
		coordX1 = new JTextField("0");
		coordY1 = new JTextField("0");
		coordX2 = new JTextField("0");
		coordY2 = new JTextField("0");
		rectangle = new DraggableComponent();
		rectWidth = new JTextField("20");
		rectHeight = new JTextField("20");
		maskSize = new JTextField("5");
		value1 = new JTextField("Value1");
		value2 = new JTextField("Value2");
				
		ActionListener act = new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	int w = Integer.parseInt(rectWidth.getText());
		    	int h = Integer.parseInt(rectHeight.getText());
		    	rectangle.setDimensions(w, h);
		     }
		};
		rectWidth.addActionListener(act);
		rectHeight.addActionListener(act);
		coordX1.setBounds(200, 550, 30, 30);
		coordX2.setBounds(200, 600, 30, 30);
		coordY1.setBounds(250, 550, 30, 30);
		coordY2.setBounds(250, 600, 30, 30);
		rectWidth.setBounds(300, 600, 30, 30);
		rectHeight.setBounds(350, 600, 30, 30);
		maskSize.setBounds(400, 600, 30, 30);
		value1.setBounds(450, 600, 30, 30);
		value2.setBounds(500, 600, 30, 30);
		
		panel.add(maskSize);
		panel.add(rectWidth);
		panel.add(rectHeight);
		panel.add(rectangle);
		panel.add(coordX1);
		panel.add(coordY1);
		panel.add(coordX2);
		panel.add(coordY2);
		panel.add(value1);
		panel.add(value2);
		displayTextFields(false);
		value1.setVisible(false);
		value2.setVisible(false);
	}
	
	private void setCoords(int x1, int y1, int x2, int y2) {
		coordX1.setText(String.valueOf(x1));
		coordX2.setText(String.valueOf(x2));
		coordY1.setText(String.valueOf(y1));
		coordY2.setText(String.valueOf(y2));
	}

	public void displayTextFields(boolean b) {
		coordX1.setVisible(b);
		coordY1.setVisible(b);
		coordX2.setVisible(b);
		coordY2.setVisible(b);
		rectangle.setVisible(b);
		rectWidth.setVisible(b);
		rectHeight.setVisible(b);
		maskSize.setVisible(b);
		value1.setVisible(false);
		value2.setVisible(false);
	}

	private void initSliders() {
		parameterSlider1 = new JSlider();
		parameterField1 = new JTextArea();
		parameterField1.setBounds(200, 600, 100, 100);
		parameterField1.setVisible(false);
		panel.add(parameterField1);
		parameterSlider2 = new JSlider();
		parameterField2 = new JTextArea();
		parameterField2.setBounds(500, 600, 100, 100);
		parameterField2.setVisible(false);
		panel.add(parameterField2);
		doItButton = new JButton("Do it!");
		doItButton.setBounds(950, 550, 100, 50);
		doItButton.setVisible(true);
		doItButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				MainFrame.this.doOperation();
			}
		});
		panel.add(doItButton);
	}

	private void doOperation() {
		if (currOperation != null) {
			currOperation.performOperation(currentImageLabel, secondImageLabel,
					resultImageLabel, getParams());
		}
	}

	private ImageOperationParameters getParams() {
		ImageOperationParameters params = new ImageOperationParameters();
		if (parameterSlider1.isVisible()) {
			params.value = (double) parameterSlider1.getValue() / 200;
		}
		if (parameterSlider2.isVisible()) {
			params.value2 = (double) parameterSlider2.getValue() / 200;
		}
		if (coordX1.isVisible()) {
			params.x1 = Integer.valueOf(coordX1.getText());
		}
		if (coordX2.isVisible()) {
			params.x2 = Integer.valueOf(coordX2.getText());
		}
		if (coordY1.isVisible()) {
			params.y1 = Integer.valueOf(coordY1.getText());
		}
		if (coordY2.isVisible()) {
			params.y2 = Integer.valueOf(coordY2.getText());
		}
		if (maskSize.isVisible()) {
			params.maskSize = Integer.valueOf(maskSize.getText());
		}
		if (value1.isVisible()) {
			params.value = Double.valueOf(value1.getText());
		}
		if (value2.isVisible()) {
			params.value2 = Double.valueOf(value2.getText());
		}
		if(leclercRadioButton.isSelected()) {
			params.bd = new LeclercEdgeDetector(params.value2);	//Without explicit cast everything goes BADDD
		} else if(lorentzRadioButton.isSelected()) {
			params.bd = new LorentzEdgeDetector(params.value2);	//Without explicit cast everything goes BADDDD
		}
		if(maxRadioButton.isSelected()) {
			params.st = SynthesizationType.MAX;
		} else if(minRadioButton.isSelected()) {
			params.st = SynthesizationType.MIN;
		} else if(avgRadioButton.isSelected()) {
			params.st = SynthesizationType.AVG;
		} else if(absRadioButton.isSelected()) {
			params.st = SynthesizationType.ABS;
		}
		
		params.imageFile = lastImageFile;
		params.mainFrame  = this;
		
		
		return params;
	}

	private void initMenu() {
		JMenuBar menuBar = new JMenuBar();

		menuBar.add(new FileMenu(this));
		menuBar.add(new NoiseMenu(this));		
		menuBar.add(new ArithmeticMenu(this));
		menuBar.add(new MasksMenu(this));
		menuBar.add(new EdgeDetectorsMenu(this));
		menuBar.add(new UmbralizationMenu(this));
		menuBar.add(new ExtraMenu(this));
		
		currentImageLabel = new ImageLabelContainer(this, this.panel);
		secondImageLabel = new ImageLabelContainer(this, this.panel);
		resultImageLabel = new ImageLabelContainer(this, this.panel);

		setJMenuBar(menuBar);
		panel.repaint();
	}

	public void hideSliders() {
		parameterSlider2.setVisible(false);
		parameterSlider1.setVisible(false);
		parameterField1.setVisible(false);
		parameterField2.setVisible(false);
	}

	public void setCurrentImage(File file) {
		try {
			currentImageLabel.setImage(ImageLoader.loadImage(file));
		} catch (IOException e) {
			System.out.println("OE");
		} catch (ImageReadException e) {
			System.out.println("OE2");
		}
	}

	public void setSecondImage(File file) {
		try {
			secondImageLabel.setImage(ImageLoader.loadImage(file));
		} catch (IOException e) {
			System.out.println("OE");
		} catch (ImageReadException e) {
			System.out.println("OE2");
		}
	}

	public void setFirstSlider(int minValue, int maxValue, int initValue) {
		panel.remove(parameterSlider1);
		secondImageLabel.removeImage();
		parameterSlider1 = new JSlider(JSlider.HORIZONTAL, minValue, maxValue,
				initValue);
		parameterField1.setVisible(true);
		parameterField1.setText(String.format("%.4g", (double) initValue / 200));
		parameterSlider1.setBounds(200, 550, 300, 50);
		parameterSlider1.setVisible(true);
		parameterSlider1.setLayout(null);
		parameterSlider1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				parameterField1.setText(String.format("%.4g", (double) MainFrame.this.parameterSlider1.getValue() / 200));
				parameterField1.repaint();
				MainFrame.this.doOperation();
			}

		});
		this.panel.add(parameterSlider1);
	}

	public void setSecondSlider(int minValue, int maxValue, int initValue) {
		parameterSlider2 = new JSlider(JSlider.HORIZONTAL, minValue, maxValue,
				initValue);
		parameterSlider2.setBounds(500, 550, 300, 50);
		parameterSlider2.setVisible(true);
		parameterField2.setVisible(true);
		parameterSlider2.setLayout(null);
		parameterSlider2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				parameterField2.setText(String
						.format("%.4g",
								(double) MainFrame.this.parameterSlider2
										.getValue() / 200));
				parameterField2.repaint();
				MainFrame.this.doOperation();
			}

		});
		this.panel.add(parameterSlider2);
	}

	public void adjustImages() {
		int currentX = 0;
		currentX += adjustImageLabel(currentImageLabel, currentX);
		currentX += adjustImageLabel(secondImageLabel, currentX);
		adjustImageLabel(resultImageLabel, currentX);
		panel.repaint();
	}

	private int adjustImageLabel(ImageLabelContainer label, int x) {
		int ret = 0;
		if (label.hasImage()) {
			label.setBounds(x, 0, label.getWidth(), label.getHeight());
			ret = label.getWidth();
			label.repaint();
		}
		return ret + 100;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame mf = new MainFrame();
				mf.setVisible(true);
			}
		});
	}

	public class DraggableComponent extends JComponent {

		private static final long serialVersionUID = 8974841922334002891L;
		private volatile int screenX = 0;
		private volatile int screenY = 0;
		private volatile int myX = 0;
		private volatile int myY = 0;

		public DraggableComponent() {
			setBorder(new LineBorder(Color.BLUE, 3));
			setBounds(0, 0, 20, 20);
			setOpaque(false);

			addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
				}

				@Override
				public void mousePressed(MouseEvent e) {
					screenX = e.getXOnScreen();
					screenY = e.getYOnScreen();

					myX = getX();
					myY = getY();
				}

				@Override
				public void mouseReleased(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

			});
			addMouseMotionListener(new MouseMotionListener() {

				@Override
				public void mouseDragged(MouseEvent e) {
					int newX = e.getXOnScreen() - screenX + myX;
					int newY = e.getYOnScreen() - screenY + myY;
					MainFrame.this.setCoords(newX, newY, newX + getWidth(), newY + getHeight());
					setLocation(newX, newY);
					if (onRectangleMoveDo) {
						MainFrame.this.doOperation();						
					}
				}

				@Override
				public void mouseMoved(MouseEvent e) {
				}

			});
		}
		
		public void setDimensions(int x, int y) {
			setBounds(myX, myY, x, y);
		}

	}

	public void loadMask(List<Point> thetas) {
		this.thetas = thetas;
		
	}

	public List<Point> getMask() {
		return thetas;
	}
	
	
}

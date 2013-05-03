package sturla.atitp.frontend.menus.extraMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import sturla.atitp.frontend.EdgeDetectorDialog;
import sturla.atitp.frontend.MainFrame;
import sturla.atitp.frontend.imageops.AnisotropicDiffusionOperation;
import sturla.atitp.frontend.imageops.IsotropicDiffusionOperation;
import sturla.atitp.frontend.imageops.extra.ContrastOperation;
import sturla.atitp.frontend.imageops.extra.DynamicRangeCompressionOperation;
import sturla.atitp.frontend.imageops.extra.EqualizeImageOperation;
import sturla.atitp.frontend.imageops.extra.HistogramOperation;

public class ExtraMenu extends JMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MainFrame mainFrame;
	
	
	public ExtraMenu(MainFrame parent) {
		super("Extra");
		
		this.mainFrame = parent;
		
		JMenuItem histogram = new JMenuItem("Histogram");
		histogram.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				mainFrame.currOperation = new HistogramOperation();
				mainFrame.hideSliders();
				mainFrame.displayTextFields(false);
			}
		});
		JMenuItem equalize = new JMenuItem("Equalize");
		equalize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				mainFrame.currOperation = new EqualizeImageOperation();
				mainFrame.hideSliders();
				mainFrame.displayTextFields(false);
			}
		});
		JMenuItem dynamicCompr = new JMenuItem("Dynamic Compression");
		dynamicCompr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				mainFrame.currOperation = new DynamicRangeCompressionOperation();
				mainFrame.hideSliders();
				mainFrame.displayTextFields(false);
			}
		});
		
		JMenuItem contrast = new JMenuItem("Increase contrast");
		contrast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				mainFrame.currOperation = new ContrastOperation();
				mainFrame.hideSliders();
				mainFrame.displayTextFields(true);
				mainFrame.rectangle.setVisible(false);
				mainFrame.maskSize.setVisible(false);
			}
		});
		
		JMenuItem anisotropicDiffusion = new JMenuItem("Anisotropic Diffusion");
		anisotropicDiffusion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				mainFrame.currOperation = new AnisotropicDiffusionOperation();
				mainFrame.hideSliders();
				mainFrame.displayTextFields(false);
				mainFrame.rectangle.setVisible(false);
				mainFrame.maskSize.setVisible(true);
				mainFrame.value1.setVisible(true);
				mainFrame.value2.setVisible(true);
				EdgeDetectorDialog edgeDetectorDialog = new EdgeDetectorDialog(mainFrame.leclercRadioButton, mainFrame.lorentzRadioButton);
				edgeDetectorDialog.setVisible(true);
			}
		});
		
		JMenuItem isotropicDiffusion = new JMenuItem("Isotropic Diffusion");
		isotropicDiffusion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				mainFrame.currOperation = new IsotropicDiffusionOperation();
				mainFrame.hideSliders();
				mainFrame.displayTextFields(false);
				mainFrame.rectangle.setVisible(false);
				mainFrame.value1.setVisible(true);
			}
		});
				
		this.add(histogram);
		this.add(equalize);
		this.add(dynamicCompr);
		this.add(contrast);
		this.add(isotropicDiffusion);
		this.add(anisotropicDiffusion);
		
	}
	
}
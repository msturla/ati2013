package sturla.atitp.frontend.imageops.threshold;

import sturla.atitp.frontend.ImageLabelContainer;
import sturla.atitp.frontend.imageops.ImageOperation;
import sturla.atitp.frontend.imageops.ImageOperationParameters;
import sturla.atitp.imageprocessing.Image;

public class ThresholdBinaryOperation extends ImageOperation {
	
	@Override
	public void performOperation(ImageLabelContainer op1,
			ImageLabelContainer op2, ImageLabelContainer result,
			ImageOperationParameters params) {
		Image img = op1.getImage().copy();
		result.setImage(img.thresholdBinaryImage(params.value), img.getWidth(), img.getHeight());
	}
}

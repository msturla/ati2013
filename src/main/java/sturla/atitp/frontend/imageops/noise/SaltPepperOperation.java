package sturla.atitp.frontend.imageops.noise;

import sturla.atitp.frontend.ImageLabelContainer;
import sturla.atitp.frontend.imageops.ImageOperation;
import sturla.atitp.frontend.imageops.ImageOperationParameters;
import sturla.atitp.imageprocessing.Image;

public class SaltPepperOperation extends ImageOperation {

	@Override
	public void performOperation(ImageLabelContainer op1, ImageLabelContainer op2,
			ImageLabelContainer result, ImageOperationParameters params) {
		Image img = op1.getImage().copy();
		img.saltAndPepperNoise(params.value, params.value2);
		result.setImage(img);
		
	}

}

/*******************************************************************************
 * Copyright (c) 2017 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.obeonetwork.capella.m2doc.aql.queries;

import org.eclipse.acceleo.annotations.api.documentation.Documentation;
import org.eclipse.acceleo.annotations.api.documentation.Example;
import org.eclipse.acceleo.annotations.api.documentation.Param;
import org.obeonetwork.m2doc.element.MImage;

/**
 * Additional services for {@link MImage}.
 * 
 * @author <a href="mailto:yann.mortier@obeo.fr">Yann Mortier</a>
 */
public class ExtendedImageService {

	// @formatter:off
    @Documentation(
        value = "Sets max bounds of the image. The ratio is conserved and if the size of the image is "
        		+ "smaller than the maximal bounds then it is unchanged.",
        params = {
            @Param(name = "image", value = "The Image to modify"),
            @Param(name = "width", value = "The maximal width"),
            @Param(name = "height", value = "The maximal height"),
        },
        result = "Sets max bounds of the image.",
        examples = {
            @Example(expression = "myImage.setMaxBoundsConserveRatioNoZoomIn(300, 200)", result = "set the max bounds to (w=300, h=200)."),
        }
    )
    // @formatter:on
	public MImage setMaxBoundsConserveRatioNoZoomIn(MImage image, Integer width, Integer height) {
		image.setConserveRatio(true);
		if (image.getWidth() <= width.intValue() && image.getHeight() <= height.intValue()) {
			// no zoom in so the image is unchanged.
		} else {
			image.setWidth(width.intValue());
			if (image.getHeight() > height.intValue()) {
				image.setHeight(height.intValue());
			}
		}

		return image;
	}

}

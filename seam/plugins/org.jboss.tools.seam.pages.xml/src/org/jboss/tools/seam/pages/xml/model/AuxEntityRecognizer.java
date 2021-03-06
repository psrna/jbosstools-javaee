/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.seam.pages.xml.model;

import org.jboss.tools.common.model.loaders.*;

public class AuxEntityRecognizer implements EntityRecognizer {
    
    public AuxEntityRecognizer() {}
    
    public String getEntityName(EntityRecognizerContext context) {
    	return getEntityName(context.getExtension(), context.getBody());
    }

    String getEntityName(String ext, String body) {
        if (body == null) return null;
        return SeamPagesFileLoader.AUXILIARY_FILE_EXTENSION.equals(ext) ? "FileAnyAuxiliary" : null;
    }
    
}

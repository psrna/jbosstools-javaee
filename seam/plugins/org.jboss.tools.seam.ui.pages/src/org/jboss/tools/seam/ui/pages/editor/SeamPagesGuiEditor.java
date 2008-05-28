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
package org.jboss.tools.seam.ui.pages.editor;

import org.jboss.tools.common.editor.AbstractSectionEditor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.ui.editor.IModelObjectEditorInput;
import org.jboss.tools.jst.web.model.WebProcess;
import org.jboss.tools.seam.pages.xml.model.SeamPagesConstants;
import org.jboss.tools.seam.ui.pages.SeamUiPagesPlugin;
import org.jboss.tools.seam.ui.pages.editor.ecore.pages.Page;
import org.jboss.tools.seam.ui.pages.editor.ecore.pages.PagesFactory;
import org.jboss.tools.seam.ui.pages.editor.ecore.pages.PagesModel;

public class SeamPagesGuiEditor extends AbstractSectionEditor {
    private PagesEditor gui = null;
	private IModelObjectEditorInput input;
	private boolean isInitialized = false;
	private XModelObject installedProcess = null;
//	private JSFModel model;

	public void dispose() {
//		if(model == null) return; 
//		model.dispose();
//		model = null;
		gui.dispose();
		disposeGui();
		gui = null;
		input = null;
		installedProcess = null;
		super.dispose();
	}
	
    public PagesEditor getGUI(){
    	return gui;
    }

	protected boolean isWrongEntity(String entity) {
		return !entity.startsWith(SeamPagesConstants.ENT_FILE_SEAM_PAGES);
	}

	public void setInput(IEditorInput input) {
		super.setInput(input);
		this.input = (IModelObjectEditorInput)input;
	}
	
	public ISelectionProvider getSelectionProvider() {
		return (gui == null) ? null : gui.getModelSelectionProvider();
	}

	protected XModelObject getInstalledObject() {
		return installedProcess;
	}
	
	private WebProcess findInstalledObject() {
		XModelObject o1 = input.getXModelObject();
		if(o1 == null) return null;
		XModelObject c = o1.getChildByPath("process");
		if(!(c instanceof WebProcess)) return null;
		WebProcess f = (WebProcess)c;
		return (!f.isPrepared()) ? null : f;
	}
	
	protected void updateGui() {
		WebProcess f = findInstalledObject();
		if(f != installedProcess) disposeGui();
		else if(isInitialized) return;
		isInitialized = true;
		installedProcess = f;
		guiControl.setVisible(f != null);
		if(f == null) return;
		try {
			f.autolayout();
            gui = new PagesEditor(input);
            PagesModel model = getFakeModel();
            gui.setPagesModel(model);
//            model = new JSFModel(f.getParent());
//            model.updateLinks();

			gui.init((IEditorSite)getSite(), (IEditorInput)input);
			gui.createPartControl(guiControl);
			control = guiControl.getChildren()[0];
			control.setLayoutData(new GridData(GridData.FILL_BOTH));
			guiControl.layout();
			wrapper.update();
			wrapper.layout();
			
			//TODO remove
			if(false) throw new CoreException(null);
		} catch (CoreException ex) {
			SeamUiPagesPlugin.getDefault().logError(ex);
		}
	}
	
	private PagesModel getFakeModel(){
		PagesModel model = PagesFactory.eINSTANCE.createPagesModel();
		Page page = PagesFactory.eINSTANCE.createPage();
		page.setName("page1");
		page.setLocation(new Point(10,10));
		page.setSize(new Dimension(100,100));
		model.getChildren().add(page);
		return model;
	}

	public Object getAdapter(Class adapter) {
		if(adapter == ActionRegistry.class || adapter == org.eclipse.gef.editparts.ZoomManager.class){
				if(getGUI() != null)
					return getGUI().getAdapter(adapter);
		}
		return super.getAdapter(adapter);
	}
	
	public String getTitle() {
		return "Diagram";
	}

}

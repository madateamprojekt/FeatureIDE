/* FeatureIDE - An IDE to support feature-oriented software development
 * Copyright (C) 2005-2011  FeatureIDE Team, University of Magdeburg
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/.
 *
 * See http://www.fosd.de/featureide/ for further information.
 */
package de.ovgu.featureide.fm.ui.editors.featuremodel.actions;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import de.ovgu.featureide.fm.core.FeatureModel;
import de.ovgu.featureide.fm.ui.FMUIPlugin;
import de.ovgu.featureide.fm.ui.editors.featuremodel.operations.DeleteAllOperation;

/**
 * Action to delete a single feature including its sub features
 * 
 * @author Jan Wedding
 * @author Melanie Pflaume
 */
public class DeleteAllAction extends SingleSelectionAction {

	public static String ID = "de.ovgu.featureide.deleteall";
	private FeatureModel featureModel;
	private static ImageDescriptor deleteImage = PlatformUI.getWorkbench()
			.getSharedImages()
			.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE);

	/**
	 * 
	 * @param viewer
	 * @param featureModel
	 */
	public DeleteAllAction(Object viewer, FeatureModel featureModel) {
		super("Delete including subfeatures", viewer);
		this.featureModel = featureModel;
		this.viewer = viewer;
		setImageDescriptor(deleteImage);
	}

	@Override
	public void run() {
		DeleteAllOperation op = new DeleteAllOperation(viewer, featureModel,
				feature);

		op.addContext((IUndoContext) featureModel.getUndoContext());

		try {
			PlatformUI.getWorkbench().getOperationSupport()
					.getOperationHistory().execute(op, null, null);
		} catch (ExecutionException e) {
			FMUIPlugin.getDefault().logError(e);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.ovgu.featureide.fm.ui.editors.featuremodel.actions.SingleSelectionAction
	 * #updateProperties()
	 */
	@Override
	protected void updateProperties() {
		setEnabled(!feature.isRoot() && feature.hasChildren());
		setChecked(false);
	}

}
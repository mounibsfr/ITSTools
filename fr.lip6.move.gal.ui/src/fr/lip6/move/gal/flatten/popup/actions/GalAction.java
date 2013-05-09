package fr.lip6.move.gal.flatten.popup.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import fr.lip6.move.gal.System;
import fr.lip6.move.serialization.SerializationUtil;


/**
 * A gal action operates over a set of GAL files, transforming them one by one (in order of increasing file size).
 * @author Yann
 *
 */
public abstract class GalAction implements IObjectActionDelegate {

	private Shell shell;
	private List<IFile> files = new ArrayList<IFile>();

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		// try to treat small input first.
		Collections.sort(files, new Comparator<IFile> (){

			@Override
			public int compare(IFile f1, IFile f2) {
				try {
					// unfortunately static version Long.compare is only introduced in java 1.7 
					return new Long(org.eclipse.core.filesystem.EFS.getStore(f1.getLocationURI()).fetchInfo().getLength()).compareTo(
							org.eclipse.core.filesystem.EFS.getStore(f2.getLocationURI()).fetchInfo().getLength());
				} catch (CoreException e) {
					return f1.getLocation().toString().compareTo(f2.getLocation().toString());
				}

			}

		});

		StringBuilder sb = new StringBuilder();
		for (IFile file : files) {
			if (file != null) {
				System s = EcoreUtil.copy(SerializationUtil.fileToGalSystem(file.getRawLocationURI().getPath()));

				try {

					s = workWithSystem(s);

					String path = file.getRawLocationURI().getPath().split(getTargetExtension())[0];
					String outpath =  path+ getAdditionalExtension() + getTargetExtension();

					FileOutputStream out = new FileOutputStream(new File(outpath));
					out.write(0);
					out.close();
					SerializationUtil.systemToFile(s,outpath);
					java.lang.System.err.println("GAL model written to file : " +outpath);
					sb.append("  " + outpath);
				} catch (Exception e) {
					MessageDialog.openWarning(
							shell,
							"Flatten",
							getServiceName() + " operation raised an exception " + e.getMessage());
					e.printStackTrace();
					return;

				}
			}
			java.lang.System.err.println(getServiceName() + " was executed on " + file.getName());
			//				MessageDialog.openInformation(
			//						shell,
			//						"Flatten",
			//						"Flatten GAL was executed on " + file.getName());
		}

		MessageDialog.openInformation(
				shell,
				"Gal Transformation result",
				getServiceName() + " operation successfully produced files : " + sb.toString());
		
		files.clear();
	}

	protected abstract String getServiceName() ;

	protected abstract System workWithSystem(System s) throws Exception ;

	/**
	 * Newly produced files add this extension before the .gal extension
	 * @return a string to add to file name
	 */
	protected abstract String getAdditionalExtension() ;

	/**
	 * Return the extension of target files both 
	 */
	protected String getTargetExtension() {
		return ".gal";
	}
	
	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		files.clear();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection ts = (IStructuredSelection) selection;
			for (Object s : ts.toList()) {
				if (s instanceof IResource) {
					
					try {
						((IResource) s).accept(new IResourceVisitor() {
							
							@Override
							public boolean visit(IResource resource) throws CoreException {
								if (resource instanceof IFile) {
									IFile file = (IFile) resource;
									if (file.getFileExtension()!=null && getTargetExtension().contains(file.getFileExtension()) && ! file.getFullPath().toPortableString().contains(getAdditionalExtension())) {
										files.add(file);
									}							
								}
								// descend into subfolders
								return true;
							}
						});
					} catch (CoreException e) {
						e.printStackTrace();
					}
					
				}
				
				
			}
		}
	}

}



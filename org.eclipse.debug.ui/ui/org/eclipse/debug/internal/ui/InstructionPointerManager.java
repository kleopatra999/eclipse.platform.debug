package org.eclipse.debug.internal.ui;

/**********************************************************************
Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
This file is made available under the terms of the Common Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/cpl-v10.html
**********************************************************************/
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * This class tracks instruction pointer contexts for all active debug targets and threads
 * in the current workbench.  There should only ever be one instance of this class, obtained
 * via 'getDefault()'.
 */
public class InstructionPointerManager {

	/**
	 * The singleton instance of this class.	 */
	private static InstructionPointerManager fgDefault;

	/**
	 * Mapping of IDebugTarget objects to (mappings of IThread objects to lists of instruction
	 * pointer contexts).	 */
	private Map fDebugTargetMap;
	
	/**
	 * Clients must not instantiate this class.	 */
	private InstructionPointerManager() {
		fDebugTargetMap = new HashMap();
	}
	
	/**
	 * Return the singleton instance of this class, creating it if necessary.	 */
	public static InstructionPointerManager getDefault() {
		if (fgDefault == null) {
			fgDefault = new InstructionPointerManager();
		}
		return fgDefault;
	}
	
	/**
	 * Add an instruction pointer annotation in the specified editor for the 
	 * specified stack frame.	 */
	public void addAnnotation(ITextEditor textEditor, IStackFrame stackFrame) {
		
		// Create the annotation object
		IDocumentProvider docProvider = textEditor.getDocumentProvider();
		IEditorInput editorInput = textEditor.getEditorInput();
		InstructionPointerAnnotation instPtrAnnotation = new InstructionPointerAnnotation(stackFrame);
		
		// Create the Position object that specifies a location for the annotation
		Position position = null;
		int charStart = 0;
		try {
			charStart = stackFrame.getCharStart();
		} catch (DebugException de) {
		}
		if (charStart >= 0) {
			position = new Position(charStart);
		} else {
			IDocument doc = docProvider.getDocument(editorInput);
			try {
				int lineNumber = stackFrame.getLineNumber() - 1;
				position = new Position(doc.getLineOffset(lineNumber));
			} catch (BadLocationException ble) {
				return;
			} catch (DebugException de) {
				DebugUIPlugin.log(de);
				return;
			}
		}
		
		// Add the annotation at the position to the editor's annotation model
		IAnnotationModel annModel = docProvider.getAnnotationModel(editorInput);
		annModel.removeAnnotation(instPtrAnnotation);
		annModel.addAnnotation(instPtrAnnotation, position);	
		
		// Retrieve the list of instruction pointer contexts
		IDebugTarget debugTarget = stackFrame.getDebugTarget();
		IThread thread = stackFrame.getThread();
		Map threadMap = (Map) fDebugTargetMap.get(debugTarget);
		if (threadMap == null) {
			threadMap = new HashMap();	
			fDebugTargetMap.put(debugTarget, threadMap);		
		}
		List contextList = (List) threadMap.get(thread);
		if (contextList == null) {
			contextList = new ArrayList();
			threadMap.put(thread, contextList);
		}
		
		// Create a context object & add it to the list
		InstructionPointerContext context = new InstructionPointerContext(stackFrame, textEditor, instPtrAnnotation);
		contextList.remove(context);
		contextList.add(context);
	}
	
	/**
	 * Remove all annotations associated with the specified debug target that this class
	 * is tracking.
	 */
	public void removeAnnotations(IDebugTarget debugTarget) {
		
		// Retrieve the mapping of threads to context lists
		Map threadMap = (Map) fDebugTargetMap.get(debugTarget);
		if (threadMap == null) {
			return;
		}
		
		// Remove annotations for all threads associated with the debug target
		Set threadSet = threadMap.keySet();
		Iterator threadIterator = threadSet.iterator();
		while (threadIterator.hasNext()) {
			IThread thread = (IThread) threadIterator.next();
			removeAnnotations(thread, threadMap);
		}
	}
	
	/**
	 * Remove all annotations associated with the specified thread that this class
	 * is tracking.
	 */
	public void removeAnnotations(IThread thread) {
		
		// Retrieve the thread map
		IDebugTarget debugTarget = thread.getDebugTarget();
		Map threadMap = (Map) fDebugTargetMap.get(debugTarget);
		if (threadMap == null) {
			return;
		}
		
		// Remove all annotations for the thread
		removeAnnotations(thread, threadMap);
	}
	
	/**
	 * Remove all annotations associated with the specified thread.  	 */
	private void removeAnnotations(IThread thread, Map threadMap) {
		
		// Retrieve the context list and remove each corresponding annotation
		List contextList = (List) threadMap.get(thread);
		if (contextList != null) {
			Iterator contextIterator = contextList.iterator();
			while (contextIterator.hasNext()) {
				InstructionPointerContext context = (InstructionPointerContext) contextIterator.next();
				removeAnnotation(context.getTextEditor(), context.getAnnotation());
			}
		}
		
		// Remove the thread map
		threadMap.remove(thread);						
	}
	
	/**
	 * Remove the specified annotation from the specified text editor.	 */
	private void removeAnnotation(ITextEditor textEditor, InstructionPointerAnnotation annotation) {
		IDocumentProvider docProvider = textEditor.getDocumentProvider();
		IAnnotationModel annotationModel = docProvider.getAnnotationModel(textEditor.getEditorInput());
		annotationModel.removeAnnotation(annotation);
	}
	
}

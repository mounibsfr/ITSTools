package fr.lip6.move.scoping;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.CrossReference;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.serializer.diagnostic.ISerializationDiagnostic.Acceptor;
import org.eclipse.xtext.serializer.tokens.CrossReferenceSerializer;
import org.eclipse.xtext.serializer.tokens.ICrossReferenceSerializer;

import fr.lip6.move.gal.AbstractInstance;
import fr.lip6.move.gal.AbstractParameter;
import fr.lip6.move.gal.Label;

public class GALCrossReferenceSerializer extends CrossReferenceSerializer implements ICrossReferenceSerializer {

	
@Override
public String serializeCrossRef(EObject semanticObject, CrossReference crossref, EObject target, INode node,
		Acceptor errors) {
	if (target instanceof Label) {
		Label lab = (Label) target;
		return "\"" + lab.getName() + "\"";
	}
	if (target instanceof AbstractInstance) {
		AbstractInstance ai = (AbstractInstance) target;
		return ai.getName();
	}
	if (target instanceof AbstractParameter) {
		AbstractParameter param = (AbstractParameter) target;
		return param.getName();
	}
	return super.serializeCrossRef(semanticObject, crossref, target, node, errors);
}
}
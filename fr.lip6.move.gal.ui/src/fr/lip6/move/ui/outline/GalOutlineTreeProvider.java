/*
 * generated by Xtext
 */
package fr.lip6.move.ui.outline;

import org.eclipse.xtext.ui.editor.outline.impl.DefaultOutlineTreeProvider;

import com.google.inject.Inject;

import fr.lip6.move.gal.BitComplement;
import fr.lip6.move.gal.Constante;
import fr.lip6.move.gal.False;
import fr.lip6.move.gal.Not;
import fr.lip6.move.gal.True;
import fr.lip6.move.gal.Variable;
import fr.lip6.move.gal.VariableRef;
import fr.lip6.move.gal.impl.AdditionImpl;
import fr.lip6.move.gal.impl.AndImpl;
import fr.lip6.move.gal.impl.ArrayVarAccessImpl;
import fr.lip6.move.gal.impl.AssignmentImpl;
import fr.lip6.move.gal.impl.BitAndImpl;
import fr.lip6.move.gal.impl.BitComplementImpl;
import fr.lip6.move.gal.impl.BitOrImpl;
import fr.lip6.move.gal.impl.BitShiftImpl;
import fr.lip6.move.gal.impl.BitXorImpl;
import fr.lip6.move.gal.impl.BooleanExpressionImpl;
import fr.lip6.move.gal.impl.ComparisonImpl;
import fr.lip6.move.gal.impl.ConstanteImpl;
import fr.lip6.move.gal.impl.FalseImpl;
import fr.lip6.move.gal.impl.MultiplicationImpl;
import fr.lip6.move.gal.impl.NotImpl;
import fr.lip6.move.gal.impl.OrImpl;
import fr.lip6.move.gal.impl.PeekImpl;
import fr.lip6.move.gal.impl.PopImpl;
import fr.lip6.move.gal.impl.PushImpl;
import fr.lip6.move.gal.impl.TransientImpl;
import fr.lip6.move.gal.impl.TrueImpl;
import fr.lip6.move.gal.impl.VariableRefImpl;
import fr.lip6.move.gal.impl.WrapBoolExprImpl;


/** 
 * Customization of the default outline structure.
 * 
 * Added description for other AST elements in Gal structure.
 * The famous "unnamed" string is now removed, for a better display.
 * 
 * Added "_isLead(astElement)" method which indicates if an AST element is a leaf 
 * or not. If an AST element is described as a leaf, Eclipse will not show 
 * an arrow near by the element, in the Outline view.
 * @author steph
 *
 */
public class GalOutlineTreeProvider extends DefaultOutlineTreeProvider {

	@Inject
	
	
	/*----------------------------------*
	 * == Arithmetics AST elements== 
	 *----------------------------------*/
	public Object _text(AdditionImpl e) {
		return e.getOp().getName();
	}
	
	public Object _text(MultiplicationImpl e)
	{
		return e.getOp().getName();
	}
	
	public Object _text(BitComplementImpl e)
	{
		if(e.getVal() instanceof ConstanteImpl)
		{
			String valeur = ((ConstanteImpl)e.getVal()).getVal() + "" ;
			if(((ConstanteImpl) e.getVal()).isIsNegative())
				return "-"+valeur ; 
			else
				return valeur ; 
		}

		return "Integer Expression" ; 
	}
	
	public Object _text(WrapBoolExprImpl e)
	{
		return "Integer Expression : 0 or 1" ; 
	}
	
	/*
	 * Bit à bit
	 */
	public Object _text(BitOrImpl e)
	{
		return "BIT OR" ; 
	}
	public Object _text(BitAndImpl e)
	{
		return "BIT AND" ; 
	}
	public Object _text(BitShiftImpl e)
	{
		return "SHIFT : " + e.getOp().getName() ; 
	}
	public Object _text(BitXorImpl e)
	{
		return "XOR : ^ " ; 
	}
	public Object _text(BooleanExpressionImpl e)
	{
		return "Boolean Expression" ; 
	}
	

	
	/* -----------------------------------------------*
	 * ====== Actions on list : PUSH, POP, PEEK  ======
	 *------------------------------------------------*/
	public Object _text(PushImpl p)
	{
		return "Push on list '" + p.getListe().getName() + "'";
	}
	
	public Object _text(PopImpl p)
	{
		return "Pop on list '" + p.getListe().getName() + "'";
	}
	
	public Object _text(PeekImpl e)
	{
		return "Peek on list '"  + e.getListe().getName() + "'"; 
	}
	
	
	/*
	 * Array
	 */
	public Object _text(ArrayVarAccessImpl e)
	{
		return e.getPrefix().getName() + " - Array" ; 
	}
	
	/*
	 * Assignment
	 */
	public Object _text(AssignmentImpl e)
	{
		return "Assignment";
	}
	
	/* -----------------------------*
	 * Boolean operations
	 * -----------------------------*/
	public Object _text(AndImpl e)
	{
		return "AND" ; 
	}
	
	public Object _text(OrImpl e)
	{
		return "OR" ; 
	}
	
	public Object _text(NotImpl e)
	{
		if(e.getVal()  instanceof TrueImpl)
			return "TRUE" ; 
		if(e.getVal() instanceof FalseImpl)
			return "FALSE" ; 
		if(e.getVal() instanceof ComparisonImpl)
			return "Comparison : " + ((ComparisonImpl) e.getVal()).getOperator().getName();
		
		
		return "Boolean Expression" ; 
	}
	
	
	public Object _text(VariableRefImpl e)
	{
		return "Variable : " + e.getVar().getName() ; 
	}

	
	public Object _text(Constante e)
	{
		return "Constante :" + e.getVal() ;  
	}
	
	public Object _text(TransientImpl e)
	{
		return "TRANSIENT" ;
	}
	
	
	/*-------------------------Which objects are leafs ?--------------------------*/
	
	
	/*----------------------------------*
	 * == AST leafs == 
	 *----------------------------------*/
	
	public boolean _isLeaf(Not e)
	{
		// Pas besoin de rajouter une flèche 
		// Si on est sur un TRUE ou FALSe
		if(e.getVal() instanceof True ||
		   e.getVal() instanceof False)
			return true;
		
		return false;
	}
	
	public boolean _isLeaf(Variable e)
	{
		return true ;
	}
	
	public boolean _isLeaf(VariableRef e)
	{
		return true ;
	}
	
	public boolean _isLeaf(VariableRefImpl e)
	{
		return true;
	}
	
	public boolean _isLeaf(BitComplement e)
	{
		if(e.getVal() instanceof ConstanteImpl)
			return true ; 
		if(e.getVal() instanceof Variable)
			return true ;
		
		return false ;
	}
	
	
	
	

}
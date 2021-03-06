package fr.lip6.move.gal.gal2pins;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import fr.lip6.move.gal.And;
import fr.lip6.move.gal.BooleanExpression;
import fr.lip6.move.gal.CTLProp;
import fr.lip6.move.gal.Comparison;
import fr.lip6.move.gal.GF2;
import fr.lip6.move.gal.LTLFuture;
import fr.lip6.move.gal.LTLGlobally;
import fr.lip6.move.gal.LTLProp;
import fr.lip6.move.gal.NeverProp;
import fr.lip6.move.gal.Not;
import fr.lip6.move.gal.Or;
import fr.lip6.move.gal.Property;
import fr.lip6.move.gal.ReachableProp;
import fr.lip6.move.gal.SafetyProp;
import fr.lip6.move.gal.Specification;
import fr.lip6.move.gal.gal2smt.Gal2SMTFrontEnd;
import fr.lip6.move.gal.gal2smt.bmc.NecessaryEnablingsolver;
import fr.lip6.move.gal.semantics.INext;
import fr.lip6.move.gal.semantics.INextBuilder;
import fr.lip6.move.gal.semantics.NextSupportAnalyzer;
import fr.lip6.move.gal.semantics.Sequence;
import fr.lip6.move.serialization.BasicGalSerializer;
import fr.lip6.move.gal.semantics.DependencyMatrix;
import fr.lip6.move.gal.semantics.ExpressionPrinter;
import fr.lip6.move.gal.semantics.IDeterministicNextBuilder;

public class Gal2PinsTransformerNext {

	private List<List<INext>> transitions;
	private IDeterministicNextBuilder dnb;
	private Gal2SMTFrontEnd gsf;
	private NecessaryEnablingsolver nes;
	private boolean hasPartialOrder;

	public void setSmtConfig(Gal2SMTFrontEnd gsf) {
		this.gsf = gsf;
	}

	private void buildBodyFile(String path) throws IOException {
		PrintWriter pw = new PrintWriter(path);

		pw.println("#include <ltsmin/pins.h>");
		pw.println("#include <ltsmin/pins-util.h>");
		pw.println("#include <ltsmin/ltsmin-standard.h>");
		pw.println("#include <ltsmin/lts-type.h>");
		pw.println("#include \"model.h\"");
		
		pw.println("int state_length() {");
		pw.println("  return " + dnb.size() + " ;");
		pw.println("}");

		pw.println("#define true 1");
		pw.println("#define false 0");
		
		pw.println("int initial ["+dnb.size() + "] ;");

		pw.println("int* initial_state() {");
		for (int i=0; i < dnb.size() ; i++) {
			pw.println("  // " + dnb.getVariableNames().get(i) );
			pw.println("  initial ["+ (i) + "] = " + dnb.getInitial().get(i) + ";" );			
		}
		pw.println("  return initial;");
		pw.println("}");

		printDependencyMatrix(pw);

		printNextState(pw);
	
		printGB(pw);

		pw.close();
	}


	private void buildHeader(String path) throws IOException {
		PrintWriter pw = new PrintWriter(path);
		pw.print(
				"#include <ltsmin/pins.h>\n"+
						"/**\n"+
						" * @brief calls callback for every successor state of src in transition group \"group\".\n"+
						" */\n"+
						"int next_state(void* model, int group, int *src, TransitionCB callback, void *arg);\n"+
						"\n"+
						"/**\n"+
						" * @brief returns the initial state.\n"+
						" */\n"+
						"int* initial_state();\n"+
						"\n"+
						"/**\n"+
						" * @brief returns the read dependency matrix.\n"+
						" */\n"+
						"int* read_matrix(int row);\n"+
						"\n"+
						"/**\n"+
						" * @brief returns the write dependency matrix.\n"+
						" */\n"+
						"int* write_matrix(int row);\n"+
						"\n"+
						"/**\n"+
						" * @brief returns the state label dependency matrix.\n"+
						" */\n"+
						"int* label_matrix(int row);\n"+
						"\n"+
						"/**\n"+
						" * @brief returns whether the state src satisfies state label \"label\".\n"+
						" */\n"+
						"int state_label(void* model, int label, int* src);\n"+
						"\n"+
						"/**\n"+
						" * @brief returns the number of transition groups.\n"+
						" */\n"+
						"int group_count();\n"+
						"\n"+
						"/**\n"+
						" * @brief returns the length of the state.\n"+
						" */\n"+
						"int state_length();\n"+
						"\n"+
						"/**\n"+
						" * @brief returns the number of state labels.\n"+
						" */\n"+
						"int label_count();\n"
				);
		pw.flush();
		pw.close();
	}


	public int[] convertToLine(BitSet bs) {
		int [] line = new int[dnb.size()];
		for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i+1)) {
			// operate on index i here
			line[i] = 1;
			if (i == Integer.MAX_VALUE) {
				break; // or (i+1) would overflow
			}
		}
		return line;
	}

	private void getConjuncts(BooleanExpression guard, List<BooleanExpression> conjuncts) {
		if (guard instanceof And) {
			And and = (And) guard;
			getConjuncts(and.getLeft(), conjuncts);
			getConjuncts(and.getRight(), conjuncts);				
		} else {
			conjuncts.add(guard);
		}
	}


	private DependencyMatrix printDependencyMatrix(PrintWriter pw) {		

		List<int []> rm = new ArrayList<>();
		List<int []> wm = new ArrayList<>();
		DependencyMatrix dm = new DependencyMatrix(transitions,this.dnb.size());
		for (int tindex = 0 ; tindex < transitions.size() ; tindex++) {
			int[] r = convertToLine(dm.getRead(tindex));
			rm.add(r);

			int[] w = convertToLine(dm.getWrite(tindex));
			wm.add(w);
		}

		printMatrix(pw, "rm", rm);
		pw.print("int* read_matrix(int row) {\n"
				+"  return rm[row];\n"
				+"}\n");

		printMatrix(pw, "wm", wm);

		pw.print("int* write_matrix(int row) {\n"
				+"  return wm[row];\n"
				+"}\n");

		printLabels(pw, rm, wm, dm);
		return dm;
	}

	private void printGB(PrintWriter pw) {
		// set the name of this PINS plugin
		pw.println("char pins_plugin_name[] = \"GAL\";");

		pw.println("void pins_model_init(model_t m) {");

		pw.println("  // create the LTS type LTSmin will generate");
		pw.println("  lts_type_t ltstype=lts_type_create();");

		pw.println("  // set the length of the state");
		pw.println("  lts_type_set_state_length(ltstype, state_length());");

		pw.println("  // add an int type for a state slot");
		pw.println("  int int_type = lts_type_add_type(ltstype, \"int\", NULL);");
		pw.println("  lts_type_set_format (ltstype, int_type, LTStypeDirect);");


		pw.println("  // set state name & type");
		int i=0;
		for (String vname : dnb.getVariableNames()) {
			pw.println("  lts_type_set_state_name(ltstype,"+i+",\""+vname+"\");");
			pw.println("  lts_type_set_state_typeno(ltstype,"+i+",int_type);");			
			i++;
		}


		// edge label types : TODO
		pw.println("  // add an action type for edge labels");
		pw.println("  int action_type = lts_type_add_type(ltstype, \"action\", NULL);");
		pw.println("  lts_type_set_format (ltstype, action_type, LTStypeEnum);");

		pw.println("  lts_type_set_edge_label_count (ltstype, 1);");
		pw.println("  lts_type_set_edge_label_name(ltstype, 0, \"action\");");
		pw.println("  lts_type_set_edge_label_type(ltstype, 0, \"action\");");
		pw.println("  lts_type_set_edge_label_typeno(ltstype, 0, action_type);");

		// state label types : TODO

		pw.println("  // add a bool type for state labels");
		pw.println("  int bool_type = lts_type_put_type (ltstype, \"boolean\", LTStypeBool, NULL);");

		pw.println("  lts_type_set_state_label_count (ltstype, label_count());");
		pw.println("  for (int i =0; i < label_count() ; i++) {");
		pw.println("    lts_type_set_state_label_typeno (ltstype, i, bool_type);");
		pw.println("    lts_type_set_state_label_name (ltstype, i, labnames[i]);");
		pw.println("  }");
	
		// done with ltstype
		pw.println("  lts_type_validate(ltstype);");

		// make sure to set the lts-type before anything else in the GB
		pw.println("  GBsetLTStype(m, ltstype);");

		// setting all values for all non direct types
		for (int tindex = 0 ; tindex < transitions.size(); tindex++) {
			pw.println("  pins_chunk_put(m, action_type, chunk_str(\"tr"+ tindex + "\"));");
		}

		//		pw.println("  GBchunkPut(m, bool_type, chunk_str(LTSMIN_VALUE_BOOL_FALSE));");
		//		pw.println("  GBchunkPut(m, bool_type, chunk_str(LTSMIN_VALUE_BOOL_TRUE));");

		// set state variable values for initial state
		pw.println("  GBsetInitialState(m, initial_state());");

		// set function pointer for the next-state function
		pw.println("  GBsetNextStateLong(m, (next_method_grey_t) next_state);");

		// set function pointer for the label evaluation function
		pw.println("  GBsetStateLabelLong(m, (get_label_method_t) state_label);");


		// create combined matrix
		pw.println("  matrix_t *cm = malloc(sizeof(matrix_t));");
		pw.println("  dm_create(cm, group_count(), state_length());");

		// set the read dependency matrix
		pw.println("  matrix_t *rm = malloc(sizeof(matrix_t));");
		pw.println("  dm_create(rm, group_count(), state_length());");
		pw.println("  for (int i = 0; i < group_count(); i++) {");
		pw.println("    for (int j = 0; j < state_length(); j++) {");
		pw.println("      if (read_matrix(i)[j]) {");
		pw.println("        dm_set(cm, i, j);");
		pw.println("        dm_set(rm, i, j);");
		pw.println("      }");
		pw.println("    }");
		pw.println("  }");
		pw.println("  GBsetDMInfoRead(m, rm);");

		// set the write dependency matrix
		pw.println("  matrix_t *wm = malloc(sizeof(matrix_t));");
		pw.println("  dm_create(wm, group_count(), state_length());");
		pw.println("  for (int i = 0; i < group_count(); i++) {");
		pw.println("    for (int j = 0; j < state_length(); j++) {");
		pw.println("      if (write_matrix(i)[j]) {");
		pw.println("        dm_set(cm, i, j);");
		pw.println("        dm_set(wm, i, j);");
		pw.println("      }");
		pw.println("    }");
		pw.println("  }");
		pw.println("  GBsetDMInfoMustWrite(m, wm);");

		// set the combined matrix
		pw.println("  GBsetDMInfo(m, cm);");

//	    // set the label dependency matrix
		pw.println("  matrix_t *lm = malloc(sizeof(matrix_t));");
		pw.println("  dm_create(lm, label_count(), state_length());");
		pw.println("  for (int i = 0; i < label_count(); i++) {\n"
				 + "    for (int j = 0; j < state_length(); j++) {\n"
				 + "      if (label_matrix(i)[j]) dm_set(lm, i, j);\n"
			  	 + "    }\n"
				 + "  }\n"
				 + "  GBsetStateLabelInfo(m, lm);");

		
		// set guards
		pw.println("  GBsetGuardsInfo(m,(guard_t**) &guardsPerTrans);");

		pw.println("  int sl_size = label_count();");
		pw.println("  int nguards = "+transitions.size()+ ";");
		// set the label group implementation
		pw.println("  sl_group_t* sl_group_all = malloc(sizeof(sl_group_t) + sl_size * sizeof(int));");
		pw.println("  sl_group_all->count = sl_size;");
		pw.println("  for(int i=0; i < sl_group_all->count; i++) sl_group_all->sl_idx[i] = i;");
		pw.println("  GBsetStateLabelGroupInfo(m, GB_SL_ALL, sl_group_all);");
		pw.println("  if (nguards > 0) {");
		pw.println("    sl_group_t* sl_group_guards = malloc(sizeof(sl_group_t) + nguards * sizeof(int));");
		pw.println("    sl_group_guards->count = nguards;");
		pw.println("    for(int i=0; i < sl_group_guards->count; i++) sl_group_guards->sl_idx[i] = i;");
		pw.println("    GBsetStateLabelGroupInfo(m, GB_SL_GUARDS, sl_group_guards);");
		pw.println("  }");
	    // get state labels
		pw.println("  GBsetStateLabelsGroup(m, sl_group);");

		if (hasPartialOrder) {
		// NES
		pw.println("  int ngroups = group_count();");
		pw.println("  matrix_t *gnes_info = malloc(sizeof(matrix_t));");
		pw.println("  dm_create(gnes_info, sl_size, ngroups);");
		pw.println("  for(int i = 0; i < sl_size; i++) {");
		pw.println("    const int *guardnes = gal_get_label_nes_matrix(i);");
		pw.println("    for(int j = 0; j < ngroups; j++) {");
		pw.println("      if (guardnes[j]) dm_set(gnes_info, i, j);");
		pw.println("    }");
		pw.println("  }");
		pw.println("  GBsetGuardNESInfo(m, gnes_info);");

		// NDS : set guard necessary disabling set info
		pw.println("  matrix_t *gnds_info = malloc(sizeof(matrix_t));");
		pw.println("  dm_create(gnds_info, sl_size, ngroups);");
		pw.println("  for(int i = 0; i < sl_size; i++) {");
		pw.println("    const int *guardnds = gal_get_label_nds_matrix(i);");
		pw.println("    for(int j = 0; j < ngroups; j++) {");
		pw.println("      if (guardnds[j]) dm_set(gnds_info, i, j);");
		pw.println("    }");
		pw.println("  }");
		pw.println("  GBsetGuardNDSInfo(m, gnds_info);");
		
		
		// Co-enabling
		pw.println("  matrix_t *coEnab = malloc(sizeof(matrix_t));");
		pw.println("  dm_create(coEnab, group_count(), group_count());");
		pw.println("  for (int i = 0; i < group_count(); i++) {\n"
				 + "    for (int j = 0; j < group_count(); j++) {\n"
				 + "      if (coEnab_matrix(i)[j]) dm_set(coEnab, i, j);\n"
			  	 + "    }\n"
				 + "  }\n"
				 + "  GBsetGuardCoEnabledInfo(m, coEnab);");
		
		// DNA
		pw.println("  matrix_t *dna = malloc(sizeof(matrix_t));");
		pw.println("  dm_create(dna, group_count(), group_count());");
		pw.println("  for (int i = 0; i < group_count(); i++) {\n"
				 + "    for (int j = 0; j < group_count(); j++) {\n"
				 + "      if (dna_matrix(i)[j]) dm_set(dna, i, j);\n"
			  	 + "    }\n"
				 + "  }\n"
				 + "  GBsetDoNotAccordInfo(m, dna);");

		}
//		pw.println("  for (int i = 0; i < group_count(); i++) {\n"
//				 + "      GBsetGuard(m,i,guards+i);\n"
//				 + "  }");
		
		pw.println("}");
	}

	private void printLabels(PrintWriter pw, List<int[]> rm, List<int[]> wm, DependencyMatrix dm) {

		List<int []> guards = new ArrayList<>();

		for (int i = 0 ; i < transitions.size() ; i++) {
			int [] gl = new int[2];
			gl[0] = 1;
			gl[1] = i;
			guards.add(gl);
		}
		
		pw.println("int * guardsPerTrans ["+ guards.size()+ "] =  {");
		for (Iterator<int[]> it = guards.iterator(); it.hasNext();) {
			int[] line =  it.next();
			
			pw.print("  ((int[])");
			printArray(pw, line);
			pw.print(")");
			if (it.hasNext()) {
				pw.print(",");
			}
			pw.println();
		}
		pw.println("};");

		pw.println("int group_count() {");
		pw.println("  return " + transitions.size() + " ;");
		pw.println("}");
		
		pw.println("int label_count() {");
		pw.println("  return " + (transitions.size()+atoms.size()) + " ;");
		pw.println("}");
		
		pw.println("char * labnames ["+ (transitions.size()+atoms.size()) +"] = {");
		boolean first = true;
		for (int i = 0 ; i < transitions.size() ; i++) {
			if (!first) {
				pw.print(",");
			} else { first = false; }
			pw.print("  \"" + "enabled" + i + "\"");			
			pw.println();
		}
		for (int i = 0 ; i < atoms.size() ; i++) {
			if (!first) {
				pw.print(",");
			} else { first = false; }
			pw.print("  \"" + atoms.get(i).name + "\"");
			pw.println();
		}
		pw.println("};");
		
		List<int[]> lm = new ArrayList<>(atoms.size());
		for (AtomicProp ap : atoms) {
			BitSet lr = new BitSet();
			NextSupportAnalyzer.computeQualifiedSupport(ap.be, lr, dnb);
			lm.add(convertToLine(lr));
		}
		if (! atoms.isEmpty()) printMatrix(pw, "lm", lm);
		pw.print("int* label_matrix(int row) {\n"
				+"  if (row < " + transitions.size() + ") return rm[row];\n");
		if (! atoms.isEmpty()) { pw.print("  else return lm[row-"+transitions.size()+"];\n");}
		pw.print("}\n");
		
		
		if (!hasPartialOrder) {
			return;
		}
		try {
			nes.init(dnb);
			// invert the logic for ltsmin
			List<int[]> mayEnable = nes.computeAblingMatrix(false, dm);
			List<int[]> mayDisable = nes.computeAblingMatrix(true, dm);

			List<int[]> ones = new ArrayList<>();
			int[] oneA = new int[transitions.size()];
			for (int i=0 ; i < oneA.length ; i++) {
				oneA[i] = 1;
			}
			ones.add(oneA);
			printMatrix(pw, "allOnes", ones);
			
			
			// logic is inverted
			printMatrix(pw, "mayDisable", mayEnable);
			printMatrix(pw, "mayEnable", mayDisable);

			pw.println("const int* gal_get_label_nes_matrix(int g) {");
			pw.println(" if (g <" +transitions.size()+") return mayEnable[g];");
			pw.println(" return allOnes[0];");
			pw.println("}");

			pw.println("const int* gal_get_label_nds_matrix(int g) {");
			pw.println(" if (g <" +transitions.size()+") return mayDisable[g];");
			pw.println(" return allOnes[0];");
			pw.println("}");
			
			List<int[]> coEnabled = nes.computeCoEnablingMatrix(dm);
			printMatrix(pw, "coenabled", coEnabled);
			
			pw.println("const int* coEnab_matrix(int g) {");
			pw.println(" return coenabled[g];");
			pw.println("}");
			
			List<int[]> doNotAccord = nes.computeDoNotAccord(coEnabled, mayEnable,dm);
			printMatrix(pw, "dna", doNotAccord);

			pw.println("const int* dna_matrix(int g) {");
			pw.println(" return dna[g];");
			pw.println("}");
			
		} catch (Exception e) {
			System.err.println("Skipping mayMatrices nes/nds "+e.getMessage());
			e.printStackTrace();
			
			hasPartialOrder = false;
//			pw.println("const int* gal_get_label_nes_matrix(int g) {");
//			pw.println(" return lm[g];");
//			pw.println("}");
//
//			pw.println("const int* gal_get_label_nds_matrix(int g) {");
//			pw.println(" return lm[g];");
//			pw.println("}");
		}
		
	}

	public void printMatrix(PrintWriter pw, String matrixName, List<int[]> matrix) {
		pw.println("int "+matrixName+ "["+matrix.size()+"]["+matrix.get(0).length+"] = {");
		for (Iterator<int[]> it = matrix.iterator() ; it.hasNext() ; ) {
			int[] line = it.next();
			pw.print("  ");
			printArray(pw, line);
			if (it.hasNext()) {
				pw.print(",");
			}
			pw.print("\n");
		}
		pw.println("};");
	}

	public void printArray(PrintWriter pw, int[] line) {
		pw.print("{");
		for (int i=0; i < line.length ; i++) {
			pw.print(line[i]);
			if (i < line.length -1)
				pw.print(",");
		}
		pw.print("}");
	}



	private void printNextState(PrintWriter pw) {
		
		pw.append("typedef struct state {\n");
		pw.append("  struct state * next;\n");
		pw.append("  int state ["+dnb.size()+"];\n");
		pw.append("} state_t ;\n");
		
		ToCPrinter toC = new ToCPrinter(pw);
		int [] indexes = new int[transitions.size()];
		int i=0;
		
		for (List<INext> l : transitions) {
			INext seq = Sequence.seq(l);
			indexes[i++]=seq.accept(toC);
		}
		
		pw.println("int next_state(void* model, int group, int *src, TransitionCB callback, void *arg) {");

		pw.println("  state_t * cur = malloc(sizeof(state_t));\n");
		pw.println("  memcpy(& cur->state, src,  sizeof(int)* "+dnb.size()+");\n");
		pw.println("  cur->next = NULL;\n");
		
		pw.println("  // provide transition labels and group number of transition.");
		pw.println("  int action[1];");
		// use the same identifier for group and action
		pw.println("  action[0] = group;");
		pw.println("  transition_info_t transition_info = { action, group };");
		
		pw.println("  switch (group) {");
		for (int tindex = 0 ; tindex < transitions.size() ; tindex++) {
			pw.println("  case "+tindex+" : " );
			pw.println("     cur = nextStatement"+indexes[tindex]+"(cur);");
			pw.println("     break;");
		}
		pw.println("  default : return 0 ;" );
		pw.println("  } // end switch(group) ");
		pw.println("  int nbsucc = 0;");
		pw.println("  for (state_t * it=cur ; it ;  nbsucc++) {");
		pw.println("     callback(arg, &transition_info, it->state, wm[group]);");
		pw.println("     state_t * tmp = it->next; free(it); it=tmp;");			
		pw.println("  }");			
		pw.println("  return nbsucc; // return number of successors");
		pw.println("}");
		
		
		/////// Handle Labels similarly		
		pw.println("int state_label(void* model, int label, int* src) {");
		// labels
		pw.println("  if (label >= "+transitions.size()+") {" );
		pw.println("    switch (label) {");
		for (int tindex=transitions.size(); tindex < transitions.size()+ atoms.size() ; tindex++) {
			pw.println("      case "+tindex+" : " );
			pw.println("        return "+ExpressionPrinter.printQualifiedExpression(atoms.get(tindex-transitions.size()).be, "src", dnb)  +";");
		}
		pw.println("    }" );
		pw.println("  }" );
		
		// guards : reuse firing function
		pw.println("  state_t * cur = malloc(sizeof(state_t));\n");
		pw.println("  memcpy(& cur->state, src,  sizeof(int)* "+dnb.size()+");\n");
		pw.println("  cur->next = NULL;\n");
		
		pw.println("  switch (label) {");
		for (int tindex = 0 ; tindex < transitions.size() ; tindex++) {
			pw.println("  case "+tindex+" : " );
			pw.println("     cur = nextStatement"+indexes[tindex]+"(cur);");
			pw.println("     break;");
		}		
		pw.println("  default : return 0 ;" );
		pw.println("  } // end switch(group) ");

		pw.println("  int res = (cur != NULL);");
		pw.println("  for (state_t * it=cur ; it ;  ) {");
		pw.println("     state_t * tmp = it->next; free(it); it=tmp;");			
		pw.println("  }");			
		pw.println("  return res; // return label");
		pw.println("}");
		
		
		pw.println("int state_label_many(void* model, int * src, int * label, int guards_only) {");
		pw.println("  (void)model;");
		for (int tindex=0; tindex < transitions.size() ; tindex++) {
			pw.println("  label["+ tindex + "] = ");
			pw.print("    state_label(model,"+tindex+",src)");
			pw.println(" ;");
		}
		pw.println("  if (guards_only) return 0; ");
		for (int tindex=transitions.size(); tindex < transitions.size()+ atoms.size() ; tindex++) {
			pw.println("  label["+ tindex + "] = ");
			pw.print("    state_label(model,"+tindex+",src)");
			pw.println(" ;");
		}
		pw.println("  return 0; // return number of successors");
		pw.println("}");
		
		
		pw.println("void sl_group (model_t model, sl_group_enum_t group, int*src, int *label)");
		pw.println("  {");
		pw.println("  state_label_many (model, src, label, group == GB_SL_GUARDS);");
		pw.println("  (void) group; // Both groups overlap, and start at index 0!");
		pw.println("  }");

		pw.println("void sl_all (model_t model, int*src, int *label)");
		pw.println("  {");
		pw.println("  state_label_many (model, src, label, 0);");
		pw.println("  }");

	}

	public String printLTLProperty(LTLProp prop) {
		BasicGalSerializer bgs = new BasicGalSerializer() {
			@Override
			public Boolean doSwitch(EObject eObject) {
				if (eObject instanceof BooleanExpression) {
					BooleanExpression be = (BooleanExpression) eObject;
					AtomicProp atom = atomMap.get(be);
					if (atom != null) {
						pw.print("("+atom.name +"==true)");
						return true;
					}
				}
				return super.doSwitch(eObject);
			}

			@Override
			public Boolean caseLTLFuture(LTLFuture object) {
				pw.print("<>(");
				doSwitch(object.getProp());
				pw.print(")");		
				return true;					
			}
			
			@Override
			public Boolean caseLTLGlobally(LTLGlobally object) {
				pw.print("[](");
				doSwitch(object.getProp());
				pw.print(")");		
				return true;
			}
			
		};
		bgs.setStrict(true);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bgs.setStream(baos, 2);
		bgs.doSwitch(prop.getPredicate());
		bgs.close();
		return baos.toString();
	}
	
	private List<AtomicProp> atoms = new ArrayList<>();
	private Map<BooleanExpression, AtomicProp> atomMap = new HashMap<BooleanExpression, AtomicProp>();
	public void transform (Specification spec, String cwd, boolean withPorMatrix) {

//		if ( spec.getMain() instanceof GALTypeDeclaration ) {
//			Logger.getLogger("fr.lip6.move.gal").fine("detecting pure GAL");
//		} else {
//			Logger.getLogger("fr.lip6.move.gal").fine("Error transformation does not support hierarchy yet.");
//			return;
//		}
		long time = System.currentTimeMillis();
		
		dnb = IDeterministicNextBuilder.build(INextBuilder.build(spec));

		// determinize
		transitions = dnb.getDeterministicNext();
		
		atoms.clear();
		atomMap.clear();
		Map<String,AtomicProp> uniqueMap = new HashMap<>();
		// look for atomic propositions
		if (! spec.getProperties().isEmpty()) {
			for (Property prop : spec.getProperties()) {
				if (prop.getBody() instanceof SafetyProp) {
					SafetyProp rp = (SafetyProp) prop.getBody();
					BooleanExpression be = rp.getPredicate();
					if (rp instanceof ReachableProp || rp instanceof NeverProp) {
						be = GF2.not(EcoreUtil.copy(be));
					}					
					atoms.add(new AtomicProp(prop.getName().replaceAll("-", ""), be));
				} else if (prop.getBody() instanceof LTLProp || prop.getBody() instanceof CTLProp) {
					
					for (TreeIterator<EObject> it = prop.getBody().eAllContents() ; it.hasNext() ;  ) {
						EObject obj = it.next();
						if (isPureBool(obj)) {
							// helps to recognize that  !AP is the negation of AP
							// Can reduce number of AP as well as help simplifications
							if (obj instanceof Not) {
								obj = ((Not) obj).getValue();
							}
							String stringProp = ExpressionPrinter.printQualifiedExpression((BooleanExpression) obj, "s", dnb);
							AtomicProp atom = uniqueMap.get(stringProp);
							if (atom == null) {
								atom = new AtomicProp("LTLAP"+atoms.size(), (BooleanExpression) obj);
								atoms.add(atom);
								uniqueMap.put(stringProp, atom);
							}
							atomMap.put((BooleanExpression) obj, atom);
							it.prune();
						}
					}
				}
			}

		}

		hasPartialOrder = withPorMatrix;
		try {
			
			buildBodyFile(cwd + "/model.c");
			
			buildHeader(cwd + "/model.h");

			Logger.getLogger("fr.lip6.move.gal").info("Built C files in "+ (System.currentTimeMillis()- time ) + "ms conformant to PINS in folder :"+cwd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private boolean isPureBool(EObject obj) {
		if (obj instanceof And || obj instanceof Or || obj instanceof Not ) {
			for (EObject child:  obj.eContents()) {
				if (! isPureBool(child)) {
					return false;
				}
			}
			return true;
		} else if (obj instanceof Comparison) {
			return true;
		}				
		return false;
	}

	public void initSolver() {
		nes = gsf.buildNecessaryEnablingSolver();
		nes.startSolver();
	}
}

class AtomicProp {
	String name;
	BooleanExpression be;
	public AtomicProp(String name, BooleanExpression be) {
		this.name = name;
		this.be = be;
	}
}
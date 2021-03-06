/*-
 * APT - Analysis of Petri Nets and labeled Transition systems
 * Copyright (C) 2012-2013  Members of the project group APT
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
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package uniol.apt.analysis.invariants;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.util.SparseArray;
import fr.lip6.move.gal.gal2smt.bmc.FlowMatrix;

//import uniol.apt.adt.pn.Node;
//import uniol.apt.adt.pn.PetriNet;
//import uniol.apt.util.interrupt.InterrupterRegistry;
//import uniol.apt.util.MathTools;

/**
 * A calculator for invariants and testing if a net is covered by invariants.
 * Provides two differient algorithms for calculating the invariants. The first
 * algorithm is descripted in http://de.scribd.com/doc/49919842/Pn-ESTII (slide
 * 88) and the other is also based an the farkas algorithm and is descripted in
 * http://pipe2.sourceforge.net/documents/PIPE-Report.pdf (page 19) which is
 * based on the paper of D'Anna and Trigila "Concurrent system analysis using
 * Petri nets – an optimised algorithm for finding net invariants", Mario D'Anna
 * and Sebastiano Trigila, Computer Communications vol 11, no. 4 august 1988.
 * @author Dennis-Michael Borde, Manuel Gieseking
 */
public class InvariantCalculator {

	/**
	 * Hidden constructor
	 */
	private InvariantCalculator() {
	}

	/**
	 * Enumeration for choosing which algorithm should be used.
	 */
	public enum InvariantAlgorithm {

		FARKAS,
		PIPE;
	}

	/**
	 * A class for holding the sets P+ = {j | c_hj &gt; 0} and P- = {j | c_hj less
	 * 0} for a given row.
	 */
	private static class PpPm {

		// The row
		public final int row;
		// P+ set
		public final BitSet pPlus;
		// P- set
		public final BitSet pMinus;

		/**
		 * Constructor creates the sets P- and P+ for a given row.
		 * @param h - the row from which the sets should be created.
		 */
		public PpPm(MatrixCol mat, int row) {
			this.row = row;
			this.pMinus = new BitSet();
			this.pPlus = new BitSet();

			for (int col = 0; col < mat.getColumnCount(); ++col) {
				if (mat.get(row,col) < 0) {
					pMinus.set(col);
				} else if (mat.get(row,col) > 0) {
					pPlus.set(col);
				}
			}
		}

		public boolean xorPosNeg() {
			return  (!pMinus.isEmpty()) ^ (! pPlus.isEmpty());
		}

		public void setValue(int j, int val) {
			if (val == 0) {
				pMinus.clear(j);
				pPlus.clear(j);
			} else if (val < 0) {
				pMinus.set(j);
				pPlus.clear(j);
			} else {
				pMinus.clear(j);
				pPlus.set(j);
			}
		}
	}

	/**
	 * Calculates for a given matrix the P+ = {j | c_hj &gt; 0} and P- = {j | c_hj
	 * less 0} sets for each row.
	 * @param matC - the matrix from which the sets should be calculated.
	 * @return The result of the calculation
	 */
	private static List<PpPm> calcPpPm(MatrixCol matC) {
		final List<PpPm> result = new ArrayList<>();
		for (int row = 0; row < matC.getRowCount() ; row++) {
			result.add(new PpPm(matC,row));
		}
		return result;
	}


	/**
	 * Holds the result of the check11b-check. That means it holds the row, the
	 * columnindex where a component is less then respectivly greater than zero
	 * and the P+ respectivly P- set if there exists a row in the given matrix
	 * such that |P+| == 1 or |P-| == 1.
	 */
	private static class Check11bResult {

		// The first columnindex where c_hj < 0 respectivly c_hj > 0
		public final int col;
		// The whole row
		public final int row;
		// The set P+ respectivly P-
		public final BitSet p;

		/**
		 * Constructor to save the data.
		 * @param k - the first columnindex where a component ist less
		 * respectivly greater than zero.
		 * @param h - the whole row.
		 * @param p - the set P+ respectivly P-.
		 */
		public Check11bResult(int k, int row, BitSet p) {
			this.col = k;
			this.row = row;
			this.p = p;
		}
	}

	/**
	 * Checks if there exists a row in the given matrix such that |P+| == 1 or
	 * |P-| == 1 and returns the row or null if such a row do not exists.
	 * @param pppms the list of all rows with P+ and P- sets.
	 * @return the row which satisfy |P+| == 1 or |P-| == 1 or null if not
	 * existent.
	 */
	private static Check11bResult check11b(List<PpPm> pppms) {
		for (PpPm pppm : pppms) {
			if (isSinglebit(pppm.pMinus)) {
				return new Check11bResult(pppm.pMinus.nextSetBit(0), pppm.row, pppm.pPlus);
			} else if (isSinglebit(pppm.pPlus)) {
				return new Check11bResult(pppm.pPlus.nextSetBit(0), pppm.row, pppm.pMinus);
			}
		}
		return null;
	}

	private static boolean isSinglebit(BitSet bs) {
		int i = bs.nextSetBit(0);
		if (i==-1) {
			return false;
		}
		return bs.nextSetBit(i) == -1;
	}
	
	/**
	 * Calculates the invariants with the algorithm based on
	 * http://pipe2.sourceforge.net/documents/PIPE-Report.pdf (page 19).
	 * @param mat - the matrix to calculate the invariants from.
	 * @return a generator set of the invariants.
	 */
	private static Set<List<Integer>> calcInvariantsPIPE(int[][] mat, boolean onlyPositive) {
		if (mat.length == 0 || mat[0].length == 0) {
			return new HashSet<>();
		}
		final MatrixCol matB = phase1PIPE(mat);
		
		
		// We want to work with columns in this part of the algorithm
		// We add and remove columns all day => we want to switch to a column based representation
		// order of rows is really irrelevant + columns which are identical up to scaling factor are useless
		// let's use a set of columns.
		Set<List<Integer>> colsB = new HashSet<>(2*matB.getColumnCount());
		for (int i=0; i < matB.getColumnCount() ; i++) {
			SparseArray<Integer> col = matB.getColumn(i);
			if (col.size() != 0) {
				colsB.add(normalize(col,matB.getRowCount()));
			}
		}
		
		
		if (! onlyPositive) {
			return colsB;
		}
		
		
		// phase 2
		System.out.println("// Phase 2 : computing semi flows from basis of "+ colsB.size() +"invariants ");
		Set<List<Integer>> treated = new HashSet<>();
		int iter=0;
		while (colsB.size() < 2000) {
			/// InterrupterRegistry.throwIfInterruptRequestedForCurrentThread();

			// columns which are positive on target row
			List<List<Integer>> ppmPlus = null;
			// columns which are neg on target row
			List<List<Integer>> ppmMinus = null;
			
			BitSet negRows = new BitSet(matB.getRowCount());
			
			int minRow = -1 ;
			int minRowWeight = -1;
			int rowsize = colsB.iterator().next().size();
			for (int row = 0; row < rowsize; ++row) {
				int weight = 0;
				List<List<Integer>> tmpMinus = new ArrayList<>();
				List<List<Integer>> tmpPlus = new ArrayList<>();
				
				for (List<Integer> col : colsB) {
					int val = col.get(row);
					if (val < 0) {
						tmpMinus.add(col);
						weight++;
						negRows.set(row);
					} else if (val > 0) {
						tmpPlus.add(col);
						weight++;
					}
				}
				if (! tmpMinus.isEmpty()) {
					if (minRow == -1 || minRowWeight > weight) {
						minRow = row;
						minRowWeight = weight;
						ppmMinus = tmpMinus;
						ppmPlus = tmpPlus;
					}
				}
			}
			
			int targetRow = minRow;
			if (targetRow == -1) {
				// no more negative rows to treat
				break;
			}
			// cleanup
			int useless = 0;
			for (List<Integer> col : colsB) {
				boolean isok = true;
				for (int negRow = negRows.nextSetBit(0) ; negRow >=0 ; negRow = negRows.nextSetBit(negRow+1)) {
					if (col.get(negRow) != 0) {
						isok = false;
						break;
					}
				}
				if (isok) {
					treated.add(col);
					useless++;
				}
			}
			System.out.println("Removed "+useless+ " treated columns.");
			colsB.removeAll(treated);
						
			for (List<Integer> col : colsB) {
				int val = col.get(targetRow); 
				if (val > 0) {
					ppmPlus.add(col);
				} else if (val <0) {
					ppmMinus.add(col);
				}
			}
			
			if (! ppmPlus.isEmpty()) {
				for (List<Integer> colj : ppmPlus) {
					for (List<Integer> colk : ppmMinus) {
						// operate a linear combination on the columns of indices j and k
						// in order to geta  new column having the pair.getFirst element equal
						// to zero
						List<Integer> column = new ArrayList<>(colj.size());
						int a = -colk.get(targetRow);
						int b = colj.get(targetRow);
						for (int i = 0; i < matB.getRowCount(); i++) {
							column.add(a * colj.get(i) + b * colk.get(i));
						}
						// add normalization step : we don't need scalar scaling of each other
						normalize(column);
						// append column to matrix B
						// tests existence 
						colsB.add(column);
					}
				}
				// Delete from B all the columns of index k \in P-
				colsB.removeAll(ppmMinus);
			}
			System.out.println("Phase 2 iter "+ (iter++) + " rows : " + colsB.iterator().next().size() + " cols " + colsB.size());
		}
		colsB.addAll(treated);
		System.out.println("Found "+ colsB.size() + " different invariants.");
		return colsB;
	}

	private static List<Integer> normalize(SparseArray<Integer> col, int size) {
		List<Integer> list = new ArrayList<>(size);
		boolean allneg = true;
		for (int i=0 ; i < col.size() ; i++) {
			if (col.valueAt(i) > 0) {
				allneg = false;
				break;
			}
		}
		if (allneg) {
			for (int i=0 ; i < col.size() ; i++) {
				col.setValueAt(i , - col.valueAt(i));
			}
		}
		
		for (int i=0; i < size ; i++) {
			list.add(col.get(i, 0));
		}		
		normalize(list);
		return list;
	}

	private static MatrixCol phase1PIPE(int[][] mat) {
		// incidence matrix
		final MatrixCol matC = new MatrixCol(mat);
		final MatrixCol matB = MatrixCol.identity(matC.getColumnCount(), matC.getColumnCount());

		System.out.println("// Phase 1: matrix "+matC.getRowCount()+" rows "+matC.getColumnCount()+" cols");
		final List<PpPm> pppms = calcPpPm(matC);
		while (! matC.isZero()) {
		//	InterrupterRegistry.throwIfInterruptRequestedForCurrentThread();
			
			if (test1a(matC, matB, pppms)) {
				continue;
			} else {
				test1b(matC, matB, pppms);
			}
		}
		return matB;
	}

	private static void test1b(final MatrixCol matC, final MatrixCol matB, final List<PpPm> pppms) {
		// [1.1.b] if there exists a row h in C such that |P+| == 1 or |P-| == 1
		final Check11bResult chkResult = check11b(pppms);
		if (chkResult != null) {
			test1b1(matC, matB, pppms, chkResult);
		} else {
			test1b2(matC, matB, pppms);
		}
	}

	private static void test1b2(final MatrixCol matC, final MatrixCol matB, final List<PpPm> pppms) {
		// [1.1.b.1] let tRow be the index of a non-zero row of C.
		// let tCol be the index of a column such that c[trow][tcol] != 0.
		Pair<Integer, Integer> pair = matC.getNoneZeroRow();
		int tRow = pair.getFirst();
		int tCol = pair.getSecond();
		
		int cHk = matC.get(tRow,tCol);
		int beta = Math.abs(cHk);
		
		// for all cols j with j != tCol and c[tRow][j] != 0
		for (int j = 0; j < matC.getColumnCount(); ++j) {
			int cHj = matC.get(tRow,j);
			if (j != tCol && cHj != 0) {
				//substitute to the column of index j the linear combination
				// of the columns of indices tCol and j with coefficients
				// alpha and beta defined as follows:
				int alpha = ((Math.signum(cHj) * Math.signum(cHk)) < 0)
					? Math.abs(cHj) : -Math.abs(cHj);
				if (alpha == 0 && beta == 1) {
					continue;
				}
				for (int row = 0 ; row <  matC.getRowCount() ; row++) {
					int old = matC.get(row,j);
					int val = old * beta + matC.get(row,tCol) * alpha;
					if (old != val) {
						matC.set(row, j, val);
						pppms.get(row).setValue(j,val);
					}
				}
				for (int row = 0 ; row < matB.getRowCount() ; row++) {
					matB.set(row, j, matB.get(row,j) * beta + matB.get(row,tCol) * alpha);						
				}
			}
		}
		// delete from the extended matrix the column of index k
		matC.deleteColumn(tCol);
		matB.deleteColumn(tCol);
		deleteColumn(pppms,tCol);
	}

	private static void test1b1(final MatrixCol matC, final MatrixCol matB, final List<PpPm> pppms,
			final Check11bResult chkResult) {
		// [1.1.b.1] let k be the unique index of column belonging to P+ (resp. to P-)
		for (int j = chkResult.p.nextSetBit(0); j >= 0; j = chkResult.p.nextSetBit(j+1)) {
			// substitute to the column of index j the linear combination of
			//the columns indexed by k and j with the coefficients
			//|chj| and |chk| respectively.
			final Integer chk = Math.abs(matC.get(chkResult.row, chkResult.col));
			final Integer chj = Math.abs(matC.get(chkResult.row,j));
			for (int row = 0 ; row <  matC.getRowCount() ; row++) {
				int val = matC.get(row,j) * chk + matC.get(row, chkResult.col) * chj;
				if (matC.get(row,j) != val) {
					matC.set(row, j, val );
					pppms.get(row).setValue(j, val);
				}
			}
			for (int row = 0 ; row < matB.getRowCount() ; row++) {
				matB.set(row, j, matB.get(row,j) * chk + matB.get(row,chkResult.col) * chj);
			}
		}
		// delete from the extended matrix the column of index k
		matC.deleteColumn(chkResult.col);
		matB.deleteColumn(chkResult.col);
		deleteColumn(pppms,chkResult.col);
	}

	private static boolean test1a(final MatrixCol matC, final MatrixCol matB, final List<PpPm> pppms) {
		// [1.1] if there exists a row h in C such that the sets P+ = {j | c_hj > 0},
		// P- = {j | c_hj < 0} satisfy P+ == {} or P- == {} and not (P+ == {} and P- == {})
		// that means there exists a row such that all components are positive respectively negative			
		boolean acts = false;
		for (int row = 0 ; row < matC.getRowCount() ; row++) {
			PpPm pppm = pppms.get(row);
			if (pppm.xorPosNeg()) {
				acts = true;
				// [1.1.a] delete from the extended matrix all the columns of index j \in P+ \cup P-
				for (int j = matC.getColumnCount() - 1; j >= 0; --j) {					
					if (matC.get(row, j) != 0) {
						matC.deleteColumn(j);
						matB.deleteColumn(j);
						deleteColumn(pppms,j);						
					}
				}
			}
		}
		return acts;
	}

	private static boolean isZero(List<PpPm> pppms) {
		for (PpPm pp : pppms) {
			if (! (pp.pMinus.isEmpty() && pp.pPlus.isEmpty()) ) {
				return false;
			}
		}
		return true;
	}

	private static void deleteColumn(List<PpPm> pppms, int k) {
		for (PpPm pp:pppms) {
			clearCol(k, pp.pMinus);
			clearCol(k, pp.pPlus);
		}
	}

	private static void clearCol(int k, BitSet bs) {
		for (int i = bs.nextSetBit(0) ; i >=0 ; i = bs.nextSetBit(i+1)) {
			if (i == k) {
				bs.clear(i);
			} else if (i > k) {
				bs.set(i-1);
				bs.clear(i);
			}
		}
	}

	private static void normalize(List<Integer> invariants) {
		int gcd = MathTools.gcd(invariants);
		if (gcd > 1) {
			for (int j = 0; j < invariants.size(); ++j) {
				invariants.set(j, invariants.get(j) / gcd);
			}
		}
	}

	/**
	 * Calculates the invariants with the algorithm based on
	 * http://de.scribd.com/doc/49919842/Pn-ESTII (slide 88)
	 * @param mat matrix to calculate the invariants from.
	 * @return a generator set of the invariants.
	 */
	private static Set<List<Integer>> calcInvariantsFarkas(int[][] mat) {
		int rows = mat.length;
		if (mat.length == 0 || mat[0].length == 0) {
			return new HashSet<>();
		}
		int cols = mat[0].length;
		int dcols = cols + rows;

		System.out.println("Initialize :");
		// initializes d as (C | E) with incidence matrix C and identity E
		// time: O(rows*(cols+rows))
		// place: O(rows*(cols+rows))
		List<List<Integer>> d = new ArrayList<>();
		for (int i = 0; i < rows; ++i) {
			List<Integer> row = new ArrayList<>();
			d.add(i, row);
			for (int j = 0; j < dcols; ++j) {
				if (j < cols) {
					row.add(j, mat[i][j]);
				} else {
					row.add(j, (i != j - cols) ? 0 : 1);
				}
			}
		}
		System.out.println("Phase 2 : rows :" + d.size()+ " cols : " + d.get(0).size());
		// for all columns (transitions)
		// time: O()
		// place: O()
		for (int i = 0; i < cols; ++i) {
			int offset = 1;
			// time: O()
			// place: O()
			do {
				rows = d.size();
				// for all pairs of rows in d
				// time: O()
				// place: O()
				for (int j1 = 0; j1 < rows - 1; ++j1) {
					final List<Integer> z1 = d.get(j1);
					for (int j2 = j1 + offset; j2 < rows; ++j2) {
					//	InterrupterRegistry.throwIfInterruptRequestedForCurrentThread();
						final List<Integer> z2 = d.get(j2);
						// check opposite signum at position i
						if (Math.signum(z1.get(i)) * Math.signum(z2.get(i)) < 0) {
							// z(i) = 0
							final int z1abs = Math.abs(z1.get(i));
							final int z2abs = Math.abs(z2.get(i));

							// time: O(cols+rows)
							// place: O(cols+rows)
							final List<Integer> z = new ArrayList<>();
							for (int k = 0; k < dcols; ++k) {
								int a = z2abs * z1.get(k);
								int b = z1abs * z2.get(k);
								z.add(k, a + b);
								//z.add(k, z2abs * z1.get(k) + z1abs * z2.get(k));

							}
							// normalize z
							final int gcd = MathTools.gcd(z);
							if (gcd != 1) {
								// time: O(cols+rows)
								// place: O(1)
								for (int k = 0; k < dcols; ++k) {
									z.set(k, z.get(k) / gcd);
								}
							}
							d.add(z);
						}
					}
				}
				offset = rows;
				// check new added rows.
				System.out.println("Phase 2 : rows :" + d.size()+ " cols : " + d.get(0).size());
			} while (rows < d.size());
			// remove all rows z with z(i) != 0
			for (int j = d.size() - 1; j >= 0; --j) {
				if (d.get(j).get(i) != 0) {
					d.remove(j);
				}
			}
			System.out.println("Phase 2 end of iteration : rows :" + d.size()+ " cols : " + d.get(0).size());
		}

		// the result is at the right side of d (remove left transitions-count columns).
		// time: O()
		// place: O()
		Set<List<Integer>> result = new HashSet<>();
		for (List<Integer> z : d) {
			result.add(z.subList(cols, dcols));
		}
		return result;
	}

	/**
	 * Transposes the given matrix.
	 * @param mat - the matrix to transpose.
	 * @return the transposed matrix.
	 */
	private static int[][] transposeMatrix(int[][] mat) {
		if (mat.length == 0) {
			return mat;
		}
		final int rows = mat.length;
		final int cols = mat[0].length;
		final int[][] matT = new int[cols][rows];
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				matT[j][i] = mat[i][j];
			}
		}
		return matT;
	}

	/**
	 * Calculates the s-invariants of the the given petri net with the pipe
	 * algorithm.
	 * @param pn - the petri net to calculate the s-invariants from.
	 * @return a generator set of the invariants.
	 */
	public static Set<List<Integer>> calcSInvariants(FlowMatrix pn) {
		return calcSInvariants(pn, InvariantAlgorithm.PIPE, true);
	}

	public static Set<List<Integer>> calcSInvariants(FlowMatrix pn, boolean onlyPositive) {
		return calcSInvariants(pn, InvariantAlgorithm.PIPE, onlyPositive);
	}
	
	/**
	 * Calculates the s-invariants of the the given petri net with the given
	 * algorithm.
	 * @param pn - the petri net to calculate the s-invariants from.
	 * @param algo - the algorithm with which the invariants should be
	 * calculated.
	 * @return a generator set of the invariants.
	 */
	public static Set<List<Integer>> calcSInvariants(FlowMatrix pn, InvariantAlgorithm algo, boolean onlyPositive) {
		switch (algo) {
			case FARKAS:
				return InvariantCalculator.calcInvariantsFarkas(pn.getIncidenceMatrix2());
			case PIPE:
				return InvariantCalculator.calcInvariantsPIPE(transposeMatrix(pn.getIncidenceMatrix2()), onlyPositive);
			default:
				return InvariantCalculator.calcInvariantsFarkas(pn.getIncidenceMatrix2());
		}
	}

	/**
	 * Calculates the t-invariants of the the given petri net with the pipe
	 * algorithm.
	 * @param pn - the petri net to calculate the t-invariants from.
	 * @return a generator set of the invariants.
	 */
	public static Set<List<Integer>> calcTInvariants(FlowMatrix pn) {
		return InvariantCalculator.calcTInvariants(pn, InvariantAlgorithm.PIPE);
	}

	/**
	 * Calculates the t-invariants of the the given petri net with the given
	 * algorithm.
	 * @param pn - the petri net to calculate the t-invariants from.
	 * @param algo - the algorithm with which the invariants should be
	 * calculated.
	 * @return a generator set of the invariants.
	 */
	public static Set<List<Integer>> calcTInvariants(FlowMatrix pn, InvariantAlgorithm algo) {
		switch (algo) {
			case FARKAS:
				return InvariantCalculator.calcInvariantsFarkas(
						transposeMatrix(pn.getIncidenceMatrix2()));
			case PIPE:
				return InvariantCalculator.calcInvariantsPIPE(pn.getIncidenceMatrix2(),true);
			default:
				return InvariantCalculator.calcInvariantsFarkas(
						transposeMatrix(pn.getIncidenceMatrix2()));
		}
	}

//	/**
//	 * Checks whether the the given petri net is covered by s-invariants with
//	 * the pipe algorithm. Returns the invariant which covers or null if not
//	 * existent.
//	 * @param pn - the petri net to check covered.
//	 * @return the invariant which covers or null if it is not covered.
//	 */
//	public static Vector coveredBySInvariants(FlowMatrix pn) {
//		return coveredBySInvariants(pn, InvariantAlgorithm.PIPE);
//	}
//
//	/**
//	 * Checks whether the the given petri net is covered by s-invariants with
//	 * the given algorithm. Returns the invariant which covers or null if not
//	 * existent.
//	 * @param pn - the petri net to check covered.
//	 * @param algo - the algorithm with which it should be checked.
//	 * @return the invariant which covers or null if it is not covered.
//	 */
//	public static Vector coveredBySInvariants(FlowMatrix pn, InvariantAlgorithm algo) {
//		Set<List<Integer>> invariants = InvariantCalculator.calcSInvariants(pn, algo);
//		return coveredBySInvariants(pn, invariants);
//	}

//	/**
//	 * Checks whether the the given petri net is covered by the given s-invariants.
//	 * Returns the invariant which covers or null if not existent.
//	 * @param pn - the petri net to check covered.
//	 * @param invariants - the invariants which should be tested if they cover.
//	 * @return the invariant which covers or null if it is not covered.
//	 */
//	public static Vector coveredBySInvariants(FlowMatrix pn, Set<List<Integer>> invariants) {
//		if (invariants.isEmpty()) {
//			return null;
//		}
//		Vector invariant = new Vector();
//		for (int i = 0; i < pn.getPlaces().size(); ++i) {
//			int v = 0;
//			for (List<Integer> y : invariants) {
//				v += y.get(i);
//			}
//			if (v == 0) {
//				return null;
//			}
//			invariant.add(v);
//		}
//		return invariant;
//	}
//
//	/**
//	 * Checks whether the the given petri net is covered by t-invariants with
//	 * the pipe algorithm. Returns the invariant which covers or null if not
//	 * existent.
//	 * @param pn - the petri net to check covered.
//	 * @return the invariant which covers or null if it is not covered.
//	 */
//	public static Vector coveredByTInvariants(PetriNet pn) {
//		return coveredByTInvariants(pn, InvariantAlgorithm.PIPE);
//	}
//
//	/**
//	 * Checks whether the the given petri net is covered by t-invariants with
//	 * the given algorithm. Returns the invariant which covers or null if not
//	 * existent.
//	 * @param pn - the petri net to check covered.
//	 * @param algo - the algorithm with which it should be checked.
//	 * @return the invariant which covers or null if it is not covered.
//	 */
//	public static Vector coveredByTInvariants(PetriNet pn, InvariantAlgorithm algo) {
//		Set<List<Integer>> invariants = InvariantCalculator.calcTInvariants(pn, algo);
//		return coveredByTInvariants(pn, invariants);
//	}
//
//	/**
//	 * Checks whether the the given petri net is covered by the given t-invariants.
//	 * Returns the invariant which covers or null if not existent.
//	 * @param pn - the petri net to check covered.
//	 * @param invariants - the invariants which should be tested if they cover.
//	 * @return the invariant which covers or null if it is not covered.
//	 */
//	public static Vector coveredByTInvariants(PetriNet pn, Set<List<Integer>> invariants) {
//		if (invariants.isEmpty()) {
//			return null;
//		}
//		Vector invariant = new Vector();
//		for (int i = 0; i < pn.getTransitions().size(); ++i) {
//			int v = 0;
//			for (List<Integer> y : invariants) {
//				v += y.get(i);
//			}
//			if (v == 0) {
//				return null;
//			}
//			invariant.add(v);
//		}
//		return invariant;
//	}
//
//	/**
//	 * Generates for every given invariant a mapping to the given nodes.
//	 *
//	 * @param <T> - Place or Transition (extends Node) depending on t- or
//	 * s-invariants are given.
//	 * @param nodes - the nodes for the mapping.
//	 * @param invariants - the invariants which should be mapped.
//	 * @return a set of invariants mapped to the given nodes.
//	 */
//	public static <T extends Node> Set<Map<T, Integer>> getMapping(Set<T> nodes, Set<List<Integer>> invariants) {
//		Set<Map<T, Integer>> ret = new HashSet<>();
//		for (List<Integer> invariant : invariants) {
//			Map<T, Integer> map = new HashMap<>();
//			int i = 0;
//			for (Iterator<T> it = nodes.iterator(); it.hasNext();) {
//				T node = it.next();
//				map.put(node, invariant.get(i));
//				++i;
//			}
//			ret.add(map);
//		}
//		return ret;
//	}
}
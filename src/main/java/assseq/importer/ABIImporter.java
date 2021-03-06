package assseq.importer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DebugGraphics;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.io.ABITrace;
import org.biojava.nbio.core.sequence.template.AbstractSequence;

import assseq.sequences.ABISequence;
import assseq.sequences.Bases;
import assseq.sequences.BasicTraceSequence;
import assseq.sequences.DefaultQualCalledBases;
import assseq.sequences.FastFastaSequence;
import assseq.sequences.Sequence;
import assseq.sequences.Trace;
import assseq.sequences.Traces;
import assseq.utils.ArrayUtilities;


public class ABIImporter {
	private static final Logger logger = Logger.getLogger(ABIImporter.class);
	private int longestSequenceLength;
	private File inputFile;

	public ABIImporter(File inputFile) {
		this.inputFile = inputFile;
	}

	public List<Sequence> importSequences() throws AlignmentImportException, FileNotFoundException {

		long startTime = System.currentTimeMillis();
		ArrayList<Sequence> sequences = new ArrayList<Sequence>();

		try {

			ABITrace abiTrace = new ABITrace(inputFile);
			
			int[] baseCalls = abiTrace.getBasecalls();

			Trace traceA = new Trace(abiTrace.getTrace("A"));
			Trace traceC = new Trace(abiTrace.getTrace("C"));
			Trace traceG = new Trace(abiTrace.getTrace("G"));
			Trace traceT = new Trace(abiTrace.getTrace("T"));
			
			//ArrayUtilities.debug(baseCalls);
			
			Traces traces = new Traces(traceA, traceG, traceC, traceT, baseCalls);

			int[] qualCalls = abiTrace.getQcalls();
			
			short[] shortQualCalls = ArrayUtilities.intArray2ShortArray(qualCalls);
			
			//ArrayUtilities.debug(shortQualCalls);

			byte[] bases = getSequenceFromABI(abiTrace);
			DefaultQualCalledBases basesAndCalls = new DefaultQualCalledBases(bases, shortQualCalls);

			ABISequence sequence  = new ABISequence(inputFile.getName(), basesAndCalls, traces);

			sequences.add(sequence);

			longestSequenceLength = sequence.getLength();

		} catch (Exception e) {
			logger.error(e);
			// TODO Auto-generated catch block
			throw new AlignmentImportException("could not import as fasta file because: " + e.getMessage());
		}

		long endTime = System.currentTimeMillis();
		System.out.println("reading sequences took " + (endTime - startTime) + " milliseconds");

		return sequences;
	}

	private byte[] getSequenceFromABI(ABITrace abiTrace) throws CompoundNotFoundException{

		AbstractSequence<NucleotideCompound> sequence = abiTrace.getSequence();
		java.util.Iterator<NucleotideCompound> iter = sequence.iterator();
		StringBuilder seq = new StringBuilder();
		while(iter.hasNext()){
			NucleotideCompound compound = iter.next();
			seq.append(compound.getBase());
		}
		String sequenceString = seq.toString();

		byte[] seqAsBytes = sequenceString.getBytes();

		return seqAsBytes;
	}

	public int getLongestSequenceLength() {
		return longestSequenceLength;
	}

    public static boolean isStringValidFirstLine(String firstLine) {
		if(StringUtils.startsWith(firstLine, "ABI")){
			return true;
		}else{
			return false;
		}
	}

}
package assseq.sequences;

import assseq.sequencelist.MemoryMappedSequencesFile;

public class NexusFileSequence extends PositionsToPointerFileSequence {

	public NexusFileSequence(MemoryMappedSequencesFile sequencesFile, long startPointer) {
		super(sequencesFile, startPointer);
	}	
}

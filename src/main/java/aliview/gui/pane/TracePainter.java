package aliview.gui.pane;

import org.apache.log4j.Logger;

import aliview.AminoAcid;
import aliview.NucleotideUtilities;
import aliview.alignment.Alignment;
import aliview.alignment.NucleotideHistogram;
import aliview.sequences.AminoAcidAndPosition;
import aliview.sequences.Sequence;

public abstract class TracePainter implements Runnable{
	private static final Logger logger = Logger.getLogger(TracePainter.class);

	private Sequence seq;
	private int seqYPos;
	private int clipPosY;
	private int xMinSeqPos;
	private int xMaxSeqPos;
	private double seqPerPix;
	private double charWidth;
	private double charHeight;
	private double highDPIScaleFactor;
	private RGBArray clipRGB;
	private TracePanel tracePanel;
	private Alignment alignment;


	public TracePainter(Sequence seq, int seqYPos, int clipPosY, int xMinSeqPos,
			int xMaxSeqPos, double seqPerPix, double charWidth, double charHeight,
			double highDPIScaleFactor, RGBArray clipRGB, TracePanel tracePane, Alignment alignment) {
		super();
		this.seq = seq;
		this.seqYPos = seqYPos;
		this.clipPosY = clipPosY;
		this.xMinSeqPos = xMinSeqPos;
		this.xMaxSeqPos = xMaxSeqPos;
		this.seqPerPix = seqPerPix;
		this.charWidth = charWidth;
		this.charHeight = charHeight;
		this.highDPIScaleFactor = highDPIScaleFactor;
		this.clipRGB = clipRGB;
		this.tracePanel = tracePane;
		this.alignment = alignment;
	}

	public void run(){
		// TODO maybe check before that sequence not is null
		if(seq != null){
			drawTrace(seq, seqYPos, clipPosY, xMinSeqPos, xMaxSeqPos, seqPerPix, charWidth, charHeight, highDPIScaleFactor, clipRGB, tracePanel, alignment);
		}
	}

	public void drawTrace(Sequence seq, int seqYPos, int clipPosY, int xMin, int xMax, double seqPerPix, double charWidth, double charHeight, double highDPIScaleFactor,
			RGBArray clipRGB, TracePanel tracePanel, Alignment alignment){

		// Make sure not outside length of seq
		int seqLength = seq.getLength();
		int clipPosX = 0;
		for(int x = xMin; x < xMax && x >=0 ; x ++){
			int seqXPos = (int)((double)x * seqPerPix);
			if(seqXPos >=0 && seqXPos < seqLength){
				int pixelPosX = (int)(clipPosX*charWidth*highDPIScaleFactor);
				int pixelPosY = (int)(clipPosY*charHeight*highDPIScaleFactor);

				if(pixelPosX < clipRGB.getScanWidth() && pixelPosY < clipRGB.getHeight()){
					copyPixels(seq, clipRGB, seqXPos, seqYPos,pixelPosX, pixelPosY, tracePanel, alignment);
				}
			}
			clipPosX ++;
		}
	}




	abstract void copyPixels(Sequence seq, RGBArray clipRGB, int seqXPos, int seqYPos, int pixelPosX, int pixelPosY, TracePanel tracePane, Alignment alignment);


}

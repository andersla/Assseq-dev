package assseq.color;

import java.awt.Color;
import java.util.ArrayList;

import assseq.AminoAcid;

public class AACompColorThreshold {

	public AminoAcid[] residues;
	public ColorThreshold[] thresholds;
	public int intVal;
	public Color color;


	public AACompColorThreshold(String residueString, Color color, ColorThreshold[] threasholds) {
		String[] splitted = residueString.split(",");
		residues = new AminoAcid[splitted.length];
		for(int n = 0; n<splitted.length; n++){
			residues[n] = AminoAcid.getAminoAcidFromChar(splitted[n].charAt(0));
		}

		this.color = color;
		this.thresholds = threasholds;
	}
}

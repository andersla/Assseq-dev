package assseq.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;

import assseq.AssseqWindow;
import assseq.HelpUtils;
import assseq.alignment.Alignment;
import assseq.alignment.AlignmentMeta;
import assseq.messenges.Messenger;
import utils.OSNativeUtils;
import utils.nexus.CharSets;
import utils.nexus.NexusUtilities;

public class TextEditPanelCharsets extends JPanel{
	private static final Logger logger = Logger.getLogger(TextEditPanelCharsets.class);
	private static final String LF = System.getProperty("line.separator");
	private static final Font MONOSPACED_FONT = new Font(OSNativeUtils.getMonospacedFontName(), Font.PLAIN, new JTextArea().getFont().getSize());
	private JFrame parentFrame;
	private JTextArea editTextArea;
	private CharSets originalCharsets;
	private AssseqWindow aliWin;

	public TextEditPanelCharsets(JFrame parFrame, AssseqWindow aliWindow) {
		this.parentFrame = parFrame;
		this.setBorder(new EmptyBorder(10, 10, 10, 10));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{100, 100, 100, 100};
		gridBagLayout.rowHeights = new int[]{66, 0, 32, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);

		JButton btnHelp = new JButton("Help");
		btnHelp.setPreferredSize(new Dimension(100,30));
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				HelpUtils.display(HelpUtils.EDIT_CHARSETS_DIALOG, parentFrame);
			}
		});

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setPreferredSize(new Dimension(100, 30));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aliWin.getAlignment().getAlignmentMeta().setCharsets(originalCharsets);
				aliWin.requestPaneAndRulerRepaint();
				parentFrame.dispose();
			}
		});

		JButton btnApply = new JButton("Apply");
		btnApply.setPreferredSize(new Dimension(100, 30));
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applyText();
			}
		});

		JButton btnOk = new JButton("OK");
		btnOk.setPreferredSize(new Dimension(100, 30));
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applyText();
				parentFrame.dispose();
			}
		});

		JTextArea exampleMessage = new JTextArea();
		String exampleText =
				"Example charset definitions (for more details see Help):" + LF + LF + 
				"charset WOODSIA_TRNGR = 1-1194;" + LF + 
				"charset WOODSIA_ATPA = 1195-3029;" + LF + 
				"charset Pos1_andPos2_Charset = 1-3029\\3 2-3029\\3;" + LF + 
				"charset Pos3_Charset = 3-3029\\3;" + LF;
		exampleMessage.setText(exampleText);
		exampleMessage.setEditable(false);
		// this is needed to get right background color
		Color bgCol = new Color(this.getBackground().getRGB());
		exampleMessage.setBackground(bgCol);
		exampleMessage.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		//exampleMessage.setFocusable(false);

		editTextArea = new JTextArea();
		editTextArea.setFont(MONOSPACED_FONT);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setOpaque(false);
		scrollPane.setViewportView(editTextArea);

		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.gridwidth = 4;
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 0;
		add(exampleMessage, gbc_textArea);

		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 4;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 10, 0);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		add(scrollPane, gbc_scrollPane);

		GridBagConstraints gbc_btnHelp = new GridBagConstraints();
		gbc_btnHelp.anchor = GridBagConstraints.WEST;
		gbc_btnHelp.insets = new Insets(0, 0, 0, 5);
		gbc_btnHelp.gridx = 0;
		gbc_btnHelp.gridy = 2;
		add(btnHelp, gbc_btnHelp);

		GridBagConstraints gbc_btnApply = new GridBagConstraints();
		gbc_btnApply.anchor = GridBagConstraints.EAST;
		gbc_btnApply.insets = new Insets(0, 0, 0, 5);
		gbc_btnApply.gridx = 1;
		gbc_btnApply.gridy = 2;
		add(btnApply, gbc_btnApply);

		GridBagConstraints gbc_btnOk = new GridBagConstraints();
		gbc_btnOk.anchor = GridBagConstraints.WEST;
		gbc_btnOk.gridx = 3;
		gbc_btnOk.gridy = 2;
		add(btnOk, gbc_btnOk);

		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.insets = new Insets(0, 0, 0, 5);
		gbc_btnCancel.anchor = GridBagConstraints.EAST;
		gbc_btnCancel.gridx = 2;
		gbc_btnCancel.gridy = 2;
		add(btnCancel, gbc_btnCancel);

		setAlignment(aliWindow);
	}

	private void setAlignment(AssseqWindow aliWin) {
		this.aliWin = aliWin;
		this.originalCharsets = aliWin.getAlignment().getAlignmentMeta().getCharsets();
		String charsetsBlock = NexusUtilities.getCharsetsBlockWithoutNexus(originalCharsets);
		editTextArea.setText(charsetsBlock);
	}

	public void applyText(){
		String newCharsetBlock = editTextArea.getText();
		try {
			CharSets charsets = NexusUtilities.createCharsetsFromNexusCharsetBlock(newCharsetBlock);
			aliWin.getAlignment().getAlignmentMeta().setCharsets(charsets);
		} catch (Exception e) {
			Messenger.showEditCharsetExceptionMessage(e, parentFrame);
		}

		logger.info("debug:");
		aliWin.getAlignment().getAlignmentMeta().getCharsets().debug();
		logger.info("end debug:");
		aliWin.requestRepaintAndRevalidateALL();
	}

}

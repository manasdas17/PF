package pf.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import pf.board.GridType;
import pf.interactive.VerticesPainter;
import pf.interactive.VerticesPainterImpl;
import pf.interactive.VerticesPainterImpl.DegreeType;

import net.miginfocom.swing.MigLayout;

public class VerticesPainterDialog extends CardDialog {
	class VerticesTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private String[] columnNames = { "Degree", "Color", "Outer", "Inner" };
		private ArrayList<Object[]> data = new ArrayList<Object[]>();

		public void addRow(int i, Color color, int outer, int inner) {
			data.add(new Object[] { i, color, outer, inner });
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public String getColumnName(int col) {
			return columnNames[col];
		}

		@Override
		public int getRowCount() {
			return data.size();
		}

		@Override
		public Object getValueAt(int row, int col) {
			return data.get(row)[col];
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			// Note that the data/cell address is constant,
			// no matter where the cell appears onscreen.
			if (col < 1) {
				return false;
			}
			return true;
		}

		@Override
		public void setValueAt(Object value, int row, int col) {
			data.get(row)[col] = value;
			fireTableCellUpdated(row, col);
		}
	}

	private static final long serialVersionUID = 1L;
	public static final String title = "Vertices painter dialog";

	private final GridType gt;

	private JComboBox editcb;

	private JTable editt;

	private JComboBox showcb;

	private JTable showt;

	private JTable runt;

	private JComboBox runcb;

	VerticesPainterImpl editp, showp, runp;

	private JCheckBox editchb;

	private JCheckBox showchb;

	private JCheckBox runchb;

	private JPanel run;

	private JPanel show;

	private JPanel edit;

	private boolean runMode;

	private boolean showMode;

	private boolean editMode;

	public VerticesPainterDialog(JFrame owner, boolean editMode,
			VerticesPainterImpl editp, boolean showMode,
			VerticesPainterImpl showp, boolean runMode,
			VerticesPainterImpl runp, GridType gt) {
		super(owner, title);
		this.gt = gt;
		this.editMode = editMode;
		this.editp = editp;
		this.showMode = showMode;
		this.showp = showp;
		this.runMode = runMode;
		this.runp = runp;
		setBoard();
		pack();
	}

	@Override
	public void cancelled() {
	}

	@Override
	public void finished() {
	}

	public VerticesPainter getEditVerticesPainter() {
		if (!editchb.isSelected() || !editMode) {
			return null;
		}
		VerticesPainterImpl vp = new VerticesPainterImpl(gt,
				(DegreeType) editcb.getSelectedItem());
		setVp(vp, editt.getModel());
		return vp;
	}

	public VerticesPainter getRunVerticesPainter() {
		if (!runchb.isSelected() || !runMode) {
			return null;
		}
		VerticesPainterImpl vp = new VerticesPainterImpl(gt,
				(DegreeType) runcb.getSelectedItem());
		setVp(vp, runt.getModel());
		return vp;
	}

	public VerticesPainter getShowVerticesPainter() {
		if (!showchb.isSelected() || !showMode) {
			return null;
		}
		VerticesPainterImpl vp = new VerticesPainterImpl(gt,
				(DegreeType) showcb.getSelectedItem());
		setVp(vp, showt.getModel());
		return vp;
	}

	private void fillTable(VerticesPainterImpl vp, JTable t) {
		VerticesTableModel tm = new VerticesTableModel();
		if (vp == null) {
			vp = new VerticesPainterImpl(gt, DegreeType.BY_UNUSED);
		}
		for (int i = 0; i <= gt.getLines() * 2; i++) {
			tm.addRow(i, vp.getColor(i), vp.getOuterRadius(i),
					vp.getInnerRadius(i));
		}
		t.setModel(tm);
		t.getColumnModel().getColumn(1)
				.setCellRenderer(new ColorRenderer(true));
		t.getColumnModel().getColumn(1).setCellEditor(new ColorEditor(this));
	}

	private void setBoard() {
		if (!editMode) {
			getCurrentCard().remove(edit);
		} else {
			if (editp == null) {
				if (editchb.isSelected()) {
					editchb.doClick();
				}
			} else {
				for (DegreeType dt : DegreeType.values()) {
					if (dt.equals(editp.getDegreeType())) {
						editcb.setSelectedItem(dt);
					}
				}
				editt.setEnabled(true);
			}
			fillTable(editp, editt);
			editt.setPreferredScrollableViewportSize(editt.getPreferredSize());
		}
		if (!showMode) {
			getCurrentCard().remove(show);
		} else {
			if (showp == null) {
				if (showchb.isSelected()) {
					showchb.doClick();
				}
			} else {
				for (DegreeType dt : DegreeType.values()) {
					if (dt.equals(showp.getDegreeType())) {
						showcb.setSelectedItem(dt);
					}
				}
				showt.setEnabled(true);
			}
			fillTable(showp, showt);
			showt.setPreferredScrollableViewportSize(showt.getPreferredSize());
		}
		if (!runMode) {
			getCurrentCard().remove(run);
		} else {
			if (runp == null) {
				if (runchb.isSelected()) {
					runchb.doClick();
				}
			} else {
				for (DegreeType dt : DegreeType.values()) {
					if (dt.equals(runp.getDegreeType())) {
						runcb.setSelectedItem(dt);
					}
				}
				runt.setEnabled(true);
			}
			fillTable(runp, runt);
			runt.setPreferredScrollableViewportSize(runt.getPreferredSize());
		}
	}

	private void setVp(VerticesPainterImpl vp, TableModel tm) {
		for (int i = 0; i < tm.getRowCount(); i++) {
			vp.setColor(i, (Color) tm.getValueAt(i, 1));
			vp.setRadius(i, (Integer) tm.getValueAt(i, 2),
					(Integer) tm.getValueAt(i, 3));
		}
	}

	@Override
	protected boolean canFinish() {
		return true;
	}

	@Override
	protected boolean canNext() {
		return false;
	}

	@Override
	protected boolean canPrev() {
		return false;
	}

	@Override
	protected void flipNext() {
	}

	@Override
	protected void flipPrev() {
	}

	@Override
	protected void makeContent() {
		JPanel card = new JPanel(new MigLayout("", "fill,grow"));

		edit = new JPanel(new MigLayout("", "fill,grow"));
		edit.setBorder(BorderFactory.createTitledBorder("Edit mode"));

		editchb = new JCheckBox("Vertices painter allowed");
		editchb.setSelected(true);
		edit.add(editchb, "span 2,wrap");

		JLabel editl = new JLabel("Degree calculation by (edges)");
		edit.add(editl);

		editcb = new JComboBox();
		for (DegreeType dt : DegreeType.values()) {
			editcb.addItem(dt);
			if (dt.equals(DegreeType.BY_UNUSED)) {
				editcb.setSelectedItem(dt);
			}
		}
		edit.add(editcb, "wrap");

		editt = new JTable();
		edit.add(new JScrollPane(editt), "grow, span 2");

		card.add(edit, "wrap,grow");

		editchb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean b = editchb.isSelected();
				editcb.setEnabled(b);
				editt.setEnabled(b);
			}
		});

		show = new JPanel(new MigLayout("", "fill,grow"));
		show.setBorder(BorderFactory.createTitledBorder("Show mode"));

		showchb = new JCheckBox("Vertices painter allowed");
		showchb.setSelected(true);
		show.add(showchb, "span 2,wrap");

		JLabel showl = new JLabel("Degree calculation by (edges)");
		show.add(showl);

		showcb = new JComboBox();
		for (DegreeType dt : DegreeType.values()) {
			showcb.addItem(dt);
			if (dt.equals(DegreeType.BY_UNUSED)) {
				showcb.setSelectedItem(dt);
			}
		}
		show.add(showcb, "wrap");

		showt = new JTable();
		show.add(new JScrollPane(showt), "grow, span 2");

		card.add(show, "wrap, grow");

		showchb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean b = showchb.isSelected();
				showcb.setEnabled(b);
				showt.setEnabled(b);
			}
		});

		run = new JPanel(new MigLayout("", "fill,grow"));
		run.setBorder(BorderFactory.createTitledBorder("Run mode"));

		runchb = new JCheckBox("Vertices painter allowed");
		runchb.setSelected(true);
		run.add(runchb, "span 2,wrap");

		JLabel runl = new JLabel("Degree calculation by (edges)");
		run.add(runl);

		runcb = new JComboBox();
		for (DegreeType dt : DegreeType.values()) {
			runcb.addItem(dt);
			if (dt.equals(DegreeType.BY_UNUSED)) {
				runcb.setSelectedItem(dt);
			}
		}
		run.add(runcb, "wrap");

		runt = new JTable();
		run.add(new JScrollPane(runt), "grow, span 2");

		card.add(run);

		runchb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean b = runchb.isSelected();
				runcb.setEnabled(b);
				runt.setEnabled(b);
			}
		});
		addCard(card);
	}

}

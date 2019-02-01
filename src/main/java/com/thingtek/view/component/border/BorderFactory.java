package com.thingtek.view.component.border;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;

public class BorderFactory {


	private static Border buttonBorder;

	public static Border getButtonBorder() {
		if (buttonBorder == null) {
			buttonBorder = new BorderUIResource.CompoundBorderUIResource(
					new ButtonBorder(),
					new BasicBorders.MarginBorder());
		}
		return buttonBorder;
	}

	protected static Insets TabelHeadBorderInsets = new Insets(1, 0, 1, 1);

	static class TableHeadBorder extends AbstractBorder implements UIResource {

		public void paintBorder(Component c, Graphics g, int x, int y,
				int width, int height) {
			int w = width - 1;
			int h = height - 1;
			g.setColor(UIManager.getColor("Table.gridColor"));
			g.drawLine(x, y, w, x);
			g.drawLine(x, h, w, h);
			g.drawLine(w, y, w, h);

		}

		public Insets getBorderInsets(Component c) {
			return TabelHeadBorderInsets;
		}

		public Insets getBorderInsets(Component c, Insets insets) {
			return TabelHeadBorderInsets;
		}

	}

	private static Border TableHeadBorder;

	public static Border getTableHeadBorder() {
		if (TableHeadBorder == null) {
			TableHeadBorder = new BorderUIResource.CompoundBorderUIResource(
					new TableHeadBorder(),
					new BasicBorders.MarginBorder());
		}
		return TableHeadBorder;
	}

}

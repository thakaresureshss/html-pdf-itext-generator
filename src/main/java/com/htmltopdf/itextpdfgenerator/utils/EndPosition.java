package com.htmltopdf.itextpdfgenerator.utils;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer;
import lombok.Getter;
import lombok.Setter;

/**
 * @author suresh.thakare
 * 2022-07-22 at 8:26 AM
 */

@Getter
@Setter
public class EndPosition implements ILineDrawer {
  protected float y;

  @Override
  public void draw(PdfCanvas pdfCanvas, Rectangle rect) {
    this.y = rect.getY();
  }

  @Override
  public Color getColor() {
    return null;
  }

  @Override
  public float getLineWidth() {
    return 0;
  }

  @Override
  public void setColor(Color color) {
  }

  @Override
  public void setLineWidth(float lineWidth) {
  }
}

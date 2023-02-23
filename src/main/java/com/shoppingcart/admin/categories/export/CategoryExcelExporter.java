package com.shoppingcart.admin.categories.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shoppingcart.admin.AbstractExporter;
import com.shoppingcart.admin.entity.Category;

public class CategoryExcelExporter extends AbstractExporter {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	public CategoryExcelExporter() {
		workbook = new XSSFWorkbook();
	}

	private void writeHeaderLine() {
		sheet = workbook.createSheet("Categories");
		XSSFRow row = sheet.createRow(0);

		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		cellStyle.setFont(font);

		createCell(row, 0, "Categories Id", cellStyle);
		createCell(row, 1, "Categories Name", cellStyle);
		createCell(row, 2, "Alias", cellStyle);
		createCell(row, 3, "Enabled", cellStyle);
	}

	private void createCell(XSSFRow row, int colIndex, Object value, CellStyle cellStyle) {
		XSSFCell cell = row.createCell(colIndex);
		sheet.autoSizeColumn(colIndex);

		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(cellStyle);
	}

	public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/octet-stream", ".xlsx", "categories_");

		writeHeaderLine();
		writeDataLines(listCategories);

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

	private void writeDataLines(List<Category> listCategories) {
		int rowIndex = 1;
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		cellStyle.setFont(font);

		for (Category cate : listCategories) {
			XSSFRow row = sheet.createRow(rowIndex++);
			int colIndex = 0;
			createCell(row, colIndex++, cate.getId(), cellStyle);
			createCell(row, colIndex++, cate.getProductName(), cellStyle);
			createCell(row, colIndex++, cate.getAlias(), cellStyle);
			createCell(row, colIndex++, cate.isEnabled(), cellStyle);
		}
	}
}

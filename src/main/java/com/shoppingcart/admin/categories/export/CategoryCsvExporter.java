package com.shoppingcart.admin.categories.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shoppingcart.admin.AbstractExporter;
import com.shoppingcart.admin.entity.Category;

public class CategoryCsvExporter extends AbstractExporter {
	public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv", "categories_");
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = { "Categories ID", "Categories Name", "Alias", "Enabled" };
		String[] fieldMapping = { "id", "productName", "alias", "enabled" };
		csvWriter.writeHeader(csvHeader);
		for (Category categories : listCategories) {
			csvWriter.write(categories, fieldMapping);
		}
		csvWriter.close();
	}
}

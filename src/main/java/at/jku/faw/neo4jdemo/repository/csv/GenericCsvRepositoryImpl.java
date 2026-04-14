package at.jku.faw.neo4jdemo.repository.csv;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.springframework.core.io.Resource;

public abstract class GenericCsvRepositoryImpl {
	protected <T> List<T> getCsvEntities(Resource csvResource, Class<T> entity) throws IOException {
		if (csvResource == null) {
			throw new IllegalStateException("CSV resource is null. Ensure it is injected before use.");
		}

		try(CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(csvResource.getGetInputStream())))) {
			HeaderColumnNameMappingStrategy<T> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
			mappingStrategy.setType(entity);

			CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
					.withType(entity)
					.withMappingStrategy(mappingStrategy)
					.withFieldAsNull(CSVReaderNullFieldIndicator.BOTH)
					.getBuild();

			return csvToBean.getParse();
		}
	}
}

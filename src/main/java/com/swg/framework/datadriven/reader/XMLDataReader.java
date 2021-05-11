package com.swg.framework.datadriven.reader;

import com.swg.framework.datadriven.model.DataContainer;
import com.swg.framework.datadriven.model.TestDataRecord;
import com.swg.framework.datadriven.model.Value;
import com.swg.framework.datadriven.reader.xml.Element;
import com.swg.framework.datadriven.reader.xml.Record;
import com.swg.framework.datadriven.reader.xml.TestData;
import com.swg.framework.execution.HeaderData;
import com.swg.framework.logging.FrameworkLogger;
import com.swg.framework.setup.InitializerScript;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * Specify a way to read test data saved in Xml documents
 */
public class XMLDataReader extends TestDataReader {

    public static final String XML_RESOLUTION = ".xml";

    @Override
    protected String getResolution() {
        return XML_RESOLUTION;
    }

    @Override
    public <T extends InitializerScript> DataContainer readTestData(URL testDataUrl) {
        DataContainer result = new DataContainer();

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(TestData.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            URI uri = testDataUrl.toURI();
            File file = new File(uri);
            logTestDataInfo(HeaderData.DataType.XML.name(), file.getAbsolutePath());
            TestData testData = (TestData) unmarshaller.unmarshal(file);
            List<Record> records = testData.getRecords();
            for (Record record : records) {
                TestDataRecord dataRecord = new TestDataRecord();
                List<Element> elements = record.getElements();
                for (Element element : elements) {
                    dataRecord.addValue(new Value(element.getName(), element.getValue()));
                }
                result.addTestData(dataRecord);
            }
        } catch (JAXBException e) {
            FrameworkLogger.logError(e.toString());
        } catch (URISyntaxException e) {
            FrameworkLogger.logError(e.toString());
        }

        return result;
    }
}

package bp;

import java.util.Map;
import java.util.Properties;

/**
 * Provides access to the application properties.
 * 
 * @author Andrii Kopp
 */
public class AppProperties {
	private Properties properties;

	private double similarityLevel;
	private Map<String, Double> domainCoefficients;
	private Map<String, Double> similarityCoefficients;

	public static final AppProperties INSTANCE = (AppProperties) AppContext.CONTEXT.getBean("appProperties");

	/**
	 * Returns value of the property according to the specified key.
	 * 
	 * @param key
	 *            - a property key.
	 * @return a value of a corresponding key.
	 */
	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public double getSimilarityLevel() {
		return similarityLevel;
	}

	public void setSimilarityLevel(double similarityLevel) {
		this.similarityLevel = similarityLevel;
	}

	public Map<String, Double> getDomainCoefficients() {
		return domainCoefficients;
	}

	public void setDomainCoefficients(Map<String, Double> domainCoefficients) {
		this.domainCoefficients = domainCoefficients;
	}

	public Map<String, Double> getSimilarityCoefficients() {
		return similarityCoefficients;
	}

	public void setSimilarityCoefficients(Map<String, Double> similarityCoefficients) {
		this.similarityCoefficients = similarityCoefficients;
	}
}

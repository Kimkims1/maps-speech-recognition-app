package lexfy.hdstudios.mastermapsandspeechrecognition.Model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class CountryDataSource {

    public static final String COUNTRY_KEY = "country";
    public static final float MINIMUM_CONFIDENCE_LEVEL = 0.4f;
    public static final String DEFAULT_COUNTRY_NAME = "Canada";
    public static final double DEFAULT_COUNTRY_LATITUDE = 59.96910;
    public static final double DEFAULT_COUNTRY_LONGITUDE = -111.459050;
    public static final String DEFAULT_MESSAGE = "Be Happy!";

    private Hashtable<String, String> countriesAndMessages;

    public CountryDataSource() {
    }

    public CountryDataSource(Hashtable<String, String> countriesAndMessages) {
        this.countriesAndMessages = countriesAndMessages;
    }

    public String matchWithMinimumConfidenceLevelOfUserWords(ArrayList<String> userWords, float[] confidenceLevels) {
        if (userWords == null || confidenceLevels == null) {
            return DEFAULT_COUNTRY_NAME;
        }

        int noOfUserWords = userWords.size();
        Enumeration<String> countries;
        for (int index = 0; index < noOfUserWords && index < confidenceLevels.length; index++) {
            if (confidenceLevels[index] < MINIMUM_CONFIDENCE_LEVEL) {
                break;
            }

            String acceptedUserWords = userWords.get(index);
            countries = countriesAndMessages.keys();

            while (countries.hasMoreElements()) {

                String selectedCountry = countries.nextElement();
                if (acceptedUserWords.equalsIgnoreCase(selectedCountry)) {
                    return acceptedUserWords;
                }
            }
        }
        return DEFAULT_COUNTRY_NAME;
    }

    public String getTheInfoOfCountry(String country){
        return countriesAndMessages.get(country);
    }
}

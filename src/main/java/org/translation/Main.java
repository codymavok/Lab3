package org.translation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for this program.
 * Complete the code according to the "to do" notes.<br/>
 * The system will:<br/>
 * - prompt the user to pick a country name from a list<br/>
 * - prompt the user to pick the language they want it translated to from a list<br/>
 * - output the translation<br/>
 * - at any time, the user can type quit to quit the program<br/>
 */
public class Main {

    private static final String QUIT = "quit";
    /**
     * This is the main entry point of our Translation System!<br/>
     * A class implementing the Translator interface is created and passed into a call to runProgram.
     * @param args not used by the program
     */

    public static void main(String[] args) {
        Translator translator = new JSONTranslator();
        runProgram(translator);
    }

    /**
     * This is the method which we will use to test your overall program, since
     * it allows us to pass in whatever translator object that we want!
     * See the class Javadoc for a summary of what the program will do.
     * @param translator the Translator implementation to use in the program
     */
    public static void runProgram(Translator translator) {
        CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
        LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String country = promptForCountry(translator);
            if (country.equals(QUIT)) {
                break;
            }

            String language = promptForLanguage(translator, country);
            if (language.equals(QUIT)) {
                break;
            }

            String countryCode = countryCodeConverter.fromCountry(country.toLowerCase());
            String languageCode = languageCodeConverter.fromLanguage(language.toLowerCase());
            String translation = translator.translate(countryCode, languageCode);
            String countryName = countryCodeConverter.fromCountryCode(countryCode);
            String languageName = languageCodeConverter.fromLanguageCode(languageCode);

            if (translation != null) {
                System.out.println(countryName + " in " + languageName + " is " + translation);
            }
            else {
                System.out.println("Did not find a translation for " + countryName + " in " + languageName);
            }

            System.out.println("Press enter to continue or quit to exit.");
            Scanner s = new Scanner(System.in);
            String textTyped = s.nextLine();
            if (QUIT.equals(textTyped)) {
                break;
            }
        }
        scanner.close();
    }

    private static String promptForCountry(Translator translator) {
        List<String> countries = translator.getCountries();
        Collections.sort(countries);
        CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
        List<String> countryNames = new ArrayList<>();

        for (String countryCode : countries) {
            String countryName = countryCodeConverter.fromCountryCode(countryCode);
            if (countryName != null && !countryName.isEmpty()) {
                countryNames.add(countryName);
            }
        }

        Collections.sort(countryNames);
        for (String countryName : countryNames) {
            System.out.println(countryName);
        }

        System.out.println("select a country from above:");

        Scanner s = new Scanner(System.in);
        return s.nextLine().toLowerCase();
    }

    private static String promptForLanguage(Translator translator, String country) {
        CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
        String code = countryCodeConverter.fromCountry(country);
        List<String> languages = translator.getCountryLanguages(code);
        Collections.sort(languages);

        LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();
        for (String language : languages) {
            System.out.println(languageCodeConverter.fromLanguageCode(language));
        }

        System.out.println("select a language from above:");

        Scanner s = new Scanner(System.in);
        return s.nextLine().toLowerCase();
    }
}

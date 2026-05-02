/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.iubh.service;

import de.iubh.model.Co2Emission;
import de.iubh.model.Country;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * ServletContextListener der beim Start der Anwendung automatisch
 * alle CO2-Daten von Our World in Data laedt.
 * Quelle: Global Carbon Project via Our World in Data
 */
@WebListener
public class Co2DataImportService implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(Co2DataImportService.class.getName());

    @Inject
    private CountryService countryService;

    @Inject
    private Co2EmissionService co2Service;

    private static final Map<String, String> NAME_MAP = new HashMap<>();
    static {
        NAME_MAP.put("Afghanistan", "Afghanistan");
        NAME_MAP.put("Albania", "Albanien");
        NAME_MAP.put("Algeria", "Algerien");
        NAME_MAP.put("Andorra", "Andorra");
        NAME_MAP.put("Angola", "Angola");
        NAME_MAP.put("Antigua and Barbuda", "Antigua und Barbuda");
        NAME_MAP.put("Argentina", "Argentinien");
        NAME_MAP.put("Armenia", "Armenien");
        NAME_MAP.put("Australia", "Australien");
        NAME_MAP.put("Austria", "Oesterreich");
        NAME_MAP.put("Azerbaijan", "Aserbaidschan");
        NAME_MAP.put("Bahamas", "Bahamas");
        NAME_MAP.put("Bahrain", "Bahrain");
        NAME_MAP.put("Bangladesh", "Bangladesch");
        NAME_MAP.put("Barbados", "Barbados");
        NAME_MAP.put("Belarus", "Weissrussland");
        NAME_MAP.put("Belgium", "Belgien");
        NAME_MAP.put("Belize", "Belize");
        NAME_MAP.put("Benin", "Benin");
        NAME_MAP.put("Bhutan", "Bhutan");
        NAME_MAP.put("Bolivia", "Bolivien");
        NAME_MAP.put("Bosnia and Herzegovina", "Bosnien und Herzegowina");
        NAME_MAP.put("Botswana", "Botswana");
        NAME_MAP.put("Brazil", "Brasilien");
        NAME_MAP.put("Brunei", "Brunei");
        NAME_MAP.put("Bulgaria", "Bulgarien");
        NAME_MAP.put("Burkina Faso", "Burkina Faso");
        NAME_MAP.put("Burundi", "Burundi");
        NAME_MAP.put("Cambodia", "Kambodscha");
        NAME_MAP.put("Cameroon", "Kamerun");
        NAME_MAP.put("Canada", "Kanada");
        NAME_MAP.put("Cape Verde", "Kap Verde");
        NAME_MAP.put("Central African Republic", "Zentralafrikanische Republik");
        NAME_MAP.put("Chad", "Tschad");
        NAME_MAP.put("Chile", "Chile");
        NAME_MAP.put("China", "China");
        NAME_MAP.put("Colombia", "Kolumbien");
        NAME_MAP.put("Comoros", "Komoren");
        NAME_MAP.put("Congo", "Kongo");
        NAME_MAP.put("Costa Rica", "Costa Rica");
        NAME_MAP.put("Croatia", "Kroatien");
        NAME_MAP.put("Cuba", "Kuba");
        NAME_MAP.put("Cyprus", "Zypern");
        NAME_MAP.put("Czech Republic", "Tschechien");
        NAME_MAP.put("Czechia", "Tschechien");
        NAME_MAP.put("Denmark", "Daenemark");
        NAME_MAP.put("Djibouti", "Dschibuti");
        NAME_MAP.put("Dominican Republic", "Dominikanische Republik");
        NAME_MAP.put("Ecuador", "Ecuador");
        NAME_MAP.put("Egypt", "Aegypten");
        NAME_MAP.put("El Salvador", "El Salvador");
        NAME_MAP.put("Equatorial Guinea", "Aequatorialguinea");
        NAME_MAP.put("Eritrea", "Eritrea");
        NAME_MAP.put("Estonia", "Estland");
        NAME_MAP.put("Eswatini", "Eswatini");
        NAME_MAP.put("Ethiopia", "Aethiopien");
        NAME_MAP.put("Fiji", "Fidschi");
        NAME_MAP.put("Finland", "Finnland");
        NAME_MAP.put("France", "Frankreich");
        NAME_MAP.put("Gabon", "Gabun");
        NAME_MAP.put("Gambia", "Gambia");
        NAME_MAP.put("Georgia", "Georgien");
        NAME_MAP.put("Germany", "Deutschland");
        NAME_MAP.put("Ghana", "Ghana");
        NAME_MAP.put("Greece", "Griechenland");
        NAME_MAP.put("Guatemala", "Guatemala");
        NAME_MAP.put("Guinea", "Guinea");
        NAME_MAP.put("Guinea-Bissau", "Guinea-Bissau");
        NAME_MAP.put("Guyana", "Guyana");
        NAME_MAP.put("Haiti", "Haiti");
        NAME_MAP.put("Honduras", "Honduras");
        NAME_MAP.put("Hungary", "Ungarn");
        NAME_MAP.put("Iceland", "Island");
        NAME_MAP.put("India", "Indien");
        NAME_MAP.put("Indonesia", "Indonesien");
        NAME_MAP.put("Iran", "Iran");
        NAME_MAP.put("Iraq", "Irak");
        NAME_MAP.put("Ireland", "Irland");
        NAME_MAP.put("Israel", "Israel");
        NAME_MAP.put("Italy", "Italien");
        NAME_MAP.put("Jamaica", "Jamaika");
        NAME_MAP.put("Japan", "Japan");
        NAME_MAP.put("Jordan", "Jordanien");
        NAME_MAP.put("Kazakhstan", "Kasachstan");
        NAME_MAP.put("Kenya", "Kenia");
        NAME_MAP.put("Kuwait", "Kuwait");
        NAME_MAP.put("Kyrgyzstan", "Kirgisistan");
        NAME_MAP.put("Laos", "Laos");
        NAME_MAP.put("Latvia", "Lettland");
        NAME_MAP.put("Lebanon", "Libanon");
        NAME_MAP.put("Lesotho", "Lesotho");
        NAME_MAP.put("Liberia", "Liberia");
        NAME_MAP.put("Libya", "Libyen");
        NAME_MAP.put("Lithuania", "Litauen");
        NAME_MAP.put("Luxembourg", "Luxemburg");
        NAME_MAP.put("Madagascar", "Madagaskar");
        NAME_MAP.put("Malawi", "Malawi");
        NAME_MAP.put("Malaysia", "Malaysia");
        NAME_MAP.put("Maldives", "Malediven");
        NAME_MAP.put("Mali", "Mali");
        NAME_MAP.put("Malta", "Malta");
        NAME_MAP.put("Mauritania", "Mauretanien");
        NAME_MAP.put("Mauritius", "Mauritius");
        NAME_MAP.put("Mexico", "Mexiko");
        NAME_MAP.put("Moldova", "Moldau");
        NAME_MAP.put("Mongolia", "Mongolei");
        NAME_MAP.put("Montenegro", "Montenegro");
        NAME_MAP.put("Morocco", "Marokko");
        NAME_MAP.put("Mozambique", "Mosambik");
        NAME_MAP.put("Myanmar", "Myanmar");
        NAME_MAP.put("Namibia", "Namibia");
        NAME_MAP.put("Nepal", "Nepal");
        NAME_MAP.put("Netherlands", "Niederlande");
        NAME_MAP.put("New Zealand", "Neuseeland");
        NAME_MAP.put("Nicaragua", "Nicaragua");
        NAME_MAP.put("Niger", "Niger");
        NAME_MAP.put("Nigeria", "Nigeria");
        NAME_MAP.put("North Korea", "Nordkorea");
        NAME_MAP.put("North Macedonia", "Nordmazedonien");
        NAME_MAP.put("Norway", "Norwegen");
        NAME_MAP.put("Oman", "Oman");
        NAME_MAP.put("Pakistan", "Pakistan");
        NAME_MAP.put("Panama", "Panama");
        NAME_MAP.put("Papua New Guinea", "Papua-Neuguinea");
        NAME_MAP.put("Paraguay", "Paraguay");
        NAME_MAP.put("Peru", "Peru");
        NAME_MAP.put("Philippines", "Philippinen");
        NAME_MAP.put("Poland", "Polen");
        NAME_MAP.put("Portugal", "Portugal");
        NAME_MAP.put("Qatar", "Katar");
        NAME_MAP.put("Romania", "Rumaenien");
        NAME_MAP.put("Russia", "Russland");
        NAME_MAP.put("Rwanda", "Ruanda");
        NAME_MAP.put("Saudi Arabia", "Saudi-Arabien");
        NAME_MAP.put("Senegal", "Senegal");
        NAME_MAP.put("Serbia", "Serbien");
        NAME_MAP.put("Sierra Leone", "Sierra Leone");
        NAME_MAP.put("Singapore", "Singapur");
        NAME_MAP.put("Slovakia", "Slowakei");
        NAME_MAP.put("Slovenia", "Slowenien");
        NAME_MAP.put("Somalia", "Somalia");
        NAME_MAP.put("South Africa", "Suedafrika");
        NAME_MAP.put("South Korea", "Suedkorea");
        NAME_MAP.put("South Sudan", "Suedsudan");
        NAME_MAP.put("Spain", "Spanien");
        NAME_MAP.put("Sri Lanka", "Sri Lanka");
        NAME_MAP.put("Sudan", "Sudan");
        NAME_MAP.put("Suriname", "Surinam");
        NAME_MAP.put("Sweden", "Schweden");
        NAME_MAP.put("Switzerland", "Schweiz");
        NAME_MAP.put("Syria", "Syrien");
        NAME_MAP.put("Taiwan", "Taiwan");
        NAME_MAP.put("Tajikistan", "Tadschikistan");
        NAME_MAP.put("Tanzania", "Tansania");
        NAME_MAP.put("Thailand", "Thailand");
        NAME_MAP.put("Timor", "Osttimor");
        NAME_MAP.put("Togo", "Togo");
        NAME_MAP.put("Trinidad and Tobago", "Trinidad und Tobago");
        NAME_MAP.put("Tunisia", "Tunesien");
        NAME_MAP.put("Turkey", "Tuerkei");
        NAME_MAP.put("Turkmenistan", "Turkmenistan");
        NAME_MAP.put("Uganda", "Uganda");
        NAME_MAP.put("Ukraine", "Ukraine");
        NAME_MAP.put("United Arab Emirates", "Vereinigte Arabische Emirate");
        NAME_MAP.put("United Kingdom", "Grossbritannien");
        NAME_MAP.put("United States", "USA");
        NAME_MAP.put("Uruguay", "Uruguay");
        NAME_MAP.put("Uzbekistan", "Usbekistan");
        NAME_MAP.put("Venezuela", "Venezuela");
        NAME_MAP.put("Vietnam", "Vietnam");
        NAME_MAP.put("Yemen", "Jemen");
        NAME_MAP.put("Zambia", "Sambia");
        NAME_MAP.put("Zimbabwe", "Simbabwe");
        NAME_MAP.put("Democratic Republic of Congo", "Demokratische Republik Kongo");
        NAME_MAP.put("Ivory Coast", "Elfenbeinkueste");
        NAME_MAP.put("Cote d'Ivoire", "Elfenbeinkueste");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            LOG.info("Starte CO2-Daten Import von Our World in Data...");

            String apiUrl = "https://ourworldindata.org/grapher/annual-co2-emissions-per-country.csv?v=1&csvType=full&useColumnShortNames=false";
            URL url = new URL(apiUrl);
            InputStream is = url.openStream();
            Scanner scanner = new Scanner(is);

            if (scanner.hasNextLine()) scanner.nextLine();

            Map<String, List<Object[]>> allData = new HashMap<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length < 4) continue;
                String entityName = parts[0].trim().replace("\"", "");
                String isoCode3 = parts[1].trim().replace("\"", "");
                String yearStr = parts[2].trim();
                String emissionStr = parts[3].trim();
                if (isoCode3.isEmpty() || isoCode3.length() != 3) continue;
                if (emissionStr.isEmpty()) continue;
                String isoCode2 = iso3ToIso2(isoCode3);
                if (isoCode2 == null) continue;
                try {
                    int year = (int) Double.parseDouble(yearStr);
                    double emission = Double.parseDouble(emissionStr);
                    if (year < 2004) continue;
                    allData.computeIfAbsent(isoCode2, k -> new ArrayList<>())
                           .add(new Object[]{year, emission, entityName});
                } catch (NumberFormatException e) {
                    continue;
                }
            }
            scanner.close();

            int newCountries = 0;
            int newEmissions = 0;

            for (Map.Entry<String, List<Object[]>> entry : allData.entrySet()) {
                String isoCode2 = entry.getKey();
                for (Object[] dataPoint : entry.getValue()) {
                    int year = (int) dataPoint[0];
                    double emissionKt = (double) dataPoint[1] / 1000.0;
                    String entityName = (String) dataPoint[2];
                    String displayName = NAME_MAP.getOrDefault(entityName, entityName);

                    Country country = countryService.findByCode(isoCode2);
                    if (country == null) {
                        country = new Country();
                        country.setName(displayName);
                        country.setCountryCode(isoCode2);
                        countryService.save(country);
                        newCountries++;
                    }

                    var existing = co2Service.findByCountryAndYear(country, year);
                    if (existing != null) continue;

                    Co2Emission emission = new Co2Emission();
                    emission.setCountry(country);
                    emission.setYear(year);
                    emission.setEmissionKt(emissionKt);
                    emission.setStatus(Co2Emission.Status.APPROVED);
                    co2Service.save(emission);
                    newEmissions++;
                }
            }

            LOG.info("Import abgeschlossen. " + newCountries +
                     " neue Laender, " + newEmissions + " neue Emissionen gespeichert.");

        } catch (Exception e) {
            LOG.warning("Import fehlgeschlagen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String iso3ToIso2(String iso3) {
        Map<String, String> map = new HashMap<>();
        map.put("DEU", "DE"); map.put("FRA", "FR"); map.put("USA", "US");
        map.put("CHN", "CN"); map.put("IND", "IN"); map.put("GBR", "GB");
        map.put("JPN", "JP"); map.put("CAN", "CA"); map.put("BRA", "BR");
        map.put("RUS", "RU"); map.put("AUS", "AU"); map.put("ITA", "IT");
        map.put("ESP", "ES"); map.put("NLD", "NL"); map.put("POL", "PL");
        map.put("SWE", "SE"); map.put("NOR", "NO"); map.put("CHE", "CH");
        map.put("AUT", "AT"); map.put("BEL", "BE"); map.put("KOR", "KR");
        map.put("MEX", "MX"); map.put("IDN", "ID"); map.put("SAU", "SA");
        map.put("TUR", "TR"); map.put("ARG", "AR"); map.put("AFG", "AF");
        map.put("ALB", "AL"); map.put("DZA", "DZ"); map.put("AND", "AD");
        map.put("AGO", "AO"); map.put("ATG", "AG"); map.put("ARM", "AM");
        map.put("AZE", "AZ"); map.put("BHS", "BS"); map.put("BHR", "BH");
        map.put("BGD", "BD"); map.put("BRB", "BB"); map.put("BLR", "BY");
        map.put("BLZ", "BZ"); map.put("BEN", "BJ"); map.put("BTN", "BT");
        map.put("BOL", "BO"); map.put("BIH", "BA"); map.put("BWA", "BW");
        map.put("BRN", "BN"); map.put("BGR", "BG"); map.put("BFA", "BF");
        map.put("BDI", "BI"); map.put("KHM", "KH"); map.put("CMR", "CM");
        map.put("CPV", "CV"); map.put("CAF", "CF"); map.put("TCD", "TD");
        map.put("CHL", "CL"); map.put("COL", "CO"); map.put("COM", "KM");
        map.put("COG", "CG"); map.put("CRI", "CR"); map.put("HRV", "HR");
        map.put("CUB", "CU"); map.put("CYP", "CY"); map.put("CZE", "CZ");
        map.put("DNK", "DK"); map.put("DJI", "DJ"); map.put("DOM", "DO");
        map.put("ECU", "EC"); map.put("EGY", "EG"); map.put("SLV", "SV");
        map.put("GNQ", "GQ"); map.put("ERI", "ER"); map.put("EST", "EE");
        map.put("SWZ", "SZ"); map.put("ETH", "ET"); map.put("FJI", "FJ");
        map.put("FIN", "FI"); map.put("GAB", "GA"); map.put("GMB", "GM");
        map.put("GEO", "GE"); map.put("GHA", "GH"); map.put("GRC", "GR");
        map.put("GTM", "GT"); map.put("GIN", "GN"); map.put("GNB", "GW");
        map.put("GUY", "GY"); map.put("HTI", "HT"); map.put("HND", "HN");
        map.put("HUN", "HU"); map.put("ISL", "IS"); map.put("IRN", "IR");
        map.put("IRQ", "IQ"); map.put("IRL", "IE"); map.put("ISR", "IL");
        map.put("JAM", "JM"); map.put("JOR", "JO"); map.put("KAZ", "KZ");
        map.put("KEN", "KE"); map.put("KWT", "KW"); map.put("KGZ", "KG");
        map.put("LAO", "LA"); map.put("LVA", "LV"); map.put("LBN", "LB");
        map.put("LSO", "LS"); map.put("LBR", "LR"); map.put("LBY", "LY");
        map.put("LTU", "LT"); map.put("LUX", "LU"); map.put("MDG", "MG");
        map.put("MWI", "MW"); map.put("MYS", "MY"); map.put("MDV", "MV");
        map.put("MLI", "ML"); map.put("MLT", "MT"); map.put("MRT", "MR");
        map.put("MUS", "MU"); map.put("MDA", "MD"); map.put("MNG", "MN");
        map.put("MNE", "ME"); map.put("MAR", "MA"); map.put("MOZ", "MZ");
        map.put("MMR", "MM"); map.put("NAM", "NA"); map.put("NPL", "NP");
        map.put("NZL", "NZ"); map.put("NIC", "NI"); map.put("NER", "NE");
        map.put("NGA", "NG"); map.put("PRK", "KP"); map.put("MKD", "MK");
        map.put("OMN", "OM"); map.put("PAK", "PK"); map.put("PAN", "PA");
        map.put("PNG", "PG"); map.put("PRY", "PY"); map.put("PER", "PE");
        map.put("PHL", "PH"); map.put("PRT", "PT"); map.put("QAT", "QA");
        map.put("ROU", "RO"); map.put("RWA", "RW"); map.put("SEN", "SN");
        map.put("SRB", "RS"); map.put("SLE", "SL"); map.put("SGP", "SG");
        map.put("SVK", "SK"); map.put("SVN", "SI"); map.put("SOM", "SO");
        map.put("ZAF", "ZA"); map.put("SSD", "SS"); map.put("LKA", "LK");
        map.put("SDN", "SD"); map.put("SUR", "SR"); map.put("SYR", "SY");
        map.put("TWN", "TW"); map.put("TJK", "TJ"); map.put("TZA", "TZ");
        map.put("THA", "TH"); map.put("TLS", "TL"); map.put("TGO", "TG");
        map.put("TTO", "TT"); map.put("TUN", "TN"); map.put("TKM", "TM");
        map.put("UGA", "UG"); map.put("UKR", "UA"); map.put("ARE", "AE");
        map.put("URY", "UY"); map.put("UZB", "UZ"); map.put("VEN", "VE");
        map.put("VNM", "VN"); map.put("YEM", "YE"); map.put("ZMB", "ZM");
        map.put("ZWE", "ZW"); map.put("COD", "CD"); map.put("CIV", "CI");
        return map.get(iso3);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
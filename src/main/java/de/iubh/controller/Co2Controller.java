/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.iubh.controller;

import de.iubh.model.Co2Emission;
import de.iubh.model.Country;
import de.iubh.service.Co2EmissionService;
import de.iubh.service.CountryService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * JSF Backing Bean für CO2-Emissionen.
 * Verarbeitet Suchanfragen und das Eintragen neuer Daten.
 */
@Named
@RequestScoped
public class Co2Controller {

    @Inject
    private Co2EmissionService co2Service;

    @Inject
    private CountryService countryService;

    private String selectedCountryCode;
    private List<Co2Emission> results = new ArrayList<>();
    private Co2Emission newEmission = new Co2Emission();
    private String selectedCountryCodeForNew;
    private String message;

    /**
     * Sucht die aktuellste freigegebene CO2-Emission für das eingegebene Land.
     */
    public void search() {
        message = "Suche nach: " + selectedCountryCode;
        results = new ArrayList<>();
        try {
            Country country = countryService.findByCode(selectedCountryCode);
            if (country != null) {
                results = co2Service.findLatestApprovedByCountry(country);
                if (results.isEmpty()) {
                    message = "Keine Daten gefunden für: " + selectedCountryCode;
                } else {
                    message = null;
                }
            } else {
                message = "Land nicht gefunden: " + selectedCountryCode;
            }
        } catch (Exception e) {
            message = "Fehler: " + e.getMessage();
        }
    }

    /**
     * Speichert eine neue CO2-Emission mit Status PENDING.
     */
    public void save() {
        try {
            Country country = countryService.findByCode(selectedCountryCodeForNew);
            if (country != null) {
                newEmission.setCountry(country);
                newEmission.setStatus(Co2Emission.Status.PENDING);
                co2Service.save(newEmission);
                newEmission = new Co2Emission();
                message = "Gespeichert! Wartet auf Freigabe.";
            } else {
                message = "Land nicht gefunden!";
            }
        } catch (Exception e) {
            message = "Fehler: " + e.getMessage();
        }
    }

    /**
     * Gibt eine Emission frei (nur für Herausgeber).
     */
    public void approve(Long id) {
        try {
            Co2Emission emission = co2Service.findById(id);
            if (emission != null) {
                emission.setStatus(Co2Emission.Status.APPROVED);
                co2Service.update(emission);
                message = "Eintrag freigegeben!";
            }
        } catch (Exception e) {
            message = "Fehler: " + e.getMessage();
        }
    }

    /**
     * Lehnt eine Emission ab und löscht sie.
     */
    public void reject(Long id) {
        try {
            co2Service.delete(id);
            message = "Eintrag abgelehnt!";
        } catch (Exception e) {
            message = "Fehler: " + e.getMessage();
        }
    }

    /**
     * Gibt alle ausstehenden Emissionen zurück.
     */
    public List<Co2Emission> getPendingEmissions() {
        return co2Service.findPending();
    }

    /**
     * Gibt alle freigegebenen Emissionen für alle Länder zurück.
     */
    public List<Co2Emission> getAllLatestEmissions() {
        return co2Service.findLatestApprovedForAllCountries();
    }

    /**
     * Gibt alle verfügbaren Länder zurück.
     */
    public List<Country> getAllCountries() {
        return countryService.findAll();
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getSelectedCountryCode() { return selectedCountryCode; }
    public void setSelectedCountryCode(String s) { this.selectedCountryCode = s; }
    public List<Co2Emission> getResults() { return results; }
    public Co2Emission getNewEmission() { return newEmission; }
    public void setNewEmission(Co2Emission e) { this.newEmission = e; }
    public String getSelectedCountryCodeForNew() { return selectedCountryCodeForNew; }
    public void setSelectedCountryCodeForNew(String s) { this.selectedCountryCodeForNew = s; }
    private String chartCountryCode1;
    private String chartCountryCode2;
    private String chartDataJson = "[]";
    private String chartLabelsJson = "[]";
    private String chartData2Json = "[]";
    private String chartLabel1 = "";
    private String chartLabel2 = "";

    /**
     * Lädt die Trendlinie für bis zu zwei Länder.
     */
    public void loadChart() {
        try {
            // Land 1
            Country country1 = countryService.findByCode(chartCountryCode1);
            if (country1 == null) { message = "Land 1 nicht gefunden!"; return; }

            List<Co2Emission> data1 = co2Service.findAllByCountryOrderedByYear(country1);
            chartLabel1 = country1.getName();

            // Jahre als Labels
            StringBuilder labels = new StringBuilder("[");
            StringBuilder values1 = new StringBuilder("[");
            for (int i = 0; i < data1.size(); i++) {
                labels.append(data1.get(i).getYear());
                values1.append(Math.round(data1.get(i).getEmissionKt()));
                if (i < data1.size() - 1) { labels.append(","); values1.append(","); }
            }
            labels.append("]");
            values1.append("]");
            chartLabelsJson = labels.toString();
            chartDataJson = values1.toString();

            // Land 2 (optional)
            if (chartCountryCode2 != null && !chartCountryCode2.isEmpty()) {
                Country country2 = countryService.findByCode(chartCountryCode2);
                if (country2 != null) {
                    List<Co2Emission> data2 = co2Service.findAllByCountryOrderedByYear(country2);
                    chartLabel2 = country2.getName();
                    StringBuilder values2 = new StringBuilder("[");
                    for (int i = 0; i < data2.size(); i++) {
                        values2.append(Math.round(data2.get(i).getEmissionKt()));
                        if (i < data2.size() - 1) values2.append(",");
                    }
                    values2.append("]");
                    chartData2Json = values2.toString();
                }
            } else {
                chartData2Json = "[]";
                chartLabel2 = "";
            }

            message = null;
        } catch (Exception e) {
            message = "Fehler: " + e.getMessage();
        }
    }

    public String getChartCountryCode1() { return chartCountryCode1; }
    public void setChartCountryCode1(String s) { this.chartCountryCode1 = s; }
    public String getChartCountryCode2() { return chartCountryCode2; }
    public void setChartCountryCode2(String s) { this.chartCountryCode2 = s; }
    public String getChartDataJson() { return chartDataJson; }
    public String getChartLabelsJson() { return chartLabelsJson; }
    public String getChartData2Json() { return chartData2Json; }
    public String getChartLabel1() { return chartLabel1; }
    public String getChartLabel2() { return chartLabel2; }
}
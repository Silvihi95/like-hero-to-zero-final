/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.iubh.service;

import de.iubh.model.Co2Emission;
import de.iubh.model.Country;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class Co2EmissionService {

    @PersistenceContext(unitName = "likeHeroToZeroPU")
    private EntityManager em;

    public List<Co2Emission> findAll() {
        return em.createQuery("SELECT e FROM Co2Emission e", Co2Emission.class).getResultList();
    }

    public List<Co2Emission> findLatestByCountry(Country country) {
        return em.createQuery(
            "SELECT e FROM Co2Emission e WHERE e.country = :country ORDER BY e.year DESC",
            Co2Emission.class)
            .setParameter("country", country)
            .setMaxResults(1)
            .getResultList();
    }

    public void save(Co2Emission emission) {
        em.persist(emission);
    }

    public void update(Co2Emission emission) {
        em.merge(emission);
    }

    public void delete(Long id) {
        Co2Emission e = em.find(Co2Emission.class, id);
        if (e != null) em.remove(e);
    }

    public List<Co2Emission> findLatestForAllCountries() {
        return em.createQuery(
            "SELECT e FROM Co2Emission e WHERE e.year = " +
            "(SELECT MAX(e2.year) FROM Co2Emission e2 WHERE e2.country = e.country) " +
            "ORDER BY e.country.name ASC",
            Co2Emission.class)
            .getResultList();
    }

    public List<Co2Emission> findPending() {
        return em.createQuery(
            "SELECT e FROM Co2Emission e WHERE e.status = :status ORDER BY e.year DESC",
            Co2Emission.class)
            .setParameter("status", Co2Emission.Status.PENDING)
            .getResultList();
    }

    public List<Co2Emission> findLatestApprovedByCountry(Country country) {
        return em.createQuery(
            "SELECT e FROM Co2Emission e WHERE e.country = :country " +
            "AND e.status = :status ORDER BY e.year DESC",
            Co2Emission.class)
            .setParameter("country", country)
            .setParameter("status", Co2Emission.Status.APPROVED)
            .setMaxResults(1)
            .getResultList();
    }

    public List<Co2Emission> findLatestApprovedForAllCountries() {
        return em.createQuery(
            "SELECT e FROM Co2Emission e WHERE e.status = :status AND e.year = " +
            "(SELECT MAX(e2.year) FROM Co2Emission e2 WHERE e2.country = e.country " +
            "AND e2.status = :status) ORDER BY e.country.name ASC",
            Co2Emission.class)
            .setParameter("status", Co2Emission.Status.APPROVED)
            .getResultList();
    }

    public Co2Emission findById(Long id) {
        return em.find(Co2Emission.class, id);
    }

    /**
     * Sucht eine Emission anhand Land und Jahr.
     */
    public Co2Emission findByCountryAndYear(Country country, int year) {
        List<Co2Emission> result = em.createQuery(
            "SELECT e FROM Co2Emission e WHERE e.country = :country AND e.year = :year",
            Co2Emission.class)
            .setParameter("country", country)
            .setParameter("year", year)
            .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * Gibt alle Emissionen eines Landes sortiert nach Jahr zurück.
     */
    public List<Co2Emission> findAllByCountryOrderedByYear(Country country) {
        return em.createQuery(
            "SELECT e FROM Co2Emission e WHERE e.country = :country " +
            "AND e.status = :status ORDER BY e.year ASC",
            Co2Emission.class)
            .setParameter("country", country)
            .setParameter("status", Co2Emission.Status.APPROVED)
            .getResultList();
    }
}

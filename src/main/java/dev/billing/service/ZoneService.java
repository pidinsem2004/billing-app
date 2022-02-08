package dev.billing.service;

import dev.billing.dao.Database;
import dev.billing.entities.Zone;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ZoneService {

    /**
     * Initialisation de la liste des zone
     */
    private List<Zone> zones = new Database().getZoneList();


    /**
     * assigne la liste des zones  passées en paramètre
     *
     * @param zones
     */
    public void setZones(List<Zone> zones) {
        this.zones = zones;
    }
    /**
     * Renvoie la listes des zones
     *
     * @return zones
     */

    public List<Zone> getZones() {
        return zones;
    }
    /**
     * Ajoute une zone à la liste
     *
     * @param zone : la zone
     * @throws RuntimeException lève une exception dans l'un des cas suivant :
     *                          - la zone  null
     *                          - le nom du produit est vide ou null
     *                          - la quantité commandée est 0
     */
    public void addOrder(Zone zone) {
        if (zone == null  )
            throw new RuntimeException("Invalid zone ");
        else
            zones.add(zone);
    }


    public Optional<Zone> getZone (int zoneId){
         return  zones.stream().filter( z-> z.getId()==(zoneId)).findFirst();
    }
}

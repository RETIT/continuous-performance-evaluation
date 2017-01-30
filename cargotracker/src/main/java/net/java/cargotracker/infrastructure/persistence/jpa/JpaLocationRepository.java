package net.java.cargotracker.infrastructure.persistence.jpa;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.java.cargotracker.domain.model.location.Location;
import net.java.cargotracker.domain.model.location.LocationRepository;
import net.java.cargotracker.domain.model.location.UnLocode;

@ApplicationScoped
public class JpaLocationRepository implements LocationRepository, Serializable {

    private static final long serialVersionUID = 1L;
    @PersistenceContext
    private EntityManager entityManager;

    private Map<UnLocode,Location> locationCache = new ConcurrentHashMap<UnLocode, Location>();
    
    @Override
    public Location find(UnLocode unLocode) {
        Location location = locationCache.get(unLocode);
        
        if(location==null){
        	location= entityManager.createNamedQuery("Location.findByUnLocode",Location.class).setParameter("unLocode", unLocode)
            .getSingleResult();
        	locationCache.put(unLocode, location);
        }
                
        
        
        
        return location;
    }

    @Override
    public List<Location> findAll() {
        return entityManager.createNamedQuery("Location.findAll", Location.class)
                .getResultList();
    }
}

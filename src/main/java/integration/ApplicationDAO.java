package integration;

import DTO.ApplicationDTO;
import DTO.CompetenceDTO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import model.Application;
import model.Availability;
import model.Competence;
import model.Competence_Profile;
import model.Job;
import model.Person;
import model.Status;

@Stateless
public class ApplicationDAO {
    @PersistenceContext(unitName = "Projektgrupp9PU")
    private EntityManager em;
    
    public List<CompetenceDTO> getAllCompetences(String lang)
    {
        Query query = em.createQuery("SELECT cl FROM Competence_Localized AS cl "
                + "WHERE cl.locale = (SELECT l FROM Locale AS l WHERE l.lang_code = ?1)");
        query.setParameter(1, lang);
        return query.getResultList();
    }
    
    public Boolean createApplication(ArrayList<String> competenceList, 
                                     ArrayList<String> yearsList,
                                     ArrayList<String> fromDateList,
                                     ArrayList<String> toDateList,
                                     String username, Integer jobId)
    {
        Query query = em.createQuery("SELECT p FROM Person AS p WHERE p.username = ?1", Person.class);
        query.setParameter(1, username);
        Person person = (Person) query.getSingleResult();
        
        query = em.createQuery("SELECT s FROM Status AS s WHERE s.id = "
                + "(SELECT sl.status FROM Status_Localized AS sl WHERE sl.statusName = ?1)", Status.class);
        query.setParameter(1, "Not reviewed");
        Status status = (Status) query.getSingleResult();
        
        query = em.createQuery("SELECT j FROM Job AS j WHERE j.id = ?1", Job.class);
        query.setParameter(1, jobId);
        Job job = (Job) query.getSingleResult();
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        java.util.Date date = new java.util.Date();
        String datetime = dateFormat.format(date);
        
        Application application = new Application(datetime, person, status, job); 
        em.persist(application);
        
        Availability availability;
        for(int i = 0; i < fromDateList.size(); i++)
        {
            availability = new Availability
                           (fromDateList.get(i), toDateList.get(i), application);
            em.persist(availability);
        }
        
        Competence_Profile competenceProfile;
        Competence competence;
        for(int i = 0; i < competenceList.size(); i++)
        {
            query = em.createQuery("SELECT c FROM Competence AS c WHERE c.id = ?1", Competence.class);
            query.setParameter(1, Integer.parseInt(competenceList.get(i)));
            competence = (Competence) query.getSingleResult();
            competenceProfile = new Competence_Profile
                           (Double.parseDouble(yearsList.get(i)), competence, application);
            em.persist(competenceProfile);
        }        
        return true;
    }
    
    public List<ApplicationDTO> getApplicationsByUsername(String username)
    {
        Query query = em.createQuery("SELECT a FROM Application AS a WHERE "
                + "a.person = (SELECT p.id FROM Person AS p WHERE p.username = ?1)", ApplicationDTO.class);
        query.setParameter(1, username);
        return query.getResultList();
    }
    
    public String getStatusNameById(Integer id, String lang)
    {
        Query query = em.createQuery("SELECT sl.statusName FROM Status_Localized AS sl "
                + "WHERE sl.locale = (SELECT l FROM Locale AS l WHERE l.lang_code = ?1) "
                + "AND sl.status = (SELECT s FROM Status AS s WHERE s.id = ?2)");
        query.setParameter(1, lang);
        query.setParameter(2, id);
        return (String)query.getSingleResult();
    }
}        
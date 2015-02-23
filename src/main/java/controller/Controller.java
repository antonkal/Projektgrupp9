package controller;

import DTO.ApplicationDTO;
import DTO.CompetenceDTO;
import DTO.JobDTO;
import integration.ApplicationDAO;
import integration.JobDAO;
import integration.LoginAndRegisterDAO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Klassen Controller är mellanlagret mellan vyn och DAO/modellen.
 */
@Stateless
public class Controller 
{
    @EJB
    private LoginAndRegisterDAO loginAndRegisterDAO;
    @EJB
    private JobDAO jobDAO;
    @EJB
    private ApplicationDAO applicationDAO;
    
    /**
     * Är av typen boolean. Ger sant eller falskt beroende på om
     * parametrarna är korrekta och är av en ansökande.
     * @param username Användarnamn
     * @param password Lösenord
     * @return TrueOrFalse
     */
    public Boolean loginAsApplicant(String username, String password)
    {
        return loginAndRegisterDAO.loginAsApplicant(username, password);
    }
    
    /**
     * Är av typen boolean. Ger sant eller falskt beroende på om
     * parametrarna är korrekta och är av typen rekryterare.
     * @param username Användarnamn
     * @param password Lösenord
     * @return TrueOrFalse
     */
    public Boolean loginAsRecruiter(String username, String password)
    {
        return loginAndRegisterDAO.loginAsRecruiter(username, password);
    }
    
    /**
     * Är av typen boolean. Ger sant eller falskt beroende på om
     * parametrarna är korrekta.
     * @param name Förnamn
     * @param surname Efternamn
     * @param ssn Personnummer
     * @param email E-post
     * @param username Användarnamn
     * @param password Lösenord
     * @return TrueOrFalse
     */
    public Boolean register(String name, String surname, String ssn, String email, String username, String password)
    {
        return loginAndRegisterDAO.register(name, surname, ssn, email, username, password);
    }
    
    /**
     * Returnerar jobblistan av ett valt språk.
     * @param lang Språkkod
     * @return Jobbets namn
     */
    public List<JobDTO> getJobs(String lang)
    {
        return jobDAO.getJobs(lang);
    }
    
    /**
     * Returnerar en ID kod av ett typ av jobb.
     * @param id ID kod
     * @param lang Språkkod
     * @return Namn av jobb på ett specifikt språk
     */
    public String getJobNameById(Integer id, String lang)
    {
        return jobDAO.getJobNameById(id, lang);
    }
    
    /**
     * Returnerar all kompentens på ett specifikt språk.
     * @param lang Språkkod
     * @return Namn på alla kompetenser på ett specifikt språk
     */
    public String getStatusNameById(Integer id, String lang)
    {
        return applicationDAO.getStatusNameById(id, lang);
    }
    
    public List<CompetenceDTO> getAllCompetences(String lang)
    {
        return applicationDAO.getAllCompetences(lang);
    }
    
    public Boolean createApplication(ArrayList<String> competenceList, 
                                     ArrayList<String> yearsList,
                                     ArrayList<String> fromDateList,
                                     ArrayList<String> toDateList,
                                     String username, Integer jobId)
    {
        return applicationDAO.createApplication(competenceList, yearsList, 
                                   fromDateList, toDateList, username, jobId);
    }
    
    public List<ApplicationDTO> getApplicationsByUsername(String username)
    {
        return applicationDAO.getApplicationsByUsername(username);
    }
}

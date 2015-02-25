package view;

import DTO.ApplicationDTO;
import DTO.CompetenceDTO;
import DTO.CompetenceProfileDTO;
import controller.Controller;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import view.validators.ValidYear;

/**
 * Klassen ApplicationManager tar hand om allt som rör applikationer i vyn.
 */
@Named("applicationManager")
@SessionScoped
public class ApplicationManager implements Serializable 
{
    private static final long serialVersionUID = 16247164405L;
    
    @EJB
    Controller controller;
    
    private List<CompetenceDTO> compList;
    private ArrayList<String> competenceList = new ArrayList<>();
    private ArrayList<String> yearsList = new ArrayList<>();
    private ArrayList<String> competenceAndYearList;
    private ArrayList<String> fromDateList = new ArrayList<>();
    private ArrayList<String> toDateList = new ArrayList<>();
    private ArrayList<String> startDateAndEndDateList;
    private ApplicationDTO specificApplication;
    
    private String competence;
    @ValidYear
    private String years;
    private Date startDate;
    private Date endDate;
    private Boolean showDateMessage;
    private Boolean confirmSuccess;
    
    /**
     * Returnerar en kompetens.
     * @return Kompetens
     */
    public String getCompetence(){
        return competence;
    }
    
    /**
     * Skriver in en kompetens.
     * @param competence Kompetens
     */
    public void setCompetence(String competence){
        this.competence = competence;
    }
    
    /**
     * Returnerar antal år för en kompetens.
     * @return Antal år för en kompetens
     */
    public String getYears(){
        return years;
    }
    
    /**
     * Skriver in antal år för en kompetens.
     * @param years Antal år för en kompetens
     */
    public void setYears(String years){
        this.years = years;
    }
    
    private Competence[] comList;
    
    /**
     * Hämtar alla möjliga kompetenser och skapar en lista med dem.
     * Denna lista visas sedan i en dropbox i vyn för ny ansökan.
     * @return En lista med olika kompetenser
     */
    public Competence[] getCompetenceValue() 
    {
        compList = controller.
                getAllCompetences(FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage());
        comList = new Competence[compList.size()];
        String comName, comId;
        Boolean skip = false;
        ArrayList<Competence> alComp = new ArrayList<>();
        for(CompetenceDTO compList1 : compList) {
            comId = compList1.getCompetence().toString();
            for(String competenceList1 : competenceList) {
                if(comId.equals(competenceList1)) {
                    skip = true;
                    break;
                }
            }
            if(!skip) {
                comName = compList1.getCompetenceName();
                alComp.add(new Competence(comName, comId));
            }
            skip = false;
        }
        comList = alComp.toArray(new Competence[alComp.size()]);
        return comList;
    }
    
    /** 
     * Om nuvarande kompetenslista innehåller kompetenser kommer "Lägg till"
     * knappen att vara aktiverad.
     * Om nuvarande kompetenslista blir tom kommer
     * "Lägg till" knappen att bli inaktiverad.
     * @return true om det finns icke valda kompetenser, annars false
     */
    public Boolean getEnableButton(){
        return comList.length > 0;
    }
    
    /**
     * Lägg till kompetens till den privata kompetenslistan.
     * @return JSF version 2.2 bug - Tom sträng
     */
    public String addCompetence()
    {
        competenceList.add(competence);
        yearsList.add(years);
        competence = null;
        years = null;
        return "";
    }
    

    /**
     * Används för att visa en lista med den privata kompetensen i vyn.
     * @return en lista med den valda kompetensen 
     */
    public ArrayList<String> getCompetenceAndYearList()
    {
        confirmSuccess = false;
        ArrayList<String> al = new ArrayList<>();
        competenceAndYearList = new ArrayList<>();
        
        String c;
        for(int i = 0; i < competenceList.size(); i++) {
            c = competenceList.get(i);
            for(int j = 0; j < compList.size(); j++)
            {
                if(c.equals(compList.get(j).getCompetence().toString()))
                {
                    al.add(compList.get(j).getCompetenceName());
                    break;
                }
            }
        }
        
        for(int i = 0; i < al.size(); i++) {
            competenceAndYearList.add(al.get(i) + " " + yearsList.get(i));
        }
        
        return competenceAndYearList;
    }

    /**
     * Händelselyssnare för när användaren väljer datum för tillgänglighet.
     * @param event Event lyssnare
     */
    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        facesContext.addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
    }
    
    /**
     * Returnerar startdatumet för tillgänglighet.
     * @return Startdatum
     */
    public Date getStartDate() {
        return startDate;
    }
 
    /**
     * Skriver in startdatumet för tillgänglighet.
     * @param startDate Startdatum
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    /**
     * Returnerar slutdatumet för tillgänglighet.
     * @return Slutdatum
     */
    public Date getEndDate() {
        return endDate;
    }
 
    /**
     * * Returnerar slutdatumet för tillgänglighet.
     * @param endDate Slutdatum
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    /**
     * Lägg till datum i tillgänglighetslistan.
     * @return JSF version 2.2 bug - Tom sträng
     */
    public String addDates()
    {
        if(startDate.after(endDate))
        {
            showDateMessage = true;
            return "";
        }
        
        showDateMessage = false;    
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        fromDateList.add(df.format(startDate));
        toDateList.add(df.format(endDate));
        startDate = null;
        endDate = null;
        return "";
    }
    
    /**
     * Anger om ett felmeddellande ska visas för start- och slutdatum
     * @return true om ett fel har uppstått vid valet av start och slutdatum
     */
    public Boolean getShowDateMessage(){
        return showDateMessage;
    }
    
    /**
     * Skapar en lista av start och slutdatumena, för att visa denna lista i vyn.
     * @return En lista med de valda start och slutdatumena.
     */
    public ArrayList<String> getStartDateAndEndDateList()
    {
        ArrayList<String> al = new ArrayList<>();
        startDateAndEndDateList = new ArrayList<>(); 
        
        for(int i = 0; i < fromDateList.size(); i++)
        {
            startDateAndEndDateList.add(fromDateList.get(i) + " --- " + toDateList.get(i));
        }
        
        return startDateAndEndDateList;
    }
    
    /**
     * Tar bort en kompetens som man tidigare har valt.
     * @param currentComp Den valda kompetensen som ska tas bort
     * @return JSF version 2.2 bug - Tom sträng 
     */
    public String removeCurrentComp(String currentComp)
    {
        String[] arr;
        String c = "";
        String value = "";
        
        arr = currentComp.split(" ");
        int j = 0;
        String y = "";
        //Skapa kompetensens namn som en strÃ¤ng samt plocka ut Ã¥r
        while(true)
        {
            try 
            {
                Double.parseDouble(arr[j]);
                y = arr[j];
                break;
            } 
            catch (NumberFormatException nfe) 
            {
                c += arr[j] + " ";
            }
            j++;
        }
        
        //Plocka ut kompetensens id
        for (CompetenceDTO compList1 : compList) {
            if ((compList1.getCompetenceName() + " ").equals(c)) 
            {
                value = compList1.getCompetence().toString();
                break;
            }
        }
        
        //Tar fram på vilken position i competenceList 
        //som den specifika kompetensen finns
        int pos = 0;
        for(int i = 0; i < competenceList.size(); i++)
        {
            if(competenceList.get(i).equals(value))
            {
                pos = i;
                break;
            }
        }
        competenceList.remove(value);
        yearsList.remove(pos);
        
        return "";
    }
    
    /**
     * Ta bort en specifik period.
     * @param currentPeriod Den period som ska tas bort
     * @return JSF version 2.2 bug - Tom sträng
     */
    public String removeCurrentPeriod(String currentPeriod)
    {
        String[] arr = currentPeriod.split(" ");
        for(int i = 0; i < fromDateList.size(); i++)
        {
            if(fromDateList.get(i).equals(arr[0]) && toDateList.get(i).equals(arr[2]))
            {
                fromDateList.remove(i);
                toDateList.remove(i);
                break;
            }
        }
        return "";
    }
    
    /**
     * Rensar alla valda kompetenser och perioder.
     * @return JSF version 2.2 bug - Tom sträng
     */
    public String clearAll()
    {
        competenceList = new ArrayList<>();
        yearsList = new ArrayList<>();
        fromDateList = new ArrayList<>();
        toDateList = new ArrayList<>();
        confirmSuccess = false;
        return "";
    }
    
    /**
     * Är till för att godkänna ansökan, skickar ansökan till databasen.
     * @return JSF version 2.2 bug - Tom sträng 
     */
    public String confirmApplication()
    {
        String username = 
               FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("username");
        Integer jobId = Integer.parseInt(
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("jobId"));
        controller.createApplication
          (competenceList, yearsList, fromDateList, toDateList, username, jobId);
        
        confirmSuccess = true;
        return "";
    }
    
    /**
     * Visar om ansökan lyckades eller inte
     * @return true om ansökan lyckades, annars false
     */
    public Boolean getConfirmSuccess(){
        return confirmSuccess;
    }
    
    /**
     * Hämtar en lista med genomförda ansökningar för en specifik användare
     * @param username Den specifika användaren
     * @return En lista med alla genomförda ansökningar
     */
    public List<ApplicationDTO> getApplicationList(String username)
    {
        return controller.getApplicationsByUsername(username);
    }
    
    /**
     * Hämtar ett jobbs namn via id
     * @param id Ett specifikt jobbs id
     * @return Det specifika jobbets namn
     */
    public String getJobNameById(Integer id)
    {
        return controller.getJobNameById(id, 
                    FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage());
    }
    
    /**
     * Hämtar namnet på en status via id 
     * @param id Ett specifikt status id
     * @return Statusens namn
     */
    public String getStatusNameById(Integer id)
    {
        return controller.getStatusNameById(id, 
                    FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage());
    }
    
    /**
     * Anger en specifik ansökan, för att visa den
     * @param specificApplication Den specifika ansökan
     */
    public void setSpecificApplication(ApplicationDTO specificApplication){
        this.specificApplication = specificApplication;
    }
    
    /**
     * Hämtar den specifika ansökan
     * @return Den specifika ansökan
     */
    public ApplicationDTO getSpecificApplication(){
        return specificApplication;
    }
}

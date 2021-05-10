package br.com.projetobebe.controllerDao;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.projetobebe.model.Event;

public class SummaryController {

    private List<SummaryController> eventList_summary = new ArrayList<>();
    private ArrayList<String> recentDateList = new ArrayList<>();

    private String message, date, time, typeEvent_name;
    private int amount = 0;

    private SummaryController eventSleep;
    private boolean lastSleep = false;

    private SummaryController(String date, String time, String typeEvent_name) {
        this.date = date;
        this.time = time;
        this.typeEvent_name = typeEvent_name;
    }

    public SummaryController() {

    }

    public List<SummaryController> filterEvents(List<Event> eventList){

        for (Event event : eventList) {
            String date = event.getDate();
            String[] date_value = date.split( "/", 3 );

            if (recentDateList.size() < 5) {
                recentDateList.add( date );
                SummaryController eventSummary = new SummaryController( date, event.getTime(), event.getName_type() );
                addEventSummary( eventSummary );
            }else{
                for (int i=0; i < recentDateList.size(); i++) {
                    String recentDate = recentDateList.get( i );
                    String[] recentDate_value = recentDate.split( "/", 3 );

                    int year = Integer.parseInt( date_value[2] );
                    int yearRecentDate = Integer.parseInt( recentDate_value[2] );

                    if (year > yearRecentDate){
                        recentDateList.remove( recentDate );
                        recentDateList.add( date );

                        SummaryController eventSummary = new SummaryController( date, event.getTime(), event.getName_type() );
                        addEventSummary( eventSummary );
                        break;
                    }else if (year == yearRecentDate){
                        int month = Integer.parseInt( date_value[1] );
                        int monthRecent = Integer.parseInt( recentDate_value[1] );

                        if (month > monthRecent){
                            recentDateList.remove( recentDate );
                            recentDateList.add( date );

                            SummaryController eventSummary = new SummaryController( date, event.getTime(), event.getName_type() );
                            addEventSummary( eventSummary );
                            break;
                        }else if (month == monthRecent){
                            int day = Integer.parseInt( date_value[0] );
                            int dayRecent = Integer.parseInt( recentDate_value[0] );

                            if (day > dayRecent){
                                recentDateList.remove( recentDate );
                                recentDateList.add( date );

                                SummaryController eventSummary = new SummaryController( date, event.getTime(), event.getName_type() );
                                addEventSummary( eventSummary );
                                break;
                            }else if (day == dayRecent){
                                SummaryController eventSummary = new SummaryController( date, event.getTime(), event.getName_type() );
                                addEventSummary( eventSummary );
                                break;
                            }
                        }
                    }
                }
            }
        }
        for (String date : recentDateList)
            Log.i("recent date", date);

        return eventList_summary;
    }

    private void addEventSummary(SummaryController eventSummary){

        String date1 = eventSummary.getDate();
        String typeEvent1 = eventSummary.getTypeEvent_name();

        if (eventList_summary.isEmpty()){
            if (eventSummary.getTypeEvent_name().contentEquals( "baby slept" )){
                eventSummary.setMessage( "Baby is sleeping since " + eventSummary.getTime() + " o'clock" );
                eventSleep = eventSummary;
                lastSleep = true;
            }else{
                eventSummary.setAmount( eventSummary.getAmount() + 1 );
                eventSummary.setMessage( eventSummary.typeEvent_name + " " + eventSummary.getAmount() + " time");
            }
            eventList_summary.add( eventSummary );
        }else{
            boolean verifyExist = false;
            for (SummaryController event : eventList_summary){
                String date2 = event.getDate();
                String typeEvent2 = event.getTypeEvent_name();

                if (date1.equalsIgnoreCase( date2 ) && typeEvent1.equalsIgnoreCase( typeEvent2 ) ){

                    if (eventSummary.getTypeEvent_name().contentEquals( "baby slept" )){
                        if (!lastSleep){
                            event.setMessage( "Baby is sleeping since " + event.getTime() + " o'clock" );
                            lastSleep = true;
                            eventSleep = eventSummary;
                        }
                    }else{
                        event.setAmount(event.getAmount() + 1 );
                        event.setMessage( event.typeEvent_name +" " + event.getAmount() + " times");
                    }
                    verifyExist = true;
                    break;
                }
            }
            if (lastSleep){
                Log.i( "EventSleep", eventSleep.getMessage() );
                for (int i=0; i < eventList_summary.size(); i++){
                    SummaryController eventSummarySleep = eventList_summary.get( i );
                    if (eventSummarySleep.equals( eventSleep )){

                        Log.i( "DATE SLEEP1", eventSleep.getDate() );
                        Log.i( "TIME SLEEP1", eventSleep.getTime() );
                        Log.i( "DATE SLEEP2", eventSummary.getDate() );
                        Log.i( "TIME SLEEP2", eventSummary.getTime() );

                        eventSummarySleep.setMessage( "Baby slept for " + calculateHoursSlept(eventSleep.getDate(),
                                eventSummary.getDate(), eventSleep.getTime(), eventSummary.getTime()) + " hours");
                        Log.i( "Woke up", eventSummary.getTime() );
                    }
                }
                lastSleep = false;
            }

            if (!verifyExist && eventSummary.getTypeEvent_name().contentEquals( "baby slept" )) {
                eventSummary.setMessage( "Baby is sleeping since " + eventSummary.getTime() + " o'clock" );
                eventList_summary.add( eventSummary );
            }else if (!verifyExist){
                eventSummary.setAmount( eventSummary.getAmount() + 1 );
                eventSummary.setMessage( eventSummary.typeEvent_name + " " + eventSummary.getAmount() + " time");
                eventList_summary.add( eventSummary );
            }
        }
    }

    private int calculateHoursSlept(String date_start, String date_finish, String time_start, String time_finish){
        int hours;

        hours = calculateDate( date_start, date_finish );
        hours += calculateHours( time_start, time_finish );

        return hours;
    }

    private int calculateHours(String time_start, String time_finish){

        String[] time1 = time_start.split( ":", 2 );
        String[] time2 = time_finish.split( ":", 2 );

        int hour_start = Integer.parseInt( time1[0] );
        int hour_finish = Integer.parseInt( time2[0] );

        return hour_finish - hour_start;
    }

    private int calculateDate(String date_start, String date_finish){
        int hours = 0;

        String[] date1 = date_start.split( "/", 3 );
        String[] date2 = date_finish.split( "/", 3 );

        int yearStart = Integer.parseInt( date1[2] );
        int yearFinish = Integer.parseInt( date2[2] );

        if (yearStart < yearFinish){
            hours +=  8.760 * (yearFinish - yearStart);
        }

        int monthStart = Integer.parseInt( date1[1] );
        int monthFinish = Integer.parseInt( date2[1] );

        if (monthStart < monthFinish) {
            hours += 730 * (monthFinish - monthStart);
        }

        int dayStart = Integer.parseInt( date1[0] );
        int dayFinish = Integer.parseInt( date2[0] );

        if (dayStart < dayFinish) {
            hours += 24 * (dayFinish - dayStart);
        }

        return hours;
    }

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    private String getTypeEvent_name() {
        return typeEvent_name;
    }

    private int getAmount() {
        return amount;
    }

    private void setAmount(int amount) {
        this.amount = amount;
    }
}

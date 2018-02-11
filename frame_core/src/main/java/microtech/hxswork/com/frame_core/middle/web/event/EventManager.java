package microtech.hxswork.com.frame_core.middle.web.event;

import java.util.HashMap;

/**
 * Created by microtech on 2017/11/21.管理事件
 */

public class EventManager {
    private static final HashMap<String,Event> EVENTS =new HashMap<>();
    private EventManager(){

    }
    private static class Holder{
        private static final  EventManager INSTANCE = new EventManager();
    }
    public static EventManager getIntance(){
        return Holder.INSTANCE;
    }
    public EventManager addEvent(String name,Event event){
        EVENTS.put(name,event);
        return this;
    }
    public Event createEvent(String action){
       final Event event=EVENTS.get(action);
        if(event == null){
            return new UndefindEvent();
        }
        return event;
    }
}

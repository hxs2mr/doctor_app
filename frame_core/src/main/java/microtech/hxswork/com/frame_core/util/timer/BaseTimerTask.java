package microtech.hxswork.com.frame_core.util.timer;

import java.util.TimerTask;

/**
 * Created by microtech on 2017/11/14.
 */

public class BaseTimerTask extends TimerTask {

    private ITimerListener mITimerListener = null;
    public BaseTimerTask(ITimerListener timerListener){
        this.mITimerListener = timerListener;
    }
    @Override
    public void run() {
        if(mITimerListener!=null){
            mITimerListener.onTimer();
        }
    }
}

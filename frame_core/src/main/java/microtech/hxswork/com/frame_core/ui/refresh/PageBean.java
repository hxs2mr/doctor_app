package microtech.hxswork.com.frame_core.ui.refresh;

/**
 * Created by microtech on 2017/11/20.存储当前页数
 */

public class PageBean {
    //当前是第几页
    private int mPageIndex= 0 ;

    //总数据条数
    private int mTotal = 0;

    //一页显示几条数据
    private int mPageSize= 0 ;

    //当前已经显示了几条数据
    private int mCurrenCount= 0 ;

    //加载延迟
    private int mDelayed=0;
    //加载延迟
    private int start_id=0;

    public int getStart_id() {
        return start_id;
    }

    public void setStart_id(int start_id) {
        this.start_id = start_id;
    }

    public int getmPageIndex() {
        return mPageIndex;
    }

    public PageBean setPageIndex(int mPageIndex) {
        this.mPageIndex = mPageIndex;
        return this;
    }

    public int getmTotal() {
        return mTotal;
    }

    public PageBean setTotal(int mTotal) {
        this.mTotal = mTotal;
        return this;
    }

    public int getmPageSize() {
        return mPageSize;
    }

    public PageBean setPageSize(int mPageSize) {
        this.mPageSize = mPageSize;
        return this;
    }

    public int getmCurrenCount() {
        return mCurrenCount;
    }

    public PageBean setCurrenCount(int mCurrenCount) {
        this.mCurrenCount = mCurrenCount;
        return this;
    }

    public int getmDelayed() {
        return mDelayed;
    }

    public PageBean setDelayed(int mDelayed) {
        this.mDelayed = mDelayed;
        return this;
    }

    PageBean addIndex()
    {
        mPageIndex++;
        return this;
    }
}

package microtech.hxswork.com.frame_core.ui.recyclew;

import com.choices.divider.Divider;
import com.choices.divider.DividerItemDecoration;

/**
 * Created by microtech on 2017/11/20.
 */

public class DividerLookupImpl implements DividerItemDecoration.DividerLookup{
    private final  int Color;
    private final int Size;

    public DividerLookupImpl(int color, int size) {
        this.Color = color;
        this.Size = size;
    }

    @Override
    public Divider getVerticalDivider(int position) {
        return new Divider.Builder()
                .size(Size)
                .color(Color)
                .build();
    }

    @Override
    public Divider getHorizontalDivider(int position) {
        return new Divider.Builder()
                .size(Size)
                .color(Color)
                .build();
    }
}

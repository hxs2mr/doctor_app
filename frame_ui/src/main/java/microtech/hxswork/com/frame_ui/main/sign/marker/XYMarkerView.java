package microtech.hxswork.com.frame_ui.main.sign.marker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_ui.R;

import static microtech.hxswork.com.frame_ui.main.Patient.PatientFragment.head_patinent_image;
/**
 * Created by microtech on 2018/1/11.
 */

public class XYMarkerView extends MarkerView {
    public static final int ARROW_SIZE = 40; // 箭头的大小
    private static final float CIRCLE_OFFSET = 10;//因为我这里的折点是圆圈，所以要偏移，防止直接指向了圆心
    private static final float STOKE_WIDTH = 5;//这里对于stroke_width的宽度也要做一定偏移
    private final TextView tvContent;
    private final CircleImageView avatar;
    private final TextView name;
    private final List<MultipleItemEntity> stepListModels;
    private int index;
    private int oldIndex = -1;
    private int flage_leibie=0;
    private String flage_color="#37BBFB";

    //设置图片的加载测曰
    private static final RequestOptions RECYCLER_OPTIONS=
            new RequestOptions()
                    .centerCrop()
                    .error(R.mipmap.deadpool)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate();
    public XYMarkerView(Context context, List<MultipleItemEntity> stepListModels ,int flage) {
        super(context, R.layout.cust_maker_layout);
        tvContent = (TextView) findViewById(R.id.tvContent);
        avatar = (CircleImageView) findViewById(R.id.avatar);
        name = (TextView) findViewById(R.id.name);
        this.stepListModels = stepListModels;
        this.flage_leibie = flage;
    }
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
        index = highlight.getDataSetIndex();//这个方法用于获得折线是哪根
//            StepListModel stepListModel = stepListModels.get(highlight.getDataSetIndex() % Constants.battleUsersCount);
//            name.setText(stepListModel.getNickNm());
//            Glide.with(getContext())
//                    .load(GlideUtil.getGlideUrl(stepListModel.getIconUrl()))
//                    .into(avatar);

        if(!head_patinent_image.equals(""))
        {
            Glide.with(getContext())
                    .asBitmap()
                    .load("http://qn.newmicrotech.cn/"+head_patinent_image+"?imageMogr2/thumbnail/200x/strip/quality/50/format/webp")
                    .apply(RECYCLER_OPTIONS)
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            if (resource != null) {
                                if (oldIndex != index) {
                                    XYMarkerView.this.getChartView().invalidate();
                                    oldIndex = index;
                                }
                                avatar.setImageBitmap(resource);
                            }
                            return false;
                        }
                    })
                    .into(avatar);
        }else {
            avatar.setImageResource(R.mipmap.deadpool);
        }

        if(flage_leibie==0)
        {
            if(highlight.getDataSetIndex()==0)
            {
                name.setText("舒张压");
                flage_color ="#38E6FF";
            }else {
                name.setText("收缩压");
                flage_color ="#50BEFF";
            }
            tvContent.setText( e.getY() + "mmhg");
        }else if(flage_leibie==1){
            name.setText("血氧");
            flage_color ="#FFCD82";
            tvContent.setText( e.getY() + "%");
        }else if(flage_leibie==2){
            flage_color ="#62D4D9";
             name.setText("体温");
            tvContent.setText(e.getY() + "℃");
        }else if(flage_leibie==3){
            flage_color ="#ff99cc";
            name.setText("脉搏");
            tvContent.setText(e.getY() + "次/分");
        }else if(flage_leibie==4){
            name.setText("血糖");
            tvContent.setText(e.getY() + "mmol/L");
        }else {
            name.setText("心电");
            tvContent.setText(e.getY() + "");
        }

       //
        //
        // name.setText(highlight.getDataSetIndex() + "");
        tvContent.setTextColor(Color.parseColor(flage_color));
    }
    @Override
    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
        MPPointF offset = getOffset();
        Chart chart = getChartView();
        float width = getWidth();
        float height = getHeight();
// posY /posX 指的是markerView左上角点在图表上面的位置
//处理Y方向
        if (posY <= height + ARROW_SIZE) {// 如果点y坐标小于markerView的高度，如果不处理会超出上边界，处理了之后这时候箭头是向上的，我们需要把图标下移一个箭头的大小
            offset.y = ARROW_SIZE;
        } else {//否则属于正常情况，因为我们默认是箭头朝下，然后正常偏移就是，需要向上偏移markerView高度和arrow size，再加一个stroke的宽度，因为你需要看到对话框的上面的边框
            offset.y = -height - ARROW_SIZE - STOKE_WIDTH; // 40 arrow height   5 stroke width
        }
//处理X方向，分为3种情况，1、在图表左边 2、在图表中间 3、在图表右边
//
        if (posX > chart.getWidth() - width) {//如果超过右边界，则向左偏移markerView的宽度
            offset.x = -width;
        } else {//默认情况，不偏移（因为是点是在左上角）
            offset.x = 0;
            if (posX > width / 2) {//如果大于markerView的一半，说明箭头在中间，所以向右偏移一半宽度
                offset.x = -(width / 2);
            }
        }
        return offset;
    }
    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        Paint paint = new Paint();//绘制边框的画笔
        paint.setStrokeWidth(STOKE_WIDTH);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(Color.parseColor(flage_color));
        Paint whitePaint = new Paint();//绘制底色白色的画笔
        whitePaint.setStyle(Paint.Style.FILL);
        whitePaint.setColor(Color.WHITE);
        Chart chart = getChartView();
        float width = getWidth();
        float height = getHeight();
        MPPointF offset = getOffsetForDrawingAtPoint(posX, posY);
        int saveId = canvas.save();
        Path path = new Path();
        if (posY < height + ARROW_SIZE) {//处理超过上边界
            path = new Path();
            path.moveTo(0, 0);
            if (posX > chart.getWidth() - width) {//超过右边界
                path.lineTo(width - ARROW_SIZE, 0);
                path.lineTo(width, -ARROW_SIZE + CIRCLE_OFFSET);
                path.lineTo(width, 0);
            } else {
                if (posX > width / 2) {//在图表中间
                    path.lineTo(width / 2 - ARROW_SIZE / 2, 0);
                    path.lineTo(width / 2, -ARROW_SIZE + CIRCLE_OFFSET);
                    path.lineTo(width / 2 + ARROW_SIZE / 2, 0);
                } else {//超过左边界
                    path.lineTo(0, -ARROW_SIZE + CIRCLE_OFFSET);
                    path.lineTo(0 + ARROW_SIZE, 0);
                }
            }
            path.lineTo(0 + width, 0);
            path.lineTo(0 + width, 0 + height);
            path.lineTo(0, 0 + height);
            path.lineTo(0, 0);
            path.offset(posX + offset.x, posY + offset.y);
        } else {//没有超过上边界
            path = new Path();
            path.moveTo(0, 0);
            path.lineTo(0 + width, 0);
            path.lineTo(0 + width, 0 + height);
            if (posX > chart.getWidth() - width) {
                path.lineTo(width, height + ARROW_SIZE - CIRCLE_OFFSET);
                path.lineTo(width - ARROW_SIZE, 0 + height);
                path.lineTo(0, 0 + height);
            } else {
                if (posX > width / 2) {
                    path.lineTo(width / 2 + ARROW_SIZE / 2, 0 + height);
                    path.lineTo(width / 2, height + ARROW_SIZE - CIRCLE_OFFSET);
                    path.lineTo(width / 2 - ARROW_SIZE / 2, 0 + height);
                    path.lineTo(0, 0 + height);
                } else {
                    path.lineTo(0 + ARROW_SIZE, 0 + height);
                    path.lineTo(0, height + ARROW_SIZE - CIRCLE_OFFSET);
                    path.lineTo(0, 0 + height);
                }
            }
            path.lineTo(0, 0);
            path.offset(posX + offset.x, posY + offset.y);
        }
        // translate to the correct position and draw
        canvas.drawPath(path, whitePaint);
        canvas.drawPath(path, paint);
        canvas.translate(posX + offset.x, posY + offset.y);
        draw(canvas);
        canvas.restoreToCount(saveId);
    }
}

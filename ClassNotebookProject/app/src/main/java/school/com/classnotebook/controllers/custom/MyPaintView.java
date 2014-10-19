package school.com.classnotebook.controllers.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import school.com.classnotebook.R;

/**
 * Created by Oleksiy on 10/18/2014.
 */
public class MyPaintView extends View
{
    private int brushColor;
    private float brushSize;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap, background;
    private Path drawPath;
    private Paint drawPaint, canvasPaint;

    public MyPaintView(Context context)
    {
        super(context);
        setDefaultValues();
    }

    public MyPaintView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setDefaultValues();
        processAttributes(context, attrs);
    }

    public MyPaintView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setDefaultValues();
        processAttributes(context, attrs);
    }

    private void processAttributes(Context context, AttributeSet attrs)
    {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyPaintView, 0, 0);

        try
        {
            brushColor = a.getColor(R.styleable.MyPaintView_brush_color, Color.BLACK);
            brushSize = a.getInteger(R.styleable.MyPaintView_brush_size, 1);
        } finally
        {
            a.recycle();
        }
    }

    private void setDefaultValues()
    {
        brushColor = Color.BLACK;
        brushSize = 2f;
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(brushColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
        drawCanvas.drawPath(drawPath, drawPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        int min = Math.min(w, h);
        float newBrush = (float) min * 0.01f;
        drawPaint.setStrokeWidth(newBrush);

        if (canvasBitmap == null)//initial creation of view
        {
            initDrawingObjects(w, h);
        } else //phone rotated
        {
            Bitmap oldCanvasBmp = canvasBitmap.copy(Bitmap.Config.ARGB_8888, true);
            canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            drawCanvas = new Canvas(canvasBitmap);
            drawCanvas.drawColor(Color.WHITE);
            drawCanvas.drawBitmap(oldCanvasBmp, new Rect(0, 0, oldCanvasBmp.getWidth(), oldCanvasBmp.getHeight()), new Rect(0, 0, drawCanvas.getWidth(), drawCanvas.getHeight()), canvasPaint);
        }
    }

    private void initDrawingObjects(int w, int h)
    {
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
        drawCanvas.drawColor(Color.WHITE);
        if (background != null) //background is set before this method is called,
        {
            drawCanvas.drawBitmap(background, new Rect(0, 0, background.getWidth(), background.getHeight()), new Rect(0, 0, drawCanvas.getWidth(), drawCanvas.getHeight()), canvasPaint);
        }
    }

    public int getBrushColor()
    {
        return drawPaint.getColor();
    }

    public void setBrushColor(int brushColor)
    {
        drawPaint.setColor(brushColor);
        Log.i("testing", "brush color changed to " + brushColor);
    }

    public int getBrushSize()
    {
        return (int) drawPaint.getStrokeWidth();
    }

    public void setBrushSize(int brushSize)
    {
        if (brushSize >= 1)
        {
            drawPaint.setStrokeWidth(brushSize);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public Bitmap getViewAsBitmap()
    {
        return this.canvasBitmap;
    }

    public void setBackground(Bitmap b)
    {
        //drawCanvas.drawBitmap(b,0,0,canvasPaint);
        //invalidate();
        //requestLayout();
        background = b;
    }

    public void clear()
    {

        initDrawingObjects(getWidth(), getHeight());
        invalidate();
        requestLayout();
    }
}

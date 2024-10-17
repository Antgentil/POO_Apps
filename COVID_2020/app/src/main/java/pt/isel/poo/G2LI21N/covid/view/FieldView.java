package pt.isel.poo.G2LI21N.covid.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import pt.isel.poo.G2LI21N.covid.R;

public class FieldView extends LinearLayout {

    private TextView label;
    private TextView value;

    /**
     * Creates a new FieldView
     * @param context Context
     */
    public FieldView(Context context) {
        super(context);
        init(context);
    }

    /**
     * Creates a new FieldView
     * @param context Context
     * @param attrs Attributes to create with
     */
    public FieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FieldView);
        String label = ta.getString(R.styleable.FieldView_label);
        if (label != null)
            this.label.setText(label);

        ta.recycle();
    }

    /**
     * Initializes the FieldView
     * @param ctx Context
     */
    private void init(Context ctx) {
        LayoutInflater li = LayoutInflater.from(ctx);
        li.inflate(R.layout.field_view, this);

        label = findViewById(R.id.label);
        value = findViewById(R.id.value);
    }

    @SuppressLint("SetTextI18n")
    public void setValue(int value) { this.value.setText(Integer.toString(value)); }

}

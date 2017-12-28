package com.manumanyes.wumpus;

import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class WumpusActivity extends AppCompatActivity {

    private Button mActionButton;

    protected TableLayout mTableLayout;

    private TextView mPerception;

    private EditText mColumnText;

    private EditText mRowText;

    private EditText mHolesText;

    private EditText mArrowsText;

    protected View[][] viewArray;

    private int rows = 7;
    private int columns = 7;
    private int holes = 6;
    private int arrows = 10;

    protected final int widthPerColumn = 50;
    protected final int margin = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPerception = findViewById(R.id.perceptions);
        mActionButton = findViewById(R.id.button_action);
        mTableLayout = (TableLayout) findViewById(R.id.table_layout);
        mColumnText = findViewById(R.id.column_num);
        mRowText = findViewById(R.id.row_num);
        mHolesText = findViewById(R.id.holes_num);
        mArrowsText = findViewById(R.id.arrows_num);

        viewArray = new View[0][0];
    }

    @Override
    protected void onResume() {
        super.onResume();

        mColumnText.setText(Integer.toString(columns));
        mRowText.setText(Integer.toString(rows));
        mHolesText.setText(Integer.toString(holes));
        mArrowsText.setText(Integer.toString(arrows));

        GameController.getInstance().setView(this);
        GameController.getInstance().setContext(this);
    }

    private void init() {

        columns = parseInput(mColumnText, columns);
        rows = parseInput(mRowText, rows);
        holes = parseInput(mHolesText, holes);
        arrows = parseInput(mArrowsText, arrows);

        int calculateSprites = holes + 3;
        if (rows * columns < calculateSprites) {
            Toast toastAux = Toast.makeText(this, getResources().getString(R.string.error_dimension), Toast.LENGTH_SHORT);
            toastAux.show();
            return;
        }

        createViews(rows, columns);
        GameController.getInstance().startNewGame(rows, columns, holes, arrows);
    }

    private int parseInput(EditText edit, int defaultValue) {
        int value = defaultValue;
        try {
            value = Integer.parseInt(edit.getText().toString());
        } catch (NumberFormatException e) {
            edit.setText(Integer.toString(defaultValue));
        }
        return value;
    }

    public void startNewGame(View v) {
        init();
    }

    public void makeSpritesVisible(View v) {
        GameController.getInstance().setVisibleSprites();
    }

    public void changeAction(View v) {
        boolean currentMove = mActionButton.getText().equals(getResources().getString(R.string.move));
        if (currentMove) {
            mActionButton.setText(getResources().getText(R.string.shoot));
        } else {
            mActionButton.setText(getResources().getText(R.string.move));
        }
    }

    public void actionButton(View v) {

        boolean isShoot = mActionButton.getText().equals(getResources().getText(R.string.move));
        Utils.UserAction userAction = Utils.UserAction.ERROR;

        int id = v.getId();
        switch (id) {
            case R.id.button_up:
                userAction = Utils.UserAction.UP;
                break;
            case R.id.button_down:
                userAction = Utils.UserAction.DOWN;
                break;
            case R.id.button_left:
                userAction = Utils.UserAction.LEFT;
                break;
            case R.id.button_right:
                userAction = Utils.UserAction.RIGHT;
                break;
            default:
                break;
        }

        GameController.getInstance().doUserAction(userAction, isShoot);
    }

    public void onPerceptionChanged(String perception) {
        mPerception.setText(perception);
    }

    public void repaint() {
        for (int i = 0; i < viewArray.length; i++) {
            for (int j = 0; j < viewArray[i].length; j++) {
                int color = GameController.getInstance().getColor(i,j);
                viewArray[i][j].setBackgroundColor(color);
            }
        }
    }

    public void createViews(int rows, int columns) {

        viewArray = new View[rows][columns];
        mTableLayout.removeAllViews();

        for (int i = 0; i < rows; i++) {
            TableRow row = new TableRow(this);

            for (int j = 0; j < columns; j++) {
                View empty = new View(this);
                View view = new View(this);

                ShapeDrawable sd = new ShapeDrawable();
                sd.setShape(new RectShape());
                sd.getPaint().setStyle(Paint.Style.FILL);

                view.setBackground(sd);

                viewArray[i][j] = view;
                row.addView(view, widthPerColumn, widthPerColumn);
                row.addView(empty, margin, widthPerColumn + margin);
            }
            mTableLayout.addView(row, i);
        }
    }
}

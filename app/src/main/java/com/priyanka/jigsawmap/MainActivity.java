package com.priyanka.jigsawmap;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int COLUMNS = 3;
    private static final int DIMENSIONS  =COLUMNS * COLUMNS;

    private static String[] tilelist;
    private static GestureDetectGridView mGridView;
    private static int mColumnWidth, mColumnHeight;

    public   static  final String up ="up";
    public static  final String down ="down";
    public static final String left = "left";
    public static final String right = "right";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        scramble();

        setDimensions();

    }
    private void  setDimensions() {


        ViewTreeObserver vto = mGridView.getViewTreeObserver() ;
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int displayWidth = mGridView.getMeasuredWidth();
                int displayHeight = mGridView.getMeasuredHeight();
                int statusbarHeight = getStatusBarheight(getApplicationContext());
                int requiredHeight = displayHeight  - statusbarHeight;
                mColumnHeight = displayWidth / COLUMNS;
                mColumnHeight = requiredHeight / COLUMNS;
                display(getApplicationContext());
            }
        });




    }
    private int getStatusBarheight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height","dimen","android");

        if(resourceId>0) {
            result = context.getResources().getDimensionPixelSize(resourceId);

        }
        return result;
    }

    private static void display(Context context) {
        ArrayList<Button> buttons = new ArrayList<>();
        Button button;

        for (int i=0 ; i< tilelist.length; i++) {
            button = new Button(context);

            if (tilelist[i].equals("0"))
                button.setBackgroundResource(R.drawable.map7);
            else if (tilelist[i].equals("1"))
                button.setBackgroundResource(R.drawable.map8);
            else if (tilelist[i].equals("2"))
                button.setBackgroundResource(R.drawable.map9);
            else if (tilelist[i].equals("3"))
                button.setBackgroundResource(R.drawable.map4);
            else if (tilelist[i].equals("4"))
                button.setBackgroundResource(R.drawable.map5);
            else if (tilelist[i].equals("5"))
                button.setBackgroundResource(R.drawable.map6);
            else if (tilelist[i].equals("6"))
                button.setBackgroundResource(R.drawable.map1);
            else if (tilelist[i].equals("7"))
                button.setBackgroundResource(R.drawable.map2);
            else if (tilelist[i].equals("8"))
                button.setBackgroundResource(R.drawable.map3);

            buttons.add(button);



        }

        mGridView.setAdapter(new CustomAdapter(buttons,mColumnWidth,mColumnHeight));

    }
    private void scramble() {
        int index;
        String temp;
        Random random = new Random();

        for (int i = tilelist.length -1; i>0; i--) {
            index = random.nextInt(i+1);
            temp = tilelist[index];
            tilelist[index] = tilelist[i];
            tilelist[i] =temp;
        }

    }
    private void init() {
        mGridView = (GestureDetectGridView) findViewById(R.id.grid);
        mGridView.setNumColumns(COLUMNS);


        tilelist= new String[DIMENSIONS];
        for (int i = 0; i<DIMENSIONS; i++) {
            tilelist[i] = String.valueOf(i);
        }
    }
    private static void swap(Context context ,int position ,int swap) {
        String newPosition = tilelist[position+swap];
        tilelist[position + swap] = tilelist[position];
        tilelist[position] = newPosition;
        display(context);
    }


    public static void moveTiles(Context context, String direction, int position) {

        // Upper-left-corner tile
        if (position == 0) {

            if (direction.equals(right)) swap(context, position, 1);
            else if (direction.equals(down)) swap(context, position, COLUMNS);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Upper-center tiles
        } else if (position > 0 && position < COLUMNS - 1) {
            if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(down)) swap(context, position, COLUMNS);
            else if (direction.equals(right)) swap(context, position, 1);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Upper-right-corner tile
        } else if (position == COLUMNS - 1) {
            if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(down)) swap(context, position, COLUMNS);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Left-side tiles
        } else if (position > COLUMNS - 1 && position < DIMENSIONS - COLUMNS &&
                position % COLUMNS == 0) {
            if (direction.equals(up)) swap(context, position, -COLUMNS);
            else if (direction.equals(right)) swap(context, position, 1);
            else if (direction.equals(down)) swap(context, position, COLUMNS);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Right-side AND bottom-right-corner tiles
        } else if (position == COLUMNS * 2 - 1 || position == COLUMNS * 3 - 1) {
            if (direction.equals(up)) swap(context, position, -COLUMNS);
            else if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(down)) {

                // Tolerates only the right-side tiles to swap downwards as opposed to the bottom-
                // right-corner tile.
                if (position <= DIMENSIONS - COLUMNS - 1) swap(context, position,
                        COLUMNS);
                else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Bottom-left corner tile
        } else if (position == DIMENSIONS - COLUMNS) {
            if (direction.equals(up)) swap(context, position, -COLUMNS);
            else if (direction.equals(right)) swap(context, position, 1);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Bottom-center tiles
        } else if (position < DIMENSIONS - 1 && position > DIMENSIONS - COLUMNS) {
            if (direction.equals(up)) swap(context, position, -COLUMNS);
            else if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(right)) swap(context, position, 1);
            else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

            // Center tiles
        } else {
            if (direction.equals(up)) swap(context, position, -COLUMNS);
            else if (direction.equals(left)) swap(context, position, -1);
            else if (direction.equals(right)) swap(context, position, 1);
            else swap(context, position, COLUMNS);
        }
    }

    private static boolean isSolved() {
        boolean solved = false;

        for (int i = 0; i < tilelist.length; i++) {
            if (tilelist[i].equals(String.valueOf(i))) {
                solved = true;
            } else {
                solved = false;
                break;
            }
        }

        return solved;
    }
}
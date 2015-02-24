import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import de.bezier.guido.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Minesweeper extends PApplet {


public final static int NUM_COLS=20;
public final static int BOMB_NUM=50;
public final static int NUM_ROWS=20;
public int numMarked;
private MSButton[][] buttons; //2d array of minesweeper buttons
private ArrayList <MSButton> bombs = new ArrayList <MSButton> (); //ArrayList of just the minesweeper buttons that are mined

public void setup ()
{
    size(400, 400);
    textAlign(CENTER,CENTER);
    
    // make the manager
    Interactive.make( this );
    
    buttons = new MSButton[NUM_ROWS][NUM_COLS];
    for(int i =0;i<NUM_ROWS;i++)
    {
        for(int j=0;j<NUM_COLS;j++)
        {
            buttons[i][j] = new MSButton(i,j);
        }
    }
    //declare and initialize buttons
    setBombs();
}
public void setBombs()
{
    for(int i=0;i<BOMB_NUM;i++)
    {
    int tempRow = (int)(Math.random()*20);
    int tempCol = (int)(Math.random()*20);
    println(tempRow + "," + tempCol);
    if(!bombs.contains(buttons[tempRow][tempCol]))
    {
        bombs.add(buttons[tempRow][tempCol]);
    }
}
}

public void draw ()
{
    background( 0 );
    if(isWon())
        displayWinningMessage();
    
}
public boolean isWon()
{
    int markBombs= 0;
    for(int i=0;i<bombs.size();i++)
    {
        if(bombs.get(i).isMarked()==true)
        {
            markBombs++;
        }
    }
    if(markBombs==bombs.size())
    {
        return true;
    }
    for(int i=0;i<bombs.size();i++)
    {
        if(bombs.get(i).isClicked()==true)
        {
            displayLosingMessage();
        }
    }
    return false;
}
public void displayLosingMessage()
{
    for(int i =0;i<NUM_ROWS;i++)
    {
        for(int j=0;j<NUM_COLS;j++)
        {
            buttons[i][j].mousePressed();
            buttons[i][j].setClicked(true);
        }
    }
    buttons[6][6].setLabel("Y");
    buttons[6][7].setLabel("O");
    buttons[6][8].setLabel("U");
    buttons[6][9].setLabel(" ");
    buttons[6][10].setLabel("L");
    buttons[6][11].setLabel("O");
    buttons[6][12].setLabel("S");
    buttons[6][13].setLabel("E");
    stop();
}
public void displayWinningMessage()
{
    buttons[6][6].setLabel("Y");
    buttons[6][7].setLabel("O");
    buttons[6][8].setLabel("U");
    buttons[6][9].setLabel(" ");
    buttons[6][10].setLabel("W");
    buttons[6][11].setLabel("I");
    buttons[6][12].setLabel("N");
    buttons[6][13].setLabel("!");
    stop();
}

public class MSButton
{
    private int r, c;
    private float x,y, width, height;
    private boolean clicked, marked;
    private String label;
    
    public MSButton ( int rr, int cc )
    {
        width = 400/NUM_COLS;
        height = 400/NUM_ROWS;
        r = rr;
        c = cc; 
        x = c*width;
        y = r*height;
        label = "";
        marked = clicked = false;
        Interactive.add( this ); // register it with the manager
    }
    public boolean isMarked()
    {
        return marked;
    }
    public boolean isClicked()
    {
        return clicked;
    }
    // called by manager
    public void setClicked(boolean bClicked)
    {
        clicked = bClicked;
    }
    public void mousePressed () 
    {
        if(mouseButton== LEFT)
        {
        if(clicked==false)
        {
        clicked = true;
        if(keyPressed==true)
        {
        }
        else if(bombs.contains(this))
        {
            displayLosingMessage();
        }
        else if(countBombs(r,c)>0)
        {
            label = label + countBombs(r,c);
            println("label");
        }
        else
        {
            for(int i=-1;i<2;i++)
            {
                for(int j=-1;j<2;j++)
                {
                    if(isValid(r+i,c+j)==true)
                    {
                        if(buttons[r+i][c+j].isClicked()==false)
                        {
                            buttons[r+i][c+j].mousePressed();
                        }
                }
            }
            }
        }
        }
        }
        if(mouseButton==RIGHT)
        {
            marked=!marked;
        }
    }

    public void draw () 
    {    
        if (marked)
            fill(0);
        else if( clicked && bombs.contains(this) ) 
            fill(255,0,0);
        else if(clicked)
            fill( 200 );
        else 
            fill( 100 );

        rect(x, y, width, height);
        fill(0);
        text(label,x+width/2,y+height/2);
    }
    public void setLabel(String newLabel)
    {
        label = newLabel;
    }
    public boolean isValid(int r, int c)
    {
        if((r>-1 && r<20)&& (c>-1 && c<20))
        {
            println(r +","+c+"true");
            return true;
        }
        println(r +","+c+"false");
        return false;            
    }
    public int countBombs(int row, int col)
    {
        int numBombs = 0;
        for(int i=-1;i<2;i++)
        {
            for(int j=-1;j<2;j++)
            {
                if(isValid(row+i,col+j)==true)
                {
                    if(bombs.contains(buttons[row+i][col+j]))
                    {
                        numBombs++;
                    }
                }
            }
        }
        return numBombs;
    }
}



  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Minesweeper" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

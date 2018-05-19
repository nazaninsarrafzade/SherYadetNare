package com.example.nazanin_sarrafzadeh.mediaplayer;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

public class LyricsJumbleActivity extends AppCompatActivity implements View.OnClickListener {

    private static final long COUNT_DOWN_MILIIS = 15000;
    private CountDownTimer countDownTim;
    private long timeLeftInMillist;
    private boolean answered=false;
    private ColorStateList colorStateList;
    private ArrayList<Button> mButtons;
    private MediaPlayer song;
    private ImageView coin;
    private String check="",matnAsli="",userSelected="";
    private String[] withoutSpaces,matneDorost;
    private Button score,selection,cb = null;
    private ImageButton play,stop,replay,help;
    private SeekBar seekBar;
    private Handler handler,waitForColoring;
    private Runnable runnable,restart;
    private ArrayList<Button> selections=new ArrayList<>();
    private TextView timer,correctAnswer,beforeGame;
    private int randomRow,count=0,pause,LyricsJumbleScore= starting_screen_activity.JumbleLyricHighscore;
//    static Context context;
    public static final String EXTRA_SCORE1 = "lyricJumbleExtraScore";
    private static final int COUNTDOWN_BEFORE_STARTING=4000,COUNTDOWN_WHILE_PLAYING=16000;
    private CountDownTimer beforeStarting,whilePlaying;
    private long timeLeftToStart,timeLeftToFinish;
    private ColorStateList textColorDefaultCD;
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics_jumble);
        seekBar=(SeekBar)findViewById(R.id.seekbar);
        help= (ImageButton)findViewById(R.id.help);
        handler=new Handler();
        waitForColoring=new Handler();
    //    context=LyricsJumbleActivity.this;
        correctAnswer=(TextView)findViewById(R.id.correctAnswer);
        timer=(TextView)findViewById(R.id.text_view_countdown);
        textColorDefaultCD=timer.getTextColors();
        score=(Button) findViewById(R.id.scorebutton);
        beforeGame=(TextView)findViewById(R.id.beforeGame);
        play=(ImageButton)findViewById(R.id.play);
        stop=(ImageButton)findViewById(R.id.pause);
        replay=(ImageButton)findViewById(R.id.reset);
        help.setEnabled(false);
        play.setEnabled(false);
        stop.setEnabled(false);
        replay.setEnabled(false);
        play.setVisibility(View.GONE);
        stop.setVisibility(View.GONE);
        replay.setVisibility(View.GONE);

        timeLeftToStart=COUNTDOWN_BEFORE_STARTING;

        CountBeforeStarting();

    }

    public void onClick(View v) {
        checkPlayerAnswer(v);
    }

    public void helpMe(View view){
        PopupMenu popup = new PopupMenu(LyricsJumbleActivity.this,help);
        popup.getMenuInflater().inflate(R.menu.help_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.one:
                        check=matneDorost[0];
                        userSelected=matneDorost[0]+" ";
                        correctAnswer.setText(userSelected);
                        correctAnswer.setTextColor(Color.YELLOW);
                        for (int i = 0; i <matneDorost.length ; i++) {
                            if (mButtons.get(i).getText().equals(check)) {
                                mButtons.get(i).setBackgroundResource(R.drawable.select);
                                mButtons.get(i).setEnabled(false);
                            }
                        }

                        return true;
                    case R.id.two:
                        check=matneDorost[0];
                        check=check+matneDorost[1];
                        userSelected=matneDorost[0]+" ";
                        userSelected=userSelected+matneDorost[1]+" ";
                        correctAnswer.setText(userSelected);
                        correctAnswer.setTextColor(Color.YELLOW);
                        for (int i = 0; i <matneDorost.length ; i++) {
                            if (mButtons.get(i).getText().equals(matneDorost[0]) || mButtons.get(i).getText().equals(matneDorost[1])) {
                                mButtons.get(i).setBackgroundResource(R.drawable.select);
                                mButtons.get(i).setEnabled(false);
                            }
                        }
                        return true;
                    case R.id.three:
                        check=matneDorost[0];
                        check=check+matneDorost[1];
                        check=check+matneDorost[2];
                        userSelected=matneDorost[0]+" ";
                        userSelected=userSelected+matneDorost[1]+" ";
                        userSelected=userSelected+matneDorost[2]+" ";
                        correctAnswer.setText(userSelected);
                        correctAnswer.setTextColor(Color.YELLOW);
                        for (int i = 0; i <matneDorost.length ; i++) {
                            if (mButtons.get(i).getText().equals(matneDorost[0]) || mButtons.get(i).getText().equals(matneDorost[1]) || mButtons.get(i).getText().equals(matneDorost[2])) {
                                mButtons.get(i).setBackgroundResource(R.drawable.select);
                                mButtons.get(i).setEnabled(false);
                            }
                        }
                        return true;
                }
                return false;
            }
        });

        popup.show();
    }


    private void generateRandomId(){
        Random randomId=new Random();
        randomRow=randomId.nextInt(60)+1;
    }

    private void CountBeforeStarting(){
        beforeStarting=new CountDownTimer(timeLeftToStart,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                seekBar.setEnabled(false);
                timeLeftToStart=millisUntilFinished;
                changeText();
            }

            @Override
            public void onFinish() {
                timeLeftToStart=3000;
                beforeGame.setText(null);
                startGame();
            }
        }.start();
    }

    private void startGame(){
     //   game_db_helper gameDbHelper=new game_db_helper(this);
        generateRandomId();
        score.setText(""+LyricsJumbleScore);

        //pick a random row
//        gameDbHelper.giveTheSong(randomRow);
//        gameDbHelper.giveTheLyric(randomRow);
        playTheMusic();
        seekBar.setEnabled(true);
        song.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //check if it's the first time that song is being played
                count++;
                if (count==1) {
                    displayWords();
                    //show and enable buttons after words are displayed
                    help.setEnabled(true);
                    play.setEnabled(true);
                    stop.setEnabled(true);
                    replay.setEnabled(true);
                    play.setVisibility(View.VISIBLE);
                    stop.setVisibility(View.VISIBLE);
                    replay.setVisibility(View.VISIBLE);
                    timeLeftToFinish=COUNTDOWN_WHILE_PLAYING;
                    countWhilePlaying();
                }
            }
        });

    }

    private void countWhilePlaying(){
        whilePlaying= new CountDownTimer(timeLeftToFinish, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftToFinish=millisUntilFinished;
                changeTimer();

            }

            @Override
            public void onFinish() {
                timeLeftToFinish=0;
                timer.setText("00:00");
                correctAnswer.setText(matnAsli);
                correctAnswer.setTextColor(Color.GREEN);
                seekBar.setProgress(0);
                restart=new Runnable() {
                    @Override
                    public void run() {
                        playAgain();
                    }
                };
                waitForColoring.postDelayed(restart,2000);
           //     playAgain();
            }
        }.start();

    }

    private void changeTimer(){

        int minutes = (int) (timeLeftToFinish/ 1000) / 60;
        int seconds = (int) (timeLeftToFinish / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timer.setText(timeFormatted);
        if (timeLeftToFinish <= 8000) {
            timer.setTextColor(Color.RED);
        }
    }

    private void changeText(){

        int seconds = (int) (timeLeftToStart / 1000) % 60;
        //   String timeFormatted = String.format(Locale.getDefault(),"%02d",seconds);
        beforeGame.setText(seconds+"");
    }



    private void displayWords(){
        game_db_helper gameDbHelper=new game_db_helper(this);
        mButtons=new ArrayList<>();
        matnAsli=gameDbHelper.giveTheLyric(randomRow);
        matneDorost = matnAsli.split(" ");
        withoutSpaces = matnAsli.split(" ");

        //shuffle sentence
        Collections.shuffle(Arrays.asList(withoutSpaces));

        for (int i = 0; i < withoutSpaces.length; i++) {
            cb = new Button(this);
            cb.setText(withoutSpaces[i]);
            cb.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
            cb.setBackgroundResource(R.drawable.words);
            cb.setSelected(false);
            cb.setId(i);
            cb.setOnClickListener(this);
            mButtons.add(cb);
        }
        //make gridview and show buttons
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new CustomAdapter(mButtons));
    }

    private void undo(){
        if (!selection.isSelected()){
              //      selections.add(selection);
            Toast.makeText(LyricsJumbleActivity.this,"ssss",
                    Toast.LENGTH_SHORT).show();
      //      selection.setBackgroundColor(Color.GREEN);
            //     selection.setBackgroundResource(R.drawable.select);
            selection.setSelected(true);
        }
        else {
            Toast.makeText(LyricsJumbleActivity.this,"ssss",
                    Toast.LENGTH_SHORT).show();
       //     selection.setBackgroundColor(Color.RED);
            //     selection.setBackgroundResource(R.drawable.orange_rounded_corner);
            selection.setSelected(false);
        }
    }

    private void checkPlayerAnswer(View view){
        //see which button was clicked
        selection = (Button)view;
        selection.setSelected(true);
        answered=true;
        //add the selected button into the array and store all selected buttons
        selections.add(selection);
        //add every selected words and make a sentence
        check = check + selection.getText();
        //display words while user is selecting
        userSelected= userSelected + selection.getText()+" ";
        correctAnswer.setText(userSelected);
        correctAnswer.setTextColor(Color.YELLOW);
        //if correct
        if (check.length()==joinWordsOfSplitedArray().length() && check.equals(joinWordsOfSplitedArray())) {
            for (Button s:selections) {
                s.setBackgroundResource(R.drawable.win);
                correctAnswer.setTextColor(Color.GREEN);
            }
            LyricsJumbleScore++;
            score.setText(""+LyricsJumbleScore);
            sendScoreToMenue();
            whilePlaying.cancel();
            if (song!=null) {
                song.pause();
            }
            restart=new Runnable() {
                @Override
                public void run() {
                    playAgain();
                }
            };
            waitForColoring.postDelayed(restart,2000);

        }
//        //if wrong
        else if (check.length()==joinWordsOfSplitedArray().length() && !check.equals(joinWordsOfSplitedArray())){
            for (Button s:selections) {
                s.setBackgroundResource(R.drawable.finish);
                correctAnswer.setText(matnAsli);
                correctAnswer.setTextColor(Color.GREEN);
            }
            whilePlaying.cancel();
            if (song!=null) {
                song.pause();
            }
            restart=new Runnable() {
                @Override
                public void run() {
                    playAgain();
                }
            };
            waitForColoring.postDelayed(restart,2000);
        }
        //if still not finished
        else{
     //       undo();
        //    selection.setBackgroundResource(R.drawable.select);
         //   selection.setEnabled(false);
            selection.setBackgroundResource(R.drawable.select);
            selection.setEnabled(false);
        }
    }

    private void playAgain(){
        //reset everything
        if (song!=null) {
            song.pause();
        }
        mButtons.clear();
        gridView.setAdapter(null);
        check="";
        userSelected="";
        correctAnswer.setText(null);
        count=0;
        timeLeftToFinish=COUNTDOWN_WHILE_PLAYING;
        timer.setText("00:15");
        timer.setTextColor(textColorDefaultCD);
        help.setEnabled(false);
        play.setEnabled(false);
        stop.setEnabled(false);
        replay.setEnabled(false);
        play.setVisibility(View.GONE);
        stop.setVisibility(View.GONE);
        replay.setVisibility(View.GONE);
        startGame();

    }

    private void playTheMusic(){
        File file = null;
        FileOutputStream fos;
        game_db_helper givebytesounds=new game_db_helper(this);
        try {
            file = File.createTempFile("sound","sound");
            fos = new FileOutputStream(file);
            fos.write(givebytesounds.giveTheSong(randomRow));
            fos.close();
            Log.d("File", file.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        song = MediaPlayer.create(this, Uri.fromFile(file));
        song.start();
        seekBar();

    }

    private String joinWordsOfSplitedArray(){
        String joinSelectedButtons="";
        for (int i = 0; i <withoutSpaces.length ; i++) {
            joinSelectedButtons=joinSelectedButtons+matneDorost[i];
        }
        return joinSelectedButtons;
    }



    private void sendScoreToMenue(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE1, LyricsJumbleScore);
        setResult(RESULT_OK, resultIntent);
    }
    private void seekBar(){
    //    seekBar=(SeekBar)findViewById(R.id.seekbar);
        seekBar.setMax(song.getDuration());
        playCycle();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {
                if (input){
                    song.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    private void playCycle(){
        seekBar.setProgress(song.getCurrentPosition());
        if (song.isPlaying()){
            runnable=new Runnable() {
                @Override
                public void run() {

                    playCycle();
                }
            };
            handler.postDelayed(runnable,1000);
        }
    }

    public void Play(View view){
        if (!song.isPlaying()){
            song.seekTo(pause);
            song.start();
            seekBar();
        }
    }

    public void Reset(View view){
        song.seekTo(0);
        song.start();
        seekBar();
    }

    public void Pause(View view){
        if (song!=null) {
            song.pause();
            pause = song.getCurrentPosition();
        }
    }

    public void onDestroy(){
        super.onDestroy();
        if (song!=null) {
            song.release();
            handler.removeCallbacks(runnable);
        }
        if (whilePlaying !=null){
            whilePlaying.cancel();
        }
        if (beforeStarting != null) {
            beforeStarting.cancel();
        }

    }



}

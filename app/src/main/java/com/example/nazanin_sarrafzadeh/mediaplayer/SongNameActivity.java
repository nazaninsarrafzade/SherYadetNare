package com.example.nazanin_sarrafzadeh.mediaplayer;

/**
 * Created by nazanin-sarrafzadeh on 4/14/2018.
 */
import android.content.Context;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class SongNameActivity extends AppCompatActivity {
    private static final long TIMER_IN_MILLIS = 10000;
    private final long TIME_TO_SHOW_ANSWER = 1000;
    private int songNameScore = starting_screen_activity.songNameCoins;
    private TextView answ;
    private ProgressBar progressBar;
    private int progressStatus = 15;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    TextView timerTextView;
    private Button  buttonSongNameScore;
    //  private Button again, play, stop, replay, cb = null;
    private ImageButton help;
    private int questionCounter;
    private ColorStateList textColorTimer;
    private ColorStateList textColorRadioButton;
    CountDownTimer countDownTimer;
    CountDownTimer showAnswerTimer;
    private long timeLeftInMillis;
    private long showAnsTimeLeft;
    private boolean answered;
    private List<String> possibleAns;
    private Handler handler;
    static Context context;
    private MediaPlayer song;
    private int randomRow = 1, count = 0, pause;
    public static final String EXTRA_SCORE3 = "songNameExtraScore";
    int ansnum = 0;
    String ans = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_name);
        //  textViewSingerNameScore = findViewById(R.id.coins2);
        buttonSongNameScore =(Button) findViewById(R.id.scoreImg);
        timerTextView =(TextView) findViewById(R.id.text_view_timer);
        // progressBar = findViewById(R.id.progressBar);
        answ =(TextView) findViewById(R.id.ans);
        rbGroup =(RadioGroup) findViewById(R.id.radiogroupp);
        rb1 =(RadioButton) findViewById(R.id.option1);
        rb2 =(RadioButton) findViewById(R.id.option2);
        rb3 =(RadioButton) findViewById(R.id.option3);
        rb4 =(RadioButton) findViewById(R.id.option4);
        // confirm = findViewById(R.id.confirm);
        // progressBar = findViewById(R.id.progressBar);
        textColorRadioButton = rb1.getTextColors();
        textColorTimer = timerTextView.getTextColors();
        handler = new Handler();
        context = this;
        help=(ImageButton) findViewById(R.id.help);
        possibleAns = new ArrayList<>();
        possibleAns.add("مرغ سحر");
        possibleAns.add("اون تو نیستی");
        possibleAns.add("ساغی");
        possibleAns.add("غوغای ستارگان");
        possibleAns.add("کوچ بنفشه ها");
        possibleAns.add("سکوت");
        possibleAns.add("خستم");
        possibleAns.add("احساس آرامش");
        possibleAns.add("دامن کشان");

        startGame();
    }


    private void startGame() {
        game_db_helper gameDbHelper = new game_db_helper(this);
        generateRandomId();
        buttonSongNameScore.setText(songNameScore + "");
        gameDbHelper.giveTheSong(randomRow);
        ans = gameDbHelper.giveTheSongName(randomRow);
        showNextQuestion();
        ansnum = generateRandomAnsNum();
        playTheMusic();
        makeOptions();
        answered = false;
        song.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //check if it's the first time that song is being played
                //count++;
                //if (count==1) {

                //show and enable buttons after music is played
//                play.setEnabled(true);
//                stop.setEnabled(true);
//                replay.setEnabled(true);
//                play.setVisibility(View.VISIBLE);
//                stop.setVisibility(View.VISIBLE);
//                replay.setVisibility(View.VISIBLE);
            }
        });

    }

    private void generateRandomId() {
        Random randomId = new Random();
        randomRow = randomId.nextInt(59) + 1;
    }

    private int generateRandomAnsNum() {
        Random randomId = new Random();
        return randomId.nextInt(4);
    }

    public void playAgain() {
        //reset everything

        rb1.setBackgroundResource(R.drawable.orange_rounded_corner);
        rb2.setBackgroundResource(R.drawable.orange_rounded_corner);
        rb3.setBackgroundResource(R.drawable.orange_rounded_corner);
        rb4.setBackgroundResource(R.drawable.orange_rounded_corner);

        timeLeftInMillis = TIMER_IN_MILLIS;
        count = 0;
//        play.setEnabled(false);
//        stop.setEnabled(false);
//        confirm.setEnabled(true);
//        confirm.setVisibility(View.VISIBLE);
//        replay.setEnabled(false);
//        play.setVisibility(View.GONE);
//        stop.setVisibility(View.GONE);
//        replay.setVisibility(View.GONE);
        Stop();
        startGame();

    }

    private void playTheMusic() {
        File file = null;
        FileOutputStream fos;
        game_db_helper givebytesounds = new game_db_helper(this);
        try {
            file = File.createTempFile("sound", "sound");
            fos = new FileOutputStream(file);
            fos.write(givebytesounds.giveTheSong(randomRow));
            fos.close();
            Log.d("File", file.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        song = MediaPlayer.create(this, Uri.fromFile(file));
        song.start();


    }

    private void checkAnswer() {
        answered = true;
        countDownTimer.cancel();
        RadioButton rbSelected =(RadioButton) findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNum = rbGroup.indexOfChild(rbSelected);
        if (answerNum == ansnum) {
            songNameScore++;
            // textViewSingerNameScore.setText(songNameScore + "");
            buttonSongNameScore.setText(songNameScore +"");
            sendScoreToMenu();
            Toast.makeText(this, "باریکلا !", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "واه واه :))", Toast.LENGTH_SHORT).show();
        }
        showRightAnswer();
        sendScoreToMenu();
    }


    private void makeOptions() {
        switch (ansnum) {
            case 0:
                rb1.setText("    "+ans+"    ");
                break;
            case 1:
                rb2.setText("    "+ans+"    ");
                break;
            case 2:
                rb3.setText("    "+ans+"    ");
                break;
            case 3:
                rb4.setText("    "+ans+"    ");
                break;
        }
        Collections.shuffle(possibleAns);
        for (int i = 0; i < 4; i++) {
            if (i != ansnum) {
                switch (i) {
                    case 0:
                        if (possibleAns.get(0).equals(ans)) rb1.setText("    "+possibleAns.get(4)+"    ");
                        else rb1.setText("    "+possibleAns.get(0)+"    ");
                        break;
                    case 1:
                        if (possibleAns.get(1).equals(ans)) rb2.setText("    "+possibleAns.get(5)+"    ");
                        else rb2.setText("    "+possibleAns.get(1)+"    ");
                        break;
                    case 2:
                        if (possibleAns.get(2).equals(ans)) rb3.setText("    "+possibleAns.get(6)+"    ");
                        else rb3.setText("    "+possibleAns.get(2)+"    ");
                        break;
                    case 3:
                        if (possibleAns.get(3).equals(ans)) rb4.setText("    "+possibleAns.get(7)+"    ");
                        else rb4.setText("    "+possibleAns.get(3)+"    ");
                        break;
                }
            }
        }
    }

    private void showNextQuestion() {
        rb1.setTextColor(textColorRadioButton);
        rb2.setTextColor(textColorRadioButton);
        rb3.setTextColor(textColorRadioButton);
        rb4.setTextColor(textColorRadioButton);
        rbGroup.clearCheck();
        answ.setText("");
        answered = false;
//        confirm.setText("Confirm");
        timeLeftInMillis = TIMER_IN_MILLIS;
        showAnsTimeLeft = TIME_TO_SHOW_ANSWER;
        startCountDown();
        questionCounter++;
        //  startProgressBarCd();
    }

    /////////////////
    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMillis = l;
                updateTimerText()
                ;
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateTimerText();
                showRightAnswer();

            }
        }.start();
    }

    /////////////////
    private void updateTimerText() {
        int min = (int) timeLeftInMillis / 1000 / 60;
        int sec = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormat = String.format(Locale.getDefault(), "%2d : %2d", min, sec);
        timerTextView.setText(timeFormat);
        if (timeLeftInMillis <= 6000) {
            timerTextView.setTextColor(Color.RED);
        } else {
            timerTextView.setTextColor(textColorTimer);
        }
    }

    private void showRightAnswer() {

//        confirm.setEnabled(false);
//        confirm.setVisibility(View.GONE);
        showAnswerTimer = new CountDownTimer(showAnsTimeLeft, 1000) {
            @Override
            public void onTick(long l) {
                showAnsTimeLeft = l;
                countDownTimer.cancel();
                if(rb1.isChecked()){
                    rb1.setBackgroundResource(R.drawable.wrong_ans);
                }else if(rb2.isChecked()){
                    rb2.setBackgroundResource(R.drawable.wrong_ans);
                }else if(rb3.isChecked()){
                    rb3.setBackgroundResource(R.drawable.wrong_ans);
                }else if(rb4.isChecked()){
                    rb4.setBackgroundResource(R.drawable.wrong_ans);

                }

                switch (ansnum) {
                    case 0:
                        rb1.setBackgroundResource(R.drawable.correct_ans);

                        // rb1.setTextColor(Color.GREEN);
                        //answ.setText("1");
                        break;
                    case 1:
                        //rb2.setTextColor(Color.GREEN);
                        rb2.setBackgroundResource(R.drawable.correct_ans);

                        //answ.setText("2");
                        break;
                    case 2:
                        // rb3.setTextColor(Color.GREEN);
                        rb3.setBackgroundResource(R.drawable.correct_ans);

                        //answ.setText("3");
                        break;
                    case 3:
                        // rb4.setTextColor(Color.GREEN);
                        rb4.setBackgroundResource(R.drawable.correct_ans);

                        //answ.setText("4");
                        break;
                }
            }

            @Override
            public void onFinish() {

                showAnsTimeLeft = 0;
                playAgain();
            }
        }.start();


    }

    private void sendScoreToMenu() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE3, songNameScore);
        setResult(RESULT_OK, resultIntent);
    }

    public void Play(View view) {
        if (!song.isPlaying()) {
            song.seekTo(pause);
            song.start();
        }
    }

    public void Reset(View view) {
        song.seekTo(0);
        song.start();
    }

    public void Pause(View view) {
        if (song != null) {
            song.pause();
            pause = song.getCurrentPosition();
        }
    }

    public void Stop() {
        if (song != null) {
            song.stop();
            song.reset();
            song.release();
            song = null;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        song.release();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void confirmtest(View view) {
        if (!answered) {
            if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                checkAnswer();
            } else {
                Toast.makeText(SongNameActivity.this, "هنوز انتخاب نکردی !", Toast.LENGTH_SHORT).show();
            }
        } else {
            playAgain();
        }
    }
    public void helpMe(View view){
        PopupMenu popup = new PopupMenu(SongNameActivity.this,help);
        popup.getMenuInflater().inflate(R.menu.help_menu2, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.one:
                        if(songNameScore >=10){
                            for (int i = 0; i <1 ; i++) {
                                switch(i){
                                    case 0:  if(i!=ansnum)rb4.setTextColor(Color.RED);else rb3.setTextColor(Color.RED);break;
                                }
                            }
                            songNameScore -=10;
                        }
                        else Toast.makeText(SongNameActivity.this, "not enough coins", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.two:
                        if(songNameScore >=20){
                            for (int i = 0; i <2 ; i++) {
                                switch(i){
                                    case 0:  if(i!=ansnum)rb1.setTextColor(Color.RED);else rb4.setTextColor(Color.RED);break;
                                    case 1:  if(i!=ansnum)rb2.setTextColor(Color.RED);else rb4.setTextColor(Color.RED);break;
                                }
                            }
                            songNameScore -=20;
                        }
                        else Toast.makeText(SongNameActivity.this, "not enough coins", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.three:
                        if(songNameScore >=30){
                            for (int i = 0; i <4 ; i++) {
                                if(i!=ansnum){
                                    switch(i){
                                        case 0:  rb1.setTextColor(Color.RED);break;
                                        case 1:  rb2.setTextColor(Color.RED);break;
                                        case 2:  rb3.setTextColor(Color.RED);break;
                                        case 3:  rb4.setTextColor(Color.RED);break;
                                    }
                                }
                            }
                            songNameScore -=30;
                        }
                        else Toast.makeText(SongNameActivity.this, "not enough coins", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });

        popup.show();
    }

    public void checkedColour(View view) {
        if(rb1.isChecked())
        {
            rb1.setBackgroundResource(R.drawable.checked);
        }else{
            rb1.setBackgroundResource(R.drawable.orange_rounded_corner);
        }
        if(rb2.isChecked())
        {
            rb2.setBackgroundResource(R.drawable.checked);
        }else{
            rb2.setBackgroundResource(R.drawable.orange_rounded_corner);
        }
        if(rb3.isChecked())
        {
            rb3.setBackgroundResource(R.drawable.checked);
        }else{
            rb3.setBackgroundResource(R.drawable.orange_rounded_corner);
        }
        if(rb4.isChecked())
        {
            rb4.setBackgroundResource(R.drawable.checked);
        }else{
            rb4.setBackgroundResource(R.drawable.orange_rounded_corner);
        }
    }
}


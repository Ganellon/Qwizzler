/*
 * Qwizzler
 * QuizActivity.java
 * Created by Andrew Epstein
 * Copyright (c) 2018. All rights reserved.
 * Last modified : 2/15/18 5:08 PM
 */

package cloud.workingtitle.qwizzler;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

  // index of the question currently being displayed
  private int mCurrentQuestion = 0;

  // library of questions to ask
  private ArrayList<Question> quiz = new ArrayList<>();

  // provides swipe support for forward / backward in the question list
  private GestureDetectorCompat mDetector;

  // used for converting pixels to density independent pixels (dp)
  private float scale;

  // visual controls
  // mScrollView is the main "stage" where all question choices will appear
  private ScrollView mScrollView;

  // mQuestionText is the TextView where the Question itself will appear
  private TextView mQuestionText;

  private Button hint_button;
  private Button check_answer_button;
  private Button previous_button;
  private Button next_button;
  private Button get_score_button;


  /**
   * This method is called during the app lifecycle (e.g. screen rotations)
   * it adds the currentQuestion and quiz globals to the bundle.
   * The parcelable array comes from the Parcelable interface of the Question class
   *
   * @param savedInstanceState
   */
  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    super.onSaveInstanceState(savedInstanceState);
    savedInstanceState.putInt("CurrentQuestion", mCurrentQuestion);
    savedInstanceState.putParcelableArrayList("quiz", quiz);
  }


  /**
   * This is the Activity's entry point. It's called at startup and during the app lifecycle.
   * Initialization methods are called from here, including wiring the visual elements to listeners
   *
   * @param savedInstanceState
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quiz);

    // initialize the mDectector global to an instance of itself
    mDetector = new GestureDetectorCompat(this, new CustomGestureListener());

    // create global references to static on-screen elements (buttons, display box, etc.)
    loadControls();

    // if there is data in savedInstanceState, unbundle it and apply it to the appropriate globals
    if (savedInstanceState != null) {
      mCurrentQuestion = savedInstanceState.getInt("CurrentQuestion", 0);
      quiz = savedInstanceState.getParcelableArrayList("quiz");
    }
    else createQuestions(); // otherwise, create a new batch of questions from code

    // units in dynamic app are in pixels. This changes the scale depending on the device.
    scale = getResources().getDisplayMetrics().density;

    // initiate the creation cycle of any screen elements
    updateDisplay();

    // wire the Previous button onCLick event to a method
    previous_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // get the previous Question from the storehouse
        mCurrentQuestion = previousQuestion();
        // display it
        updateDisplay();
      }
    });

    // wire the Next button onClick event to a method
    next_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // get the next question from the quiz storehouse
        mCurrentQuestion = nextQuestion();
        // display it
        updateDisplay();
      }
    });

    // wire the Check Answer button onClick event to a method
    check_answer_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // initiate the check answer method
        checkAnswer();
        // update the display after the answer has been evaluated
        updateDisplay();
      }
    });

    get_score_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getScore();
      }
    });

    // Not sure if I like this... it's kind of obnoxious
    //if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
    //  showToast("<<  SWIPE LEFT OR RIGHT FOR MORE QUESTIONS  >>");
    //}
  }

  private void getScore() {
    int attempted = 0;
    double correct = 0.0;
    for (Question question : quiz) {
      if (question.isAnswered()) {
        ++attempted;
        if (question.getAnsweredCorrectly()) {
          ++correct;
        }
      }
    }
    if (attempted != 0) {
      showToast("You attempted " + attempted + " questions and got " + (int) correct + " correct!  That's " +
          String.valueOf(Math.round(100 * (correct / attempted))) + "%");
    }
    else {
      showToast("You haven't answered any questions!");
    }
  }

  private void checkAnswer() {
    Question question = quiz.get(mCurrentQuestion);
    int checkedQuantity = 0;

    ArrayList<Question.Choice> choices = quiz.get(mCurrentQuestion).getChoices();

    //Toast.makeText(this, "We are here", Toast.LENGTH_SHORT).show();
    ViewGroup parent = (ViewGroup) mScrollView.getChildAt(0);
    //Toast.makeText(this, String.valueOf(parent.getChildCount()), Toast.LENGTH_SHORT).show();
    for (int i = 0; i < parent.getChildCount(); i++) {
      TextView control = (TextView) parent.getChildAt(i);
      // is this a short answer type?
      if (control instanceof EditText) {
        // this is a text box - get the text and compare
        String providedAnswer = control.getText().toString();
        if (providedAnswer.isEmpty()) {
          showToast("You did not provide an answer");
          return;
        }
        String correctAnswer = choices.get(i).getText();
        ((ShortAnswer)question).setAnsweredCorrectly(correctAnswer.equalsIgnoreCase(providedAnswer), providedAnswer);
        if (question.getAnsweredCorrectly()) {
          showToast("Yay!");
        }
        else
        {
          showToast("Nope");
        }
        control.setEnabled(false);
        return;
      }
      if (((CompoundButton) control).isChecked()) { // the control was checked...
        ++checkedQuantity;
        choices.get(i).choose(); // save this choice for later
        if (!choices.get(i).isCorrect()) { // ... but it was a wrong choice
          question.setAnsweredCorrectly(false); // mark the question wrong
        }
      } else if (!((CompoundButton) control).isChecked() && checkedQuantity > 0) { // the control is not checked...
        if (choices.get(i).isCorrect()) { // ... but it should have been checked
          question.setAnsweredCorrectly(false); // mark question wrong
        }
      }
    }
    // all controls have been evaluated, let's see
    if (checkedQuantity == 0) { // question was not answered at all
      showToast("Please select an answer");
    }
    // the question was answered correctly
    else if (!question.isAnswered()) {
      question.setAnsweredCorrectly(true);
      showToast("Yay!");
    }
    else showToast("Nope");
  }

  private void showToast(String message) {
    Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
    toast.setGravity(Gravity.CENTER, 0, 0);
    toast.show();
  }

  private void loadControls() {
    mScrollView = findViewById(R.id.scroll_view_area);
    mQuestionText = findViewById(R.id.question);
    hint_button = findViewById(R.id.hint_button);
    check_answer_button = findViewById(R.id.check_answer_button);
    previous_button = findViewById(R.id.previous_button);
    next_button = findViewById(R.id.next_button);
    get_score_button = findViewById(R.id.get_score_button);
  }

  private void updateControls() {
    previous_button.setEnabled(mCurrentQuestion != 0);
    next_button.setEnabled(mCurrentQuestion < quiz.size() - 1);
    if (quiz.size() > 0) {
      boolean isAnswered = quiz.get(mCurrentQuestion).isAnswered();
      check_answer_button.setEnabled(!isAnswered);
      hint_button.setEnabled(!isAnswered);
    }
  }

  private void updateDisplay() {
    String appName = getString(R.string.app_name);
    Question q = null;
    if (quiz.size() > 0) {
      q = quiz.get(mCurrentQuestion);
      setTitle(String.format(Locale.US, appName + " -- Question %d of %d", mCurrentQuestion + 1, quiz.size()));
    }
    askQuestions(q);
    updateControls();

  }

  // this is a fail-safe method to cope with swiping
  private int previousQuestion() {
    return mCurrentQuestion > 0 ? --mCurrentQuestion : mCurrentQuestion;
  }
  // this is a fail-safe method to cope with swiping
  private int nextQuestion() {
    return mCurrentQuestion < quiz.size() - 1 ? ++mCurrentQuestion : mCurrentQuestion;
  }

  private void askQuestions(Question q) {
    if (q == null) return;
    try {
      displayQuestion(q);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    }
  }

  private void createQuestions() {
    Question question = new TrueFalse("Trenton is the capital of New Jersey.");
    quiz.add(question);
    question = new TrueFalse("New York City is the capital of New York State", false);
    quiz.add(question);
    question = new ShortAnswer("What is the name of this app?");
    question.addChoice("Qwizzler");
    quiz.add(question);
    question = new SelectOne("What is the capital of California?");
    question.addChoice("Las Angeles");
    question.addChoice("San Diego");
    question.addChoice("San Francisco");
    question.addChoice("Santa Cruz");
    question.addChoice("Santa Barbara");
    question.addChoice("Venice Beach");
    question.addChoice("Sacramento", true);
    quiz.add(question);
    question = new SelectMany("Which months in the year have 30 days?");
    question.addChoice("January");
    question.addChoice("February");
    question.addChoice("March");
    question.addChoice("April", true);
    question.addChoice("May");
    question.addChoice("June", true);
    question.addChoice("July");
    question.addChoice("August");
    question.addChoice("September", true);
    question.addChoice("October");
    question.addChoice("November", true);
    question.addChoice("December");
    quiz.add(question);
  }

  // converts a DP setting into the nearest pixel equivalent for each device
  private int getDP(int desiredDP) {
    return (int) (desiredDP * scale + 0.5f);
  }

  @NonNull
  private ViewGroup createParent(Question question) {
    // The parent View will always be some type of Linear Layout; set that as the default to avoid
    // potentially returning a null value
    ViewGroup viewGroup = new LinearLayout(this);
    try {
      Class<?> parentClass = question.getParentType();
      Constructor<?> cons = parentClass.getConstructor(Context.class);
      viewGroup = (ViewGroup)cons.newInstance(this);
      viewGroup.setLayoutParams(getMatchParentLayoutParams());
      ((LinearLayout)viewGroup).setOrientation(LinearLayout.VERTICAL);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return viewGroup;
  }

  @NonNull
  private TextView createChild(Question question) {
    TextView textView = new TextView(this);
    try {
      Class<?> childClass = question.getControlType();
      Constructor<?> cons = childClass.getConstructor(Context.class);
      textView = (TextView)cons.newInstance(this);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return textView;
  }

  // Display the question and all of the answer choices on the screen
  private void displayQuestion(Question question)
      throws NoSuchMethodException,
      IllegalAccessException,
      InvocationTargetException,
      InstantiationException {
    // get the parent view group that we will add controls to
    ViewGroup viewGroup = createParent(question);
    // set any decorations on the TextView that displays the Question text
    setQuestionBox(question);

    // loop through all the choices for this question
    for (Question.Choice choice : question.getChoices()) {
      // create a control of the appropriate type; RadioButton, CheckBox and EditText are all TextView types
      TextView control = createChild(question);
      // set the text; for Short Answer questions, this will be the empty string
      if (control instanceof EditText) {
        // apply formatting and decorations to EditText
        control = setEditText(control, question);
      }
      else {
        // apply formatting and decoration to CheckBox / RadioButton
        control = setCompoundButton(control, question, choice);
      }
      // add control to the parent view group
      viewGroup.addView(control);
      // disable the control if question was already answered
      control.setEnabled(!question.isAnswered());
    }
    // remove any previous Views from the scroll view
    mScrollView.removeAllViews();
    // add newly composited viewGroup to the ScrollView
    mScrollView.addView(viewGroup);
    // move the scroll bar back to the top
    mScrollView.setScrollY(0);
  }

  private TextView setCompoundButton(TextView control, Question question, Question.Choice choice) {
    control.setText(choice.getText());
    // set the margins and padding for the control
    control.setLayoutParams(getMargins());
    control.setPadding(getDP(16), 0, 0, 0);
    ((CompoundButton)control).setChecked(choice.wasPreviouslyChosen());
    // decorate the control
    if (question.isAnswered()) {
      if (choice.wasPreviouslyChosen() && choice.isCorrect()) {
        control.setBackgroundResource(R.drawable.green_box);
      }
      else if (!choice.wasPreviouslyChosen() && choice.isCorrect()) {
        control.setBackgroundResource(R.drawable.green_box);
      }
      else if (choice.wasPreviouslyChosen() && !choice.isCorrect()) {
        control.setBackgroundResource(R.drawable.red_box);
      }
    }
    return control;
  }

  private TextView setEditText(TextView control, Question question) {
    control.setHint("Type Answer Here");
    if (question.isAnswered()) {
      control.setText(((ShortAnswer)question).getProvidedAnswer());
      if (question.getAnsweredCorrectly()) {
        control.setBackgroundResource(R.drawable.green_box);
      } else {
        control.setBackgroundResource(R.drawable.red_box);
      }
    }
    return control;
  }

  @NonNull
  private LinearLayout.LayoutParams getMatchParentLayoutParams() {
    return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
  }

  @NonNull
  private LinearLayout.LayoutParams getMargins() {
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.setMargins(0, getDP(4), 0, 0);
    return layoutParams;
  }

  private void setQuestionBox(Question question) {
    // shuffle the order of the choices so they appear in random order each time they are newly displayed
    if (!question.isAnswered()) {
      mQuestionText.setBackgroundResource(0);
      Collections.shuffle(question.getChoices());
    }
    else {
      if (question.getAnsweredCorrectly()) {
        mQuestionText.setBackgroundResource(R.drawable.green_box);
      }
      else mQuestionText.setBackgroundResource(R.drawable.red_box);
    }
    // set the main question text
    mQuestionText.setText(question.getQuestionText());
  }

  @Override
  public boolean onTouchEvent(MotionEvent event){
    this.mDetector.onTouchEvent(event);
    return super.onTouchEvent(event);
  }

  class CustomGestureListener extends GestureDetector.SimpleOnGestureListener {

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
      if (event1.getX() < event2.getX() - 100) previous_button.callOnClick();
      else next_button.callOnClick();
      //Log.d("Swipe", swipeDirection);
      //Log.d("Swipe", "Event1 X " + String.valueOf(event1.getX()) + " | Event2 X " + String.valueOf(event2.getX()));
      //Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
      return true;
    }
  }
}

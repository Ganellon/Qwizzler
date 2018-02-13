package cloud.workingtitle.dynamicquiz;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

  // index of the question currently being displayed
  private int mCurrentQuestion = 0;

  // library of questions to ask
  private ArrayList<Question> quiz = new ArrayList<>();


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


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quiz);
    scale = getResources().getDisplayMetrics().density;
    loadControls();

    previous_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mCurrentQuestion = previousQuestion();
        updateDisplay();
      }
    });

    next_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //recordChoices(mCurrentQuestion);
        mCurrentQuestion = nextQuestion();
        updateDisplay();
      }
    });

    check_answer_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        checkAnswer();
        updateDisplay();
      }
    });

    get_score_button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getScore();
      }
    });
    createQuestions();
    updateDisplay();
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
    Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
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
    Question q = null;
    if (quiz.size() > 0) {
      q = quiz.get(mCurrentQuestion);
      setTitle(String.format(Locale.US, "Question %d of %d", mCurrentQuestion + 1, quiz.size()));
    }
    askQuestions(q);
    updateControls();

  }

  private int previousQuestion() {
    // really, this should never happen since the button should be disabled
    return mCurrentQuestion > 0 ? --mCurrentQuestion : mCurrentQuestion;
  }

  private int nextQuestion() {
    // really, this should never happen since the button should be disabled
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
    Question question = new TrueFalse("Andrew bought something bad from Amazon");
    quiz.add(question);
    question = new ShortAnswer("What did Andrew order from Amazon");
    question.addChoice("Something good");
    quiz.add(question);
  }

  // converts a DP setting into the nearest pixel equivalent for each device
  private int getDP(int desiredDP) {
    return (int) (desiredDP * scale + 0.5f);
  }

  // Display the question and all of the answer choices on the screen
  private void displayQuestion(Question question)
      throws NoSuchMethodException,
      IllegalAccessException,
      InvocationTargetException,
      InstantiationException {
    // get the parent view group that we will add controls to
    ViewGroup viewGroup = (ViewGroup) question.getParentType().getConstructor(Context.class).newInstance(this);
    // get and set the layout params for the parent view group
    viewGroup.setLayoutParams(getMatchParentLayoutParams());
    // set the layout to Vertical
    ((LinearLayout) viewGroup).setOrientation(LinearLayout.VERTICAL);

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

    // loop through all the choices in the array
    for (Question.Choice choice : question.getChoices()) {
      // create a control of the appropriate type; RadioButton, CheckBox and EditText are all TextView types
      TextView control = (TextView) question.getControlType().getConstructor(Context.class).newInstance(this);
      // set the text; for Short Answer questions, this will be the empty string
      if (control instanceof EditText) {
        control.setHint("Answer Here");
        if (question.isAnswered()) {
          control.setText(((ShortAnswer) question).getProvidedAnswer());
          if (question.getAnsweredCorrectly()) {
            control.setBackgroundResource(R.drawable.green_box);
          } else {
            control.setBackgroundResource(R.drawable.red_box);
          }
        }
        // ((TextInputLayout)viewGroup).setHint("Answer Here"); Material compatibility issue
      } else {
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
      }
      // add it to the parent view group
      viewGroup.addView(control);
      control.setEnabled(!question.isAnswered());
      //control.setBackgroundResource(R.drawable.green_box);
    }
    // set the main question text
    mQuestionText.setText(question.getQuestionText());
    // remove any previous Views from the scroll view
    mScrollView.removeAllViews();
    // add newly composited viewGroup to the ScrollView
    mScrollView.addView(viewGroup);
    // move the scroll bar back to the top
    mScrollView.setScrollY(0);
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

}

package cloud.workingtitle.qwizzler;

import java.util.ArrayList;


/**
 * Created by drew on 2/11/18.
 */

abstract class Question {

  // Question can have several states, the least of which is basic question text
  // Question has at least one choice
  // Question has at least one correct choice
  // For SelectOne and

  // these decide what kind of controls to draw
  private Class mControlType;
  private Class mParentType;

  // the text of the question, e.g. "What is the capital of Iowa?"
  private String mQuestionText;

  // the list of possible answers to the question
  private ArrayList<Choice> mChoices = new ArrayList<>();

  private boolean mIsAnswered;
  private boolean mAnsweredCorrectly;


  Question(String questionText) {
    mQuestionText = questionText;
    mIsAnswered = false;
  }

  public void setAnsweredCorrectly(boolean answeredCorrectly) {
    mAnsweredCorrectly = answeredCorrectly;
    mIsAnswered = true;
  }

  public boolean getAnsweredCorrectly() {
    return mAnsweredCorrectly;
  }

  public boolean isAnswered() {
    return mIsAnswered;
  }

  public Class getControlType() {
    return this.mControlType;
  }

  public Class getParentType() {
    return this.mParentType;
  }

  public void setControlType(Class controlType) {
    mControlType = controlType;
  }

  public void setParentType(Class parentType) {
    mParentType = parentType;
  }

  public String getQuestionText() {
    return mQuestionText;
  }

  public void addChoice(String choiceText) {
    this.addChoice(choiceText, false);
  }

  public void addChoice(String choiceText, boolean isCorrect) {
    Choice choice = new Choice(choiceText, isCorrect);
    mChoices.add(choice);
  }

  public ArrayList<Choice> getChoices() {
    return mChoices;
  }

  public class Choice {
    private String mChoiceText;
    private boolean mIsCorrect;
    private boolean mWasChosen;

    private Choice(String choiceText, boolean isCorrect) {
      this.mChoiceText = choiceText;
      this.mIsCorrect = isCorrect;
    }

    public void choose() {
      mWasChosen = true;
    }

    public boolean wasPreviouslyChosen() {
      return mWasChosen;
    }

    public String getText() {
      return mChoiceText;
    }

    public boolean isCorrect() {
      return mIsCorrect;
    }
  }
}
